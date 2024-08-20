<template>
    <v-container>
      <v-list>
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
        this.stompClient.subscribe('/topic/public', tick => {
          const message = JSON.parse(tick.body);
          this.messages.push(message);
        });
      });
    },
    sendMessage() {
      const userId = localStorage.getItem('userId'); // userId를 localStorage에서 가져오기

      if (!userId) {
        console.error("User ID is not available in localStorage");
        return;
      }

      if (this.stompClient && this.stompClient.connected) {
        const message = {
          content: this.newMessage,
          senderId: userId, // userId를 senderId로 설정
        };
        this.stompClient.send('/app/chat.sendMessage', {}, JSON.stringify(message));
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
  