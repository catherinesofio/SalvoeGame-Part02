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
        setIsLoading: function(value) {
            this.isLoading = value;
        }
    },
    mounted: function() {
        bus.$on('start-load', () => { this.setIsLoading(true); });
        bus.$on('end-load', () => { this.setIsLoading(false); });
    }
};
</script>