<template>
    <v-container class="mt-5">
      <v-card v-if="questionDetail">
        <v-card-title>
          <h3>{{ questionDetail.title }}</h3>
        </v-card-title>
  
        <v-card-text>
          <v-row>
            <v-col cols="12">
              <p><strong>작성자:</strong> {{ questionDetail.questionUserNickname }}</p>
              <p><strong>작성 시간:</strong> {{ formatDate(questionDetail.createdTime) }}</p>
              <!-- 질문 이미지 표시 -->
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
              <!-- 답변 이미지 표시 -->
              <v-img
                v-if="questionDetail.aimageUrl"
                :src="questionDetail.aimageUrl"
                alt="답변 이미지"
                max-width="400"
                class="my-3"
              />
              <p><strong>답변 내용:</strong></p>
              <p>{{ questionDetail.answerText }}</p>
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
    </v-container>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        questionDetail: null,
        error: null,
      };
    },
    created() {
      this.fetchQuestionDetail();
    },
    methods: {
      async fetchQuestionDetail() {
        const questionId = this.$route.params.id;
        try {
          const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/qna/detail/${questionId}`);
          this.questionDetail = response.data.result;
          console.log("Question Image URL:", this.questionDetail.qimageUrl); // 이미지 URL을 확인
          console.log("Answer Image URL:", this.questionDetail.aimageUrl);   // 이미지 URL을 확인
          console.log(response.data); // API 응답을 콘솔에 출력
        } catch (error) {
          this.error = error.response ? error.response.data.message : '질문 정보를 불러오는 중 오류가 발생했습니다.';
        }
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
  