<template>
  <v-container class="mt-5">
    <v-card>
      <v-card-title>
        <h3>QnA 질문 등록하기</h3>
      </v-card-title>

      <v-card-text>
        <v-form ref="form" @submit.prevent="submitQuestion">
          <!-- 질문 제목 -->
          <v-text-field
            label="제목"
            v-model="questionTitle"
            required
          />

          <!-- 질문 내용 -->
          <v-textarea
            label="질문 내용"
            v-model="questionText"
            rows="5"
            required
          />

          <!-- 질문 이미지 (선택사항) -->
          <v-file-input
            ref="fileInput"
            @change="onFileChange"
            label="질문 이미지 (선택사항)"
            accept="image/*"
          />
          <v-img v-if="previewImageSrc" :src="previewImageSrc" max-width="200" class="my-3"/>

          <v-btn type="submit" color="primary" class="mt-3">질문 제출</v-btn>
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
      questionTitle: '',
      questionText: '',
      questionImage: null,
      previewImageSrc: null,
    };
  },
  methods: {
    onFileChange(event) {
      const files = event?.target?.files || event?.dataTransfer?.files;
      if (files && files.length > 0) {
        this.questionImage = files[0];
        this.previewImage();
      } else {
        this.questionImage = null;
        this.previewImageSrc = null;
      }
    },
    previewImage() {
      if (this.questionImage) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.previewImageSrc = e.target.result;
        };
        reader.readAsDataURL(this.questionImage);
      } else {
        this.previewImageSrc = null;
      }
    },
    async submitQuestion() {
      const formData = new FormData();
      formData.append('title', this.questionTitle);
      formData.append('questionText', this.questionText);
      if (this.questionImage) {
        formData.append('image', this.questionImage);
      }

      try {
        const response = await axios.post(
          `${process.env.VUE_APP_API_BASE_URL}/qna/create`,
          formData,
          {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          }
        );
        console.log(response);
        alert('질문이 성공적으로 등록되었습니다!');
        this.$router.push({ name: 'QnaList' });
      } catch (error) {
        const errorMessage =
          error.response && error.response.data
            ? error.response.data.message
            : '질문 등록에 실패했습니다.';
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
