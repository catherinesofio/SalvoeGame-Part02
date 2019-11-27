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
    gridHeadersY: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J']
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
      
      axios.get('/api/matches').then(response => {
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
    register: ({ context, dispatch }, params) => {
      axios.post('/api/register', new URLSearchParams(params)).then(() => {
        dispatch('login', { email: params.email, password: params.password });
      });
    },
    login: (context, params) => {
      axios.post('/api/login', new URLSearchParams(params)).finally(() => {
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
      bus.$emit('start-load');
      axios.get('/api/templates/salvoes').then(response => callback(response.data));
    },
    setShips: (context, { gp, params }) => {
      fetch('/api/games/players/' + gp + '/ships', { method: 'POST', body: JSON.stringify(params), mode: 'cors', headers: { 'Content-Type': 'application/json' } }).then(response => {
        router.push({ name: 'view', params: { gp: gp } });
      });
    },
    setSalvoes: (context, { gp, params }) => {
      fetch('/api/games/players/' + gp + '/salvoes', { method: 'POST', body: JSON.stringify(params), mode: 'cors', headers: { 'Content-Type': 'application/json' } }).then(response => {
        bus.$emit('trigger-instant-refresh');
      });
    },
    getMatchData: (context, { gp, callback }) => {
      bus.$emit('start-load');
      axios.get('/api/game_view/' + gp).then(response => {
        callback(response.data);
        bus.$emit('end-load');
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