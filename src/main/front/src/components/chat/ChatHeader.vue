<template>
    <div class="chat-header clearfix">
        <div class="chat-about">
            <div class="chat-with">Spring Chat'ик</div>
            <div class="chat-num-messages">feel the simplicity in spring</div>
        </div>

        <div class="chat-user">

            <div v-if="isLogin" class="chat-user-login">
                <a href="#">{{userSession.userName}}</a>

                <div class="dropdown-menu-user">
                    <ul>
                        <li @click="logOut()">Logout</li>
                    </ul>
                </div>
            </div>


            <div v-else class="chat-auth-links">
                <router-link to='/auth'>Sing In</router-link>
                <router-link to="/reg">Sing Up</router-link>
            </div>
        </div>
    </div>
</template>

<script>
    import {mapGetters, mapState} from "vuex";

    export default {
        name: "ChatHeader",

        computed: {
            ...mapState(["userSession"]),
            ...mapGetters(["isLogin"])
        },

        methods: {
            logOut() {
                this.$store.dispatch("logOut");
            }
        }
    };
</script>

<style scoped>
</style>
