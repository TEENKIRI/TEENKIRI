<template>
    <v-container class="mt-5">
      <v-form @submit.prevent="updateAnswer">
        <v-textarea
          label="답변 내용"
          v-model="answer.answerText"
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
        answer: {
          answerText: '',
          aImage: null,
        },
        previewImageSrc: null,
      };
    },
    created() {
      this.fetchAnswerDetail();
    },
    methods: {
      async fetchAnswerDetail() {
        const answerId = this.$route.params.id;
        try {
          const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/qna/answer/detail/${answerId}`);
          this.answer = response.data.result;
          if (this.answer.aImage) {
            this.previewImageSrc = `${process.env.VUE_APP_API_BASE_URL}/${this.answer.aImage}`;
          }
        } catch (error) {
          this.$router.push('/qna/list');
        }
      },
      onFileChange(event) {
        const files = event?.target?.files || event?.dataTransfer?.files;
        if (files && files.length > 0) {
          this.answer.aImage = files[0];
          this.previewImage();
        } else {
          this.answer.aImage = null;
          this.previewImageSrc = null;
        }
      },
      previewImage() {
        if (this.answer.aImage) {
          const reader = new FileReader();
          reader.onload = (e) => {
            this.previewImageSrc = e.target.result;
          };
          reader.readAsDataURL(this.answer.aImage);
        } else {
          this.previewImageSrc = null;
        }
      },
      async updateAnswer() {
        const formData = new FormData();
        formData.append('answerText', this.answer.answerText);
        if (this.answer.aImage) {
          formData.append('image', this.answer.aImage);
        }
  
        try {
          await axios.post(`${process.env.VUE_APP_API_BASE_URL}/qna/update/answer/${this.$route.params.id}`, formData);
          this.$router.push(`/qna/detail/${this.$route.params.id}`);
        } catch (error) {
          alert('답변 수정에 실패했습니다.');
        }
      },
      goBack() {
        const questionId = this.$route.params.id;
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
  