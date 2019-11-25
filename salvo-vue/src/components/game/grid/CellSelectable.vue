<template>
    <td class='cell' v-on:click='select' :id='id' :isOccupied='isOccupied' :isSelected='isSelected'></td>
</template>

<script>
import { bus } from '@/main.js';

export default {
    props: ['id', 'isOccupied', 'salvoe', 'isTurn'],
    data: function() {
        return {
            isSelected: false
        };
    },
    watch: {
        salvoe: function(n, o) {
            if (n != null) {
                this.$el.setAttribute('success', n.success);
                this.$el.innerHTML = n.turn;
            }
        }
    },
    methods: {
        select: function(e) {
            if (this.isTurn && !this.isOccupied) {
                bus.$emit('check-salvoe', { cell: this.$el, state: this.isSelected, callback: this.setIsSelected });
            }
        },
        setIsSelected: function(value) {
            this.isSelected = value;
        }
    },
    mounted: function() {
        this.isSelected = this.isOccupied;
    }
};
</script>