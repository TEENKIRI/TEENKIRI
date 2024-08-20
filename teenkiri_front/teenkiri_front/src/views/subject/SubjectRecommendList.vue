<template>
  <v-container>
    <h1>티니키리 서비스가 추천하는 강좌 👍</h1>

    <!-- 강좌 업로드 버튼 (관리자만 표시) -->
    <v-row>
      <v-col>
        <v-row v-if="this.user.role === 'ADMIN'">
          <v-col class="d-flex flex-row">
            <v-btn @click="$router.push('/subject/create')">강좌 업로드</v-btn>
          </v-col>
        </v-row>
      </v-col>
    </v-row>

    <!-- 강좌 목록 섹션 -->
    <v-row>
      <v-card class="w-100">
        <v-card-title>강좌 목록</v-card-title>
        <v-card-text>
          <div class="swiper swiperLectureBest">
            <div
              class="swiper-slide"
              v-for="s in subject.subjectList"
              :key="s.id"
              @click="goToDetail(s.id)"
            >
              <div class="thumb">
                <a href="javascript:void(0)">
                  <img :src="s.subjectThumUrl" alt="강좌 썸네일" />
                </a>
              </div>
              <div class="txt">
                <p class="subject">{{ s.title }}</p>
                <p class="name">{{ s.teacherName }}</p>
              </div>
              <button
                type="button"
                class="btn_like"
                @click.stop="toggleWish(s.id, 'subjectList')"
                :class="{
                  'mdi mdi-heart': s.isSubscribe,
                  'mdi mdi-heart-outline': !s.isSubscribe,
                }"
              ></button>
            </div>
          </div>
        </v-card-text>
      </v-card>
    </v-row>

    <!-- 페이지네이션 -->
    <v-pagination
      v-model="page"
      :length="totalPages"
      @input="performSearch"
      class="my-4"
    ></v-pagination>
  </v-container>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      user: {},
      searchType: "all",
      searchOptions: [
        { text: "전체", value: "all" },
        { text: "강사명", value: "userTeacher" },
        { text: "강좌명", value: "title" },
      ],
      selectedType: "latest", // 최신순 또는 평점순
      selectedOptions: [
        { text: "최신순", value: "latest" },
        { text: "평점순", value: "like" },
      ],
      searchValue: "",
      subject: {
        subjectList: [],
        page: {
          pageSize: 5,
          currentPage: 1,
        },
      },
      page: 1,
      totalPages: 1,
    };
  },
  methods: {
    onPageChange(page) {
      this.page = page;
      this.performSearch();
    },
    async performSearch() {
      try {
        const params = {
          size: this.subject.page.pageSize,
          page: this.page - 1, // MySQL에서의 페이지는 0부터 시작
          search: this.searchValue,
          searchType: this.searchType,
          sortType: this.selectedType,
        };

        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/subject/list`,
          { params }
        );
        this.subject.subjectList = response.data.result.content;
        this.totalPages = response.data.result.totalPages;
      } catch (e) {
        console.error(e);
      }
    },
    goToDetail(id) {
      this.$router.push({ name: "SubjectDetail", params: { id } });
    },
    async toggleWish(id) {
      if (Object.keys(this.user).length > 0) { // 로그인한 유저만 가능
        try {
          const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/wish/toggle/${id}`);
          const subject = this.subject.subjectList.find((sm) => sm.id === id);
          if (subject) {
            subject.isSubscribe = response.data.result.status;
          }
        } catch (error) {
          alert("찜 추가 실패");
          console.error(error);
        }
      } else {
        alert("로그인 후 사용 가능합니다.");
      }
    },
  },
  watch: {
    page() {
      this.performSearch();
    },
    selectedType() {
      this.performSearch();
    },
    searchValue() {
      this.performSearch();
    },
    searchType() {
      this.performSearch();
    },
  },
  async created() {
    try {
      await this.$store.dispatch("setUserAllInfoActions");
      this.user = this.$store.getters.getUserObj;
    } catch (error) {
      console.error("사용자 정보를 가져오는 중 오류가 발생했습니다:", error);
    }
    this.performSearch();
  },
};
</script>

<style>
.select-small .v-select {
  min-height: 32px;
  max-height: 32px;
  font-size: 14px;
  line-height: 32px;
}

.v-btn + .v-btn {
  margin-left: 16px;
}
</style>
