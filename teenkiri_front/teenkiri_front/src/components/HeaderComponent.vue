<template>
  <v-app-bar app >
    <v-container>
      <v-row align="center">
        <v-col>
          <v-toolbar-title>
            <router-link to="/" class="logo">
              <img :src="logo" alt="로고" class="logo-image" style="max-width:100%;" />
            </router-link>
          </v-toolbar-title>
        </v-col>
        <v-col>
          <v-row class="menu flex-nowrap">
            <v-btn text @click="navigate('강좌')">강좌</v-btn>
            <v-btn text @click="navigate('추천')">추천</v-btn>
            <v-btn text @click="navigate('이벤트')">이벤트</v-btn>
            <v-btn text @click="navigate('공지사항')">공지사항</v-btn>
            <v-btn text @click="navigate('자유게시판')">자유게시판</v-btn>
            <v-btn text @click="navigate('QnA')">질문게시판</v-btn>
            <v-btn v-if="isAdmin" text @click="navigate('신고리스트')">신고리스트</v-btn>
          </v-row>
        </v-col>
        <v-col class="text-right">
          <v-btn icon @click="handleAccountClick">
            <v-icon>mdi-account</v-icon>
          </v-btn>
          <v-btn icon class="teen_red_font">
            <v-badge
              color="red"
              :content="unreadNotificationsCount"
              overlap
              v-if="unreadNotificationsCount > 0"
            >
              <v-icon>mdi-bell</v-icon>
            </v-badge>
            <v-icon v-else>mdi-bell</v-icon>

            <v-menu activator="parent" offset-y>
              <v-list max-width="300" max-height="400" style="overflow-y: auto;">
                <template v-if="unreadNotifications.length > 0">
                  <v-list-item
                    v-for="(notification, index) in unreadNotifications"
                    :key="index"
                    :class="{'unread-notification': notification.delYN === 'N'}"
                    @click="markAsReadAndNavigate(notification, index)"
                  >
                    <v-list-item-content>
                      <v-list-item-title>{{ notification.message }}</v-list-item-title>
                    </v-list-item-content>
                  </v-list-item>
                </template>
                <template v-else>
                  <v-list-item>
                    <v-list-item-content>
                      <v-list-item-title>알림이 없습니다</v-list-item-title>
                    </v-list-item-content>
                  </v-list-item>
                </template>
              </v-list>
            </v-menu>
          </v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-app-bar>
</template>

<script>
import { EventSourcePolyfill } from 'event-source-polyfill';
import axios from 'axios';

export default {
  name: 'HeaderComponent',
  data() {
    return {
      logo: require('@/assets/images/ico_logo.png'),
      isLogin: false, // 초기값 설정
      isAdmin: false, // 관리자인지 여부를 확인하기 위한 변수 추가
      notifications: [],
    };
  },
  computed: {
    unreadNotificationsCount() {
      return this.notifications.filter(notification => notification.delYN === 'N').length;
    },
    unreadNotifications() {
      return this.notifications.filter(notification => notification.delYN === 'N');
    }
  },
  mounted() {
    const token = localStorage.getItem('token');
    this.isLogin = !!token;

    if (this.isLogin) {
      this.isAdmin = localStorage.getItem('role') === 'ADMIN'; // 로그인 후 역할이 ADMIN인지 확인
      this.fetchNotifications();

      const eventSource = new EventSourcePolyfill(`${process.env.VUE_APP_API_BASE_URL}/subscribe`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      eventSource.addEventListener('notification', (event) => {
        const notification = JSON.parse(event.data);
        this.notifications.push(notification);
      });

      eventSource.onerror = (error) => {
        console.error('SSE 연결 오류:', error);
      };
    }
  },
  methods: {
    async fetchNotifications() {
      if(this.isLogin){
        try {
          const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/api/notifications/list`, {
            headers: {
              Authorization: `Bearer ${localStorage.getItem('token')}`,
            },
          });

          // 알림을 id 기준으로 내림차순 정렬
          this.notifications = response.data.sort((a, b) => b.id - a.id);
        } catch (error) {
          console.error('알림 목록을 가져오는 중 오류 발생:', error);
        }
      }
    },
    async markAsReadAndNavigate(notification, index) {
      if (notification.delYN === 'N') {
        try {
          const id = notification.id;
          await axios.get(`${process.env.VUE_APP_API_BASE_URL}/api/notifications/update/${id}`, null, {
            headers: {
              Authorization: `Bearer ${localStorage.getItem('token')}`,
            },
          });
          this.notifications[index].delYN = 'Y';

          if (notification.postId) {
            window.location.href = `/board/detail/post/${notification.postId}`;
          } else if (notification.qnaId) {
            window.location.href = `/qna/detail/${notification.qnaId}`;
          } else if (notification.reportId) {
            window.location.href = `/report/list`;
          } else {
            console.log('error!!!!!!!!!!!');
          }
        } catch (error) {
          console.error('알림을 읽음으로 표시하는 중 오류 발생:', error);
        }
      }
    },
    navigate(section) {
      if (section === '강좌') {
        this.$router.push({ name: 'SubjectList', params: { category: 'subject' } });
      } else if (section === '추천') {
        this.$router.push({ name: 'SubjectRecommendList', params: { category: 'subjectRecommend' } });
      } else if (section === '이벤트') {
        this.$router.push({ name: 'BoardList', params: { category: 'event' } });
      } else if (section === '공지사항') {
        this.$router.push({ name: 'BoardList', params: { category: 'notice' } });
      } else if (section === '자유게시판') {
        this.$router.push({ name: 'BoardList', params: { category: 'post' } });
      } else if (section === 'QnA') {
        this.$router.push({ name: 'QnaList', params: { category: 'qna' } });
      } else if (section === '신고리스트') {
        if (this.isAdmin) {
          this.$router.push({ name: 'ReportList', params: { category: 'report' } });
        } else {
          console.log('관리자만 접근할 수 있습니다.');
        }
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
    handleAccountClick() {
      if (this.isLogin) {
        this.$emit('open-sidebar');
      } else {
        alert("로그인 후 사용이 가능합니다.");
        this.$router.push('/login');
      }
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

.unread-notification {
  background-color: white;
}

.v-list-item {
  background-color: white;
}
</style>
