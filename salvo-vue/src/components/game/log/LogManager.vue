<template>
    <div>
        <LogContainer :gamePlayers='gamePlayers' :logs='data' />
        <LogPopup v-if='showNotifications' :logs='logQueue' : />
        <span v-if='getQueueCount > 0'></span>
    </div>
</template>

<script>
import LogContainer from '@/components/game/log/LogContainer.vue';
import LogPopup from '@/components/game/log/LogContainer.vue';

export default {
    props: ['data'],
    data: function() {
        return {
            count = -1,
            queueIndex = -1,
            logQueue: [],
            gamePlayers: [],
            showNotifications: true
        };
    },
    components: {
        LogContainer,
        LogPopup
    },
    watch: {
        data: function(newValue, oldValue) {
            if (newValue != oldValue) {
                this.count = this.data.logs.length;
                this.queueIndex = this.count - 1;
            }
        }
    },
    computed: {
        getQueueCount: function() {
            return this.logQueue.length;
        }
    },
    methods: {
        closeNotification: function() {
            this.queueIndex = Math.min(this.queueIndex + 1, this.ge);

            this.logQueue = (index > -1) ? data.logs.slice(index): [];
        },
        showNotifications: function(value) {

        }
    },
    mounted: function() {
        this.logQueue = this.data;
        this.queueIndex = this.data.length;
    }
};
</script>