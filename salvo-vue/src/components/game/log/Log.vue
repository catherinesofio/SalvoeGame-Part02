<template>
    <li>
        <span>TURN: {{ getTurn }}</span>
        <p>{{ getMessage }}</p>
        <span>{{ getDate }}</span>
    </li>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
    props: ['data', 'gamePlayers'],
    data: function() {
        return {
            user: null
        };
    },
    computed: {
        ...mapGetters(['getUserName']),
        getMessage: function() {
            if (this.data == null) {
                return '';
            }

            let message = this.data.message;
            let params = this.data.params;

            if (params != null) {
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
            return (this.data != null) ? this.data.turn : '';
        },
        getDate: function() {
            return (this.data != null) ? this.data.date : '';
        },
        getUser: function() {

        }
    },
    methods: {
        getPlayerName: function(gp) {
            let id = parseInt(this.gamePlayers.filter(x => x.id == gp)[0].player.id);
            
            return this.getUserName(id);
        }
    }
};
</script>>