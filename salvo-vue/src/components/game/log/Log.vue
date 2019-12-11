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
            if (!isValid(this.data.date)) {
                return '';
            }

            let date = new Date(this.data.date);

            return date.getDay() + '/' + date.getMonth() + '/' + date.getFullYear();
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
    border-radius: var(--border-radius);
    border-width: calc(var(--border-width) / 2);
    border-color: var(--color-00);
    border-style: solid;
    text-align: justify;
}

.log span:last-of-type {
    position: absolute;
    width: calc(100% - (var(--padding) * 3.5));
    text-align: right;
    left: calc(var(--padding) * 2);
}
</style>