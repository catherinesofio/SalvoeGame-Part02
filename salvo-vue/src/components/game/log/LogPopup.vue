<template>
    <div v-if='getShow' class='notification'>
        <Log :data='getData' :gamePlayers='gamePlayers' />
        <button v-on:click='triggerClose'>X</button>
    </div>
</template>

<script>
import Log from '@/components/game/log/Log.vue';
import { bus } from '@/main.js';

export default {
    props: ['logs', 'gamePlayers'],
    data: function() {
        return {
            count: 0,
            time: 3000,
            interval: null,
            isPaused: false
        }
    },
    components: {
        Log
    },
    watch: {
        logs: function(n, o){
            this.count = n.length;
            
            if (o.length == 0 && this.count > 0) {
                this.triggerOpen();
            }
        }
    },
    computed: {
        getShow: function() {
            return this.count > 0 && !this.isPaused;
        },
        getData: function() {
            return (this.logs.length > 0) ? this.logs[0] : null;
        }
    },
    methods: {
        triggerClose: function() {
            clearInterval(this.interval);
            
            bus.$emit('notification-close');
        },
        triggerOpen: function() {
            this.interval = setInterval(this.triggerClose, this.time);
        },
        triggerPause: function() {
            this.isPaused = true;

            clearInterval(this.interval);
        },
        triggerResume: function() {
            this.isPaused = false;

            this.interval = setInterval(this.triggerClose, this.time);
        }
    },
    mounted: function() {
        bus.$on('notification-open', this.triggerOpen);
        bus.$on('notification-pause', this.triggerPause);
        bus.$on('notification-resume', this.triggerResume);
    },
    beforeDestroy: function() {
        clearInterval(this.interval);

        bus.$off('notification-open', this.triggerOpen);
        bus.$off('notification-pause', this.triggerPause);
        bus.$off('notification-resume', this.triggerResume);
    }
};
</script>

<style>
.notification, .notification * {
    z-index: var(--layer-notification);
}

.notification {
    position: absolute;
    left: calc(0vw - (var(--padding)));
    bottom: calc(var(--padding) * -1);/*calc(var(--margin) * 3);*/
    width: 100vw;;
    padding: var(--padding);
}

.notification button {
    position: absolute;
    left: calc(100vw - (var(--padding)));
    top: calc(var(--padding) * 2);
}
</style>