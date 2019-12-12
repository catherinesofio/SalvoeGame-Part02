<template>
    <tr :isPlayer='isPlayer'>
        <td>{{ this.getPosition }}</td>
        <td>{{ this.getName }}</td>
        <td>{{ this.points }}</td>
    </tr>
</template>

<script>
import { mapState } from 'vuex';

export default {
    props: ['position', 'username', 'points', 'isPlayer'],
    computed: {
        ...mapState(['badges']),
        getName: function() {
            return (this.isPlayer) ? 'You' : this.username;
        },
        getPosition: function() {
            return (this.position < 4) ? this.badges[this.position - 1] : this.position; 
        }
    }
};
</script>