<template>
    <v-container class="mt-5">
      <v-card v-if="questionDetail">
        <v-card-title>
          <h3>{{ questionDetail.title }}</h3>
          <v-spacer></v-spacer>
          <!-- 질문 수정 버튼 (작성자만 보이도록) -->
          <v-btn v-if="isQuestionAuthor" color="warning" @click="editQuestion">질문 수정</v-btn>
          <!-- 삭제 버튼 (관리자만 보이도록) -->
          <v-btn v-if="isAdmin" color="error" @click="confirmDeleteQuestion">삭제</v-btn>
        </v-card-title>
  
        <v-card-text>
          <v-row>
            <v-col cols="12">
              <p><strong>작성자:</strong> {{ questionDetail.questionUserNickname }}</p>
              <p><strong>작성 시간:</strong> {{ formatDate(questionDetail.createdTime) }}</p>
              <v-img
                v-if="questionDetail.qimageUrl"
                :src="questionDetail.qimageUrl"
                alt="질문 이미지"
                max-width="400"
                class="my-3"
              />
              <p><strong>질문 내용:</strong></p>
              <p>{{ questionDetail.questionText }}</p>
            </v-col>
  
            <v-col cols="12" v-if="questionDetail.answerText">
              <v-divider class="my-3"></v-divider>
              <p><strong>답변자:</strong> {{ questionDetail.answeredByNickname }}</p>
              <p><strong>답변 시간:</strong> {{ formatDate(questionDetail.answeredAt) }}</p>
              <v-img
                v-if="questionDetail.aimageUrl"
                :src="questionDetail.aimageUrl"
                alt="답변 이미지"
                max-width="400"
                class="my-3"
              />
              <p><strong>답변 내용:</strong></p>
              <p>{{ questionDetail.answerText }}</p>
              <!-- 답변 수정 버튼 (관리자와 선생님만 보이도록) -->
              <v-btn v-if="canEditAnswer" color="warning" @click="editAnswer">답변 수정</v-btn>
            </v-col>
          </v-row>
  
          <!-- 댓글 목록 -->
          <v-row>
            <v-col cols="12">
              <v-divider class="my-3"></v-divider>
              <h4>댓글</h4>
              <v-list>
                <v-list-item v-for="comment in questionDetail.comments" :key="comment.id">
                  <v-list-item-content>
                    <v-list-item-title>{{ comment.nickname }} ({{ formatDate(comment.createdTime) }})</v-list-item-title>
                    <v-list-item-subtitle>{{ comment.content }}</v-list-item-subtitle>
                  </v-list-item-content>
                  <!-- 댓글 삭제 버튼 (관리자만 볼 수 있음) -->
                  <v-list-item-action v-if="isAdmin">
                    <v-btn icon @click="deleteComment(comment.id)">
                      <v-icon>mdi-delete</v-icon>
                    </v-btn>
                  </v-list-item-action>
                </v-list-item>
              </v-list>
  
              <!-- 댓글 작성 -->
              <v-form @submit.prevent="submitComment">
                <v-textarea
                  label="댓글 작성"
                  v-model="newComment"
                  required
                />
                <v-btn type="submit" color="primary">댓글 등록</v-btn>
              </v-form>
            </v-col>
          </v-row>
        </v-card-text>
  
        <v-card-actions>
          <v-btn color="primary" @click="goBack">목록으로 돌아가기</v-btn>
        </v-card-actions>
      </v-card>
  
      <v-alert type="error" v-else-if="error">
        {{ error }}
      </v-alert>
  
      <v-progress-circular v-else indeterminate color="primary"></v-progress-circular>
  
      <!-- 질문 삭제 확인 다이얼로그 -->
      <v-dialog v-model="deleteDialog" max-width="500px">
        <v-card>
          <v-card-title class="headline">게시글 삭제</v-card-title>
          <v-card-text>게시글을 정말 삭제하시겠습니까?</v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" text @click="deleteDialog = false">취소</v-btn>
            <v-btn color="error" text @click="deleteQuestion">삭제</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-container>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        questionDetail: null,
        newComment: '',
        error: null,
        deleteDialog: false, // 삭제 확인 다이얼로그 표시 여부
        userEmail: '', // 토큰에서 디코드한 사용자 이메일
        userRole: '', // 로그인된 사용자 역할
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
      }
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
        //   console.log('이메일?', this.userEmail)
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
            userId: localStorage.getItem('userId') // 로그인한 사용자 ID
          });
          this.newComment = '';
          this.fetchQuestionDetail(); // 댓글 목록 갱신을 위해 상세 정보를 다시 불러옴
        } catch (error) {
          this.error = '댓글 등록에 실패했습니다.';
        }
      },
      async deleteComment(commentId) {
        try {
          await axios.get(`${process.env.VUE_APP_API_BASE_URL}/comment/delete/${commentId}`);
          this.fetchQuestionDetail(); // 댓글 목록 갱신을 위해 상세 정보를 다시 불러옴
        } catch (error) {
          this.error = '댓글 삭제에 실패했습니다.';
        }
      },
      confirmDeleteQuestion() {
        this.deleteDialog = true; // 삭제 확인 다이얼로그 표시
      },
      async deleteQuestion() {
        const questionId = this.$route.params.id;
        try {
          await axios.get(`${process.env.VUE_APP_API_BASE_URL}/qna/delete/${questionId}`);
          this.$router.push('/qna/list'); // 삭제 후 목록으로 돌아가기
        } catch (error) {
          this.error = '질문 삭제에 실패했습니다.';
        }
      },
      editQuestion() {
        this.$router.push(`/qna/update/question/${this.$route.params.id}`); // 질문 수정 페이지로 이동
      },
      editAnswer() {
        this.$router.push(`/qna/edit/answer/${this.$route.params.id}`); // 답변 수정 페이지로 이동
      },
      formatDate(date) {
        if (!date) return '';
        const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
        return new Date(date).toLocaleDateString('ko-KR', options);
      },
      goBack() {
        this.$router.push('/qna/list');
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
  