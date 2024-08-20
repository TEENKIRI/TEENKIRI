<template>
  <v-container>
    <!-- 추천 강좌 섹션 -->
    <v-card class="mt-5">
      <v-card-title class="font_notrahope" style="font-size:2rem;">
        티니키리 서비스가 <span class="teen_red_font">추천</span>해요! 👍
      </v-card-title>
      <v-card-text v-if="subject.subjectIsMainList.length">
        <swiper
          :slides-per-view="4"
          :spaceBetween="30"
          :loop="true"
          :pagination="{clickable: true}"
          :modules="modules"
          :autoplay="{
            delay: 2000,
            disableOnInteraction: false,
          }"
        >
          <swiper-slide 
              v-for="sm in subject.subjectIsMainList"
              :key="sm.id"
              @click="goToDetail(sm.id)"
            >
              <v-img
                aspect-ratio="16/9"
                cover
                :src="sm.subjectThumUrl"
                style="border-radius: 0px;"
                alt="강좌 썸네일"
              ></v-img>
              <div class="txt">
                <p class="subject">{{ sm.title }}</p>
                <p class="name">{{ sm.teacherName }}</p>
              </div>
            </swiper-slide>
        </swiper>
      </v-card-text>
    </v-card>

    <!-- 검색 및 정렬 섹션 -->
    <br />
    <v-row>
      <v-col class="d-flex flex-row align-center" cols="12" md="2">
        <v-select
          v-model="selectedType"
          :items="selectedOptions"
          item-title="text"
          item-value="value"
          label="정렬 기준"
          class="ml-auto"
          width="200"
          required
          @change="performSearch"
        ></v-select>
      </v-col>

      <v-col cols="4" md="3">
        <v-select
          v-model="searchType"
          :items="searchOptions"
          item-title="text"
          item-value="value"
          label="검색 범위"
          required
        ></v-select>
      </v-col>
      <v-col cols="8" md="7">
        <v-text-field
          v-model="searchValue"
          label="검색어를 입력하세요."
          append-icon="mdi-magnify"
          @click:append="performSearch"
          required
        ></v-text-field>
      </v-col>
    </v-row>

    <!-- 과목 선택 섹션 -->
    <div class="text-right mb-2">
    <v-btn
      color="grey-darken-1"
      @click="$router.push('/subject/create')"
      v-if="this.user.role === `ADMIN`"
      >강좌 업로드</v-btn
    >
    <v-btn
      color="grey-darken-1"
      @click="$router.push('/course/create')"
      v-if="this.user.role === `ADMIN`"
      >과목 업로드</v-btn
    >
    </div>
    

    <v-row style="background:#ffffff;">
      <v-col cols="12" class="py-2">
        <v-btn-toggle
          v-model="course.selectedMenu"
          selected-class="teen_mint_bg_c_white"
          rounded="0"
          group
          class="d-flex flex-row"
          @change="performSearch"
        >
          <v-btn class="flex-grow-1" value="all"> all </v-btn>
          <v-btn
            class="flex-grow-1"
            v-for="c in course.courseList"
            :key="c.id"
            :value="c.id"
          >
            {{ c.title }}
            <span
              v-if="this.user.role === `ADMIN`"
              class="mdi mdi-pencil"
              style="
                width: 24px;
                height: 24px;
                font-size: 24px;
                line-height: 24px;
                color: #f27885;
              "
              @click="editCourse(c.id, $event)"
            ></span>
          </v-btn>
        </v-btn-toggle>
      </v-col>
    </v-row>

    <!-- 학년 선택 및 정렬 -->
    <v-row>
      <v-col>
        <v-item-group
          selected-class="teen_mint_bg_c_white"
          multiple
          class="d-flex justify-start"
          v-model="grade.selectedGrades"
          @change="performSearch"
        >
          <div class="mr-5">학년</div>
          <v-item
            v-for="n in grade.gradeList"
            :key="n.value"
            v-slot="{ selectedClass, toggle }"
            :value="n.value"
          >
            <v-chip class="mr-2" :class="selectedClass" @click="toggle" :value="n.value">
              {{ n.text }}
            </v-chip>
          </v-item>
        </v-item-group>
        <br />
      </v-col>
    </v-row>

    <!-- 강좌 목록 섹션 -->
    <v-card class="py-5 px-5">
        <!-- <v-card-title>강좌 목록</v-card-title> -->
        <v-card-text class="lectureList">
            <div
              class="item"
              v-for="s in subject.subjectList"
              :key="s.id"
              @click="goToDetail(s.id)"
            >
              <div class="thumb">
                <a href="javascript:void(0)">
                  <img v-bind:src="s.subjectThumUrl" alt="강좌 썸네일" />
                </a>
              </div>
              <div class="txt">
                <p class="subject">{{ s.title }}</p>
                <p class="name">{{ s.teacherName }}</p>
              </div>
              <div class="text-right">
                <button
                type="button"
                class="btn_like"
                @click="toggleWish(s.id, $event, `subjectList`)"
                :class="{
                  'mdi mdi-heart': s.isSubscribe,
                  'mdi mdi-heart-outline': !s.isSubscribe,
                }"
              ></button>
              </div>
            </div>
        </v-card-text>
    </v-card>

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
import { Swiper, SwiperSlide } from 'swiper/vue';
import { Autoplay, Pagination, Navigation } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/pagination';
import 'swiper/css/navigation';

