<template>
    <ul id='log-container'>
        <Log v-for='(log, i) in logs' :key='i' :data='log' />
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