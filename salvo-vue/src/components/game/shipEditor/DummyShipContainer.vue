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
    width: calc(100vw - (var(--margin) * 2));
    height: 5em;
    margin-top: calc(var(--spacer-height) / 2);
    margin-left: var(--margin);
    background-color: rgba(0,0,0,0.5);
    border-radius: var(--border-radius);
}
</style>