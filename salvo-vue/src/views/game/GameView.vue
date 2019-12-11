<template>
    <div id='container-view'>
        <div class='container-wrap'>
            <div class='wrap-content'>
                <GridSimple :id='playerId' :occupiedCells='player.occupiedCells' :salvoes='player.salvoes' />
            </div>
            <GUI class='wrap-content panel' :gp='gp' :turn='turn' :player='player' :opponent='opponent' />
        </div>
        <GridSelectable id='opponent' :occupiedCells='opponent.occupiedCells' :salvoes='opponent.salvoes' :isTurn='isTurn' />
        <Ship v-for='ship in opponent.ships' :key='ship.type' :id='opponent' :type='ship.type' :size='ship.size' :locations='ship.locations' :isDown='true' />
        <button class='help'>?</button>
    </div>
</template>

<script>
import GUI from '@/components/game/GUI/GUI.vue';
import GridSimple from '@/components/game/grid/GridSimple.vue';
import GridSelectable from '@/components/game/grid/GridSelectable.vue';
import Ship from '@/components/game/Ship.vue';
import { mapActions } from 'vuex';
import { bus } from '@/main.js';

export default {
    props: ['gp', 'turn', 'data'],
    data: function() {
        return {
            playerId: 'player-gui',
            player: {
                id: -1,
                state: '',
                ships: [],
                activeShips: 0,
                occupiedCells: [],
                salvoes: []
            },
            opponent: {
                id: -1,
                state: '',
                ships: [],
                activeShips: 0,
                occupiedCells: [],
                salvoes: []
            },
            shipsTemplate: []
        };
    },
    components: {
        GUI,
        GridSimple,
        GridSelectable,
        Ship
    },
    watch: {
        data: function (n) {
            if (isValid(n) && this.shipsTemplate.length > 0) {
                let player = n.gamePlayers.filter(gp => gp.id == this.gp)[0];
                this.player = this.getPlayer(player, false);

                let opponent = n.gamePlayers.filter(gp => gp.id != this.gp)[0];
                if (isValid(opponent)) {
                    this.opponent = this.getPlayer(opponent, true);
                }

                this.swapSalvoes();
            }
        }
    },
    computed: {
        isTurn: function() {
            return this.player.state == 'PLAYING_TURN';
        }
    },
    methods: {
        ...mapActions(['getShipsTemplate', 'getSalvoesTemplate']),
        setShips: function(data) {
            this.shipsTemplate = data;
        },
        getPlayer: function(gp, opponent) {
            if (!isValid(gp)) {
                return {
                    id: -1,
                    state: '',
                    ships: [],
                    activeShips: '',
                    occupiedCells: [],
                    salvoes: []
                }
            }

            let size;
            let ships = (opponent) ? gp.ships.sunkShips : gp.ships.all;
            
            ships = ships.map(ship => {
                size = this.shipsTemplate.filter(x => x.type == ship.type)[0].size;

                return { type: ship.type, size: size, locations: ship.locations, isDown: ship.isDown };
            });
            let locations = ships.flatMap(ship => ship.locations);
            
            let player = {
                id: parseInt(gp.player.id),
                state: gp.state,
                ships: ships,
                activeShips: parseInt(gp.ships.activeShips),
                occupiedCells: locations,
                salvoes: gp.salvoes
            };
            
            return player;
        },
        swapSalvoes: function() {
            let temp = this.player.salvoes;

            this.player = Object.assign({}, this.player, { salvoes: this.opponent.salvoes });
            this.opponent = Object.assign({}, this.opponent, { salvoes: temp });
        }
    },
    mounted: function() {
        bus.$emit('trigger-data-reload');
        this.getShipsTemplate(this.setShips);
    }
};
</script>

<style>
.container-wrap {
    width: 100vw;
}

.wrap-content {
    min-width: 40vw;
    margin: var(--margin);
    display: flexbox;
}

.panel {
    padding: calc(var(--padding) / 2);
    border-width: var(--border-width);
    border-color: var(--color-00);
    border-style: solid;
    border-radius: var(--border-radius);
    background-color: var(--color-05);
    text-align: justify;
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

#opponent-table {
    margin: var(--margin);
    width: calc(100vw - (var(--margin) * 2));
    overflow: visible;
}

#opponent-table th {
    width: var(--cell-size);
    height: var(--cell-size);
    background-color: var(--color-00);
    color: var(--color-09);
    border-width: 0;
    border-color: var(--color-04);
    border-style: solid;
}

#opponent-table thead th {
    border-top-width: var(--border-width);
}

#opponent-table thead th:first-of-type {
    border-left-width: var(--border-width);
}

#opponent-table thead th:last-of-type {
    border-right-width: var(--border-width);
}

#opponent-table tbody th {
    border-left-width: var(--border-width);
}

#opponent-table td {
    width: var(--cell-size);
    height: var(--cell-size);
    border-right-width: calc(var(--border-width) / 2);
    border-bottom-width: calc(var(--border-width) / 2);
    border-color: var(--color-00);
    border-style: solid;
}

#opponent-table tbody tr td:last-of-type {
    border-right-width: var(--border-width);
    border-right-color: var(--color-04);
    border-style: solid;
}

#opponent-table thead :first-child {
    border-top-left-radius: var(--border-radius);
}

#opponent-table thead :last-child {
    border-top-right-radius: var(--border-radius);
}

#opponent-table tfoot td {
    width: calc(var(--cell-size) / 2);
    background-color: var(--color-05);
    border-width: 0;
    border-bottom-width: var(--border-width);
    border-color: var(--color-04);
    border-style: solid;
}

#opponent-table tfoot td:first-of-type {
    border-bottom-left-radius: var(--border-radius);
    border-left-width: var(--border-width);
}

#opponent-table tfoot td:last-child {
    border-bottom-right-radius: var(--border-radius);
    border-right-width: var(--border-width);
}

#container-editor {
    overflow: visible;
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

#player-gui-table .cell[isOccupied='true'] {
    background-color: var(--color-00);
}

#player-gui-table .cell[success='true'] {
    background-color: var(--color-05);
}

#player-gui-table .cell[success='false'] {
    background-color: var(--color-12);
}
</style>