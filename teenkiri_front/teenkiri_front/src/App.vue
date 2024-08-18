<!-- App.vue -->
<template>
  <v-app class="app">
    <HeaderComponent @open-sidebar="toggleSidebar" />
    <AppSidebar ref="sidebar" />
    <v-main class="main-content">
      <router-view />
    </v-main>
    <FooterComponent />
  </v-app>
</template>

<script>
import HeaderComponent from './components/HeaderComponent.vue';
import FooterComponent from './components/FooterComponent.vue';
import AppSidebar from './components/AppSidebar.vue';

export default {
  name: 'App',
  components: {
    HeaderComponent,
    FooterComponent,
    AppSidebar
  },
  methods: {
    toggleSidebar() {
      this.$refs.sidebar.toggleSidebar();
    }
  }
}
</script>

<style>
.app {
  display: flex;
  flex-direction: column;
  min-height: 100vh; /* 화면 높이의 100%를 차지하도록 설정 */
}
.main-content {
  flex: 1; /* 콘텐츠가 footer 위에 표시되도록 설정 */
}
</style>
