<template>
    <li class='log'>
        <span>TURN: {{ getTurn }}</span>
        <p>{{ getMessage }}</p>
        <span>{{ getDate }}</span>
    </li>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
    props: ['data', 'gamePlayers'],
    computed: {
        ...mapGetters(['getUserName']),
        getMessage: function() {
            if (!isValid(this.data)) {
                return '';
            }

            let message = this.data.message;
            let params = this.data.params;

            if (isValid(params)) {
                if (params.length > 1) {
                    params = params.join(', ');
                } else if (params.length == 1) {
                    params = params[0];
                }

                message = message.replace('{st}', params);
                message = message.replace('{ss}', params);
            }
            
            message = message.replace('{gp}', this.getPlayerName(this.data.gamePlayerId));
            
            return message;
        },
        getTurn: function() {
            return (isValid(this.data)) ? this.data.turn : '';
        },
        getDate: function() {
            return (isValid(this.data)) ? this.data.date : '';
        }
    },
    methods: {
        getPlayerName: function(gp) {
            let id = parseInt(this.gamePlayers.filter(x => x.id == gp)[0].player.id);
            
            return this.getUserName(id);
        }
    }
};
</script>

<style>
.log {
    padding: var(--padding);
    background-color: var(--color-06);
}
</style>