function initState(){
    return {
        token: "", // user의 token
        role: "",  // user의 권한
        userId: "", // user의 고유한 id
        email: "",  // user의 email
    };
}

const user = {
    state: initState(),
    mutations: {
        setUserAllInfo(state) {
            state.token = localStorage.getItem('token');
            state.role = localStorage.getItem('role');
            state.userId = localStorage.getItem('userId');
            state.email = localStorage.getItem('email');
            console.log('User ID set in Vuex:', state.userId);
            console.log('User Email set in Vuex:', state.email);
        }
    },
    actions: {
        setUserAllInfoActions({ commit }) {
            commit('setUserAllInfo');
        }
    },
    getters: {
        getUserObj: state => state,
        getUserToken: state => state.token,
        getUserRole: state => state.role,
        getUserId: state => state.userId,
        getUserEmail: state => state.email,
    }
}

export default user;
