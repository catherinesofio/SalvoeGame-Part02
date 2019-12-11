<template>
    <div v-if='show' class='pop-up'>
        <div id='popup-content'>
            <h1>{{ title }}</h1>
            <p class='description'>{{ details }}</p>
            <router-link :to='direction' class='btn'>
                <span v-on:click='closePopUp'>X</span>
            </router-link>
        </div>
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
        };
    },
    methods: {
        openPopUp: function(data) {
            this.title = data.title;
            this.details = data.details;
            this.direction = data.direction;

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

<style>
.pop-up {
    width: 100vw;
    height: 100vh;
    position: absolute;
    top: 0;
    left: 0;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: var(--layer-popup);
    text-align: center;
    overflow: visible;
}

#popup-content {
    position: absolute;
    width: 50vw;
    top: 50%;
    left: 50%;
    transform: translate(-55%, -55%);
    margin: calc(var(--margin) * 2);
    padding: var(--padding);
    background-color: var(--color-05);
    border-radius: var(--border-radius);
    border-width: var(--border-width);
    border-color: var(--color-04);
    border-style: solid;
    white-space: pre-wrap;
    overflow: visible;
}

#popup-content h1 {
    margin: 0;
}

#popup-content p {
    text-align: justify;
}

#popup-content .btn {
    box-sizing: content-box;
    width: calc(var(--cell-size) * 1.5);
    height: calc(var(--cell-size) * 1.5);
    position: absolute;
    top: -1em;
    right: -1em;
    margin: 0;
    padding: 0;
    border-radius: calc(var(--cell-size) * 2);
    text-align: center; 
}
</style>