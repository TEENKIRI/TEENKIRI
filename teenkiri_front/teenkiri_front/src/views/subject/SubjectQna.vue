<template>
  <v-app>
    <v-container>
      <SubjectDetailComponent v-model="selectedMenu" />

      <h2>QnA 목록</h2>
      <v-card>
        <v-card-title>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="createNewQuestion">질문 작성하기</v-btn>
        </v-card-title>
        <v-card-text>
          <v-table>
            <thead>
              <tr>
                <th>번호</th>
                <th>작성자</th>
                <th>제목</th>
                <th>강좌명</th>
                <th>생성 시간</th>
                <th>수정 시간</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(question, index) in questions" :key="question.id">
                <td>{{ index + 1 + (currentPage - 1) * itemsPerPage }}</td>
                <td>{{ question.questionUserName }}</td>
                <td @click="viewDetail(question.id)" class="clickable">{{ question.title }}</td>
                <td>{{ question.subjectTitle }} </td>
                <td>{{ formatDate(question.createdTime) }}</td>
                <td>{{ formatDate(question.updatedTime) }}</td>
              </tr>
            </tbody>
          </v-table>
        </v-card-text>
        <v-card-actions class="pagination">
          <v-btn @click="goToPreviousPage" :disabled="currentPage === 1">Previous</v-btn>
          <v-btn
            v-for="page in totalPages"
            :key="page"
            @click="goToPage(page)"
            :class="{ active: currentPage === page }"
            class="pagination-page"
            text
          >
            {{ page }}
          </v-btn>
          <v-btn @click="goToNextPage" :disabled="currentPage === totalPages">Next</v-btn>
        </v-card-actions>
      </v-card>
    </v-container>
  </v-app>
</template>

<script>
import axios from 'axios';
import SubjectDetailComponent from '@/components/subject/SubjectDetailComponent.vue';

export default {
  components: {
    SubjectDetailComponent,
  },
  data() {
    return {
      subjectId: this.$route.params.id,
      selectedMenu: 'SubjectQna',
      questions: [],
      currentPage: 1,
      totalPages: 1,
      itemsPerPage: 10,
      userRole: localStorage.getItem('role'),
    };
  },
  created() {
    this.fetchQuestions();
  },
  methods: {
    async fetchQuestions() {
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/qna/subject/${this.subjectId}/qna/list`, {
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
      this.$router.push(`/qna/create?subjectId=${this.subjectId}`);
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
  },
};
</script>

<style scoped>
.clickable {
  cursor: pointer;
  color: blue;
  text-decoration: underline;
}
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}
.pagination-page {
  margin: 0 5px;
  cursor: pointer;
}
.pagination-page.active {
  font-weight: bold;
  color: blue;
}
</style>
