<template>
    <div class='view-panel'>
        <h1>MY MATCHES</h1>
        <div>
            <button id='btn-current' v-on:click='setShowCurrent(true)'>CURRENT</button>
            <button id='btn-history' v-on:click='setShowCurrent(false)'>HISTORY</button>
        </div>
        <table class='custom-table'>
            <thead>
                <th>CHALLENGER</th>
                <th><span>YOU</span></th>
                <th v-if='showCurrent'>
                    <button class='help' v-on:click='triggerIconHelp'>?</button>
                </th>
            </thead>
            <tbody>
                <UserMatchInfo v-for='match in this.getMatches' :key='match.gamePlayers.player.id' :id='match.gamePlayers.player.id' :showCurrent='showCurrent' :opponent='getName(match.gamePlayers.opponent)' :isOnline='isOnline(match.gamePlayers.opponent)' :state='getStates(match)' />
            </tbody>
            <tfoot>
                <td></td>
                <td></td>
                <td v-if='showCurrent'></td>
            </tfoot>
        </table>
    </div>
</template>

<script>
import UserMatchInfo from '@/components/menu/UserMatchInfo.vue';
import { mapState, mapGetters } from 'vuex';
import { bus } from '@/main.js';

export default {
    data: function() {
        return {
            showCurrent: true,
            btnCurrent: null,
            btnHistory: null
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

            if (value) {
                select(this.btnCurrent);
                unselect(this.btnHistory);
            } else {
                select(this.btnHistory);
                unselect(this.btnCurrent);
            }
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
        },
        triggerIconHelp: function() {
            bus.$emit('open-popUp', {
                title: 'Icon Guide',
                details: "🕒: waiting for player to join the fun!\n\n🐈: player is still placing their kittens.\n\n💖: player is sending love to the opponents' kittens.\n\n⏳: player is waiting for it's opponent to send the love back.",
                direction: ''
            });
        }
    },
    mounted: function() {
        this.btnCurrent = this.$el.querySelector('#btn-current');
        this.btnHistory = this.$el.querySelector('#btn-history');

        select(this.btnCurrent);
    }
};
</script>