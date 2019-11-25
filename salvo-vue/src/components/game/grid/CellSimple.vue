<template>
    <td class='cell' :id='id' :isOccupied='isOccupied' ></td>
</template>

<script>
export default {
    props: ['id', 'isOccupied', 'salvoe'],
    watch: {
        salvoe: function(n, o) {
            if (n != null) {
                this.$el.setAttribute('success', n.success);
                this.$el.innerHTML = n.turn;
            }
        }
    }
};
</script>