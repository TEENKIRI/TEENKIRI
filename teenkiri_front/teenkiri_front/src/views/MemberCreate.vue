<template>
    <v-container>
      <v-row justify="center">
        <v-col cols="12" sm="6" md="8">
          <v-card>
            <v-card-title class="text-h5 text-center">회원가입</v-card-title>
            <v-card-text>
              <v-form @submit.prevent="register">
                <v-text-field
                  label="아이디(이메일)"
                  v-model="email"
                  type="email"
                  prepend-icon="mdi-email"
                  required
                ></v-text-field>
                <v-text-field
                  label="비밀번호"
                  v-model="password"
                  type="password"
                  prepend-icon="mdi-lock"
                  required
                ></v-text-field>
                <v-text-field
                  label="비밀번호 확인"
                  v-model="passwordConfirm"
                  type="password"
                  prepend-icon="mdi-lock"
                  required
                ></v-text-field>
                <v-text-field
                  label="이름"
                  v-model="name"
                  prepend-icon="mdi-account"
                  required
                ></v-text-field>
                <v-text-field
                  label="닉네임"
                  v-model="nickname"
                  prepend-icon="mdi-tag"
                ></v-text-field>
                <v-text-field
                  label="핸드폰번호"
                  v-model="phoneNumber"
                  prepend-icon="mdi-phone"
                  required
                ></v-text-field>
                <v-text-field
                  label="생년월일"
                  v-model="birthDate"
                  prepend-icon="mdi-cake"
                  required
                ></v-text-field>
                <v-text-field
                  label="주소"
                  v-model="address"
                  prepend-icon="mdi-home"
                ></v-text-field>
                <v-text-field
                  label="상세주소"
                  v-model="detailAddress"
                  prepend-icon="mdi-home-outline"
                ></v-text-field>
                <v-checkbox
                  v-model="agree1"
                  label="개인정보 수집 활용 동의"
                  required
                ></v-checkbox>
                <v-checkbox
                  v-model="agree2"
                  label="이용약관 동의"
                  required
                ></v-checkbox>
                <v-btn block type="submit" color="primary">회원가입</v-btn>
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
    name: 'RegisterPage',
    data() {
      return {
        email: "",
        password: "",
        passwordConfirm: "",
        name: "",
        nickname: "",
        phoneNumber: "",
        birthDate: "",
        address: "",
        detailAddress: "",
        agree1: false,
        agree2: false,
      };
    },
    methods: {
      async register() {
        if (this.password !== this.passwordConfirm) {
          alert("비밀번호가 일치하지 않습니다.");
          return;
        }
  
        try {
          const registerData = {
            email: this.email,
            password: this.password,
            name: this.name,
            nickname: this.nickname,
            phoneNumber: this.phoneNumber,
            birthDate: this.birthDate,
            address: this.address,
            detailAddress: this.detailAddress,
            agree1: this.agree1,
            agree2: this.agree2,
          };
          await axios.post(`${process.env.VUE_APP_API_BASE_URL}/user/register`, registerData);
          alert("회원가입이 완료되었습니다.");
          this.$router.push("/login");
        } catch (e) {
          const error_message = e.response.data.error_message;
          console.log(error_message);
          alert(error_message);
        }
      }
    }
  };
  </script>

  