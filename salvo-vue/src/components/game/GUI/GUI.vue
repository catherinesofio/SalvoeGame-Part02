<template>
    <ul>
        <PlayerGUI :id='player.id' :state='player.state' :activeShips='player.activeShips' :isPlayer='true' />
        <PlayerGUI v-if='opponentExists' :id='opponent.id' :state='opponent.state' :activeShips='opponent.activeShips' :isPlayer='false' />
        <p v-if='!opponentExists'>Waiting for player...</p>
        <SalvoesManager :isTurn='isTurn' :gp='gp' :turn='turn' />
    </ul>
</template>

<script>
import PlayerGUI from '@/components/game/GUI/PlayerGUI.vue';
import SalvoesManager from '@/components/game/GUI/SalvoesManager.vue';
import { mapActions } from 'vuex';

export default {
    props: ['gp', 'turn', 'player', 'opponent'],
    components: {
        PlayerGUI,
        SalvoesManager
    },
    computed: {
        isTurn: function() {
            return this.player.state == 'PLAYING_TURN';
        },
        opponentExists: function() {
            return this.opponent.id > -1;
        }
    }
};
</script>