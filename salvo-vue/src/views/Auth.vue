<template>
    <div>
        <div v-if='login'>
            <button v-on:click='setLogin(false)'>REGISTER</button>
            <LoginForm />
        </div>
        <div v-if='!login'>
            <button v-on:click='setLogin(true)'>LOGIN</button>
            <RegisterForm />
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