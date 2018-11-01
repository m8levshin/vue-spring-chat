<template>
    <div class="chat-history">
        <ul>
            <chat-message-cloud v-for="(event, index) of events"
                                :event="event"
                                :key="index">
            </chat-message-cloud>
        </ul>
    </div>
</template>

<script>
    import ChatMessageCloud from "./ChatMessageCloud";

    export default {
        name: "ChatHistory",
        components: {ChatMessageCloud},
        props: {
            events: Array
        },

        data: function () {
            return {
                needScrollBottom: true
            };
        },

        updated() {
            if (this.needScrollBottom) {
                let elementById = document.getElementsByClassName("chat-history")[0];
                elementById.scrollTop = elementById.scrollHeight;
                this.needScrollBottom = false;
            }
        }
    };
</script>
