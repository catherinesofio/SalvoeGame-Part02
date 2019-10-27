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
  getters: {
    getUserName: state => id => {
      return state.users[id - 1].name;
    },
    userIsOnline: state => id => {
      return state.users[id - 1].isOnline;
    }
  },
  mutations: {
    INIT_USER: async state => {
      await axios.get('/api/user').then(response => { 
        let data = JSON.parse(JSON.stringify(response.data));
        data = (data.name == null) ? null : data;

        state.user = data;
      }).finally(() => {
        checkUser(state.user);
      });
    },
    SET_USER: (state, value) => {
      state.user = value;
      
      checkUser(value);
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

        let users = data.users.sort(function(a, b) {
          return a.id - b.id;
        });
        state.users = users;
        state.matches = data.matches;
        state.userMatches = data.userMatches;
        state.leaderboards = users.map(function(user) {
          let scores = user.scores;

          let points = (scores.won + (scores.lost / 2)) * 100 / (scores.won + scores.lost + scores.tied);
          points = (Number.isNaN(points)) ? 0 : points;

          return { id: user.id, points: points };
        }).sort(function(a, b) {
          return a.points - b.points;
        });
      });
    }
  },
  actions: {
    login: async (context, params) => {
      await axios.post('/api/login', new URLSearchParams(params)).finally(() => {
        context.commit('INIT_USER');
      });
    },
    logout: async (context) => {
      await axios.post('/api/logout').finally(() => { 
        context.commit('SET_USER', null)});
    },
    createMatch: async (context) => {
      await axios.post('/api/games').then(response => {
        router.push({ name: 'game', params: { gp: response.data } });
      });
    },
    joinMatch: async (context, gm) => {
      await axios.post('/api/game/' + gm + '/players').then(response => {
        router.push({ name: 'game', params: { gp: response.data } });
      });
    },
    loadMatch: (context, gp) => {
      router.push({ name: 'game', params: { gp: gp } });
    },
    getMatchData: (context, { gp, callback }) => {
      axios.get('/api/game_view/' + gp).then(response => {
        callback(response.data);
      });
    },
    getTurnData: (context, { gp, tn, callback }) => {
      axios.get('/api/game_view/' + gp + '/turns/' + tn).then(response => {
        callback(response.data);
      });
    }
  }
});

export default store;

function checkUser(user) {
  if (user == null) {
    store.commit('UPDATE_INFO');

    router.push({ name: 'login', replace: true }).catch(err => {});
  } else {
    store.commit('UPDATE_INFO');

    router.push({ name: 'menu', replace: true }).catch(err => {});
  }
}