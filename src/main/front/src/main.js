import Vue from "vue";
import App from "./App";
import router from "./router";
import store from "./store/index";
import VueWait from "vue-wait";

Vue.use(VueWait);

Vue.config.productionTip = false;

Vue.filter("formatDate", function (timestamp) {
    if (timestamp) {
        let date = new Date();
        date.setTime(timestamp);
        return date.toLocaleString();
    }
});

new Vue({
    router,
    store,
    render: h => h(App),
    wait: new VueWait({
        useVuex: true
    })
}).$mount("#app");
