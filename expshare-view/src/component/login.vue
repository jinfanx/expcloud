<template>
    <el-row>
        <el-col :offset="4" :span="12">
            <el-form ref="loginForm" :model="user" :rules="rules" align="left" label-position="left" label-width="70px">

                <el-row class="marginBottom text-left">
                    <el-col :span="15" :offset="9"><h4>登录</h4></el-col>
                </el-row>

                <el-form-item label="用户名" prop="username">
                    <el-input placeholder="用户名" v-model="user.username"></el-input>
                </el-form-item>

                <el-form-item label="密码" prop="password">
                    <el-input type="password" placeholder="密码" label="密码" v-model="user.password"></el-input>
                </el-form-item>

                <el-form-item class="text-left">
                    <el-button @click="login" type="primary">登录</el-button>
                    <el-button @click="resetForm('loginForm')" type="primary">重置</el-button>
                </el-form-item>

                <el-form-item class="text-left">
                    <el-button type="text" @click="githubLogin">github登录</el-button>
                </el-form-item>

            </el-form>
        </el-col>
    </el-row>
    
</template>

<script>
    export default {
        name: "login",
        data(){
            return {
                user:{},
                rules:{
                    username: '',
                    password: ''
                }
            }
        },
        methods:{
            login(){
                console.log("登录");
                return false;
            },
            githubLogin() {
                // let clientId = '8b3d20a67541139ce356';
                // let redirectUrl = 'http://localhost/callback';
                let clientId = '96c98bacb4357d85acc2';
                let redirectUrl = 'http://www.freej.top/callback';
                let url = 'https://github.com/login/oauth/authorize?';
                let param = "response_type=code&" + "client_id=" + clientId + "&redirect_uri=" + redirectUrl;
                window.open(url+param,'_blank');
                // window.open(this.$service+"/login/github",'_blank');

            },
            resetForm(form) {
                this.$refs[form].resetFields();
            }
        },
    }
</script>

<style scoped>
    .marginBottom {
        margin-bottom:30px;
    }
    .text-left{
        text-align: left;
    }
</style>