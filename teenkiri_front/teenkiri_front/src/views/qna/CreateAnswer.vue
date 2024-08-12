<template>
    <v-container class="mt-5">
      <v-card>
        <v-card-title>
          <h3>QnA 답변하기</h3>
        </v-card-title>
  
        <v-card-text>
          <!-- 질문자 정보 -->
          <v-row>
            <v-col cols="6">
              <v-text-field
                label="질문자"
                v-model="questionUserNickname"
                readonly
              />
            </v-col>
            <v-col cols="6">
              <v-text-field
                label="질문 날짜"
                v-model="questionDate"
                readonly
              />
            </v-col>
          </v-row>
  
          <!-- 질문 내용 -->
          <v-text-field label="제목" v-model="questionTitle" readonly />
          <v-textarea label="질문 내용" v-model="questionText" rows="5" readonly />
  
          <!-- 질문 이미지 -->
          <v-img
            v-if="questionImage"
            :src="questionImage"
            alt="질문 이미지"
            max-width="400"
            class="my-3"
          ></v-img>
  
          <!-- 답변 폼 -->
          <v-form ref="form" @submit.prevent="submitAnswer">
            <v-textarea
              label="답변 내용"
              v-model="answerText"
              rows="5"
              required
            />
            <v-file-input
              ref="fileInput"
              @change="onFileChange"
              label="답변 이미지 (선택사항)"
              accept="image/*"
            />
            <v-img v-if="previewImageSrc" :src="previewImageSrc" max-width="200" />
            <v-btn type="submit" color="primary" class="mt-3">답변 제출</v-btn>
          </v-form>
        </v-card-text>
      </v-card>
    </v-container>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        questionUserNickname: '',
        questionDate: '',
        questionTitle: '',
        questionText: '',
        questionImage: null,
  
        answererEmail: '',
        answerText: '',
        answerImage: null,
        previewImageSrc: null,
        answerAt: ''
      };
    },
    created() {
      this.fetchQuestionDetails();
    },
    methods: {
      fetchQuestionDetails() {
        const questionId = this.$route.params.id;
        axios
          .get(`${process.env.VUE_APP_API_BASE_URL}/qna/detail/${questionId}`)
          .then((response) => {
            console.log(response.data); // 응답 데이터를 콘솔에 출력하여 확인
            const question = response.data.result;
            this.questionUserNickname = question.questionUserNickname;
            this.questionDate = new Date(question.createdTime).toLocaleString();
            this.questionTitle = question.title;
            this.questionText = question.questionText;
            this.questionImage = question.qimageUrl;
          })
          .catch((error) => {
            console.error('질문 정보를 불러오는 중 오류가 발생했습니다:', error);
          });
      },
      onFileChange(event) {
        const files = event.target.files || event.dataTransfer.files;
        if (files && files.length > 0) {
          this.answerImage = files[0];
          this.previewImage();
        } else {
          this.answerImage = null;
          this.previewImageSrc = null;
        }
      },
      previewImage() {
        if (this.answerImage) {
          const reader = new FileReader();
          reader.onload = (e) => {
            this.previewImageSrc = e.target.result;
          };
          reader.readAsDataURL(this.answerImage);
        } else {
          this.previewImageSrc = null;
        }
      },
      async submitAnswer() {
        const formData = new FormData();
        formData.append('answererEmail', this.answererEmail);
        formData.append('answerText', this.answerText);
        if (this.answerImage) {
          formData.append('image', this.answerImage);
        }
  
        try {
          const response = await axios.post(
            `${process.env.VUE_APP_API_BASE_URL}/qna/answer/${this.$route.params.id}`,
            formData,
            {
              headers: {
                'Content-Type': 'multipart/form-data',
              },
            }
          );
          console.log(response);
          alert('답변이 성공적으로 제출되었습니다!');
          this.$router.push({ name: 'QnaList' });
        } catch (error) {
          const errorMessage =
            error.response && error.response.data
              ? error.response.data.message
              : '답변 제출에 실패했습니다.';
          alert(errorMessage);
          console.error('Error details:', error);
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .v-container {
    max-width: 600px;
    margin: auto;
  }
  </style>
  