import axios from 'axios';
import store from '@/store'; // Vuex 스토어 임포트

axios.defaults.baseURL = 'http://localhost:8081';

export function setAuthHeader(token) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

export async function fetchUser() {
  const response = await axios.get('/api/user-info');
  store.commit('setUser', response.data.result);
}
