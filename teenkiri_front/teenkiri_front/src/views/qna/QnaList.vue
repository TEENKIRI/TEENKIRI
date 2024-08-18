<template>
  <div class="board-container">
    <div class="inner">
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
          <col width="140" />
        </colgroup>
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>강좌명</th>
            <th>작성자</th>
            <th>작성일</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(question, index) in questions" :key="question.id">
            <td>{{ index + 1 + (currentPage - 1) * itemsPerPage }}</td>
            <td @click="viewDetail(question.id)" class="text_left subject">{{ question.title }}</td>
            <td>{{ question.subjectTitle }}</td>
            <td>{{ question.questionUserName }}</td>
            <td>{{ formatDate(question.createdTime) }}</td>
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
      searchCategory: '전체',
      searchQuery: '',
      selectedSubject: '', // 선택한 강좌
      subjects: [], // 강좌 리스트
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

    // subjectId가 ''(전체)인 경우 파라미터에서 삭제
    if (this.selectedSubject !== '') {
      params.subjectId = this.selectedSubject;
    }

    // 검색 범위가 전체인 경우, 제목, 작성자, 강좌명 모두 검색하도록 처리
    if (this.searchCategory === '전체' && this.searchQuery) {
      // 서버에서 '제목', '작성자', '강좌명'을 모두 검색하는 쿼리를 처리할 수 있도록
      params.searchCategory = 'all'; // 'all'은 예시이므로 서버에서 처리할 수 있도록 설정
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
      return `${year}년.${month}월.${day}일`;
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
  padding: 12px 25px;
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
