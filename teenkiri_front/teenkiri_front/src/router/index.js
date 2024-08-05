import { createRouter, createWebHistory } from 'vue-router';
import UserLogin from '../components/UserLogin.vue';
import UserWelcome from '../components/UserWelcome.vue';
import MyPage from '../components/MyPage.vue';
import Register from '../components/Register.vue';

const routes = [
  { path: '/login', component: UserLogin },
  { path: '/user-welcome', component: UserWelcome },
  { path: '/mypage', component: MyPage },
  { path: '/register', component: Register },
  { path: '/', redirect: '/login' }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
