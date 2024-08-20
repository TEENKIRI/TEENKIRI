<template>
  <v-app>
    <v-carousel show-arrows="hover" cycle hide-delimiter-background class="font">
      <v-carousel-item :src="require('../assets/images/visual_bg_1.png')" cover>
        <v-container>
          <v-row class="mainVisual">
            <v-col class="txtWrap">
              <p class="description">친구들과 대화하며 신나고 즐겁게 공부하자</p>
              <h1 class="font_notrahope">즐겁게 캐치! 성취도 캐치!</h1>
            </v-col>
          </v-row>
        </v-container>
      </v-carousel-item>
      <v-carousel-item :src="require('../assets/images/visual_bg_2.png')" cover>
        <v-container>
          <v-row class="mainVisual">
            <v-col class="txtWrap">
              <p class="description text-red-lighten-1">내 사랑을 듬~뿍 줄게!</p>
              <h1 class="font_notrahope">어렵다면?! 도움 캐치!</h1>
            </v-col>
          </v-row>
        </v-container>
      </v-carousel-item>
      <v-carousel-item :src="require('../assets/images/visual_bg_3.png')" cover>
        <v-container>
          <v-row class="mainVisual">
            <v-col class="txtWrap">
              <p class="description">좋~아 할 수 있따! 아잣!</p>
              <h1 class="font_notrahope">할 수 있어 캐치!! 아자!</h1>
            </v-col>
          </v-row>
        </v-container>
      </v-carousel-item>
      <v-carousel-item :src="require('../assets/images/visual_bg_4.png')" cover>
        <v-container>
          <v-row class="mainVisual">
            <v-col class="txtWrap">
              <p class="description">백마로 변신해서 캐치!</p>
              <h1 class="font_notrahope">백마 캐치! 변신 캐치!</h1>
            </v-col>
          </v-row>
        </v-container>
      </v-carousel-item>
    </v-carousel>
    <!-- 추천 강좌 섹션 -->
    <v-container>
        <v-card-title class="font_notrahope" style="font-size:2rem;">
          티니키리 서비스가 <span class="teen_red_font">추천</span>해요! 👍
        </v-card-title>
        <v-card-text v-if="subject.subjectIsMainList.length">
        <swiper
          :slides-per-view="3"
          :spaceBetween="30"
          :loop="true"
          :pagination="{clickable: true}"
          :modules="modules"
          :autoplay="{
            delay: 2000,
            disableOnInteraction: false,
          }"
        >
          <swiper-slide 
              v-for="sm in subject.subjectIsMainList"
              :key="sm.id"
              @click="goToDetail(sm.id)"
            >
              <v-img
                aspect-ratio="16/9"
                cover
                :src="sm.subjectThumUrl"
                style="border-radius: 0px;"
                alt="강좌 썸네일"
              ></v-img>
              <div class="txt">
                <p class="subject">{{ sm.title }}</p>
                <p class="name">{{ sm.teacherName }}</p>
              </div>
            </swiper-slide>
        </swiper>
        </v-card-text>
    </v-container>
    <v-sheet class="mainStudy">
      <v-container class="inner">
        <div class="movieWrap" v-if="this.subject.subjectList.length > 0" @click="goToDetail(subject.subjectList[0].id)">
          <div class="movie">
            <v-img
                aspect-ratio="16/9"
                cover
                :src="subject.subjectList[0].subjectThumUrl"
                alt="강좌 썸네일"
              ></v-img>
          </div>
          <div class="txt">
            <p class="subject" >
              {{ subject.subjectList[0].title }}
            </p>
            <p class="name">{{ subject.subjectList[0].teacherName }}</p>
          </div>
        </div>
        <div class="eventWrap">
          <div class="titimg">
            <a href="board/event/list">
              <img src="../assets/images/event_img.png" alt="" />
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
      </v-container>
    </v-sheet>

    <v-container class="mx-auto mt-5 mb-5" elevation="4" max-width="1200">
      <v-card-title class="mb-4 font_notrahope" style="font-size:2rem;">
          최고 <span class="teen_red_font">인기</span>강좌 에요! 😍
      </v-card-title>
      <v-card-text v-if="subject.subjectList.length">
        <swiper
            :slides-per-view="3"
            :spaceBetween="30"
            :loop="true"
            :pagination="{clickable: true}"
            :modules="modules"
            :autoplay="{
              delay: 2000,
              disableOnInteraction: false,
            }"
          >
            <swiper-slide 
                v-for="s in subject.subjectList"
                :key="s.id"
                @click="goToDetail(s.id)"
              >
                <v-img
                  aspect-ratio="16/9"
                  cover
                  :src="s.subjectThumUrl"
                  style="border-radius: 0px;"
                  alt="강좌 썸네일"
                ></v-img>
                <div class="txt">
                  <p class="subject">{{ s.title }}</p>
                  <p class="name">{{ s.teacherName }}</p>
                </div>
              </swiper-slide>
          </swiper>
      </v-card-text>
      
    </v-container>

    <v-container class="mainNews">
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
    </v-container>
    <v-sheet style="background:#F0BFB3;">
      <div class="mainBanner" style="font-size:0;" @click="moveEventPage">
        <img src="../assets/images/banner.png" alt="" />
      </div>
    </v-sheet>
    
  </v-app>
