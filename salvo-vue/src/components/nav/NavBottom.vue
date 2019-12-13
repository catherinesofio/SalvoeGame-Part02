<template>
    <div>
        <button>
            <router-link to='/menu/matches'>📜</router-link>
        </button>
        <button>
            <router-link to='/menu/my-matches'>⚔️</router-link>
        </button>
        <button>
            <router-link to='/menu/leaderboards'>🏆</router-link>
        </button>
    </div>
</template>

<style scoped>
div {
    width: 100vw;
    position: fixed;
    align-items: baseline;
    top: calc(65vh + (var(--spacer-height) * 1.15) + (var(--margin) * 2));
    text-align: center;
}

div, div * {
    z-index: var(--layer-ui) !important;
}
</style>