<template>
  <v-sheet v-if="subjectData">
    <v-row>
      <v-col>
        <h1>{{ subjectData.title }}</h1>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <img v-bind:src="subjectData.subjectThumUrl" />
      </v-col>
      <v-col>
        <v-row>
          <v-col>
            <!-- 수강중인 경우 버튼 비활성화 -->
            <v-btn :disabled="this.subjectData.isRegistered" @click="applyForSubject">
              {{ this.subjectData.isRegistered ? "이미 수강중입니다" : "수강신청" }}
            </v-btn>
            <v-btn @click="handleWishlist">
              {{ this.subjectData.isSubscribe ? "찜 취소하기" : "찜하기" }}
            </v-btn>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-list lines="two">
              <v-list-item
                title="선생님"
                :subtitle="subjectData.userTeacherName"
              ></v-list-item>
              <v-list-item title="수강대상" :subtitle="subjectData.grade"></v-list-item>
              <v-list-item
                title="강좌구성"
                :subtitle="subjectData.userTeacherName"
              ></v-list-item>
              <v-list-item
                title="평점"
                :subtitle="subjectData.rating + ' 점'"
              ></v-list-item>
              <v-list-item title="설명" :subtitle="subjectData.description"></v-list-item>
            </v-list>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-btn-toggle v-model="internalValue" color="primary" rounded="0" group>
          <v-btn
            class="flex-grow-1"
            v-for="item in menuItems"
            :key="item.value"
            :value="item.value"
            @click="goToPage(item.value)"
          >
            {{ item.title }}
          </v-btn>
        </v-btn-toggle>
      </v-col>
    </v-row>
  </v-sheet>
</template>

<script>
import axios from "axios";

export default {
  props: {
    modelValue: {
      type: String,
      required: true,
    },
  },
  inject: ['getSubjectData'],
  data() {
    return {
      internalValue: this.modelValue,
      subjectId: "",
      subjectData: null,
    };
  },
  created() {
    this.subjectId = this.$route.params.id;
    this.getSubjectDetail();
  },
  mounted() {
  },
  methods: {
    async getSubjectDetail() {
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/subject/detail/${this.subjectId}`
        );
        this.subjectData = response.data.result;
        console.log(this.subjectData);

        // 데이터가 로드된 후 상위 컴포넌트에 알림
        this.$emit('subject-data-loaded', this.subjectData);

      } catch (error) {
        console.error("강좌 세부 정보 조회 실패:", error);
      }
    },
    async checkWishlistStatus() {
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/wish/check/${this.subjectId}`
        );
        this.subjectData.isSubscribe = response.data.isInWishlist;
      } catch (error) {
        console.error("위시리스트 상태 확인 실패:", error);
      }
    },
    async checkEnrollmentStatus() {
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/my/subject/check-enrollment/${this.subjectId}`
        );
        this.subjectData.isRegistered = response.data.isEnrolled; // 사용자가 수강중인지 확인
      } catch (error) {
        console.error("수강 상태 확인 실패:", error);
      }
    },
    async handleWishlist() {
      if (this.subjectData.isSubscribe) {
        await this.removeFromWishlist();
      } else {
        await this.addToWishlist();
      }
      // this.isInWishlist = !this.isInWishlist;
    },
    async addToWishlist() {
      try {
        await axios.post(`${process.env.VUE_APP_API_BASE_URL}/wish/${this.subjectId}`);
        alert("찜 추가 성공");
        this.subjectData.isSubscribe = true;
      } catch (error) {
        alert("찜 추가 실패");
        console.error(error);
      }
    },
    async removeFromWishlist() {
      try {
        await axios.delete(`${process.env.VUE_APP_API_BASE_URL}/wish/${this.subjectId}`);
        alert("찜 취소 성공");
        this.subjectData.isSubscribe = false;
      } catch (error) {
        alert("찜 취소 실패");
        console.error(error);
      }
    },
    async applyForSubject() {
      if (this.subjectData.isRegistered) {
        alert("이미 수강중인 강좌입니다.");
        return;
      }
      try {
        await axios.post(`${process.env.VUE_APP_API_BASE_URL}/my/subject/create`, {
          subjectId: this.subjectId,
        });
        alert("강좌 수강 신청이 완료되었습니다.");
        this.subjectData.isRegistered = true; // 신청 후 수강 상태 업데이트
      } catch (error) {
        alert("강좌 수강 신청 실패");
        console.error(error);
      }
    },
    goToPage(pathName) {
      this.$router.push({ name: pathName, params: { id: this.subjectId } });
    },
  },
  computed: {
    menuItems() {
      return [
        { title: "강의목록", value: "SubjectDetail" },
        { title: "Q&A 게시판", value: "SubjectQna" },
        { title: "강의 후기", value: "SubjectReview" },
      ];
    },
  },
};
</script>

<style></style>
