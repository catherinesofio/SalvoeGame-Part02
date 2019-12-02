<template>
    <div v-if='show'>
        <h1>{{ title }}</h1>
        <p>{{ details }}</p>
        <router-link :to='direction'>
            <span v-on:click='closePopUp'>{{ button }}</span>
        </router-link>
    </div>
</template>

<script>
import { bus } from '@/main.js';

export default {
    data: function() {
        return {
            show: false,
            title: '',
            details: '',
            direction: '',
            button: ''
        };
    },
    methods: {
        openPopUp: function(data) {
            this.title = data.title;
            this.details = data.details;
            this.direction = data.direction;
            this.button = data.button;

            this.show = true;
        },
        closePopUp: function() {
            this.show = false;
        }
    },
    mounted: function() {
        bus.$on('open-popUp', this.openPopUp);
    },
    beforeDestroy: function() {
        bus.$off('open-popUp', this.openPopUp);
    }
};
</script>