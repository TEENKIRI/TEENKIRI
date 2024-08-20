<template>
  <v-container class="mt-5">
    <v-card>
      <v-card-title>
        <h3>QnA 질문 등록하기</h3>
      </v-card-title>

      <v-card-text>
        <v-form ref="form" @submit.prevent="submitQuestion">
          <!-- 강좌 선택 -->
          <v-select
            v-model="selectedSubject"
            :items="subjects"
            item-text="title"
            item-value="id"
            label="강좌 선택"
            required
          ></v-select>

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
      selectedSubject: null,
      subjects: [], // 강좌 목록을 저장할 배열
      questionTitle: '',
      questionText: '',
      questionImage: null,
      previewImageSrc: null,
    };
  },
  mounted() {
    const subjectIdFromQuery = this.$route.query.subjectId;
    if (subjectIdFromQuery) {
      this.selectedSubject = Number(subjectIdFromQuery); // 쿼리 파라미터로 전달된 강좌 ID를 선택
    }
    this.fetchSubjects(); // 컴포넌트가 마운트될 때 강좌 목록을 불러옵니다.
  },
  methods: {
    async fetchSubjects() {
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/subject/list`);
        this.subjects = response.data.result.content; // 강좌 목록을 subjects 배열에 저장
      } catch (error) {
        console.error('강좌 목록을 불러오는 중 오류가 발생했습니다:', error);
        this.subjects = []; // 오류 발생 시 빈 배열로 초기화
      }
    },
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
      if (!this.selectedSubject) {
        alert('강좌를 선택해주세요.');
        return;
      }

      const formData = new FormData();
      formData.append('title', this.questionTitle);
      formData.append('questionText', this.questionText);
      formData.append('subjectId', this.selectedSubject); // 선택한 강좌 ID를 추가
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
        console.error('오류 세부사항:', error);
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
