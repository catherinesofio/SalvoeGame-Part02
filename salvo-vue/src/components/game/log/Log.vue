<template>
    <li>
        <span>{{ getTurn }}</span>
        <p>{{ getMessage }}</p>
        <span>{{ getDate }}</span>
    </li>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
    props: ['data'],
    computed: {
        getMessage: function() {
            if (this.data == null) {
                return '';
            }

            let message = data.message;
            let params = data.params;

            if (params != null) {
                if (params.length > 1) {
                    params = params.join(', ');
                } else if (params.length == 1) {
                    params = params[0];
                }

                message = message.replace('{st}', params);
                message = message.replace('{ss}', params);
            }

            message = message.replace('{gp}', this.getUserName(data.gamePlayerId));
            
            return message;
        },
        getTurn: function() {
            return (this.data != null) ? this.data.turn : '';
        },
        getDate: function() {
            return (this.data != null) ? this.data.date : '';
        }
    },
    methods: {
        ...mapGetters(['getUserName'])
    }
};
</script>>