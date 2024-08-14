import CreateQuestion from "@/views/qna/CreateQuestion.vue";
import QnaList from "@/views/qna/QnaList.vue";
import CreateAnswer from "@/views/qna/CreateAnswer.vue"
import QnaDetail from "@/views/qna/QnaDetail.vue";


export const qnaRouter = [
    {
        path: '/qna/create',
        name: 'CreateQuestion',
        component: CreateQuestion
    },
    {
        path: '/qna/list',
        name: 'QnaList',
        component: QnaList
    },
    {
        path: '/qna/answer/:id',
        name: 'CreateAnswer',
        component: CreateAnswer
    },
    {
        path: '/qna/detail/:id',
        name: 'QnaDetail',
        component: QnaDetail 
    },
    
]