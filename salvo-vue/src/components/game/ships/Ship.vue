<template>
    <div v-bind:isPositioned='isPositioned'></div>
</template>

<script>
export default {
    props: [('isPositioned', 'cell')],
    data: function() {
        return {
        };
    }
};
</script>