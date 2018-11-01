<template>
    <div class="chat-message clearfix">
				<textarea
                        :disabled="!isLogin"
                        :class="{'valid-error-input': messageValidError}"
                        name="message-to-send"
                        id="message-to-send"
                        placeholder="Type your message" v-model="inputMessage"
                        rows="2"></textarea>

        <button @click="sendButtonClick()" :hidden="!isLogin">
            <half-circle-spinner v-if="$wait.waiting('sendMsg')"
                                 style="margin: 0 auto;" :animation-duration="1000" :size="20" color="#86BB71"/>
            <span v-else>SEND</span>
        </button>
    </div>
</template>

<script>
    import {mapGetters, mapState} from "vuex";
    import {HalfCircleSpinner} from "epic-spinners";

    export default {
        name: "ChatMessageArea",

        components: {
            HalfCircleSpinner
        },

        data: function () {
            return {
                inputMessage: "",
                messageValidError: false
            };
        },

        computed: {
            ...mapState(["userSession"]),
            ...mapGetters(["isLogin"])
        },

        methods: {
            async sendButtonClick() {
                this.messageValidError = false;
                if (this.inputMessage.length > 0) {
                    this.$wait.start("sendMsg");
                    await this.$store
                        .dispatch("chat/sendMessage", this.inputMessage)
                        .then(() => {
                            this.inputMessage = "";
                            this.messageValidError = false;
                        })
                        .catch(() => {
                            this.messageValidError = true;
                        });
                }
                this.$wait.end("sendMsg");
            }
        }
    };
</script>

<style scoped>
</style>
