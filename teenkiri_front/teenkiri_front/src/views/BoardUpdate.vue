<template>
    <div class="edit-container">
      <h1>게시글 수정</h1>
      <form @submit.prevent="submitForm">
        <div>
          <label for="title">제목:</label>
          <input type="text" v-model="title" id="title" required>
        </div>
        <div>
          <label for="content">내용:</label>
          <textarea v-model="content" id="content" required></textarea>
        </div>
        <div>
          <label for="image">이미지:</label>
          <input type="file" @change="onFileChange">
        </div>
        <button type="submit">저장</button>
      </form>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        title: '',
        content: '',
        image: null,
      };
    },
    created() {
      this.fetchPostDetail(); // 컴포넌트 생성 시 기존 게시글 정보를 로드
    },
    methods: {
      async fetchPostDetail() {
        try {
          const postId = this.$route.params.id;
          const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/event/detail/${postId}`);
          const post = response.data.result;
          this.title = post.title;
          this.content = post.content;
        } catch (error) {
          console.error('게시글을 불러오는 데 실패했습니다:', error);
          alert('게시글을 불러오는 데 실패했습니다.');
        }
      },
      onFileChange(e) {
        this.image = e.target.files[0];
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
          await axios.post(`${process.env.VUE_APP_API_BASE_URL}/event/update/${postId}`, formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          });
  
          alert('게시글이 성공적으로 수정되었습니다.');
          this.$router.push({ name: 'BoardDetail', params: { id: postId } }); // 수정 후 상세 페이지로 이동
        } catch (error) {
          console.error('게시글을 수정하는 데 실패했습니다:', error);
          alert('게시글 수정에 실패했습니다.');
        }
      },
    },
  };
  </script>
  
  <style scoped>
  /* 스타일 생략 */
  </style>
  