<template>
  <div>
    <h1>내 질문 목록</h1>
    <table v-if="questions.length">
      <thead>
        <tr>
          <th>제목</th>
          <th>작성일</th>
          <th>답변</th>
          <th>답변일</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="question in questions" :key="question.id">
          <td>
            <a :href="`/qna/detail/${question.id}`">{{ question.title }}</a>
          </td>
          <td>{{ formatDate(question.createdTime) }}</td>
          <td>{{ question.answerText || '답변 없음' }}</td>
          <td>{{ formatDate(question.answeredAt) || '답변 없음' }}</td>
        </tr>
      </tbody>
    </table>
    <p v-else>질문이 없습니다.</p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      questions: [] // 빈 배열로 초기화
    };
  },
  async created() {
    await this.loadQuestions();
  },
  methods: {
    async loadQuestions() {
      try {
        // API 베이스 URL 설정
        const apiBaseUrl = process.env.VUE_APP_API_BASE_URL || 'http://localhost:8082';
        // API 요청
        const response = await axios.get(`${apiBaseUrl}/qna/my`);
        // 응답 구조를 콘솔에 출력하여 확인
        console.log('API Response:', response.data);

        // 응답 데이터의 result 배열을 questions에 할당
        this.questions = response.data.result || [];
      } catch (error) {
        // 오류 발생 시 콘솔에 출력
        console.error('질문 목록을 가져오는 데 실패했습니다.', error.response ? error.response.data : error.message);
        // 오류 발생 시 빈 배열로 설정
        this.questions = [];
      }
    },
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleDateString();
    }
  }
};
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
}

th, td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

th {
  background-color: #f4f4f4;
}

tr:nth-child(even) {
  background-color: #f9f9f9;
}

tr:hover {
  background-color: #f1f1f1;
}
</style>
