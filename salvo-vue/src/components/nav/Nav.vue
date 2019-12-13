<template>
    <nav>
        <NavTop :user='user' />
        <NavBottom v-if='show' />
        <button id='btn-expand' v-if='!show' v-on:click='setShow(true)'>▲</button>
        <button id='btn-hide' v-if='show' v-on:click='setShow(false)'>▼</button>
    </nav>
</template>

<script>
import NavTop from '@/components/nav/NavTop.vue';
import NavBottom from '@/components/nav/NavBottom.vue';
import { map } from '@/main.js';

export default {
    props: ['user'],
    data: function() {
        return {
            show: true
        };
    },
    components: {
        NavTop,
        NavBottom
    },
    methods: {
        setShow: function(value) {
            this.show = value
        }
    }
};
</script>

<style>
nav * {
    z-index: var(--layer-ui);
}

#btn-expand, #btn-hide {
    position: absolute;
    top: calc(65vh + (var(--spacer-height) * 1.15) + (var(--margin) * 2) + 3.5em);
    right: 50%;
    transform: translateX(50%);
    margin: 0;
    padding: calc(var(--padding) / 2);
}

#btn-expand {
    top: calc(65vh + (var(--spacer-height) * 1.75) + (var(--margin) * 2));
}
</style>