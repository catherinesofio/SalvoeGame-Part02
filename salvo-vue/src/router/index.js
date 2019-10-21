import Vue from 'vue';
import VueRouter from 'vue-router';
import Login from '../views/Login.vue';
import Menu from '../views/menu/Menu.vue';
import MenuQuests from '../views/menu/MenuQuests.vue';
import MenuUserQuests from '../views/menu/MenuUserQuests.vue';
import MenuLeaderboards from '../views/menu/MenuLeaderboards.vue';

Vue.use(VueRouter);

const routes = [
  {
    path: '/login',
    name: 'login',
    component: Login
  }
  ,{
    path: '/menu',
    name: 'menu',
    redirect: "/menu/quests",
    component: Menu,
    children: [
      {
        path: "quests",
        component: MenuQuests
      },
      {
        path: "my-quests",
        component: MenuUserQuests
      },
      {
        path: "leaderboards",
        component: MenuLeaderboards
      }
    ]
  }
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

export default router;