<template>

    <div class="container clearfix">

        <h1>Please, login</h1>


        <div v-if="userBannedError" class="valid-error-message">Пользователь забанен</div>
        <div v-if="credentialsAreNotValid" class="valid-error-message">Данные не верны</div>
        <div v-if="isLoginValid" class="valid-error-message">Логин должен содержать от 3 до 50 символов</div>
        <div v-if="isPasswordValid" class="valid-error-message">Пароль должен содержать от 3 до 50 символов</div>

        <div class="input-container">
            <div class="input">
                <div class="title-for-login">User name</div>
                <input v-bind:class="{ 'valid-error-input': isLoginValid || credentialsAreNotValid }"
                       type="text"
                       name="userName"
                       v-model="userNameInput"/>
            </div>

            <div class="input">
                <div class="title-for-login">Password</div>
                <input type="password"
                       v-bind:class="{ 'valid-error-input': isPasswordValid || credentialsAreNotValid }"
                       name="password" v-model="passwordInput"/>
            </div>

            <button @click="login">
                <half-circle-spinner v-if="$wait.waiting('auth')"
                                     style="margin: 0 auto;" :animation-duration="1000" :size="20" color="#86BB71"/>
                <span v-else>LOGIN</span>
            </button>

        </div>

    </div>

</template>

<script>
    import {mapGetters} from "vuex";
    import {HalfCircleSpinner} from "epic-spinners";

    export default {
        components: {
            HalfCircleSpinner
        },
        name: "Auth",

        data: function () {
            return {
                userNameInput: "",
                passwordInput: "",
                errors: []
            };
        },

        computed: {
            ...mapGetters(["isLogin"]),

            isLoginValid: function () {
                return this.errors.indexOf("userNameValidationError") !== -1;
            },

            isPasswordValid: function () {
                return this.errors.indexOf("passwordValidationError") !== -1;
            },

            credentialsAreNotValid: function () {
                return this.errors.indexOf("badCredentialError") !== -1;
            },

            userBannedError: function () {
                return this.errors.indexOf("userBannedError") !== -1;
            }
        },

        methods: {
            resetForm() {
                this.resetErrors();
                this.passwordInput = "";
                this.userNameInput = "";
            },

            resetErrors() {
                this.errors = [];
            },

            async login() {
                this.$wait.start("auth");

                await this.$store
                    .dispatch("authUser", {
                        userName: this.userNameInput,
                        password: this.passwordInput
                    })

                    .then(() => {
                        this.resetForm();
                        this.$router.push("/");
                    })

                    .catch(error => {
                        this.resetErrors();
                        let errors = error.response.data;
                        for (const err of errors) {
                            this.errors.push(err);
                            this.passwordInput = "";
                        }
                    });
                this.$wait.end("auth");
            }
        },

        mounted() {
            if (this.isLogin) {
                this.$router.push("/");
            }
        }
    };
</script>

<style scoped>
    .container {
        padding: 20px;
    }

    h1 {
        text-align: center;
        font-size: 28px;
    }

    .title-for-login {
        font-size: 18px;
    }

    .input-container {
        padding: 20px;
        width: 500px;
        margin: 0 auto;
    }

    .input-container .input {
        margin: 15px 0 2px 0;
    }

    .input input {
        padding: 8px;
        width: 100%;
    }

    .input-container button {
        margin: 15px 0 0 0;
        padding: 8px 0 8px 0;
        background-color: #f6f6f6;
        width: 30%;
        float: right;
        font-size: 18px;
        border: 1px solid gray;
    }

    .input-container button:hover {
        cursor: pointer;
        background-color: #cecece;
    }
</style>
