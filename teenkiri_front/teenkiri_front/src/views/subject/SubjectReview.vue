<template>
  <v-app>
    <v-container>
      <SubjectDetailComponent v-model="selectedMenu" />

      <h2>REview 부분입니다~!~!</h2>
      <!-- REview 목록에 해당하는 내용 -->
    </v-container>
  </v-app>
</template>
  
  <script>
import SubjectDetailComponent from '@/components/subject/SubjectDetailComponent.vue';

export default {
  components: {
    SubjectDetailComponent,
  },
  created() {
    this.subjectId = this.$route.params.id;
    console.log(`id >>>> ${this.subjectId}`);
    
  },
  data() {
    return {
      subjectId : "",
      selectedMenu: 'SubjectReview'
    };
  },
  methods:{
    
    handleMenuUpdate(newSelection) {
      this.selectedMenu = newSelection;
      console.log("메뉴 바뀜 >> ",newSelection)
    }
  }
};
</script>