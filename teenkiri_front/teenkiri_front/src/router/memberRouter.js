import UserCreate from "@/views/user/UserCreate.vue";
import LoginPage from "@/views/user/LoginPage.vue";
import FindIdPage from "@/views/user/FindIdPage.vue";
import FindPasswordPage from "@/views/user/FindPasswordPage.vue";
import UserList from "@/views/user/UserList.vue";
import ResetPasswordPage from "@/views/user/ResetPasswordPage.vue";
import UpdateUserInfo from "@/views/user/UpdateUserInfo.vue";
import ReportCreate from "@/views/report/ReportCreate.vue";
import SubscribePage from "@/views/user/SubscribePage.vue";
import WishListPage from "@/views/user/WishListPage.vue";

import UserNotificaiton from "@/views/user/UserNotificaion.vue"
import { jwtDecode } from "jwt-decode";
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
        path: '/my/subject/:id',
        name: 'SubscribePage',
        component: SubscribePage
    },
    {
        path: '/user/wishlist',
        name: 'WishListPage',
        component: WishListPage,
    },
    {
        path: '/user/notification',
        name: 'UserNotificaiton',
        component: UserNotificaiton
    },
    // {
    //     path: '/loginSuccess',
    //     component: LoginPage,
    //     beforeEnter: (to, from, next) => {
    //         const accessToken = to.query.token;
    //         const refreshToken = to.query.refreshToken; // 쿼리에서 리프레시 토큰도 가져옵니다.
    //         if (accessToken && refreshToken) {
    //             const decoded = jwtDecode(accessToken);
    //             localStorage.setItem("token", accessToken);
    //             localStorage.setItem("refreshToken", refreshToken); // 리프레시 토큰을 로컬 스토리지에 저장합니다.
    //             localStorage.setItem("email", decoded.email);
    //             localStorage.setItem("role", decoded.role);
    //             window.location.href = "/";
    //         } else {
    //             next('/login'); // 토큰이 없다면 로그인 페이지로 리다이렉트
    //         }
    //     }
    // }
    {
        path: '/loginSuccess',
        component: LoginPage,
        beforeEnter: (to, from, next) => {
            const accessToken = to.query.token; // 쿼리 파라미터에서 토큰을 가져옴
            // 쿼리 파라미터에서 리프레시 토큰도 가져오려면 아래와 같이 사용합니다.
            const refreshToken = to.query.refreshToken;
    
            if (accessToken) {
                try {
                    // JWT 토큰을 디코딩하여 사용자 정보를 추출합니다.
                    const decoded = jwtDecode(accessToken);
                    // 로컬 스토리지에 토큰과 사용자 정보를 저장합니다.
                    localStorage.setItem("token", accessToken);
                    if (refreshToken) {
                        localStorage.setItem("refreshToken", refreshToken);
                    }
                    localStorage.setItem("userId", decoded.userId)
                    localStorage.setItem("email", decoded.sub); // 이메일은 JWT에서 'sub' 클레임으로 보통 저장됩니다.
                    localStorage.setItem("role", decoded.role);
    
                    // 홈 페이지로 리다이렉트합니다.
                    window.location.href = "/";
                } catch (error) {
                    console.error("Invalid token", error);
                    next('/login'); // 토큰이 유효하지 않은 경우 로그인 페이지로 리다이렉트
                }
            } else {
                next('/login'); // 토큰이 없다면 로그인 페이지로 리다이렉트
            }
        }
    }

]
