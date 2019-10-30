<template>
    <div>
        <DummyShip v-for='ship in ships' v-bind:key='ship' />
    </div>
</template>

<script>
import DummyShip from '@/components/game/ships/DummyShip.vue';
import { mapActions } from 'vuex';

export default {
    components: {
        DummyShip
    },
    data: function() {
        return {
            ships: []
        };
    },
    methods: {
        ...mapActions(['getShipsTemplate']),
        setShips: function(data) {
            this.ships = data;
        }
    },
    mounted: function() {
        this.getShipsTemplate(this.setShips);
    }
};
</script>