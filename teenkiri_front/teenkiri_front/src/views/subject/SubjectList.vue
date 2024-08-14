<template>
  <v-container>
    <v-sheet class="mx-auto" width="500">
      <v-form ref="form" class="d-flex">
        <v-select
          v-model="searchType"
          :items="searchOptions"
          item-title="text"
          item-value="value"
          required
        ></v-select>
        <v-text-field
          v-model="searchValue"
          label="검색어를 입력하세요."
          required
        ></v-text-field>
      </v-form>
    </v-sheet>
    <v-card>
      <v-card-title>티니키리 서비스가 이 강좌를 추천해요!</v-card-title>
      <v-card-text>
        <div class="swiper swiperLectureBest">
          <div class="swiper-slide">
            <div class="thumb">
              <a href="javascript:void(0)"
                ><img
                  v-bind:src="require(`@/assets/images/thumb_1.png`)"
                  alt=""
              /></a>
            </div>
            <div class="txt">
              <p class="subject">함께하는 슬기로운 생활</p>
              <p class="name">한핑핑즈 선생님</p>
            </div>
            <button type="button" class="btn_like"></button>
          </div>
        </div>
      </v-card-text>
    </v-card>
    <v-row class="mt-5">
      <v-col cols="12" class="py-2">
        <v-btn-toggle
          v-model="text"
          color="primary"
          rounded="0"
          group
          class="d-flex flex-row"
        >
          <v-btn value="left" class="flex-grow-1"> 국어 </v-btn>
          <v-btn value="center" class="flex-grow-1 v-item--active">
            국어
          </v-btn>
          <v-btn value="right" class="flex-grow-1"> 국어 </v-btn>
          <v-btn value="justify" class="flex-grow-1"> Justify </v-btn>
        </v-btn-toggle>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-item-group
          selected-class="bg-purple"
          multiple
          class="d-flex justify-start"
        >
          <div class="mr-5">학년</div>
          <v-item v-for="n in 6" :key="n" v-slot="{ selectedClass, toggle }">
            <v-chip class="mr-2" :class="selectedClass" @click="toggle">
              학년 {{ n }}
            </v-chip>
          </v-item>
        </v-item-group>
      </v-col>
      <v-col>
        <v-row>
          <v-col class="d-flex flex-row">
            <v-btn>강좌 업로드</v-btn>
            <v-select
              v-model="selectedType"
              :items="selectedOptions"
              item-title="text"
              item-value="value"
              width="100"
              required
            ></v-select>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <h1>채팅이 들어가는 자리</h1>
    </v-row>
    <v-row>
      <v-card class="w-100">
        <v-card-title>강좌 목록</v-card-title>
        <v-card-text>
          <div class="swiper swiperLectureBest">
            <div class="swiper-slide">
              <div class="thumb">
                <a href="javascript:void(0)"
                  ><img
                    v-bind:src="require(`@/assets/images/thumb_1.png`)"
                    alt=""
                /></a>
              </div>
              <div class="txt">
                <p class="subject">함께하는 슬기로운 생활</p>
                <p class="name">한핑핑즈 선생님</p>
              </div>
              <button type="button" class="btn_like"></button>
            </div>
          </div>
        </v-card-text>
      </v-card>
    </v-row>
  </v-container>
</template>
  
  <script>
export default {
  data() {
    return {
      searchType: "all",
      searchOptions: [
        { text: "전체", value: "all" },
        { text: "강사명", value: "userTeacher" },
        { text: "강좌명", value: "title" },
      ],
      selectedType: "recent",
      selectedOptions: [
        { text: "최신순", value: "recent" },
        { text: "인기순", value: "rank" },
        { text: "평점순", value: "like" },
      ],
      searchValue: "",
      text: "center",
      icon: "justify",
      toggle_none: null,
      toggle_one: 0,
      toggle_exclusive: 2,
      toggle_multiple: [0, 1, 2],
    };
  },
  created() {},
  method: {},
};
</script>
  
  <style>
.inner {
  width: 1280px;
  width: 1280px;
  margin: 0 auto;
  position: relative;
}
.inner h2 {
  font-size: 36px;
  font-weight: 700;
}
.swiperLectureBest {
  margin: 35px 0 0;
  border-right: 1px solid #ccc;
  display: flex;
  flex-wrap: wrap;
}
.swiper-button-prev:after,
.swiper-button-next:after {
  display: none;
}
.swiper-button-prev {
  left: -60px;
  width: 30px;
  height: 51px;
  background: url("@/assets/images/ico_sw_prev.png") 50% 50% no-repeat;
  background-size: contain;
}
.swiper-button-next {
  right: -60px;
  width: 30px;
  height: 51px;
  background: url("@/assets/images/ico_sw_next.png") 50% 50% no-repeat;
  background-size: contain;
}
.swiper-slide {
  position: relative;
  width: calc(100% / 4);
  height: 390px;
  border: 1px solid #ccc;
  padding: 20px 27px;
}
.swiper-slide .thumb {
  width: 100%;
  height: 220px;
  overflow: hidden;
}
.swiper-slide .thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.swiper-slide .txt {
  margin: 20px 0 0;
}
.swiper-slide .txt .subject {
  font-size: 18px;
  font-weight: 400;
}
.swiper-slide .txt .name {
  margin: 15px 0 0;
  font-size: 14px;
  font-weight: 400;
  color: #5b5b5b;
}
.swiper-slide .btn_like {
  position: absolute;
  bottom: 20px;
  right: 27px;
  width: 24px;
  height: 24px;
  background: url("@/assets/images/ico_like.png") 50% 50% no-repeat;
  background-size: contain;
}
</style>