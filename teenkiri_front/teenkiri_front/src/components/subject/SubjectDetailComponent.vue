<template>
  <v-sheet v-if="subjectData">
    <v-row class="mb-5">
      <v-col>
        <h1>{{ subjectData.title }}</h1>
      </v-col>
      <v-col class="d-flex justify-end flex-end">
        <v-btn v-if="this.user.role == `ADMIN`" @click="editSubject()">강좌 수정</v-btn>
        <v-btn
          :disabled="this.subjectData.isRegistered"
          @click="applyForSubject"
          class="teen_red_bg_c_white"
        >
          {{ this.subjectData.isRegistered ? "수강 중" : "수강신청" }}
        </v-btn>
        <v-btn @click="handleWishlist" color="secondary">
          {{ this.subjectData.isSubscribe ? "찜 취소하기" : "찜하기" }}
        </v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12" md="6">
        <v-img
          :src="subjectData.subjectThumUrl"
          max-width="100%"
          max-height="400px"
          class="mx-auto my-4 mt-8"
          aspect-ratio="16/9"
          contain
          elevation="4"
          rounded="lg"
          
        ></v-img>
      </v-col>
      <v-col cols="12" md="6">
        <v-row>
          
        </v-row>
        <v-row>
          <v-col>
            <v-list dense lines="two">
              <v-list-item>
                <v-list-item-title class="custom-title">선생님</v-list-item-title>
                <v-list-item-subtitle class="custom-subtitle">{{ subjectData.userTeacherName }}</v-list-item-subtitle>
              </v-list-item>
              <v-list-item>
                <v-list-item-title class="custom-title">수강대상</v-list-item-title>
                <v-list-item-subtitle class="custom-subtitle">{{ changeGradeWord(subjectData.grade) }}</v-list-item-subtitle>
              </v-list-item>
              <v-list-item>
                <v-list-item-title class="custom-title">강좌구성</v-list-item-title>
                <v-list-item-subtitle class="custom-subtitle">{{ subjectData.userTeacherName }}</v-list-item-subtitle>
              </v-list-item>
              <v-list-item>
                <v-list-item-title class="custom-title">평점</v-list-item-title>
                <v-list-item-subtitle class="custom-subtitle">{{ subjectData.rating + ' 점' }}</v-list-item-subtitle>
              </v-list-item>
              <v-list-item>
                <v-list-item-title class="custom-title">설명</v-list-item-title>
                <v-list-item-subtitle class="custom-subtitle">{{ subjectData.description }}</v-list-item-subtitle>
              </v-list-item>
            </v-list>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-btn-toggle
          v-model="internalValue"
          selected-class="teen_mint_bg_c_white"
          rounded="0"
          group
        >
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
  inject: ["getSubjectData"],
  data() {
    return {
      user: {},
      internalValue: this.modelValue,
      subjectId: "",
      subjectData: null,
    };
  },
  async created() {
    try {
      await this.$store.dispatch("setUserAllInfoActions");
      this.user = this.$store.getters.getUserObj;
    } catch (error) {
      console.error("사용자 정보를 가져오는 중 오류가 발생했습니다:", error);
    }
    this.subjectId = this.$route.params.id;
    this.getSubjectDetail();
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
        this.$emit("subject-data-loaded", this.subjectData);
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
    },
    async addToWishlist() {
      try {
        await axios.post(
          `${process.env.VUE_APP_API_BASE_URL}/wish/${this.subjectId}`
        );
        alert("찜 추가 성공");
        this.subjectData.isSubscribe = true;
      } catch (error) {
        alert("찜 추가 실패");
        console.error(error);
      }
    },
    async removeFromWishlist() {
      try {
        await axios.delete(
          `${process.env.VUE_APP_API_BASE_URL}/wish/${this.subjectId}`
        );
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
        await axios.post(
          `${process.env.VUE_APP_API_BASE_URL}/my/subject/create`,
          {
            subjectId: this.subjectId,
          }
        );
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
    editSubject(){
      this.$router.push({ name: "SubjectEdit", params: { id: this.subjectId } });
    },
    changeGradeWord(grade) {
      switch (grade) {
          case 'GRADE_1':
              return '1학년';
          case 'GRADE_2':
              return '2학년';
          case 'GRADE_3':
              return '3학년';
          case 'GRADE_4':
              return '4학년';
          case 'GRADE_5':
              return '5학년';
          case 'GRADE_6':
              return '6학년';
          default:
              return '알 수 없음';  // 예외 처리
      }
    }
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

<style>
.v-img {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.v-btn {
  min-width: 150px;
}

.custom-title {
  font-size: 1.25rem !important; /* 약 20px */
  font-weight: bold;
}

.custom-subtitle {
  font-size: 1rem !important; /* 약 16px */
}

</style>
