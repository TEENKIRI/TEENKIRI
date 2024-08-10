
import EventBoard from "@/views/EventBoard.vue";
import BoardCreate from "@/views/BoardCreate.vue";
import BoardUpdate from "@/views/BoardUpdate.vue";
import BoardDetail from "@/views/BoardDetail.vue";

export const boardRouter = [
    {
        path: '/board/event',
        name: 'EventBoard',
        component: EventBoard
    },
    {
        path: '/board/create',
        name: 'BoardCreate',
        component: BoardCreate,
    },
    {
        path: '/board/Update/:id',
        name: 'BoardUpdate',
        component: BoardUpdate,
    },
    {
        path: '/board/Detail/:id',
        name: 'BoardDetail',
        component: BoardDetail,
    },

]