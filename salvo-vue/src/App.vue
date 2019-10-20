<template>
  <div id="app">
    <router-view/>
    <Nav v-if="userExists" :user="user" />
  </div>
</template>

<script>
import Nav from "@/components/Nav.vue";
import { mapState } from 'vuex';

export default {
  components: {
    Nav
  },
  computed: {
    ...mapState(['user']),
    userExists: function() {
      return this.user != "";
    }
  }
}
</script>