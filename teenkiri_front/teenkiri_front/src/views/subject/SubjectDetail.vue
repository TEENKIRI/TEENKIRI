<template>
  <v-app>
    <v-container>
      <!-- subjectData가 로드된 후에만 컴포넌트를 렌더링 -->
      <SubjectDetailComponent @subject-data-loaded="handleSubjectDataLoaded" v-model="selectedMenu" />
      <!-- <SubjectDetailComponent  /> -->

      <v-row v-if="this.user.isAdmin">
        <v-col>
        <router-link to="/lecture/create">
          <v-btn>강의 업로드하기</v-btn>
        </router-link>
        </v-col>
      </v-row>

      <v-row>
        <v-col>
          <v-table>
            <thead>
              <tr>
                <th>번호</th>
                <th>제목</th>
                <th>수강여부</th>
                <th>강의시간</th>
                <th>이동</th>
                <th v-if="this.user.isAdmin">관리</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in lecture.lectureList" :key="item.id">
                <td>{{ item.id }}</td>
                <td>{{ item.title }}</td>
                <td>{{ item.progress }}</td>
                <td>{{ item.videoDuration }}</td>
                <td>
                  <v-btn v-if="user.userId" 
                    @click="goToDetail(item.id)">강의보기
                  </v-btn>
                </td>
                <td v-if="this.user.isAdmin">
                  <v-btn  
                    :to="`/lecture/create/${item.id}`">수정
                  </v-btn>
                </td>
              </tr>
            </tbody>
          </v-table>
        </v-col>
      </v-row>

      <!-- 강의 목록에 해당하는 내용 -->
    </v-container>
  </v-app>
</template>

<script>
import SubjectDetailComponent from "@/components/subject/SubjectDetailComponent.vue";
import axios from "axios";

export default {
  components: {
    SubjectDetailComponent,
  },
  provide() {
    return {
      getSubjectData: () => this.subjectData, // subjectData를 함수로 제공
    };
  },
  data() {
    return {
      user: {},

      subjectData: null, // 초기 상태는 null

      subjectId: "",
      selectedMenu: "SubjectDetail", // 기본으로 선택된 메뉴
      lecture: {
        lectureList: [],
        page: {
          pageSize: 5,
          currentPage: 0,
        },
      },
    };
  },
  async created() {
    this.subjectId = this.$route.params.id;
    try {
      await this.$store.dispatch("setUserAllInfoActions");
      this.user = this.$store.getters.getUserObj;
    } catch (error) {
      console.error("An error occurred while fetching user info:", error);
    }

    this.getSubjectPerLectureList();
  },
  watch: {
    subjectData: {
      deep: true,
      handler(newVal) {
        console.log("Subject Data Updated:", newVal);
      },
    },
  },
  methods: {
    async getSubjectPerLectureList() {
      const params = {
        size: this.lecture.page.pageSize,
        page: this.lecture.page.currentPage,
      };

      const response = await axios.get(
        `${process.env.VUE_APP_API_BASE_URL}/subject/${this.subjectId}/lecture/list`,
        { params }
      );
      const addtionalData = response.data.result.content;
      console.log(response);
      this.lecture.lectureList = [...this.lecture.lectureList, ...addtionalData];
      
    },
    setLectureCreateBtn() {
      console.log(this.user);
      console.log(this.user.userId, this.subjectData.userTeacherId)
      if(Number(this.user.userId) === this.subjectData.userTeacherId || this.user.role == "ADMIN"){
        console.log("수정 가능!!")
        this.user.isAdmin = true;
      }else{
        console.log("수정 불가!")
      }
    },
    handleSubjectDataLoaded(data) {
      this.subjectData = data;
      // 여기에 추가 작업을 작성하면 됩니다.
      if(this.subjectData && this.user.userId){
        this.setLectureCreateBtn();
      }
    },
    handleMenuUpdate(newSelection) {
      this.selectedMenu = newSelection;
      console.log("메뉴 바뀜 >> ", newSelection);
    },
    goToDetail(lectureId) {
      console.log("강의페이지로 이동~", lectureId);
      this.$router.push({ name: "LectureDetail", params: { id: lectureId } });
    },
  },
};
</script>

<style></style>
