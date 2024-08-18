<template>
    <v-container class="mt-5">
      <h2 class="text-h4 font-weight-bold mb-4">알림 내역</h2>
  
      <v-list two-line>
        <v-list-item
          v-for="(notification, index) in paginatedNotifications"
          :key="notification.id"
          class="notification-item"
        >
          <v-list-item-content>
            <v-list-item-title class="font-weight-medium">
              {{ index + 1 + (currentPage - 1) * itemsPerPage }}. {{ notification.message }}
            </v-list-item-title>
          </v-list-item-content>
          <v-list-item-action>
            <v-btn icon @click="viewDetail(notification)">
              <v-icon>mdi-arrow-right-bold-circle-outline</v-icon> <!-- 바뀐 아이콘 -->
            </v-btn>
          </v-list-item-action>
        </v-list-item>
      </v-list>
  
      <!-- 페이지네이션 -->
      <v-row justify="center" class="mt-4">
        <v-pagination
          v-model="currentPage"
          :length="totalPages"
          @input="fetchNotifications"
        ></v-pagination>
      </v-row>
    </v-container>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        notifications: [],
        currentPage: 1,
        itemsPerPage: 20,
        totalPages: 1,
      };
    },
    created() {
      this.fetchNotifications();
    },
    computed: {
      paginatedNotifications() {
        const start = (this.currentPage - 1) * this.itemsPerPage;
        const end = start + this.itemsPerPage;
        return this.notifications.slice(start, end);
      },
    },
    methods: {
      async fetchNotifications() {
        try {
          const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/user/notification`);
          if (response.status === 200) {
            this.notifications = response.data.result;
            this.totalPages = Math.ceil(this.notifications.length / this.itemsPerPage);
          } else {
            console.error('알림 목록을 불러오는 데 실패했습니다:', response.data.message || response.statusText);
          }
        } catch (error) {
          console.error('알림 목록을 불러오는 중 오류가 발생했습니다:', error);
        }
      },
      viewDetail(notification) {
        if (notification.qnaId) {
          this.$router.push(`/qna/detail/${notification.qnaId}`);
        } else if (notification.postId) {
          this.$router.push(`/post/detail/${notification.postId}`);
        } else if (notification.reportId) {
          this.$router.push(`/report/detail/${notification.reportId}`);
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .v-container {
    max-width: 800px;
    margin: 0 auto;
  }
  
  .notification-item {
    border-bottom: 1px solid #e0e0e0;
    transition: background-color 0.2s;
  }
  
  .notification-item:hover {
    background-color: #f5f5f5;
  }
  
  .v-list-item-title {
    font-size: 16px;
    color: #424242;
  }
  
  .v-list-item-action .v-btn {
    color: #1e88e5;
  }
  
  .v-pagination {
    margin-top: 20px;
  }
  </style>
  