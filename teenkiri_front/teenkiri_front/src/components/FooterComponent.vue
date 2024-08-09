<template>
    <v-footer app absolute="false">
      <v-container>
        <v-row>
          <v-col>
            <div class="footer-content">
              <div class="logo">로고</div>
              <p class="addr">
                <span class="gray">(03045) 서울 종로구 효자로 12 (세종로 1-57)</span>
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
        <v-row>
          <v-col>
            <div class="tel">
              <dl>
                <dt>강좌상담</dt>
                <dd>02.1234.5678 </dd>
              </dl>
              <dl>
                <dt>운영시간</dt>
                <dd>09:00 ~ 18:00</dd>
              </dl>
              <dl>
                <dt>시스템 이용 문의</dt>
                <dd>02.1234.5678 </dd>
              </dl>
              <dl>
                <dt>운영시간</dt>
                <dd>09:00 ~ 18:00</dd>
              </dl>
            </div>
          </v-col>
        </v-row>
      </v-container>
    </v-footer>
  </template>
  
  <script>
  export default {
    name: 'FooterComponent'
  }
  </script>
  
  <style scoped>
  .footer-content {
    text-align: center;
    margin-bottom: 20px;
  }
  
  .logo {
    font-weight: bold;
    margin-bottom: 10px;
  }
  
  .addr {
    font-size: 14px;
    color: #666;
  }
  
  .copy {
    font-size: 12px;
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

  </style>
  