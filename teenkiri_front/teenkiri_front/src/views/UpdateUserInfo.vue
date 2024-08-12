<template>
    <v-container>
      <v-row justify="center">
        <v-col cols="12" sm="8" md="6">
          <v-card>
            <v-card-title class="text-h5 text-center">회원 정보 수정</v-card-title>
            <v-card-text>
              <v-form @submit.prevent="updateUserInfo">
                <v-text-field
                  label="닉네임"
                  v-model="userEditInfo.nickname"
                  prepend-icon="mdi-account"
                  required
                ></v-text-field>
                <v-text-field
                  label="이름"
                  v-model="userEditInfo.name"
                  prepend-icon="mdi-account-box"
                  required
                ></v-text-field>
                <v-text-field
                  label="이메일"
                  v-model="userEditInfo.email"
                  prepend-icon="mdi-email"
                  disabled
                ></v-text-field>
                <v-text-field
                  label="핸드폰 번호"
                  v-model="userEditInfo.phone"
                  prepend-icon="mdi-phone"
                  required
                ></v-text-field>
  
                <!-- 주소 필드 변경 -->
                <v-text-field
                  label="주소"
                  v-model="userEditInfo.address.city"
                  prepend-icon="mdi-home"
                  required
                ></v-text-field>
                <v-text-field
                  label="상세주소"
                  v-model="userEditInfo.address.street"
                  prepend-icon="mdi-home-outline"
                  required
                ></v-text-field>
                <v-text-field
                  label="우편번호"
                  v-model="userEditInfo.address.zipcode"
                  prepend-icon="mdi-home-outline"
                  required
                ></v-text-field>
  
                <v-divider class="my-4"></v-divider>
                <v-text-field
                  label="새 비밀번호"
                  v-model="userEditReq.password"
                  type="password"
                  prepend-icon="mdi-lock"
                ></v-text-field>
                <v-text-field
                  label="비밀번호 확인"
                  v-model="userEditReq.confirmPassword"
                  type="password"
                  prepend-icon="mdi-lock-check"
                ></v-text-field>
                <v-btn block type="submit" color="primary">회원 정보 수정</v-btn>
              </v-form>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    name: 'UserEditPage',
    data() {
      return {
        userEditInfo: {
          nickname: '',
          name: '',
          email: '',
          phone: '',
          address:{
            city: '',       
          street: '',     
          zipcode: ''  
          }
        },
        userEditReq: {
          password: '',
          confirmPassword: ''
        }
      };
    },
    created() {
      this.fetchUserInfo();
    },
    methods: {
      async fetchUserInfo() {
        try {
          const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/user/edit-info`);
          if (response.status === 200 && response.data.result) {
            this.userEditInfo = response.data.result;
          } else {
            alert('회원 정보 조회에 실패했습니다.');
          }
        } catch (e) {
          alert(e.response?.data?.status_message || '회원 정보 조회 중 오류가 발생했습니다.');
        }
      },
      async updateUserInfo() {
        if (this.userEditReq.password && this.userEditReq.password !== this.userEditReq.confirmPassword) {
          alert('비밀번호가 일치하지 않습니다.');
          return;
        }
        try {
          const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/user/edit-info`, {
            nickname: this.userEditInfo.nickname,
            password: this.userEditReq.password,
            confirmPassword: this.userEditReq.confirmPassword,
            address: {
              city: this.userEditInfo.city,
              street: this.userEditInfo.street,
              zipcode: this.userEditInfo.zipcode
            }
          });
          if (response.status === 200) {
            alert('회원 정보가 성공적으로 수정되었습니다.');
          } else {
            alert('회원 정보 수정에 실패했습니다.');
          }
        } catch (e) {
          alert(e.response?.data?.status_message || '회원 정보 수정 중 오류가 발생했습니다.');
        }
      }
    }
  };
  </script>
  
  <style scoped>
  /* 필요에 따라 스타일 조정 */
  </style>
  