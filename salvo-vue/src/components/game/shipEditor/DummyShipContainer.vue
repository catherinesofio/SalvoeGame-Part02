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
    width: calc((var(--cell-size) * 5) + (var(--padding) * 2));
    height: calc(var(--cell-size) * 3);
    margin-left: var(--margin);
    padding: var(--padding);
    background-color: var(--color-05);
    border-radius: var(--border-radius);
    overflow: hidden;
    overflow-y: scroll;
    border-width: var(--border);
    border-style: solid;
}
</style>