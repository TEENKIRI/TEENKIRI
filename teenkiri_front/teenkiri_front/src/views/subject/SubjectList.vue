<template>
  <v-container>
    <!-- 검색 및 필터 폼 -->
    <v-sheet class="mx-auto" width="600">
      <v-form ref="form" class="d-flex">
        <v-select
          v-model="searchType"
          :items="searchOptions"
          item-title="text"
          item-value="value"
          label="검색 범위"
          class="mr-4"
          required
        ></v-select>
        <v-text-field
          v-model="searchValue"
          label="검색어를 입력하세요."
          append-icon="mdi-magnify"
          @click:append="performSearch"
          required
        ></v-text-field>
      </v-form>
    </v-sheet>
    
    <!-- 추천 강좌 섹션 -->
    <v-card class="mt-5">
      <v-card-title>티니키리 서비스가 이 강좌를 추천해요!</v-card-title>
      <v-card-text>
        <div class="swiper swiperLectureBest">
          <div class="swiper-slide" 
            v-for="sm in subject.subjectIsMainList" :key="sm.id"
            @click="goToDetail(sm.id)"
          >
            <div class="thumb">
              <a href="javascript:void(0)">
                <img v-bind:src="sm.subjectThumUrl" alt="강좌 썸네일" />
              </a>
            </div>
            <div class="txt">
              <p class="subject">{{ sm.title }}</p>
              <p class="name">{{ sm.teacherName }}</p>
            </div>
            <button type="button" class="btn_like"></button>
          </div>
        </div>
      </v-card-text>
    </v-card>

    <!-- 과목 선택 섹션 -->
    <v-row class="mt-5">
      <v-col cols="12" class="py-2">
        <v-btn-toggle
          v-model="course.selectedMenu"
          color="primary"
          rounded="0"
          group
          class="d-flex flex-row"
          @change="performSearch"
        >
          <v-btn class="flex-grow-1" value="all">
            all
          </v-btn>
          <v-btn class="flex-grow-1" v-for="c in course.courseList" :key="c.id" :value="c.id">
            {{ c.title }}
          </v-btn>
        </v-btn-toggle>
      </v-col>
    </v-row>

    <!-- 학년 선택 및 정렬 -->
    <v-row>
      <v-col>
        <v-item-group
          selected-class="bg-purple"
          multiple
          class="d-flex justify-start"
          v-model="grade.selectedGrades"
          @change="performSearch"
        >
          <div class="mr-5">학년</div>
          <v-item v-for="n in grade.gradeList" :key="n.value" v-slot="{ selectedClass, toggle }" :value="n.value">
            <v-chip
              class="mr-2"
              :class="selectedClass"
              @click="toggle"
              :value="n.value"
            >
              {{ n.text }}
            </v-chip>
          </v-item>
        </v-item-group>
      </v-col>
      <v-col>
        <v-row>
          <v-col class="d-flex flex-row align-center">
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
            <v-btn class="ml-4" color="success" @click="$router.push('/subject/create')">강좌 업로드</v-btn>
          </v-col>
        </v-row>
      </v-col>
    </v-row>

    <!-- 강좌 목록 섹션 -->
    <v-row class="mt-5">
      <v-card class="w-100">
        <v-card-title>강좌 목록</v-card-title>
        <v-card-text>
          <div class="swiper swiperLectureBest">
            <div class="swiper-slide" 
              v-for="s in subject.subjectList" :key="s.id"
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
              <button type="button" class="btn_like"></button>
            </div>
          </div>
        </v-card-text>
      </v-card>
    </v-row>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      searchType: "all",
      searchOptions: [
        { text: "전체", value: "all" },
        { text: "강사명", value: "userTeacher" },
        { text: "강좌명", value: "title" },
      ],
      selectedType: "latest",  // 최신순 또는 평점순
      selectedOptions: [
        { text: "최신순", value: "latest" },
        { text: "평점순", value: "like" },
      ],
      searchValue: "",
      course: {
        courseList: [],
        selectedMenu: "all",  // 선택된 과목 ID
        page: {
          pageSize: 5,
          currentPage: 0,
        }
      },
      subject: {
        subjectList: [],
        page: {
          pageSize: 5,
          currentPage: 0,
        }
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
        selectedGrades: []  
      },
    };
  },
  created() {
    this.getCourseList();
    this.getSubjectList();
  },
  watch: {
    selectedType() {
      this.performSearch(); 
    },
    selectedGrades() {
      this.performSearch();  
    },
    'course.selectedMenu': function() {
      this.performSearch();  
    },
    searchValue() {
      this.performSearch();  
    },
    searchType() {
      this.performSearch();  
    }
  },

  methods: {
    async getCourseList() {
      try {
        const params = {
          size: this.course.page.pageSize,
          page: this.course.page.currentPage
        };
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/course/list`, { params });
        this.course.courseList = response.data.result.content;
      } catch (e) {
        console.error(e);
      }
    },
    async getSubjectList() {
      try {
        const params = {
          size: this.subject.page.pageSize,
          page: this.subject.page.currentPage,
          search: this.searchValue, 
          searchType: this.searchType,
          sortType: this.selectedType,  
          grades: this.grade.selectedGrades.join("&"),
          courseId: this.course.selectedMenu !== "all" ? this.course.selectedMenu : null, 
        };

        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/subject/list`, { params });
        this.subject.subjectList = response.data.result.content;
      } catch (e) {
        console.error(e);
      }
    },

    resetSubjectVariables() {
      this.subject.page.currentPage = 0;
      this.subject.subjectList = [];
    },

    performSearch() {
      this.resetSubjectVariables(); 
      this.getSubjectList();
    },

    goToDetail(id) {
      this.$router.push({ name: 'SubjectDetail', params: { id } });
    },
  },
};
</script>

<style src="@/assets/css/SubjectList.css"></style>
