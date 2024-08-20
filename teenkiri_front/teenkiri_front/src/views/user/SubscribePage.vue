<template>
  <v-container>
    <v-row class="subject-header">
      <v-col>
        <h1>수강 중인 강좌</h1>
      </v-col>
    </v-row>
    <v-row>
      <v-col
        v-for="subject in subjects"
        :key="subject.id"
        cols="12" sm="6" md="4"
      >
        <v-card 
          class="ma-4" 
          :elevation="4" 
          @click="goToSubjectDetail(subject.id)" 
          style="cursor: pointer;"
        >
          <v-img :src="subject.subjectThumUrl" height="200px"></v-img>
          <v-card-title>{{ subject.title }}</v-card-title>
          <v-card-subtitle>강사: {{ subject.teacherName }}</v-card-subtitle>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  name: 'UserSubjects',
  data() {
    return {
      subjects: [],
      error: null
    };
  },
  async mounted() {
    await this.fetchUserSubjects();
  },
  methods: {
    async fetchUserSubjects() {
      try {
        const userId = localStorage.getItem('userId'); // 로컬 스토리지에서 사용자 ID 가져오기
        if (!userId) {
          console.error('사용자 ID가 로컬 스토리지에 없습니다.');
          return;
        }
        // API 호출
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/my/subject/${userId}`);
        console.log(response.data);
        
        // API 응답 데이터 구조에 맞게 수정
        if (response.data && response.data.result) {
          this.subjects = response.data.result.subjects || [];
          if (this.subjects.length === 0) {
            console.log('현재 수강 중인 강좌가 없습니다.');
          }
        } else {
          console.error('API 응답의 형식이 잘못되었습니다.');
        }
      } catch (error) {
        this.error = '수강 중인 강좌를 가져오는 도중 오류가 발생했습니다.';
        console.error('수강 중인 강좌 가져오기 오류:', error);
      }
    },
    goToSubjectDetail(subjectId) {
      // 디버깅: subjectId 값 확인
      console.log(`Navigating to /subject/detail/${subjectId}`);
      if (subjectId) {
        this.$router.push(`/subject/detail/${subjectId}`);
      } else {
        console.error('유효한 subjectId가 없습니다.');
      }
    }
  }
}
</script>

<style scoped>
.subject-header {
  margin-bottom: 20px;
}

.v-card {
  max-width: 400px;
  margin: auto;
}

.v-card-title {
  font-weight: bold;
}

.v-card-subtitle {
  color: #757575;
}
</style>
