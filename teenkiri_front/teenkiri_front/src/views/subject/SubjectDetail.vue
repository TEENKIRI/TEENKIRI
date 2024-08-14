<template>
  <v-app>
    <v-container>
      <SubjectDetailComponent v-model="selectedMenu" />
      
      <v-row>
        <v-col>
          <v-btn>강의 업로드하기</v-btn>
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
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item) in lecture.lectureList" :key="item.id">
                <td>{{ item.id }}</td>
                <td>{{ item.title }}</td>
                <td>{{ item.progress }}</td>
                <td>{{ item.videoDuration }}</td>
                <td>
                  <v-btn @click="goToDetail(item.id)">강의보기</v-btn>
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
import SubjectDetailComponent from '@/components/subject/SubjectDetailComponent.vue';
import axios from 'axios';

export default {
  components:{
      SubjectDetailComponent
  },
  data() {
    return{
      subjectId:"",
      selectedMenu: 'SubjectDetail',  // 기본으로 선택된 메뉴
      lecture:{
        lectureList:[],
        page:{
          pageSize: 5,
          currentPage:0,
        }
      },
    }
  },
  created() {
    this.subjectId = this.$route.params.id;
    console.log(`id >>>> ${this.subjectId}`);

    this.getSubjectPerLectureList();
  },
  methods:{
    async getSubjectPerLectureList(){
      const params = {
          size: this.lecture.page.pageSize,
          page: this.lecture.page.currentPage
      }
      
      const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/subject/${this.subjectId}/lecture/list`, {params});
      const addtionalData = response.data.result.content;
      console.log(response)
      this.lecture.lectureList = [...this.lecture.lectureList, ...addtionalData]
    },
    handleMenuUpdate(newSelection) {
      this.selectedMenu = newSelection;
      console.log("메뉴 바뀜 >> ",newSelection)
    },
    goToDetail(lectureId){
      console.log("강의페이지로 이동~",lectureId)
    }
  }
};
</script>

<style>
</style>