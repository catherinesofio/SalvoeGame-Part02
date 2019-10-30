<template>
    <div class='view'>
        <Nav :user='this.user' />
        <Spacer />
        <router-view />
    </div>
</template>

<script>
import Nav from '@/components/nav/Nav.vue';
import Spacer from '@/components/Spacer.vue';
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
        Spacer
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
                    path = '/game/' + this.gp + '/ships';
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