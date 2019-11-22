<template>
    <div>
        <PlayerGUI :id='player.id' :state='player.state' :activeShips='player.activeShips' />
        <PlayerGUI v-if='opponentExists' :id='opponent.id' :state='opponent.state' :activeShips='opponent.activeShips' />
        <p v-if='!opponentExists'>Waiting for player...</p>
    </div>
</template>

<script>
import PlayerGUI from '@/components/game/GUI/PlayerGUI.vue';
import { mapActions } from 'vuex';

export default {
    props: ['player', 'opponent'],
    data: function() {
        return {
            salvoesTemplate: 0,
            currSalvoes: -1
        };
    },
    components: {
        PlayerGUI
    },
    computed: {
        opponentExists: function() {
            return this.opponent.id > -1;
        }
    },
    methods: {
        ...mapActions(['getSalvoesTemplate']),
        setSalvoes: function(salvoes) {
            this.salvoesTemplate = salvoes;
        }
    },
    mounted: function() {
        this.getSalvoesTemplate(this.setSalvoes);
    }
};
</script>