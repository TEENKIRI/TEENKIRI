<template>
  <v-sheet>
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
            <v-btn>수강신청</v-btn>
            <v-btn @click="handleWishlist">
              {{ isInWishlist ? '찜 취소하기' : '찜하기' }}
            </v-btn>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-list lines="two">
              <v-list-item title="선생님" :subtitle="subjectData.userTeacherName"></v-list-item>
              <v-list-item title="수강대상" :subtitle="subjectData.grade"></v-list-item>
              <v-list-item title="강좌구성" :subtitle="subjectData.userTeacherName"></v-list-item>
              <v-list-item title="평점" :subtitle="subjectData.rating + ' 점'"></v-list-item>
              <v-list-item title="설명" :subtitle="subjectData.description"></v-list-item>
            </v-list>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-btn-toggle v-model="internalValue" color="primary" rounded="0" group>
          <v-btn class="flex-grow-1" v-for="item in menuItems" :key="item.value" :value="item.value" @click="goToPage(item.value)">
            {{ item.title }}
          </v-btn>
        </v-btn-toggle>
      </v-col>
    </v-row>
  </v-sheet>
</template>

<script>
import axios from 'axios';

export default {
  props: {
    modelValue: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      internalValue: this.modelValue,
      subjectId: "",
      subjectData: {},
      isInWishlist: false // 위시리스트 여부 상태
    };
  },
  created() {
    this.subjectId = this.$route.params.id;
    this.getSubjectDetail();
    this.checkWishlistStatus(); // 강좌가 위시리스트에 있는지 확인
  },
  methods: {
    async getSubjectDetail() {
      const response = await axios.get(
        `${process.env.VUE_APP_API_BASE_URL}/subject/detail/${this.subjectId}`
      );
      const addtionalData = response.data.result;
      this.subjectData = addtionalData;
    },
    async checkWishlistStatus() {
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/wish/check/${this.subjectId}`
        );
        this.isInWishlist = response.data.isInWishlist; // 응답 데이터에서 위시리스트 여부를 설정
      } catch (error) {
        console.error('위시리스트 상태 확인 실패:', error);
      }
    },
    async handleWishlist() {
      if (this.isInWishlist) {
        await this.removeFromWishlist();
      } else {
        await this.addToWishlist();
      }
      this.isInWishlist = !this.isInWishlist; // 상태 토글
    },
    async addToWishlist() {
      try {
        await axios.post(
          `${process.env.VUE_APP_API_BASE_URL}/wish/${this.subjectId}`
        );
        alert('찜 추가 성공');
      } catch (error) {
        alert('찜 추가 실패');
        console.error(error);
      }
    },
    async removeFromWishlist() {
      try {
        await axios.delete(
          `${process.env.VUE_APP_API_BASE_URL}/wish/${this.subjectId}`
        );
        alert('찜 취소 성공');
      } catch (error) {
        alert('찜 취소 실패');
        console.error(error);
      }
    },
    goToPage(pathName){
      console.log(pathName);
      this.$router.push({ name: pathName, params: {id : this.subjectId}});
    }
  },
  computed: {
    menuItems() {
      return [
        { title: "강의목록", value: "SubjectDetail" },
        { title: "Q&A 게시판", value: "SubjectQna" },
        { title: "강의 후기", value: "SubjectReview" }
      ];
    }
  }
};
</script>

<style>
</style>
