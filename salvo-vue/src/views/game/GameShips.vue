<template>
    <div>
        <h1>SHIPS</h1>
        <DummyShipContainer />
        <Grid :id='id' :occupiedCells='[]' />
        <button v-on:click='triggerSubmit' :disabled='!allShipsPlaced'>SUBMIT</button>
    </div>
</template>

<script>
import Grid from '@/components/game/grid/Grid.vue';
import DummyShipContainer from '@/components/game/shipEditor/DummyShipContainer.vue';
import GizmoObject from '@/components/game/shipEditor/gizmoObject/GizmoObject.vue';
import { mapActions } from 'vuex';

export default {
    data: function() {
        return {
            id: 'player',
            data: [],
            allShipsPlaced: false
        };
    },
    components: {
        Grid,
        DummyShipContainer
    },
    methods: {
        ...mapActions(['setShips']),
        triggerSubmit: function() {
            if (this.allShipsPlaced) {
                this.setShips({ gp: this.$route.params.gp, params: this.data });
            }
        }
    }
};
</script>