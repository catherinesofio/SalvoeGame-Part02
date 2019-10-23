import Vue from 'vue';
import VueRouter from 'vue-router';
import store from '@/store/index.js';
import Login from '../views/Login.vue';
import Menu from '../views/menu/Menu.vue';
import MenuQuests from '../views/menu/MenuQuests.vue';
import MenuUserQuests from '../views/menu/MenuUserQuests.vue';
import MenuLeaderboards from '../views/menu/MenuLeaderboards.vue';
import Game from '../views/game/Game.vue';
import GameView from '../views/game/GameView.vue';
import GameShips from '../views/game/GameShips.vue';
import GameResults from '../views/game/GameResults.vue';

Vue.use(VueRouter);

const routes = [
  {
    path: '/login',
    name: 'login',
    component: Login
  }
  ,
  {
    path: '/menu',
    name: 'menu',
    redirect: '/menu/quests',
    component: Menu,
    children: [
      {
        path: 'quests',
        component: MenuQuests
      },
      {
        path: 'my-quests',
        component: MenuUserQuests
      },
      {
        path: 'leaderboards',
        component: MenuLeaderboards
      }
    ]
  },
  {
    path: '/game',
    name: 'game',
    component: Game,
    children: [
      {
        path: 'ships',
        component: GameShips
      },
      {
        path: 'view',
        component: GameView
      },
      {
        path: 'results',
        component: GameResults
      }
    ]
  }
];

const router = new VueRouter({
  mode: 'history',
  base: '/salvo',
  routes
});

router.beforeEach((to, from, next) => {
  let userExists = store.state.user != null;
console.log(from.path);
console.log(to.path);
console.log(from.path === '/');
  if (from.path === '/') {
    next(false);
  }

  if (to.path.includes('/menu') && !userExists) {
    next({ path: '/login', replace: true });
  }
  
  if (to.path == '/login' && userExists) {
    next({ path: '/menu', replace: true });
  }

  next();
});

export default router;