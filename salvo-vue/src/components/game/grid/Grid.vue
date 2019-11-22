<template>
    <table v-bind:id='getGridId'>
        <thead>
            <th></th>
            <th v-for='x in gridHeaders' v-bind:key='x'>{{ x }}</th>
        </thead>
        <tbody>
            <tr v-for='(y, index) in gridHeadersY' v-bind:key='index'>
                <th>{{ y }}</th>
                <Cell v-for='x in gridHeaders' v-bind:key='x' :id='getCellId(x, y)' :isOccupied='isOccupied(x, y)' :isTurn='isTurn' :callBack='selectCell' />
            <tr/>
        </tbody>
    </table>
</template>

<script>
import Cell from '@/components/game/grid/Cell.vue';
import { mapState } from 'vuex';

export default {
    props: ['id', 'occupiedCells', 'isTurn', 'maxSelection', 'callBack'],
    components: {
        Cell
    },
    data: function() {
        return {
            selectedCells: []
        };
    },
    watch: {
        isTurn: function(n, p) {
            if (!n) {
                this.selectedCells = [];
                this.callBack(false);
            }
        }
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
            return this.occupiedCells.includes(y + x);
        },
        selectCell: function(cell, prev) {
            if (this.selectedCells.includes(cell)) {
                let index = this.selectedCells.indexOf(cell);
                this.selectedCells.splice(index, 1);
                this.callBack(false);

                return false;
            } else if (this.selectedCells.length < this.maxSelection) {
                this.selectedCells.push(cell);
                this.callBack(this.selectedCells.length == this.maxSelection);

                return true;
            }

            return prev;
        }
    }
};
</script>