<template>
  <v-container class="mt-5">
    <v-card>
      <v-card-title>
        <h3>글쓰기</h3>
      </v-card-title>

      <v-card-text>
        <v-form ref="form" @submit.prevent="submitForm">
          <!-- 게시판 선택 -->
          <v-select
            v-model="category"
            :items="availableCategory"
            label="게시판"
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
      title: '',
      content: '',
      image: null,
      previewImageSrc: null,
      category: 'notice', // 기본 게시판 종류
      availableCategory: [
        { value: 'notice', text: '공지사항' },
        { value: 'event', text: '이벤트' },
        { value: 'post', text: '자유게시판' }
      ], // 선택 가능한 게시판 종류
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

      const decodedToken = this.parseJwt(token);
      const role = decodedToken.role;

      if (role !== 'ADMIN') {
        this.availableCategory = [
          { value: 'post', text: '자유게시판' },
        ];
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
      // 토큰 확인 및 관리자 여부 체크
      const token = localStorage.getItem('token');
      if (!token) {
        alert('로그인이 필요합니다.');
        return;
      }

      const decodedToken = this.parseJwt(token);
      if (decodedToken.role !== 'ADMIN' && this.category !== 'post') {
        alert('관리자만 공지와 이벤트 게시글을 작성할 수 있습니다.');
        return;
      }

      // 폼 데이터 생성
      const formData = new FormData();
      formData.append('title', this.title);
      formData.append('content', this.content);
      formData.append('category', this.category); // 게시판 종류 추가
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
            Authorization: `Bearer ${token}`, // 토큰을 헤더에 추가
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
