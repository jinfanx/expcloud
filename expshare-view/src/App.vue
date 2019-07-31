<template>
    <div id="app">
        <el-container>
            <el-header>

                <el-menu class="el-menu-demo" mode="horizontal" @select="handleSelect"
                         background-color="#545c64"
                         text-color="#fff"
                         active-text-color="#ffd04b">
                    <el-menu-item class="big-bold" index="noAction"><a href="#" @click="jump2home">Expshare</a></el-menu-item>
                    <el-menu-item v-for="(category,index) in categories" :index="String(index)">{{ category.name }}</el-menu-item>
                    <el-submenu index="noAction-addCategory">
                        <template slot="title">添加分类</template>
                        <el-menu-item index="noAction-addCategory">新增分类</el-menu-item>
                        <el-menu-item index="noAction-addShare">新增问题</el-menu-item>
                    </el-submenu>
                    <el-submenu index="noAction-user">
                        <template slot="title">{{username}}</template>
                        <template v-if="loggedIn">
<!--                            <el-menu-item index="noAction-username">{{ username }}</el-menu-item>-->
                            <el-menu-item index="noAction-logout">退出登录</el-menu-item>
                        </template>
                        <template v-else>
                            <el-menu-item index="noAction-register">注册</el-menu-item>
                            <el-menu-item index="noAction-login">登录</el-menu-item>
                        </template>

                    </el-submenu>

                    <el-menu-item index="noAction-search">
                        <form class="nav-search-form">
                            <div>
                                <el-input placeholder="输入关键字" size="small" v-model="searchKeywords"></el-input>
                            </div>
                            <div>
                                <el-button type="primary" size="small" @click="search">搜索</el-button>
                            </div>
                        </form>
                    </el-menu-item>


                </el-menu>
            </el-header>
            <el-container>
                <el-aside>
                    <el-menu @select="queryBySubCategory">
                        <el-menu-item v-for="(item,index) in items" :index="String(index)">{{item.name}}</el-menu-item>
                    </el-menu>
                </el-aside>
                <el-main>
                    <component v-bind:is="mainComponent" :categories="categories"
                               :share="share"
                               :mode="mode"
                               :category="category"
                               :categoryLevel="categoryLevel"
                               :searchedShares="searchResult"
                               @add-share-success="jump2share"
                               @change-template="changeTemplate"
                               @change-user-info="changeUserInfo"/>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    export default {
        methods: {
            handleSelect:function(key) {
                if (key.indexOf('noAction')==-1){
                    // debugger;
                    this.currentTopCategory = this.categories[key];
                    this.items = this.subcategories[key];
                    this.category = this.categories[key].name;
                    this.categoryLevel = '1';
                    this.mainComponent = 'me-shares';
                }else if(key=='noAction-addShare'){
                    this.mainComponent = 'me-add-share';
                }else if(key=='noAction-login'){
                    this.mainComponent = 'me-login';
                }
            },
            changeTemplate:function(obj){
                // console.log(obj);
                this.share = obj.share;
                this.mode = obj.mode;
                this.mainComponent = obj.templateName;
            },
            changeUserInfo:function(obj){
                this.user = obj.user;
                this.$user = obj.user;
                this.mainComponent = obj.templateName;
            },
            queryBySubCategory:function(key){
                this.category = this.currentTopCategory.name+"-"+this.items[key].name;
                this.categoryLevel = '0';
                this.mainComponent = 'me-shares';
            },
            jump2share:function(category){

                this.category = category;
                this.categoryLevel = '0';
                this.mainComponent = 'me-shares';
            },
            jump2home:function(){
                this.category='';
                this.mainComponent = 'me-shares';
            },
            search:function(){
                // console.log(this.searchKeywords);
                this.$axios.get(this.$service+'/api/share/search?keywords='+this.searchKeywords)
                .then((resp)=>{
                    // console.log(resp.data);
                    this.category = "搜索结果-"+this.searchKeywords;
                    this.searchResult = resp.data;
                    this.mainComponent = 'me-shares';
                }).catch((error)=>{
                    console.log(error);
                    this.$message({
                        message: error,
                        type: error,
                    });
                });
            }
        },
        data() {
            return {
                categories: [],
                items: [],
                subcategories: [],
                mainComponent: 'me-shares',
                share: {},
                mode: '',
                user: {},
                category: '',
                categoryLevel: '1',
                currentTopCategory: {},
                searchKeywords: '',
                searchResult: [],
            };
        },
        created() {
            let location = window.location.toString();
            if (location.indexOf('code=')!=-1) {
                this.mainComponent = 'me-oauth';
            }
            this.$axios.get(this.$service+"/api/category")
                .then((resp)=>{
                    this.categories = resp.data;
                    resp.data.forEach((category,index)=>{
                        this.subcategories[index] = category.items;
                    });
                    if (resp.data && resp.data[0]){
                        this.currentTopCategory = resp.data[0];
                        this.items = resp.data[0].items;
                    }

                })
                .catch((error)=>{
                    console.log(error);
                });
        },
        computed:{
            loggedIn(){
                return (this.user.login!=null && this.user.login!='') ||
                    (this.user.username!=null && this.user.username!='');
            },
            username(){
                return this.user.login||this.user.username||'用户';
            }
        }
    }
</script>

<style>
    #app {
        font-family: Helvetica, sans-serif;
        text-align: center;
    }

    /* 固定页脚
    /*.el-footer {*/
    /*    background-color: #B3C0D1;*/
    /*    color: #333;*/
    /*    text-align: center;*/
    /*    line-height: 60px;*/
    /*    position: absolute;*/
    /*    bottom: 0;*/
    /*    width: 100%;*/
    /*    height: 100px;*/
    /*}*/


    .el-aside{
        margin-top:30px;
    }

    .el-main{
        margin-top:30px;
    }

    .el-header {
        background-color: #545c64;
    }

    body{
        padding: 0px;
        margin: 0px;

    }

    .big-bold{
        font-size: 20px;
        font-weight: bold;
    }

    .big-bold a{
        text-decoration:none;
    }

    .nav-search-form div{
        display: inline;
        margin-right: 10px;
        vertical-align: middle;
    }
    .nav-search-form{
        margin-left: 150px;
    }


</style>
