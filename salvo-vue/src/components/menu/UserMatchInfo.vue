<template>
    <tr>
        <td class='td-join'>
           <IconOnline v-if='this.showCurrent && opponentExist' v-bind:isOnline='this.isOnline' />
            <p>{{ this.getPlayerState('opponent') }} {{ this.getOpponentName }}</p>
        </td>
        <td>
            <p>{{ this.getPlayerState('player') }} You</p>
        </td>
        <td v-if='showCurrent'>
            <button v-on:click='triggerLoadMatch'>🐾</button>
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
        getOpponentName() {
            return (this.opponent != '') ? this.opponent : 'Waiting for player...';
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
                default:
                    return '🕒';
                    break;
            }
        }
    }
};
</script>