<template>
  <v-container class="mt-5">
    <v-card class="elevation-2">
      <v-card-title class="d-flex align-center justify-space-between">
        <h3 class="mb-0">신고 목록</h3>
        <v-select
          v-model="selectedType"
          :items="types"
          item-title="text"
          item-value="value"
          label="신고 유형 필터"
          @change="filterReports"
          density="compact"
          outlined
          class="filter-select"
        ></v-select>
      </v-card-title>

      <v-divider></v-divider>

      <v-card-text>
        <v-simple-table class="styled-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>신고자 이메일</th>
              <th>피신고자 이메일</th>
              <th>사유</th>
              <th>상세사유</th>
              <th v-if="showQnaCol">QnA ID</th>
              <th v-if="showPostCol">Post ID</th>
              <th v-if="showCommentCol">Comment ID</th>
              <th v-if="showChatMessageCol">Chat Message ID</th> <!-- chatMessageId 컬럼 추가 -->
              <th>생성 시간</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="report in filteredReports" :key="report.id">
              <td>{{ report.id }}</td>
              <td>{{ report.reportEmail }}</td>
              <td>{{ report.suspectEmail }}</td>
              <td>{{ formatReason(report.reason) }}</td>
              <td>{{ report.details }}</td>
              <td v-if="showQnaCol">{{ report.qnaId }}</td>
              <td v-if="showPostCol">{{ report.postId }}</td>
              <td v-if="showCommentCol">{{ report.commentId }}</td>
              <td v-if="showChatMessageCol">{{ report.chatMessageId }}</td> <!-- chatMessageId 데이터 표시 -->
              <td>{{ formatDate(report.createdTime) }}</td>
            </tr>
          </tbody>
        </v-simple-table>

        <v-pagination
          v-if="totalPages > 1"
          v-model:currentPage="page"
          :length="totalPages"
          @input="fetchReports"
          class="mt-4"
          align-self="center"
        ></v-pagination>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      reports: [],
      selectedType: '',
      types: [
        { text: '전체', value: '' },
        { text: 'QnA 신고', value: 'qna' },
        { text: '게시글 신고', value: 'post' },
        { text: '댓글 신고', value: 'comment' },
        { text: '채팅 신고', value: 'chatMessage' } // chatMessage 타입 추가
      ],
      page: 1,
      totalPages: 1,
      loading: false,
    };
  },
  computed: {
    showQnaCol() {
      return this.selectedType === '' || this.selectedType === 'qna';
    },
    showPostCol() {
      return this.selectedType === '' || this.selectedType === 'post';
    },
    showCommentCol() {
      return this.selectedType === '' || this.selectedType === 'comment';
    },
    showChatMessageCol() {
      return this.selectedType === '' || this.selectedType === 'chatMessage';
    },
    filteredReports() {
      return this.reports.filter(report => {
        if (this.selectedType === 'qna' && !report.qnaId) return false;
        if (this.selectedType === 'post' && !report.postId) return false;
        if (this.selectedType === 'comment' && !report.commentId) return false;
        if (this.selectedType === 'chatMessage' && !report.chatMessageId) return false; // 필터에 chatMessageId 추가
        return true;
      });
    }
  },
  methods: {
    async fetchReports() {
      this.loading = true;
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/report/list`, {
          params: {
            type: this.selectedType || '',
            page: this.page - 1
          }
        });
        this.reports = response.data.result.content;
        this.totalPages = response.data.result.totalPages;
      } catch (error) {
        console.error('신고 목록을 불러오는 중 오류가 발생했습니다:', error);
      } finally {
        this.loading = false;
      }
    },
    formatReason(reason) {
      const reasonMap = {
        SPAM: '스팸홍보/도배글',
        PORNOGRAPHY: '음란물',
        ILLEGAL_INFORMATION: '불법정보 포함',
        HARMFUL_TO_MINORS: '청소년에게 유해한 내용',
        ABUSIVE_LANGUAGE: '욕설/생명경시/혐오/차별적 표현',
        PRIVACY_VIOLATION: '개인정보 노출',
        OFFENSIVE_CONTENT: '불쾌한 표현'
      };
      return reasonMap[reason] || '알 수 없는 이유';
    },
    formatDate(date) {
      if (!date) return '';
      const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
      return new Date(date).toLocaleDateString('ko-KR', options);
    },
    filterReports() {
      this.page = 1;
      this.fetchReports();
    }
  },
  created() {
    this.fetchReports();
  }
};
</script>

<style scoped>
.v-container {
  max-width: 1200px;
  margin: 0 auto;
}

.v-card-title {
  font-size: 24px;
  font-weight: bold;
  color: #4a4a4a;
}

.filter-select {
  max-width: 250px;
}

.v-card-text {
  font-size: 16px;
  padding-top: 0;
}

.styled-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
  letter-spacing: 0.5px;
}

.styled-table th, 
.styled-table td {
  padding: 15px 20px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.styled-table th {
  background-color: #f5f5f5;
  font-weight: bold;
}

.styled-table tr:hover {
  background-color: #f1f1f1;
}

.v-pagination {
  justify-content: center;
}

.v-pagination .v-btn {
  color: #1976d2;
}

.v-pagination .v-btn:hover {
  background-color: #e3f2fd;
}

.v-pagination .v-btn--active {
  background-color: #1976d2;
  color: white;
}
</style>
