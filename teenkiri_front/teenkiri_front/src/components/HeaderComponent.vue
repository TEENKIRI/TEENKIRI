<template>
  <v-app-bar app>
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
              <!-- 알림 목록 -->
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

          <v-btn icon @click="showChatModal = true">
            <v-icon>mdi-chat</v-icon>
          </v-btn>

          <v-dialog v-model="showChatModal" max-width="600px">
            <v-card>
              <v-card-title class="headline">채팅</v-card-title>
              <v-card-text>
                <ChatComponent />
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" @click="showChatModal = false">닫기</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>


          <v-btn icon @click="goToDeleteAccountPage" v-if="isLogin" class="mr-2">
            <v-icon>mdi-account-remove</v-icon>
          </v-btn>

        </v-col>
      </v-row>
    </v-container>
  </v-app-bar>

  <!-- 로그인 안내 스낵바 (중앙 모달 스타일) -->
  <v-snackbar color="white"
    v-model="loginSnackbar" 
    :timeout="3000"
    class="custom-snackbar"
    top
  >
    <div class="snackbar-content">
      로그인을 하셔야 해당 기능을 사용할 수 있습니다.
      <v-btn color="#6fc8b8" text @click="goToLoginPage">로그인</v-btn>
      <v-btn color="#6fc8b8" text @click="closeLoginSnackbar">닫기</v-btn>
    </div>
  </v-snackbar>
</template>

<script>
import { EventSourcePolyfill } from 'event-source-polyfill';
import axios from 'axios';
import ChatComponent from '@/components/ChatComponent.vue'; // 채팅 컴포넌트 추가
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

export default {
  name: 'HeaderComponent',
  components: {
    ChatComponent, // ChatComponent를 components에 추가
  },
  data() {
    return {
      showChatModal: false, // 채팅 모달 제어를 위한 데이터
      logo: require('@/assets/images/ico_logo.png'),
      isLogin: false,
      isAdmin: false,
      notifications: [],
      loginSnackbar: false, // 로그인 안내 스낵바 상태
      stompClient: null,    // WebSocket 클라이언트
      connected: false,     // WebSocket 연결 상태
    };
  },
  computed: {
    unreadNotificationsCount() {
      return this.notifications.filter((notification) => notification.delYN === 'N').length;
    },
    unreadNotifications() {
      return this.notifications.filter((notification) => notification.delYN === 'N');
    },
  },
  watch: {
    '$route': 'getIsUserCertificate', // router가 변경될 때마다 호출
  },
  mounted() {
    const token = localStorage.getItem('token');
    this.isLogin = !!token;

    if (this.isLogin) {
      this.isAdmin = localStorage.getItem('role') === 'ADMIN';
      this.fetchNotifications();
      this.initEventSource(token);
      this.connectWebSocket();  // WebSocket 연결 설정
    }
  },
  methods: {
    async getIsUserCertificate(){
      const path = this.$route.path; // 현재 URL의 경로
      const isContain= ['create', 'edit', 'detail'].some(keyword => path.includes(keyword));
      console.log("이 주소는?", path, "포함여부는? : ", isContain)
      if(isContain){ // 주소에 위 3개의 배열 중 하나가 있다면
        if(this.isLogin){ // 로그인이 되어있다면
         
        }
      }
    },


    connectWebSocket() {
      const socket = new SockJS(`${process.env.VUE_APP_API_BASE_URL}/ws`);
      this.stompClient = Stomp.over(socket);

      this.stompClient.connect({}, () => {
        this.connected = true;
        this.stompClient.subscribe("/topic/logout", (message) => {
          const email = message.body;
          if (email === this.getUserEmail()) {  
            alert("누적 신고 횟수가 5회 이상이므로 계정이 영구 정지 되었습니다.");
            this.logout();
          }
        });
      }, (error) => {
        console.error('WebSocket 연결 오류:', error);
        this.connected = false;
      });
    },
    getUserEmail() {
      return localStorage.getItem("email");
    },
    logout() {
      localStorage.removeItem("token");
      localStorage.removeItem("refreshToken");
      localStorage.removeItem("userId");
      localStorage.removeItem("email");
      localStorage.removeItem("role");
      this.$router.push("/login");
    },
    initEventSource(token) {
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
    },
    async fetchNotifications() {
      if (this.isLogin) {
        try {
          const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/api/notifications/list`, {
            headers: {
              Authorization: `Bearer ${localStorage.getItem('token')}`,
            },
          });

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
    goToDeleteAccountPage() {
        this.$router.push({ name: 'DeleteAccount' });
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
        this.loginSnackbar = true; // 로그인 안내 스낵바 열기
      }
    },
    closeLoginSnackbar() {
      this.loginSnackbar = false;
    },
    goToLoginPage() {
      this.loginSnackbar =false
      this.$router.push('/login');
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

.custom-snackbar {
  color:#ffdb69 !important;
  margin: auto;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border-radius: 8px;

}

.v-snackbar--variant-elevated, 
.v-snackbar--variant-flat {
  background-color: white !important; /* 흰색 배경 */
}
</style>
