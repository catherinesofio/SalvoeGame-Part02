<template>
    <div class='view-panel'>
        <h1>MATCHES</h1>
        <button v-on:click='this.createMatch'>🐾 CREATE MATCH 🐾</button>
        <table class='custom-table'>
            <thead>
                <th>CHALLENGER</th>
                <th>
                    <button class='help' v-on:click='triggerIconHelp'>?</button>
                </th>
            </thead>
            <tbody>
                <MatchInfo v-for="match in this.matches" :key="match.id" :id="match.id" :opponent="getName(match.gamePlayers.opponent)" :isOnline="isOnline(match.gamePlayers.opponent)" :state="getStates(match)" />
            </tbody>
            <tfoot>
                <td></td>
                <td></td>
            </tfoot>
        </table>
    </div>
</template>

<script>
import MatchInfo from '@/components/menu/MatchInfo.vue';
import { mapState, mapGetters, mapActions } from 'vuex';
import { bus } from '@/main.js';

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
            return (isValid(user)) ? this.getUserName(user.id) : '';
        },
        isOnline: function(user) {
            return (isValid(user)) ? this.userIsOnline(user.id) : false;
        },
        getStates: function(data) {
            let opponent = data.gamePlayers.opponent;
            let player = data.gamePlayers.player;

            return { game: data.state , opponent: (isValid(opponent)) ? opponent.state : '', player: (isValid(player)) ? player.state : '' };
        },
        triggerIconHelp: function() {
            bus.$emit('open-popUp', {
                title: 'Icon Guide',
                details: "🕒: waiting for player to join the fun!\n\n🐈: player is still placing their kittens.\n\n💖: player is sending love to the opponents' kittens.\n\n⏳: player is waiting for it's opponent to send the love back.",
                direction: ''
            });
        }
    }
};
</script>