<template>
  <div class="board-container">
    <div class="inner">
      <h1 class="board-title">QnA 목록</h1>
      <table class="tbl_list">
        <caption></caption>
        <colgroup>
          <col width="80" />
          <col width="auto" />
          <col width="140" />
          <col width="140" />
          <col width="140" />
          <col width="140" />
        </colgroup>
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>강좌명</th>
            <th>작성자</th>
            <th>생성 시간</th>
            <th>수정 시간</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(question, index) in questions" :key="question.id">
            <td>{{ index + 1 + (currentPage - 1) * itemsPerPage }}</td>
            <td @click="viewDetail(question.id)" class="text_left subject">{{ question.title }}</td>
            <td>{{ question.subjectTitle }}</td>
            <td>{{ question.questionUserName }}</td>
            <td>{{ formatDate(question.createdTime) }}</td>
            <td>{{ formatDate(question.updatedTime) }}</td>
          </tr>
        </tbody>
      </table>
      <div class="btnWrap">
        <button @click="createNewQuestion" class="btn_write">질문 작성하기</button>
      </div>
      <div class="pagingWrap">
        <ul>
          <li><a href="javascript:void(0)" @click="goToPage(1)" class="btn_paging_start"></a></li>
          <li><a href="javascript:void(0)" @click="goToPreviousPage" class="btn_paging_prev"></a></li>
          <li v-for="page in totalPages" :key="page">
            <a href="javascript:void(0)" @click="goToPage(page)" :class="{ btn_paging: true, active: currentPage === page }">{{ page }}</a>
          </li>
          <li><a href="javascript:void(0)" @click="goToNextPage" class="btn_paging_next"></a></li>
          <li><a href="javascript:void(0)" @click="goToPage(totalPages)" class="btn_paging_end"></a></li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      questions: [],
      currentPage: 1,
      totalPages: 1,
      itemsPerPage: 10,
      userRole: '',
    };
  },
  created() {
    this.fetchQuestions();
    this.userRole = localStorage.getItem('role');
  },
  methods: {
    async fetchQuestions() {
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/qna/list`, {
          params: {
            page: this.currentPage - 1,
            size: this.itemsPerPage,
          },
        });

        const result = response.data.result;
        if (result && result.content) {
          this.questions = result.content;
          this.totalPages = result.totalPages;
        } else {
          console.error('올바르지 않은 데이터 형식입니다:', response.data);
        }
      } catch (error) {
        console.error('질문 목록을 불러오는 중 오류가 발생했습니다:', error);
      }
    },
    formatDate(date) {
      const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
      return new Date(date).toLocaleDateString('ko-KR', options);
    },
    createNewQuestion() {
      this.$router.push('/qna/create');
    },
    viewDetail(id) {
      this.$router.push(`/qna/detail/${id}`);
    },
    goToPreviousPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchQuestions();
      }
    },
    goToNextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchQuestions();
      }
    },
    goToPage(page) {
      if (page !== this.currentPage) {
        this.currentPage = page;
        this.fetchQuestions();
      }
    },
  }
};
</script>

<style scoped>
.board-container {
  width: 80%;
  margin: 0 auto;
  padding-top: 50px;
}

.inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.board-title {
  font-size: 24px;
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

.btnWrap {
  text-align: right;
  margin-top: 20px;
}

.btn_write {
  padding: 10px 20px;
  background-color: #333;
  color: #fff;
  border: none;
  cursor: pointer;
}

.btn_write:hover {
  background-color: #555;
}

.pagingWrap ul {
  list-style: none;
  padding: 0;
  margin: 0;
  text-align: center;
  margin-top: 20px;
}

.pagingWrap li {
  display: inline-block;
}

.pagingWrap li a {
  margin: 0 5px;
  text-decoration: none;
  color: black;
  cursor: pointer;
}

.pagingWrap li a.active {
  font-weight: bold;
  color: blue;
}

.pagingWrap .btn_paging_start:before {
  content: "<<";
}

.pagingWrap .btn_paging_prev:before {
  content: "<";
}

.pagingWrap .btn_paging_next:before {
  content: ">";
}

.pagingWrap .btn_paging_end:before {
  content: ">>";
}
</style>
