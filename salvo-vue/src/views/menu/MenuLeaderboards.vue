<template>
    <div class='view-panel'>
        <h1>LEADERBOARDS</h1>
        <table id='leadearboads' class='custom-table'>
            <thead>
                <th>RANKING</th>
                <th>USER</th>
                <th>POINTS</th>
            </thead>
            <tbody>
                <LeaderboardInfo v-for='(user, index) in this.leaderboards' :key='index' :position='index + 1' :username='getName(index + 1)' :points='user.points' />
            </tbody>
        </table>
    </div>
</template>

<script>
import LeaderboardInfo from '@/components/menu/LeaderboardInfo.vue';
import { mapState, mapGetters } from 'vuex';

export default {
    components: {
        LeaderboardInfo
    },
    computed: {
        ...mapState(['leaderboards']),
        ...mapGetters(['getUserName'])
    },
    methods: {
        getName: function (id) {
            return this.getUserName(id);
        }
    }
};
</script>