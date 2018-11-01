import props from "../../properties";
import axios from "axios";

const chatModule = {
    namespaced: true,
    state: {
        events: [],
        onlineUsers: []
    },

    mutations: {
        SET_EVENTS(state, events) {
            state.events = events;
        },

        SET_ONLINE_USERS(state, onlineUsers) {
            state.onlineUsers = onlineUsers;
        }
    },

    actions: {
        getLastEvents(context) {
            const endPoint = props.REMOTE_API + "/events/";
            return axios
                .get(endPoint)
                .then(response => {
                    context.commit("SET_EVENTS", response.data);
                })
                .catch(erorr => erorr);
        },

        getOnlineUsers(context) {
            const endPoint = props.REMOTE_API + "/users/";
            return axios
                .get(endPoint)
                .then(response => {
                    context.commit("SET_ONLINE_USERS", response.data);
                })
                .catch(erorr => erorr);
        },

        sendMessage({dispatch, commit, getters, root}, message) {
            const messageDto = {
                message
            };

            const endPoint = props.REMOTE_API + "/messages/";
            return axios.post(endPoint, messageDto).then(response => {
                dispatch("refreshChat", null, {root: true});
            });
        }
    }
};

export default chatModule;
