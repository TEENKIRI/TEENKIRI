<template>
    <v-container class="mt-5">
      <v-form @submit.prevent="updateQuestion">
        <v-text-field
          label="제목"
          v-model="question.title"
          required
        ></v-text-field>
  
        <v-textarea
          label="질문 내용"
          v-model="question.questionText"
          required
        ></v-textarea>
  
        <v-file-input
          label="이미지 선택"
          @change="onFileChange"
          accept="image/*"
        ></v-file-input>
  
        <!-- 미리보기 이미지 -->
        <v-img v-if="previewImageSrc" :src="previewImageSrc" max-width="200" class="my-3"/>
  
        <v-btn type="submit" color="primary">수정 완료</v-btn>
        <v-btn color="secondary" @click="goBack">취소</v-btn>
      </v-form>
    </v-container>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        question: {
          title: '',
          questionText: '',
          qImage: null,
        },
        previewImageSrc: null,
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
          this.question = response.data.result;
          if (this.question.qImage) {
            this.previewImageSrc = `${process.env.VUE_APP_API_BASE_URL}/${this.question.qImage}`;
          }
        } catch (error) {
          this.$router.push('/qna/list');
        }
      },
      onFileChange(event) {
        const files = event?.target?.files || event?.dataTransfer?.files;
        if (files && files.length > 0) {
          this.question.qImage = files[0];
          this.previewImage();
        } else {
          this.question.qImage = null;
          this.previewImageSrc = null;
        }
      },
      previewImage() {
        if (this.question.qImage) {
          const reader = new FileReader();
          reader.onload = (e) => {
            this.previewImageSrc = e.target.result;
          };
          reader.readAsDataURL(this.question.qImage);
        } else {
          this.previewImageSrc = null;
        }
      },
      async updateQuestion() {
        const formData = new FormData();
        formData.append('title', this.question.title);
        formData.append('questionText', this.question.questionText);
        if (this.question.qImage) {
          formData.append('image', this.question.qImage);
        }
  
        try {
          await axios.post(`${process.env.VUE_APP_API_BASE_URL}/qna/update/question/${this.$route.params.id}`, formData);
          this.$router.push('/qna/list');
        } catch (error) {
          alert('질문 수정에 실패했습니다.');
        }
      },
      goBack() {
        const questionId = this.$route.params.id ;
        
        this.$router.push(`/qna/detail/${questionId}`);
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
  