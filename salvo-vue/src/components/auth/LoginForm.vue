<template>
    <form>
        <fieldset>
            <label for='email'>E-mail</label>
            <input type='email' name='email' v-model='email'>
        </fieldset>
        <fieldset>
            <label for='password'>Password</label>
            <input type='password' name='password' v-model='password'>
        </fieldset>
        <button type='button' v-on:click="triggerLogin">Login</button>
    </form>
</template>

<script>
import { mapActions } from 'vuex';

export default {
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