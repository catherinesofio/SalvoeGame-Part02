<template>
    <div>
        <table>
            <caption>QUESTS</caption>
            <thead>
                <th>CHALLENGER</th>
            </thead>
            <tbody>
                <QuestInfo v-for="quest in this.quests" v-bind:key="quest.id" :id="quest.id" :opponent="quest.opponent" :isOnline="quest.isOnline" />
            </tbody>
        </table>
        <button v-on:click='this.createMatch'>🗡️ CREATE QUEST 🗡️</button>
    </div>
</template>2

<script>
import QuestInfo from '@/components/menu/QuestInfo.vue';
import { mapState, mapActions } from 'vuex';

export default {
    components: {
        QuestInfo
    },
    computed: {
        ...mapState(['quests'])
    },
    methods: {
        ...mapActions(['createMatch'])
    }
};
</script>