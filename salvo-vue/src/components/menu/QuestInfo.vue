<template>
    <tr>
        <td>
            <IconOnline :isOnline="this.isOnline" />
            <h2>{{ this.opponent }}</h2>
        </td>
        <td>
            <button v-on:click='triggerJoinMatch'>🗡️</button>
        </td>
    </tr>
</template>

<script>
import IconOnline from '@/components/IconOnline.vue';
import { mapActions } from 'vuex';

export default {
    props: ['id', 'opponent', 'isOnline'],
    components: {
        IconOnline
    },
    methods: {
        ...mapActions(['joinMatch']),
        triggerJoinMatch: function() {
            this.joinMatch(this.id);
        }
    }
};
</script>