<template>
  <v-app>
    <v-container>
      <SubjectDetailComponent v-model="selectedMenu" />

      <h2>QnA 부분입니다~!~!</h2>
      <!-- QnA 목록에 해당하는 내용 -->
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
      selectedMenu: 'SubjectQna'
    };
  },
  methods:{
    handleMenuUpdate(newSelection) {
      this.selectedMenu = newSelection;
      console.log(" 메뉴 바뀜 >> ",newSelection)
      // 선택된 메뉴에 따라 다른 동작을 처리할 수 있습니다.
      // 예를 들어, 서버로부터 데이터를 가져오거나 다른 로직을 실행합니다.
    }
  }
};
</script>