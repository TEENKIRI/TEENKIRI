<template>
  <v-container>
    <v-sheet class="mx-auto" width="500">
      <v-form ref="form" class="d-flex">
        <v-select
          v-model="searchType"
          :items="searchOptions"
          item-title="text"
          item-value="value"
          required
        ></v-select>
        <v-text-field
          v-model="searchValue"
          label="검색어를 입력하세요."
          required
        ></v-text-field>
      </v-form>
    </v-sheet>
    <v-card>
      <v-card-title>티니키리 서비스가 이 강좌를 추천해요!</v-card-title>
      <v-card-text>
        <div class="swiper swiperLectureBest">
          <div class="swiper-slide" 
            v-for="sm in subject.subjectIsMainList" :key="sm.id"
            @click="goToDetail(sm.id)"
          >
            <div class="thumb">
              <a href="javascript:void(0)"
                ><img
                  v-bind:src="sm.subjectThumUrl"
                  alt="강좌 썸네일"
              /></a>
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
    <v-row class="mt-5">
      <v-col cols="12" class="py-2">
        <v-btn-toggle
          v-model="course.selectedMenu"
          color="primary"
          rounded="0"
          group
          class="d-flex flex-row"
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
    <v-row>
      <v-col>
        <v-item-group
          selected-class="bg-purple"
          multiple
          class="d-flex justify-start"
          v-model="grade.selectedGrades"
          @update:modelValue="onSelectionGradeChange"
        >
          <div class="mr-5">학년</div>
          <v-item v-for="n in grade.gradeList" :key="n.value" v-slot="{ selectedClass, toggle }" :value="n.value">
            <v-chip
              class="mr-2"
              :class="selectedClass"
              @click="toggle"
              :value="n.value"
            >
              {{ n.text }} <!-- 1학년, 2학년 등 -->
            </v-chip>
          </v-item>
        </v-item-group>
      </v-col>
      <v-col>
        <v-row>
          <v-col class="d-flex flex-row">
            <v-btn>강좌 업로드</v-btn>
            <v-select
              v-model="selectedType"
              :items="selectedOptions"
              item-title="text"
              item-value="value"
              width="100"
              required
            ></v-select>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <h1>채팅이 들어가는 자리</h1>
    </v-row>
    <v-row>
      <v-card class="w-100">
        <v-card-title>강좌 목록</v-card-title>
        <v-card-text>
          <div class="swiper swiperLectureBest">
            <div class="swiper-slide" 
              v-for="s in subject.subjectList" :key="s.id"
              @click="goToDetail(s.id)"
            >
              <div class="thumb">
                <a href="javascript:void(0)"
                  ><img
                    v-bind:src="s.subjectThumUrl"
                    alt="강좌 썸네일"
                /></a>
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
      selectedType: "recent",
      selectedOptions: [
        { text: "최신순", value: "recent" },
        { text: "인기순", value: "rank" },
        { text: "평점순", value: "like" },
      ],
      searchValue: "",
      course:{
        courseList:[],
        selectedMenu:"all",
        page:{
          pageSize: 5,
          currentPage:0,
        }
      },
      subject:{
        subjectIsMainList:[],
        subjectList:[],
        page:{
          pageSize: 5,
          currentPage:0,
        }
      },
      grade:{
        gradeList:[ // DB에 있는 GRADE 붙여넣음!
          {value:"GRADE_1", text:"1학년"}, 
          {value:"GRADE_2", text:"2학년"}, 
          {value:"GRADE_3", text:"3학년"}, 
          {value:"GRADE_4", text:"4학년"}, 
          {value:"GRADE_5", text:"5학년"}, 
          {value:"GRADE_6", text:"6학년"},
        ],
        selectedGrades: []
      },
      toggle_none: null,
      toggle_one: 0,
      toggle_exclusive: 2,
      toggle_multiple: [0, 1, 2],
      
    };
  },
  created(){
    this.getCourseList();
    this.getSubjectMainList();
    this.getSubjectList();
  },
  watch:{
      'course.selectedMenu': function(newValue, oldValue) { // 2depth여서 문자열로 작성해야 작동함
      console.log("선택한 메뉴 변수 값이 변경되었다!");
      console.log('변경 전:', oldValue, '변경 후:', newValue);

      this.resetSubjectVariables(); // subject와 관련된 값 초기화

      this.getSubjectList();
    }
  },
  methods: {
    async getCourseList(){
      const params = {
          size: this.course.page.pageSize,
          page: this.course.page.currentPage
      }
      
      const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/course/list`, {params});
      const addtionalData = response.data.result.content;
      console.log(response)
      this.course.courseList = [...this.course.courseList, ...addtionalData]
    },
    async getSubjectMainList(){
      const params = {
          size: 10, //10개만 보이도록 작업~
          page: 0
      }
      
      const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/subject/main/list`, {params});
      const addtionalData = response.data.result.content;
      console.log(response)
      this.subject.subjectIsMainList = [...this.subject.subjectIsMainList, ...addtionalData]
    },
    async getSubjectList(){
      const params = {
          size: this.subject.page.pageSize,
          page: this.subject.page.currentPage
      }
      let api_url = `${process.env.VUE_APP_API_BASE_URL}/subject/${this.course.selectedMenu}/list`
      if(this.course.selectedMenu === "all"){
        api_url = `${process.env.VUE_APP_API_BASE_URL}/subject/list`
      }else{
        // course id가 있는 경우
        if(this.grade.selectedGrades.length >= 1){ //선택된 학년이 있는 경우
          const selectedGradeStr = this.grade.selectedGrades.join("&");
          api_url = `${process.env.VUE_APP_API_BASE_URL}/subject/${this.course.selectedMenu}/${selectedGradeStr}/list`
        }
      }
      
      const response = await axios.get(api_url, {params});
      const addtionalData = response.data.result.content;
      console.log(response)
      this.subject.subjectList = [...this.subject.subjectList, ...addtionalData]
    },
    resetSubjectVariables(){
      this.subject.page.currentPage = 0;
      this.subject.subjectList = [];

      this.grade.selectedGrades = [];
    },
    onSelectionGradeChange(newValue) {
      console.log("선택된 학년이 변경되었습니다:", newValue);
      this.subject.page.currentPage = 0;
      this.subject.subjectList = [];
      this.getSubjectList();
    },

    goToDetail(id) {
      this.$router.push({ name: 'SubjectDetail', params: { id } });
    },
  },
};
</script>
  
  <style>
