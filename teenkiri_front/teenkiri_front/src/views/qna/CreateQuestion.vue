<template>
  <v-container>
    <v-form ref="form" @submit.prevent="submitForm">
      <v-text-field v-model="formData.title" label="제목" required></v-text-field>
      <v-textarea v-model="formData.questionText" label="내용" required></v-textarea>
      <v-file-input
        ref="fileInput"
        @change="onFileChange"
        label="이미지 업로드"
        accept="image/*"
      ></v-file-input>
      <v-img v-if="previewImageSrc" :src="previewImageSrc" max-width="200"></v-img>
      <v-btn color="primary" type="submit">저장</v-btn>
    </v-form>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      formData: {
        title: '',
        questionText: '',
        qImage: null, // 파일 첨부
      },
      previewImageSrc: null,
    };
  },
  methods: {
    onFileChange(event) {
      const files = event.target.files || event.dataTransfer.files;
      if (files && files.length > 0) {
        this.formData.qImage = files[0];
        this.previewImage();
      } else {
        this.formData.qImage = null;
        this.previewImageSrc = null;
      }
    },
    previewImage() {
      if (this.formData.qImage) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.previewImageSrc = e.target.result;
        };
        reader.readAsDataURL(this.formData.qImage);
      } else {
        this.previewImageSrc = null;
      }
    },
    async submitForm() {
      const formData = new FormData();
      formData.append('title', this.formData.title);
      formData.append('questionText', this.formData.questionText);
      if (this.formData.qImage) {
        formData.append('image', this.formData.qImage);
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
        console.log(response.data);
        alert('질문이 성공적으로 작성되었습니다!');
        this.resetForm();
        this.$router.push({ name: 'QnaList' }); // 질문 목록 페이지로 이동
      } catch (error) {
        const errorMessage =
          error.response && error.response.data
            ? error.response.data.message
            : '질문 작성에 실패했습니다.';
        alert(errorMessage);
        console.error('Error details:', error); // 전체 오류 출력
      }
    },
    resetForm() {
      this.formData.title = '';
      this.formData.questionText = '';
      this.formData.qImage = null;
      this.previewImageSrc = null;
      if (this.$refs.form) {
        this.$refs.form.reset(); // 폼 초기화
      }
      if (this.$refs.fileInput) {
        // 파일 입력 초기화 시, 파일이 존재하지 않더라도 오류 발생을 방지
        this.$refs.fileInput.$refs.input.value = null;
      }
    },
  },
};
</script>

<style scoped>
.v-container {
  max-width: 600px;
  margin: 0 auto;
  padding-top: 50px;
}
</style>
