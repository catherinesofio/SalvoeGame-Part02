<template>
    <div v-if='isLoading'>
        <h1>L O A D I N G</h1>
    </div>    
</template>

<script>
import { bus } from '@/main.js';

export default {
    data: function() {
        return {
            isLoading: false
        };
    },
    methods: {
        showLoading: function() {
            this.isLoading = true;
        },
        hideLoading: function() {
            this.isLoading = false;
        }
    },
    mounted: function() {
        bus.$on('start-load', this.showLoading);
        bus.$on('end-load', this.hideLoading);
    },
    beforeDestroy: function() {
        bus.$off('start-load', this.showLoading);
        bus.$off('end-load', this.hideLoading);
    }
};
</script>