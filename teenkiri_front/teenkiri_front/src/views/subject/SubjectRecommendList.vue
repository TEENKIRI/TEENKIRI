<template>
  <v-container>
    <v-row class="align-end">
      <v-col class="font_notrahope" style="font-size:2.5rem;">
        티니키리 서비스가 <span class="teen_red_font">추천</span>해요! 👍
      </v-col>
      <v-col class="text-right">
        <v-btn v-if="this.user.role===`ADMIN`">강좌 업로드</v-btn>
      </v-col>
    </v-row>

    <!-- 강좌 목록 섹션 -->
    <v-row>
      <v-card class="w-100 py-5 px-5 mb-5">
        <v-card-text class="lectureList">
          <div
            class="item"
            v-for="s in subject.subjectList"
            :key="s.id"
            @click="goToDetail(s.id)"
          >
            <div class="thumb">
              <a href="javascript:void(0)"
                ><img v-bind:src="s.subjectThumUrl" alt="강좌 썸네일"
              /></a>
            </div>
            <div class="txt">
              <p class="subject">{{ s.title }}</p>
              <p class="name">{{ s.teacherName }}</p>
            </div>
            <div class="text-right">
              <button type="button" 
                class="btn_like" 
                @click="toggleWish(s.id, $event)" 
                :class="{ 'mdi mdi-heart': s.isSubscribe, 'mdi mdi-heart-outline': !s.isSubscribe }">
              </button>
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
          pageSize: 9,
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
        };

        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/subject/main/list`,
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
      event.stopPropagation(); // 이벤트 전파 방지
      if (this.user.token != null) { // 로그인한 유저만 가능
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
