<template>
  <div class="update-container">
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
      category: '', // 현재 게시판 종류
    };
  },
  created() {
    this.category = this.$route.params.category;
    console.log('Category:', this.category); // 확인용 로그
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
.update-container {
  width: 80%;
  margin: 0 auto;
}

h1 {
  font-size: 24px;
  margin-bottom: 20px;
}

form > div {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

input[type="text"],
textarea {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

button {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>
