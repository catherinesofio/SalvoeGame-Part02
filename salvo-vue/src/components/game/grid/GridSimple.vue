<template>
    <table v-bind:id='getGridId' class='grid'>
        <thead>
            <th></th>
            <th v-for='x in gridHeaders' v-bind:key='x'>{{ x }}</th>
        </thead>
        <tbody>
            <tr v-for='(y, index) in gridHeadersY' v-bind:key='index'>
                <th>{{ y }}</th>
                <Cell v-for='x in gridHeaders' v-bind:key='x' :id='getCellId(x, y)' :isOccupied='isOccupied(x, y)' :salvoe='getSalvoe(x, y)' />
            <tr/>
        </tbody>
        <tfoot>
            <td></td>
            <td v-for='x in gridHeaders' v-bind:key='x'></td>
        </tfoot>
    </table>
</template>

<script>
import Cell from '@/components/game/grid/CellSimple.vue';
import { mapState } from 'vuex';

export default {
    props: ['id', 'occupiedCells', 'salvoes'],
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
            let cell = y + x;
            let salvoe = (this.salvoes != null && this.salvoes != 'undefined') ? this.salvoes.some(x => x.cell == cell) : false;

            return this.occupiedCells.includes(cell) || salvoe;
        },
        getSalvoe: function(x, y) {
            let cell = y + x;
            let salvoe = (this.salvoes != null && this.salvoes != 'undefined') ? this.salvoes.filter(x => x.cell == cell) : [];

            return (salvoe.length > 0) ? salvoe[0] : null;
        }
    }
};
</script>