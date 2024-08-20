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

              <!-- 우편번호 찾기 버튼 및 주소 필드들 -->
              <v-text-field
                label="우편번호"
                v-model="zipcode"
                prepend-icon="mdi-home-outline"
                append-icon="mdi-magnify"
                @click:append="sample4_execDaumPostcode"
                readonly
              ></v-text-field>
              <v-text-field
                label="도로명주소"
                v-model="roadAddress"
                prepend-icon="mdi-home"
                readonly
              ></v-text-field>
              <v-text-field
                label="지번주소"
                v-model="jibunAddress"
                prepend-icon="mdi-map-marker"
                readonly
              ></v-text-field>
              <v-text-field
                label="상세주소"
                v-model="detailAddress"
                prepend-icon="mdi-home-outline"
              ></v-text-field>
              <v-text-field
                label="참고항목"
                v-model="extraAddress"
                prepend-icon="mdi-information-outline"
                readonly
              ></v-text-field>

              <!-- 예상 주소 안내 -->
              <span id="guide" style="color:#999; display:none;"></span>

              <!-- 약관 동의 체크박스 -->
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

              <!-- 회원가입 버튼 -->
              <v-btn block type="submit" color="primary" :disabled="!emailVerified || !nicknameChecked">회원가입</v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
/* global daum */
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
      zipcode: "",        // 추가된 우편번호 필드
      roadAddress: "",
      jibunAddress: "",
      detailAddress: "",
      extraAddress: "",
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
            city: this.roadAddress,
            street: this.detailAddress,
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
    },
    sample4_execDaumPostcode() {
      if (typeof daum === 'undefined') {
        alert('Daum API가 로드되지 않았습니다.');
        return;
      }

      new daum.Postcode({
        oncomplete: (data) => {
          var roadAddr = data.roadAddress; // 도로명 주소 변수
          var extraRoadAddr = ''; // 참고 항목 변수

          // 법정동명이 있을 경우 추가한다. (법정리는 제외)
          if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
            extraRoadAddr += data.bname;
          }
          // 건물명이 있고, 공동주택일 경우 추가한다.
          if (data.buildingName !== '' && data.apartment === 'Y') {
            extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
          }
          // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
          if (extraRoadAddr !== '') {
            extraRoadAddr = ' (' + extraRoadAddr + ')';
          }

          // 우편번호와 주소 정보를 해당 필드에 넣는다.
          this.zipcode = data.zonecode;
          this.roadAddress = roadAddr;
          this.jibunAddress = data.jibunAddress;

          // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
          this.extraAddress = roadAddr !== '' ? extraRoadAddr : '';

          var guideTextBox = document.getElementById("guide");
          if (guideTextBox) {
            if (data.autoRoadAddress) {
              var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
              guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
              guideTextBox.style.display = 'block';
            } else if (data.autoJibunAddress) {
              var expJibunAddr = data.autoJibunAddress;
              guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
              guideTextBox.style.display = 'block';
            } else {
              guideTextBox.innerHTML = '';
              guideTextBox.style.display = 'none';
            }
          }
        }
      }).open();
    }
  },
  mounted() {
    const script = document.createElement('script');
    script.src = "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
    script.onload = () => {
      console.log('Daum Postcode script loaded.');
    };
    script.onerror = () => {
      console.error('Daum Postcode script failed to load.');
    };
    document.head.appendChild(script);
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