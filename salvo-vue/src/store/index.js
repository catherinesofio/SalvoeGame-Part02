import Vue from 'vue';
import Vuex from 'vuex';
import axios from 'axios';
import router from '@/router/index.js';
import { bus } from '@/main.js';

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
      /*fetch('/api/user', 
      { method: 'GET', mode: 'cors', headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' } }).then(response => response.json()).then(data => {
        data = (data.name == null) ? null : data;
        
        state.user = data;
      }).finally(() => {
        checkUser(state.user);
        bus.$emit('end-load');
      });*/
      axios.get('/api/user').then(response => { 
        let data = JSON.parse(JSON.stringify(response.data));
        data = (data.name == null) ? null : data;
        
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
      
      axios.get('https://neko-voe.herokuapp.com/api/matches').then(response => {
        let data = JSON.parse(JSON.stringify(response.data));
        
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
      axios.post({ url: '/api/register', params: new URLSearchParams(params), withCredentials: true }).then(() => {
        dispatch('login', { email: params.email, password: params.password });
      });
    },
    login: (context, params) => {
      /*fetch('/api/login', { method: 'POST', body: JSON.stringify(params), mode: 'cors', credentials: 'include', headers: { 'Content-Type': 'application/json' } }).finally(() => {
        context.commit('INIT_USER');
      });*/
      axios.post({ url: 'https://neko-voe.herokuapp.com/api/login', params: new URLSearchParams(params), withCredentials: true }).finally(() => {
        context.commit('INIT_USER');
      });
    },
    logout: (context) => {
      axios.post('/api/logout').finally(() => { 
        context.commit('SET_USER', null)});
    },
    createMatch: (context) => {
      axios.post('/api/games').then(response => {
        router.push({ name: 'game', params: { gp: response.data } });
      });
    },
    joinMatch: (context, gm) => {
      axios.post('/api/game/' + gm + '/players').then(response => {
        router.push({ name: 'game', params: { gp: response.data } });
      });
    },
    loadMatch: (context, gp) => {
      bus.$emit('start-load');
      router.push({ name: 'game', params: { gp: gp } }).finally(() => { bus.$emit('end-load'); });
    },
    getShipsTemplate: (context, callback) => {
      axios.get('/api/templates/ships').then(response => callback(response.data));
    },
    getSalvoesTemplate: (context, callback) => {
      axios.get('/api/templates/salvoes').then(response => callback(response.data));
    },
    setShips: (context, { gp, params }) => {
      fetch('/api/games/players/' + gp + '/ships', { method: 'POST', body: JSON.stringify(params), mode: 'cors', headers: { 'Content-Type': 'application/json' } }).then(response => {
        router.push({ name: 'view', params: { gp: gp } });
      });
    },
    setSalvoes: (context, { gp, params }) => {
      fetch('/api/games/players/' + gp + '/salvoes', { method: 'POST', body: JSON.stringify(params), mode: 'cors', headers: { 'Content-Type': 'application/json' } }).finally(() => { bus.$emit('trigger-instant-refresh'); });
    },
    getMatchData: (context, { gp, callback }) => {
      bus.$emit('start-load');
      axios.get('/api/game_view/' + gp).then(response => { callback(response.data); }).finally(() => { bus.$emit('end-load'); });
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

function sortByMostRecent(array) {
  return array.sort((a, b) => new Date(b.id) - new Date(a.id));
}