<template>
    <td v-bind:id='id' v-bind:isEmpty='isEmpty'></td>
</template>

<script>
export default {
    props: ['id', 'isEmpty']
};
</script>

<style scoped>
td[isEmpty=true] {
    color: blue;
}

td[isEmpty=false] {
    color: gray;
}
</style>