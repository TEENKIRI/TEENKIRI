<template>
  <v-container>
    <v-row>
      <v-col
        v-for="item in wishList"
        :key="item.id"
        cols="12" sm="6" md="4">
        <v-card class="ma-4" :elevation="4" @click="viewDetails(item.id)" style="cursor: pointer;">
          <v-img :src="item.subjectThumUrl" height="200px" contain></v-img>
          <v-card-title>
            {{ item.title }}
          </v-card-title>
          <v-card-subtitle>
            강사: {{ item.teacherName }}
          </v-card-subtitle>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  name: "WishList",
  data() {
    return {
      wishList: [] // 위시리스트를 저장할 데이터
    };
  },
  async mounted() {
    await this.fetchWishList();
  },
  methods: {
    async fetchWishList() {
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/user/wishlist`);
        if (response.status === 200) {
          this.wishList = response.data.result;
        } else {
          console.error('위시리스트 가져오기 실패:', response.data.message || response.statusText);
        }
      } catch (error) {
        if (error.response) {
          console.error('서버 오류:', error.response.data.message || error.response.statusText);
        } else if (error.request) {
          console.error('요청 오류:', error.request);
        } else {
          console.error('오류 발생:', error.message);
        }
      }
    },
    viewDetails(id) {
      this.$router.push(`/subject/detail/${id}`);
    }
  }
};
</script>

<style scoped>
.v-card {
  max-width: 400px;
  margin: auto;
}
.v-card-title {
  font-weight: bold;
}
.v-card-subtitle {
  color: #757575;
}
.v-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
</style>
ㅞㅡ