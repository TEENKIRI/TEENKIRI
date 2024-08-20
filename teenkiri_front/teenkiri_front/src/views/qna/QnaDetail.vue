<template>
  <v-container class="mt-5">
    <v-row>
      <v-col cols="12">
        <v-card class="pa-3 mb-4">
<h1 class="board-title"> QnA </h1>
        </v-card>
      </v-col>
    </v-row>
    <!-- 질문 카드와 관련된 내용 -->
    <v-card class="pa-5" v-if="questionDetail">
      <div class="d-flex justify-space-between align-center mb-3">
        <h2 class="text-h4 font-weight-bold">{{ questionDetail.title }} (강좌 명:{{ questionDetail.subjectTitle }})</h2>
        <div class="d-flex justify-end mb-3">
          <v-btn class="btn_st2 mr-2" @click="openReportModal('question')">신고</v-btn>
        </div>
      </div>

      <!-- 질문 상세 내용 -->
      <v-row class="mb-5">
        <v-col cols="12" md="12">
          <v-card flat>
            <v-card-text>
              <div class="text-body-1">
                <p><strong>작성자:</strong> {{ questionDetail.questionUserNickname }} | <strong>작성 시간:</strong>{{ formatDate(questionDetail.createdTime) }} | <strong>수정 시간:</strong> {{ formatDate(questionDetail.updatedTime) }}</p>
              </div>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>

      <!-- 질문 이미지 -->
      <div class="image-container">
        <v-img v-if="questionDetail.qimageUrl" :src="questionDetail.qimageUrl" alt="질문 이미지" class="mb-3 rounded center-image" />
      </div>
      
      <!-- 질문 내용 -->
      <v-row class="mb-5">
        <v-col cols="12" md="12">
          <v-card flat>
            <v-card-text>
              <div class="text-body-1">
                <p class="text-h5"><strong>질문 내용:</strong> <br>{{ questionDetail.questionText }}</p>
              </div>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
      <v-divider></v-divider>

      <!-- 답변 섹션 -->
      <v-row v-if="questionDetail.answerText" class="mb-5">
        <v-col cols="12">
          <v-card flat>
            <v-card-text>
              <!-- 답변 이미지 -->
              <div class="image-container">
                <v-img v-if="questionDetail.aimageUrl" :src="questionDetail.aimageUrl" alt="답변 이미지" class="mb-3 rounded center-image" />
              </div>

              <!-- 답변 내용 -->
              <div class="text-body-1">
                <p><strong>답변자:</strong> {{ questionDetail.answeredByNickname }} | <strong>답변 시간:</strong> {{ formatDate(questionDetail.answeredAt) }}</p>
                <p class="text-h5"><strong>답변 내용:</strong> <br>{{ questionDetail.answerText }}</p>
              </div>
            </v-card-text>
            <v-card-actions class="d-flex justify-end">
              <v-btn v-if="canEditAnswer" class="btn_st2" @click="editAnswer">답변 수정</v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
      
      <!-- 댓글 작성 및 표시 -->
      <v-row>
        <v-divider></v-divider>
        <v-col cols="12">
          <h4 class="text-h6 font-weight-bold">댓글</h4>
          <v-list two-line>
            <v-list-item v-for="comment in questionDetail.comments" :key="comment.id" class="py-2">
              <v-list-item-content class="comment-content">
                <div class="comment-text">
                  <v-list-item-title class="text-subtitle-1">{{ comment.content }}</v-list-item-title>
                  <v-list-item-subtitle>{{ comment.nickname }} - {{ formatDate(comment.createdTime) }}</v-list-item-subtitle>
                </div>
                <v-list-item-action class="action-buttons">
                  <a href="javascript:void(0)" class="btn_board_option" @click="openReportModal('comment', comment)">신고</a>
                  <a v-if="isAdmin" href="javascript:void(0)" class="btn_board_option" @click="deleteComment(comment.id)">삭제</a>
                </v-list-item-action>
              </v-list-item-content>
            </v-list-item>
          </v-list>

          <v-form @submit.prevent="submitComment" class="mt-3">
            <v-textarea label="댓글 작성" v-model="newComment" outlined required></v-textarea>
            <div class="mt-3 d-flex justify-end button-container">
              <v-btn class="btn_solid" @click="goBack">목록으로</v-btn>
              <v-btn type="submit" class="btn_comment_ok">댓글작성</v-btn>
              <v-btn v-if="isQuestionAuthor" class="btn_st2" @click="editQuestion">수정</v-btn>
              <v-btn v-if="canDeleteQuestion" class="btn_del" @click="confirmDeleteQuestion">삭제</v-btn>
              <v-btn v-if="canAnswer" class="btn_write" @click="goToAnswer">답변</v-btn>
            </div>
          </v-form>
          
        </v-col>
      </v-row>
    </v-card>

    <!-- 오류 메시지 -->
    <v-alert type="error" v-if="error">{{ error }}</v-alert>

    <!-- 삭제 확인 다이얼로그 -->
    <v-dialog v-model="deleteDialog" max-width="500px">
      <v-card>
        <v-card-title class="headline">게시글 삭제</v-card-title>
        <v-card-text>게시글을 정말 삭제하시겠습니까?</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn class="btn_solid" text @click="deleteDialog = false">취소</v-btn>
          <v-btn class="btn_del" text @click="deleteQuestion">삭제</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 신고 모달 -->
    <ReportCreate v-if="showReportModal" :postId="reportData.postId" :commentId="reportData.commentId" :qnaId="reportData.qnaId" @close="closeReportModal" />
  </v-container>
