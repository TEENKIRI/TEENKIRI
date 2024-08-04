
import { ref, onMounted, computed } from 'vue';
import { useStore } from 'vuex';

export default function useAuth() {
  const store = useStore();
  const errorMessage = ref('');

  onMounted(async () => {
    try {
      await store.dispatch('fetchUser');
    } catch (error) {
      errorMessage.value = error.response ? error.response.data.message : error.message;
    }
  });

  const user = computed(() => store.state.user);

  return {
    user,
    errorMessage
  };
}
