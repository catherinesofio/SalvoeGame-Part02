<template>
    <div :class='getClass' :size='size' orientation='horizontal' isPositioned='false' v-on:click='triggerSelect'></div>
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

<style>
.ship {
    position: relative;
    background-color: gainsboro;
    z-index: var(--layer-foreground);
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