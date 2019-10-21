<template>
    <tr>
        <td>
            <IconOnline v-if='this.showCurrent' v-bind:isOnline='this.isOnline' />
            {{ this.opponent }}
        </td>
        <td>{{ this.state }}</td>
        <td v-if='this.showCurrent'>
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
    methods: {
        ...mapActions(['loadMatch']),
        triggerLoadMatch: function() {
            if (this.showCurrent) {
                this.loadMatch(this.id);
            }
        }
    }
};
</script>