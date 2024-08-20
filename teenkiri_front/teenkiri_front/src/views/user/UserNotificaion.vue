<template>
    <v-app>
      <v-container>
        <h1 class="board-title">알림 내역</h1>
  
        <table class="tbl_list">
          <caption></caption>
          <colgroup>
            <col width="80" />
            <col width="auto" />
            <col width="160" />
          </colgroup>
          <thead>
            <tr>
              <th>번호</th>
              <th>알림 내용</th>
              <th>보기</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(notification, index) in paginatedNotifications" :key="notification.id">
              <td>{{ index + 1 + (currentPage - 1) * itemsPerPage }}</td>
              <td class="text_left">{{ notification.message }}</td>
              <td>
                <v-btn @click="viewDetail(notification)" class="btn_view" color="grey">
                  보기
                </v-btn>
              </td>
            </tr>
          </tbody>
        </table>
  
        <div class="pagingWrap">
          <ul>
            <li><a href="javascript:void(0)" @click="goToPage(1)" class="btn_paging_start"></a></li>
            <li><a href="javascript:void(0)" @click="goToPreviousPage" class="btn_paging_prev"></a></li>
            <li v-for="page in totalPages" :key="page">
              <a href="javascript:void(0)" @click="goToPage(page)" :class="{ btn_paging: true, active: currentPage === page }">{{ page }}</a>
            </li>
            <li><a href="javascript:void(0)" @click="goToNextPage" class="btn_paging_next"></a></li>
            <li><a href="javascript:void(0)" @click="goToPage(totalPages)" class="btn_paging_end"></a></li>
          </ul>
        </div>
      </v-container>
    </v-app>
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
          console.log(this.response)
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
          this.$router.push(`/board/detail/post/${notification.postId}`);
        } else if (notification.reportId) {
          this.$router.push(`/report/detail/${notification.reportId}`);
        }
      },
      goToPage(page) {
        this.currentPage = page;
      },
      goToPreviousPage() {
        if (this.currentPage > 1) {
          this.currentPage--;
        }
      },
      goToNextPage() {
        if (this.currentPage < this.totalPages) {
          this.currentPage++;
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .board-title {
    font-size: 26px;
    font-weight: bold;
    margin-bottom: 20px;
  }
  
  .tbl_list {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
  }
  
  .tbl_list th,
  .tbl_list td {
    border-top: 1px solid #ddd;
    border-bottom: 1px solid #ddd;
    padding: 10px;
    text-align: left;
  }
  
  .tbl_list th {
    background-color: #f4f4f4;
    font-weight: bold;
  }
  
  .text_left {
    text-align: left;
  }
  
  .btn_view {
    text-transform: uppercase;
    color: #fff; /* 텍스트 색상 흰색 */
  }
  
  .v-btn.btn_view {
    background-color: grey !important; /* 기본 회색 */
  }
  
  .v-btn.btn_view:hover {
    background-color: #777 !important; /* hover 시 더 진한 회색 */
  }
  
  .pagingWrap ul {
    list-style: none;
    padding: 0;
    margin: 0;
    text-align: center;
    margin-top: 20px;
  }
  
  .pagingWrap li {
    display: inline-block;
  }
  
  .pagingWrap li a {
    margin: 0 5px;
    text-decoration: none;
    color: #333;
    cursor: pointer;
  }
  
  .pagingWrap li a.active {
    font-weight: bold;
    color: #0056b3;
  }
  
  .pagingWrap .btn_paging_start:before {
    content: "<<";
  }
  
  .pagingWrap .btn_paging_prev:before {
    content: "<";
  }
  
  .pagingWrap .btn_paging_next:before {
    content: ">";
  }
  
  .pagingWrap .btn_paging_end:before {
    content: ">>";
  }
  </style>
  