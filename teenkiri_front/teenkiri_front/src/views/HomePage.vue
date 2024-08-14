<template>
  <v-app>
    <v-carousel hide-delimiters>
      <v-carousel-item :src="require('../assets/images/visual_bg.png')" cover>
        <v-container>
          <v-row class="mainVisual">
            <v-col class="txtWrap">
              <p class="description">
                친구들과 대화하며 신나고 즐겁게 공부하자
              </p>
              <h1>즐겁게 캐치! 성취도 캐치!</h1>
              <a href="javascript:void(0)" class="more">자세히 보기</a>
            </v-col>
          </v-row>
        </v-container>
      </v-carousel-item>
    </v-carousel>
    <v-container>
      <v-row class="mb-4">
        <h2>티니키리 서비스가 이 강좌를 추천해요 👍</h2>
      </v-row>
      <v-row>
        <v-sheet class="mx-auto" elevation="4" max-width="1200">
          <v-slide-group
            v-model="model"
            class="pa-4"
            selected-class="bg-success"
            show-arrows
          >
            <v-slide-group-item
              v-for="n in 15"
              :key="n"
              v-slot="{ toggle, selectedClass }"
            >
              <v-card
                :class="['ma-4', selectedClass]"
                @click="toggle"
              >
                <div
                  class="swiper-slide d-flex flex-sm-column fill-height align-center justify-center w-100"
                >
                  <div class="thumb">
                    <a href="javascript:void(0)"
                      ><img
                        :src="require('../assets/images/thumb_2.png')"
                        alt=""
                    /></a>
                  </div>
                  <div class="txt">
                    <p class="subject">함께하는 슬기로운 생활</p>
                    <p class="name">한핑핑즈 선생님</p>
                  </div>
                  <button type="button" class="btn_like"></button>
                </div>
              </v-card>
            </v-slide-group-item>
          </v-slide-group>
        </v-sheet>
      </v-row>
    </v-container>
    <v-sheet class="mainStudy">
      <div class="inner">
        <div class="movieWrap">
          <div class="movie">
            <img src="../assets/images/movie_thumb.png" alt="" />
          </div>
          <div class="txt">
            <p class="subject">
              발표할때가 되면 창핑해! - 함께하는 발표 강좌
              <span>강의수 :</span> 10강
            </p>
            <p class="name">인기쟁이 창핑이</p>
            <button type="button" class="btn_like"></button>
          </div>
        </div>
        <div class="eventWrap">
          <div class="titimg">
            <a href="javascript:void(0)"
              ><img src="../assets/images/event_img.png" alt=""
            /></a>
          </div>
          <div class="event">
            <div class="evTit">
              <h3>EVENT</h3>
              <p>이벤트에 참여해 보세요</p>
            </div>
            <div class="evList">
              <ul>
                <li>
                  <a href="javascript:void(0)">영상으로 보는 티니키리</a>
                  <span>2024.08.08</span>
                </li>
                <li>
                  <a href="javascript:void(0)">성적표 제출 & 후기 이벤트</a>
                  <span>2024.08.08</span>
                </li>
                <li>
                  <a href="javascript:void(0)"
                    >회원 전용 ‘해외 이용’ 후기 이벤트</a
                  >
                  <span>2024.08.08</span>
                </li>
                <li>
                  <a href="javascript:void(0)"
                    >더욱 풍성한 혜택 ‘추천 이벤트’</a
                  >
                  <span>2024.08.08</span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </v-sheet>

    <v-container class="mainLectureBest">
      <div class="inner">
        <h2 class="mb-4">최고 인기 강좌</h2>
        <v-sheet class="mx-auto" elevation="4" max-width="1200">
          <v-slide-group
            v-model="model"
            class="pa-4"
            selected-class="bg-success"
            show-arrows
          >
            <v-slide-group-item
              v-for="n in 15"
              :key="n"
              v-slot="{ toggle, selectedClass }"
            >
              <v-card
                :class="['ma-4', selectedClass]"
                @click="toggle"
              >
                <div
                  class="swiper-slide d-flex flex-sm-column fill-height align-center justify-center w-100"
                >
                  <div class="thumb">
                    <a href="javascript:void(0)"
                      ><img
                        :src="require('../assets/images/thumb_2.png')"
                        alt=""
                    /></a>
                  </div>
                  <div class="txt">
                    <p class="subject">함께하는 슬기로운 생활</p>
                    <p class="name">한핑핑즈 선생님</p>
                  </div>
                  <button type="button" class="btn_like"></button>
                </div>
              </v-card>
            </v-slide-group-item>
          </v-slide-group>
        </v-sheet>
      </div>
    </v-container>
    <div class="mainNews">
      <div class="inner">
        <div class="tit">
          <h2>티니키리 소식</h2>
          <a href="javascript:void(0)">More</a>
        </div>
        <ul class="newsList">
          <li>
            <a href="javascript:void(0)">강좌리스트 이용 서비스 개편 안내</a>
            <span>2024.08.08</span>
          </li>
          <li>
            <a href="javascript:void(0)"
              >겨울방학 MindSpark 이벤트 당첨자 발표</a
            >
            <span>2024.08.08</span>
          </li>
          <li>
            <a href="javascript:void(0)"
              >2024년 1학기 내신진도강좌 & 과목별 특강 강좌 오픈</a
            >
            <span>2024.08.08</span>
          </li>
          <li>
            <a href="javascript:void(0)"
              >포인트샵(기프티콘, 문화상품권) 점검 안내 (8/4(일) 00:00~06:00)</a
            >
            <span>2024.08.08</span>
          </li>
          <li>
            <a href="javascript:void(0)"
              >티니키리 휴면 정책 변경 및 개인정보 처리방침 개정 사전 안내</a
            >
            <span>2024.08.08</span>
          </li>
        </ul>
      </div>
    </div>
    <div class="mainBanner">
      <img src="../assets/images/banner.png" alt="" />
    </div>
  </v-app>