export default {
  components: {
    Swiper,
    SwiperSlide
  },
  setup() {
    const onSwiper = (swiper) => {
      console.log(swiper);
    };
    const onSlideChange = () => {
      console.log('slide change');
    };
    return {
      onSwiper,
      onSlideChange,
      modules: [Autoplay, Pagination, Navigation],
    };
  },
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
      course: {
        courseList: [],
        selectedMenu: "all", // 선택된 과목 ID
        page: {
          pageSize: 5,
          currentPage: 0,
        },
      },
      subject: {
        subjectList: [],
        subjectIsMainList: [],
        page: {
          pageSize: 5,
          currentPage: 0,
        },
      },
      grade: {
        gradeList: [
          { value: "GRADE_1", text: "1학년" },
          { value: "GRADE_2", text: "2학년" },
          { value: "GRADE_3", text: "3학년" },
          { value: "GRADE_4", text: "4학년" },
          { value: "GRADE_5", text: "5학년" },
          { value: "GRADE_6", text: "6학년" },
        ],
        selectedGrades: [],
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
          size:9,
          page: this.page - 1, // MySQL에서의 페이지는 0부터 시작
          search: this.searchValue,
          searchType: this.searchType,
          sortType: this.selectedType,
          grades: this.grade.selectedGrades.join("&"),
          courseId: this.course.selectedMenu !== "all" ? this.course.selectedMenu : null,
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
        async getCourseList() {
      try {
        const params = {
          size: 1000,
          page: this.course.page.currentPage,
        };
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/course/list`,
          { params }
        );
        this.course.courseList = response.data.result.content;
      } catch (e) {
        console.error(e);
      }
    },
    async getSubjectMainList() {
      try {
        const params = {
          size: 10, // 상단 추천 강좌는 10개만 보임
          page: 0,
        };
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/subject/main/list`,
          { params }
        );
        this.subject.subjectIsMainList = [
          ...this.subject.subjectIsMainList,
          ...response.data.result.content,
        ];
      } catch (e) {
        console.error(e);
      }
    },
    goToDetail(id) {
      this.$router.push({ name: "SubjectDetail", params: { id } });
    },
    async toggleWish(id, event, type) {
      event.stopPropagation(); // 이벤트 전파 방지
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/wish/toggle/${id}`
        );

        let subject;
        if (type == "subjectList") {
          subject = this.subject.subjectList.find((s) => s.id === id);
        } else if (type == "subjectMain") {
          subject = this.subject.subjectIsMainList.find((sm) => sm.id === id);
        }

        if (subject) {
          subject.isSubscribe = response.data.result.status;
        }
      } catch (error) {
        alert("찜 추가 실패");
        console.error(error);
      }
    },
    editCourse(id, event) {
      event.stopPropagation(); // 이벤트 전파 방지
      this.$router.push({ name: "CourseEdit", params: { id } });
    },
  },
  watch: {
    page() {
    this.performSearch();
  },
    selectedType() {
      this.performSearch();
    },
    selectedGrades() {
      this.performSearch();
    },
    "course.selectedMenu": function () {
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
    this.getCourseList();
    this.getSubjectMainList();
    this.performSearch();
  },
};
</script>

<!-- <style src="@/assets/css/SubjectList.css"></style> -->
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

.swiper-pagination{
  position:static;
  margin-top:12px;
}

</style>