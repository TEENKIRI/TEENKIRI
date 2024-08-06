<template>
    <v-footer app>
      <v-container>
        <v-row>
          <v-col class="text-center">
            <div class="copyright">
              <div class="logo">로고</div>
              <p class="addr">
                <span class="gray">(03045) 서울 종로구 효자로 12 (세종로 1-57)</span>
                대표전화 02.1234.5678<span class="gap"></span>대표팩스 02.5678.1234<span class="gap"></span>이메일 teenkiri@teenkiri.co.kr
              </p>
              <p class="copy">
                Copyright 2024 © teenkiri. all rights reserved.
              </p>
            </div>
            <div class="info">
              <ul class="link">
                <li><a href="javascript:void(0)" class="active">개인정보처리방침</a></li>
                <li><a href="javascript:void(0)">개인정보 보호</a></li>
                <li><a href="javascript:void(0)">저작권 정책</a></li>
                <li><a href="javascript:void(0)">찾아오시는길</a></li>
              </ul>
              <div class="tel">
                <dl>
                  <dt>강좌상담</dt>
                  <dd>02.1234.5678 </dd>
                  <dt>운영시간</dt>
                  <dd>09:00 ~ 18:00</dd>
                </dl>
                <dl>
                  <dt>시스템 이용 문의</dt>
                  <dd>02.1234.5678 </dd>
                  <dt>운영시간</dt>
                  <dd>09:00 ~ 18:00</dd>
                </dl>
              </div>
            </div>
          </v-col>
        </v-row>
      </v-container>
    </v-footer>
  </template>
  
  <script>
  export default {
    name: 'FooterComponent',
  }
  </script>
  
  