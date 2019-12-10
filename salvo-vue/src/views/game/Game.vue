<template>
    <div class='view'>
        <Nav :user='this.user' />
        <Spacer />
        <router-view :gp='gp' :turn='turn' :data='data' />
        <LogManager :data='data' :logs='logs' />
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
            bus.$emit('open-popUp', {
                title: state,
                details: 'juejue',
                direction: '/menu/matches',
                button: 'K cool'
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
.ship {
    position: relative;
    background-color: gainsboro;
    z-index: var(--layer-foreground-ships);
}

.ship[size='2'][orientation='vertical'] {
    width: var(--cell-size);
    height: calc(2 * var(--cell-size));
    padding: 0.25em;
    padding-bottom: 0.3em;
    padding-top: 0.3em;
}

.ship[size='2'][orientation='horizontal'] {
    width: calc(2 * var(--cell-size));
    height: var(--cell-size);
    padding: 0.25em;
    padding-right: 0.3em;
    padding-left: 0.3em;
}

.ship[size='3'][orientation='vertical'] {
    width: var(--cell-size);
    height: calc(3 * var(--cell-size));
    padding: 0.25em;
    padding-bottom: 0.3em;
    padding-top: 0.3em;
}

.ship[size='3'][orientation='horizontal'] {
    width: calc(3 * var(--cell-size));
    height: var(--cell-size);
    padding: 0.25em;
    padding-right: 0.3em;
    padding-left: 0.3em;
}

.ship[size='4'][orientation='vertical'] {
    width: var(--cell-size);
    height: calc(4 * var(--cell-size));
    padding: 0.25em;
    padding-bottom: 0.3em;
    padding-top: 0.3em;
}

.ship[size='4'][orientation='horizontal'] {
    width: calc(4 * var(--cell-size));
    height: var(--cell-size);
    padding: 0.25em;
    padding-right: 0.3em;
    padding-left: 0.3em;
}

.ship[size='5'][orientation='vertical'] {
    width: var(--cell-size);
    height: calc(5 * var(--cell-size));
    padding: 0.25em;
    padding-bottom: 0.3em;
    padding-top: 0.3em;
}

.ship[size='5'][orientation='horizontal'] {
    width: calc(5 * var(--cell-size));
    height: var(--cell-size);
    padding: 0.25em;
    padding-right: 0.3em;
    padding-left: 0.3em;
}

.ship[isDown='true'] {
    background-color: red;
}

.ship[isDown='false'] {
    background-color: gainsboro;
}

.cell {
    position: relative;
    margin: 0;
    padding: 0;
    background-color: var(--color-09);
    border-width: 0;
    z-index: var(--layer-foreground-cells);
    overflow: visible;
}

.cell[isOccupied='true'] {
    background-color: grey;
}

.cell[isSelected='true'] {
    background-color: whitesmoke;
}

.cell[success='true'] {
    background-color: green;
}

.cell[success='false'] {
    background-color: lightcoral;
}

.cell .ship {
    position: absolute;
    top: 0;
    left: 0;
    z-index: var(--layer-foreground-ships);
}

.grid {
    border-spacing: 0;
}
</style>