<template>
    <tr>
        <td class='td-join'>
            <IconOnline v-if='this.showCurrent && opponentExist' v-bind:isOnline='this.isOnline' />
            <p>{{ this.opponent }}</p>
        </td>
        <td>
            <p>OPP: {{ this.getPlayerState('opponent') }}</p>
            <p>YOU: {{ this.getPlayerState('player') }}</p>
        </td>
        <td v-if='showCurrent'>
            <button v-on:click='triggerLoadMatch'>🗡️</button>
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