</template>

<script>
export default {
  data() {},
};
</script>

<style>
.mainVisual {
  position: relative;
}
.mainVisual .txtWrap {
  position: absolute;
  top: 150px;
  left: 0;
}
.mainVisual .txtWrap .description {
  font-size: 26px;
  font-weight: 500;
  color: #3370b6;
}
.mainVisual .txtWrap h1 {
  font-size: 50px;
  font-weight: 500;
}
.mainVisual .txtWrap .more {
  display: block;
  width: 170px;
  height: 40px;
  line-height: 40px;
  text-align: center;
  margin: 30px 0 0;
  font-size: 16px;
  font-weight: 400;
  color: #fff;
  border-radius: 10px;
  background: #f27885;
}

.mainLecture {
  padding: 10px 0 100px;
}
.mainLecture .inner {
  width: 1280px;
  width: 1280px;
  margin: 0 auto;
  position: relative;
}
.mainLecture .inner h2 {
  font-size: 24px;
  font-weight: 700;
}
.mainLecture .swiperLecture {
  margin: 35px 0 0;
  border-right: 1px solid #ccc;
}
.mainLecture .swiper-button-prev:after,
.mainLecture .swiper-button-next:after {
  display: none;
}
.mainLecture .swiper-button-prev {
  left: -60px;
  width: 30px;
  height: 51px;
  background: url("../assets/images/ico_sw_prev.png") 50% 50% no-repeat;
  background-size: contain;
}
.mainLecture .swiper-button-next {
  right: -60px;
  width: 30px;
  height: 51px;
  background: url("../assets/images/ico_sw_next.png") 50% 50% no-repeat;
  background-size: contain;
}
.mainLecture .swiper-slide {
  position: relative;
  height: 390px;
  border: 1px solid #ccc;
  border-right: 0;
  padding: 20px 27px;
}
.mainLecture .swiper-slide .thumb {
  width: 100%;
  height: 220px;
  overflow: hidden;
}
.mainLecture .swiper-slide .thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.mainLecture .swiper-slide .txt {
  margin: 20px 0 0;
}
.mainLecture .swiper-slide .txt .subject {
  font-size: 18px;
  font-weight: 400;
}
.mainLecture .swiper-slide .txt .name {
  margin: 15px 0 0;
  font-size: 14px;
  font-weight: 400;
  color: #5b5b5b;
}
.mainLecture .swiper-slide .btn_like {
  position: absolute;
  bottom: 20px;
  right: 27px;
  width: 24px;
  height: 24px;
  background: url("../assets/images/ico_like.png") 50% 50% no-repeat;
  background-size: contain;
}

.mainStudy {
  background: url("../assets/images/study_bg.png") 50% 50% no-repeat !important;
  background-size: cover;
  margin: 12vh 0;
}
.mainStudy .inner {
  width: 1280px;
  margin: 0 auto;
  position: relative;
  display: flex;
  gap: 30px;
}
.mainStudy .inner .movieWrap {
  margin: 40px 0 0;
  width: 800px;
  height: 540px;
  padding: 25px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 3px 3px 8px 3px rgba(0, 0, 0, 0.1);
}
.mainStudy .inner .movieWrap .movie {
  width: 750px;
  height: 420px;
  overflow: hidden;
}
.mainStudy .inner .movieWrap .movie img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.mainStudy .inner .movieWrap .txt {
  position: relative;
  margin: 20px 0 0;
}
.mainStudy .inner .movieWrap .txt .subject {
  font-size: 18px;
  font-weight: 400;
}
.mainStudy .inner .movieWrap .txt .subject span {
  margin-left: 15px;
  color: #747474;
}
.mainStudy .inner .movieWrap .txt .name {
  margin: 10px 0 0;
  font-size: 14px;
  color: #5b5b5b;
  font-weight: 400;
}
.mainStudy .inner .movieWrap .txt .btn_like {
  position: absolute;
  top: 0;
  right: 0;
  width: 24px;
  height: 24px;
  background: url("../assets/images/ico_like.png") 50% 50% no-repeat;
  background-size: contain;
}

