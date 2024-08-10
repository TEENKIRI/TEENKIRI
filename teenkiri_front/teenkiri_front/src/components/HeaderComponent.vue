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
          </v-row>
        </v-col>
        <v-col cols="3" class="text-right">
          <v-btn icon @click="goToMember">
            <v-icon>mdi-account</v-icon>
          </v-btn>
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
      logo: require('@/assets/images/ico_logo.png')
    };
  },
  methods: {
    navigate(section) {
      if (section === '이벤트') {
        this.$router.push({ name: 'EventBoard' });
      } else {
        console.log(section);
        // 다른 섹션에 대한 처리 추가 가능
      }
    },
    goToMember() {
      console.log('Go to member');
    },
    goToMenu() {
      console.log('Go to menu');
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
