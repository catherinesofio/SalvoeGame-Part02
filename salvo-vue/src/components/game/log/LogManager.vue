<template>
    <div>
        <LogContainer :gamePlayers='gamePlayers' :logs='getLogs' />
        <LogPopup v-if='showN' :logs='logQueue' />
        <span v-if='getQueueIsFull'>{{ getQueueCount }}</span>
    </div>
</template>

<script>
import LogContainer from '@/components/game/log/LogContainer.vue';
import LogPopup from '@/components/game/log/LogContainer.vue';

export default {
    props: ['data'],
    data: function() {
        return {
            count: -1,
            queueIndex: -1,
            logQueue: [],
            gamePlayers: [],
            showN: true,
            showL: false
        };
    },
    components: {
        LogContainer,
        LogPopup
    },
    watch: {
        data: function(newValue, oldValue) {
            if (newValue != oldValue && newValue != null && newValue != 'undefined') {
                this.count = newValue.logs.length;
                this.queueIndex = this.count - 1;
                this.setGamePlayers(newValue.gamePlayers);
            }
        }
    },
    computed: {
        getLogs: function() {
            return (this.data != null) ? this.data.logs: [];
        },
        getQueueCount: function() {
            return this.logQueue.length;
        },
        getQueueIsFull: function() {
            return this.getQueueCount > 0;
        }
    },
    methods: {
        closeNotification: function() {
            this.queueIndex = Math.min(this.queueIndex + 1, this.ge);

            this.logQueue = (index > -1) ? this.data.logs.slice(index): [];
        },
        showNotifications: function(value) {
            this.showN = value;
        },
        showLog: function(value) {
            this.showL = value;
        },
        setGamePlayers: function(gamePlayers) {
            this.gamePlayers = gamePlayers.map(gp => { return { id: gp.id, player: gp.player }; });
        }
    },
    beforeMount: function() {
        this.logQueue = (this.data != null) ? this.data.logs : [];
        this.queueIndex = this.logQueue.length;
        this.setGamePlayers((this.data != null) ? this.data.gamePlayers : null);
    }
};
</script>