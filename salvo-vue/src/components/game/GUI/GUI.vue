<template>
    <div>
        <PlayerGUI :id='player.id' :state='player.state' :activeShips='player.activeShips' />
        <PlayerGUI v-if='opponentExists' :id='opponent.id' :state='opponent.state' :activeShips='opponent.activeShips' />
        <p v-if='!opponentExists'>Waiting for player...</p>
    </div>
</template>

<script>
import PlayerGUI from '@/components/game/GUI/PlayerGUI.vue';

export default {
    props: ['player', 'opponent'],
    components: {
        PlayerGUI
    },
    computed: {
        opponentExists: function() {
            return this.opponent.id > -1;
        }
    }
};
</script>