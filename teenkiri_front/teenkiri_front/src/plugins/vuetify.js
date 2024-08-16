import 'vuetify/styles';
import {createVuetify} from 'vuetify';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';
import {mdi} from 'vuetify/iconsets/mdi';

export default createVuetify({
    // components는 뷰티파이에서 사용할 수 있는 UI 컴포넌트들을 의미
    components,

    // directives는 뷰타파이에서 HTML 요소에 행동을 부여하는데 사용
    directives, 
    icons: {
        defaultSet: 'mdi',
        sets:{
            mdi, 
        }
    }
});