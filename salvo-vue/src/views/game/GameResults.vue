<template>
    <div>
        <h1>RESULTS</h1>
    </div>
</template>

<script>
import { bus } from '@/main.js';

export default {
    props: ['gp', 'data'],
    mounted: function() {
        bus.$emit('trigger-data-reload', true);
    }
};
</script>