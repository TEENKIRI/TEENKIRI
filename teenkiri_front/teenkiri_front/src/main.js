// main.js는 vue 애플리케이션의 시작점
import { createApp } from 'vue';
import App from './App.vue';
import router from '@/router/index.js';
import vuetify from './plugins/vuetify';
import '@mdi/font/css/materialdesignicons.css';
import axios from 'axios';
import '@/assets/css/font.css';
import store from './store/index.js';
import mitt from 'mitt';

import '@/assets/css/global.css';

// Vue 애플리케이션 생성
const app = createApp(App);

// mitt를 사용한 Event Bus 설정
const emitter = mitt();
app.config.globalProperties.emitter = emitter;

// axios 요청 인터셉터를 설정하여 모든 요청에 엑세스 토큰을 추가
axios.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        console.error('Axios 요청 인터셉터 오류:', error);
        return Promise.reject(error);
    }
);

// axios 응답 인터셉터를 설정하여 401 오류 처리 및 토큰 갱신
axios.interceptors.response.use(
    response => response,
    async error => {
        if (error.response && error.response.status === 401) {
            const refreshToken = localStorage.getItem('refreshToken');

            if (refreshToken) {
                try {
                    // 토큰 갱신 요청
                    localStorage.removeItem('token');
                    const response = await axios.post(
                        `${process.env.VUE_APP_API_BASE_URL}/member/refresh-token`,
                        { refreshToken }
                    );
                    localStorage.setItem('token', response.data.result.token);
                    window.location.reload();
                } catch (e) {
                    console.error('토큰 갱신 실패:', e);
                    localStorage.clear();
                    window.location.href = '/login';
                }
            } else {
                // 리프레시 토큰이 없는 경우 로그인 페이지로 리다이렉트
                console.warn('리프레시 토큰이 없음, 로그인 페이지로 리다이렉트합니다.');
                localStorage.clear();
                window.location.href = '/login';
            }
        } else {
            console.error('Axios 응답 오류:', error);
        }
        return Promise.reject(error);
    }
);


// Vue 애플리케이션에 플러그인 및 라우터 설정
app.use(store);
app.use(router);
app.use(vuetify);

// Vue 애플리케이션 마운트
app.mount('#app');


