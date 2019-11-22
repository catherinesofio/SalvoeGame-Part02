<template>
    <td class='cell' v-on:click='select' :id='id' :isOccupied='isOccupied' :isSelected='isSelected'></td>
</template>

<script>
export default {
    props: ['id', 'isOccupied', 'isTurn', 'callBack'],
    data: function() {
        return {
            isSelected: false
        };
    },
    methods: {
        select: function(e) {
            if (this.isTurn && !this.isOccupied) {
                this.isSelected = this.callBack(this.$el, this.isSelected);
            }
        }
    },
    mounted: function() {
        this.isSelected = this.isOccupied;
    }
};
</script>

<style>
.cell {
    position: relative;
    width: var(--cell-size);
    background-color: lightskyblue;
}

.cell[isOccupied=true], .cell[isSelected=true] {
    background-color: grey;
}

.cell .ship {
    position: absolute;
}
</style>