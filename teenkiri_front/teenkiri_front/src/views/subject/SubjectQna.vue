<template>
  <v-app>
    <v-container>
      <SubjectDetailComponent v-model="selectedMenu" />

      <div class="btnWrap">
        <a href="javascript:void(0)" class="btn_write" @click="createNewQuestion">질문 작성하기</a>
      </div>

      <table class="tbl_list">
        <caption></caption>
        <colgroup>
          <col width="80" />
          <col width="" />
          <col width="" />
          <col width="140" />
          <col width="180" /> <!-- 작성일 열의 너비를 180px로 설정 -->
          <col width="140" />
          <col v-if="canManage" width="140" /> <!-- 관리 필드 -->
        </colgroup>
        <thead>
          <tr>
            <th>번호</th>
            <th>강의명</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일</th>
            <th>상태</th>
            <th v-if="canManage">관리</th> 
          </tr>
        </thead>
        <tbody>
          <tr v-for="(question, index) in questions" :key="question.id">
            <td>{{ index + 1 + (currentPage - 1) * itemsPerPage }}</td>
            <td class="text_left">{{ question.subjectTitle }}</td>
            <td class="text_left">
              <a href="javascript:void(0)" class="subject" @click="viewDetail(question.id)">
                {{ question.title }} <span class="cnt">{{ question.answerCount }}</span>
              </a>
            </td>
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
            <td v-if="canManage">
              <div class="relative">
                <button
                  type="button"
                  class="btn_adm_control"
                  @click="toggleConLayer(question.id)"
                >•••</button>
                <div class="conLayer" v-show="activeItem === question.id">
                  <a href="javascript:void(0)" class="btn_board_modify" @click="answer(question)">답변</a>
                  <a href="javascript:void(0)" class="btn_board_modify" @click="modifyPost(question.id)">답변 수정</a>
                  <a href="javascript:void(0)" class="btn_board_del btn_del" @click="deletePost(question.id)">삭제</a>
                </div>
              </div>
            </td>
          </tr>

          <!-- 답변 row -->
          <template v-for="question in questions" :key="question.id">
            <template v-if="question.isAnswered">
              <tr v-for="reply in question.replies" :key="reply.id" class="reply">
                <td></td>
                <td class="text_left">{{ reply.subjectTitle }}</td>
                <td class="text_left">
                  <i class="ico_reply"></i>
                  <a href="javascript:void(0)" class="subject" @click="viewDetail(reply.id)">
                    [답변] {{ reply.title }}
                  </a>
                </td>
                <td>{{ reply.userName }}</td>
                <td>{{ formatDate(reply.createdTime) }}</td>
                <td>
                  <v-chip
                    :color="reply.isAnswered ? 'green' : 'red'"
                    dark
                    small
                  >
                    {{ reply.isAnswered ? '답변완료' : '미답변' }}
                  </v-chip>
                </td>
                <td v-if="canManage">
                  <div class="relative">
                    <button type="button" class="btn_adm_control" @click="toggleConLayer(reply.id)">•••</button>
                    <div class="conLayer" v-show="activeItem === reply.id">
                      <a href="javascript:void(0)" class="btn_board_modify" @click="answer(reply)">답변</a>
                      <a href="javascript:void(0)" class="btn_board_modify" @click="modifyPost(reply.id)">답변 수정</a>
                      <a href="javascript:void(0)" class="btn_board_del btn_del" @click="deletePost(reply.id)">삭제</a>
                    </div>
                  </div>
                </td>
              </tr>
            </template>
          </template>
        </tbody>
      </table>

      <div class="pagingWrap">
        <ul>
          <li><a href="javascript:void(0)" class="btn_paging_start" @click="goToPage(1)"></a></li>
          <li><a href="javascript:void(0)" class="btn_paging_prev" @click="goToPreviousPage"></a></li>
          <li v-for="page in totalPages" :key="page">
            <a href="javascript:void(0)" class="btn_paging" :class="{ active: currentPage === page }" @click="goToPage(page)">
              {{ page }}
            </a>
          </li>
          <li><a href="javascript:void(0)" class="btn_paging_next" @click="goToNextPage"></a></li>
          <li><a href="javascript:void(0)" class="btn_paging_end" @click="goToPage(totalPages)"></a></li>
        </ul>
      </div>
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
      activeItem: null, // 활성화된 수정/삭제 메뉴의 ID를 추적
    };
  },
  computed: {
    canManage() {
      return this.userRole === 'ADMIN' || this.userRole === 'TEACHER';
    },
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
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${year}년 ${month}월 ${day}일`;
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
    toggleConLayer(id) {
      this.activeItem = this.activeItem === id ? null : id; 
    },
    modifyPost(id) {
      this.$router.push(`/qna/update/answer/${id}`);
    },
    answer(question) {
        this.$router.push(`/qna/answer/${question.id}`);
    },
    async deletePost(id) {
      if (confirm('정말로 삭제하시겠습니까?')) {
        try {
          await axios.get(`${process.env.VUE_APP_API_BASE_URL}/qna/delete/${id}`);
          this.fetchQuestions(); 
        } catch (error) {
          console.error('질문 삭제 중 오류가 발생했습니다:', error);
        }
      }
    },
  },
};
</script>

<style scoped>
.container {
  padding-top: 20px;
}

.LectureDetails {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
}

.inner {
  max-width: 1200px;
  margin: auto;
}

.btnWrap {
  text-align: right;
  margin-bottom: 20px;
}

.btn_write {
  background-color: black;
  color: #fff;
  padding: 10px 20px;
  text-decoration: none;
  border-radius: 5px;
  font-size: 16px;
  display: inline-block;
  transition: background-color 0.3s ease;
}

.btn_write:hover {
  background-color: #0056b3;
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
  /* 기존 스타일 제거 */
  text-decoration: none; /* 링크 밑줄 제거 */
}

.answer {
  color: #dc3545;
  font-weight: bold;
}

.answer.active {
  color: #28a745;
}

.pagingWrap {
  text-align: center;
  margin-top: 20px;
}

.pagingWrap ul {
  list-style: none;
  padding: 0;
}

.pagingWrap li {
  display: inline;
  margin: 0 5px;
}

.pagingWrap a {
  color: #007bff;
  text-decoration: none;
}

.pagingWrap a.active {
  font-weight: bold;
  color: #0056b3;
}

.relative {
  position: relative;
}

.conLayer {
  display: none;
  position: absolute;
  background-color: white;
  border: 1px solid #ddd;
  z-index: 10;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.btn_adm_control {
  background: none;
  border: none;
  cursor: pointer;
}

.relative .btn_adm_control:hover + .conLayer,
.relative .conLayer:hover {
  display: block;
}

.conLayer a {
  display: block;
  padding: 5px 10px;
  color: #333;
  text-decoration: none;
}

.conLayer a:hover {
  background-color: #f4f4f4;
}
</style>
