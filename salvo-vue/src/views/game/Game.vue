<template>
    <div id='game-container' class='view'>
        <Nav :user='this.user' />
        <Spacer />
        <LogManager :data='data' :logs='logs' />
        <router-view :gp='gp' :turn='turn' :data='data' />
        <Spacer />
    </div>
</template>

<script>
import Nav from '@/components/nav/Nav.vue';
import Spacer from '@/components/Spacer.vue';
import LogManager from '@/components/game/log/LogManager.vue';
import { bus } from '@/main.js';
import { mapState, mapActions } from 'vuex';

export default {
    name: 'Game',
    data: function() {
        return {
            gp: 0,
            data: null,
            logs: [],
            turn: -1,
            interval: null,
            time: 2500
        };
    },
    components: {
        Nav,
        Spacer,
        LogManager
    },
    computed: {
        ...mapState(['user'])
    },
    methods: {
        ...mapActions(['getMatchData', 'getTurnData']),
        setMatchData: function(data) {
            this.data = data;
            this.logs = data.logs;
            this.turn = data.turn;
            let path = '/menu';

            switch (data.state) {
                case 'WAITING':
                    if (data.gamePlayers.filter(g => g.id == this.gp)[0].state == 'WAITING_PLAYER') {
                        path = '/game/' + this.gp + '/view';
                    } else {
                        path = '/game/' + this.gp + '/ships';
                    }
                    break;
                case 'PLAYING':
                    path = '/game/' + this.gp + '/view';
                    break;
                case 'FINISHED':
                    path = '/game/' + this.gp + '/view';
                    this.triggerPopUp(data.gamePlayers.filter(g => g.id == this.gp)[0].state);
                    break;
            }
            
            this.$router.push({ path: path });
            
            clearInterval(this.interval);
            this.interval = setInterval(this.triggerUpdateMatchData, this.time);
        },
        updateMatchData: function(data) {     
            //TURN       
            let oldTurn = this.turn;
            this.turn = data.turn;
            
            //GAMEPLAYERS
            let playerUpdate = data.gamePlayers.filter(x => x.id == this.gp)[0];
            let player = this.data.gamePlayers.filter(x => x.id == this.gp)[0];
            let opponent = this.data.gamePlayers.filter(x => x.id != this.gp)[0];
            let opponentUpdate = data.gamePlayers.filter(x => x.id != this.gp)[0];

            player = this.updatePlayer(playerUpdate, player, false, oldTurn);
            opponent = this.updatePlayer(opponentUpdate, opponent, true, oldTurn);
            
            let gps = [];
            gps.push(player);
            gps.push(opponent);
            gps = gps.filter(x => isValid(x));
            
            this.data = Object.assign({}, this.data, { turn: data.turn, state: data.state, gamePlayers: gps });

            //LOGS
            this.updateLogs(data.logs, oldTurn);
            
            clearInterval(this.interval);
            if (data.state == 'FINISHED') {
                this.triggerPopUp(playerUpdate.state);
            } else {
                this.interval = setInterval(this.triggerUpdateMatchData, this.time);
            }
        },
        updatePlayer: function(n, o, isOpponent, oldTurn) {
            if (!isOpponent && !isValid(o) || !isValid(n)) {
                return null;
            } else if (isOpponent && !isValid(o)) {
                return n;
            }

            o.state = n.state;

            if (oldTurn == this.turn && !o.state.includes('FINISHED')) {
                return o;
            }

            //SHIPS
            o.ships.activeShips = n.ships.activeShips;

            if (!isOpponent && n.ships.sunkShips.length > 0) {
                let ships = o.ships.all;
                let nShips = n.ships.sunkShips;

                if (ships.length > 0 && !ships.some(x => x.sunkInTurn == nShips[0].sunkInTurn)) {
                    let temp;

                    for (let i = ships.length - 1; i >= 0; i--) {
                        temp = nShips.filter(x => x.type == ships[i].type)[0];

                        if (isValid(temp)) {
                            ships[i] = temp;
                        }
                    }
                    
                    o.ships.all = ships;
                }
                
            } else if (isOpponent && n.ships.sunkShips.length > 0) {
                let ships = o.ships.sunkShips;
                let nShips = n.ships.sunkShips;

                if (ships.length == 0 && nShips.length > 0) {
                    o.ships.sunkShips = nShips;
                } else if (ships.length > 0 && !ships.some(x => x.sunkInTurn == nShips[0].sunkInTurn)) {
                    o.ships.sunkShips = ships.concat(nShips);
                }
            }

            //SALVOES
            if (n.salvoes.length > 0 && isValid(o.salvoes) && !o.salvoes.some(x => x.turn == n.salvoes[0].turn)) {
                o.salvoes = o.salvoes.concat(n.salvoes);
            }

            return o;
        },
        updateLogs: function(logs, oldTurn) {
            let isIncluded = false;
            
            if (!isValid(logs) || logs.length == 0) {
                return false;
            } 
            else if (logs[0].turn == 0) {
                logs.forEach(log => {
                    isIncluded = this.logs.some(x => {
                        return x.turn == log.turn && x.gamePlayerId == log.gamePlayerId && x.message == log.message;
                    });

                    if (!isIncluded) {
                        this.logs.push(log);
                    }
                });
            } else {
                logs.forEach(log => {
                    isIncluded = this.logs.some(x => {
                        return x.turn == log.turn && x.gamePlayerId == log.gamePlayerId && x.message == log.message;
                    });

                    if (!isIncluded && oldTurn != this.turn) {
                        this.logs.push(log);
                    }
                });
            }
        },
        triggerPopUp: function(state) {
            let title = '';
            let details = '';
            
            switch (state) {
                case 'FINISHED_WON':
                    title = 'You Won';
                    details = 'Congratulations!';
                    break;
                case 'FINISHED_LOST':
                    title = 'You Lost';
                    details = 'Better luck next time!';
                    break;
                case 'FINISHED_TIED':
                    title = "IT'S A TIE!";
                    details = 'Well played!';
                    break;
            }

            bus.$emit('open-popUp', {
                title: title,
                details: details,
                direction: '/menu/matches',
                button: 'X'
            });
        },
        setFinalData: function(data) {
            this.data = data;
            this.logs = data.logs;
            this.turn = data.turn;
        },
        triggerUpdateMatchData: function() {
            this.getTurnData({ gp: this.gp, tn: this.turn, callback: this.updateMatchData });
        },
        triggerInstantRefresh: function() {
            clearInterval(this.interval);
            this.triggerUpdateMatchData();
        },
        triggerReloadMatchData: function(isFinal) {
            clearInterval(this.interval);

            let callback = (isFinal) ? this.setFinalData : this.setMatchData;
            this.getMatchData({ gp: this.gp, callback: callback });
        }
    },
    mounted: function() {
        this.gp = parseInt(this.$route.params.gp);
        this.getMatchData({ gp: this.gp, callback: this.setMatchData });
        
        bus.$on('trigger-instant-refresh', this.triggerInstantRefresh);
        bus.$on('trigger-data-reload', this.triggerReloadMatchData);
    },
    beforeDestroy: function() {
        clearInterval(this.interval);
        
        bus.$off('trigger-instant-refresh', this.triggerInstantRefresh);
        bus.$off('trigger-data-reload', this.triggerReloadMatchData);
    }
}
</script>

