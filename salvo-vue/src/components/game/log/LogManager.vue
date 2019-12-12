<template>
    <div>
        <div id='log-panel'>
            <button id='btn-notification' selected='true' v-on:click='switchNotifications'>{{ getQueueCount }} ⚡</button>
            <button id='btn-logs' v-on:click='switchLog'>🗨️</button>
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
            showLog: false,
            btnNotification: null
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
                unselect(this.btnNotification);
            } else {
                select(this.btnNotification);
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
        this.btnNotification = this.$el.querySelector('#btn-notification');
        select(this.btnNotification);

        this.setGamePlayers((isValid(this.data)) ? this.data.gamePlayers : null);
        this.logQueue = (isValid(this.logs)) ? this.logs : [];
        
        let length = this.logQueue.length;
        this.count = length;
        this.queueIndex = length - 1;

        bus.$on('notification-close', this.closeNotification);
        bus.$on('trigger-switch-log', this.switchLog);
    },
    beforeDestroy: function() {
        bus.$off('notification-close', this.closeNotification);
        bus.$off('trigger-switch-log', this.switchLog);
    }
};
</script>

<style>
#log-panel {
    box-sizing: border-box;
    text-align: right;
    position: fixed;
    top: 0;
    width: 100vw;
    padding: calc(var(--padding) / 2);
}

#log-panel * {
    margin-left: var(--margin);
    margin-right: var(--margin);
}

#log-panel, #log-panel * {
    z-index: var(--layer-popup);
}

#btn-notification {
    background-color: var(--color-12);
}

#btn-notification[selected=true] {
    background-color: var(--color-08);
}
</style>