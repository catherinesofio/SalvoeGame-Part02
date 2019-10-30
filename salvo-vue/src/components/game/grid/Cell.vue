<template>
    <td class='cell' :id='id' :isOccupied='isOccupied'></td>
</template>

<script>
export default {
    props: ['id', 'isOccupied']
};
</script>

<style>
.cell {
    width: var(--cell-size);
    background-color: lightskyblue;
}

.cell[isOccupied=true] {
    background-color: grey;
}
</style>