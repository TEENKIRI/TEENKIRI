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
        </v-col>
      </v-row>
    </v-container>
  </v-app-bar>
</template>

<script>
export default {
  name: 'HeaderComponent',
  data() {
    return {
      logo: require('@/assets/images/ico_logo.png'),
      isLogin: false // 초기값 설정
    };
  },
  mounted() {
    // 로컬 스토리지에서 토큰을 읽어 로그인 상태를 설정
    const token = localStorage.getItem('token');
    this.isLogin = !!token;
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
      } else if (section === 'QnA')  {
        this.$router.push({ name: 'QnaList', params: {category: 'qna'}});
      } else {
        console.log(section);
        // 다른 섹션에 대한 처리 추가 가능
      }
    },
    goToMember() {
      if (this.isLogin) {
        // 로그인된 상태에서 사용자가 클릭할 때
        this.$router.push('/user/edit-info'); // 로그인 후 이동할 페이지
      } else {
        // 로그인되지 않은 상태에서 사용자가 클릭할 때
        this.$router.push('/login'); // 로그인 페이지로 이동
      }
    },
    goToMenu() {
      console.log('Go to menu');
    },
    doLogout() {
      localStorage.removeItem('role');
      localStorage.removeItem('token'); // 로컬 스토리지에서 토큰 삭제
      this.isLogin = false; // 로그인 상태 업데이트
      console.log('Logged out');
      window.location.reload(); // 페이지 새로고침
    }
  }
}
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
