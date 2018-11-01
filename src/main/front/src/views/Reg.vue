<template>

    <div class="container clearfix">

        <h1>Registration</h1>

        <div class="input-container">

            <div v-if="userExistError" class="valid-error-message">Пользователь с таким именем уже существует</div>
            <div v-if="passwordEqualsValidationError" class="valid-error-message">Пароли не совпадают</div>
            <div v-if="passwordError" class="valid-error-message">Пароль должен быть от 3 до 50 символов</div>
            <div v-if="userNameValidationError" class="valid-error-message">Логин должен быть от 3 до 50 символов</div>

            <div class="input">
                <div class="title-for-login">User name</div>
                <input v-bind:class="{ 'valid-error-input': userNameValidationError }"
                       type="text"
                       name="userName"
                       v-model="userNameInput"/>
            </div>

            <div class="input">
                <div class="title-for-login">Password</div>
                <input type="password"
                       v-bind:class="{ 'valid-error-input': passwordError || passwordEqualsValidationError }"
                       name="password" v-model="passwordInput"/>
            </div>

            <div class="input">
                <div class="title-for-login">Repeat password</div>
                <input type="password"
                       v-bind:class="{ 'valid-error-input': passwordError || passwordEqualsValidationError }"
                       name="repeatPassword" v-model="passwordRepeatInput"/>
            </div>

            <button @click="reg">
                <half-circle-spinner v-if="$wait.waiting('reg')"
                                     style="margin: 0 auto;" :animation-duration="1000" :size="20" color="#86BB71"/>
                <span v-else>REGISTRATION</span>
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
        name: "Reg",

        data: function () {
            return {
                userNameInput: "",
                passwordInput: "",
                passwordRepeatInput: "",
                errors: []
            };
        },

        computed: {
            ...mapGetters(["isLogin"]),

            userNameValidationError: function () {
                return this.errors.indexOf("userNameValidationError") !== -1;
            },

            passwordError: function () {
                return this.errors.indexOf("passwordValidationError") !== -1;
            },

            passwordEqualsValidationError: function () {
                return this.errors.indexOf("passwordEqualsValidationError") !== -1;
            },

            userExistError: function () {
                return this.errors.indexOf("userExistError") !== -1;
            }
        },

        methods: {
            resetForm() {
                this.userNameInput = "";
                this.passwordInput = "";
                this.passwordRepeatInput = "";
                this.errors = [];
            },

            resetErrors() {
                this.errors = [];
            },

            async reg() {
                this.$wait.start("reg");
                this.resetErrors();

                await this.$store
                    .dispatch("regUser", {
                        userName: this.userNameInput,
                        password: this.passwordInput,
                        repeatPassword: this.passwordRepeatInput
                    })
                    .then(async () => {
                        await this.$store.dispatch("authUser", {
                            userName: this.userNameInput,
                            password: this.passwordInput
                        });

                        this.resetForm();
                        this.$router.push("/");
                    })

                    .catch(error => {
                        let errors = error.response.data;
                        for (const err of errors) {
                            this.errors.push(err);
                            this.passwordInput = "";
                            this.passwordRepeatInput = "";
                        }
                    });
                this.$wait.end("reg");
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
        width: 40%;
        float: right;
        font-size: 18px;
        border: 1px solid gray;
    }

    .input-container button:hover {
        cursor: pointer;
        background-color: #cecece;
    }
</style>
