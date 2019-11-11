<template>
    <div :class='getClass' :type='type' :size='size' orientation='horizontal' isPositioned='false' v-on:click='triggerSelect'></div>
</template>

<script>
export default {
    props: ['type', 'size', 'callback'],
    data: function() {
        return {
            obj: null
        };
    },
    computed: {
        getClass() {
            return 'ship ' + this.type.toLowerCase();
        }
    },
    methods: {
        triggerSelect: function() {
            this.callback(this.obj);
        }
    },
    mounted: function() {
        this.obj = document.getElementsByClassName(this.type.toLowerCase())[0];
    }
};
</script>