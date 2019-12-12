<template>
    <nav>
        <NavTop :user='user' />
        <NavBottom />
        <!--button id='btn-expand' v-on:click='triggerExpand'>▲</button>
        <button id='btn-hide' v-on:click='triggerHide'>▼</button-->
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

#btn-expand, #btn-hide {
    position: fixed;
    left: 50%;
    margin: 0;
    padding: 0;
    transform: translate(-55%, 55%);
    border-width: 0;
    background-color: rgba(0,0,0,0);
    font-size: calc(var(--text-size-03) * 2);
    color: var(--color-00);
}

#btn-hide {
    bottom: calc(var(--text-size-03) + (var(--padding) * 2.5));
}

#btn-expand {
    bottom: calc((var(--text-size-03) + (var(--padding) * 2.5)));
}
</style>