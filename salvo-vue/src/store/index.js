import Vue from 'vue';
import Vuex from 'vuex';
import router from '@/router/index.js';
import { bus } from '@/main.js';

Vue.use(Vuex);

const backend = 'https://neko-voe.herokuapp.com/api';

const store = new Vuex.Store({
  state: {
    user: null,
    users: [],
    matches: [],
    userMatches: {
      current: [],
      history: []
    },
    leaderboards: [],
    gridHeaders: 10,
    gridHeadersY: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'],
    badges: ['🥇', '🥈', '🥉']
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
    INIT_USER: state => {
      bus.$emit('start-load');
      fetch(backend + '/user', { method: 'GET', mode: 'cors', credentials: "include" }).then(response => response.json()).then(data => {
        data = isValid(data.name) ? data : null;
        
        state.user = data;
      }).finally(() => {
        checkUser(state.user);
        bus.$emit('end-load');
      });
    },
    SET_USER: (state, value) => {
      state.user = value;
      
      checkUser(value);
    },
    UPDATE_INFO: state => {
      if (state.user == null) {
        state.users = [];
        state.matches = [];
        state.userMatches = { current: [], history: [] };
        state.leaderboards = [];
        
        return;
      }
      
      fetch(backend + '/matches', { method: 'GET', mode: 'cors', credentials: "include" }).then(response => response.json()).then(data => {
        data = JSON.parse(JSON.stringify(data));
        
        let users = data.users.sort(function(a, b) {
          return a.id - b.id;
        });
        state.users = users;
        state.matches = sortByMostRecent(data.matches);
        state.userMatches = data.userMatches;
        state.userMatches.current = sortByMostRecent(state.userMatches.current);
        state.userMatches.history = sortByMostRecent(state.userMatches.history);
        state.leaderboards = users.map(function(user) {
          let scores = user.scores;
          
          let points = (scores.won + (scores.lost / 2)) * 100 / (scores.won + scores.lost + scores.tied);
          points = (Number.isNaN(points)) ? 0 : points;
          
          return { id: user.id, points: points };
        }).sort(function(a, b) {
          return b.points - a.points;
        });
      });
    }
  },
  actions: {
    register: ({ context, dispatch }, params) => {
      let data = new URLSearchParams();
      data.set('name', params.name);
      data.set('email', params.email);
      data.set('password', params.password);
      
      fetch(backend + '/register', { method: 'POST', mode: 'cors', body: data, credentials: "include" }).then(() => { dispatch('login', { email: params.email, password: params.password }); });
    },
    login: (context, params) => {
      let data = new URLSearchParams();
      data.set('email', params.email);
      data.set('password', params.password);

      fetch(backend + '/login', { method: 'POST', mode: 'cors', body: data, credentials: "include" }).finally(() => { context.commit('INIT_USER'); });
    },
    logout: (context) => {
      fetch(backend + '/logout', { method: 'POST', mode: 'cors', credentials: "include" }).finally(() => { context.commit('INIT_USER'); }).finally(() => { context.commit('SET_USER', null); });
    },
    createMatch: (context) => {
      fetch(backend + '/games', { method: 'POST', mode: 'cors', credentials: "include" }).then(response => response.json()).then(data => { router.push({ name: 'game', params: { gp: data } }); });
    },
    joinMatch: (context, gm) => {
      fetch(backend + '/game' + gm + '/players', { method: 'POST', mode: 'cors', credentials: "include" }).then(response => response.json()).then(data => { router.push({ name: 'game', params: { gp: data } }); });
    },
    loadMatch: (context, gp) => {
      bus.$emit('start-load');
      router.push({ name: 'game', params: { gp: gp } }).finally(() => { bus.$emit('end-load'); });
    },
    getShipsTemplate: (context, callback) => {
      fetch(backend + '/templates/ships', { method: 'GET', mode: 'cors', credentials: "include" }).then(response => response.json()).then(data => { callback(data); });
    },
    getSalvoesTemplate: (context, callback) => {
      fetch(backend + '/templates/salvoes', { method: 'GET', mode: 'cors', credentials: "include" }).then(response => response.json()).then(data => { callback(data); });
    },
    setShips: (context, { gp, params }) => {
      fetch(backend + '/games/players/' + gp + '/ships', { method: 'POST', mode: 'cors', body: JSON.stringify(params), credentials: "include" }).then(response => { router.push({ name: 'view', params: { gp: gp } }); });
    },
    setSalvoes: (context, { gp, params }) => {
      fetch(backend + '/games/players/' + gp + '/salvoes', { method: 'POST', mode: 'cors', body: JSON.stringify(params), credentials: "include" }).finally(() => { bus.$emit('trigger-instant-refresh'); });
    },
    getMatchData: (context, { gp, callback }) => {
      bus.$emit('start-load');
      fetch(backend + '/game_view/' + gp, { method: 'GET', mode: 'cors', credentials: "include" }).then(response => response.json()).then(data => { callback(data); }).finally(() => { bus.$emit('end-load'); });
    },
    getTurnData: (context, { gp, tn, callback }) => {
      fetch(backend + '/game_view/' + gp + '/turns/' + tn, { method: 'GET', mode: 'cors', credentials: "include" }).then(response => response.json()).then(data => { callback(data); });
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

function sortByMostRecent(array) {
  return array.sort((a, b) => new Date(b.id) - new Date(a.id));
}