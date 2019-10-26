import Vue from 'vue';
import Vuex from 'vuex';
import axios from 'axios';
import router from '@/router/index.js';

Vue.use(Vuex);

const store = new Vuex.Store({
  state: {
    user: null,
    users: [],
    matches: [],
    userMatches: {
      current: [],
      history: []
    },
    leaderboards: []
  },
  mutations: {
    INIT_USER: async state => {
      await axios.get('/api/user').then(response => { 
        let data = JSON.parse(JSON.stringify(response.data));
        data = (data.name == null) ? null : data;

        state.user = data;
        
        checkUser(data);
      });
    },
    SET_USER: state => {
      state.user = state;
      
      checkUser(state);
    },
    UPDATE_INFO: async state => {
      if (state.user == null) {
        state.users = [];
        state.matches = [];
        state.userMatches = { current: [], history: [] };
        state.leaderboards = [];

        return;
      }

      await axios.get('/api/matches').then(response => {
        let data = JSON.parse(JSON.stringify(response.data));

        let users = data.users;
        state.users = users;
        state.matches = data.matches;
        state.userMatches = data.userMatches;
        state.leaderboards = users.foreach(function(user) {
          let points = (user.won + (user.lost / 2)) * 100 / (user.won + user.won + user.lost);

          return { id: user.id, points: points };
        }).sort(function(a, b) {
          return a.points - b.points;
        });
      })
    }
  },
  actions: {
    login: async (context, params) => {
      await axios.post('/api/login', new URLSearchParams(params)).then(response => {
        let data = JSON.parse(JSON.stringify(response.data));

        context.commit('SET_USER', data);
      });
    },
    logout: async (context) => {
      await axios.post('/api/logout').then(() => context.commit('SET_USER', null));
    },
    joinMatch: (context, params) => {
      //JOIN MATCH
    },
    createMatch: (context) => {
      //CREATE MATCH
    },
    loadMatch: (context, params) => {
      //LOAD MATCH
    }
  }
});

export default store;

function checkUser(user) {
  if (user == null) {
    store.commit('UPDATE_INFO');

    router.push('/login').catch(err => {});
  } else {
    store.commit('UPDATE_INFO');

    router.push('/menu').catch(err => {});
  }
}