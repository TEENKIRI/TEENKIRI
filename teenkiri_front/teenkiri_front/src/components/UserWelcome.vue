<template>
  <div>
    <h1>Welcome, {{ user.username }}!</h1>
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
