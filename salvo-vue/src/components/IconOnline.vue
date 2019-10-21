<template>
    <div v-bind:isOnline='this.isOnline'></div>
</template>

<script>
export default {
    props: ['isOnline']
};
</script>

<style scoped>
div {
    width: 1em;
    height: 1em;
    background-color: lightgray;
}

div[isOnline=true] {
    background-color: greenyellow;
}
</style>