<template>
  <div class="board-container">
    <div class="inner">
      <h1 class="board-title">{{ boardTitle }}</h1>

      <v-form ref="form" class="d-flex mb-4">
        <v-col cols="12" md="2"> 
          <v-select
            v-model="searchType"
            :items="searchOptions"
            item-title="text"
            item-value="value"
            label="검색 범위"
            required
          ></v-select>
        </v-col>
        <v-col cols="12" md="10"> 
          <v-text-field
            v-model="searchQuery"
            label="검색어를 입력하세요."
            append-icon="mdi-magnify"
            @click:append="performSearch"
            required
          ></v-text-field>
        </v-col>
      </v-form>

      <!-- 게시글 목록 테이블 -->
      <table class="tbl_list">
        <caption></caption>
        <colgroup>
          <col width="80" />
          <col width="" />
          <col width="140" />
          <col width="140" />
          <col width="140" />
        </colgroup>
        <thead>
          <tr>
            <th scope="col">번호</th>
            <th scope="col">제목</th>
            <th scope="col">작성자</th>
            <th scope="col">작성일</th>
            <th scope="col">관리</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in boardItems" :key="item.id">
            <td>{{ index + 1 + (currentPage - 1) * itemsPerPage }}</td>
            <td @click="goToDetail(item.id, category)" class="text_left subject">{{ item.title }}</td>
            <td>{{ item.nickname }}</td>
            <td>{{ formatDate(item.createdTime) }}</td>
            <td>
              <button
                type="button"
                class="btn_adm_control"
                v-if="canEditOrDelete(item)"
                @click="toggleConLayer(item.id)"
              >•••</button>
              <div class="conLayer" v-if="activeItem === item.id">
                <a href="javascript:void(0)" class="btn_board_modify" @click="modifyPost(item.id)">수정</a>
                <a href="javascript:void(0)" class="btn_board_del btn_del" @click="deletePost(item.id)">삭제</a>
              </div>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- 작성하기 버튼 -->
      <div class="btnWrap">
        <button 
          v-if="isAdmin || category === 'post'" 
          @click="createNewPost" 
          class="btn_write"
        >
          작성하기
        </button>
      </div>

      <!-- 페이지네이션 -->
      <v-pagination
        v-model="currentPage"
        :length="totalPages"
        @input="fetchBoardItems"
        class="my-4"
      ></v-pagination>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      boardItems: [], // 게시글 목록 데이터
      currentPage: 1, // 현재 페이지 번호
      totalPages: 1, // 총 페이지 수
      itemsPerPage: 10, // 페이지당 항목 수
      isAdmin: false, // 관리자인지 여부
      userId: null, // 현재 로그인된 사용자의 ID
      role: null, // 현재 로그인된 사용자의 역할
      category: '', // 현재 게시판 종류
      boardTitle: '', // 게시판 제목
      activeItem: null, // 현재 열려있는 conLayer의 아이템 ID

      // 검색 필드 추가
      searchType: 'all',
      searchQuery: '',
      searchOptions: [
        { text: "전체", value: "all" },
        { text: "제목", value: "title" },
        { text: "작성자", value: "userNickname" },
      ], 
    };
  },
  watch: {
    '$route.params.category': 'updateCategoryAndFetchItems', // category가 변경될 때마다 호출
  },
  created() {
    this.checkUserRole();
    this.updateCategoryAndFetchItems(); // 컴포넌트 생성 시 게시글 목록을 가져옴
    this.userId = localStorage.getItem('userId'); // 로컬스토리지에서 userId 가져오기
  },
  methods: {
    checkUserRole() {
      const role = localStorage.getItem('role');
      this.isAdmin = role === 'ADMIN';
      this.role = role;
    },
    updateCategoryAndFetchItems() {
      this.category = this.$route.params.category;
      this.setBoardTitle();
      this.fetchBoardItems();
    },
    async fetchBoardItems() {
        try {
            const params = {
                page: this.currentPage - 1,
                size: this.itemsPerPage,
                searchType: this.searchType,
                searchQuery: this.searchQuery,
            };

            // 이 부분은 검색 카테고리에 따라 'all'을 처리하는 로직입니다.
            if (this.searchType === 'all' && this.searchQuery) {
                params.searchType = 'all';
            }

            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/board/${this.category}/list`, { params });

            const result = response.data.result;
            if (result && result.content) {
                this.boardItems = result.content;
                this.totalPages = result.totalPages;
            } else {
                console.error('올바르지 않은 데이터 형식입니다:', response.data);
            }
        } catch (error) {
            console.error('목록을 가져오는 중 오류가 발생했습니다:', error);
        }
    },

    setBoardTitle() {
      if (this.category === 'event') {
        this.boardTitle = '이벤트 게시판';
      } else if (this.category === 'post') {
        this.boardTitle = '자유게시판';
      } else if (this.category === 'notice') {
        this.boardTitle = '공지사항';
      } else {
        this.boardTitle = '게시판';
      }
    },
    formatDate(date) {
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(date).toLocaleDateString(undefined, options);
    },
    goToPreviousPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchBoardItems();
      }
    },
    goToNextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchBoardItems();
      }
    },
    goToPage(page) {
      if (page !== this.currentPage) {
        this.currentPage = page;
        this.fetchBoardItems();
      }
    },
    createNewPost() {
      if (this.category !== 'post' && !this.isAdmin) {
        alert('관리자만 이 게시판에 글을 작성할 수 있습니다.');
        return;
      }
      this.$router.push({ name: 'BoardCreate', params: { category: this.category } });
    },
    goToDetail(id) {
      this.$router.push({ name: 'BoardDetail', params: { id, category: this.category } });
    },
    canEditOrDelete(item) {
      return this.isAdmin || item.userId === this.userId;
    },
    toggleConLayer(itemId) {
      this.activeItem = this.activeItem === itemId ? null : itemId;
    },
    modifyPost(id) {
      this.$router.push({ name: 'BoardUpdate', params: { id, category: this.category } });
    },
    async deletePost(id) {
      if (confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
        let apiUrl = `${process.env.VUE_APP_API_BASE_URL}/board/${this.category}/delete/${id}`;

        try {
          await axios.delete(apiUrl);
          this.fetchBoardItems();
          alert('게시글이 삭제되었습니다.');
        } catch (error) {
          console.error('게시글을 삭제하는 데 실패했습니다:', error);
          alert('게시글 삭제에 실패했습니다.');
        }
      }
    },
    performSearch() {
      this.currentPage = 1; // 검색할 때 첫 페이지로 초기화
      this.fetchBoardItems();
    },
  },
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
  border: none; /* 모든 테두리 제거 */
  padding: 10px;
  text-align: left;
}

/* 테두리 스타일을 유지하려면 아래 코드 사용 */
.tbl_list th {
  background-color: #f4f4f4;
}

.tbl_list tr:not(:last-child) td {
  border-bottom: 1px solid #ccc; /* 하단 선만 유지 */
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

.btn_adm_control {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 14px;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s, color 0.3s;
  border-radius: 8px;
}

.btn_adm_control:hover {
  background-color: #ccc;
}

.btn_board_modify {
  background-color: #6cb1ff;
  color: #fff;
  padding: 5px 10px;
  border-radius: 4px;
  text-decoration: none;
  display: inline-block;
}

.btn_board_modify:hover {
  background-color: #007bff;
}

.btn_board_del {
  background-color: #f57380;
  color: #fff;
  padding: 5px 10px;
  border-radius: 4px;
  text-decoration: none;
  display: inline-block;
}

.btn_board_del:hover {
  background-color: #dc3545;
}

.conLayer {
  display: inline-block;
  background-color: white;
  border: 1px solid #ccc;
  position: absolute;
  z-index: 1;
  right: 0;
  padding: 5px;
}

.btnWrap {
  text-align: right;
  margin-top: 20px;
}

.btn_write {
  padding: 10px 20px;
  background-color: #f27885;
  color: #fff;
  border: none;
  cursor: pointer;
  border-radius: 4px;
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