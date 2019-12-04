<template>
    <div>
        <div>
            <span>{{ getQueueCount }}</span>
            <button v-on:click='switchNotifications'>⚡</button>
            <button v-on:click='switchLog'>🗨️</button>
        </div>
        <LogPopup v-if='showNotifications' :logs='logQueue' :gamePlayers='gamePlayers' />
        <LogContainer v-if='showLog' :gamePlayers='gamePlayers' :logs='logs' />
    </div>
</template>

<script>
import LogContainer from '@/components/game/log/LogContainer.vue';
import LogPopup from '@/components/game/log/LogPopup.vue';
import { bus } from '@/main.js';

export default {
    props: ['data', 'logs'],
    data: function() {
        return {
            count: 0,
            queueIndex: 0,
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
        data: function(n, o) {
            if (isValid(n) && n != o) {
                this.setGamePlayers(n.gamePlayers);
            }
        },
        logs: function(n, o) {
            if (isValid(n) && n.length > this.count) {
                this.count = n.length;

                this.logQueue = this.logQueue.concat(n.slice(this.queueIndex));
            }
        }
    },
    computed: {
        getQueueCount: function() {
            return this.logQueue.length;
        },
        getQueueIsFull: function() {
            return this.getQueueCount > 0;
        }
    },
    methods: {
        closeNotification: function() {
            let index = this.queueIndex = Math.min(this.queueIndex + 1, this.count);
            
            this.logQueue = (index > 0 && index < this.count) ? this.logs.slice(index) : [];
            
            if (this.showNotifications && this.logQueue.length > 0) {
                bus.$emit('notification-open');
            }
        },
        switchNotifications: function() {
            this.showNotifications = !this.showNotifications;

            if (!this.showNotifications) {
                this.closeNotification();
            } else {
                bus.$emit('notification-open');
            }
        },
        switchLog: function() {
            this.showLog = !this.showLog;

            if (this.showLog) {
                this.queueIndex = this.count;
                this.logQueue = [];
            }

            if (this.showNotifications && this.showLog) {
                bus.$emit('notification-pause');
            } else if (this.showNotifications && !this.showLog) {
                bus.$emit('notification-resume');
            }
        },
        setGamePlayers: function(gamePlayers) {
            if (isValid(gamePlayers)) {
                this.gamePlayers = gamePlayers.map(gp => { 
                    return { id: gp.id, player: gp.player };
                });
            } else {
                this.gamePlayers = [];
            }
        }
    },
    mounted: function() {
        this.setGamePlayers((isValid(this.data)) ? this.data.gamePlayers : null);
        this.logQueue = (isValid(this.logs)) ? this.logs : [];
        
        let length = this.logQueue.length;
        this.count = length;
        this.queueIndex = length - 1;

        bus.$on('notification-close', this.closeNotification);
    },
    beforeDestroy: function() {
        bus.$off('notification-close', this.closeNotification);
    }
};
</script>