<template>
  <v-container class="mt-5">
    <v-card>
      <v-card-title>
        <h3>과목 관리</h3>
      </v-card-title>

      <v-card-text>
        <v-form ref="form" @submit.prevent="submitCourse">
          <!-- 과목 제목 -->
          <v-text-field label="과목명" v-model="this.courseData.title" required />
          <div class="mt-3">
            <v-btn type="submit" color="primary">저장</v-btn>
            <v-btn color="red" class="ml-3" v-if="this.routeName === 'CourseEdit'" @click="deleteCourse"
              >삭제</v-btn
            >
          </div>
        </v-form>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      user: {},
      courseId: this.$route.params.id,
      routeName: this.$route.name,
      courseData: {},
    };
  },
  async created() {
    try {
      await this.$store.dispatch("setUserAllInfoActions");
      this.user = this.$store.getters.getUserObj;

      if (this.user.token === "") {
        alert("로그인이 필요합니다.");
        location.href = -1;
      } else if (this.user.role !== "ADMIN") {
        alert("관리자만 생성이 가능합니다!");
        location.href = -1;
      } else {
        this.courseId =
          this.courseId == undefined || this.courseId == "" ? "" : this.courseId;
        console.log("courseId 아이디 >> ", this.courseId);
        console.log("routeName 라우터 이름 >> ", this.routeName);
        if (this.routeName === "CourseEdit") {
          // 수정용 라우터
          this.getCourseDetail();
        }
      }
    } catch (error) {
      console.error("사용자 정보를 가져오는 중 오류가 발생했습니다:", error);
    }
  },
  methods: {
    async getCourseDetail() {
      console.log("detail 불러오기~~~");
      if (this.courseId != "") {
        try {
          const response = await axios.get(
            `${process.env.VUE_APP_API_BASE_URL}/course/detail/${this.courseId}`
          );
          this.courseData = response.data.result;
          console.log(this.courseData);
        } catch (error) {
          console.error("과목 세부 정보 조회 실패:", error);
        }
      }
    },
    async submitCourse() {
      console.log("이얍~~");
      this.courseData.title = this.courseData.title.trim();

      if (this.courseData.title != "") {
        try {
          const postData = { title: this.courseData.title };
          let apiUrl = `${process.env.VUE_APP_API_BASE_URL}/course/create`;
          if (this.routeName === "CourseEdit") {
            apiUrl = `${process.env.VUE_APP_API_BASE_URL}/course/update/${this.courseId}`;
            await axios.patch(apiUrl, postData);
            alert("과목이 성공적으로 수정되었습니다!");
          } else {
            await axios.post(apiUrl, postData);
            alert("과목이 성공적으로 등록되었습니다!");
          }
          history.go(-1);
        } catch (error) {
          console.error("Error details:", error);
        }
      }
    },
    async deleteCourse() {
      if (this.courseId != "") {
        try {
          const response = await axios.delete(
            `${process.env.VUE_APP_API_BASE_URL}/course/delete/${this.courseId}`
          );
          console.log(response)
          alert("과목 삭제가 완료되었습니다.")
          history.go(-1);
        }catch (error) {
          console.error("Error details:", error);
        }
      }
    },
  },
};
</script>
