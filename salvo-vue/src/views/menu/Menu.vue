<template>
    <div>
        <Nav :user="user" />
        <Spacer />
        <router-view />
    </div>
</template>

<script>
import Nav from '@/components/nav/Nav.vue';
import Spacer from '@/components/Spacer.vue';
import { mapState } from 'vuex';

export default {
    name: 'Menu',
    data: function() {
        return {
            interval,
            time: 2500
        }
    },
    components: {
        Nav,
        Spacer
    },
    computed: {
        ...mapState(['user']),
    },
    methods: {
        updateInfo: function() {
            this.$store.commit('UPDATE_INFO'); 

            this.interval = setInterval(this.updateInfo, this.time);
        }
    }
    mounted: function() {
        this.interval = setInterval(this.updateInfo, this.time);
    },
    beforeDestroy: function() {
        clearInterval(this.interval);
    }
};
</script>