</template>

<script>
import axios from "axios";
import { Swiper, SwiperSlide } from 'swiper/vue';
import { Autoplay, Pagination, Navigation } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/pagination';
import 'swiper/css/navigation';

export default {
  components: {
    Swiper,
    SwiperSlide
  },
  setup() {
    const onSwiper = (swiper) => {
      console.log(swiper);
    };
    const onSlideChange = () => {
      console.log('slide change');
    };
    return {
      onSwiper,
      onSlideChange,
      modules: [ Autoplay, Pagination, Navigation],
    };
  },
  data() {
    return {
      event: [],
      news: [],
      subject: {
        subjectList: [],
        subjectIsMainList: [],
      },
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
    this.getSubjectMainList();
    this.getSubjectList();
  },
  methods: {
    fetchEvents() {
      fetch(`${process.env.VUE_APP_API_BASE_URL}/board/event/list`)
        // 자꾸 에러나서 예외처리 만들었음. 삭제할 예정
        .then((response) => {
          if (!response.ok) {
            throw new Error("네트워크 문제.");
          }
          return response.json();
        })
        .then((data) => {
          console.log("API 응답 데이터:", data);
          this.event = data.result.content || [];
        })
        .catch((error) => {
          console.error("이벤트를 불러올 수 없습니다:", error);
          this.event = [];
        });
    },
    fetchNews() {
      fetch(`${process.env.VUE_APP_API_BASE_URL}/board/notice/list`)
        // 자꾸 에러나서 예외처리 만들었음. 삭제할 예정

        .then((response) => {
          if (!response.ok) {
            throw new Error("네트워크 문제.");
          }
          return response.json();
        })
        .then((data) => {
          console.log("API 응답 데이터:", data);
          this.news = data.result.content || [];
        })
        .catch((error) => {
          console.error("소식을 불러올 수 없습니다:", error);
          this.news = [];
        });
    },
    formatDate(date) {
      const options = { year: "numeric", month: "long", day: "numeric" };
      return new Date(date).toLocaleDateString(undefined, options);
    },
    async getSubjectMainList() {
      try {
        const params = {
          size: 10, // 상단 추천 강좌는 10개만 보임
          page: 0,
        };
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/subject/main/list`,
          { params }
        );
        this.subject.subjectIsMainList = [
          ...this.subject.subjectIsMainList,
          ...response.data.result.content,
        ];
      } catch (e) {
        console.error(e);
      }
    },
    async getSubjectList() {
      try {
        const params = {
          size: 20,
          page: 0,
          search: "",
          searchType: "all",
          sortType: "like",
          grades: "",
        };

        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/subject/list`,
          { params }
        );
        this.subject.subjectList = response.data.result.content;
      } catch (e) {
        console.error(e);
      }
    },
    goToDetail(id) {
      this.$router.push({ name: 'SubjectDetail', params: { id } });
    },
    moveEventPage(){
      this.$router.push({ name: 'BoardList', params: { category: 'event' } });
    }
  },
};
</script>

<style src="@/assets/css/HomePage.css"></style>
<style>
.swiper-pagination{
  position:static;
  margin-top:12px;
}
</style>