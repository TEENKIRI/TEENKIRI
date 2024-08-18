<template>
  <v-app>
    <v-container>
      <v-row class="wishlist-header">
        <v-col>
          <h1>내 위시리스트</h1>
        </v-col>
      </v-row>
      <v-row v-if="wishlist.length > 0">
        <v-col
          v-for="item in wishlist"
          :key="item.id"
          cols="12" sm="6" md="4"
        >
          <v-card 
            class="ma-4" 
            :elevation="4" 
            @click="goToSubjectDetail(item.id)" 
            style="cursor: pointer;"
          >
            <v-card-title>
              강좌 명 : {{ item.title || '제목 없음' }}
            </v-card-title>
            <v-card-subtitle>
              선생님: {{ item.teacherName || '선생님 정보 없음' }}
            </v-card-subtitle>
            <v-card-actions>
              <v-btn @click.stop="removeFromWishlist(item.id)" color="red">제거</v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
      <v-row v-else>
        <v-col>
          <v-alert type="info" :value="true">
            위시리스트에 항목이 없습니다.
          </v-alert>
        </v-col>
      </v-row>
      <v-row v-if="loading" justify="center">
        <v-progress-circular indeterminate color="primary"></v-progress-circular>
      </v-row>
      <v-row v-if="error" justify="center">
        <v-alert type="error" :value="true">
          {{ error }}
        </v-alert>
      </v-row>
    </v-container>
  </v-app>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      wishlist: [],
      loading: false,
      error: null
    };
  },
  async created() {
    await this.fetchWishlist();
  },
  methods: {
    async fetchWishlist() {
      this.loading = true;
      this.error = null;
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/user/wishlist`);
        console.log(response.data);

        if (response.status === 200 && response.data) {
          this.wishlist = response.data.result || [];
        } else {
          console.error('응답 데이터가 없습니다.');
        }
      } catch (error) {
        console.error('위시리스트 불러오기 오류:', error.response ? error.response.data.message : error.message);
        this.error = error.response ? error.response.data.message : error.message;
      } finally {
        this.loading = false;
      }
    },
    async removeFromWishlist(id) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.delete(`${process.env.VUE_APP_API_BASE_URL}/wish/${id}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (response.status === 200) {
          this.fetchWishlist(); // 삭제 후 위시리스트를 다시 불러옴
        } else {
          console.error('찜 삭제 실패:', response.data.message);
        }
      } catch (error) {
        console.error('찜 삭제 중 오류 발생:', error.response ? error.response.data.message : error.message);
      }
    },
    goToSubjectDetail(id) {
      this.$router.push({ name: 'SubjectDetail', params: { id } });
    }
  }
};
</script>

<style scoped>
.wishlist-header {
  margin-bottom: 20px;
}

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

.v-btn {
  margin-top: 10px;
}
</style>
