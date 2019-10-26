<template>
    <div>
        <div>
            <button v-on:click='setShowCurrent(true)'>CURRENT</button>
            <button v-on:click='setShowCurrent(false)'>HISTORY</button>
        </div>
        <table>
            <caption>MY QUESTS</caption>
            <thead>
                <th>CHALLENGER</th>
                <th>STATUS</th>
            </thead>
            <tbody>
                <UserQuestInfo v-for='quest in this.getQuests' v-bind:key='quest.id' :showCurrent='showCurrent' :opponent='quest.opponent' :isOnline='quest.isOnline' :state='quest.state' />
            </tbody>
        </table>
    </div>
</template>

<script>
import UserQuestInfo from '@/components/menu/UserQuestInfo.vue';
import { mapState } from 'vuex';

export default {
    data: function() {
        return {
            showCurrent: true
        }
    },
    components: {
        UserQuestInfo
    },
    computed: {
        ...mapState(['userMatches']),
        getQuests() {
            return (this.showCurrent) ? this.userQuests.current : this.userQuests.history;
        }
    },
    methods: {
        setShowCurrent(value) {
            this.showCurrent = value;
        }
    }
};
</script>