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
            <Ship v-for='ship in opponent.ships' :key='ship.type' id='opponent' :type='ship.type' :size='ship.size' :locations='ship.locations' isDown='true' />
        </div>
    </div>
</template>

<script>
import GUI from '@/components/game/GUI/GUI.vue';
import GridSimple from '@/components/game/grid/GridSimple.vue';
import GridSelectable from '@/components/game/grid/GridSelectable.vue';
import Ship from '@/components/game/Ship.vue';
import { bus } from '@/main.js';
import { mapActions } from 'vuex';

export default {
    props: ['gp', 'turn', 'data', 'ships'],
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
        data: function (newValue, oldValue) {
            if (newValue != oldValue && newValue != null && newValue != 'undefined' && this.shipsTemplate != []) {
                let p = newValue.gamePlayers.filter(gp => gp.id == this.gp)[0];
                let pSalvoes = this.player.salvoes;
                let o = newValue.gamePlayers.filter(gp => gp.id != this.gp)[0];
                let oSalvoes = this.opponent.salvoes;

                this.player = this.getPlayer(p, false);

                if (o != null) {
                    this.opponent = this.getPlayer(o, true);

                    this.player['salvoes'] = this.updateSalvoes(pSalvoes, o.salvoes);
                    this.opponent['salvoes'] = this.updateSalvoes(oSalvoes, p.salvoes);
                }
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
            let size;
            let ships = (opponent) ? gp.ships.sunkShips : this.ships;
            
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
                occupiedCells: locations
            };
            
            return player;
        },
        updateSalvoes: function(o, n) {
            if (n.length == 0 || (o.length > 0 && o[o.length - 1].turn == n[0].turn)) {
                return o;
            }

            return o.concat(n);
        }
    },
    mounted: function() {
        this.getShipsTemplate(this.setShips);
    }
};
</script>>