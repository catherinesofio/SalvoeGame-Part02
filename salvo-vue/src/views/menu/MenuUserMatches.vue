<template>
    <div class='view-panel'>
        <h1>MY MATCHES</h1>
        <div>
            <button v-on:click='setShowCurrent(true)'>CURRENT</button>
            <button v-on:click='setShowCurrent(false)'>HISTORY</button>
        </div>
        <table class='custom-table'>
            <thead>
                <th>CHALLENGER</th>
                <th></th>
                <th v-if='showCurrent'></th>
            </thead>
            <tbody>
                <UserMatchInfo v-for='match in this.getMatches' :key='match.gamePlayers.player.id' :id='match.gamePlayers.player.id' :showCurrent='showCurrent' :opponent='getName(match.gamePlayers.opponent)' :isOnline='isOnline(match.gamePlayers.opponent)' :state='getStates(match)' />
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
            return (isValid(user)) ? this.getUserName(user.id) : '';
        },
        isOnline: function(user) {
            return (isValid(user)) ? this.userIsOnline(user.id) : false;
        },
        getStates: function(data) {
            let opponent = data.gamePlayers.opponent;
            let player = data.gamePlayers.player;

            return { game: data.state , opponent: (isValid(opponent)) ? opponent.state : '', player: (isValid(player)) ? player.state : '' };
        }
    }
};
</script>