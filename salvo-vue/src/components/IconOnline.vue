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
    margin-right: var(--margin);
    background-color: lightgray;
    border-radius: 100px;
    border-style: solid;
    border-color: var(--color-00);
    border-width: var(--border-width);
}

div[isOnline=true] {
    background-color: greenyellow;
}
</style>