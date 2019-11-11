<template>
    <div id='shipContainer'>
        <DummyShip v-for='(ship, index) in ships' v-bind:key='index' :type='ship.type' :size='ship.size' :callback='callback' />
    </div>
</template>

<script>
import DummyShip from '@/components/game/shipEditor/DummyShip.vue';
import { mapActions } from 'vuex';

export default {
    components: {
        DummyShip
    },
    props: ['callback'],
    data: function() {
        return {
            ships: []
        };
    },
    methods: {
        ...mapActions(['getShipsTemplate']),
        setShips: function(ships) {
            this.ships = ships;
        }
    },
    mounted: function() {
        this.getShipsTemplate(this.setShips);
    }
};
</script>

<style>
#shipContainer {
    box-sizing: border-box;
    position: relative;
    width: 100%;
    height: 5em;
    background-color: rgba(0,0,0,0.5);
}
</style>