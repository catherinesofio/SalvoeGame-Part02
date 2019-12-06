<template>
    <nav>
        <NavTop :user='user' />
        <NavBottom />
    </nav>
</template>

<script>
import NavTop from '@/components/nav/NavTop.vue';
import NavBottom from '@/components/nav/NavBottom.vue';

export default {
    props: ['user'],
    components: {
        NavTop,
        NavBottom
    }
};
</script>

<style>
nav * {
    z-index: var(--layer-ui);
}

nav {
    position: absolute;
    width: 100%;
    height: 100%;
    z-index: var(--layer-middle);
}
</style>