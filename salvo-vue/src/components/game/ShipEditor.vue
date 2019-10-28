<template>
    <div>
        <Grid :id='id' :occupiedCells='[]' />
    </div>
</template>

<script>
import Grid from '@/components/game/grid/Grid.vue';

export default {
    data: {
        id: 'player'
    },
    components: {
        Grid
    }
};
</script>