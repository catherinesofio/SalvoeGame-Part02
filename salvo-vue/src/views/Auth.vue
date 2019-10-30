<template>
    <div class='view'>
        <div class='auth'>
            <button v-on:click='setLogin(true)' :disabled='login'>LOGIN</button>
            <button v-on:click='setLogin(false)' :disabled='!login'>REGISTER</button>
            <LoginForm v-if='login' />
            <RegisterForm v-if='!login' />
        </div>
    </div>
</template>

<script>
import LoginForm from '@/components/auth/LoginForm.vue';
import RegisterForm from '@/components/auth/RegisterForm.vue';

export default {
    name: 'Auth',
    data: function() {
        return {
            login: true
        };
    },
    components: {
        LoginForm,
        RegisterForm
    },
    methods: {
        setLogin: function(value) {
            this.login = value;
        }
    }
};
</script>

<style>
.auth {
    box-sizing: border-box;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: var(--color-01);
}
</style>