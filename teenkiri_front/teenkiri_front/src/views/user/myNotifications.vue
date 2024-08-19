<template>
  <div class="notifications-container">
    <h1>받은 알림 내역</h1>
      <div v-if="notifications.length" class="notifications-list">
        <div v-for="notification in notifications" :key="notification.id" class="notification-item">
          <p class="notification-message">{{ notification.message }}</p>
          <button @click="markAsRead(notification.id)" class="mark-read-button" v-if="notification.delYN === 'N'">읽음으로 표시</button>
        </div>
      </div>
  <p v-else>알림이 없습니다.</p>
  </div>
</template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        notifications: [],
      };
    },
    async mounted() {
      await this.fetchNotifications();
    },
    methods: {
      async fetchNotifications() {
        try {
          const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/api/notifications/list/all`);
          console.log('API 응답 데이터:', response.data); // 데이터 구조 확인
          // 응답 데이터가 배열인지 확인 후 할당
          this.notifications = Array.isArray(response.data) ? response.data : [];
        } catch (error) {
          if (error.response) {
            console.error('서버 응답 오류:', error.response.data.message || error.response.statusText);
          } else if (error.request) {
            console.error('요청 오류:', error.request);
          } else {
            console.error('오류 발생:', error.message);
          }
        }
      },
      async markAsRead(id) {
        try {
          await axios.get(`${process.env.VUE_APP_API_BASE_URL}/api/notifications/update/${id}`);
          this.notifications = this.notifications.map(notification =>
            notification.id === id ? { ...notification, delYN: 'Y' } : notification
          );
        } catch (error) {
          console.error('알림 상태 업데이트 중 오류 발생:', error);
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .notifications-container {
    padding: 20px;
    max-width: 800px;
    margin: 0 auto;
    border: 1px solid #ddd;
    border-radius: 8px;
    background-color: #f9f9f9;
  }
  
  h1 {
    text-align: center;
    margin-bottom: 20px;
  }
  
  .notifications-list {
    list-style-type: none;
    padding: 0;
  }
  
  .notification-item {
    padding: 10px;
    margin-bottom: 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
    background-color: #fff;
  }
  
  .notification-message {
    margin: 0;
  }
  
  .mark-read-button {
    background-color: #007bff;
    color: #fff;
    border: none;
    padding: 5px 10px;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .mark-read-button:hover {
    background-color: #0056b3;
  }
  </style>
  