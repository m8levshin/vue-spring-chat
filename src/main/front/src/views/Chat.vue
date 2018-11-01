<script>
    import ChatHistory from "../components/chat/ChatHistory";
    import ChatHeader from "../components/chat/ChatHeader";
    import ChatContactList from "../components/chat/ChatContactList";
    import ChatMessageArea from "../components/chat/ChatMessageArea";
    import {mapState} from "vuex";

    export default {
        name: "home",
        components: {ChatHistory, ChatHeader, ChatMessageArea, ChatContactList},

        computed: {
            ...mapState("chat", ["onlineUsers", "events"])
        },

        mounted() {
            this.$store.dispatch("chat/getOnlineUsers");
            this.$store.dispatch("chat/getLastEvents");

            setTimeout(
                function run(self) {
                    self.$store.dispatch("chat/getOnlineUsers");
                    self.$store.dispatch("chat/getLastEvents");
                    setTimeout(run, 5000, self);
                },
                5000,
                this
            );
        }
    };
</script>

<template>
    <div class="container clearfix">


        <div class="people-list" id="people-list">
            <chat-contact-list :online-user-list="onlineUsers"></chat-contact-list>
        </div>


        <div class="chat">
            <chat-header></chat-header>
            <chat-history :events="events"></chat-history>
            <chat-message-area></chat-message-area>
        </div>

    </div>
</template>
