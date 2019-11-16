<template>
    <div>
        <div>
            <span>{{ getQueueCount }}</span>
            <button v-on:click='switchNotifications'>⚡</button>
            <button v-on:click='switchLog'>🗨️</button>
        </div>
        <LogPopup v-if='showNotifications' :logs='logQueue' />
        <LogContainer v-if='showLog' :gamePlayers='gamePlayers' :logs='getLogs' />
    </div>
</template>

<script>
import LogContainer from '@/components/game/log/LogContainer.vue';
import LogPopup from '@/components/game/log/LogContainer.vue';

export default {
    props: ['data'],
    data: function() {
        return {
            count: 0,
            queueIndex: -1,
            logQueue: [],
            gamePlayers: [],
            showNotifications: true,
            showLog: false
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
        switchNotifications: function() {
            this.showNotifications = !this.showNotifications;
        },
        switchLog: function() {
            this.showLog = !this.showLog;
        },
        setGamePlayers: function(gamePlayers) {
            if (gamePlayers != null) {
                this.gamePlayers = gamePlayers.map(gp => { 
                    return { id: gp.id, player: gp.player };
                });
            } else {
                this.gamePlayers = [];
            }
        }
    },
    mounted: function() {
        this.setGamePlayers((this.data != null) ? this.data.gamePlayers : null);
        this.logQueue = (this.data != null) ? this.data.logs : [];
        
        let length = this.logQueue.length;
        this.count = length;
        this.queueIndex = length - 1;
    }
};
</script>