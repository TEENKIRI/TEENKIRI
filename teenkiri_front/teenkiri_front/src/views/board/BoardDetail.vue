<template>
  <v-container class="mt-5">
    <v-card v-if="post" class="pa-5">
      <!-- 제목 -->
      <v-card-title class="d-flex justify-space-between align-center">
        <h2 class="text-h5 font-weight-bold">{{ post.title }}</h2>
      </v-card-title>

      <!-- 본문 내용 -->
      <v-card-text>
        <v-row>
          <v-col cols="12">
            <ul class="info">
              <li><strong>작성자:</strong> 관리자</li>
              <li><strong>작성일:</strong> {{ formatDate(post.createdTime) }}</li>
            </ul>
            <v-img v-if="post.imageUrl" :src="post.imageUrl" alt="Post Image" max-width="400" class="my-3"/>
            <div v-html="post.content" class="text-body-1 bodyTxt"></div>
          </v-col>
        </v-row>
      </v-card-text>

      <!-- 댓글 섹션 -->
      <v-row v-if="isFreeBoard">
        <v-col cols="12">
          <v-divider class="my-3"></v-divider>
          <h4>댓글</h4>
          <v-list>
            <v-list-item v-for="comment in comments" :key="comment.id">
              <v-list-item-content>
                <v-list-item-title>{{ comment.nickname }} ({{ formatDate(comment.createdTime) }})</v-list-item-title>
                <v-list-item-subtitle>{{ comment.content }}</v-list-item-subtitle>
              </v-list-item-content>
              <v-list-item-action class="d-flex justify-end">
                <v-btn icon @click="toggleCommentOptions(comment.id)" small>
                  <v-icon small>mdi-dots-vertical</v-icon>
                </v-btn>
                <div v-if="activeComment === comment.id" class="conLayer">
                  <a v-if="canDeleteComment()" href="javascript:void(0)" class="btn_board_option" @click="deleteComment(comment.id)">삭제</a>
                  <a href="javascript:void(0)" class="btn_board_option" @click="openCommentReportModal(comment)">신고</a>
                </div>
              </v-list-item-action>
            </v-list-item>
          </v-list>

          <v-form v-if="isLoggedIn" @submit.prevent="submitComment" class="mt-3">
            <v-textarea label="댓글 작성" v-model="newCommentContent" required outlined />
            <!-- <div class="mt-3 d-flex justify-end"> -->
            <v-btn type="submit" class="mt-2 btn_comment_ok">댓글 등록</v-btn>
            <!-- </div> -->
          </v-form>
        </v-col>
      </v-row>

      <!-- 액션 버튼들 -->
      <v-card-actions class="d-flex justify-end">
        <v-btn class="mr-2 btn_white" @click="goBack">목록으로 돌아가기</v-btn>
        <v-btn v-if="canEditPost" class="mr-2 btn_white" @click="editPost">수정</v-btn>
        <v-btn v-if="canDeletePost" class="mr-2 btn_white" @click="confirmDeletePost">삭제</v-btn>
        <v-btn v-if="isFreeBoard" class="btn_alert mr-2" @click="openPostReportModal">신고하기</v-btn>
      </v-card-actions>
    </v-card>

    <!-- 에러 및 로딩 상태 -->
    <v-alert type="error" v-else-if="error">{{ error }}</v-alert>
    <v-progress-circular v-else indeterminate color="primary"></v-progress-circular>

    <!-- 게시글 삭제 확인 다이얼로그 -->
    <v-dialog v-model="deleteDialog" max-width="500px">
      <v-card>
        <v-card-title class="headline">게시글 삭제</v-card-title>
        <v-card-text>게시글을 정말 삭제하시겠습니까?</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn class="btn_white" text @click="deleteDialog = false">취소</v-btn>
          <v-btn class="btn_del" text @click="deletePost">삭제</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 신고 모달 창 -->
    <ReportCreate 
      v-if="showReportModal" 
      :postId="reportData.postId" 
      :postTitle="reportData.postTitle" 
      :postContent="reportData.postContent" 
      :authorEmail="reportData.authorEmail" 
      :postCategory="reportData.postCategory" 
      :commentId="reportData.commentId" 
      :commentContent="reportData.commentContent" 
      :commentAuthor="reportData.commentAuthor" 
      @close="closeReportModal" 
    />
  </v-container>
</template>

<script>
import axios from 'axios';
import ReportCreate from '../report/ReportCreate.vue';

