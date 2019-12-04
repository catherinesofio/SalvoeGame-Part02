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
    top: 45%;
    left: 50%;
    padding: calc(var(--padding) / 1.25);
    transform: translate(-50%, -50%);
    background-image: var(--background-panel);
    border-radius: var(--border-radius);
    box-shadow: var(--box-shadow);
    border-style: solid;
    border-color: var(--color-02);
    border-width: var(--border-width);
}

.auth fieldset {
    margin-top: var(--margin);
    margin-bottom: var(--margin);
    background-color: var(--color-03);
    border-radius: var(--border-radius);   
}

.auth label, .auth input {
    text-transform: lowercase;
    padding-left: calc(var(--padding) / 4);
    padding-right: calc(var(--padding) / 4);
}

.auth input {
    border-top-right-radius: var(--border-radius);
    border-bottom-right-radius: var(--border-radius);
}
</style>