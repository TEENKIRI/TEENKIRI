import UserCreate from "@/views/user/UserCreate.vue";
import LoginPage from "@/views/user/LoginPage.vue";
import FindIdPage from "@/views/user/FindIdPage.vue";
import FindPasswordPage from "@/views/user/FindPasswordPage.vue";
import UserList from "@/views/user/UserList.vue";
import ResetPasswordPage from "@/views/user/ResetPasswordPage.vue";

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
    }
]
