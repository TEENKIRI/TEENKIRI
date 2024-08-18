<template>
  <div class="sidebar" :class="{ 'is-active': isActive }">
    <nav class="nav-content">
      <ul>
        <li v-for="item in menuItems" :key="item.name">
          <router-link :to="getLink(item.link)">{{ item.name }}</router-link>
        </li>
      </ul>
    </nav>
    <div class="sidebar-footer">
      <button @click="doLogout">로그아웃</button>
    </div>
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
        { name: '나의 질문', link: '/qna/my' },
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
    },
    doLogout() {
      localStorage.removeItem('role');
      localStorage.removeItem('token');
      this.$router.push('/login');
      console.log('Logged out');
      window.location.reload();
    }
  }
}
</script>

<style scoped>
.sidebar {
  position: fixed;
  right: 0;
  top: 64px;
  width: 250px;
  height: calc(100% - 64px);
  background-color: #fff;
  color: #333;
  transform: translateX(100%);
  transition: transform 0.3s ease;
  z-index: 1000;
  display: flex;
  flex-direction: column;
}

.sidebar.is-active {
  transform: translateX(0);
}

.nav-content {
  flex: 1;
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
  font-size: 18px;
}

.sidebar nav ul li a:hover {
  background-color: #f1f1f1;
}

.sidebar-footer {
  padding: 10px 10px;
  border-top: 1px solid #ddd;
  background-color: #f9f9f9;
}

.sidebar-footer button {
  width: 100%;
  padding: 10px 20px;
  border: none;
  background: none;
  font-size: 18px;
  cursor: pointer;
  color: #333;
  text-align: left;
}

.sidebar-footer button:hover {
  background-color: #f1f1f1; 
}
</style>
