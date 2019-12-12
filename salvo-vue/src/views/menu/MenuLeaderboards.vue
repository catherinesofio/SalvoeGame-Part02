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
                <LeaderboardInfo v-for='(user, index) in this.leaderboards' :key='index' :position='index + 1' :username='getName(user.id)' :points='user.points' :isPlayer='getIsPlayer(user.id)' />
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
        getIsPlayer: function (id) {
            return (this.user.id == id);
        }
    }
};
</script>

<style>
#leaderboards td {
  padding: calc(var(--padding) / 2);
}

#leadearboads tbody tr:first-child td {
  background-color: var(--color-09);
}

tr[size=3] td {
    height: calc(var(--padding) * 4);
    font-size: calc(var(--text-size-03) * 4);
}

tr[size=2], tr[size=2] td {
    height: 10em !important;
    font-size: calc(var(--text-size-03) * 3);
}

tr[size=1] td {
    height: calc(var(--padding) * 2);
    font-size: calc(var(--text-size-03) * 2);
}

tr[isPlayer=true] td {
  background-color: var(--color-10);
}
</style>