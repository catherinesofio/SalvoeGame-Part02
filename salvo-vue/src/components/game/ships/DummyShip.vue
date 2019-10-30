<template>
    <div v-bind:class='getClass' v-bind:isPositioned='isPositioned' v-bind:orientation='orientation'></div>
</template>

<script>
export default {
    props: ['name', 'size'],
    data: function() {
        return {
            orientation: 'horizontal',
            isPositioned: false,
            cell: ''
        };
    },
    computed: {
        getClass() {
            return 'ship ' + this.name;
        }
    },
    methods: {
        setShipData: function({ isPositioned, orientation, cell }) {
            this.isPositioned = isPositioned;
            this.orientation = orientation;
            this.cell = cell;
        },
        getShipData: function() {
            return { size: this.size, orientation: this.orientation, isPositioned: this.isPositioned, cell: this.cell };
        }
    }
};
</script>