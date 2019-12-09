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
    position: absolute;
    align-items: baseline;
    bottom: calc(var(--text-size-03) + (var(--padding) * 2));
    text-align: center;
}

div, div * {
    z-index: var(--layer-ui) !important;
}
</style>