<template>
    <div>
        <h1>{{ getName }}</h1>
        <p>🏵️ #1</p>
        <button v-on:click='logout'>🚪</button>
    </div>
</template>

<script>
import { mapActions } from 'vuex';

export default {
    props: ['user'],
    computed: {
        getName: function() {
            return (this.user == null) ? "" : this.user.name;
        }
    },
    methods: {
        ...mapActions(['logout'])
    }
}
</script>>