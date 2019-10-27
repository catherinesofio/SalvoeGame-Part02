<template>
    <div>
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
            data: {}
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
        ...mapActions(['getMatchData']),
        setMatchData: function(data) {
            this.data = data;
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
        }
    },
    mounted: function() {
        this.gp = this.$route.params.gp;
        this.getMatchData({ gp: this.gp, callback: this.setMatchData });
    }
}
</script>