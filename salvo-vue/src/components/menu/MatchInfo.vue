<template>
    <tr>
        <td class='td-join'>
            <IconOnline :isOnline="this.isOnline" />
            <p>{{ this.opponent }}</p>
        </td>
        <td>
            <p>OPP: {{ this.getPlayerState('opponent') }}</p>
            <p>YOU: {{ this.getPlayerState('player') }}</p>
        </td>
        <td>
            <button v-on:click='triggerJoinMatch' class='fixed'>🗡️</button>
        </td>
    </tr>
</template>

<script>
import IconOnline from '@/components/IconOnline.vue';
import { mapActions } from 'vuex';

export default {
    props: ['id', 'opponent', 'isOnline', 'state'],
    components: {
        IconOnline
    },
    methods: {
        ...mapActions(['joinMatch']),
        triggerJoinMatch: function() {
            this.joinMatch(this.id);
        },
        getPlayerState(player) {
            let state = (player == 'opponent') ? this.state.opponent : this.state.player;
            
            switch (state) {
                case 'WAITING_PREPARING':
                    return '⚓';
                    break;
                case 'PLAYING_WAITING':
                    return '⏳';
                    break;
                case 'PLAYING_TURN':
                    return '💣';
                    break;
                default:
                    return '🕒';
                    break;
            }
        }
    }
};
</script>