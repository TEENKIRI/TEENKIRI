<template>
  <v-footer app absolute="false"  color="#D7E4F0" >
    <v-container>
      <v-row>
        <v-col>
          <div class="footer-content">
            <img :src="logo" alt="로고" class="logo-image" />
            <p class="addr">
              <span class="gray">(07060) 서울특별시 동작구 보라매로 87 (신대방동 344-4)</span>
              대표전화 02.1234.5678<span class="gap"></span>대표팩스 02.5678.1234<span class="gap"></span>이메일 teenkiri@teenkiri.co.kr
            </p>
            <p class="copy">
              Copyright 2024 © teenkiri. all rights reserved.
            </p>
          </div>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <ul class="link">
            <li><a href="javascript:void(0)" class="active">개인정보처리방침</a></li>
            <li><a href="javascript:void(0)">개인정보 보호</a></li>
            <li><a href="javascript:void(0)">저작권 정책</a></li>
            <li><a href="javascript:void(0)">찾아오시는길</a></li>
          </ul>
        </v-col>
      </v-row>
    </v-container>
  </v-footer>
</template>

<script>
export default {
  name: 'FooterComponent',
  data() {
    return {
      logo: require('@/assets/images/ico_logo.png'),
      isLogin: false // 초기값 설정
    };
  },
}
</script>

<style scoped>

.footer-content {
  text-align: center;
  margin-bottom: 20px;
}

.logo {
  font-weight: bold;
  margin-bottom: 5px;
}

.addr {
  font-size: 12px;
  color: #666;
}

.copy {
  font-size: 10px;
  color: #999;
}

.link {
  list-style: none;
  padding: 0;
  text-align: center;
}

.link li {
  display: inline;
  margin-right: 15px;
}

.link a {
  color: inherit;
  text-decoration: none;
}

.tel dl {
  display: inline-block;
  margin-right: 20px;
  text-align: left;
}

.tel dt {
  font-weight: bold;
}

.tel dd {
  margin: 0 0 10px 0;
}

.logo-image {
  height: 3%; 
}
</style>
