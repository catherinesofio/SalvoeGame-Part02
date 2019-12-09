<template>
    <ul id='log-container'>
        <button v-on:click='triggerSwitchLog'>🗨️</button>
        <Log v-for='(log, i) in logs' :key='i' :data='log' :gamePlayers='gamePlayers' />
    </ul>
</template>

<script>
import Log from '@/components/game/log/Log.vue';
import { bus } from '@/main.js';

export default {
    props: ['gamePlayers', 'logs'],
    components: {
        Log
    },
    methods: {
        triggerSwitchLog: function () {
            bus.$emit('trigger-switch-log');
        }
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
    top: calc(var(--spacer-height) * -0.25);
    left: 0;
    width: 100vw;
    height: 100vh;
    padding: var(--padding);
    background-color: var(--color-04);
    overflow: hidden;
    overflow-y: scroll;
}

#log-container .log {
    margin-top: var(--margin);
}
</style>