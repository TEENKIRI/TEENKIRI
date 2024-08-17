<template>
  <div class="sidebar" :class="{ 'is-active': isActive }">
    <nav>
      <ul>
        <li v-for="item in menuItems" :key="item.name">
          <router-link :to="getLink(item.link)">{{ item.name }}</router-link>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script>
export default {
  name: 'SidebarComponent',
  data() {
    return {
      isActive: false,
      menuItems: [
        { name: '내 정보 수정', link: '/user/edit-info' },
        { name: '수강 목록', link: '/my/subject/:id' },
        { name: '찜 강좌', link: '/user/wishlist' },
        { name: '나의 질문', link: '/contact' },
        { name: '알림 내역', link: '/contact' },
      ]
    };
  },
  methods: {
    toggleSidebar() {
      this.isActive = !this.isActive;
    },
    getLink(link) {
      if (link.includes(':id')) {
        // 예를 들어, `id`가 123인 경우:
        return { path: link.replace(':id', 123) };
      }
      return link;
    }
  }
}
</script>

<style scoped>
.sidebar {
  position: fixed;
  right: 0; /* 사이드바를 오른쪽에 고정 */
  top: 64px; /* 헤더 아래로 위치 조정, 헤더 높이에 따라 조정 필요 */
  width: 250px;
  height: calc(100% - 64px); /* 사이드바의 높이를 전체 높이에서 헤더 높이만큼 제외 */
  background-color: #fff; /* 배경 색상 하얀색으로 변경 */
  color: #333; /* 텍스트 색상 */
  transform: translateX(100%); /* 사이드바가 화면 오른쪽 밖에 위치하도록 설정 */
  transition: transform 0.3s ease;
  z-index: 1000; /* 헤더보다 위에 표시되도록 설정 */
  font-size: 16px; /* 폰트 사이즈를 16px로 설정 */
}

.sidebar.is-active {
  transform: translateX(0); /* 사이드바가 화면에 나타나도록 설정 */
}

.sidebar nav ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.sidebar nav ul li {
  padding: 15px 20px;
}

.sidebar nav ul li a {
  color: #333;
  text-decoration: none;
  display: block;
  font-size: 18px; /* 링크 텍스트 폰트 사이즈를 18px로 설정 */
}

.sidebar nav ul li a:hover {
  background-color: #f1f1f1; /* 링크에 마우스를 올렸을 때 배경색 */
}
</style>
