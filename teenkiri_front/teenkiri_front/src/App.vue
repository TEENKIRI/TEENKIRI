<template>
  <div id="app">
    <nav>
      <router-link to="/login">로그인</router-link>
      <router-link to="/register">회원가입</router-link>
      <router-link to="/mypage">마이페이지</router-link>
    </nav>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
};
</script>

<style>
nav {
  display: flex;
  gap: 10px;
}
</style>
