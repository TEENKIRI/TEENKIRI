<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" sm="6" md="8">
        <v-card>
          <v-card-title class="text-h5 text-center">회원가입</v-card-title>
          <v-card-text>
            <p class="info"><span class="required">*</span>표시는 필수입력사항입니다.</p>
            <v-form @submit.prevent="register">
              <v-text-field
                label="아이디(이메일)"
                v-model="email"
                type="email"
                prepend-icon="mdi-email"
                required
              >
                <template v-slot:append>
                  <v-btn @click="sendVerificationCode" :disabled="verificationSent">인증 코드 전송</v-btn>
                </template>
              </v-text-field>
              <v-text-field
                v-if="verificationSent"
                label="이메일 인증 코드"
                v-model="verificationCode"
                type="text"
                prepend-icon="mdi-check-circle"
                required
              >
                <template v-slot:append>
                  <v-btn @click="verifyEmailCode">확인</v-btn>
                </template>
              </v-text-field>
              <v-text-field
                :type="showPassword ? 'text' : 'password'"
                label="비밀번호"
                v-model="password"
                prepend-icon="mdi-lock"
                required
              >
                <template v-slot:append>
                  <v-btn icon @click="showPassword = !showPassword">
                    <v-icon>{{ showPassword ? 'mdi-eye-off' : 'mdi-eye' }}</v-icon>
                  </v-btn>
                </template>
              </v-text-field>
              <v-text-field
                :type="showPasswordConfirm ? 'text' : 'password'"
                label="비밀번호 확인"
                v-model="passwordConfirm"
                prepend-icon="mdi-lock"
                required
              >
                <template v-slot:append>
                  <v-btn icon @click="showPasswordConfirm = !showPasswordConfirm">
                    <v-icon>{{ showPasswordConfirm ? 'mdi-eye-off' : 'mdi-eye' }}</v-icon>
                  </v-btn>
                </template>
              </v-text-field>
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
                required
              >
                <template v-slot:append>
                  <v-btn @click="checkNickname">중복 확인</v-btn>
                </template>
              </v-text-field>
              <v-text-field
                label="핸드폰번호"
                v-model="phone"
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
                v-model="city"
                prepend-icon="mdi-home"
              ></v-text-field>
              <v-text-field
                label="상세주소"
                v-model="street"
                prepend-icon="mdi-home-outline"
              ></v-text-field>
              <v-text-field
                label="우편번호"
                v-model="zipcode"
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
              <v-btn block type="submit" color="primary" :disabled="!emailVerified || !nicknameChecked">회원가입</v-btn>
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
      verificationCode: "",
      password: "",
      passwordConfirm: "",
      name: "",
      nickname: "",
      phone: "",
      birthDate: "",
      city: "",
      street: "",
      zipcode: "",        // 추가된 우편번호 필드
      agree1: false,
      agree2: false,
      showPassword: false,
      showPasswordConfirm: false,
      verificationSent: false,
      emailVerified: false,
      nicknameChecked: false,
    };
  },
  methods: {
    async sendVerificationCode() {
      try {
        await axios.post(`${process.env.VUE_APP_API_BASE_URL}/user/send-verification-code`, { email: this.email });
        this.verificationSent = true;
        alert("인증 코드가 전송되었습니다.");
      } catch (e) {
        const error_message = e.response.data.error_message;
        console.log(error_message);
        alert(error_message);
      }
    },
    async verifyEmailCode() {
      try {
        await axios.post(`${process.env.VUE_APP_API_BASE_URL}/user/verify-email`, { email: this.email, code: this.verificationCode });
        this.emailVerified = true;
        alert("이메일 인증이 완료되었습니다.");
      } catch (e) {
        const error_message = e.response.data.error_message;
        console.log(error_message);
        alert(error_message);
      }
    },
    async checkNickname() {
      try {
        await axios.post(`${process.env.VUE_APP_API_BASE_URL}/user/check-nickname`, { nickname: this.nickname });
        this.nicknameChecked = true;
        alert("닉네임을 사용할 수 있습니다.");
      } catch (e) {
        const error_message = e.response.data.error_message;
        console.log(error_message);
        alert(error_message);
      }
    },
    async register() {
      if (!this.nicknameChecked) {
        alert("닉네임 중복 확인을 해주세요.");
        return;
      }
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
          phone: this.phone,
          birthDate: this.birthDate,
          address: {
            city: this.city,
            street: this.street,
            zipcode: this.zipcode
          },      
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

<style scoped>
/* 회원가입 페이지 관련 스타일 */
.info {
  font-size: 14px;
  margin-bottom: 20px;
}

.required {
  color: red;
}

.v-input__control {
  margin-bottom: 16px;
}

.btn_value_view {
  margin-left: -40px;
}

.btn_submit_primary {
  background-color: #1976D2;
  color: white;
  width: 100%;
}

.btn_submit_primary:hover {
  background-color: #1565C0;
}

.chkBoxIco {
  display: flex;
  align-items: center;
}

.chkBoxIco label {
  margin-left: 8px;
}

.mt20 {
  margin-top: 20px;
}
</style>
