<template>
    <div>
    </div>
</template>

<script>
export default {
    data: function() {
        return {
            ship: {
                name: '',
                size: '',
                orientation: ''
            }
        };
    },
    methods: {
        setShip: function(ship) {
            this.ship = ship;
        },
        move: function(direction) {
            
        }
    }
};
</script>