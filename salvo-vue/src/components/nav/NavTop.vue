<template>
    <div>
        <button v-on:click='triggerLogout'>🚪</button>
        <span>{{ getName }}</span>
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
        ...mapActions(['logout']),
        triggerLogout: function() {
            this.logout();
        }
    }
};
</script>

<style scoped>
div {
    width: 100vw;
    position: absolute;
    top: 0;
}
</style>