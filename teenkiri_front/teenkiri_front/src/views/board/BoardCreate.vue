<template>
  <v-container v-if="isComponentReady" class="mt-5">
    <v-card>
      <v-card-title>
        <h3>글쓰기</h3>
      </v-card-title>

      <v-card-text>
        <v-form ref="form" @submit.prevent="submitForm">
          <!-- 게시판 선택 -->
          <v-select
            v-model="selectedCategory"
            :items="categories"
            item-text="text"
            item-value="value"
            label="게시판 선택"
            required
          ></v-select>

          <!-- 제목 -->
          <v-text-field
            label="제목"
            v-model="title"
            required
          />

          <!-- 내용 -->
          <v-textarea
            label="내용"
            v-model="content"
            rows="10"
            required
          />

          <!-- 파일 첨부 -->
          <v-file-input
            @change="onFileChange"
            label="파일첨부"
            accept="image/*"
          />

          <!-- 미리보기 이미지 -->
          <v-img v-if="previewImageSrc" :src="previewImageSrc" max-width="200" class="my-3"/>

          <div class="btnWrap">
            <v-btn text @click="cancel">취소</v-btn>
            <v-btn color="primary" type="submit" class="ml-4">저장</v-btn>
          </div>
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
      isComponentReady: false,
      title: '',
      content: '',
      image: null,
      previewImageSrc: null,
      selectedCategory: null, // 선택된 게시판
      categories: [], // 게시판 목록을 저장할 배열
    };
  },
  async mounted() {
    await this.fetchCategories(); // 컴포넌트가 마운트될 때 게시판 목록을 불러옵니다.
    this.isComponentReady = true; // 데이터가 로드되면 컴포넌트를 렌더링
  },
  methods: {
    async fetchCategories() {
      try {
        const token = localStorage.getItem('token');
        if (!token) {
          alert('로그인이 필요합니다.');
          this.$router.push('/login');
          return;
        }

        const decodedToken = this.parseJwt(token);
        const role = decodedToken.role;

        if (role === 'ADMIN') {
          this.categories = [
            { value: 'notice', text: '공지사항' },
            { value: 'event', text: '이벤트' },
            { value: 'post', text: '자유게시판' },
          ];
        } else {
          this.categories = [
            { value: 'post', text: '자유게시판' },
          ];
        }
      } catch (error) {
        console.error('카테고리를 불러오는 중 오류 발생:', error);
      }
    },
    parseJwt(token) {
      try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
          atob(base64)
            .split('')
            .map(function (c) {
              return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            })
            .join('')
        );
        return JSON.parse(jsonPayload);
      } catch (error) {
        return null;
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
      const token = localStorage.getItem('token');
      if (!token) {
        alert('로그인이 필요합니다.');
        return;
      }

      const decodedToken = this.parseJwt(token);
      if (decodedToken.role !== 'ADMIN' && this.selectedCategory !== 'post') {
        alert('관리자만 공지와 이벤트 게시글을 작성할 수 있습니다.');
        return;
      }

      const formData = new FormData();
      formData.append('title', this.title);
      formData.append('content', this.content);
      formData.append('category', this.selectedCategory); // 선택한 게시판 카테고리 추가
      if (this.image) {
        formData.append('image', this.image);
      }

      try {
        const apiUrl = this.getApiUrl();
        const response = await axios.post(apiUrl, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: `Bearer ${token}`,
          },
        });
        console.log('저장 성공:', response.data);
        this.$router.push({ name: 'BoardList', params: { category: this.selectedCategory } });
      } catch (error) {
        console.error('저장 실패:', error.response?.data || '서버와의 통신에 실패했습니다.');
        alert('게시글 저장에 실패했습니다.');
      }
    },
    getApiUrl() {
      switch (this.selectedCategory) {
        case 'event':
          return `${process.env.VUE_APP_API_BASE_URL}/board/event/create`;
        case 'notice':
          return `${process.env.VUE_APP_API_BASE_URL}/board/notice/create`;
        case 'post':
        default:
          return `${process.env.VUE_APP_API_BASE_URL}/board/post/create`;
      }
    },
    cancel() {
      this.$router.go(-1); // 이전 페이지로 이동
    },
  },
};
</script>

<style scoped>
.v-container {
  max-width: 800px;
  margin: auto;
}
.btnWrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
.my-3 {
  margin-top: 1rem;
  margin-bottom: 1rem;
}
</style>
