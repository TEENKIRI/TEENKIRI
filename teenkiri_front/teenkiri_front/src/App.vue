<!-- App.vue -->
<template>
  <v-app>
    <HeaderComponent @open-sidebar="toggleSidebar" />
    <AppSidebar ref="sidebar" />
    <v-main>
      <router-view />
      <FooterComponent />
    </v-main>
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
