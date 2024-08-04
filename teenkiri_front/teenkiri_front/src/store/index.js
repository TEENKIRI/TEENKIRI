import { createStore } from 'vuex';
import axios from 'axios';

export default createStore({
  state: {
    user: null,
    token: sessionStorage.getItem('token') || ''
  },
  mutations: {
    setUser(state, user) {
      state.user = user;
    },
    setToken(state, token) {
      state.token = token;
      sessionStorage.setItem('token', token);
    }
  },
  actions: {
    async loginUser({ commit }, credentials) {
      const response = await axios.post('http://localhost:8081/api/login', credentials);
      const token = response.data.result;
      commit('setToken', token);
      await this.dispatch('fetchUser');
    },
    async fetchUser({ commit, state }) {
      if (!state.token) return; 
      const response = await axios.get('http://localhost:8081/api/user-info', {
        headers: {
          'Authorization': `Bearer ${state.token}`
        }
      });
      commit('setUser', response.data.result);
    },
    async registerUser(_, userData) { 
      await axios.post('http://localhost:8081/api/register', userData);
    }
  }
});
