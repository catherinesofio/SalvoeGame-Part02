<template>
    <tr :isPlayer='isPlayer' :size='getSize'>
        <td>{{ this.getPosition }}</td>
        <td>{{ this.username }}</td>
        <td>{{ this.points }}</td>
    </tr>
</template>

<script>
import { mapState } from 'vuex';

export default {
    props: ['position', 'username', 'points', 'isPlayer'],
    computed: {
        ...mapState(['badges']),
        getPosition: function() {
            return (this.position < 4) ? this.badges[this.position - 1] : this.position; 
        },
        getSize: function() {
            return (this.position < 4) ? 4 - this.position : false;
        }
    }
};
</script>