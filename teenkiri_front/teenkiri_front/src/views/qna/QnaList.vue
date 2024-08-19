<template>
  <v-app>
    <v-container>
      <h1 class="board-title">QnA 목록</h1>

      <div class="filters">
        <v-row>
          <v-col cols="12" md="3">
            <v-select
              v-model="selectedSubject"
              :items="subjects"
              label="강좌 분류"
              item-text="title"
              item-value="id"
              @change="fetchQuestions"
            ></v-select>
          </v-col>
          <v-col cols="12" md="3">
            <v-select
              v-model="searchCategory"
              :items="searchCategories"
              label="검색 범위"
            ></v-select>
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field
              v-model="searchQuery"
              label="검색어"
              append-icon="mdi-magnify"
              @keyup.enter="fetchQuestions"
              @click:append="fetchQuestions"
            ></v-text-field>
          </v-col>
        </v-row>
      </div>

      <table class="tbl_list">
        <caption></caption>
        <colgroup>
          <col width="80" />
          <col width="auto" />
          <col width="140" />
          <col width="140" />

        </colgroup>
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>강좌명</th>
            <th>작성자</th>
            <th>작성일</th>
            <th>상태</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(question, index) in questions" :key="question.id">
            <td>{{ index + 1 + (currentPage - 1) * itemsPerPage }}</td>
            <td @click="viewDetail(question.id)" class="text_left subject">{{ question.title }}</td>
            <td>{{ question.subjectTitle }}</td>
            <td>{{ question.questionUserName }}</td>
            <td>{{ formatDate(question.createdTime) }}</td>
            <td>
              <v-chip
                :color="question.answeredAt ? 'green' : 'red'"
                dark
                small
              >
                {{ question.answeredAt ? '답변완료' : '미답변' }}
              </v-chip>
            </td>
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
    </v-container>
  </v-app>
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
      searchCategory: '전체',
      searchQuery: '',
      selectedSubject: '',
      subjects: [],
      searchCategories: ['전체', '제목', '작성자'],
    };
  },
  created() {
    this.fetchSubjects();
    this.fetchQuestions();
    this.userRole = localStorage.getItem('role');
  },
  methods: {
    async fetchSubjects() {
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/subject/list`);
        this.subjects = [{ id: '', title: '전체' }, ...response.data.result.content.map(subject => ({
          id: subject.id,
          title: subject.title,
        }))];
      } catch (error) {
        console.error('강좌 목록을 불러오는 중 오류가 발생했습니다:', error);
      }
    },
    async fetchQuestions() {
      try {
        const params = {
          page: this.currentPage - 1,
          size: this.itemsPerPage,
          searchCategory: this.searchCategory,
          searchQuery: this.searchQuery,
        };

        if (this.selectedSubject !== '') {
          params.subjectId = this.selectedSubject;
        }

        if (this.searchCategory === '전체' && this.searchQuery) {
          params.searchCategory = 'all';
        }

        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/qna/list`, { params });

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
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${year}년 ${month}월 ${day}일`;
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
.container {
  padding-top: 20px;
}

.board-title {
  font-size: 26px;
  font-weight: bold;
  margin-bottom: 20px;
}

.filters {
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
  border-left: none; /* 세로 줄 제거 */
  border-right: none; /* 세로 줄 제거 */
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
}

.subject:hover {
  text-decoration: underline;
}

.btnWrap {
  text-align: right;
  margin-top: 20px;
}

.btn_write {
  padding: 12px 25px;
  background-color: #f27885;
  color: #fff;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn_write:hover {
  background-color: #fa5263;
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
  color: #333;
  cursor: pointer;
}

.pagingWrap li a.active {
  font-weight: bold;
  color: #0056b3;
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
