<template>
    <table v-bind:id='getGridId'>
        <thead>
            <th></th>
            <th v-for='x in gridHeaders' v-bind:key='x'>{{ x }}</th>
        </thead>
        <tbody>
            <tr v-for='(y, index) in gridHeadersY' v-bind:key='index'>
                <th>{{ y }}</th>
                <Cell v-for='x in gridHeaders' v-bind:key='x' :id='getCellId(x, y)' :isOccupied='isOccupied(x, y)' />
            <tr/>
        </tbody>
    </table>
</template>

<script>
import Cell from '@/components/game/grid/Cell.vue';
import { mapState } from 'vuex';

export default {
    props: ['id', 'occupiedCells'],
    components: {
        Cell
    },
    computed: {
        ...mapState(['gridHeaders', 'gridHeadersY']),
        getGridId: function() {
            return this.id + '-table';
        }
    },
    methods: {
        getCellId: function(x, y) {
            return this.id + '-' + y + x;
        },
        isOccupied: function(x, y) {
            return this.occupiedCells.includes(this.gridHeadersY[y] + x);
        }
    }
};
</script>