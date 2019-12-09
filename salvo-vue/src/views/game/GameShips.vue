<template>
    <div>
        <GizmoObject :tableId='id' :ship='ship' :callback='unselectShip' v-show='showGizmo' />
        <div class='container-wrap'>
            <DummyShipContainer :callback='selectShip' />
            <button v-on:click='triggerSubmit' :disabled='!allShipsPlaced'>SUBMIT</button>
        </div>
        <Grid :id='id' :occupiedCells='[]' :isTurn='false' />
    </div>
</template>

<script>
import Grid from '@/components/game/grid/GridSimple.vue';
import GizmoObject from '@/components/game/shipEditor/gizmoObject/GizmoObj.vue';
import DummyShipContainer from '@/components/game/shipEditor/DummyShipContainer.vue';
import { mapState, mapActions } from 'vuex';

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
                    if (orientation == 'horizontal') {
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

<style>
#player-table {
    margin: var(--margin);
    width: calc(100vw - (var(--margin) * 2));
}

#player-table th {
    width: var(--cell-size);
    height: var(--cell-size);
    background-color: var(--color-00);
    color: var(--color-09);
}

#player-table td {
    width: var(--cell-size);
    height: var(--cell-size);
}

#player-table thead :first-child {
    border-top-left-radius: var(--border-radius);
}

#player-table thead :last-child {
    border-top-right-radius: var(--border-radius);
}

.container-wrap {
  margin-top: calc(var(--spacer-height) / 6);
  box-sizing: border-box;
  width: 100vw;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.container-wrap button {
  padding: 0;
  height: 50%;
  margin-left: var(--margin);
  margin-right: var(--margin);
  padding: var(--padding);
  text-align: center;
}
</style>