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
        qnaList: [] // 초기값은 빈 배열
      };
    },
    async mounted() {
      await this.fetchQnAList(); // 컴포넌트가 마운트될 때 Q&A 리스트를 불러옵니다.
    },
    methods: {
      async fetchQnAList() {
        try {
          const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/user/qna`);
          console.log(response.data);
          if (response.status === 200) {
            this.qnaList = response.data.result; // 응답 데이터에서 Q&A 리스트를 추출
          } else {
            console.error('Q&A 리스트 가져오기 실패:', response.data.message || response.statusText);
          }
        } catch (error) {
          console.error('Q&A 리스트 가져오기 중 오류 발생:', error.response ? error.response.data.message : error.message);
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
  