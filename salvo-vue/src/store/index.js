import Vue from 'vue';
import Vuex from 'vuex';
import axios from 'axios';

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: ""
  },
  getters: {
  },
  mutations: {
    INIT_USER: state => {
      axios.get("/api/user").then(response => state.user = response.data);
    },
    SET_USER: state => {
      state.user = state;
    }
  },
  actions: {
    login: (context, { email, password }) => {
      axios.post('/api/login', { email: email, password: password }).then(response => context.commit('SET_USER', response.data));
    },
    logout: (context) => {
      axios.post('/api/logout').then(() => context.commit('SET_USER', ""));
    }
  },
  modules: {
  }
})