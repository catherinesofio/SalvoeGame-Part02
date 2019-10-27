<template>
    <div>
        <table>
            <caption>MATCHES</caption>
            <thead>
                <th>CHALLENGER</th>
                <th>STATE</th>
            </thead>
            <tbody>
                <MatchInfo v-for="match in this.matches" v-bind:key="match.id" :id="match.id" :opponent="getName(match.gamePlayers.opponent)" :isOnline="isOnline(match.gamePlayers.opponent)" :state="getStates(match)" />
            </tbody>
        </table>
        <button v-on:click='this.createMatch'>🗡️ CREATE QUEST 🗡️</button>
    </div>
</template>

<script>
import MatchInfo from '@/components/menu/MatchInfo.vue';
import { mapState, mapGetters, mapActions } from 'vuex';

export default {
    components: {
        MatchInfo
    },
    computed: {
        ...mapState(['matches']),
        ...mapGetters(['getUserName', 'userIsOnline'])
    },
    methods: {
        ...mapActions(['createMatch']),
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