<template>
  <div>
    <form @submit.prevent="loginUser">
      <label for="email">Email:</label>
      <input type="email" v-model="email" required>
      <label for="password">Password:</label>
      <input type="password" v-model="password" required>
      <button type="submit">Login</button>
    </form>
    <div v-if="errorMessage">{{ errorMessage }}</div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      email: '',
      password: '',
      errorMessage: ''
    };
  },
  methods: {
    async loginUser() {
      try {
        await this.$store.dispatch('loginUser', {
          email: this.email,
          password: this.password
        });
        this.$router.push('/user-welcome');
      } catch (error) {
        this.errorMessage = error.status_message || error.message;
      }
    }
  }
};
</script>
