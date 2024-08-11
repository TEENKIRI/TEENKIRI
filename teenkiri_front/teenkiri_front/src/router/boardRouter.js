
import BoardList from "@/views/BoardList.vue";
import BoardCreate from "@/views/BoardCreate.vue";
import BoardUpdate from "@/views/BoardUpdate.vue";
import BoardDetail from "@/views/BoardDetail.vue";

export const boardRouter = [
    {
        path: '/board/:category/list',
        name: 'BoardList',
        component: BoardList
    },
    {
        path: '/board/create',
        name: 'BoardCreate',
        component: BoardCreate,
    },
    {
        path: '/board/update/:category/:id',
        name: 'BoardUpdate',
        component: BoardUpdate
    },
      
    {
        path: '/board/Detail/:category/:id',
        name: 'BoardDetail',
        component: BoardDetail,
    },

]