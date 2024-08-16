<template>
  <v-container>
    <v-card>
      <v-card-title> 강좌 이름이 들어간다 </v-card-title>
      <v-card-text> </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      user:{
        token:"",
        id:"",
        email:""
      },

      lectureId: "",
      lectureData: {},
    };
  },
  async created() {
    try {
      await this.$store.dispatch('setUserAllInfoActions');
      this.user = this.$store.getters.getUserObj;

      if(this.user.token == ""){
        alert('로그인이 필요합니다.');
        location.href = -1;
      }

      this.lectureId = this.$route.params.id;
      this.getLectureDetail();
    } catch (error) {
      console.error("An error occurred while fetching user info:", error);
    }
  },
  methods:{
    async getLectureDetail(){
      console.log("lecture detail 실행!",this.lectureId)
      try{
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/lecture/detail/${this.lectureId}`
        );
        const addtionalData = response.data.result;
        console.log(response);
        this.lectureData = addtionalData;
      }catch(e){
        console.log(e)
      }
      
    }
  }
};
</script>
