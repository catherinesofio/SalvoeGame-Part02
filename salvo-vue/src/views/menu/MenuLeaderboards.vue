<template>
    <div>
        <table>
            <caption>LEADERBOARDS</caption>
            <thead>
                <th>RANKING</th>
                <th>USER</th>
                <th>POINTS</th>
            </thead>
            <tbody>
                <LeaderboardInfo v-for='user in this.leaderboards' v-bind:key='user.username' :position='user.position' :username='user.username' :points='user.points' />
            </tbody>
        </table>
    </div>
</template>

<script>
import LeaderboardInfo from '@/components/menu/LeaderboardInfo.vue';
import { mapState } from 'vuex';

export default {
    components: {
        LeaderboardInfo
    },
    computed: {
        ...mapState(['leaderboards'])
    }
};
</script>