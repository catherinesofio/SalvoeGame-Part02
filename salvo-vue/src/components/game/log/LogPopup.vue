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
            time: 15000,
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
                this.interval = setInterval(this.time, this.afterInterval);
            }
        }
    },
    computed: {
        getData: function() {
            return (this.logs.length > 0) ? this.logs[0]: null;
        }
    },
    methods: {
        triggerClose: function() {
            clearInterval(this.interval);
            this.callback();
        }
    },
    mounted: function() {
        this.interval = setInterval(this.time, this.triggerClose);
    },
    beforeDestroy: function() {
        clearInterval(this.interval);
    }
};
</script>