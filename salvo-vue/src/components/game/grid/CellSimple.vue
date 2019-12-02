<template>
    <td class='cell' :id='id' :isOccupied='isOccupied'>
        <span class='cell-content'></span>
    </td>
</template>

<script>
export default {
    props: ['id', 'isOccupied', 'salvoe'],
    data: function() {
        return {
            content: null
        };
    },
    watch: {
        salvoe: function(n, o) {
            if (isValid(n)) {
                this.$el.setAttribute('success', n.success);
                this.content.innerHTML = n.turn;
            }
        }
    },
    mounted: function() {
        this.content = this.$el.querySelector('.cell-content');
    }
};
</script>