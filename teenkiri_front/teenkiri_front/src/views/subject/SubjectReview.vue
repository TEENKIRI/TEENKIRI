<template>
  <v-app>
    <v-container>
      <SubjectDetailComponent v-model="selectedMenu" />

      <h2>강의 후기</h2>
      <v-row>
        <v-col>
          <v-btn color="primary" @click="openReviewForm">후기 작성</v-btn>
        </v-col>
      </v-row>

      <!-- 리뷰 목록 -->
      <v-row v-if="reviews && reviews.length">
        <v-col v-for="review in reviews" :key="review.id" cols="12" md="6">
          <v-card>
            <v-card-title>
              <div class="review-info">
                <span>작성자: {{ review.nickname }}</span>
                <span class="rating-text">별점: {{ review.rating }} / 5</span>
              </div>
            </v-card-title>
            <v-card-text>{{ review.reviewText }}</v-card-text>
            <v-card-subtitle>
              작성일: {{ formatDate(review.createdTime) }}
            </v-card-subtitle>
          </v-card>
        </v-col>
      </v-row>
      <v-row v-else>
        <v-col>
          <p>작성된 후기가 없습니다.</p>
        </v-col>
      </v-row>

      <v-dialog v-model="dialog" max-width="500px">
        <v-card>
          <v-card-title>
            <span class="headline">후기 작성</span>
          </v-card-title>
          <v-card-text>
            <v-form ref="reviewForm" v-model="valid">
              <v-rating v-model="newReview.rating" label="평점" required></v-rating>
              <v-textarea
                v-model="newReview.reviewText"
                label="후기"
                rows="5"
                required
              ></v-textarea>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="closeReviewForm">취소</v-btn>
            <v-btn color="blue darken-1" text @click="submitReview">작성</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-snackbar v-model="snackbar" :timeout="3000">
        {{ snackbarMessage }}
        <v-btn color="red" text @click="snackbar = false">Close</v-btn>
      </v-snackbar>
    </v-container>
  </v-app>
</template>

<script>
import SubjectDetailComponent from '@/components/subject/SubjectDetailComponent.vue';

export default {
  components: {
    SubjectDetailComponent,
  },
  data() {
    return {
      user: {
        token: '',
        id: '',
        email: '',
        nickname: '',
      },
      subjectId: this.$route.params.id, 
      selectedMenu: 'SubjectReview',
      reviews: [],
      dialog: false,
      valid: false, 
      newReview: {
        rating: 0,
        reviewText: '',
      },
      snackbar: false, 
      snackbarMessage: '',
    };
  },
  async created() {
    try {
      await this.$store.dispatch('setUserAllInfoActions');
      this.user = this.$store.getters.getUserObj;

      if (this.user.token === '') {
        alert('로그인이 필요합니다.');
        location.href = -1;
      } else {
        this.fetchReviews();
      }
    } catch (error) {
      console.error('사용자 정보를 가져오는 중 오류가 발생했습니다:', error);
    }
  },
  methods: {
    fetchReviews() {
      fetch(`${process.env.VUE_APP_API_BASE_URL}/reviews/list?subject_id=${this.subjectId}`)
        .then(response => response.json())
        .then(data => {
          if (data && data.status_code === 200 && data.result && Array.isArray(data.result.content)) {
            this.reviews = data.result.content;
          } else {
            console.warn('리뷰 데이터를 찾을 수 없습니다.');
            this.reviews = [];
          }
        })
        .catch(error => {
          console.error('리뷰 목록을 불러올 수 없습니다:', error);
          this.reviews = [];
        });
    },
    openReviewForm() {
      this.dialog = true;
    },
    closeReviewForm() {
      this.dialog = false;
    },
    submitReview() {
      if (this.$refs.reviewForm.validate()) {
        const reviewPayload = {
          rating: this.newReview.rating,
          reviewText: this.newReview.reviewText,
          subjectId: this.subjectId,
          userId: this.user.id, 
        };

        fetch(`${process.env.VUE_APP_API_BASE_URL}/reviews/create`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${this.user.token}`,
          },
          body: JSON.stringify(reviewPayload),
        })
          .then(response => {
            if (response.status === 409) {
              throw new Error('이미 이 과목에 대한 리뷰를 작성하셨습니다.');
            }
            return response.json();
          })
          .then(data => {
            if (data.status === 'CREATED') {
              this.reviews.unshift({
                ...reviewPayload,
                id: data.data,
                createdTime: new Date(),
              });
              this.snackbarMessage = '리뷰가 성공적으로 등록되었습니다.';
              this.snackbar = true; 
              this.closeReviewForm();
            }
          })
          .catch(error => {
            console.error('리뷰 제출 중 오류가 발생했습니다.', error);
            alert(error.message); 
          });
      }
    },
    formatDate(date) {
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(date).toLocaleDateString(undefined, options);
    }},
}

</script>

<style scoped>
.review-info {
  display: flex;
  justify-content: space-between;
  font-size: 1.2rem;
  font-weight: bold;
}

.rating-text {
  font-weight: bold;
}
</style>
