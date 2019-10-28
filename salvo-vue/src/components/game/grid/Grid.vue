<template>
    <table v-bind:id='getGridId'>
        <thead>
            <th></th>
            <th v-for='x in headers' v-bind:key='x'>{{ x }}</th>
        </thead>
        <tbody>
            <tr v-for='x in headers' v-bind:key='x'>
                <th>{{ headersY[x] }}</th>
                <Cell v-for='y in headers' v-bind:key='y' :id='getCellId(x, y)' :isEmpty='isOccupied(x, y)' />
            <tr/>
        </tbody>
    </table>
</template>

<script>
import Cell from '@/components/grid/Cell.vue';

export default {
    props: ['id', 'occupiedCells'],
    data: {
        headers: 10,
        headersY: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'],
    },
    components: {
        Cell
    },
    computed: {
        getGridId: function() {
            return this.id + '-table';
        }
    },
    methods: {
        getCellId: function(x, y) {
            return this.id + '-' + this.headersY[y] + x;
        },
        isOccupied: function(x, y) {
            return this.occupiedCells.includes(this.headersY[y] + x);
        }
    }
};
</script>