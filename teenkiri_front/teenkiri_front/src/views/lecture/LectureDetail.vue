<template>
  <v-container>
    <div class="mt-5">
      <h1>{{ lectureData.title }}</h1>
      <div>나의 진행률 : <span>{{ userProgress }}</span>%</div>
      <div class="d-flex justify-center mt-5">
        <!-- videoOptions.sources가 설정되었을 때만 VideoPlayer를 렌더링 -->
        <!-- <video-player v-if="videoOptions.sources[0].src" :options="videoOptions" /> -->
        <video
          ref="videoPlayer"
          class="video-js vjs-theme-city vjs-16-9 vjs-big-play-centered"
        ></video>
      </div>
    </div>
  </v-container>
</template>

<script>
// import VideoPlayer from '@/components/Lecture/videoPlayer.vue';
import axios from "axios";
import videojs from "video.js";

export default {
  // name: 'VideoExample',
  name: "VideoPlayer",
  // components: {
  //   VideoPlayer
  // },
  data() {
    return {
      user: {
        token: "",
        id: "",
        email: "",
      },
      lectureId: "",
      lectureData: {},

      // 영상 진행률 체크를 위한 코드
      isVideoPlay: false,
      currentVideoTime : 0,
      mostWatchedTime : 0,
      userProgress : 0, // 유저 진행률 표시용

      json: {
        createdTime: "2024-08-16T16:28:53",
        enrollmentId: 4,
        id: 1,
        imageUrl:
          "https://hannahbucket21.s3.ap-northeast-2.amazonaws.com/1_t240612014338002.jpg",
        isCompleted: false,
        progress: 0,
        title: "20분 강의 - 1번강좌",
        updatedTime: "2024-08-16T16:28:55",
        userLectureDuration: 0,
        videoDuration: 1294,
        videoUrl: "https://hannahbucket2",
      },

      player: null,

      videoOptions: {
        autoplay: true,
        controls: true,
        sources: [
          {
            src: "",
            type: "video/mp4",
          },
        ],
      },

    // 인터벌 ID를 저장할 변수
      checkInterval: null, 
      updateUserProgressInterval : null
    };
  },
  async created() {
    try {
      await this.$store.dispatch("setUserAllInfoActions");
      this.user = this.$store.getters.getUserObj;

      if (this.user.token === "") {
        alert("로그인이 필요합니다.");
        location.href = -1;
      }

      this.lectureId = this.$route.params.id;

      // 강의 세부 정보를 가져옵니다.
      await this.getLectureDetail();

      // 강의 세부 정보에서 비디오 URL을 설정하고 VideoPlayer 컴포넌트를 렌더링합니다.
      this.setLectureDetail();
    } catch (error) {
      console.error("An error occurred while fetching user info:", error);
    }
  },
  mounted() {
    // player를 초기화합니다.
    this.initPlayer();
  },
  watch: {
    "videoOptions.sources": {
      handler(newSources) {
        const newSrc = newSources[0].src;
        console.log("주소 바뀜 watch!! >> ", newSrc);
        if (newSrc) {
          if (this.player) {
            this.player.src(newSrc);
          } else {
            this.initPlayer();
          }
        }
      },
      deep: true, // 깊은 변경 감지
    },
  },
  methods: {
    async getLectureDetail() {
      console.log("lecture detail 실행!", this.lectureId);
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/user/lecture/detail/${this.lectureId}`
        );
        const additionalData = response.data.result;
        console.log(response);
        this.lectureData = additionalData;


        // 시간용 변수 적용
        this.currentVideoTime = this.lectureData.userLectureDuration;
        this.mostWatchedTime = this.currentVideoTime;
        this.updateUserProgress(); // 유저 표시용도의 퍼센트 계산

        // lectureData에서 videoUrl을 가져와 videoOptions를 업데이트합니다.
        this.videoOptions.sources[0].src = this.lectureData.videoUrl;
      } catch (e) {
        if (e.response.data.status_code === 404) {
          alert("수강신청이 되지 않았습니다.");
          history.go(-1);
        } else {
          console.error(e);
        }
      }
    },
    setLectureDetail() {
      // videoOptions가 업데이트된 후 loadVideo를 true로 설정하여 VideoPlayer를 렌더링합니다.
      this.loadVideo = true;
    },
    initPlayer() {
      if (this.videoOptions.sources[0].src) {
        this.player = videojs(this.$refs.videoPlayer, this.videoOptions, () => {
          // 영상 시작 시간을 설정하는 이벤트
          this.player.on("loadstart", () => {
            this.setStartTime();
          });

          // 영상이 시작될 때
          this.player.on("play", () => {
            this.onVideoPlay();
          });

          // 영상이 일시정지될 때
          this.player.on("pause", () => {
            this.onVideoPause();
          });

          // 영상이 끝날 때
          this.player.on("ended", () => {
            this.onVideoEnded();
          });
        });
      }
    },
    setStartTime() {
      // 영상의 시작 시간을 설정
      this.player.currentTime(this.currentVideoTime);
      console.log("영상의 시작 시간이 설정되었습니다.");
    },
    onVideoPlay() {
      console.log("영상이 시작되었습니다.");
      // 3초마다 플레이된 시간 체크 시작
      this.checkInterval = setInterval(() => {
        this.checkPlayTime();
      }, 3000);

      this.updateUserProgressInterval = setInterval(() => {
        this.updateUserProgress();
      }, 1000);
    },
    onVideoPause() {
      console.log("영상이 일시정지되었습니다.");
      // 3초마다 플레이된 시간 체크 중지
      if (this.checkInterval) {
        clearInterval(this.checkInterval);
        this.checkInterval = null;
      }

      // 퍼센트 계산하던 것 체크 중지
      if (this.updateUserProgressInterval) {
        clearInterval(this.updateUserProgressInterval);
        this.updateUserProgressInterval = null;
      }
    },
    onVideoEnded() {
      console.log("영상이 끝났습니다.");
      clearInterval(this.checkInterval); // 영상이 끝나면 인터벌을 정리합니다.
      clearInterval(this.updateUserProgressInterval); // 영상이 끝나면 인터벌을 정리합니다.

      this.postUserVideoEndedStatus(); // 비디오 끝 함수 호츨
    },
    checkPlayTime() {
      this.currentVideoTime = Math.floor(this.player.currentTime()); // 소수점 내림으로 고정. (비디오 전체시간때문에!)
      this.postUserDurationVideoTime();
      console.log("현재 플레이된 시간: ", this.currentVideoTime, "초");
    },
    updateUserProgress() {
      if(this.currentVideoTime >= this.mostWatchedTime){
        const percentage = (this.currentVideoTime / this.lectureData.videoDuration) * 100; //현재 시간을 기준으로 퍼센트 계산
        let roundPercent = Math.round(percentage * 10) / 10; // 소수점 첫 번째 자리까지만 존재하도록 반올림
        roundPercent = (roundPercent >=100 && !this.lectureData.isCompleted) ? 99 : (roundPercent >= 100) ? 100 : roundPercent; // isCompleted true 여야 100
        this.userProgress = roundPercent; 
      }
    },

    async postUserDurationVideoTime(){
      if(this.currentVideoTime >= this.mostWatchedTime){
        try {
          const userLectureDuration = {
            userLectureDuration : this.currentVideoTime
          }
          const response = await axios.patch(
            `${process.env.VUE_APP_API_BASE_URL}/enroll/update/duration/${this.lectureData.enrollmentId}`, 
            userLectureDuration
          );
          console.log(response);
        } catch (e) {
          if (e.response.data.status_code === 404) {
            alert(e.response.data.status_code.e.response.data.status_message);
            history.go(-1);
          } else {
            alert(e.response.data.status_code.e.response.data.status_message);
            console.error(e);
          }
        }
      }
    },
    async postUserVideoEndedStatus(){
      console.log("비디오 끝~ 보내보내~")
      if(this.currentVideoTime > this.lectureData.videoDuration){
        this.currentVideoTime = this.lectureData.videoDuration; // 더 큰값이 들어가면 초기화
      }
      try {
        const isCompleted = {
          isCompleted : true
        }
        const response = await axios.patch(
          `${process.env.VUE_APP_API_BASE_URL}/enroll/update/complete/${this.lectureData.enrollmentId}`, 
          isCompleted
        );
        console.log(response);
      } catch (e) {
        if (e.response.data.status_code === 404) {
          alert(e.response.data.status_code.e.response.data.status_message);
          history.go(-1);
        } else {
          alert(e.response.data.status_code.e.response.data.status_message);
          console.error(e);
        }
      }
    }
  },
  beforeUnmount() {
    if (this.player) {
      this.player.dispose();
    }

    if (this.checkInterval) {
      clearInterval(this.checkInterval);
    }

    if (this.updateUserProgressInterval) {
      clearInterval(this.updateUserProgressInterval);
    }
  },
};
</script>
