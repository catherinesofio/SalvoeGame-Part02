<template>
    <button class='btn' :type='type' v-on:click='triggerCallback' isBlocked='false'></button>
</template>

<script>
export default {
    props: ['type', 'callback'],
    methods: {
        triggerCallback: function() {
            this.disable = this.callback(this.type, this.setDisabled);
        }
    }  
};
</script>

<style>
.btn {
    position: absolute;
    width: var(--cell-size);
    height: var(--cell-size);
    border-radius: 50%;
    z-index: var(--layer-foreground-btn);
}

.btn[isBlocked=false] {
    position: absolute;
    display: block;
}

.btn[isBlocked=true] {
    display: none;
}

.btn[type='accept'] {
    bottom: -50%;
    right: 100%;
    background-color: var(--color-08);
}

.btn[type='cancel'] {
    bottom: -50%;
    left: 100%;
    background-color: var(--color-06);
}

.btn[type='rotate'] {
    top: calc(-1 * var(--cell-size));
    right: calc(-1 * var(--cell-size));
}

.btn[type='east'] {
    top: 50%;
    right: calc(-1 * var(--cell-size));
    transform: translateY(-50%);
}

.btn[type='west'] {
    top: 50%;
    left: calc(-1 * var(--cell-size));
    transform: translateY(-50%);
}

.btn[type='north'] {
    top: calc(-1 * var(--cell-size));
    left: 50%;
    transform: translateX(-50%);
}

.btn[type='south'] {
    bottom: calc(-1 * var(--cell-size));
    left: 50%;
    transform: translateX(-50%);
}
</style>