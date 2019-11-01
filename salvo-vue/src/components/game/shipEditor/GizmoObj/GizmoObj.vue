<template>
    <div>
        <GizmoButton type='accept' :callback='' />
        <GizmoButton type='cancel' :callback='' />
        <GizmoButton type='rotate' :callback='rotate' />
        <GizmoButton type='east' :callback='move' />
        <GizmoButton type='west' :callback='move' />
        <GizmoButton type='north' :callback='move' />
        <GizmoButton type='south' :callback='move' />
    </div>
</template>

<script>
import GizmoButton from '@/components/game/shipEditor/gizmoObject/GizmoButton.vue';

export default {
    data: function() {
        return {
            ship: {
                name: '',
                size: '',
                parent: null,
                orientation: '',
                obj: null
            }
        };
    },
    methods: {
        setShip: function(ship) {
            this.ship = ship;
        },
        triggerPlace: function() {

        },
        triggerCancel: function() {

        },
        rotate: function() {
            
        },
        move: function(direction) {
            
        }
    }
};
</script>