
import UserCreate from "@/views/UserCreate.vue";
import LoginPage from "@/views/LoginPage.vue";

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

]