<template>
    <form>
        <fieldset>
            <label for='name'>Name</label>
            <input type='name' name='name' v-model='name'>
        </fieldset>
        <fieldset>
            <label for='email'>E-mail</label>
            <input type='email' name='email' v-model='email'>
        </fieldset>
        <fieldset>
            <label for='password'>Password</label>
            <input type='password' name='password' v-model='password'>
        </fieldset>
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