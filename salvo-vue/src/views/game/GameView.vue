<template>
    <div class='view'>
        <h1>VIEW</h1>
        <div>
            <Grid id='player' :occupiedCells='player.occupiedCells' />
            <Ship v-for='ship in player.ships' :key='ship.type' id='player' :type='ship.type' :size='ship.size' :locations='ship.locations' :isDown='ship.isDown' />
        </div>
        <div>
            <Grid id='opponent' :occupiedCells='opponent.occupiedCells' />
            <Ship v-for='ship in opponent.ships' :key='ship.type' id='opponent' :type='ship.type' :size='ship.size' :locations='ship.locations' isDown='true' />
        </div>
    </div>
</template>

<script>
import Grid from '@/components/game/grid/Grid.vue';
import Ship from '@/components/game/Ship.vue';

export default {
    props: [('gp', 'turn', 'data')],
    data: function() {
        return {
            player: {
                ships: [],
                activeShips: 0,
                occupiedCells: []
            },
            opponent: {
                ships: [],
                activeShips: 0,
                occupiedCells: []
            }
        }
    },
    components: {
        Grid,
        Ship
    }
};
</script>>