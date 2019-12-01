<template>
    <div v-if='isTurn'>
        <p>Salvoes: {{ salvoesLeft }}</p>
        <button v-if='canPlaceSalvoes' v-on:click='submitSalvoes'>SUBMIT</button>
    </div>
</template>

<script>
import { bus } from '@/main.js';
import { mapActions } from 'vuex';

export default {
    props: ['isTurn', 'gp', 'turn'],
    data: function() {
        return {
            salvoesLeft: 0,
            salvoesPlaced: [],
            salvoesTemplate: 0
        };
    },
    watch: {
        isTurn: function(n, p) {
            if (!n) {
                this.resetSalvoes();
            }
        }
    },
    computed: {
        canPlaceSalvoes: function() {
            return this.isTurn && this.salvoesLeft == 0;
        }
    },
    methods: {
        ...mapActions(['getSalvoesTemplate', 'setSalvoes']),
        setSalvoesTemplate: function(salvoes) {
            this.salvoesTemplate = this.salvoesLeft = salvoes;
        },
        resetSalvoes: function() {
            this.salvoesPlaced = [];
            this.salvoesLeft = this.salvoesTemplate;
        },
        checkSalvoe: function({ cell, state, callback }) {
            if (this.salvoesPlaced.includes(cell)) {
                let index = this.salvoesPlaced.indexOf(cell);

                this.salvoesPlaced.splice(index, 1);
                this.salvoesLeft += 1;

                state = false;
                cell.innerHTML = '';
            } else if (this.salvoesPlaced.length < this.salvoesTemplate) {
                this.salvoesPlaced.push(cell);
                this.salvoesLeft -= 1;

                state = true;
                cell.innerHTML = this.turn;
            }

            callback(state);
        },
        submitSalvoes: function() {
            let location;
            let params = [];
            let index = ('opponent-').length;
            params = this.salvoesPlaced.map(salvoe => {
                location = salvoe.getAttribute('id').slice(index);
                return { cell: location };
            });

            this.resetSalvoes();
            this.setSalvoes({ gp: this.gp, params: params });
        }
    },
    mounted: function() {
        this.getSalvoesTemplate(this.setSalvoesTemplate);
        bus.$on('check-salvoe', this.checkSalvoe);
    },
    beforeDestroy: function() {
        bus.$off('check-salvoe', this.checkSalvoe);
    }
};
</script>