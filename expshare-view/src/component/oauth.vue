<template>
</template>

<script>
    export default {
        name: "oauth",
        created() {
            const search = window.location.search.toString();
            // let code = search.substring(search.indexOf("?"));
            this.$axios.get(this.$service+'/login/github'+search).then(
                (resp)=>{
                    this.$message({
                        message: '登录成功！'+resp.data.username,
                        type: "success"
                    });
                    let obj = new Object();
                    obj.user = resp.data;
                    obj.templateName = 'me-shares';
                    this.$emit("change-user-info",obj);
                    // window.location.href = window.location.host.toString();
                }
            ).catch((error)=>{
                this.$message({
                    message: '登录失败！'+error,
                    type: "error"
                });
            });
        }
    }
</script>

<style scoped>

</style>