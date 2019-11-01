<template>
    <button class='btn' :type='type' v-on:click='triggerCallback' :disabled='disable'></button>
</template>

<script>
export default {
    props: ['type', 'callback'],
    data: function() {
        return {
            disable: true
        };
    },
    methods: {
        triggerCallback: function() {
            this.disable = this.callback(this.type);
        }
    }  
};
</script>

<style>
.btn {
    display: block;
    position: absolute;
    width: calc(var(--cell-size) / 2);
    height: calc(var(--cell-size / 2));
}

.btn[disabled] {
    display: none;
}

.btn[type='accept'] {
    bottom: -50%;
    right: 100%;
    top: calc(-1 * var(--cell-size));;
    right: calc(-1 * var(--cell-size));;
}

.btn[type='cancel'] {
    bottom: -50%;
    left: 100%;
    top: calc(-1 * var(--cell-size));;
    right: calc(-1 * var(--cell-size));;
}

.btn[type='rotate'] {
    top: calc(-1 * var(--cell-size));;
    right: calc(-1 * var(--cell-size));;
}

.btn[type='east'] {
    top: 50%;
    left: calc(-1 * var(--cell-size));
    transform: translateY(-50%);
    background-color: greenyellow;
}

.btn[type='west'] {
    top: 50%;
    left: calc(-1 * var(--cell-size));
    transform: translateY(-50%);
    background-color: green;
}

.btn[type='north'] {
    top: calc(-1 * var(--cell-size));
    left: 50%;
    transform: translateX(-50%);
    background-color: blue;
}

.btn[type='south'] {
    top: calc(-1 * var(--cell-size));
    left: 50%;
    transform: translateX(-50%);
    background-color: navy;
}
</style>