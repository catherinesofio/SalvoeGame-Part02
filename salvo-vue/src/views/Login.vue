<template>
    <div>
        <form>
            <input type='email' name='email' v-model='email'>
            <input type='password' name='password' v-model='password'>
            <button type='button' v-on:click="triggerLogin">Login</button>
        </form>
    </div>
</template>

<script>
import { mapActions } from 'vuex';

export default {
    name: 'Login',
    data: function() {
        return {
            email: '',
            password: ''
        }
    },
    methods: {
        ...mapActions(['login']),
        triggerLogin: function() {
            this.login({ email: this.email, password: this.password });
        }
    }
};
</script>