</template>

<script>
import axios from 'axios';
import ReportCreate from '../report/ReportCreate.vue';

export default {
  components: {
    ReportCreate,
  },
  data() {
    return {
      questionDetail: null,
      newComment: '',
      error: null,
      deleteDialog: false,
      userEmail: '',
      userRole: '',
      showReportModal: false,
      reportData: {},
      activeComment: null,
    };
  },
  computed: {
    isQuestionAuthor() {
      return this.questionDetail && this.questionDetail.userEmail === this.userEmail;
    },
    isAdmin() {
      return this.userRole === 'ADMIN';
    },
    canEditAnswer() {
      return this.userRole === 'ADMIN' || this.userRole === 'TEACHER';
    },
    canAnswer() {
      return this.userRole === 'ADMIN' || this.userRole === 'TEACHER';
    },
    canDeleteQuestion() {
      return this.isAdmin || this.isQuestionAuthor;
    },
  },
  created() {
    this.decodeToken();
    this.fetchQuestionDetail();
  },
  methods: {
    decodeToken() {
      const token = localStorage.getItem('token');
      if (token) {
        const decoded = this.parseJwt(token);
        this.userEmail = decoded.sub;
        this.userRole = decoded.role;
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
    async fetchQuestionDetail() {
      const questionId = this.$route.params.id;
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/qna/detail/${questionId}`);
        this.questionDetail = response.data.result;
      } catch (error) {
        this.error = error.response ? error.response.data.message : '질문 정보를 불러오는 중 오류가 발생했습니다.';
      }
    },
    async submitComment() {
      const questionId = this.$route.params.id;
      try {
        await axios.post(`${process.env.VUE_APP_API_BASE_URL}/comment/create`, {
          qnaId: questionId,
          content: this.newComment,
          userId: localStorage.getItem('userId'),
        });
        this.newComment = '';
        this.fetchQuestionDetail();
      } catch (error) {
        this.error = '댓글 등록에 실패했습니다.';
      }
    },
    async deleteComment(commentId) {
      try {
        await axios.get(`${process.env.VUE_APP_API_BASE_URL}/comment/delete/${commentId}`);
        this.fetchQuestionDetail();
      } catch (error) {
        this.error = '댓글 삭제에 실패했습니다.';
      }
    },
    toggleCommentOptions(commentId) {
      this.activeComment = this.activeComment === commentId ? null : commentId;
    },
    confirmDeleteQuestion() {
      this.deleteDialog = true;
    },
    async deleteQuestion() {
      const questionId = this.$route.params.id;
      try {
        await axios.get(`${process.env.VUE_APP_API_BASE_URL}/qna/delete/${questionId}`);
        this.$router.push('/qna/list');
      } catch (error) {
        this.error = '질문 삭제에 실패했습니다.';
      }
    },
    editQuestion() {
      this.$router.push(`/qna/update/question/${this.$route.params.id}`);
    },
    editAnswer() {
      this.$router.push(`/qna/update/answer/${this.$route.params.id}`);
    },
    goToAnswer() {
      this.$router.push(`/qna/answer/${this.$route.params.id}`);
    },
    formatDate(date) {
      if (!date) return '';
      const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
      return new Date(date).toLocaleDateString('ko-KR', options);
    },
    goBack() {
      this.$router.push('/qna/list');
    },
    openReportModal(type, comment = null) {
      if (type === 'question') {
        this.reportData = {
          qnaId: this.questionDetail.id,
        };
      } else if (type === 'comment' && comment) {
        this.reportData = {
          postId: this.questionDetail.id,
          commentId: comment.id,
        };
      }
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
  max-width: 900px;
  margin: 0 auto;
}

.text-body-1 {
  font-size: 1rem;
  line-height: 1.5;
  color: #555;
}

h2.text-h5 {
  color: #333;
}

.v-btn {
  font-weight: bold;
  border-radius: 0;
  min-width: 100px;
}

.v-btn.primary {
  background-color: #1e88e5 !important;
  color: white;
}

.v-btn.teal.darken-3 {
  background-color: #00796b !important;
  color: white;
}

.v-btn.red.darken-2 {
  background-color: #d32f2f !important;
  color: white;
}

.v-btn.secondary {
  background-color: #424242 !important;
  color: white;
}

.v-btn:hover {
  opacity: 0.9;
}

.button-container {
  display: flex;
  gap: 8px; /* 버튼 간의 간격을 일정하게 설정 */
}

.v-list-item-title {
  font-weight: bold;
}

.v-list-item-subtitle {
  color: #757575;
}

.v-card-actions {
  padding: 0;
}

.v-img {
  border-radius: 10px;
}

.image-col {
  display: flex;
  justify-content: center;
}

.image-container {
  display: flex;
  justify-content: center; /* 수평 중앙 정렬 */
  align-items: center; /* 수직 중앙 정렬 */
}

.center-image {
  max-width: 60%;
  height: auto;
}

.btn_solid {
  background-color: #ffdb69 !important;
  color: rgb(255, 255, 255);
  border-radius: 8px;
}

.btn_st2 {
  background-color: #424242 !important;
  color: white;
  border-radius: 8px;
}

.btn_del {
  background-color: #f27885 !important;
  color: white;
  border-radius: 8px;
}

.btn_write {
  background-color: #6fc8b8 !important;
  color: white;
  border-radius: 8px;
}

.btn_comment_ok {
  background-color: #5087c7 !important;
  color: white;
  border-radius: 8px;
}

.btn_alert {
  background-color: #ffb300 !important;
  color: white;
}

.conLayer {
  display: inline-block;
  background-color: white;
  border: 1px solid #ccc;
  position: absolute;
  z-index: 1;
  right: 0;
}

.comment-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.comment-text {
  flex-grow: 1;
}

.action-buttons {
  display: flex;
  align-items: center;
  margin-left: 16px; /* 원하는 여백 추가 */
}

.btn_board_option {
  display: block;
  padding: 5px 10px;
  color: #ffffff;
  text-decoration: none;
  background-color: #f27885;
  border-bottom: 1px solid #ccc;
  border-radius: 8px;
}

.btn_board_option:hover {
  background-color: #f5f5f5;
  color: black;
}

.v-icon {
  font-size: 18px !important;
}
</style>