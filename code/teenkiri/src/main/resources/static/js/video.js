var MovieModule = (function () {

    var maxTime = 0;
    var video_end = false;
    var currentTime = 0;
    var is_post_watch_time = true;
    var post_watch_time = 0
    var myPlayer
    var USED_NUM_ARR = []
    var is_first_play = true;
    var CHANGE_TIMER;

  function init() {
    settingVideo();
  }

  function settingVideo() {
    videojs("lecture_movie", {
      width: "960"
    }).ready(function () {
      myPlayer = this;
      // myPlayer.play();
      myPlayer.on("loadedmetadata", function (e) {
        myPlayer.currentTime(currentTime);
        var duration = Math.floor(myPlayer.duration());
        var min = Math.floor(duration / 60);
        var sec = duration - 60 * min;
      });


      myPlayer.on("seeking", function (event) {
        if (maxTime < Math.floor(myPlayer.currentTime()) && !video_end) {
          console.log("***1***", maxTime, myPlayer.currentTime());
          clearInterval(CHANGE_TIMER);
          myPlayer.paused();
          myPlayer.currentTime(maxTime - 1);
          // alert('시청하신 시간보다 앞으로가기는 하실 수 없습니다.');
          myPlayer.play();
          updateCurrentTime();
          console.log("@@@@@@@@@@@@@@@@@@@@",currentTime)
          // console.log('seeked maxTime :: ',currentTime,maxTime,myPlayer.currentTime())
        }
      });

      myPlayer.on("seeked", function (event) {
        // if(!video_end){
        // console.log(maxTime > myPlayer.currentTime(),' || ',maxTime < myPlayer.currentTime(),' || ',currentTime < myPlayer.currentTime())
        if (maxTime < Math.floor(myPlayer.currentTime()) && !video_end) {
          console.log("***2***", maxTime, myPlayer.currentTime());
          clearInterval(CHANGE_TIMER);
          myPlayer.paused();
          myPlayer.currentTime(maxTime - 1);
          // alert('시청하신 시간보다 앞으로가기는 하실 수 없습니다.');
          myPlayer.play();
          updateCurrentTime();
        }
      });

      myPlayer.on("ended", function (event) {
        // console.log("video end!");
        video_end = true;
        currentTime = Math.floor(myPlayer.currentTime());
        maxTime = Math.floor(currentTime);
        if (Math.abs(post_watch_time - maxTime) > 10) {
          alert("시청한 시간보다 앞으로가기는 하실 수 없습니다.");
          myPlayer.currentTime(post_watch_time);
          maxTime = post_watch_time;
          is_post_watch_time = true;
          return false;
        }
        updateWatchTime();
        is_post_watch_time = false;
      });

      myPlayer.on("error", function (event) {
        // console.log("비디오 송출 에러!!", event);
        if (USED_NUM_ARR.length >= 3) {
          myPlayer.pause();
          alert(
            "사용자 증가로 접속이 원활하지 않습니다.\n잠시 후 다시 시도해주세요. 감사합니다."
          );
          location.replace("./list.html");
          console.log(event.message);
          return false;
        } else {
          checkAnotherUrl();
        }
      });
      myPlayer.on("play", function (e) {
        if (is_first_play === true) {
          is_first_play = false;
          myPlayer.currentTime(currentTime);
        }
      });

      var update_time_event = setInterval(function () {
        if (is_post_watch_time === false) {
          clearInterval(update_time_event);
          return false;
        }
        if (!myPlayer.paused()) {
          updateWatchTime();
        }
      }, 10000);

      // $("video").attrchange({
      //   trackValues: true,
      //   callback: function (e) {
      //     var _this = $(this);
      //     if (_this.attr("controls")) {
      //       _this.attr("controls", false);
      //     }
      //     return false;
      //   },
      // });
    });
    videojs.options.autoplay = true;
    updateCurrentTime();
    // 출처 - https://blog.naver.com/nanan75/221657098392
    // 앞으로감기시, 최대로 본곳까지는 앞으로 감기 가능하도록 maxTime으로 변경
  }

  function updateCurrentTime() {
    CHANGE_TIMER = setInterval(function () {
      if (!myPlayer.paused()) {
        currentTime = Math.floor(myPlayer.currentTime());
        if (currentTime > maxTime) {
          maxTime = Math.floor(currentTime);
        }
      }
    }, 1000);
  }

  return {
    init: init,
  };
})();
(function () {
  MovieModule.init();
})();
