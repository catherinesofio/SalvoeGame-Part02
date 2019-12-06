<template>
    <div>
        <button v-on:click='triggerLogout'>🚪</button>
        <h1>{{ getName }}</h1>
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
    display: flex;
    align-items: center;
    position: absolute;
    top: 0;
    width: 100vw;
    height: var(--spacer-height);
    padding: calc(var(--padding) / 3);
    background-color: var(--color-10);
}
</style>