<template>
    <tr :state='getState'>
        <td class='td-join' v-if='showCurrent'>
           <IconOnline v-if='this.showCurrent && opponentExist' v-bind:isOnline='this.isOnline' />
            <p>{{ this.getPlayerState('opponent') }} {{ this.getOpponentName }}</p>
        </td>
        <td v-if='showCurrent'>
            <p>{{ this.getPlayerState('player') }} You</p>
        </td>
        <td v-if='showCurrent'>
            <button v-on:click='triggerLoadMatch'>🐾</button>
        </td>
        <td class='td-join' v-if='!showCurrent'>
            <p>{{ this.getPlayerState('opponent') }} {{ this.getOpponentName }}</p>
        </td>
        <td v-if='!showCurrent'>
            <p>{{ this.getPlayerState('player') }} You</p>
        </td>
    </tr>
</template>

<script>
import IconOnline from '@/components/IconOnline.vue';
import { mapActions } from 'vuex';

export default {
    props: ['showCurrent', 'id', 'opponent', 'isOnline', 'state'],
    components: {
        IconOnline
    },
    computed: {
        opponentExist: function() {
            return this.opponent != '';
        },
        getOpponentName: function() {
            return (this.opponent != '') ? this.opponent : 'Waiting for player...';
        },
        getState: function() {
            if (this.showCurrent) {
                return false;
            }
            
            let state = this.state.player;
            
            switch (state) {
                case 'FINISHED_WON':
                    return 'won';
                    break;
                case 'FINISHED_LOST':
                    return 'lost';
                    break;
                case 'FINISHED_TIED':
                    return 'tied';
                    break;
            }
        }
    },
    methods: {
        ...mapActions(['loadMatch']),
        triggerLoadMatch: function() {
            this.loadMatch(this.id);
        },
        getPlayerState(player) {
            let state = (player == 'opponent') ? this.state.opponent : this.state.player;
            
            switch (state) {
                case 'WAITING_PREPARING':
                    return '🐈';
                    break;
                case 'PLAYING_WAITING':
                    return '⏳';
                    break;
                case 'PLAYING_TURN':
                    return '💖';
                    break;
                case 'FINISHED_WON':
                    return '🏆';
                    break;
                case 'FINISHED_LOST':
                    return '🥈';
                    break;
                case 'FINISHED_TIED':
                    return '🏳️';
                    break;
                default:
                    return '🕒';
                    break;
            }
        }
    }
};
</script>