<template>
  <v-container class="mt-5">
    <v-form @submit.prevent="updateAnswer">
      <v-textarea
        label="답변 내용"
        v-model="answer.answerText"
        required
      ></v-textarea>

      <!-- 이미지 선택 -->
      <v-file-input
        label="이미지 선택"
        @change="onFileChange"
        accept="image/*"
      ></v-file-input>

      <!-- 미리보기 이미지 -->
      <v-img v-if="previewImageSrc || answer.aimageUrl" :src="previewImageSrc || answer.aimageUrl" max-width="200" class="my-3"/>

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
        aimageUrl: null, // 기존 이미지 URL
        aImage: null // 새로 선택된 이미지 파일
      },
      previewImageSrc: null, // 새로 선택한 이미지 미리보기
    };
  },
  created() {
    this.fetchAnswerDetail();
  },
  methods: {
    async fetchAnswerDetail() {
      const questionId = this.$route.params.id;
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/qna/detail/${questionId}`);
        this.answer = response.data.result;

        // 기존 이미지가 있는 경우 미리보기 이미지로 설정
        if (this.answer.aimageUrl) {
          this.previewImageSrc = this.answer.aimageUrl;
        }
      } catch (error) {
        this.$router.push('/qna/list');
      }
    },
    onFileChange(event) {
      const files = event?.target?.files || event?.dataTransfer?.files;
      if (files && files.length > 0) {
        this.answer.aImage = files[0]; // 새로 선택된 파일을 저장
        this.previewImage(files[0]); // 새 이미지로 미리보기 업데이트
      } else {
        this.answer.aImage = null;
        this.previewImageSrc = this.answer.aimageUrl; // 기존 이미지로 설정
      }
    },
    previewImage(file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        this.previewImageSrc = e.target.result;
      };
      reader.readAsDataURL(file);
    },
    async updateAnswer() {
      const formData = new FormData();
      formData.append('answerText', this.answer.answerText);

      // 새 이미지가 선택된 경우에만 FormData에 추가
      if (this.answer.aImage) {
        formData.append('aImage', this.answer.aImage);
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