.mainStudy .inner .eventWrap {
  margin: 57px 0 0;
}
.mainStudy .inner .eventWrap .titimg {
  position: relative;
  left: -15px;
  bottom: -10px;
  z-index: 2;
}
.mainStudy .inner .eventWrap .event {
  position: relative;
  top: 0;
  z-index: 1;
}
.mainStudy .inner .eventWrap .event .evTit {
  display: flex;
  gap: 20px;
  align-items: center;
  height: 60px;
  background: #fff;
  border-radius: 0 0 10px 10px;
  border-bottom: 2px dotted #ccc;
  padding: 0 20px;
  box-shadow: 3px 3px 8px 3px rgba(0, 0, 0, 0.1);
}
.mainStudy .inner .eventWrap .event .evTit h3 {
  font-size: 36px;
  font-weight: 700;
  color: #f27885;
}
.mainStudy .inner .eventWrap .event .evTit p {
  margin: 10px 0 0;
  font-size: 18px;
  font-weight: 400;
}
.mainStudy .inner .eventWrap .event .evList {
  padding: 30px 20px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 3px 3px 8px 3px rgba(0, 0, 0, 0.1);
}
.mainStudy .inner .eventWrap .event .evList li {
  display: flex;
}
.mainStudy .inner .eventWrap .event .evList li a {
  font-size: 18px;
  font-weight: 400;
}
.mainStudy .inner .eventWrap .event .evList li span {
  margin-left: auto;
  font-size: 18px;
  font-weight: 400;
  color: #757575;
}
.mainStudy .inner .eventWrap .event .evList li + li {
  margin: 15px 0 0;
}

.mainLectureBest .inner {
  width: 1280px;
  margin: 12vh 0;
  position: relative;
}
.mainLectureBest .inner h2 {
  font-size: 36px;
  font-weight: 700;
}
.mainLectureBest .swiperLectureBest {
  margin: 35px 0 0;
  border-right: 1px solid #ccc;
}
.mainLectureBest .swiper-button-prev:after,
.mainLectureBest .swiper-button-next:after {
  display: none;
}
.mainLectureBest .swiper-button-prev {
  left: -60px;
  width: 30px;
  height: 51px;
  background: url("../assets/images/ico_sw_prev.png") 50% 50% no-repeat;
  background-size: contain;
}
.mainLectureBest .swiper-button-next {
  right: -60px;
  width: 30px;
  height: 51px;
  background: url("../assets/images/ico_sw_next.png") 50% 50% no-repeat;
  background-size: contain;
}
.mainLectureBest .swiper-slide {
  position: relative;
  height: 390px;
  border: 1px solid #ccc;
  border-right: 0;
  padding: 20px 27px;
}
.mainLectureBest .swiper-slide .thumb {
  width: 100%;
  height: 220px;
  overflow: hidden;
}
.mainLectureBest .swiper-slide .thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.mainLectureBest .swiper-slide .txt {
  margin: 20px 0 0;
}
.mainLectureBest .swiper-slide .txt .subject {
  font-size: 18px;
  font-weight: 400;
}
.mainLectureBest .swiper-slide .txt .name {
  margin: 15px 0 0;
  font-size: 14px;
  font-weight: 400;
  color: #5b5b5b;
}
.mainLectureBest .swiper-slide .btn_like {
  position: absolute;
  bottom: 20px;
  right: 27px;
  width: 24px;
  height: 24px;
  background: url("../assets/images/ico_like.png") 50% 50% no-repeat;
  background-size: contain;
}

.mainNews {
  padding: 0 0 90px;
}
.mainNews .inner {
  width: 1280px;
  margin: 0 auto;
  position: relative;
  display: flex;
}
.mainNews .tit h2 {
  font-size: 36px;
  font-weight: 700;
}
.mainNews .tit a {
  display: inline-block;
  margin: 25px 0 0;
  font-size: 18px;
  font-weight: 400;
  padding-right: 40px;
  background: url("../assets/images/ico_arrow.png") 100% 8px no-repeat;
}
.mainNews .newsList {
  margin-left: auto;
  width: 755px;
  border-top: 1px solid #bdbdbd;
}
.mainNews .newsList li {
  display: flex;
  align-items: center;
  height: 55px;
  border-bottom: 1px solid #bdbdbd;
  padding: 0 20px;
}
.mainNews .newsList li a {
  font-size: 16px;
  font-weight: 400;
}
.mainNews .newsList li span {
  margin-left: auto;
  font-size: 16px;
  font-weight: 400;
  color: #757575;
}

.mainBanner {
  width: 1280px;
  margin: 0 auto;
}
</style>