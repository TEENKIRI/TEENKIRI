// main.js는 vue 애플리케이션의 시작점
import { createApp } from 'vue';
import App from './App.vue';
import router from '@/router/index.js';
import vuetify from './plugins/vuetify';
import '@mdi/font/css/materialdesignicons.css';
import axios from 'axios';
import "@/assets/css/font.css";
import store from './store/index.js';

import mitt from 'mitt';

import '@mdi/font/css/materialdesignicons.css';
import '@/assets/css/global.css'

const app = createApp(App);
const emitter = mitt();
app.config.globalProperties.emitter = emitter;

// axios 요청 인터셉터를 설정하여 모든 요청에 엑세스 토큰을 요청(전역적으로 setting 됐는지 test)
axios.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if(token){
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        console.log('에러다에러에러에러')

        return Promise.reject(error );

    }
);

axios.interceptors.response.use(
    response => response,
    async error => {
        if(error.response && error.response.status === 401){
            const refreshToken = localStorage.getItem('refreshToken');

            try{
                localStorage.removeItem('token');
                const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/member/refresh-token`, {refreshToken});
                localStorage.setItem('token',response.data.result.token);
                window.location.reload();
            }catch(e){
                localStorage.clear();
                window.location.href='/login';
            }
        }
        return Promise.reject(error);
    }
);

// app.use(VueVideoPlayer)
app.use(store);
app.use(router);
app.use(vuetify);
app.mount('#app');
