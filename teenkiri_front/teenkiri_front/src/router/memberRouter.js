import UserCreate from "@/views/user/UserCreate.vue";
import LoginPage from "@/views/user/LoginPage.vue";
import FindIdPage from "@/views/user/FindIdPage.vue";
import FindPasswordPage from "@/views/user/FindPasswordPage.vue";
import UserList from "@/views/user/UserList.vue";
import ResetPasswordPage from "@/views/user/ResetPasswordPage.vue";
import UpdateUserInfo from "@/views/user/UpdateUserInfo.vue";
import ReportCreate from "@/views/report/ReportCreate.vue";

export const memberRouter = [
    {
        path: '/user/create',
        name: 'UserCreate',
        component: UserCreate
    },
    {
        path: '/login',
        name: 'LoginPage',
        component: LoginPage
    },
    {
        path: '/user/find-id',
        name: 'FindIdPage',
        component: FindIdPage
    },
    {
        path: '/user/find-password',
        name: 'FindPasswordPage',
        component: FindPasswordPage
    },
    {
        path: '/user/list',
        name: 'UserList',
        component: UserList
    },
    {
        path: '/user/reset-password',
        name: 'ResetPasswordPage',
        component: ResetPasswordPage,
        props: route => ({ token: route.query.token })
    },
    {
        path: '/user/edit-info',
        name: 'UpdateUserInfo',
        component: UpdateUserInfo,
    },
    {
        path: '/report',
        name: 'ReportCreate',
        component: ReportCreate,
        props: route => ({
          // 기존 게시글 정보
          postId: route.query.postId,

          // 댓글 정보
          commentId: route.query.commentId, 

          // QnA 관련 추가 정보
          qnaId: route.query.qnaId, // 질문글 ID
        })
    },
    {
        path: '/user/wishlist',
        name: 'WishListPage',
        component: WishListPage,
    },
    {
        path: '/user/qna/list',
        name: 'myQnAListPage',
        component: myQnAListPage
      }

]
