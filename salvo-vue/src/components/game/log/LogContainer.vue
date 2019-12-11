<template>
    <ul id='log-container'>
        <Log v-for='(log, i) in logs' :key='i' :data='log' :gamePlayers='gamePlayers' />
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

<style>
#log-container, #log-container * {
    z-index: var(--layer-log);
}

#log-container {
    box-sizing: border-box;
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    margin: 0;
    padding: var(--padding);
    background-color: rgba(0, 0, 0, 0.5);
    overflow: hidden;
    overflow-y: scroll;
}

#log-container .log {
    margin: 0;
    margin-top: calc(var(--spacer-height) / 1.5);
}
</style>