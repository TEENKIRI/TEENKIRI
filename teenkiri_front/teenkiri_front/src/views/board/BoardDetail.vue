<template>
  <v-container class="mt-5">
    <v-card v-if="post">
      <v-card-title>
        <h3>{{ post.title }}</h3>
        <v-spacer></v-spacer>
        <!-- 수정 및 삭제 버튼 -->
        <v-btn v-if="canEditPost" color="warning" @click="editPost">수정</v-btn>
        <v-btn v-if="canDeletePost" color="error" @click="confirmDeletePost">삭제</v-btn>
        <v-btn color="secondary" @click="openPostReportModal">신고하기</v-btn>
      </v-card-title>

      <v-card-text>
        <v-row>
          <v-col cols="12">
            <p><strong>작성자:</strong> {{ post.nickname }}</p>
            <p><strong>작성일:</strong> {{ formatDate(post.createdTime) }}</p>
            <v-img v-if="post.imageUrl" :src="post.imageUrl" alt="Post Image" max-width="400" class="my-3"/>
            <p><strong>내용:</strong></p>
            <p>{{ post.content }}</p>
          </v-col>
        </v-row>

        <!-- 자유게시판일 때만 댓글 섹션을 표시 -->
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
                <v-list-item-action v-if="isAdmin || comment.user_id === parseInt(userId, 10)">
                  <v-btn icon @click="deleteComment(comment.id)">
                    <v-icon>mdi-delete</v-icon>
                  </v-btn>
                </v-list-item-action>
                <v-list-item-action>
                  <v-btn icon @click="openCommentReportModal(comment)">
                    <v-icon>mdi-alert-circle-outline</v-icon>
                  </v-btn>
                </v-list-item-action>
              </v-list-item>
            </v-list>

            <v-form v-if="isLoggedIn" @submit.prevent="submitComment">
              <v-textarea label="댓글 작성" v-model="newCommentContent" required />
              <v-btn type="submit" color="primary">댓글 등록</v-btn>
            </v-form>
          </v-col>
        </v-row>
      </v-card-text>

      <v-card-actions>
        <v-btn color="primary" @click="goBack">목록으로 돌아가기</v-btn>
      </v-card-actions>
    </v-card>

    <v-alert type="error" v-else-if="error">{{ error }}</v-alert>
    <v-progress-circular v-else indeterminate color="primary"></v-progress-circular>

    <!-- 게시글 삭제 확인 다이얼로그 -->
    <v-dialog v-model="deleteDialog" max-width="500px">
      <v-card>
        <v-card-title class="headline">게시글 삭제</v-card-title>
        <v-card-text>게시글을 정말 삭제하시겠습니까?</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="deleteDialog = false">취소</v-btn>
          <v-btn color="error" text @click="deletePost">삭제</v-btn>
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
      post: null, // 게시글 데이터를 저장할 객체
      isAdmin: false, // 관리자인지 여부
      comments: [], // 댓글 목록
      newCommentContent: '', // 새로운 댓글 내용
      isLoggedIn: false, // 로그인 여부
      isFreeBoard: false, // 자유게시판 여부
      showReportModal: false, // 신고 모달 창 표시 여부
      reportData: {}, // 신고 모달에 전달할 데이터
      userId: localStorage.getItem('userId'), // 로그인된 사용자의 ID
      deleteDialog: false, // 삭제 확인 다이얼로그 표시 여부
      error: null, // 에러 메시지
      userEmail: '', // 현재 로그인한 사용자의 이메일
    };
  },
  computed: {
    canEditPost() {
      // 관리자는 모든 게시글을 수정 가능, 'post' 카테고리는 작성자도 수정 가능
      return this.isAdmin || (this.isFreeBoard && this.post && this.post.userEmail === this.userEmail);
    },
    canDeletePost() {
      // 관리자는 모든 게시글을 삭제 가능, 'post' 카테고리는 작성자도 삭제 가능
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
        this.$router.push('/login'); // 토큰이 없으면 로그인 페이지로 이동
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
        postId: comment.id,
        postTitle: this.post.title,
        postContent: comment.content,
        authorEmail: comment.nickname,
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
</style>
