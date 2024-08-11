<template>
  <div class="board-detail-container">
    <h1 class="board-detail-title">{{ post.title }}</h1>
    <div class="board-detail-info">
      <span>작성자: {{ post.nickname }}</span>
      <span>작성일: {{ formatDate(post.createdTime) }}</span>
    </div>
    <div class="board-detail-content">
      <p>{{ post.content }}</p>
    </div>
    <div class="board-detail-actions">
      <button @click="goBack">목록으로 돌아가기</button>
      <button v-if="isAdmin" @click="editPost">수정</button>
      <button v-if="isAdmin" @click="deletePost">삭제</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      post: {}, // 게시글 데이터를 저장할 객체
      isAdmin: false, // 관리자인지 여부
    };
  },
  created() {
    this.checkAdminRole();
    this.fetchPostDetail(); // 컴포넌트가 생성될 때 게시글 상세 정보를 가져옴
  },
  methods: {
    checkAdminRole() {
      const role = localStorage.getItem('role');
      if (role === 'ADMIN') {
        this.isAdmin = true;
      }
    },
    async fetchPostDetail() {
      try {
        const postId = this.$route.params.id; // URL에서 게시글 ID를 가져옴
        const category = this.$route.params.category;
        
        let apiUrl;
        if (category === 'event') {
          apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/event/detail/${postId}`;
        } else if (category === 'notice') {
          apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/notice/detail/${postId}`;
        } else if (category === 'post') {
          apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/post/detail/${postId}`;
        } else {
          throw new Error('잘못된 카테고리입니다.');
        }

        const response = await axios.get(apiUrl);
        this.post = response.data.result; // 서버에서 받아온 데이터를 post 객체에 저장
      } catch (error) {
        console.error('게시글을 불러오는 데 실패했습니다:', error);
        alert('게시글을 불러오는 데 실패했습니다.');
      }
    },
    formatDate(date) {
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(date).toLocaleDateString(undefined, options);
    },
    goBack() {
      const category = this.$route.params.category;
      let routeName = 'BoardList';

      this.$router.push({ name: routeName, params: { category } });
    },
    editPost() {
      const category = this.$route.params.category;
      this.$router.push({ name: 'BoardUpdate', params: { id: this.post.id, category } });
    },
    async deletePost() {
      try {
        const confirmed = confirm("이 게시글을 삭제하시겠습니까?");
        if (confirmed) {
          const category = this.$route.params.category;

          let apiUrl;
          if (category === 'event') {
            apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/event/delete/${this.post.id}`;
          } else if (category === 'notice') {
            apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/notice/delete/${this.post.id}`;
          } else if (category === 'post') {
            apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/post/delete/${this.post.id}`;
          } else {
            throw new Error('잘못된 카테고리입니다.');
          }

          await axios.get(apiUrl);
          alert('게시글이 삭제되었습니다.');
          this.goBack(); // 삭제 후 목록으로 돌아가기
        }
      } catch (error) {
        console.error('게시글을 삭제하는 데 실패했습니다:', error);
        alert('게시글 삭제에 실패했습니다.');
      }
    },
  },
};
</script>

<style scoped>
.board-detail-container {
  width: 80%;
  margin: 0 auto;
}

.board-detail-title {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 20px;
}

.board-detail-info {
  font-size: 14px;
  color: #777;
  margin-bottom: 20px;
}

.board-detail-content {
  font-size: 18px;
  line-height: 1.6;
  margin-bottom: 40px;
}

.board-detail-actions {
  text-align: right;
}

.board-detail-actions button {
  padding: 10px 20px;
  margin-left: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}

.board-detail-actions button:hover {
  background-color: #0056b3;
}
</style>
