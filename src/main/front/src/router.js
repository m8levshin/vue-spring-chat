import Vue from "vue";
import Router from "vue-router";

import Chat from "./views/Chat";
import Auth from "./views/Auth";
import Reg from "./views/Reg";

Vue.use(Router);

export default new Router({
    mode: "history",
    base: process.env.BASE_URL,
    routes: [
        {path: "/", name: "Chat", component: Chat},
        {path: "/auth", name: "Auth", component: Auth},
        {path: "/reg", name: "Reg", component: Reg}
    ]
});
