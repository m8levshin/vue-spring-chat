<template>
    <div class="message-cloud">


        <li class="clearfix">


            <!--REG EVENTS-->
            <div v-if="event.eventType.eventTypeId === 1">
                <div class="message-data align-right">
                    <span class="message-data-name">SYSTEM</span>
                    <span class="message-data-time">{{event.timestamp | formatDate}}</span>
                </div>


                <div class="message system-message float-right">
                    Пользователь {{event.fromUser.userName}} зарегистрировался в чатике.
                </div>
            </div>

            <!--MESSAGE EVENTS-->
            <div v-if="event.eventType.eventTypeId === 2">

                <div v-if="event.fromUser.userName === userSession.userName">
                    <div class="message-data align-right">
                        <span class="message-data-name">{{event.fromUser.userName}} </span>
                        <span class="message-data-time">{{event.timestamp | formatDate}}</span>
                    </div>


                    <div class="message other-message float-right">
                        {{event.message}}
                    </div>
                </div>

                <div v-else>
                    <div class="message-data">
                        <span class="message-data-name">{{event.fromUser.userName}} </span>
                        <span class="message-data-time">{{event.timestamp | formatDate}}</span>
                    </div>

                    <div class="message my-message">
                        {{event.message}}
                    </div>
                </div>
            </div>


            <!--LOG OUT-->
            <div v-if="event.eventType.eventTypeId === 3">
                <div class="message-data align-right">
                    <span class="message-data-name">SYSTEM</span>
                    <span class="message-data-time">{{event.timestamp | formatDate}}</span>
                </div>


                <div class="message system-message float-right">
                    Пользователь {{event.fromUser.userName}} вышел из чатика.
                </div>
            </div>

            <!--LOG IN-->
            <div v-if="event.eventType.eventTypeId === 4">
                <div class="message-data align-right">
                    <span class="message-data-name">SYSTEM</span>
                    <span class="message-data-time">{{event.timestamp | formatDate}}</span>
                </div>


                <div class="message system-message float-right">
                    Пользователь {{event.fromUser.userName}} вошел в чатик.
                </div>
            </div>


            <!--KICK-->
            <div v-if="event.eventType.eventTypeId === 5">
                <div class="message-data align-right">
                    <span class="message-data-name">SYSTEM</span>
                    <span class="message-data-time">{{event.timestamp | formatDate}}</span>
                </div>

                <div class="message system-message float-right">
                    Пользователь {{event.fromUser.userName}} кикнул пользователя {{event.toUser.userName}}.
                </div>
            </div>


            <!--BAN-->
            <div v-if="event.eventType.eventTypeId === 6">
                <div class="message-data align-right">
                    <span class="message-data-name">SYSTEM</span>
                    <span class="message-data-time">{{event.timestamp | formatDate}}</span>
                </div>

                <div class="message system-message float-right">
                    Пользователь {{event.fromUser.userName}} забанил пользователя {{event.toUser.userName}}.
                </div>
            </div>

        </li>
    </div>
</template>


<script>
    import {mapState} from "vuex";

    export default {
        computed: {
            ...mapState(["userSession"])
        },

        name: "ChatMessageCloud",
        props: {
            event: {
                required: true
            }
        }
    };
</script>
