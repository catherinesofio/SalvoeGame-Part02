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
            cellX: '',
            cellY: ''
        };
    },
    components: {
        GizmoButton
    },
    computed: {
        ...mapState(['gridHeaders', 'gridHeadersY'])
    },
    watch: {
        ship: function (newValue, oldValue) {
            if (oldValue != null) {
                oldValue.setAttribute('orientation', this.orientation);
                this.parent.appendChild(oldValue);
            }
            
            this.ship = newValue;
            if (newValue != null) {
                this.updateData();
            }
        }
    },
    methods: {
        updateData: function() {
            this.size = parseInt(this.ship.getAttribute('size'));
            this.orientation = this.currOrientation = this.ship.getAttribute('orientation');

            this.parent = this.ship.parentNode;
            this.placed = this.ship.getAttribute('isPositioned');
            this.placed = (this.placed != true) ? false : true;
            if (this.placed) {
                let parentId = this.parent.getAttribute('id');

                this.cellX = parseInt(parentId.substring(0, (this.tableId.length + 2)));
                this.cellY = this.gridHeadersY.indexOf(parentId.substring(0, (this.tableId.length + 1))[0]);
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
            this.ship.setAttribute('isPositioned', true);
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

            if (this.currOrientation == 'horizontal') {
                offsetX = 1;
            } else {
                offsetY = 1;
            }
        }
    },
    mounted: function() {
        this.root = document.documentElement;

        this.obj = document.getElementById('gizmo-obj');
        
        this.handlers.push(document.querySelector('[type="north"]'));
        this.handlers.push(document.querySelector('[type="south"]'));
        this.handlers.push(document.querySelector('[type="west"]'));
        this.handlers.push(document.querySelector('[type="east"]'));
    }
};
</script>

<style>
:root {
    --gizmo-width: 1;
    --gizmo-height: 2;
}

#gizmo-obj {
    position: absolute;
    top: 0;
    left: 0;
    width: calc((var(--gizmo-width)) * var(--cell-size));
    height: calc((var(--gizmo-height)) * var(--cell-size));
    padding: 0.25em;
    z-index: var(--gizmo-index);
}

#gizmo-obj .ship {
    position: absolute;
    top: 0 !important;
    left: 0;
}
</style>