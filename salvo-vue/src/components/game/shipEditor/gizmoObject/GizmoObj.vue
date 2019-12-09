<template>
    <div id='gizmo-obj'>
        <GizmoButton type='accept' :callback='triggerPlace' />
        <GizmoButton type='cancel' :callback='triggerUnselect' />
        <GizmoButton type='rotate' :callback='rotate' />
        <GizmoButton type='east' :callback='move' />
        <GizmoButton type='west' :callback='move' />
        <GizmoButton type='north' :callback='move' />
        <GizmoButton type='south' :callback='move' />
    </div>
</template>

<script>
import GizmoButton from '@/components/game/shipEditor/gizmoObject/GizmoButton.vue';
import { mapState } from 'vuex';

export default {
    props: ['tableId', 'ship', 'callback'],
    data: function() {
        return {
            root: null,
            obj: null,
            handlers: [],
            size: 0,
            orientation: '',
            currOrientation: '',
            parent: null,
            currParent: null,
            placed: null,
            prevX: 0,
            prevY: 0,
            cellX: 0,
            cellY: 0,
            prevCells: [],
            occupiedCells: []
        };
    },
    components: {
        GizmoButton
    },
    computed: {
        ...mapState(['gridHeaders', 'gridHeadersY'])
    },
    watch: {
        ship: function (n, o) {
            if (o != null) {
                o.setAttribute('orientation', this.orientation);
                this.parent.appendChild(o);
            }
            
            this.ship = n;
            if (n != null) {
                this.updateData();
            }
        }
    },
    methods: {
        updateData: function() {
            this.size = parseInt(this.ship.getAttribute('size'));
            this.orientation = this.currOrientation = this.ship.getAttribute('orientation');

            this.parent = this.ship.parentNode;
            this.placed = (this.ship.getAttribute('isPositioned') == 'true') ? true : false;
            this.prevCells = [];

            if (this.placed) {
                let parentId = this.parent.getAttribute('id');

                this.prevX = this.cellX = parseInt(parentId.substring(this.tableId.length + 2));
                this.prevY = this.cellY = this.gridHeadersY.indexOf(parentId.substring(this.tableId.length + 1)[0]);

                let offsetX = 0;
                let offsetY = 0;
                switch(this.orientation) {
                    case 'horizontal':
                        offsetX = 1;
                        break;
                    default:
                        offsetY = 1;
                        break;
                }

                let cell;
                for (let i = 0; i < this.size; i++) {
                    cell = this.gridHeadersY[(this.cellY + (i * offsetY))] + '' + (this.cellX + (i * offsetX));
                    this.prevCells.push(cell);
                }
            } else {
                this.cellX = 5;
                this.cellY = 5;
            }
            
            this.currParent = document.getElementById(this.tableId + '-' + this.gridHeadersY[this.cellY] + this.cellX);
            this.currParent.appendChild(this.obj);
            this.obj.appendChild(this.ship);
            
            this.checkGizmoHandlers();
            this.resizeGizmoObj();
            this.checkObjPosition();
        },
        triggerPlace: function() {
            let offsetX = 0;
            let offsetY = 0;
            let cell;

            if (this.placed) {
                for (let i = this.prevCells.length - 1; i >= 0; i--) {
                    cell = this.prevCells[i];
                    this.occupiedCells.splice(this.occupiedCells.indexOf(cell), 1);
                }
            }

            switch(this.currOrientation) {
                case 'horizontal':
                    offsetX = 1;
                    break;
                default:
                    offsetY = 1;
                    break;
            }

            for (let i = 0; i < this.size; i++) {
                cell = this.gridHeadersY[(this.cellY + (i * offsetY))] + '' + (this.cellX + (i * offsetX));
                if (!this.occupiedCells.includes(cell)) {
                    this.occupiedCells.push(cell);
                }
            }
            
            this.ship.setAttribute('isPositioned', 'true');
            this.currParent.appendChild(this.ship);
            this.parent = this.currParent;
            this.orientation = this.currOrientation;

            this.callback(this.ship);
        },
        triggerUnselect: function() {
            this.ship.setAttribute('orientation', this.orientation);
            this.parent.appendChild(this.ship);

            this.callback(this.ship);
        },
        rotate: function() {
            this.currOrientation = (this.currOrientation == 'horizontal') ? 'vertical': 'horizontal';

            this.ship.setAttribute('orientation', this.currOrientation);

            this.checkGizmoHandlers();
            this.resizeGizmoObj();
            this.checkObjPosition();
        },
        move: function(direction) {
            switch (direction) {
                case 'north':
                    this.cellY -= 1;
                    this.currParent = this.currParent.parentNode.previousSibling.querySelector("#" + this.tableId + "-" + this.gridHeadersY[this.cellY] + this.cellX);
                    this.currParent.appendChild(this.obj);
                    break;

                case 'south':
                    this.cellY += 1;
                    this.currParent = this.currParent.parentNode.nextSibling.querySelector("#" + this.tableId + "-" + this.gridHeadersY[this.cellY] + this.cellX);
                    this.currParent.appendChild(this.obj);
                    break;

                case 'west':
                    this.cellX -= 1;
                    this.currParent = this.currParent.previousSibling;
                    this.currParent.appendChild(this.obj);
                    break;

                case 'east':
                    this.cellX += 1;
                    this.currParent = this.currParent.nextSibling;
                    this.currParent.appendChild(this.obj);
                    break;
            }

            this.checkGizmoHandlers();
            this.resizeGizmoObj();
            this.checkObjPosition();
        },
        checkGizmoHandlers: function() {
            let offsetX = 0;
            let offsetY = 0;

            switch(this.currOrientation) {
                case 'horizontal':
                    let dx = (this.cellX + this.size - 1);
                    offsetX -= (this.size - 1);
                    offsetY -= 1;
                    
                    if (this.gridHeaders < dx) {
                        this.repositionGizmoObj((dx - this.gridHeaders) * - 1, 0);
                        this.handlers[3].setAttribute('isBlocked', true);
                    }
                    break;

                case 'vertical':
                    let dy = (this.cellY + this.size);
                    offsetY -= this.size;

                    if (this.gridHeaders < dy) {
                        this.repositionGizmoObj(0, (dy - this.gridHeaders) * - 1);
                        this.handlers[1].setAttribute('isBlocked', true);
                    }
                    break;
            }

            switch(this.cellX) {
                case 1:
                    this.handlers[2].setAttribute('isBlocked', true);
                    break;

                case (this.gridHeaders + offsetX):
                    this.handlers[3].setAttribute('isBlocked', true);
                    break;

                default:
                    this.handlers[2].setAttribute('isBlocked', false);
                    this.handlers[3].setAttribute('isBlocked', false);
                    break;
            }

            switch(this.cellY) {
                case 0:
                    this.handlers[0].setAttribute('isBlocked', true);
                    break;

                case (this.gridHeaders + offsetY):
                    this.handlers[1].setAttribute('isBlocked', true);
                    break;

                default:
                    this.handlers[0].setAttribute('isBlocked', false);
                    this.handlers[1].setAttribute('isBlocked', false);
                    break;
            }
        },
        resizeGizmoObj: function () {
            if (this.currOrientation == 'horizontal') {
                this.root.style.setProperty('--gizmo-width', this.size);
                this.root.style.setProperty('--gizmo-height', 1);
            } else {
                this.root.style.setProperty('--gizmo-width', 1);
                this.root.style.setProperty('--gizmo-height', this.size);
            }
        },
        repositionGizmoObj: function(offsetX, offsetY) {
            this.cellX += offsetX;
            this.cellY += offsetY;
            
            this.currParent = document.getElementById(this.tableId + '-' + this.gridHeadersY[this.cellY] + '' + this.cellX);
            this.currParent.appendChild(this.obj);
        },
        checkObjPosition: function() {
            let offsetX = 0;
            let offsetY = 0;
            let cell;

            switch(this.currOrientation) {
                case 'horizontal':
                    offsetX = 1;
                    break;
                default:
                    offsetY = 1;
                    break;
            }

            for (let i = 0; i < this.size; i++) {
                cell = this.gridHeadersY[(this.cellY + (i * offsetY))] + '' + (this.cellX + (i * offsetX));
                if (this.occupiedCells.includes(cell) && !this.prevCells.includes(cell)) {
                    this.handlers[4].setAttribute('isBlocked', true);
                    return;
                }
            }

            this.handlers[4].setAttribute('isBlocked', false);
        }
    },
    mounted: function() {
        this.root = document.documentElement;

        this.obj = document.getElementById('gizmo-obj');
        
        this.handlers.push(document.querySelector('[type="north"]'));
        this.handlers.push(document.querySelector('[type="south"]'));
        this.handlers.push(document.querySelector('[type="west"]'));
        this.handlers.push(document.querySelector('[type="east"]'));
        this.handlers.push(document.querySelector('[type="accept"]'));
    }
};
</script>

<style>
#gizmo-obj {
    position: absolute;
    top: 0;
    left: 0;
    width: calc((var(--gizmo-width)) * var(--cell-size));
    height: calc((var(--gizmo-height)) * var(--cell-size));
    padding: var(--padding);
    z-index: var(--layer-foreground-gizmo);
}

#gizmo-obj .ship {
    position: absolute;
    top: 0 !important;
    left: 0;
    z-index: var(--layer-foreground-gizmo);
}
</style>