<template>
    <div class='view'>
        <Nav :user='this.user' />
        <Spacer />
        <router-view :gp='gp' :turn='turn' :data='data' />
        <LogManager :data='data' />
    </div>
</template>

<script>
import Nav from '@/components/nav/Nav.vue';
import Spacer from '@/components/Spacer.vue';
import LogManager from '@/components/LogManager.vue';
import { mapState, mapActions } from 'vuex';

export default {
    name: 'Game',
    data: function() {
        return {
            gp: 0,
            data: {},
            turn: -1,
            interval: null,
            time: 30000
        };
    },
    components: {
        Nav,
        Spacer,
        LogManager
    },
    computed: {
        ...mapState(['user'])
    },
    methods: {
        ...mapActions(['getMatchData', 'getTurnData']),
        setMatchData: function(data) {
            this.data = data;
            this.turn = data.turn;
            let path = '/menu';

            switch (data.state) {
                case 'WAITING':
                    if (data.gamePlayers.filter(g => g.id == this.gp)[0].state == 'WAITING_PLAYER') {
                        path = '/game/' + this.gp + '/view';
                    } else {
                        path = '/game/' + this.gp + '/ships';
                    }
                    break;
                case 'PLAYING':
                    path = '/game/' + this.gp + '/view';
                    break;
                case 'FINISHED':
                    path = '/game/' + this.gp + '/results';
                    break;
            }
            
            this.$router.push({ path: path });

            clearInterval(this.interval);
            this.interval = setInterval(this.triggerUpdateMatchData, this.time);
        },
        triggerUpdateMatchData: function() {
            this.getTurnData({ gp: this.gp, tn: this.turn, callback: this.updateMatchData });
        },
        updateMatchData: function(data) {
            this.data = data;
            this.turn = data.turn;

            clearInterval(this.interval);
            this.interval = setInterval(this.triggerUpdateMatchData, this.time);
        }
    },
    mounted: function() {
        this.gp = this.$route.params.gp;
        this.getMatchData({ gp: this.gp, callback: this.setMatchData });
    },
    beforeDestroy: function() {
        clearInterval(this.interval);
    }
}
</script>

<style>
.ship {
    position: relative;
    background-color: gainsboro;
    z-index: var(--layer-foreground);
}

.ship[size='2'][orientation='vertical'] {
    width: var(--cell-size);
    height: calc(2 * var(--cell-size));
    padding: 0.25em;
    padding-bottom: 0.3em;
    padding-top: 0.3em;
}

.ship[size='2'][orientation='horizontal'] {
    width: calc(2 * var(--cell-size));
    height: var(--cell-size);
    padding: 0.25em;
    padding-right: 0.3em;
    padding-left: 0.3em;
}

.ship[size='3'][orientation='vertical'] {
    width: var(--cell-size);
    height: calc(3 * var(--cell-size));
    padding: 0.25em;
    padding-bottom: 0.3em;
    padding-top: 0.3em;
}

.ship[size='3'][orientation='horizontal'] {
    width: calc(3 * var(--cell-size));
    height: var(--cell-size);
    padding: 0.25em;
    padding-right: 0.3em;
    padding-left: 0.3em;
}

.ship[size='4'][orientation='vertical'] {
    width: var(--cell-size);
    height: calc(4 * var(--cell-size));
    padding: 0.25em;
    padding-bottom: 0.3em;
    padding-top: 0.3em;
}

.ship[size='4'][orientation='horizontal'] {
    width: calc(4 * var(--cell-size));
    height: var(--cell-size);
    padding: 0.25em;
    padding-right: 0.3em;
    padding-left: 0.3em;
}

.ship[size='5'][orientation='vertical'] {
    width: var(--cell-size);
    height: calc(5 * var(--cell-size));
    padding: 0.25em;
    padding-bottom: 0.3em;
    padding-top: 0.3em;
}

.ship[size='5'][orientation='horizontal'] {
    width: calc(5 * var(--cell-size));
    height: var(--cell-size);
    padding: 0.25em;
    padding-right: 0.3em;
    padding-left: 0.3em;
}
</style>