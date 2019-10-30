<template>
    <button v-if='show' v-bind:id='name' v-bind:class='getClass' v-on:click='triggerCallback'></button>
</template>

<script>
export default {
    props: ['name', 'type', 'callback'],
    data: function() {
        return {
            show: true
        };
    },
    computed: {
        getClass: function() {
            return 'btn-' + type;
        }
    },
    methods: {
        triggerCallback: function() {
            this.show = this.callback(this.name);
        }
    }  
};
</script>