<style>
@media (orientation: landscape) {
  #game-container {
    overflow: scroll;
  }
}

@media (orientation: portrait) {
  #game-container {
    overflow: hidden;
  }
}

#container-view .help {
    position: absolute;
    top: calc(var(--spacer-height) + 42.5vw);
    right: var(--margin);
}

#container-editor {
    overflow: visible;
}

.container-wrap {
    width: 100vw;
    margin-top: 0;
}

.wrap-content {
    min-width: 40vw;
    margin: var(--margin);
    margin-bottom: 0;
    display: flexbox;
}

.panel {
    box-sizing: border-box;
    width: 50vw;
    height: 45vw;
    padding: calc(var(--padding) / 2);
    border-width: var(--border-width);
    border-color: var(--color-00);
    border-style: solid;
    border-radius: var(--border-radius);
    background-color: var(--color-04);
    text-align: justify;
}

.panel p, .panel * span {
    margin-top: var(--margin);
    font-size: calc(var(--text-size-03) * 0.75);
}

.grid {
    border-spacing: 0;
}

.grid * {
    overflow: visible;
}

#player-table, #opponent-table {
    margin: calc(var(--margin) * 2);
    padding: 0;
    overflow: visible;
}

#player-table th, #opponent-table th  {
    width: var(--cell-size);
    height: var(--cell-size);
    background-color: var(--color-00);
    color: var(--color-09);
    border-width: 0;
    margin: 0;
    padding: 0;
    border-color: var(--color-04);
    border-style: solid;
}

#player-table thead th, #opponent-table thead th {
    border-top-width: var(--border-width);
}

#player-table thead th:first-of-type, #opponent-table thead th:first-of-type {
    border-left-width: var(--border-width);
}

