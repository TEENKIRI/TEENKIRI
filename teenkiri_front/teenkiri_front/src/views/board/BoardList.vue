<template>
  <div class="board-container">
    <h1 class="board-title">{{ boardTitle }}</h1>
    <h2 v-if="isAdmin || category === 'post'">
      <button class="create-button" @click="createNewPost">게시글 작성</button>
    </h2>
    <table class="board-table">
      <thead>
        <tr>
          <th>번호</th>
          <th>제목</th>
          <th>작성자</th>
          <th>작성일</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in boardItems" :key="item.id">
          <td>{{ index + 1 + (currentPage - 1) * itemsPerPage }}</td>
          <td @click="goToDetail(item.id, category)" class="clickable">{{ item.title }}</td>
          <td>{{ item.nickname }}</td>
          <td>{{ formatDate(item.createdTime) }}</td>
        </tr>
      </tbody>
    </table>
    <div class="pagination">
      <button @click="goToPreviousPage">Previous</button>
      <span v-for="page in totalPages" :key="page" @click="goToPage(page)" :class="{ active: currentPage === page }">{{ page }}</span>
      <button @click="goToNextPage">Next</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      boardItems: [], // 게시글 목록 데이터
      currentPage: 1, // 현재 페이지 번호
      totalPages: 1, // 총 페이지 수
      itemsPerPage: 10, // 페이지당 항목 수
      isAdmin: false, // 관리자인지 여부
      userId: null, // 현재 로그인된 사용자의 ID
      category: '', // 현재 게시판 종류
      boardTitle: '', // 게시판 제목
    };
  },
  watch: {
    '$route.params.category': 'fetchBoardItems', // category가 변경될 때마다 fetchBoardItems 호출
  },
  created() {
    this.checkAdminRole();
    this.fetchBoardItems(); // 컴포넌트 생성 시 게시글 목록을 가져옴
    this.userId = localStorage.getItem('userId'); // 로컬스토리지에서 userId 가져오기
  },
  methods: {
    checkAdminRole() {
      const role = localStorage.getItem('role');
      this.isAdmin = role === 'ADMIN';
    },
    async fetchBoardItems() {
      this.category = this.$route.params.category;
      this.setBoardTitle();

      let apiUrl = '';
      if (this.category === 'event') {
        apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/event/list`;
      } else if (this.category === 'notice') {
        apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/notice/list`;
      } else if (this.category === 'post') {
        apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/post/list`;
      } else {
        console.error('잘못된 카테고리입니다.');
        return;
      }

      try {
        const response = await axios.get(apiUrl, {
          params: {
            page: this.currentPage - 1, // 페이지 번호 (0부터 시작)
            size: this.itemsPerPage, // 페이지당 항목 수
          },
        });
        const data = response.data.result;
        this.boardItems = data.content;
        this.totalPages = data.totalPages;
      } catch (error) {
        console.error('목록을 가져오는 데 실패했습니다:', error);
        alert('목록을 가져오는 데 실패했습니다.');
      }
    },
    setBoardTitle() {
      if (this.category === 'event') {
        this.boardTitle = '이벤트 게시판';
      } else if (this.category === 'notice') {
        this.boardTitle = '공지사항 게시판';
      } else if (this.category === 'post') {
        this.boardTitle = '자유게시판';
      } else {
        this.boardTitle = '게시판';
      }
    },
    formatDate(date) {
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(date).toLocaleDateString(undefined, options);
    },
    goToPreviousPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchBoardItems();
      }
    },
    goToNextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchBoardItems();
      }
    },
    goToPage(page) {
      this.currentPage = page;
      this.fetchBoardItems();
    },
    createNewPost() {
      if (this.category !== 'post' && !this.isAdmin) {
        alert('관리자만 이 게시판에 글을 작성할 수 있습니다.');
        return;
      }
      this.$router.push({ name: 'BoardCreate', params: { category: this.category } });
    },
    goToDetail(id, category) {
      this.$router.push({ name: 'BoardDetail', params: { category, id } });
    },
  },
};
</script>

<style scoped>
.board-container {
  width: 80%;
  margin: 0 auto;
}

.board-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
}

.board-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.board-table th,
.board-table td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: left;
}

.board-table th {
  background-color: #f4f4f4;
}

.board-table td.clickable {
  cursor: pointer;
  color: blue;
  text-decoration: underline;
}

.pagination {
  text-align: center;
  margin-bottom: 20px;
}

.pagination button {
  margin: 0 5px;
}

.pagination span {
  margin: 0 5px;
  cursor: pointer;
}

.pagination .active {
  font-weight: bold;
  color: blue;
}

.create-button {
  display: block;
  margin: 0 auto;
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}

.create-button:hover {
  background-color: #0056b3;
}
</style>
