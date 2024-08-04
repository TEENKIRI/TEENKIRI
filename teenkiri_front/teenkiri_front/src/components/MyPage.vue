<template>
  <div>
    <h1>My Page</h1>
    <div v-if="user">
      <p><strong>Username:</strong> {{ user.username }}</p>
      <p><strong>Email:</strong> {{ user.email }}</p>
      <p><strong>Nickname:</strong> {{ user.nickname }}</p>
      <p><strong>Address:</strong> {{ user.address }}</p>
      <p><strong>Phone:</strong> {{ user.phone }}</p>
      <!-- Add more fields as needed -->
    </div>
    <div v-else>Loading...</div>
    <div v-if="errorMessage">{{ errorMessage }}</div>
  </div>
</template>

<script>
import useAuth from '@/utils/useAuth';

export default {
  setup() {
    const { user, errorMessage } = useAuth();
    return {
      user,
      errorMessage
    };
  }
};
</script>
