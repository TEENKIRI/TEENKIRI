<template>
  <div class="modal-overlay" @click.self="cancelReport">
    <div class="modal-container">
      <h1 class="modal-title">
        <!-- 제목을 동적으로 변경 -->
        <template v-if="postId">게시글 신고하기</template>
        <template v-else-if="qnaId">질문글 신고하기</template>
        <template v-else-if="commentId">댓글 신고하기</template>
      </h1>

      <div class="reason-section">
        <label class="label">신고 사유</label>
        <v-radio-group v-model="selectedReason">
          <v-radio
            label="스팸홍보/도배글입니다."
            value="SPAM"
            @change="toggleDetails('SPAM')"
          ></v-radio>
          <div v-show="showDetails['SPAM']" class="details">
            <p>-사행성 오락이나 도박을 홍보하거나 권장하는 내용 등의 부적절한 스팸 홍보 행위</p>
            <p>-동일하거나 유사한 내용 반복 게시</p>
          </div>

          <v-radio
            label="음란물입니다."
            value="PORNOGRAPHY"
            @change="toggleDetails('PORNOGRAPHY')"
          ></v-radio>
          <div v-show="showDetails['PORNOGRAPHY']" class="details">
            <p>-성적 수치심을 일으키는 내용</p>
            <p>-아동이나 청소년을 성 대상화한 표현</p>
            <p>-과도하거나 의도적인 신체 노출</p>
            <p>-음란한 행위와 관련된 부적절한 내용</p>
          </div>

          <v-radio
            label="불법정보를 포함하고 있습니다."
            value="ILLEGAL_INFORMATION"
            @change="toggleDetails('ILLEGAL_INFORMATION')"
          ></v-radio>
          <div v-show="showDetails['ILLEGAL_INFORMATION']" class="details">
            <p>-불법 행위, 불법 링크에 대한 정보 제공</p>
            <p>-불법 상품을 판매하거나 유도하는 내용</p>
          </div>

          <v-radio
            label="청소년에게 유해한 내용입니다."
            value="HARMFUL_TO_MINORS"
            @change="toggleDetails('HARMFUL_TO_MINORS')"
          ></v-radio>
          <div v-show="showDetails['HARMFUL_TO_MINORS']" class="details">
            <p>-가출/왕따/학교폭력/자살 등 청소년에게 부정적인 영향을 조성하는 내용</p>
          </div>

          <v-radio
            label="욕설/생명경시/혐오/차별적 표현입니다."
            value="ABUSIVE_LANGUAGE"
            @change="toggleDetails('ABUSIVE_LANGUAGE')"
          ></v-radio>
          <div v-show="showDetails['ABUSIVE_LANGUAGE']" class="details">
            <p>-직·간접적인 욕설을 사용하여 타인에게 모욕감을 주는 내용</p>
            <p>-생명을 경시하거나 비하하는 내용</p>
            <p>-계층/지역/종교/성별 등을 혐오하거나 비하하는 표현</p>
            <p>-신체/외모/취향 등을 경멸하는 표현</p>
          </div>

          <v-radio
            label="개인정보 노출 게시물입니다."
            value="PRIVACY_VIOLATION"
            @change="toggleDetails('PRIVACY_VIOLATION')"
          ></v-radio>
          <div v-show="showDetails['PRIVACY_VIOLATION']" class="details">
            <p>-법적으로 중요한 타인의 개인정보를 게시</p>
            <p>-당사자 등의 없는 특정 개인을 인지할 수 있는 정보</p>
          </div>

          <v-radio
            label="불쾌한 표현이 있습니다."
            value="OFFENSIVE_CONTENT"
            @change="toggleDetails('OFFENSIVE_CONTENT')"
          ></v-radio>
        </v-radio-group>
      </div>

      <div class="details-section">
        <label class="label">상세 내용</label>
        <textarea
          v-model="reportDetails"
          placeholder="내용을 입력하세요. (선택)"
          class="details-textarea"
        ></textarea>
      </div>

      <div class="actions">
        <button @click="submitReport" class="submit-button">등록</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  props: {
    postId: {
      type: Number,
      required: false,
    },
    qnaId: {
      type: Number,
      required: false,
    },
    commentId: {
      type: Number,
      required: false,
    },
  },
  data() {
    return {
      selectedReason: "SPAM", // 기본 선택 사유를 '스팸홍보/도배글입니다.'로 설정
      reportDetails: "",
      showDetails: {
        SPAM: false,
        PORNOGRAPHY: false,
        ILLEGAL_INFORMATION: false,
        HARMFUL_TO_MINORS: false,
        ABUSIVE_LANGUAGE: false,
        PRIVACY_VIOLATION: false,
        OFFENSIVE_CONTENT: false,
      },
    };
  },
  methods: {
    toggleDetails(reason) {
      this.showDetails = {
        SPAM: false,
        PORNOGRAPHY: false,
        ILLEGAL_INFORMATION: false,
        HARMFUL_TO_MINORS: false,
        ABUSIVE_LANGUAGE: false,
        PRIVACY_VIOLATION: false,
        OFFENSIVE_CONTENT: false,
      };
      this.showDetails[reason] = true;
    },
    async submitReport() {
      try {
        const reportData = {
          qnaId: this.qnaId || null,
          postId: this.postId || null,
          commentId: this.commentId || null,
          reason: this.selectedReason,
          details: this.reportDetails,
        };

        console.log("Submitting report with data:", reportData);

        await axios.post(
          `${process.env.VUE_APP_API_BASE_URL}/report/create`,
          reportData
        );
        alert("신고가 성공적으로 접수되었습니다.");
        this.cancelReport();
      } catch (error) {
        console.error("신고를 제출하는 데 실패했습니다:", error);
        console.log(this.reportData)
        alert("신고를 제출하는 데 실패했습니다.");
      }
    },
    cancelReport() {
      this.$emit("close");
    },
  },
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-container {
  background-color: #1c1c1c;
  padding: 20px;
  border-radius: 8px;
  max-width: 500px;
  width: 100%;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
  color: #fff;
}

.modal-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #fff;
}

.reason-section {
  margin-bottom: 20px;
}

.details-section {
  margin-bottom: 20px;
}

.label {
  font-size: 14px;
  margin-bottom: 10px;
  display: block;
  color: #bbb;
}

.details {
  padding-left: 24px;
  font-size: 12px;
  color: #bbb;
}

.details-textarea {
  width: 100%;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #555;
  background-color: #333;
  color: #fff;
  font-size: 14px;
  resize: none;
  height: 80px;
}

.actions {
  display: flex;
  justify-content: center;
}

.submit-button {
  background-color: #1f87e9;
  padding: 10px 20px;
  font-size: 16px;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>
