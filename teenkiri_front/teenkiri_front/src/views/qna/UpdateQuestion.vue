<template>
    <v-container class="mt-5">
      <v-form @submit.prevent="updateQuestion">
        <v-text-field
          label="제목"
          v-model="question.title"
          required
          outlined
          dense
        ></v-text-field>
        <p><strong>강좌명:</strong></p>
        <p>{{ question.subjectTitle }}</p>
  
        <v-textarea
          label="질문 내용"
          v-model="question.questionText"
          required
          outlined
          dense
        ></v-textarea>
  
        <!-- 이미지 선택 -->
        <v-file-input
          label="이미지 선택"
          @change="onFileChange"
          accept="image/*"
          outlined
          dense
        ></v-file-input>
  
        <!-- 미리보기 이미지 -->
        <v-img v-if="previewImageSrc || question.qimageUrl" :src="previewImageSrc || question.qimageUrl" max-width="200" class="my-3"/>
  
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
          qimageUrl: null, // 기존 이미지 URL
          qImage: null // 새로 선택된 이미지 파일
        },
        previewImageSrc: null, // 새로 선택한 이미지 미리보기
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
  
          // 기존 이미지가 있는 경우 미리보기 이미지로 설정
          if (this.question.qimageUrl) {
            this.previewImageSrc = this.question.qimageUrl;
          }
        } catch (error) {
          this.$router.push(`/qna/detail/${questionId}`);
        }
      },
      onFileChange(event) {
        const files = event?.target?.files || event?.dataTransfer?.files;
        if (files && files.length > 0) {
          this.question.qImage = files[0]; // 새로 선택된 파일을 저장
          this.previewImage(files[0]); // 새 이미지로 미리보기 업데이트
        } else {
          this.question.qImage = null;
          this.previewImageSrc = this.question.qimageUrl; // 기존 이미지로 설정
        }
      },
      previewImage(file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.previewImageSrc = e.target.result;
        };
        reader.readAsDataURL(file);
      },
      async updateQuestion() {
        const formData = new FormData();
        formData.append('title', this.question.title);
        formData.append('questionText', this.question.questionText);
  
        // 새 이미지가 선택된 경우에만 FormData에 추가하고, 기존 이미지는 URL을 전송
        if (this.question.qImage) {
          formData.append('qImage', this.question.qImage);
        } else if (this.question.qimageUrl) {
          formData.append('qimageUrl', this.question.qimageUrl);
        }
  
        try {
          await axios.post(`${process.env.VUE_APP_API_BASE_URL}/qna/update/question/${this.$route.params.id}`, formData);
          this.$router.push(`/qna/detail/${this.$route.params.id}`);
        } catch (error) {
          alert('질문 수정에 실패했습니다.');
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
  