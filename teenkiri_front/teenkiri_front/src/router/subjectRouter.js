import SubjectDetail from "@/views/subject/SubjectDetail.vue";
import SubjectList from "@/views/subject/SubjectList.vue";

export const subjectRouter = [
    {
        path: '/subject/list',
        name: 'SubjectList',
        component: SubjectList
    },
    {
        path: '/subject/detail/:id',
        name: 'SubjectDetail',
        component: SubjectDetail
    },
]