<template>
  <div class="register">
    <h2>회원가입</h2>
    <form @submit.prevent="register">
      <div>
        <label for="username">이름:</label>
        <input type="text" v-model="username" required />
      </div>
      <div>
        <label for="email">이메일:</label>
        <input type="email" v-model="email" required />
        <button type="button" @click="sendVerificationCode">이메일 인증</button>
      </div>
      <div>
        <label for="verificationCode">인증 코드:</label>
        <input type="text" v-model="verificationCode" required />
        <button type="button" @click="verifyEmail">인증 확인</button>
      </div>
      <div>
        <label for="password">비밀번호:</label>
        <input type="password" v-model="password" required />
      </div>
      <div>
        <label for="nickname">닉네임:</label>
        <input type="text" v-model="nickname" required />
        <button type="button" @click="checkNickname">중복 확인</button>
      </div>
      <div>
        <label for="address">주소:</label>
        <input type="text" v-model="address" />
      </div>
      <div>
        <label for="phone">핸드폰 번호:</label>
        <input type="text" v-model="phone" required />
      </div>
      <button type="submit">회원가입</button>
    </form>
    <div id="error-message" v-if="errorMessage" class="error">{{ errorMessage }}</div>
    <div id="success-message" v-if="successMessage" class="success">{{ successMessage }}</div>
  </div>
</template>

<script>
export default {
  name: 'UserRegister',
  data() {
    return {
      username: '',
      email: '',
      verificationCode: '',
      password: '',
      nickname: '',
      address: '',
      phone: '',
      isEmailVerified: false,
      isNicknameChecked: false,
      errorMessage: '',
      successMessage: '',
    };
  },
  methods: {
    async sendVerificationCode() {
      const response = await fetch('/api/send-verification-code', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email: this.email }),
      });

      if (response.ok) {
        alert('인증 코드가 이메일로 전송되었습니다.');
        this.successMessage = '';
        this.errorMessage = '';
      } else {
        const errorData = await response.json().catch(() => ({ message: 'Unknown error occurred' }));
        this.errorMessage = `이메일 전송에 실패했습니다: ${errorData.message}`;
        this.successMessage = '';
      }
    },
    async verifyEmail() {
      const response = await fetch('/api/verify-email', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email: this.email, code: this.verificationCode }),
      });

      if (response.ok) {
        this.isEmailVerified = true;
        this.successMessage = '이메일 인증에 성공했습니다.';
        this.errorMessage = '';
      } else {
        const errorData = await response.json().catch(() => ({ message: 'Unknown error occurred' }));
        this.errorMessage = `이메일 인증에 실패했습니다: ${errorData.message}`;
        this.successMessage = '';
      }
    },
    async checkNickname() {
      const response = await fetch('/api/check-nickname', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ nickname: this.nickname }),
      });

      if (response.ok) {
        this.isNicknameChecked = true;
        this.successMessage = '사용 가능한 닉네임입니다.';
        this.errorMessage = '';
      } else {
        const errorData = await response.json();
        this.errorMessage = `닉네임 중복 확인에 실패했습니다: ${errorData.message}`;
        this.successMessage = '';
      }
    },
    async register() {
      if (this.password.length <= 7) {
        this.errorMessage = '비밀번호는 8자 이상이어야 합니다.';
        this.successMessage = '';
        return;
      }

      if (!this.isEmailVerified) {
        this.errorMessage = '이메일 인증을 완료해주세요.';
        this.successMessage = '';
        return;
      }

      if (!this.isNicknameChecked) {
        this.errorMessage = '닉네임 중복 확인을 완료해주세요.';
        this.successMessage = '';
        return;
      }

      const response = await fetch('/api/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username: this.username,
          email: this.email,
          password: this.password,
          nickname: this.nickname,
          address: this.address,
          phone: this.phone,
        }),
      });

      if (response.ok) {
        alert('회원가입이 완료되었습니다.');
        this.errorMessage = '';
        this.successMessage = '';
      } else {
        const errorData = await response.json();
        this.errorMessage = `회원가입에 실패했습니다: ${errorData.message}`;
        this.successMessage = '';
      }
    },
  },
};
</script>

<style scoped>
.error {
  color: red;
}
.success {
  color: green;
}
body {
  background: linear-gradient(to right, #ff7e5f, #feb47b);
  height: 100vh;
  margin: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: white;
  font-family: Arial, sans-serif;
}
</style>
