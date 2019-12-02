<template>
    <div :class='getClass' :type='type' :size='size' :orientation='getOrientation' :isDown='isDown'></div>
</template>

<script>
export default {
    props: ['id', 'type', 'size', 'locations', 'isDown'],
    computed: {
        getClass: function() {
            return 'ship ' + this.type;
        },
        getOrientation: function() {
            let a = this.locations[0].charAt(0);
            let b = this.locations[1].charAt(0);
            
            return (a == b) ? 'horizontal' : 'vertical';
        }
    },
    mounted: function() {
        this.locations = this.locations.sort();
        document.getElementById(this.id + '-' + this.locations[0]).appendChild(this.$el);
    }
};
</script>