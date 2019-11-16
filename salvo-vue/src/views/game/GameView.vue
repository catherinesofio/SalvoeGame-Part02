<template>
    <div class='view'>
        <GUI :player='player' :opponent='opponent' />
        <div class='container-intro'>
            <div class='container-half content-left'>
                <Grid id='player' :occupiedCells='player.occupiedCells' />
                <Ship v-for='ship in player.ships' :key='ship.type' id='player' :type='ship.type' :size='ship.size' :locations='ship.locations' :isDown='ship.isDown' />
            </div>
        </div>
        <div class='container-body'>
            <Grid id='opponent' :occupiedCells='opponent.occupiedCells' />
            <Ship v-for='ship in opponent.ships' :key='ship.type' id='opponent' :type='ship.type' :size='ship.size' :locations='ship.locations' isDown='true' />
        </div>
    </div>
</template>

<script>
import GUI from '@/components/game/GUI/GUI.vue';
import Grid from '@/components/game/grid/Grid.vue';
import Ship from '@/components/game/Ship.vue';
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
                occupiedCells: []
            },
            opponent: {
                id: -1,
                state: '',
                ships: [],
                activeShips: 0,
                occupiedCells: []
            },
            shipsTemplate: []
        };
    },
    components: {
        GUI,
        Grid,
        Ship
    },
    watch: {
        data: function (newValue, oldValue) {
            if (newValue != oldValue && newValue != null && newValue != 'undefined' && this.shipsTemplate != []) {
                this.player = this.getPlayer(newValue.gamePlayers.filter(gp => gp.id == this.gp)[0], false);

                if (newValue.gamePlayers.length > 1) {
                    this.opponent = this.getPlayer(newValue.gamePlayers.filter(gp => gp.id != this.gp)[0], true);
                }
            }
        }
    },
    methods: {
        ...mapActions(['getShipsTemplate']),
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
        }
    },
    mounted: function() {
        this.getShipsTemplate(this.setShips);
    }
};
</script>>