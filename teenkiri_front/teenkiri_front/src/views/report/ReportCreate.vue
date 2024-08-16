<template>
  <div class="modal-overlay" @click.self="cancelReport">
    <div class="modal-container">
      <h1>신고하기</h1>
      <p>신고할 대상: 
        <span v-if="commentContent">{{ commentContent }}</span> <!-- 댓글 신고 시 댓글 내용 표시 -->
        <span v-else>{{ postTitle }}</span> <!-- 게시글 신고 시 게시글 제목 표시 -->
      </p>
      <div>
        <label>신고 사유:</label>
        <select v-model="selectedReason">
          <option value="ABUSIVE">욕설</option>
          <option value="WALLPAPERS">도배</option>
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
      required: false // 게시글 신고가 아닐 수 있으므로 false로 설정
    },
    postTitle: {
      type: String,
      required: false // 게시글 신고가 아닐 수 있으므로 false로 설정
    },
    postContent: {
      type: String,
      required: false // 게시글 신고가 아닐 수 있으므로 false로 설정
    },
    authorEmail: {
      type: String,
      required: false // 게시글 신고가 아닐 수 있으므로 false로 설정
    },
    postCategory: {
      type: String,
      required: false // 게시글 신고가 아닐 수 있으므로 false로 설정
    },
    commentId: {
      type: Number,
      required: false // 댓글 신고가 아닐 수 있으므로 false로 설정
    },
    commentContent: {
      type: String,
      required: false // 댓글 신고가 아닐 수 있으므로 false로 설정
    },
    commentAuthor: {
      type: String,
      required: false // 댓글 신고가 아닐 수 있으므로 false로 설정
    }
  },
  data() {
    return {
      selectedReason: 'ABUSIVE', // 기본 선택 사유
    };
  },
  created() {
    // 전달된 props 데이터를 콘솔에 출력
    console.log('Received props:');
    console.log('Post ID:', this.postId);
    console.log('Post Title:', this.postTitle);
    console.log('Post Content:', this.postContent);
    console.log('Author Email:', this.authorEmail);
    console.log('Post Category:', this.postCategory);
    console.log('Comment ID:', this.commentId);
    console.log('Comment Content:', this.commentContent);
    console.log('Comment Author:', this.commentAuthor);
  },
  methods: {
    async submitReport() {
      try {
        const reportData = {
          postId: this.postId,
          commentId: this.commentId, // 댓글 ID 추가
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
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
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