#player-table thead th:last-of-type, #opponent-table thead th:last-of-type {
    border-right-width: var(--border-width);
}

#player-table tbody th, #opponent-table tbody th {
    border-left-width: var(--border-width);
}

#player-table td, #opponent-table td {
    width: var(--cell-size);
    height: var(--cell-size);
    border-right-width: calc(var(--border-width) / 2);
    border-bottom-width: calc(var(--border-width) / 2);
    border-color: var(--color-00);
    border-style: solid;
}

#player-table tbody tr td:last-of-type, #opponent-table tbody tr td:last-of-type {
    border-right-width: var(--border-width);
    border-right-color: var(--color-04);
    border-style: solid;
}

#player-table thead :first-child, #opponent-table thead :first-child {
    border-top-left-radius: var(--border-radius);
}

#player-table thead :last-child, #opponent-table thead :last-child {
    border-top-right-radius: var(--border-radius);
}

#player-table tfoot td, #opponent-table tfoot td {
    width: calc(var(--cell-size) / 2);
    background-color: var(--color-05);
    border-width: 0;
    border-bottom-width: var(--border-width);
    border-color: var(--color-04);
    border-style: solid;
}

#player-table tfoot td:first-of-type, #opponent-table tfoot td:first-of-type {
    border-bottom-left-radius: var(--border-radius);
    border-left-width: var(--border-width);
}

#player-table tfoot td:last-child, #opponent-table tfoot td:last-child {
    border-bottom-right-radius: var(--border-radius);
    border-right-width: var(--border-width);
}

#player-gui-table .cell[isOccupied='true'] {
    background-color: var(--color-00);
}

#player-gui-table .cell[success='true'] {
    background-color: var(--color-05);
}

#player-gui-table .cell[success='false'] {
    background-color: var(--color-12);
}

#player-gui-table {
    width: 40vw;
    height: 40vw;
    overflow: visible;
}

#player-gui-table td {
    width: 4vw;
    height: 4vw;
    border-width: calc(var(--border-width) / 4);
    border-color: var(--color-00);
    border-style: solid;
    background-color: var(--color-08);
}

#player-gui-table thead th, #player-gui-table th, #player-gui-table tfoot td {
    width: 0;
    height: 0;
    visibility: hidden;
    display: none;
}

#opponent-table .cell[isOccupied='true'] {
    background-color: var(--color-02);
}

#opponent-table .cell[isSelected='true'] {
    background-color: var(--color-12);
}

#opponent-table .cell[success='true'] {
    background-color: var(--color-11);
}

#opponent-table .cell[success='false'] {
    background-color: var(--color-05);
}

.ship {
    position: relative;
    background-color: var(--color-01);
    z-index: var(--layer-foreground-ships);
}

.ship[size='2'][orientation='vertical'] {
    width: var(--cell-size);
    height: calc((var(--cell-size) * 2) + (var(--border-width)));
}

.ship[size='2'][orientation='horizontal'] {
    width: calc((var(--cell-size) * 2) + var(--border-width));
    height: var(--cell-size);
}

.ship[size='3'][orientation='vertical'] {
    width: var(--cell-size);
    height: calc((var(--cell-size) * 3) + var(--border-width));
}

.ship[size='3'][orientation='horizontal'] {
    width: calc((var(--cell-size) * 3) + var(--border-width));
    height: var(--cell-size);
}

.ship[size='4'][orientation='vertical'] {
    width: var(--cell-size);
    height: calc((var(--cell-size) * 4) + (var(--border-width) * 2));
}

.ship[size='4'][orientation='horizontal'] {
    width: calc((var(--cell-size) * 4) + (var(--border-width) * 2));
    height: var(--cell-size);
}

.ship[size='5'][orientation='vertical'] {
    width: var(--cell-size);
    height: calc((var(--cell-size) * 5) + (var(--border-width) * 2));
}

.ship[size='5'][orientation='horizontal'] {
    width: calc((var(--cell-size) * 5) + (var(--border-width) * 2));
    height: var(--cell-size);
}

.ship[isDown='true'] {
    background-color: var(--color-05);
}

.ship[isDown='false'] {
    background-color: var(--color-01);
}

.cell {
    position: relative;
    margin: 0;
    padding: 0;
    background-color: var(--color-09);
    border-width: 0;
    overflow: visible;
    text-align: center;
}

.cell .ship {
    position: absolute;
    top: 0;
    left: 0;
    z-index: var(--layer-foreground-ships);
}
</style>