<template>
  <v-container>
      <v-row justify="center">
          <v-col cols="12" sm="6" md="8">
              <v-card>
                  <v-card-title class="text-h5 text-center">로그인</v-card-title>
                  <v-card-text>
                      <v-form @submit.prevent="doLogin">
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
                          <v-checkbox
                              v-model="rememberEmail"
                              label="ID 저장"
                          ></v-checkbox>
                          <v-checkbox
                              v-model="autoLogin"
                              label="자동로그인"
                          ></v-checkbox>
                          <v-btn block type="submit" color="primary">로그인</v-btn>
                          <v-divider class="my-4"></v-divider>
                          <v-btn block color="warning" @click="kakaoLogin">카카오 로그인</v-btn>
                          <v-divider class="my-4"></v-divider>
                          <v-list>
                              <v-list-item @click="findId">
                                  <v-list-item-title>아이디 찾기</v-list-item-title>
                              </v-list-item>
                              <v-list-item @click="findPassword">
                                  <v-list-item-title>비밀번호 찾기</v-list-item-title>
                              </v-list-item>
                              <v-list-item @click="signUp">
                                  <v-list-item-title>회원가입</v-list-item-title>
                              </v-list-item>
                          </v-list>
                      </v-form>
                  </v-card-text>
              </v-card>
          </v-col>
      </v-row>
  </v-container>
</template>

<script>
import axios from 'axios';
import {jwtDecode} from 'jwt-decode';

export default {
  name: "LoginPage",
  data() {
      return {
          email: "",
          password: "",
          rememberEmail: false,
          autoLogin: false,
      };
  },
  methods: {
      async doLogin() {
          try {
              const loginData = {
                  email: this.email,
                  password: this.password,
                  rememberEmail: this.rememberEmail,
                  autoLogin: this.autoLogin,
              };
              const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/user/login`, loginData);
              console.log('로그인 성공');
              const token = response.data.result;
              const decodedToken = jwtDecode(token);
              const role = decodedToken.role;
              const userId = decodedToken.userId;

              localStorage.setItem('token', token);
              localStorage.setItem('role', role);
              localStorage.setItem('userId', userId);  // user.id 저장

              window.location.href = "/";
          } catch (e) {
              alert('로그인 실패');
              console.error(e);
              const error_message = e.response?.data?.status_message || "로그인에 실패했습니다.";
              alert(error_message);
          }
      },
      kakaoLogin() {
          console.log("카카오 로그인");
      },
      findId() {
          this.$router.push("/find-id");
      },
      findPassword() {
          this.$router.push("/find-password");
      },
      signUp() {
          this.$router.push("/register");
      },
  },
};
</script>
