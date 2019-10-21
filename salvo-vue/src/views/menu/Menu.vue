<template>
    <div>
        <Nav :user="user" />
        <Spacer />
        <router-view/>
    </div>
</template>

<script>
import Nav from '@/components/nav/Nav.vue';
import Spacer from '@/components/Spacer.vue';
import { mapState } from 'vuex';

export default {
    name: 'Menu',
    components: {
        Nav,
        Spacer
    },
    computed: {
        ...mapState(['user']),
    }
};
</script>