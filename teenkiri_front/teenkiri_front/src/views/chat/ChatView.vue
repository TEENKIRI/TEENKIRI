<template>
  <v-container>
    <v-alert v-if="!userId || !email" type="error">
      유저 정보가 없습니다. 다시 로그인 해주세요.
    </v-alert>

    <v-list v-if="userId && email">
      <v-list-item v-for="message in messages" :key="message.id">
        <v-list-item-content>
          <v-list-item-title>{{ message.senderNickname }}</v-list-item-title>
          <v-list-item-subtitle>{{ message.content }}</v-list-item-subtitle>
          <v-list-item-subtitle>{{ message.createdTime }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-list>

    <v-text-field
      v-model="newMessage"
      label="메시지를 입력하세요..."
      @keyup.enter="sendMessage"
    ></v-text-field>
  </v-container>
</template>

<script>
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

export default {
  data() {
    return {
      messages: [],
      newMessage: '',
      stompClient: null,
      channel: 'science',
      userId: localStorage.getItem('userId'),
      email: localStorage.getItem('email')
    };
  },
  mounted() {
    this.connectWebSocket();
  },
  methods: {
    connectWebSocket() {
      const socket = new SockJS(`${process.env.VUE_APP_API_BASE_URL}/ws`);
      this.stompClient = Stomp.over(socket);

      this.stompClient.connect({}, frame => {
        console.log('Connected: ' + frame);
        this.stompClient.subscribe(`/topic/${this.channel}`, tick => {
          const message = JSON.parse(tick.body);
          this.messages.push(message);
        });
      });
    },
    sendMessage() {
      if (!this.userId || !this.email) {
        console.error("유저 정보가 없습니다.");
        return;
      }

      const message = {
        content: this.newMessage,
        senderId: this.userId,
        senderEmail: this.email,
        channel: this.channel,
      };

      console.log('Sending message:', message);  

      if (this.stompClient && this.stompClient.connected) {
        this.stompClient.send(`/app/chat.sendMessage`, {}, JSON.stringify(message));
        this.newMessage = '';
      }
    }
  },
  beforeUnmount() {
    if (this.stompClient) {
      this.stompClient.disconnect();
    }
  },
};
</script>

<style scoped>
.v-container {
  max-width: 800px;
  margin: auto;
}

.v-list-item {
  border-bottom: 1px solid #e0e0e0;
  padding-bottom: 10px;
  margin-bottom: 10px;
}
</style>
