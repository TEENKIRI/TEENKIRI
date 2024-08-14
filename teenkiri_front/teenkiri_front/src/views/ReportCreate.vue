<template>
  <div class="modal-overlay" @click.self="cancelReport">
    <div class="modal-container">
      <h1>게시글 신고하기</h1>
      <p>신고할 게시글: {{ postTitle }}</p>
      <div>
        <label>신고 사유:</label>
        <select v-model="selectedReason">
          <option value="ABUSIVE">욕설</option>
          <option value="SPAM">도배</option>
          <option value="ADVERTISING">광고</option>
          <option value="OTHER">기타</option>
        </select>
      </div>
      <div class="actions">
        <button @click="submitReport">신고 제출</button>
        <button @click="cancelReport">취소</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  props: {
    postId: {
      type: Number,
      required: true
    },
    postTitle: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      selectedReason: 'ABUSIVE', // 기본 선택 사유
    };
  },
  methods: {
    async submitReport() {
      try {
        const reportData = {
          postId: this.postId,
          reason: this.selectedReason,
        };

        await axios.post(`${process.env.VUE_APP_API_BASE_URL}/report/create`, reportData);
        alert('신고가 성공적으로 접수되었습니다.');
        this.cancelReport();
      } catch (error) {
        console.error('신고를 제출하는 데 실패했습니다:', error);
        alert('신고를 제출하는 데 실패했습니다.');
      }
    },
    cancelReport() {
      this.$emit('close'); // 부모 컴포넌트로 close 이벤트 전송
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
  background-color: rgba(0, 0, 0, 0.5); /* 배경을 어둡게 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000; /* 다른 요소보다 앞에 위치 */
}

.modal-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  max-width: 400px;
  width: 100%;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

.modal-container h1 {
  font-size: 24px;
  margin-bottom: 20px;
}

.modal-container p {
  font-size: 16px;
  margin-bottom: 10px;
}

.modal-container label {
  font-size: 14px;
  margin-right: 10px;
}

.modal-container select {
  padding: 5px;
  font-size: 14px;
}

.actions {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
}

.actions button {
  padding: 10px 20px;
  font-size: 14px;
  cursor: pointer;
  border: none;
  border-radius: 4px;
  color: white;
}

.actions button:first-child {
  background-color: #007bff;
}

.actions button:last-child {
  background-color: #ccc;
  color: black;
}
</style>
