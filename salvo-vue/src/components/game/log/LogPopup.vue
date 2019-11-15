<template>
    <div>
        <Log :data='getData' />
        <button v-on:click='triggerClose'>X</button>
    </div>
</template>

<script>
import Log from '@/components/game/log/Log.vue';

export default {
    props: ['logs', 'callback'],
    data: function() {
        return {
            count: 0,
            time: 3000,
            interval: null
        }
    },
    components: {
        Log
    },
    watch: {
        logs: function(newValue, oldValue){
            this.count = newValue.length;

            if (this.count > 0) {
                interval = setInterval(this.time, this.afterInterval);
            }
        }
    },
    computed: {
        getData: function() {
            return (logs.length > 0) ? logs[0]: null;
        }
    },
    methods: {
        triggerClose: function() {
            clearInterval(interval);
            this.callback();
        }
    },
    mounted: function() {
        interval = setInterval(this.time, this.triggerClose);
    },
    beforeDestroy: function() {
        clearInterval(interval);
    }
};
</script>