   <template>
    <v-container>
      <v-row class="qna-header">
        <v-col>
          <h1>내 질문 목록</h1>
        </v-col>
      </v-row>
      <v-row>
        <v-col
          v-for="item in qnaList"
          :key="item.id"
          cols="12" sm="6" md="4"
        >
          <v-card class="ma-4" :elevation="4">
            <v-card-title>
              {{ item.title }}
            </v-card-title>
            <v-card-subtitle>
              작성일: {{ formatDate(item.createdTime) }}
            </v-card-subtitle>
            <v-card-subtitle v-if="item.answeredAt">
              답변일: {{ formatDate(item.answeredAt) }}
            </v-card-subtitle>
            <v-card-actions>
              <v-btn @click="viewDetails(item.id)" color="primary">내 질문 보러가기</v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </template>
  
  <script>
import axios from 'axios';

export default {
  name: "QnAList",
  data() {
    return {
      qnaList: []
    };
  },
  async mounted() {
    await this.fetchQnAList();
  },
  methods: {
    async fetchQnAList() {
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/api/notifications/list`);
        if (response.status === 200) {
          this.qnaList = response.data.result;
        } else {
          console.error('Q&A 리스트 가져오기 실패:', response.data.message || response.statusText);
        }
      } catch (error) {
        // 개선된 에러 핸들링
        if (error.response) {
          // 서버가 응답을 했고 상태 코드가 2xx가 아님
          console.error('서버 오류:', error.response.data.message || error.response.statusText);
        } else if (error.request) {
          // 요청이 만들어졌으나 응답을 받지 못함
          console.error('요청 오류:', error.request);
        } else {
          // 오류가 요청을 설정하는 중에 발생함
          console.error('오류 발생:', error.message);
        }
      }
    },
    formatDate(dateString) {
      return new Date(dateString).toLocaleString();
    },
    viewDetails(id) {
      this.$router.push(`/qna/detail/${id}`); // ID를 포함한 상세 페이지 URL로 이동
    }
  }
};
</script>

  
  <style scoped>
  .qna-header {
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
  
  .v-btn {
    margin-top: 10px;
  }
  </style>
  