<template>
    <v-container>
      <v-list>
        <v-list-item
          v-for="message in messages"
          :key="message.id"
          :class="{'my-message': isMyMessage(message.senderId), 'other-message': !isMyMessage(message.senderId)}"
        >
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
  import axios from 'axios';
  import SockJS from 'sockjs-client';
  import { Stomp } from '@stomp/stompjs';
  
  export default {
    data() {
      return {
        messages: [],
        newMessage: '',
        stompClient: null,
        userId: localStorage.getItem('userId'),
      };
    },
    mounted() {
      this.loadChatHistory();
      this.connectWebSocket();
    },
    methods: {
      async loadChatHistory() {
        try {
          const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/api/chat/messages`);
          this.messages = response.data;
        } catch (error) {
          console.error('채팅 기록을 불러오는 중 오류 발생:', error);
        }
      },
      connectWebSocket() {
        const socket = new SockJS(`${process.env.VUE_APP_API_BASE_URL}/ws`);
        this.stompClient = Stomp.over(socket);
  
        this.stompClient.connect({}, (frame) => {
          console.log('Connected: ' + frame);
          this.stompClient.subscribe('/topic/public', (tick) => {
            const message = JSON.parse(tick.body);
            this.messages.push(message);
          });
        });
      },
      sendMessage() {
        if (!this.userId) {
          console.error('User ID is not available in localStorage');
          return;
        }
  
        if (this.stompClient && this.stompClient.connected) {
          const message = {
            content: this.newMessage,
            senderId: this.userId, 
          };
          this.stompClient.send('/app/chat.sendMessage', {}, JSON.stringify(message));
          this.newMessage = '';
        }
      },
      isMyMessage(senderId) {
        return senderId == this.userId;
      },
    },
    beforeUnmount() {
      if (this.stompClient) {
        this.stompClient.disconnect();
      }
    },
  };
  </script>
  
  <style scoped>
  .v-list-item {
    padding: 10px;
    margin-bottom: 10px;
    border-radius: 10px;
    max-width: 60%;
  }
  
  .my-message {
    background-color: #2196f3; 
    color: white;
    margin-left: auto;
    text-align: right;
  }
  
  .other-message {
    background-color: #f44336;
    color: white;
    margin-right: auto;
    text-align: left;
  }
  </style>
  