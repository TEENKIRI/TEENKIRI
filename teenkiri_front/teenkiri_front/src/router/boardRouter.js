import BoardList from "@/views/board/BoardList.vue";
import BoardCreate from "@/views/board/BoardCreate.vue";
import BoardUpdate from "@/views/board/BoardUpdate.vue";
import BoardDetail from "@/views/board/BoardDetail.vue";


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
