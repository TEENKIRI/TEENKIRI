<template>
  <v-container class="mt-5">
    <v-card class="mx-auto" max-width="800">
      <v-card-title>
        <h3>강좌 생성</h3>
      </v-card-title>

      <v-card-text>
        <v-form ref="form" @submit.prevent="submitForm">
          <v-row>
            <!-- 강좌명 입력 -->
            <v-col cols="12">
              <v-text-field
                label="강좌명"
                v-model="subject.title"
                required
              />
            </v-col>

            <!-- 선생님 이메일 선택 -->
            <v-col cols="12">
              <v-select
                v-model="subject.userTeacherEmail"
                :items="teachers"
                label="선생님 이메일"
                item-text="email"
                item-value="email"
                required
              />            
            </v-col>

            <!-- 과목 선택 -->
            <v-col cols="6">
              <v-select
                label="과목"
                v-model="subject.courseId"
                :items="courses"
                item-text="title"
                item-value="id"
                required
              />
            </v-col>

            <!-- 학년 선택 -->
            <v-col cols="6">
              <v-select
                label="학년"
                v-model="subject.grade"
                :items="grades"
                required
              >
              </v-select>
            </v-col>

            <!-- 메인 페이지 상단 노출 여부 -->
            <v-col cols="12">
              <v-checkbox
                v-model="subject.isMainSubject"
                label="메인 페이지 상단 노출"
              />
            </v-col>

            
            <!-- 내용 입력 -->
            <v-col cols="12">
              <v-textarea
                label="내용"
                v-model="subject.description"
                rows="5"
                required
              />
            </v-col>

            <!-- 썸네일 이미지 업로드 -->
            <v-col cols="12">
              <v-file-input
                label="강좌 썸네일 이미지 업로드"
                v-model="subject.thumbnail"
                accept="image/*"
                @change="onFileChange"
              />
              <v-img v-if="previewImageSrc" :src="previewImageSrc" max-width="200" />
            </v-col>

            <!-- 규격 설명 -->
            <v-col cols="12" class="d-flex justify-space-between">
              <v-col cols="6">
                <p class="caption">
                  1. png, jpg, jpeg, gif 형식의 이미지만 업로드 됩니다.<br/>
                  2. 기타 문의사항 발생 시 아래 연락처로 연락 바랍니다.<br/>
                  070-1111-2222 (09:00-18:00 // 점심시간 12:00-13:00)
                </p>
              </v-col>
              <v-col cols="6" class="text-right">
                <p class="caption text-right red--text">
                  * 16:9 사이즈의 이미지 업로드를 권장드립니다.
                </p>
              </v-col>
            </v-col>

            <!-- 버튼들 -->
            <v-col cols="12" class="d-flex justify-space-between">
              <v-btn @click="goBack" outlined>목록으로</v-btn>
              <v-btn type="submit" color="primary">저장</v-btn>
            </v-col>
          </v-row>
        </v-form>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      subject: {
        title: '',
        userTeacherEmail: null,
        courseId: null,
        grade: '',
        description: '',
        thumbnail: null,
        isMainSubject: false,  // 메인 페이지 상단 노출 여부
      },
      teachers: [],
      courses: [],
      grades: ['GRADE_1', 'GRADE_2', 'GRADE_3', 'GRADE_4', 'GRADE_5', 'GRADE_6'], 
      previewImageSrc: null,
    };
  },

  created() {
    this.fetchTeacherList();
    this.fetchCourseList();
  },

  methods: {
    async fetchTeacherList() {
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/user/teachers`);
        this.teachers = response.data.result.map(teacher => teacher.email);
        console.log(this.teachers);
      } catch (error) {
        console.error("Error fetching teachers:", error);
        alert("선생님 정보를 가져오는 데 실패했습니다.");
      }
    },

    async fetchCourseList() {
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/course/list`, {
          params: { page: 0, size: 100, sort: 'id,desc' },
        });
        this.courses = response.data.result.content;
        console.log(this.courses);
      } catch (error) {
        console.error("Error fetching courses:", error);
        alert("과목 정보를 가져오는 데 실패했습니다.");
      }
    },

    onFileChange(event) {
      const files = event.target.files || event.dataTransfer.files;
      if (files && files.length > 0) {
        this.subject.thumbnail = files[0];
        this.previewImage();
      } else {
        this.subject.thumbnail = null;
        this.previewImageSrc = null;
      }
    },

    previewImage() {
      if (this.subject.thumbnail) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.previewImageSrc = e.target.result;
        };
        reader.readAsDataURL(this.subject.thumbnail);
      } else {
        this.previewImageSrc = null;
      }
    },

    async submitForm() {
      const formData = new FormData();
      formData.append('title', this.subject.title);
      formData.append('userTeacherEmail', this.subject.userTeacherEmail);
      formData.append('courseId', this.subject.courseId);
      formData.append('grade', this.subject.grade);
      formData.append('description', this.subject.description);
      formData.append('isMainSubject', this.subject.isMainSubject);  // 추가된 필드

      if (this.subject.thumbnail) {
        formData.append('subjectThum', this.subject.thumbnail);
      }

      try {
        const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/subject/create`, formData, {
          headers: { 'Content-Type': 'multipart/form-data' },
        });
        console.log(response);
        alert('강좌 생성이 완료되었습니다.');
        this.$router.push('/subject/list');
      } catch (error) {
        console.error('Error creating subject:', error);
        alert('강좌 생성에 실패했습니다.');
      }
    },

    goBack() {
      this.$router.push('/subject/list');
    },
  },
};
</script>