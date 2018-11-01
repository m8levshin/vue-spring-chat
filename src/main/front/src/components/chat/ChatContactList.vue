<template>
    <ul class="list">
        <chat-contact v-for="(user, index) of onlineUserList" :key="index" :user-name="user.userName"/>
    </ul>
</template>

<script>
    import ChatContact from "./ChatContact";

    export default {
        name: "ChatContactList",
        components: {ChatContact},

        props: {
            onlineUserList: Array
        }
    };
</script>
