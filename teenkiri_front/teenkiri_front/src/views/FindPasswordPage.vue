<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" sm="6" md="8">
        <v-card>
          <v-card-title class="text-h5 text-center">비밀번호 찾기</v-card-title>
          <v-card-text>
            <v-form @submit.prevent="sendResetLink">
              <v-text-field
                label="이름"
                v-model="name"
                type="text"
                prepend-icon="mdi-account"
                required
              ></v-text-field>
              <v-text-field
                label="이메일"
                v-model="email"
                type="email"
                prepend-icon="mdi-email"
                required
              ></v-text-field>
              <v-text-field
                label="핸드폰 번호"
                v-model="phone"
                type="text"
                prepend-icon="mdi-phone"
                required
              ></v-text-field>
              <v-btn block type="submit" color="primary">비밀번호 재설정 링크 보내기</v-btn>
              <v-divider class="my-4"></v-divider>
              <v-btn block @click="$router.push('/login')" color="secondary">로그인 페이지로 돌아가기</v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
    <v-alert v-if="errorMessage" type="error">{{ errorMessage }}</v-alert>
    <v-alert v-if="successMessage" type="success">{{ successMessage }}</v-alert>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  name: "PasswordResetPage",
  data() {
    return {
      name: "",
      email: "",
      phone: "",
      errorMessage: "",
      successMessage: ""
    };
  },
  methods: {
    async sendResetLink() {
      if (!this.name || !this.email || !this.phone) {
        alert('모든 필드를 입력해주세요.');
        return;
      }

      try {
        const response = await axios.post(
          `${process.env.VUE_APP_API_BASE_URL}/user/find-password`,
          {
            name: this.name,
            phone: this.phone,
            email: this.email
          }
        );

        this.successMessage = '비밀번호 재설정 링크가 이메일로 발송되었습니다.';
        this.errorMessage = "";
        console.log(response);
      } catch (e) {
        console.log(e.response);
        this.errorMessage = e.response?.data?.status_message || '알 수 없는 오류가 발생했습니다.';
        console.log(this.errorMessage);
        this.successMessage = "";
      }
    }
  }
};
</script>

<style scoped>
/* 스타일은 필요에 따라 조정하세요 */
</style>
