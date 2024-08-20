<template>
  <v-row>
    <v-col class="d-flex justify-center">
      <video ref="videoPlayer" class="video-js vjs-theme-city vjs-16-9 vjs-big-play-centered"></video>
    </v-col>
  </v-row> 
</template>

<script>
import videojs from 'video.js';

export default {
  name: 'VideoPlayer',
  props: {
    options: {
      type: Object,
      default() {
        return {};
      }
    }
  },
  data() {
    return {
      player: null
    }
  },
  mounted() {
    console.log(this.options)
    this.player = videojs(this.$refs.videoPlayer, this.options, () => {
      this.player.log('onPlayerReady', this);
    });
  },
  // beforeDestroy() {
  //   if (this.player) {
  //     this.player.dispose();
  //   }
  // }
}
</script>