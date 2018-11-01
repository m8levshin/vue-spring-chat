<template>
    <li class="clearfix">
        <div class="about">
            <div class="name">{{userName}}</div>
            <div class="status">
                <i class="fa fa-circle online"></i> online
            </div>
        </div>
    </li>
</template>

<script>
    export default {
        name: "ChatContact",
        props: {
            userName: {
                type: String,
                required: true
            }
        }
    };
</script>
