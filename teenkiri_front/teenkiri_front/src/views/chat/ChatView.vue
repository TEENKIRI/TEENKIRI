<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <h1>Chat Room</h1>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <ChatComponent />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import ChatComponent from '@/components/ChatComponent.vue';

export default {
  components: {
    ChatComponent
  }
};
</script>

<style scoped>
.v-container {
  max-width: 800px;
  margin: auto;
  padding-top: 20px;
}

h1 {
  text-align: center;
  margin-bottom: 20px;
}
</style>
