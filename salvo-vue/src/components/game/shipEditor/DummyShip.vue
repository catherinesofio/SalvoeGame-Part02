<template>
    <div :class='getClass' :isPositioned='isPositioned' :size='size' :orientation='orientation' v-on:click='select'></div>
</template>

<script>
export default {
    props: ['type', 'size'],
    data: function() {
        return {
            orientation: 'horizontal',
            isPositioned: false,
            cell: ''
        };
    },
    computed: {
        getClass() {
            return 'ship ' + this.type.toLowerCase();
        }
    },
    methods: {
        select: function() {
            console.log("selected askjdhkas");
        },
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

<style>
.ship {
    position: relative;
    background-color: gainsboro;
}

.ship[size='2'][orientation='vertical'] {
    width: var(--cell-size);
    height: calc(2 * var(--cell-size));
    padding: 0.25em;
    padding-bottom: 0.3em;
    padding-top: 0.3em;
}

.ship[size='2'][orientation='horizontal'] {
    width: calc(2 * var(--cell-size));
    height: var(--cell-size);
    padding: 0.25em;
    padding-right: 0.3em;
    padding-left: 0.3em;
}

.ship[size='3'][orientation='vertical'] {
    width: var(--cell-size);
    height: calc(3 * var(--cell-size));
    padding: 0.25em;
    padding-bottom: 0.3em;
    padding-top: 0.3em;
}

.ship[size='3'][orientation='horizontal'] {
    width: calc(3 * var(--cell-size));
    height: var(--cell-size);
    padding: 0.25em;
    padding-right: 0.3em;
    padding-left: 0.3em;
}

.ship[size='4'][orientation='vertical'] {
    width: var(--cell-size);
    height: calc(4 * var(--cell-size));
    padding: 0.25em;
    padding-bottom: 0.3em;
    padding-top: 0.3em;
}

.ship[size='4'][orientation='horizontal'] {
    width: calc(4 * var(--cell-size));
    height: var(--cell-size);
    padding: 0.25em;
    padding-right: 0.3em;
    padding-left: 0.3em;
}

.ship[size='5'][orientation='vertical'] {
    width: var(--cell-size);
    height: calc(5 * var(--cell-size));
    padding: 0.25em;
    padding-bottom: 0.3em;
    padding-top: 0.3em;
}

.ship[size='5'][orientation='horizontal'] {
    width: calc(5 * var(--cell-size));
    height: var(--cell-size);
    padding: 0.25em;
    padding-right: 0.3em;
    padding-left: 0.3em;
}
</style>