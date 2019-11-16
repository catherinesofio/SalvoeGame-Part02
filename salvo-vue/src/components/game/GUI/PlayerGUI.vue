<template>
    <div>
        <div>
            <IconOnline :isOnline='getIsOnline' />
            <span>{{ getName }} {{ getState }}</span>
        </div>
        <p>Ships: {{ activeShips }}</p>
    </div>
</template>

<script>
import IconOnline from '@/components/IconOnline.vue';
import { mapGetters } from 'vuex';

export default {
    props: ['id', 'state', 'activeShips'],
    data: function() {
        return {
            states: ['⚓', '💣', '⏳']
        };
    },
    components: {
        IconOnline
    },
    computed: {
        ...mapGetters(['userIsOnline', 'getUserName']),
        getIsOnline: function() {
            return (this.id > -1) ? this.userIsOnline(this.id): false;
        },
        getName: function() {
            return (this.id > -1) ? this.getUserName(this.id): '';
        },
        getState: function() {
            switch(this.state) {
                case 'WAITING_PREPARING':
                    return this.states[0];
                    break;
                case 'PLAYING_TURN':
                    return this.states[1];
                    break;
                default:
                    return this.states[2]
                    break;
            }
        }
    }
};
</script>