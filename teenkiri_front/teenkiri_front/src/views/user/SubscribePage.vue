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
          :key="subject.title"
          cols="12" sm="6" md="4"
        >
          <v-card class="ma-4" :elevation="4">
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
        subjects: []
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
          const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/my/subject/${userId}`);
          console.log(response.data);
          this.subjects = response.data.subjects;
        } catch (error) {
          console.error('수강 중인 강좌 가져오기 오류:', error);
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
  