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
              <v-btn>찜하기</v-btn>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <v-list lines="two">
                <v-list-item
                  title="선생님"
                  :subtitle="subjectData.userTeacherName"
                ></v-list-item>
                <v-list-item
                  title="수강대상"
                  :subtitle="subjectData.grade"
                ></v-list-item>
                <v-list-item
                  title="강좌구성"
                  :subtitle="subjectData.userTeacherName"
                ></v-list-item>
                <v-list-item
                  title="평점"
                  :subtitle="subjectData.rating + ' 점'"
                ></v-list-item>
                <v-list-item
                  title="설명"
                  :subtitle="subjectData.description"
                ></v-list-item>
              </v-list>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-btn-toggle
          v-model="internalValue"
          color="primary"
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
import axios from 'axios';

export default {
  props: {
    modelValue: { // 각 페이지에서 주는 prop로 활성화되는 메뉴 선택
      type: String,
      required: true
    }
  },
  data() {
    return {
      internalValue: this.modelValue,
      subjectId: "",
      subjectData: {},
    };
  },
  watch: {
  },
  created() {
    this.subjectId = this.$route.params.id;
    console.log(`id >>>> ${this.subjectId}`);
    this.getSubjectDetail();
  },
  methods: {
    async getSubjectDetail() {
      const response = await axios.get(
        `${process.env.VUE_APP_API_BASE_URL}/subject/detail/${this.subjectId}`
      );
      const addtionalData = response.data.result;
      console.log(response);
      this.subjectData = addtionalData;
    },
    goToPage(pathName){
      console.log(pathName)
      this.$router.push({ name: pathName, params: {id : this.subjectId}});
    },
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