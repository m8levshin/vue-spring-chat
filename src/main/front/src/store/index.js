import Vue from "vue";
import Vuex from "vuex";
import chatModule from "./module/chat";

import props from "../properties";
import axios from "axios";

Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        chat: chatModule
    },

    state: {
        userSession: {}
    },

    getters: {
        isLogin: state => Object.keys(state.userSession).length !== 0
    },

    mutations: {
        SET_USER_SESSION(state, userSession) {
            state.userSession = userSession;
        }
    },

    actions: {
        refreshChat({dispatch}) {
            dispatch("chat/getLastEvents");
            dispatch("chat/getOnlineUsers");
        },

        getUserSession(context) {
            const endPoint = props.REMOTE_API + "/sessions/";

            return axios.get(endPoint).then(response => {
                context.commit("SET_USER_SESSION", response.data);
            });
        },

        authUser(context, {userName, password}) {
            const endPoint = props.REMOTE_API + "/sessions/";

            const authDto = {
                userName: userName,
                password: password
            };

            return axios.post(endPoint, authDto).then(response => {
                context.commit("SET_USER_SESSION", response.data);
            });
        },

        regUser(context, {userName, password, repeatPassword}) {
            const endPoint = props.REMOTE_API + "/users/";

            const regDto = {
                userName: userName,
                password: password,
                repeatPassword: repeatPassword
            };
            console.log(regDto);

            return axios.post(endPoint, regDto).then(response => {
                context.commit("SET_USER_SESSION", response.data);
            });
        },

        logOut({commit, dispatch}) {
            const endPoint = props.REMOTE_API + "/sessions/";
            return axios.delete(endPoint).then(() => {
                commit("SET_USER_SESSION", {});
                dispatch("refreshChat");
            });
        },

        invalidateSession(context) {
            context.commit("SET_USER_SESSION", {});
        }
    }
});
