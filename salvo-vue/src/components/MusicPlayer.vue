<template>
    <audio autoplay loop>
        <source :src="music" type="audio/mp3">
    </audio>
</template>

<script>
import theme from '@/assets/theme.mp3';

export default {
    data: function() {
        return {
            music: theme
        };
    }
};
</script>