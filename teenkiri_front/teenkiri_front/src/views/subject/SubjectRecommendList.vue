<template>
  <v-container>
    <v-row class="align-end">
      <v-col class="font_notrahope" style="font-size:2.5rem;">
        티니키리 서비스가 <span class="teen_red_font">추천</span>해요! 👍
      </v-col>
      <v-col class="text-right">
        <v-btn v-if="this.user.role===`ADMIN`">강좌 업로드</v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-card class="w-100 py-5 px-5 mb-5">
        <v-card-text class="lectureList">
          <div
            class="item"
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
            <div class="text-right">
              <button type="button" 
                class="btn_like" 
                @click="toggleWish(s.id, $event)" 
                :class="{ 'mdi mdi-heart': s.isSubscribe, 'mdi mdi-heart-outline': !s.isSubscribe }">
              </button>
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
      user:{},
      
      subject:{
        subjectIsMainList:[],
        page:{
          pageSize: 5,
          currentPage:0,
        }
      },
    }
  },
  async created(){
    try {
      await this.$store.dispatch("setUserAllInfoActions");
      this.user = this.$store.getters.getUserObj;
    } catch (error) {
      console.error("사용자 정보를 가져오는 중 오류가 발생했습니다:", error);
    }
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
    async toggleWish(id, event){
      event.stopPropagation();  // 이벤트 전파 방지
      console.log("클릭!!", id);
      if(Object.keys(this.user).length > 0){ // 로그인 한 유저
        try {
          const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/wish/toggle/${id}`);
          const subject = this.subject.subjectIsMainList.find(sm => sm.id === id);
          if (subject) {
            subject.isSubscribe = response.data.result.status;
          }
        } catch (error) {
          alert("찜 추가 실패");
          console.error(error);
        }
      }else{
        alert("로그인 후 사용 가능합니다.")
      }
    },
  }
}
</script>
