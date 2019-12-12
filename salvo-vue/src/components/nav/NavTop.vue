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
    box-sizing: border-box;
    display: flex;
    align-items: center;
    position: fixed;
    top: 0;
    width: 100vw;
    padding: calc(var(--padding) / 3);
    background-color: var(--color-10);
    border-width: 0;
    border-bottom-width: var(--border-width);
    border-style: solid;
    border-color: var(--color-00);
}

@media (orientation: landscape) {
    div {
        height: calc(var(--spacer-height) * 2);
    }
}

@media (orientation: portrait) {
    div {
        height: var(--spacer-height);
    }
}
</style>