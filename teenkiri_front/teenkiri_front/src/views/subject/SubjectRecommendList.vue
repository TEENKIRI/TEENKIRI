<template>
  <v-container>
    <h1>티니키리 서비스가 추천하는 강좌 👍</h1>
    <v-row>
      <v-col>
        <v-row>
          <v-col class="d-flex flex-row">
            <v-btn>강좌 업로드</v-btn>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-card class="w-100">
        <v-card-title>강좌 목록</v-card-title>
        <v-card-text>
          <div class="swiper swiperLectureBest">
            <div
              class="swiper-slide"
              v-for="s in subject.subjectIsMainList"
              :key="s.id"
              @click="goToDetail(s.id)"
            >
              <div class="thumb">
                <a href="javascript:void(0)"
                  ><img v-bind:src="s.subjectThumUrl" alt="강좌 썸네일"
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
  data(){
    return{
      subject:{
        subjectIsMainList:[],
        page:{
          pageSize: 5,
          currentPage:0,
        }
      },
    }
  },
  created(){
    this.getSubjectMainList();
  },
  methods:{
    async getSubjectMainList(){
      try {
        const params = {
          size: this.subject.page.pageSize,
          page: this.subject.page.currentPage
        }
        
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/subject/main/list`, {params});
        const addtionalData = response.data.result.content;
        console.log(response)
        this.subject.subjectIsMainList = [...this.subject.subjectIsMainList, ...addtionalData]
      }catch (e) {
        console.error(e)
      }
    },
    goToDetail(id) {
      this.$router.push({ name: 'SubjectDetail', params: { id } });
    },
  }
}
</script>
