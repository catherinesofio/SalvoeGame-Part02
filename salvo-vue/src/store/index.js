import Vue from 'vue';
import Vuex from 'vuex';
import router from '@/router/index.js';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    user: null,
    quests: [{
      id: 1,
      opponent: 'DaKingJJ',
      isOnline: true
    },
    {
      id: 2,
      opponent: 'Bertorto',
      isOnline: false
    },
    {
      id: 3,
      opponent: 'LordDross',
      isOnline: false
    },
    {
      id: 4,
      opponent: 'Allmightho',
      isOnline: true
    }],
    userQuests: {
      current: [{
        id: 10,
        opponent: 'KaBoom',
        isOnline: false,
        state: 'PLAYING_TURN'
      },
      {
        id: 11,
        opponent: 'AnaMov',
        isOnline: false,
        state: 'WAITING_SHIPS'
      },
      {
        id: 12,
        opponent: 'Izukuwu',
        isOnline: true,
        state: 'PLAYING_WAITING'
      }],
      history: [{
        id: 6,
        opponent: 'LordDross',
        isOnline: false,
        state: 'PLAYER_LOST'
      },
      {
        id: 7,
        opponent: 'Izukuwu',
        isOnline: true,
        state: 'PLAYER_TIED'
      }]
    },
    leaderboards: [{
      username: 'Allmightho',
      points: 3000,
      position: 1
    },
    {
      username: 'ToddyCaliente',
      points: 2999,
      position: 2
    },
    {
      username: 'Bertorto',
      points: 2000,
      position: 3
    },
    {
      username: 'DaKingJJ',
      points: 1000,
      position: 4
    },
    {
      username: 'AnaMov',
      points: 700,
      position: 5
    },
    {
      username: 'LordDross',
      points: 666,
      position: 6
    },
    {
      username: 'elDementoide',
      points: 420,
      position: 7
    },
    {
      username: 'KaBoom',
      points: 22,
      position: 8
    },
    {
      username: 'JaegerBomb',
      points: 10,
      position: 9
    },
    {
      username: 'Izukuwu',
      points: 1,
      position: 10
    }]
  },
  mutations: {
    INIT_USER: state => {
      state.user = {
        name: 'JaegerBomb',
        id: 1
      }
      /*$.get("/api/user").then(response => {
        state.user = (response == "") ? null : response;
        
        if (state.user != null) {
          router.push("/menu").catch(err => {});
        } else {
          router.push("/login").catch(err => {});
        }
      });*/
    },
    SET_USER: state => {
      state.user = state;
      
      if (state.user != null) {
        router.push('/menu').catch(err => {});
      } else {
        router.push('/login').catch(err => {});
      }
    }
  },
  actions: {
    login: (context, params) => {
      $.post('/api/login', params).done(response => context.commit('SET_USER', response.data));
    },
    logout: (context) => {
      $.post('/api/logout').done(() => context.commit('SET_USER', null));
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