export default {
  components: {
    ReportCreate
  },
  data() {
    return {
      post: null,
      isAdmin: false,
      comments: [],
      newCommentContent: '',
      isLoggedIn: false,
      isFreeBoard: false,
      showReportModal: false,
      reportData: {},
      userId: localStorage.getItem('userId'),
      deleteDialog: false,
      error: null,
      userEmail: '',
      activeComment: null, // 현재 열려있는 conLayer의 댓글 ID
    };
  },
  computed: {
    canEditPost() {
      return this.isAdmin || (this.isFreeBoard && this.post && this.post.userEmail === this.userEmail);
    },
    canDeletePost() {
      return this.isAdmin || (this.isFreeBoard && this.post && this.post.userEmail === this.userEmail);
    }
  },
  created() {
    this.decodeToken();
    this.checkAdminRole();
    this.checkLoginStatus();
    this.checkIfFreeBoard();
    this.fetchPostDetail();

    if (this.isFreeBoard) {
      this.fetchComments();
    }
  },
  methods: {
    decodeToken() {
      const token = localStorage.getItem('token');
      if (token) {
        const decoded = this.parseJwt(token);
        this.userEmail = decoded.sub;
      } else {
        this.$router.push('/login');
      }
    },
    parseJwt(token) {
      try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
          atob(base64)
            .split('')
            .map(function(c) {
              return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            })
            .join('')
        );
        return JSON.parse(jsonPayload);
      } catch (error) {
        return null;
      }
    },
    checkAdminRole() {
      const role = localStorage.getItem('role');
      this.isAdmin = role === 'ADMIN';
    },
    checkLoginStatus() {
      const token = localStorage.getItem('token');
      this.isLoggedIn = !!token;
    },
    checkIfFreeBoard() {
      const category = this.$route.params.category;
      this.isFreeBoard = category === 'post';
    },
    async fetchPostDetail() {
      try {
        const postId = this.$route.params.id;
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
        this.post = response.data.result;
      } catch (error) {
        console.error('게시글을 불러오는 데 실패했습니다:', error);
        alert('게시글을 불러오는 데 실패했습니다.');
      }
    },
    async fetchComments() {
      try {
        const postId = this.$route.params.id;
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/comment/post/${postId}`);
        this.comments = response.data.result;
      } catch (error) {
        console.error('댓글을 불러오는 데 실패했습니다:', error);
      }
    },
    async submitComment() {
      try {
        if (!this.newCommentContent.trim()) {
          alert('댓글 내용을 입력하세요.');
          return;
        }

        const postId = this.$route.params.id;
        const userId = localStorage.getItem('userId');
        const newComment = {
          content: this.newCommentContent,
          postId: postId,
          userId: userId
        };

        await axios.post(`${process.env.VUE_APP_API_BASE_URL}/comment/create`, newComment);
        this.newCommentContent = '';
        this.fetchComments();
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
          this.fetchComments();
        }
      } catch (error) {
        console.error('댓글 삭제에 실패했습니다:', error);
        alert('댓글 삭제에 실패했습니다.');
      }
    },
    toggleCommentOptions(commentId) {
      if (this.activeComment === commentId) {
        this.activeComment = null;
      } else {
        this.activeComment = commentId;
      }
    },
    canDeleteComment() {
      return this.isAdmin;
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
    confirmDeletePost() {
      this.deleteDialog = true;
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
          this.goBack();
        }
      } catch (error) {
        console.error('게시글을 삭제하는 데 실패했습니다:', error);
        alert('게시글 삭제에 실패했습니다.');
      }
    },
    openPostReportModal() {
      this.reportData = {
        postId: this.post.id,
        postTitle: this.post.title,
        postContent: this.post.content,
        authorEmail: this.post.nickname,
        postCategory: this.$route.params.category
      };
      this.showReportModal = true;
    },
    openCommentReportModal(comment) {
      this.reportData = {
        commentId: comment.id,
        commentContent: comment.content,
        commentAuthor: comment.nickname,
        postTitle: this.post.title,
        postCategory: this.$route.params.category
      };
      this.showReportModal = true;
    },
    closeReportModal() {
      this.showReportModal = false;
    },
  },
};
</script>

<style scoped>
.v-container {
  max-width: 800px;
  margin: 0 auto;
}

.my-3 {
  margin-top: 1rem;
  margin-bottom: 1rem;
}

.text-body-1 {
  font-size: 1rem;
  line-height: 1.5;
  color: #555;
}

.info {
  list-style: none;
  padding: 0;
  margin: 0;
}

.info li {
  display: inline-block;
  margin-right: 20px;
}

.v-btn {
  font-weight: bold;
  border-radius: 0;
}

.btn_white {
  background-color: white !important;
  color: #424242 !important;
  border: 1px solid #dcdcdc !important;
}

.btn_white:hover {
  background-color: #f5f5f5 !important;
}

.btn_del {
  background-color: #d32f2f !important;
  color: white;
}

.btn_alert {
  background-color: #d32f2f !important;
  color: white;
}

.btn_comment_ok {
  background-color: #1e88e5 !important;
  color: white;
}

.v-list-item-action {
  margin-left: auto;
}

.v-list-item-action .v-btn {
  margin-left: 8px;
}

.conLayer {
  display: inline-block;
  background-color: white;
  border: 1px solid #ccc;
  position: absolute;
  z-index: 1;
  right: 0;
}

.btn_board_option {
  display: block;
  padding: 5px 10px;
  color: #424242;
  text-decoration: none;
  background-color: white;
  border-bottom: 1px solid #ccc;
}

.btn_board_option:hover {
  background-color: #f5f5f5;
  color: black;
}

.v-icon {
  font-size: 18px !important;
}
</style>
