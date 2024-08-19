<template>
  <v-app>
    <v-container>
      <!-- subjectData가 로드된 후에만 컴포넌트를 렌더링 -->
      <SubjectDetailComponent
        @subject-data-loaded="handleSubjectDataLoaded"
        v-model="selectedMenu"
      />
      <!-- <SubjectDetailComponent  /> -->

      <div class="btnWrap" v-if="this.user.isAdmin">
        <v-col>
          <router-link :to="`/lecture/create/${subjectId}`">
            <v-btn class="btn_write">강의 업로드하기</v-btn>
          </router-link>
        </v-col>
      </div>

      <!-- 강의 목록에 해당하는 내용 -->
      <!-- 강의 목록 -->
      <table class="tbl_list">
        <caption></caption>
        <colgroup>
          <col width="80" />
          <col width="" />
          <col width="140" />
          <col :width="isAdmin ? '180' : '280'" /> <!-- 수강여부 열의 너비를 관리자가 아닐 때 넓힘 -->
          <col v-if="isAdmin" width="100" /> <!-- 관리 열 -->
        </colgroup>
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th class="text-center">수강여부</th>
            <th>강의시간</th>
            <th>이동</th>
            <th v-if="this.user.isAdmin">관리</th> <!-- 관리 버튼 헤더 -->
          </tr>
        </thead>
        <tbody>
          <tr v-if="lecture.lectureList.length === 0">
            <td :colspan="isAdmin ? 6 : 5" class="text_center">강의가 없습니다.</td>
          </tr>
          <tr v-for="(item, index) in lecture.lectureList" :key="item.id">
            <td>{{ index + 1 }}</td>
            <td class="text_left">{{ item.title }}</td>
            <td class="text-center">
              <div class="py-2">
                    <div v-if="item.progress">{{ item.progress }} %</div>
                    <div>
                      <v-chip v-if="item.isCompleted" color="primary">
                        수강완료
                      </v-chip>
                      <v-chip v-if="item.isCompleted === false" color="green">
                        수강 중
                      </v-chip>
                      <v-chip
                        v-if="item.isCompleted === null"
                        color="secondary"
                      >
                        시청 전
                      </v-chip>
                    </div>
                  </div>
            </td>
            <td>{{ item.videoDuration }}</td>
            <td>
              <a v-if="user.userId" href="javascript:void(0)" class="btn_write" @click="goToDetail(item.id)">강의보기</a>
            </td>
            <td v-if="this.user.isAdmin">
              <a href="javascript:void(0)" class="btn_write" :to="`/lecture/edit/${item.id}`">수정</a>
            </td>
          </tr>
        </tbody>
      </table>
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
      console.error("사용자 정보를 가져오는 중 오류가 발생했습니다:", error);
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
      const additionalData = response.data.result.content;
      console.log(response);
      this.lecture.lectureList = [
        ...this.lecture.lectureList,
        ...additionalData,
      ];
    },
    setLectureCreateBtn() {
      console.log(this.user);
      console.log(this.user.userId, this.subjectData.userTeacherId);
      if (Number(this.user.userId) === this.subjectData.userTeacherId || this.user.role == "ADMIN") {
        console.log("수정 가능!!");
        this.user.isAdmin = true;
      } else {
        console.log("수정 불가!");
        this.user.isAdmin = false;
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
      this.$router.push({ name: "LectureDetail", params: { id: lectureId } });
    },
  },
  computed: {
    isAdmin() {
      return this.user.isAdmin;
    },
  },
};
</script>

<style scoped>
.container {
  padding-top: 20px;
}

.btnWrap {
  text-align: right;
  margin-bottom: 20px;
}

.btn_write {
  background-color:   #f27885;
  color: #fff;
  padding: 10px 20px;
  text-decoration: none;
  border-radius: 5px;
  font-size: 16px;
  display: inline-block;
  transition: background-color 0.3s ease;
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
  border-left: none;
  border-right: none;
}

.tbl_list th {
  background-color: #f4f4f4;
  font-weight: bold;
}

.text_left {
  text-align: left;
}

.text_center {
  text-align: center;
}
</style>
