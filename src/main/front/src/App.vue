<template>
    <div>
        <div class="load-spinner " v-if="$wait.waiting('fetchingSession')">
            <half-circle-spinner :animation-duration="2000" :size="100" color="#86BB71"/>
        </div>
        <div v-else>
            <router-view/>
        </div>
    </div>


</template>


<script>
    import {HalfCircleSpinner} from "epic-spinners";
    import axios from "axios";

    export default {
        name: "App",

        components: {
            HalfCircleSpinner
        },

        beforeCreate() {
            axios.interceptors.response.use(undefined, error => {
                if (error.response.data["YouKickedOrBan"]) {
                    this.$store.dispatch("invalidateSession");
                    this.$store.dispatch("chat/getOnlineUsers");
                    this.$store.dispatch("chat/getLastEvents");
                }
                return Promise.reject(error);
            });
        },

        async beforeMount() {
            this.$wait.start("fetchingSession");
            await this.$store.dispatch("getUserSession").catch(() => {
            });
            this.$wait.end("fetchingSession");
        }
    };
</script>

<style>
    @import url("https://fonts.googleapis.com/css?family=Comfortaa:400,700&subset=cyrillic");

    *,
    *:before,
    *:after {
        box-sizing: border-box;
    }

    body {
        background: #c5ddeb;
        font: 14px/20px "Comfortaa", Arial, sans-serif;
        padding: 10px 0;
        color: white;
    }

    .container {
        margin: 0 auto;
        width: 750px;
        background: #444753;
        border-radius: 5px;
    }

    .people-list {
        overflow-y: scroll;
        width: 260px;
        float: left;
        height: 700px;
    }

    .people-list .search {
        padding: 20px;
    }

    .people-list input {
        border-radius: 3px;
        border: none;
        padding: 14px;
        color: white;
        background: #6a6c75;
        width: 90%;
        font-size: 14px;
    }

    .people-list .fa-search {
        position: relative;
        left: -25px;
    }

    .people-list ul {
        padding: 20px;
    }

    .people-list ul li {
        list-style-type: none;
        padding-bottom: 10px;
    }

    .people-list img {
        float: left;
    }

    .people-list .about {
        float: left;
        margin-top: 8px;
    }

    .people-list .about {
        padding-left: 8px;
    }

    .people-list .status {
        color: #92959e;
    }

    .chat {
        width: 490px;
        float: left;
        background: #f2f5f8;
        border-top-right-radius: 5px;
        border-bottom-right-radius: 5px;
        color: #434651;
    }

    .chat-history ul li {
        list-style-type: none;
    }

    .chat .chat-header {
        padding: 20px;
        border-bottom: 2px solid white;
        height: 100px;
    }

    .chat .chat-header img {
        float: left;
    }

    .chat .chat-header .chat-about {
        float: left;
        padding-left: 10px;
        margin-top: 6px;
    }

    .chat .chat-header .chat-with {
        font-weight: bold;
        font-size: 16px;
    }

    .chat .chat-header .chat-num-messages {
        color: #92959e;
    }

    .chat .chat-header .chat-user {
        position: relative;
        float: right;
        padding-left: 10px;
        margin-top: 4px;
    }

    .chat-user a {
        text-decoration: none;
        color: #919191;
    }

    .chat-user .dropdown-menu-user {
        display: none;
        position: absolute;
        top: 20px;
        padding: 0;
        width: 100px;
        background-color: #dedede;
    }

    .chat-user-login:hover .dropdown-menu-user {
        display: block;
    }

    .dropdown-menu-user ul {
        padding: 0;
        margin: 0;
    }

    .dropdown-menu-user ul li {
        text-align: center;
        list-style: none;
        padding: 8px 0;
        border-bottom: 1px solid white;
    }

    .dropdown-menu-user ul li:hover {
        background-color: #ececec;
        color: black;
    }

    /*REG / AUTH LINKS*/
    .chat .chat-auth-links a {
        text-decoration: none;
        padding-bottom: 5px;
        display: block;
        color: #6c6d58;
    }

    .chat-auth-links a:hover {
        color: #292b1f;
    }

    .chat .chat-history {
        padding: 30px 30px 20px;
        border-bottom: 2px solid white;
        overflow-y: scroll;
        height: 450px;
    }

    .chat .chat-history .message-data {
        margin-bottom: 15px;
    }

    .chat .chat-history .message-data-time {
        color: #a8aab1;
        padding-left: 6px;
    }

    .chat .chat-history .message {
        color: white;
        padding: 18px 20px;
        line-height: 26px;
        font-size: 16px;
        border-radius: 7px;
        margin-bottom: 30px;
        width: 90%;
        position: relative;
        word-break: break-word;
    }

    .chat .chat-history .message:after {
        bottom: 100%;
        left: 7%;
        border: solid transparent;
        content: " ";
        height: 0;
        width: 0;
        position: absolute;
        pointer-events: none;
        border-bottom-color: #86bb71;
        border-width: 10px;
        margin-left: -10px;
    }

    .chat .chat-history .my-message {
        background: #86bb71;
    }

    .chat .chat-history .other-message {
        background: #94c2ed;
    }

    .chat .chat-history .other-message:after {
        border-bottom-color: #94c2ed;
        left: 93%;
    }

    .chat .chat-history .system-message {
        background: rgba(255, 79, 68, 0.3);
    }

    .chat .chat-history .system-message:after {
        border-bottom: none;
        left: 10%;
    }

    .chat .chat-message {
        padding: 30px;
        height: 150px;
    }

    .chat .chat-message textarea {
        width: 100%;
        padding: 10px 20px;
        font: 14px/22px "Lato", Arial, sans-serif;
        margin-bottom: 10px;
        border-radius: 5px;
        resize: none;
    }

    .chat .chat-message .fa-file-o,
    .chat .chat-message .fa-file-image-o {
        font-size: 16px;
        color: gray;
        cursor: pointer;
    }

    .chat .chat-message button {
        float: right;
        color: #94c2ed;
        font-size: 16px;
        text-transform: uppercase;
        border: none;
        cursor: pointer;
        font-weight: bold;
        background: #f2f5f8;
    }

    .chat .chat-message button:hover {
        color: #75b1e8;
    }

    .online,
    .offline,
    .me {
        margin-right: 3px;
        font-size: 14px;
    }

    .online {
        color: #86bb71;
    }

    .offline {
        color: #e38968;
    }

    .me {
        color: #94c2ed;
    }

    .align-left {
        text-align: left;
    }

    .align-right {
        text-align: right;
    }

    .float-right {
        float: right;
    }

    .clearfix:after {
        visibility: hidden;
        display: block;
        font-size: 0;
        content: " ";
        clear: both;
        height: 0;
    }

    .valid-error-input {
        border: 2px solid red;
    }

    .valid-error-message {
        text-align: center;
        color: red;
    }

    .load-spinner {
        position: absolute;
        top: 50%;
        left: 50%;
    }
</style>
