<template>
  <v-container class="chat-container">
    <!-- Close Button -->
    <v-btn icon @click="closeChat" class="close-button">
      <v-icon>mdi-close</v-icon>
    </v-btn>

    <!-- Chat Messages -->
    <v-list class="chat-box">
      <v-list-item
        v-for="message in messages"
        :key="message.id"
        :class="{'my-message': isMyMessage(message.senderId), 'other-message': !isMyMessage(message.senderId)}"
        class="message-item"
      >
        <!-- 자신이 보낸 메시지에는 신고 버튼을 숨김 -->
        <v-btn v-if="!isMyMessage(message.senderId)" icon small class="report-button" @click="reportMessage(message)">
          <v-icon small>mdi-alarm-light-outline</v-icon>
        </v-btn>
        <v-list-item-content>
          <v-list-item-title>{{ message.senderNickname }}</v-list-item-title>
          <v-list-item-subtitle class="message-content">
            {{ message.content }}
          </v-list-item-subtitle>
          <v-list-item-subtitle :class="['message-timestamp', { 'left-align': !isMyMessage(message.senderId) }]">
            {{ formatTime(message.createdTime) }}
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-list>

    <!-- Message Input and Send Button -->
    <div class="message-input-wrapper">
      <v-text-field
        v-model="newMessage"
        label="메시지를 입력하세요..."
        hide-details
        dense
        class="message-input"
        @keyup.enter="sendMessage"
      ></v-text-field>
      <v-btn @click="sendMessage" class="send-button" color="primary">전송</v-btn>
    </div>

    <!-- Report Create Modal -->
    <ReportCreate v-if="showReportModal" :chatMessageId="selectedChatMessageId" @close="closeReportModal" />
  </v-container>
</template>

<script>
import ReportCreate from '@/views/report/ReportCreate.vue';  // 모달 컴포넌트 임포트
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
      loginTime: new Date().toISOString().slice(0, 19),
      showReportModal: false,
      selectedChatMessageId: null,
    };
  },
  mounted() {
    this.loadChatHistory();
    this.connectWebSocket();
  },
  methods: {
    async loadChatHistory() {
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/api/chat/messages`, {
          params: { since: this.loginTime },
        });
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
    formatTime(datetime) {
      const date = new Date(datetime);
      return `${date.getHours()}:${String(date.getMinutes()).padStart(2, '0')}`;
    },
    closeChat() {
      console.log("Chat closed");
    },
    reportMessage(message) {
      this.selectedChatMessageId = message.id;
      this.showReportModal = true;
    },
    closeReportModal() {
      this.showReportModal = false;
    }
  },
  components: {
    ReportCreate,
  }
};
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 500px; /* 높이를 고정 */
  width: 580px; /* 가로 크기를 고정 */
  margin: 20px auto;
  background: #f9f9f9;
  border-radius: 8px;
  overflow: hidden;
  padding: 10px;
  position: relative;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
}

.chat-box {
  flex-grow: 1;
  padding: 10px;
  background: rgb(255, 255, 255);
  display: flex;
  flex-direction: column;
  gap: 15px;
  overflow-y: auto;
  overflow-x: hidden; /* 가로 스크롤 안보이게 설정 */
}

.message-item {
  display: flex;
  align-items: flex-start;
  position: relative;
}

.report-button {
  position: absolute;
  right: -30px; /* 말풍선 오른쪽으로 이동 */
  top: 50%;
  transform: translateY(-50%);
  width: 40px;
  height: 20px;
  font-size: 14px;
  color: #f44336;
}

.message-input-wrapper {
  display: flex;
  align-items: center;
  border-top: 1px solid #ccc;
  padding: 10px;
  background: #f9f9f9;
}

.message-input {
  flex-grow: 1;
  border: none;
  padding: 10px;
  margin-right: 10px;
  height: 40px;
  background: #f9f9f9;
}

.send-button {
  width: auto;
  height: 40px;
  padding: 0 15px;
}

.message-wrapper {
  display: flex;
  flex-direction: column;
  position: relative;
  max-width: 50%; /* 말풍선 가로 크기를 줄임 */
}

.message-content {
  font-size: 1rem;
  white-space: pre-wrap;
  word-wrap: break-word;
  word-break: break-word;
  max-width: 100%;
}

.my-message {
  background-color: #ffeb3b;
  align-self: flex-end;
  text-align: right;
}

.other-message {
  background-color: #e5f1fb;
  align-self: flex-start;
  text-align: left;
}

.message-sender {
  font-size: 0.8em;
  color: gray;
  margin-bottom: 5px;
}

.message-timestamp {
  font-size: 0.8em;
  color: gray;
  margin-top: 5px;
  text-align: right;
}

.left-align {
  text-align: left !important; /* 왼쪽 정렬을 강제로 적용 */
  padding-left: 0; /* 왼쪽으로 붙이기 위해 패딩 제거 */
  margin-left: 0; /* 왼쪽으로 붙이기 위해 마진 제거 */
}
</style>

