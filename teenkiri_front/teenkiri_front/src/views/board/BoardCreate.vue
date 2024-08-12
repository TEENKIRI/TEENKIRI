<template>
  <div>
    <h1>게시글 작성</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="category">게시판 종류:</label>
        <select v-model="category" id="category" required>
          <option v-for="option in availableCategory" :key="option.value" :value="option.value">
            {{ option.text }}
          </option>
        </select>
      </div>
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
import { jwtDecode } from 'jwt-decode';

export default {
  data() {
    return {
      title: '',
      content: '',
      image: null,
      category: 'post', // 기본 게시판 종류
      availableCategory: [], // 선택 가능한 게시판 종류
    };
  },
  created() {
    this.setAvailableCategory(); // 컴포넌트가 생성될 때 선택 가능한 게시판 종류를 설정
  },
  methods: {
    setAvailableCategory() {
      const token = localStorage.getItem('token');
      if (!token) {
        alert('로그인이 필요합니다.');
        this.$router.push('/login');
        return;
      }

      const decodedToken = jwtDecode(token);
      const role = decodedToken.role;

      if (role === 'ADMIN') {
        this.availableCategory = [
          { value: 'event', text: '이벤트' },
          { value: 'notice', text: '공지' },
          { value: 'post', text: '자유게시판' },
        ];
      } else {
        this.availableCategory = [
          { value: 'post', text: '자유게시판' },
        ];
      }
    },
    onFileChange(e) {
      this.image = e.target.files[0];
    },
    async submitForm() {
      // 토큰 확인 및 관리자 여부 체크
      const token = localStorage.getItem('token');
      if (!token) {
        alert('로그인이 필요합니다.');
        return;
      }

      const decodedToken = jwtDecode(token);
      if (decodedToken.role !== 'ADMIN' && this.category !== 'post') {
        alert('관리자만 공지와 이벤트 게시글을 작성할 수 있습니다.');
        return;
      }

      // 폼 데이터 생성
      const formData = new FormData();
      formData.append('title', this.title);
      formData.append('content', this.content);
      formData.append('category', this.category);  // 게시판 종류 추가
      if (this.image) {
        formData.append('image', this.image);
      }

      try {
        let apiUrl = '';
        switch (this.category) {
          case 'event':
            apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/event/create`;
            break;
          case 'notice':
            apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/notice/create`;
            break;
          case 'post':
          default:
            apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/post/create`;
            break;
        }

        // 서버로 POST 요청 보내기
        const response = await axios.post(apiUrl, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': `Bearer ${token}`,  // 토큰을 헤더에 추가
          },
        });
        console.log('저장 성공:', response.data);

        // 저장 후 적절한 게시판으로 이동
        this.$router.push({ name: 'BoardList', params: { category: this.category } });
      } catch (error) {
        if (error.response) {
          console.error('저장 실패:', error.response.data);
        } else {
          console.error('저장 실패: 서버와의 통신에 실패했습니다.');
        }
        alert('게시글 저장에 실패했습니다.');
      }
    },
  },
};
</script>

<style scoped>
form {
  display: flex;
  flex-direction: column;
}

form > div {
  margin-bottom: 10px;
}

button {
  align-self: flex-start;
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
