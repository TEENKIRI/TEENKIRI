<template>
    <v-container>
      <v-card class="mx-auto" max-width="400">
        <v-card-title>
          <span class="headline">회원 탈퇴</span>
        </v-card-title>
        <v-card-text>
          <v-form ref="form" v-model="valid" lazy-validation>
            <v-text-field
              v-model="confirmationText"
              label="회원 탈퇴 문구를 입력하세요"
              :rules="confirmationRules"
              required
            ></v-text-field>
            <small>회원 탈퇴 문구: "틴끼리 사이트 회원 탈퇴에 동의합니다"</small>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red" @click="deleteAccount" :disabled="!valid">탈퇴</v-btn>
        </v-card-actions>
      </v-card>
    </v-container>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        confirmationText: '',
        valid: false,
        confirmationRules: [
          v => v === '틴끼리 사이트 회원 탈퇴에 동의합니다' || '회원 탈퇴 문구가 올바르지 않습니다.',
        ],
      };
    },
    methods: {
  async deleteAccount() {
    try {
      const response = await axios.post(
        `${process.env.VUE_APP_API_BASE_URL}/user/delete-account`,
        { confirmation: this.confirmationText },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
          },
        }
      );

      if (response.status === 200) {
        alert('회원 탈퇴가 완료되었습니다.');

        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('userId');
        localStorage.removeItem('email');
        localStorage.removeItem('role');

        this.$router.push('/login');
      } else {
        alert('회원 탈퇴에 실패했습니다. 다시 시도해주세요.');
      }
    } catch (error) {
      if (error.response && error.response.data && error.response.data.message) {
        alert(`오류: ${error.response.data.message}`);
      } else {
        alert('회원 탈퇴 중 오류가 발생했습니다.');
      }
      console.error(error);
    }
  },
},
  }

  </script>
  
  <style scoped>
  .mx-auto {
    margin-top: 20px;
  }
  </style>
  