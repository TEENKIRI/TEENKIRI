import UserCreate from "@/views/UserCreate.vue";
import LoginPage from "@/views/LoginPage.vue";
import FindIdPage from "@/views/FindIdPage.vue";
import FindPasswordPage from "@/views/FindPasswordPage.vue";
import ResetPasswordPage from "@/views/ResetPasswordPage.vue";

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
        path: '/find-id',
        name: 'FindIdPage',
        component: FindIdPage
    },
    {
        path: '/find-password',
        name: 'FindPasswordPage',
        component: FindPasswordPage
    },
    {
        path: '/user/reset-password',
        name: 'ResetPasswordPage',
        component: ResetPasswordPage,
        props: route => ({ token: route.query.token }) // URL에서 token 쿼리 파라미터를 가져와 props로 전달
    },
]
