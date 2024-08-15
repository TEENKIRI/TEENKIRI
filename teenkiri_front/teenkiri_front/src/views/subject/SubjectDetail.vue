<template>
  <h1>강좌별 상세페이지</h1>
  <p>강좌별 상세페이지인, 해당 강좌 내부의 강의 리스트 등이 보일 예정입니당</p>
</template>

<script>
export default {
  data() {
    return{
      subjectId:""
    }
  },
  created() {
    this.subjectId = this.$route.params.id;
    console.log(`id >>>> ${this.subjectId}`);
    
  }
};
</script>

<style>
</style>