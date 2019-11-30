<template>
    <div class='view'>
        <GUI :gp='gp' :turn='turn' :player='player' :opponent='opponent' />
        <div class='container-intro'>
            <div class='container-half content-left'>
                <GridSimple id='player' :occupiedCells='player.occupiedCells' :salvoes='player.salvoes' />
                <Ship v-for='ship in player.ships' :key='ship.type' id='player' :type='ship.type' :size='ship.size' :locations='ship.locations' :isDown='ship.isDown' />
            </div>
        </div>
        <div class='container-body'>
            <GridSelectable id='opponent' :occupiedCells='opponent.occupiedCells' :salvoes='opponent.salvoes' :isTurn='isTurn' />
            <Ship v-for='ship in opponent.ships' :key='ship.type' id='opponent' :type='ship.type' :size='ship.size' :locations='ship.locations' :isDown='true' />
        </div>
    </div>
</template>

<script>
import GUI from '@/components/game/GUI/GUI.vue';
import GridSimple from '@/components/game/grid/GridSimple.vue';
import GridSelectable from '@/components/game/grid/GridSelectable.vue';
import Ship from '@/components/game/Ship.vue';
import { mapActions } from 'vuex';

export default {
    props: { gp: Number, turn: Number, data: Object },
    data: function() {
        return {
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
        data: function (n, o) {console.log(n);
            if (n != null && n != 'undefined') {
                let player = n.gamePlayers.filter(gp => gp.id == this.gp)[0];
                this.player = this.getPlayer(player, false);

                let opponent = n.gamePlayers.filter(gp => gp.id != this.gp)[0];
                this.opponent = this.getPlayer(opponent, true);

                this.swapSalvoes();
                console.log(player);
                console.log(opponent);
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
        setShips: function(ships) {
            this.shipsTemplate = ships;
        },
        getPlayer: function(gp, opponent) {
            if (gp == 'undefined' || gp == null) {
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

            this.player.salvoes = this.opponent.salvoes;
            this.opponent.salvoes = temp;
        }
    },
    mounted: function() {
        this.getShipsTemplate(this.setShips);
    }
};
</script>>