
import UserCreate from "@/views/UserCreate.vue";
import LoginPage from "@/views/LoginPage.vue";
import FindIdPage from "@/views/FindIdPage.vue";
import FindPasswordPage from "@/views/FindPasswordPage.vue";
import UserList from "@/views/UserList.vue";
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
        path: '/user/list',
        name: 'UserList',
        component: UserList
    },
]