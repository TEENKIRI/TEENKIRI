<template>
  <div class="board-detail-container">
    <h1 class="board-detail-title">{{ post.title }}</h1>
    <div class="board-detail-info">
      <span>작성자: {{ post.nickname }}</span>
      <span>작성일: {{ formatDate(post.createdTime) }}</span>
    </div>
    <div v-if="post.imageUrl" class="board-detail-image">
      <img :src="post.imageUrl" alt="Post Image" v-if="post.imageUrl" />
    </div>
    <div class="board-detail-content">
      <p>{{ post.content }}</p>
    </div>
    <div class="board-detail-actions">
      <button @click="goBack">목록으로 돌아가기</button>
      <button v-if="isAdmin" @click="editPost">수정</button>
      <button v-if="isAdmin" @click="deletePost">삭제</button>
    </div>
    
    <!-- 자유게시판일 때만 댓글 섹션을 표시 -->
    <div v-if="isFreeBoard" class="comments-section">
      <h2>댓글</h2>
      <ul>
        <li v-for="comment in comments" :key="comment.id">
          <p><strong>{{ comment.nickname }}</strong>: {{ comment.content }}</p>
          <small>{{ formatDate(comment.createdTime) }}</small>
          <button v-if="isAdmin" @click="deleteComment(comment.id)">삭제</button> <!-- 관리자만 삭제 가능 -->
        </li>
      </ul>

      <div v-if="isLoggedIn">
        <textarea v-model="newCommentContent" placeholder="댓글을 작성하세요"></textarea>
        <button @click="submitComment">댓글 작성</button>
      </div>
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
      comments: [], // 댓글 목록
      newCommentContent: '', // 새로운 댓글 내용
      isLoggedIn: false, // 로그인 여부
      nickname: '', // 현재 사용자 닉네임
      isFreeBoard: false, // 자유게시판 여부
    };
  },
  created() {
    this.checkAdminRole();
    this.checkLoginStatus();
    this.checkIfFreeBoard();
    this.fetchPostDetail(); // 컴포넌트가 생성될 때 게시글 상세 정보를 가져옴

    if (this.isFreeBoard) {
      this.fetchComments(); // 자유게시판일 때만 댓글 목록 가져오기
    }
  },
  methods: {
    checkAdminRole() {
      const role = localStorage.getItem('role');
      if (role === 'ADMIN') {
        this.isAdmin = true;
      }
    },
    checkLoginStatus() {
      // 로그인 상태와 현재 사용자 이메일을 확인하는 로직
      const token = localStorage.getItem('token');
      this.isLoggedIn = !!token;
      this.userEmail = localStorage.getItem('email'); // 로그인된 사용자의 이메일
    },
    checkIfFreeBoard() {
      const category = this.$route.params.category;
      this.isFreeBoard = category === 'post'; // 'post'를 자유게시판 카테고리로 간주
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
    async fetchComments() {
      try {
        const postId = this.$route.params.id; // URL에서 게시글 ID를 가져옴
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/comment/post/${postId}`);
        this.comments = response.data.result; // 서버에서 받아온 댓글 목록을 comments 배열에 저장
      } catch (error) {
        console.error('댓글을 불러오는 데 실패했습니다:', error);
      }
    },
    async submitComment() {
    try {
        const postId = this.$route.params.id;
        const userId = localStorage.getItem('userId');  // 로컬 스토리지에서 userId를 가져옵니다.
        const newComment = {
            content: this.newCommentContent,
            postId: postId,
            userId: userId  // userId를 포함시킵니다.
        };
        await axios.post(`${process.env.VUE_APP_API_BASE_URL}/comment/create`, newComment);
        this.newCommentContent = ''; // 입력 필드 초기화
        this.fetchComments(); // 댓글 목록 새로고침
    } catch (error) {
        console.error('댓글 작성에 실패했습니다:', error);
        alert('댓글 작성에 실패했습니다.');
    }
},
    async deleteComment(commentId) {
      try {
        const confirmed = confirm("이 댓글을 삭제하시겠습니까?");
        if (confirmed) {
          await axios.delete(`${process.env.VUE_APP_API_BASE_URL}/comment/delete/${commentId}`);
          this.fetchComments(); // 댓글 목록 새로고침
        }
      } catch (error) {
        console.error('댓글 삭제에 실패했습니다:', error);
        alert('댓글 삭제에 실패했습니다.');
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

.board-detail-image {
  text-align: center;
  margin-bottom: 20px;
}

.board-detail-image img {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
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

.comments-section {
  margin-top: 40px;
}

.comments-section h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.comments-section ul {
  list-style-type: none;
  padding: 0;
}

.comments-section li {
  margin-bottom: 20px;
}

.comments-section textarea {
  width: 100%;
  height: 100px;
  margin-top: 10px;
  margin-bottom: 10px;
}

.comments-section button {
  padding: 10px 20px;
  background-color: #28a745;
  color: white;
  border: none;
  cursor: pointer;
}

.comments-section button:hover {
  background-color: #218838;
}
</style>
