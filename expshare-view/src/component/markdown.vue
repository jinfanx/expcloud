<template>
    <div class="md" v-html="parseMd(src)">
    </div>
</template>

<script>
    let marked = require("marked");
    export default {
        name: 'markdown',
        props: {
            src: String
        },
        methods: {
            parseMd(src){
                return marked(src);
            }
        }
    }

</script>

<style>

</style>