<template>
  <v-container class="mt-5">
    <v-card>
      <v-card-title>
        <h3>게시글 수정</h3>
      </v-card-title>

      <v-card-text>
        <v-form ref="form" @submit.prevent="submitForm">
          <!-- 제목 -->
          <v-text-field
            label="제목"
            v-model="title"
            required
            outlined
            dense
          />

          <!-- 내용 -->
          <v-textarea
            label="내용"
            v-model="content"
            rows="5"
            required
            outlined
            dense
          />

          <!-- 이미지 선택 -->
          <v-file-input
            @change="onFileChange"
            label="이미지 선택"
            accept="image/*"
            outlined
            dense
          />

          <!-- 미리보기 이미지 -->
          <v-img v-if="previewImageSrc" :src="previewImageSrc" max-width="200" class="my-3"/>

          <v-btn type="submit" color="primary" class="mt-3">저장</v-btn>
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
      title: '',
      content: '',
      image: null,
      previewImageSrc: null,
      category: '', // 현재 게시판 종류
    };
  },
  created() {
    this.category = this.$route.params.category;
    this.fetchPostDetail(); // 컴포넌트 생성 시 기존 게시글 정보를 로드
  },
  methods: {
    async fetchPostDetail() {
      try {
        const postId = this.$route.params.id;
        let apiUrl;

        // 카테고리별로 API URL을 설정
        if (this.category === 'event') {
          apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/event/detail/${postId}`;
        } else if (this.category === 'notice') {
          apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/notice/detail/${postId}`;
        } else if (this.category === 'post') {
          apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/post/detail/${postId}`;
        } else {
          throw new Error('잘못된 카테고리입니다.');
        }

        const response = await axios.get(apiUrl);
        const post = response.data.result;
        this.title = post.title;
        this.content = post.content;
      } catch (error) {
        console.error('게시글을 불러오는 데 실패했습니다:', error);
        alert('게시글을 불러오는 데 실패했습니다.');
      }
    },
    onFileChange(event) {
      const files = event?.target?.files || event?.dataTransfer?.files;
      if (files && files.length > 0) {
        this.image = files[0];
        this.previewImage();
      } else {
        this.image = null;
        this.previewImageSrc = null;
      }
    },
    previewImage() {
      if (this.image) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.previewImageSrc = e.target.result;
        };
        reader.readAsDataURL(this.image);
      } else {
        this.previewImageSrc = null;
      }
    },
    async submitForm() {
      try {
        const formData = new FormData();
        formData.append('title', this.title);
        formData.append('content', this.content);
        if (this.image) {
          formData.append('image', this.image);
        }

        const postId = this.$route.params.id;
        let apiUrl;

        // 카테고리별로 API URL을 설정
        if (this.category === 'event') {
          apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/event/update/${postId}`;
        } else if (this.category === 'notice') {
          apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/notice/update/${postId}`;
        } else if (this.category === 'post') {
          apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/post/update/${postId}`;
        } else {
          throw new Error('잘못된 카테고리입니다.');
        }

        await axios.post(apiUrl, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });

        alert('게시글이 성공적으로 수정되었습니다.');
        this.$router.push({ name: 'BoardDetail', params: { id: postId, category: this.category } }); // 수정 후 상세 페이지로 이동
      } catch (error) {
        console.error('게시글을 수정하는 데 실패했습니다:', error);
        alert('게시글 수정에 실패했습니다.');
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
.my-3 {
  margin-top: 1rem;
  margin-bottom: 1rem;
}
</style>
