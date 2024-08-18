<template>
  <div class="board-container">
    <div class="inner">
      <h1 class="board-title">내 질문 목록</h1>

      <table class="tbl_list">
        <caption></caption>
        <colgroup>
          <col width="80" />
          <col width="auto" />
          <col width="140" />
          <col width="140" />
          <col width="160" /> <!-- 작성일 칸의 너비를 160으로 증가 -->
        </colgroup>
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>답변일</th>
            <th>작성일</th>
            <th>보기</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in qnaList" :key="item.id">
            <td>{{ index + 1 }}</td>
            <td @click="viewDetails(item.id)" class="text_left subject">{{ item.title }}</td>
            <td>{{ item.answeredAt ? formatDate(item.answeredAt) : '미답변' }}</td>
            <td>{{ formatDate(item.createdTime) }}</td>
            <td>
              <v-btn @click="viewDetails(item.id)" class="btn_view">
                보기
              </v-btn>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
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
.board-container {
  width: 90%;
  margin: 0 auto;
  padding-top: 50px;
}

.inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

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
  border: 1px solid #ccc;
  padding: 10px;
  text-align: left;
}

.tbl_list th {
  background-color: #f4f4f4;
}

.text_left {
  text-align: left;
}

.subject {
  cursor: pointer;
  color: #333;
  text-decoration: none;
}

.subject:hover {
  text-decoration: underline;
}

.btn_view {
  padding: 6px 12px;
  background-color: #333;
  color: #fff;
  border: none;
  cursor: pointer;
  font-size: 14px;
  text-transform: uppercase;
}

.btn_view:hover {
  background-color: #555;
}
</style>
