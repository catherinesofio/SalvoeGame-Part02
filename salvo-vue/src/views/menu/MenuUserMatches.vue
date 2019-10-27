<template>
    <div>
        <div>
            <button v-on:click='setShowCurrent(true)'>CURRENT</button>
            <button v-on:click='setShowCurrent(false)'>HISTORY</button>
        </div>
        <table>
            <caption>MY MATCHES</caption>
            <thead>
                <th>CHALLENGER</th>
                <th>STATUS</th>
            </thead>
            <tbody>
                <UserMatchInfo v-for='match in this.getMatches' v-bind:key='match.id' :showCurrent='showCurrent' :opponent='getName(match.gamePlayers.opponent)' :isOnline='isOnline(match.gamePlayers.opponent)' :state='getStates(match)' />
            </tbody>
        </table>
    </div>
</template>

<script>
import UserMatchInfo from '@/components/menu/UserMatchInfo.vue';
import { mapState, mapGetters } from 'vuex';

export default {
    data: function() {
        return {
            showCurrent: true
        }
    },
    components: {
        UserMatchInfo
    },
    computed: {
        ...mapState(['userMatches']),
        ...mapGetters(['getUserName', 'userIsOnline']),
        getMatches: function() {
            let matches = (this.showCurrent) ? this.userMatches.current : this.userMatches.history;
            
            return (matches.length > 0) ? matches : [];
        }
    },
    methods: {
        setShowCurrent: function(value) {
            this.showCurrent = value;
        },
        getName: function(user) {
            return (user != null) ? this.getUserName(user.id) : '';
        },
        isOnline: function(user) {
            return (user != null) ? this.userIsOnline(user.id) : false;
        },
        getStates: function(data) {
            let opponent = data.gamePlayers.opponent;

            return { game: data.state , opponent: (opponent != null) ? opponent.state : '' };
        }
    }
};
</script>