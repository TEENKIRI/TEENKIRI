<template>
    <v-app-bar app>
      <v-toolbar-title>
        <a href="javascript:void(0)" class="logo">로고</a>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn text @click="navigate('강좌')">강좌</v-btn>
      <v-btn text @click="navigate('추천')">추천</v-btn>
      <v-btn text @click="navigate('이벤트')">이벤트</v-btn>
      <v-btn text @click="navigate('공지사항')">공지사항</v-btn>
      <v-spacer></v-spacer>
      <v-btn icon @click="goToMember">
        <v-icon>mdi-account</v-icon>
      </v-btn>
      <v-btn icon @click="goToMenu">
        <v-icon>mdi-menu</v-icon>
      </v-btn>
    </v-app-bar>
  </template>
  
  <script>
  export default {
    name: 'HeaderComponent',
    methods: {
      navigate(section) {
        console.log(section);
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
  </style>
  