<template>
  <v-container class="chat-container">
    <!-- 닫기 버튼 -->
    <v-btn icon @click="closeChat" class="close-button">
      <v-icon>mdi-close</v-icon>
    </v-btn>

    <!-- 채팅 메시지 -->
    <v-list class="chat-box">
      <v-list-item
        v-for="message in filteredMessages"
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
            {{ filterMessage(message.content) }} <!-- 필터링된 메시지 표시 -->
          </v-list-item-subtitle>
          <v-list-item-subtitle :class="['message-timestamp', { 'left-align': !isMyMessage(message.senderId) }]">
            {{ formatTime(message.createdTime) }}
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-list>

    <!-- 메시지 입력 및 전송 버튼 -->
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

    <!-- 주제 선택 버튼 -->
    <div class="topic-buttons">
      <v-btn
        v-for="topic in topics"
        :key="topic"
        @click="subscribeToTopic(topic)"
        :class="{'selected-topic': selectedTopic === topic}"
        class="topic-button"
      >
        {{ topicNames[topic] }}
      </v-btn>
    </div>

    <!-- 신고 작성 모달 -->
    <ReportCreate v-if="showReportModal" :chatMessageId="selectedChatMessageId" @close="closeReportModal" />
  </v-container>
</template>

<script>
import ReportCreate from '@/views/report/ReportCreate.vue'; // 모달 컴포넌트 임포트
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
      topics: ['/topic/korean', '/topic/english', '/topic/math', '/topic/social', '/topic/science'],
      topicNames: {
        '/topic/korean': '국어',
        '/topic/english': '영어',
        '/topic/math': '수학',
        '/topic/social': '사회',
        '/topic/science': '과학',
      },
      selectedTopic: '/topic/korean', // 기본 선택 주제
      forbiddenWords: [], // 필터링할 금지된 단어 목록
    };
  },
  computed: {
    filteredMessages() {
      // 현재 선택된 주제에 맞는 메시지만 필터링
      const currentChannel = this.selectedTopic.replace('/topic/', '');
      return this.messages.filter(message => message.channel === currentChannel);
    }
  },
  mounted() {
    this.loadChatHistory();
    this.connectWebSocket();
    this.loadForbiddenWords(); // 금지된 단어 목록 로드
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
    async loadForbiddenWords() {
      try {
        const response = await axios.get('/badwords.txt'); // 로컬 경로에서 badwords.txt 파일을 불러옵니다.
        this.forbiddenWords = response.data.split('\n').map(word => word.trim()).filter(word => word); // 줄바꿈을 기준으로 단어를 분리하고, 공백을 제거
        
        // 금지된 단어 목록을 콘솔에 출력하여 확인
        console.log('Loaded forbidden words:', this.forbiddenWords);
      } catch (error) {
        if (error.response && error.response.status === 404) {
          console.error('Error 404: Resource not found. Please check the file path.');
        } else {
          console.error('An unexpected error occurred:', error.message);
        }
      }
    },
    connectWebSocket() {
      const socket = new SockJS(`${process.env.VUE_APP_API_BASE_URL}/ws`);
      this.stompClient = Stomp.over(socket);

      this.stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);

        // 기본 주제를 구독합니다.
        this.subscribeToTopic(this.selectedTopic);
      }, (error) => {
        console.error('웹소켓 연결 실패:', error);
      });
    },
    subscribeToTopic(topic) {
      if (this.stompClient) {
        // 기존 구독을 해제합니다.
        if (this.selectedTopic) {
          this.stompClient.unsubscribe(this.selectedTopic);
        }

        // 새로운 주제에 대해서만 구독합니다.
        this.selectedTopic = topic;
        this.stompClient.subscribe(topic, (message) => {
          const receivedMessage = JSON.parse(message.body);

          // 메시지를 모두 추가합니다. 필터링은 computed 속성에서 수행
          this.messages.push(receivedMessage);
        });
      }
    },
    filterMessage(content) {
  this.forbiddenWords.forEach(word => {
    // 각 문자의 사이에 다양한 특수 문자가 올 수 있도록 허용하는 정규식
    const regex = new RegExp(
      word
        .split('')
        .map(char => `[${char}]+[^\\w\\s]*`)  // 각 글자 사이에 어떤 문자가 오든 필터링 되도록 처리
        .join(''),
      'gi'
    );
    content = content.replace(regex, '*'.repeat(word.length));
  });

  // 필터링된 내용을 콘솔에 출력하여 확인
  console.log('Filtered content:', content);
  return content;
},

    sendMessage() {
      if (!this.userId) {
        console.error('User ID is not available in localStorage');
        return;
      }

      // 채널이 올바르게 설정되어 있는지 확인
      const channel = this.selectedTopic ? this.selectedTopic.replace('/topic/', '') : '';
      if (!channel) {
        console.error('Channel is not set.');
        alert('채널이 설정되지 않았습니다.');
        return;
      }

      if (this.stompClient && this.stompClient.connected) {
        // 사용자가 입력한 메시지 내용을 필터링
        const filteredContent = this.filterMessage(this.newMessage);

        const message = {
          content: filteredContent,  // 필터링된 내용을 사용
          senderId: this.userId,
          channel: channel
        };

        this.stompClient.send(`/app/chat.sendMessage`, {}, JSON.stringify(message));
        this.newMessage = '';  // 메시지를 전송한 후 입력창을 비웁니다.
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
  height: 800px; /* 높이를 약간 더 늘려서 버튼이 들어갈 공간을 확보 */
  width: 550px; /* 가로 크기를 고정 */
  margin: 20px auto;
  background: #f9f9f9;
  border-radius: 8px;
  overflow: hidden;
  padding: 7px;
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
  margin-bottom: 0px; /* 메시지 입력 창과 버튼 사이의 간격 */
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

.topic-buttons {
  display: flex;
  justify-content: center;
  gap: 10px;
  padding: 10px 0;
  margin-top: 50px; /* 메시지 입력 창과 버튼 사이의 간격 */
  margin-bottom: 0px;
  background: #f9f9f9; /* 버튼 배경 색상 */
}

.topic-button {
  min-width: 80px;
}

.selected-topic {
  background-color: #3f51b5 !important;
  color: white !important;
}
</style>
