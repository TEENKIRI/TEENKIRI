<template>
  <v-app>
    <v-container>
      <h1 class="board-title">내 질문 목록</h1>

      <table class="tbl_list">
        <caption></caption>
        <colgroup>
          <col width="80" />
          <col width="auto" />
          <col width="140" />
          <col width="160" /> <!-- 작성일 칸의 너비를 160으로 증가 -->
          <col width="140" /> <!-- 상태 칸 추가 -->
        </colgroup>
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>상태</th>
            <th>작성일</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in qnaList" :key="item.id">
            <td>{{ index + 1 }}</td>
            <td @click="viewDetails(item.id)" class="text_left subject">{{ item.title }}</td>
            <td>
              <v-chip
                :color="item.answeredAt ? 'green' : 'red'"
                dark
                small
              >
                {{ item.answeredAt ? '답변완료' : '미답변' }}
              </v-chip>
            </td>
            <td>{{ formatDate(item.createdTime) }}</td>
          </tr>
        </tbody>
      </table>
    </v-container>
  </v-app>
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
.board-title {
  font-size: 26px;
  font-weight: bold;
  margin-bottom: 20px;
}

.tbl_list {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.tbl_list th,
.tbl_list td {
  border-top: 1px solid #ddd;
  border-bottom: 1px solid #ddd;
  padding: 10px;
  text-align: left;
}

.tbl_list th {
  background-color: #f4f4f4;
  font-weight: bold;
}

.text_left {
  text-align: left;
}

.subject {
  text-decoration: none;
  color: #333;
  cursor: pointer;
}

.subject:hover {
  text-decoration: underline;
}

.btn_view:hover {
  background-color: #555;
}
</style>
