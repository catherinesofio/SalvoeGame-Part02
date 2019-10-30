<template>
    <div class='view'>
        <Nav :user='this.user' />
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
            interval: null,
            time: 30000
        };
    },
    components: {
        Nav,
        Spacer
    },
    computed: {
        ...mapState(['user']),
        userExists: function() {
            return this.user != null;
        }
    },
    methods: {
        updateInfo: function() {
            if (this.userExists) {
                this.$store.commit('UPDATE_INFO');

                clearInterval(this.interval);
                this.interval = setInterval(this.updateInfo, this.time);
            }
        }
    },
    mounted: function() {
        this.updateInfo();
    },
    beforeDestroy: function() {
        clearInterval(this.interval);
    }
};
</script>