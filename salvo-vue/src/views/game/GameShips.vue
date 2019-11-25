<template>
    <div>
        <GizmoObject :tableId='id' :ship='ship' :callback='unselectShip' v-show='showGizmo' />
        <DummyShipContainer :callback='selectShip' />
        <Grid :id='id' :occupiedCells='[]' :isTurn='false' />
        <button v-on:click='triggerSubmit' :disabled='!allShipsPlaced'>SUBMIT</button>
    </div>
</template>

<script>
import Grid from '@/components/game/grid/GridSimple.vue';
import GizmoObject from '@/components/game/shipEditor/gizmoObject/GizmoObj.vue';
import DummyShipContainer from '@/components/game/shipEditor/DummyShipContainer.vue';
import { mapState, mapActions } from 'vuex';
//import { log } from 'util';

export default {
    data: function() {
        return {
            id: 'player',
            ship: null,
            ships: [],
            showGizmo: false,
            allShipsPlaced: false
        };
    },
    components: {
        Grid,
        GizmoObject,
        DummyShipContainer
    },
    computed: {
        ...mapState(['gridHeaders', 'gridHeadersY'])
    },
    methods: {
        ...mapActions(['setShips']),
        triggerSubmit: function() {
            if (this.allShipsPlaced) {
                let data = [];
                let ship;
                
                for (let i = this.ships.length - 1; i >= 0; i--) {
                    ship = this.ships[i];

                    let parentId = ship.parentNode.getAttribute('id');
                    let cellX = parseInt(parentId.substring(this.id.length + 2));
                    let cellY = this.gridHeadersY.indexOf(parentId.substring(this.id.length + 1)[0]);

                    let offsetX = 0;
                    let offsetY = 0;
                    let orientation = ship.getAttribute('orientation');
                    if (orientation = 'horizontal') {
                        offsetX = 1;
                    } else {
                        offsetY = 1;
                    }

                    let locations = [];
                    let size = ship.getAttribute('size');
                    for (let i = 0; i < size; i++) {
                        locations.push(this.gridHeadersY[(cellY + (i * offsetY))] + (cellX + (i * offsetX)));
                    }

                    data.push({ type: ship.getAttribute('type'), locations: locations });
                }
                
                this.setShips({ gp: '' + this.$route.params.gp + '', params: data });
            }
        },
        selectShip: function(ship) {
            this.showGizmo = true;
            if (this.ship != ship) {
                this.ship = ship;
            }
        },
        unselectShip: function(ship) {
            this.showGizmo = false;
            this.ship = null; 
            
            for (let i = this.ships.length - 1; i >= 0; i--) {
                if (this.ships[i].getAttribute('isPositioned') != 'true') {
                    return false;
                }
            }

            this.allShipsPlaced = true;
        }
    },
    mounted: function() {
        this.ships = document.getElementsByClassName('ship');
    }
};
</script>