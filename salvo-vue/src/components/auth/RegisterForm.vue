<template>
    <form>
        <input type='name' name='name' v-model='name'>
        <input type='email' name='email' v-model='email'>
        <input type='password' name='password' v-model='password'>
        <button type='button' v-on:click="triggerRegister">Register</button>
    </form>
</template>

<script>
import { mapActions } from 'vuex';

export default {
    data: function() {
        return {
            name: '',
            email: '',
            password: ''
        };
    },
    methods: {
        ...mapActions(['register']),
        triggerRegister: function() {
            this.register({ name: this.name, email: this.email, password: this.password });
        }
    }
};
</script>