import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import App from './App.vue'
import axios from  "axios"

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import BootstrapVue from 'bootstrap-vue'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
// import marked from 'marked'


// 自定义组件
import markdown from "./component/markdown"
import shares from "./component/shares"
import addShare from "./component/add-share"
import detailSahre from './component/detail-share'
import login from './component/login'
import oauth from './component/oauth'

Vue.component("me-markdown",markdown)
Vue.component("me-shares",shares)
Vue.component("me-add-share",addShare)
Vue.component("me-detail-share",detailSahre)
Vue.component("me-login",login)
Vue.component('me-oauth',oauth)

Vue.use(mavonEditor)
Vue.use(BootstrapVue)
Vue.use(ElementUI)
// Vue.use(marked)

axios.defaults.withCredentials =true;
Vue.prototype.$axios = axios
// 此变量值为接口地址，如https://www.freej.top:9001,生产环境使用nginx代理，不考虑跨域问题，此处为空值
Vue.prototype.$service = ""
Vue.prototype.$rmfunc = deleteAllFunctionsAndPartAttribute
Vue.prototype.$user = {}

new Vue({
    el: '#app',
    render: h => h(App),
});

function deleteAllFunctionsAndPartAttribute(obj,attrs) {
    for(let i in obj){
        let attrSet = new Set(attrs);
        if(typeof obj[i] === 'function' || attrSet.has(i)){
            delete obj[i];
        }
    }
    return new Object(obj);

}
