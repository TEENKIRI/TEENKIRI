<template>
  <v-app-bar app>
    <v-container>
      <v-row align="center">
        <v-col cols="3">
          <v-toolbar-title>
            <router-link to="/" class="logo">
              <img :src="logo" alt="로고" class="logo-image" />
            </router-link>
          </v-toolbar-title>
        </v-col>
        <v-col cols="6">
          <v-row justify="center">
            <v-btn text @click="navigate('강좌')">강좌</v-btn>
            <v-btn text @click="navigate('추천')">추천</v-btn>
            <v-btn text @click="navigate('이벤트')">이벤트</v-btn>
            <v-btn text @click="navigate('공지사항')">공지사항</v-btn>
            <v-btn text @click="navigate('자유게시판')">자유게시판</v-btn>
            <v-btn text @click="navigate('QnA')">질문게시판</v-btn>
          </v-row>
        </v-col>
        <v-col cols="3" class="text-right">
          <v-btn icon @click="goToMember">
            <v-icon>mdi-account</v-icon>
          </v-btn>
          <v-btn v-if="isLogin" @click="doLogout">로그아웃</v-btn>
          <v-btn icon @click="goToMenu">
            <v-icon>mdi-menu</v-icon>
          </v-btn>

          <!-- 알림 아이콘 및 알림 목록 -->
          <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
              <v-btn icon v-bind="attrs" v-on="on">
                <v-badge
                  color="red"
                  :content="notifications.length"
                  overlap
                  v-if="notifications.length > 0"
                >
                  <v-icon>mdi-bell</v-icon>
                </v-badge>
                <v-icon v-else>mdi-bell</v-icon>
              </v-btn>
            </template>
            <v-list>
              <v-list-item
                v-for="(notification, index) in notifications"
                :key="index"
              >
                <v-list-item-content>
                  <v-list-item-title>{{ notification.message }}</v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-list>
          </v-menu>
        </v-col>
      </v-row>
    </v-container>
  </v-app-bar>
</template>

<script>
import { EventSourcePolyfill } from 'event-source-polyfill';

export default {
  name: 'HeaderComponent',
  data() {
    return {
      logo: require('@/assets/images/ico_logo.png'),
      isLogin: false,
      notifications: [],
    };
  },
  mounted() {
    const token = localStorage.getItem('token');
    this.isLogin = !!token;

    if (this.isLogin) {
      const eventSource = new EventSourcePolyfill(`${process.env.VUE_APP_API_BASE_URL}/subscribe`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      eventSource.addEventListener('connect', (event) => {
        console.log(event)
        })

        eventSource.addEventListener('notification', (event) => {
          console.log(event);
          const notification = JSON.parse(event.data);
          this.notifications.push(notification);
          console.log('새 알림:', notification.message);
        })


      // eventSource.onmessage = (event) => {
      //   const notification = JSON.parse(event.data);
      //   this.notifications.push(notification);
      //   console.log('새 알림:', notification.message);
      // };

      eventSource.onerror = (error) => {
        console.error('SSE 연결 오류:', error);
      };
    }
  },
  methods: {
    navigate(section) {
      if (section === '강좌') {
        this.$router.push({ name: 'SubjectList', params: { category: 'subject' } });
      } else if (section === '이벤트') {
        this.$router.push({ name: 'BoardList', params: { category: 'event' } });
      } else if (section === '공지사항') {
        this.$router.push({ name: 'BoardList', params: { category: 'notice' } });
      } else if (section === '자유게시판') {
        this.$router.push({ name: 'BoardList', params: { category: 'post' } });
      } else if (section === 'QnA') {
        this.$router.push({ name: 'QnaList', params: { category: 'qna' } });
      } else {
        console.log(section);
      }
    },
    goToMember() {
      if (this.isLogin) {
        this.$router.push('/user/edit-info');
      } else {
        this.$router.push('/login');
      }
    },
    goToMenu() {
      console.log('Go to menu');
    },
    doLogout() {
      localStorage.removeItem('role');
      localStorage.removeItem('token');
      this.isLogin = false;
      console.log('Logged out');
      window.location.reload();
    }
  }
};
</script>

<style scoped>
.logo {
  font-weight: bold;
  color: inherit;
  text-decoration: none;
}

.logo-image {
  height: 10%;
}
</style>