.inner {
  width: 1280px;
  width: 1280px;
  margin: 0 auto;
  position: relative;
}
.inner h2 {
  font-size: 36px;
  font-weight: 700;
}
.swiperLectureBest {
  margin: 35px 0 0;
  border-right: 1px solid #ccc;
  display: flex;
  flex-wrap: wrap;
}
.swiper-button-prev:after,
.swiper-button-next:after {
  display: none;
}
.swiper-button-prev {
  left: -60px;
  width: 30px;
  height: 51px;
  background: url("@/assets/images/ico_sw_prev.png") 50% 50% no-repeat;
  background-size: contain;
}
.swiper-button-next {
  right: -60px;
  width: 30px;
  height: 51px;
  background: url("@/assets/images/ico_sw_next.png") 50% 50% no-repeat;
  background-size: contain;
}
.swiper-slide {
  position: relative;
  width: calc(100% / 4);
  height: 390px;
  border: 1px solid #ccc;
  padding: 20px 27px;
}
.swiper-slide .thumb {
  width: 100%;
  height: 220px;
  overflow: hidden;
}
.swiper-slide .thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.swiper-slide .txt {
  margin: 20px 0 0;
}
.swiper-slide .txt .subject {
  font-size: 18px;
  font-weight: 400;
}
.swiper-slide .txt .name {
  margin: 15px 0 0;
  font-size: 14px;
  font-weight: 400;
  color: #5b5b5b;
}
.swiper-slide .btn_like {
  position: absolute;
  bottom: 20px;
  right: 27px;
  width: 24px;
  height: 24px;
  background: url("@/assets/images/ico_like.png") 50% 50% no-repeat;
  background-size: contain;
}
</style>