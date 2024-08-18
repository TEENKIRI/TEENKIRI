import LectureCreate from "@/views/lecture/LectureCreate.vue";
import LectureDetail from "@/views/lecture/LectureDetail.vue";
import SubjectDetail from "@/views/subject/SubjectDetail.vue";
import SubjectList from "@/views/subject/SubjectList.vue";
import SubjectRecommendList from "@/views/subject/SubjectRecommendList.vue";
import SubjectQna from "@/views/subject/SubjectQna.vue";
import SubjectReview from "@/views/subject/SubjectReview.vue";

export const subjectRouter = [
    {
        path: '/subject/list',
        name: 'SubjectList',
        component: SubjectList
    },    
    {
        path: '/subject/recommend/list',
        name: 'SubjectRecommendList',
        component: SubjectRecommendList
    },
    {
        path: '/subject/detail/:id',
        name: 'SubjectDetail',
        component: SubjectDetail
    },
    {
        path: '/subject/:id/qna',
        name: 'SubjectQna',
        component: SubjectQna
    },
    {
        path: '/subject/:id/review',
        name: 'SubjectReview',
        component: SubjectReview
    },
    {
        path: '/lecture/create', // 생성용
        name: 'LectureCreate',
        component: LectureCreate
    },
    {
        path: '/lecture/create/:id', // 수정용
        name: 'LectureCreate',
        component: LectureCreate
    },
    {
        path: '/lecture/detail/:id',
        name: 'LectureDetail',
        component: LectureDetail
    },
]