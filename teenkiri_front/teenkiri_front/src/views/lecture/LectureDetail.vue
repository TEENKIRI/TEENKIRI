<template>
  <v-container>
    <div class="mt-5">
      <h1>{{ lectureData.title }}</h1>
      <div class="d-flex justify-center mt-5">
        <!-- videoOptions.sources가 설정되었을 때만 VideoPlayer를 렌더링 -->
        <video-player v-if="videoOptions.sources[0].src" :options="videoOptions" />
      </div>
    </div>
  </v-container>
</template>

<script>
import VideoPlayer from '@/components/Lecture/videoPlayer.vue';
import axios from 'axios';

export default {
  name: 'VideoExample',
  components: {
    VideoPlayer
  },
  data() {
    return {
      user: {
        token: "",
        id: "",
        email: ""
      },
      lectureId: "",
      lectureData: {},
      videoOptions: {
        autoplay: true,
        controls: true,
        sources: [
          {
            src: "",
            type: 'video/mp4'
          }
        ]
      }
    };
  },
  async created() {
    try {
      await this.$store.dispatch('setUserAllInfoActions');
      this.user = this.$store.getters.getUserObj;

      if (this.user.token === "") {
        alert('로그인이 필요합니다.');
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
  methods: {
    async getLectureDetail() {
      console.log("lecture detail 실행!", this.lectureId)
      try {
        const response = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/user/lecture/detail/${this.lectureId}`
        );
        const additionalData = response.data.result;
        console.log(response);
        this.lectureData = additionalData;

        // lectureData에서 videoUrl을 가져와 videoOptions를 업데이트합니다.
        console.log(this.videoOptions)
        this.videoOptions.sources[0].src = this.lectureData.videoUrl;
        console.log(this.videoOptions)
      } catch (e) {
        console.log(e)
      }
    },
    setLectureDetail() {
      // videoOptions가 업데이트된 후 loadVideo를 true로 설정하여 VideoPlayer를 렌더링합니다.
      this.loadVideo = true;
    }
  }
};
</script>