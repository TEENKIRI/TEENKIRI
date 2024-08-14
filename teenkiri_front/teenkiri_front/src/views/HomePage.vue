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
          <v-slide-group v-model="model" class="pa-4" selected-class="bg-success" show-arrows>
            <v-slide-group-item v-for="n in 15" :key="n" v-slot="{ toggle, selectedClass }">
              <v-card :class="['ma-4', selectedClass]" @click="toggle">
                <div class="swiper-slide d-flex flex-sm-column fill-height align-center justify-center w-100">
                  <div class="thumb">
                    <a href="javascript:void(0)">
                      <img :src="require('../assets/images/thumb_2.png')" alt=""/>
                    </a>
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
            <a href="board/event/list">
              <img src="../assets/images/event_img.png" alt=""/>
            </a>            
          </div>

          <div class="event">
            <div class="evTit">
              <h3>EVENT</h3>
              <p>이벤트에 참여해 보세요</p>
            </div>
            <div class="evList">
              <p v-if="event && event.length === 0">이벤트가 없습니다.</p>
              <ul v-else id="event-list">
                <li v-for="event in top5" :key="event.id">
                  <a :href="`/board/Detail/event/${event.id}`">{{ event.title }}</a>
                  <span>{{ formatDate(event.updatedTime) }}</span>
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
          <v-slide-group v-model="model" class="pa-4" selected-class="bg-success" show-arrows>
            <v-slide-group-item v-for="n in 15" :key="n" v-slot="{ toggle, selectedClass }">
              <v-card :class="['ma-4', selectedClass]" @click="toggle">
                <div class="swiper-slide d-flex flex-sm-column fill-height align-center justify-center w-100">
                  <div class="thumb">
                    <a href="javascript:void(0)">
                      <img :src="require('../assets/images/thumb_2.png')" alt=""/>
                    </a>
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
          <a href="board/notice/list">More</a>
        </div>
        <ul class="newsList">
          <li v-for="newsItem in topNews" :key="newsItem.id">
            <a :href="`/board/Detail/notice/${newsItem.id}`">{{ newsItem.title }}</a>
            <span>{{ formatDate(newsItem.updatedTime) }}</span>
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
  data() {
    return {
      event: [],
      news: [], 
    };
  },
  computed: {
    top5() {
      return this.event.slice(0, 4);
    },
    topNews() {
      return this.news.slice(0, 4);
    },
  },
  created() {
    this.fetchEvents(); 
    this.fetchNews(); 
  },
  methods: {
    fetchEvents() {
      fetch(`${process.env.VUE_APP_API_BASE_URL}/board/event/list`)

      // 자꾸 에러나서 예외처리 만들었음. 삭제할 예정
        .then(response => {
          if (!response.ok) {
            throw new Error('네트워크 문제.');
          }
          return response.json();
        })
        .then(data => {
          console.log('API 응답 데이터:', data);
          this.event = data.result.content || [];
        })
        .catch(error => {
          console.error('이벤트를 불러올 수 없습니다:', error);
          this.event = [];
        });
    },
    fetchNews() {
      fetch(`${process.env.VUE_APP_API_BASE_URL}/board/notice/list`)

      // 자꾸 에러나서 예외처리 만들었음. 삭제할 예정

        .then(response => {
          if (!response.ok) {
            throw new Error('네트워크 문제.');
          }
          return response.json();
        })
        .then(data => {
          console.log('API 응답 데이터:', data);
          this.news = data.result.content || [];
        })
        .catch(error => {
          console.error('소식을 불러올 수 없습니다:', error);
          this.news = [];
        });
    },
    formatDate(date) {
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(date).toLocaleDateString(undefined, options);
    }},
};
</script>

<style src="@/assets/css/HomePage.css"></style>
