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
                <LeaderboardInfo v-for='(user, index) in this.leaderboards' :key='index' :position='index + 1' :username='getName(index + 1)' :points='user.points' :id='getId(user.id)' />
            </tbody>
            <tfoot>
                <td></td>
                <td></td>
                <td></td>
            </tfoot>
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
        ...mapState(['user', 'leaderboards']),
        ...mapGetters(['getUserName'])
    },
    methods: {
        getName: function (id) {
            return this.getUserName(id);
        },
        getId: function (id) {
            return (this.user.id == id) ? 'player' : false;
        }
    }
};
</script>