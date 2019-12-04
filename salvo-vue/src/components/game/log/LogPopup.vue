<template>
    <div v-if='count > 0'>
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
            time: 2500,
            interval: null
        }
    },
    components: {
        Log
    },
    watch: {
        logs: function(n, o){
            this.count = n.length;

            if (o.length == 0 && this.count > 0) {console.log('khe');
                this.triggerOpen();
            }
        }
    },
    computed: {
        getData: function() {
            return (this.logs.length > 0) ? this.logs[0] : null;
        }
    },
    methods: {
        triggerClose: function() {
            clearInterval(this.interval);

            bus.$emit('close-notification');
        },
        triggerOpen: function() {
            this.interval = setInterval(this.time, this.triggerClose);
        }
    },
    mounted: function() {
        this.interval = setInterval(this.time, this.triggerClose);
        
        bus.$on('open-notification', this.triggerOpen);
    },
    beforeDestroy: function() {
        clearInterval(this.interval);

        bus.$off('open-notification', this.triggerOpen);
    }
};
</script>