<template>
    <ul id='log-container'>
        <Log />
    </ul>
</template>

<script>
import Log from '@/components/game/log/Log.vue';

export default {
    props: ['gamePlayers', 'logs'],
    components: {
        Log
    }
};
</script>