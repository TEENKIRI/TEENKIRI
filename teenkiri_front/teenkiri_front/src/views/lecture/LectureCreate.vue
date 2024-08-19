<template>
  <v-app>
    <v-container>
      <v-card class="mt-5 mx-auto" max-width="800">
        <v-card-title>
          <h3>강의 관리</h3>
        </v-card-title>
        <v-card-text>
          <v-form ref="form" @submit.prevent="submitForm">
            <v-row>
            <!-- 강좌명 입력 -->
              <v-col cols="12">
                <v-text-field
                  label="강좌명"
                  v-model="lectureData.subjectTitle"
                  disabled="true"
                  required
                />
              </v-col>
            </v-row>
            <v-row>
              <!-- 강의명 입력 -->
              <v-col cols="12">
                <v-text-field
                  label="강의명"
                  v-model="lectureData.title"
                  required
                />
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="6">
                <h3>비디오 업로드</h3>
                <v-row>
                  <v-col cols="12">
                  <!-- 파일 첨부 -->
                    <v-file-input
                      @change="onFileVideoChange"
                      label="비디오 파일첨부"
                      accept="video/mp4"
                    />
                    <!-- 미리보기 이미지 -->
                    <div>
                      <video v-if="previewVideoSrc" :src="previewVideoSrc" max-width="200" class="my-3 w-100" controls />
                    </div>
                  </v-col>
                </v-row>
              </v-col>
              <v-col cols="6">
                <h3>규격</h3>
                <v-list lines="one">
                  <v-list-item>
                    mp4 형식의 비디오만 업로드 됩니다.
                  </v-list-item>
                  <v-list-item>
                    최대 4GB 영상만 업로드 됩니다.
                  </v-list-item>
                  <v-list-item>
                    기타 문의사항 발생 시 아래 연락처로 연락 바랍니다.<br>
                    070-1111-2222 (09:00-18:00 / 월-금)
                  </v-list-item>
                  <v-list-item
                    class="font-weight-bold"
                  >
                    주의<br>
                    기존 학생 시청 데이터 존재 시 데이터가 <span class="text-red-lighten-1">초기화</span> 되오니 참고 바랍니다. 
                  </v-list-item>
                </v-list>
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="6">
                <h3>비디오 썸네일 업로드</h3>
                <v-row>
                  <v-col cols="12">
                  <!-- 파일 첨부 -->
                    <v-file-input
                      @change="onFileChange"
                      label="이미지 파일첨부"
                      accept="image/*"
                    />
                    <!-- 미리보기 이미지 -->
                    <div>
                      <v-img v-if="previewImageSrc" :src="previewImageSrc" max-width="200" class="my-3"/>
                    </div>
                  </v-col>
                </v-row>
              </v-col>
              <v-col cols="6">
                <h3>규격</h3>
                <v-list lines="one">
                  <v-list-item>
                    png, jpg, jpeg, gif 형식의 이미지만 업로드 됩니다.
                  </v-list-item>
                  <v-list-item>
                    기타 문의사항 발생 시 아래 연락처로 연락 바랍니다.<br>
                    070-1111-2222 (09:00-18:00 / 월-금)
                  </v-list-item>
                  <v-list-item
                    class="font-weight-bold"
                  >
                    주의<br>
                    <span class="text-red-lighten-1">16:9 사이즈</span>의 이미지 업로드를 권장드립니다.  
                  </v-list-item>
                </v-list>
              </v-col>
            </v-row>
            <div class="btnWrap">
              <!-- <v-btn text @click="cancel">취소</v-btn> -->
              <v-btn color="primary" type="submit" class="">저장</v-btn>
            </div>
          </v-form>
        </v-card-text>
      </v-card>
      
    </v-container>
  </v-app>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      user: {
        token: "",
        userId: "",
        email: "",
        nickname: "",
      },


      subjectId: this.$route.params.subjectId,
      lectureId: this.$route.params.id,

      routeName : this.$route.name,

      lectureData : {
        title: "",
        subjectId : "",
        videoDuration : "",
        video : null,
        image : null
      },

      subjectData:{},


      image: null,
      previewImageSrc: null,
      video: null,
      previewVideoSrc: null,
    };
  },
  async created() {
    try {
      await this.$store.dispatch("setUserAllInfoActions");
      this.user = this.$store.getters.getUserObj;

      if (this.user.token === "") {
        alert("로그인이 필요합니다.");
        location.href = -1;
      }else{
        this.lectureId = (this.lectureId == undefined || this.lectureId == "") ? "" : this.lectureId;
        console.log("lecture 아이디 >> ", this.lectureId)
        console.log("subjectId 아이디 >> ", this.subjectId)
        console.log("routeName 라우터 이름 >> ",this.routeName)
        if(this.routeName === "LectureCreate"){ // 생성용 라우터
          this.getSubjectDetail();
        }
        this.getLectureDetail();
      }
    } catch (error) {
      console.error("사용자 정보를 가져오는 중 오류가 발생했습니다:", error);
    }
  },
  methods: {
    async submitForm() {
      const formData = new FormData();
      formData.append('title', this.lectureData.title);
      formData.append('subjectId', this.subjectId);

      if (this.image != null) {
        formData.append('image', this.image);
      }

      if (this.video != null) {
        formData.append('video', this.video);
        formData.append('videoDuration', this.lectureData.videoDuration);
      }

      try {
        let response = null;
        let apiUrl = `${process.env.VUE_APP_API_BASE_URL}/lecture/create`;
        if(this.routeName === "LectureEdit"){
          apiUrl = `${process.env.VUE_APP_API_BASE_URL}/lecture/update/${this.lectureId}`;
          response = await axios.patch(
            apiUrl, formData,
            {
              headers: {
                'Content-Type': 'multipart/form-data',
              },
            }
          );
          alert('강의가 성공적으로 수정되었습니다!');
        }else{
          response = await axios.post(
            apiUrl, formData,
            {
              headers: {
                'Content-Type': 'multipart/form-data',
              },
            }
          );
          alert('강의가 성공적으로 등록되었습니다!');
        }


        console.log(response);
        
        // history.go(-1);

      } catch (error) {
        console.error('Error details:', error);
      }
    },
    async getLectureDetail(){
      if(this.lectureId != ""){
        try {
          const response = await axios.get(
            `${process.env.VUE_APP_API_BASE_URL}/lecture/detail/${this.lectureId}`
          );
          this.lectureData = response.data.result;
          console.log(this.lectureData);
          this.previewImageSrc = this.lectureData.imageUrl;
          this.previewVideoSrc = this.lectureData.videoUrl;

          this.subjectId = this.lectureData.subjectId;

        } catch (error) {
          console.error("강의 세부 정보 조회 실패:", error);
        }
      }
    },
    async getSubjectDetail(){
      try {
          const response = await axios.get(
            `${process.env.VUE_APP_API_BASE_URL}/subject/detail/${this.subjectId}`
          );
          this.subjectData = response.data.result;
          console.log(this.subjectData);
          this.lectureData.subjectTitle = this.subjectData.title;
        } catch (error) {
          console.error("강의 세부 정보 조회 실패:", error);
        }
    },
    onFileChange(event) {
      const files = event?.target?.files || event?.dataTransfer?.files;
      if (files && files.length > 0) {
        this.image = files[0];
        this.previewImage();
      } else {
        this.image = null;
        this.previewImageSrc = null;
      }
    },
    onFileVideoChange(event) {
      const files = event?.target?.files || event?.dataTransfer?.files;
      console.log(files)
      if (files && files.length > 0) {
        this.video = files[0];
        this.video.onloadedmetadata = () => {
          
        };
        
        this.previewVideo();
      } else {
        this.video = null;
        this.previewVideoSrc = null;
      }
    },
    previewImage() {
      if (this.image) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.previewImageSrc = e.target.result;
        };
        reader.readAsDataURL(this.image);
      } else {
        this.previewImageSrc = null;
      }
    },
    previewVideo() {
      if (this.video) {
        const reader = new FileReader();
        reader.onload = (e) => {
          console.log("로드!", e);
          this.previewVideoSrc = e.target.result;

          const videoElement = document.createElement('video');
          videoElement.src = this.previewVideoSrc;

          videoElement.onloadedmetadata = () => {
            this.lectureData.videoDuration = Math.floor(videoElement.duration); // 소수점 내림으로 고정
          };
        };
        reader.readAsDataURL(this.video);
      } else {
        this.previewVideoSrc = null;
      }
    },
  },
};
</script>

