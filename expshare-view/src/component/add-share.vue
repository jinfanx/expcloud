<template>
    <el-form ref="share" class="text-left" :model="share" :rules="rules" label-position="top" align="left">

        <el-row>
            <el-col :span="15" :offset="9"><h4>新增问题</h4></el-col>
        </el-row>
        <el-form-item label="问题" prop="description">
            <el-input placeholder="简要描述问题" v-model="share.description" required="true"></el-input>
        </el-form-item>
        <el-form-item label="原因" prop="reason">
            <el-input type="textarea" placeholder="描述原因，支持markdown" rows="5" v-model="share.reason" required="true"></el-input>
        </el-form-item>
        <el-form-item label="解决办法" prop="solution">
            <div class="mavonEditor">
                <mavon-editor :subfield="false" placeholder="具体解决办法"  v-model="share.solution" required="true"/>
            </div>
        </el-form-item>

        <el-form-item label="选择分类(单选)" prop="category">
            <el-tree
                    :props="props"
                    :data="categories"
                    accordion
                    node-key="name"
                    show-checkbox
                    ref="tree"
                    :check-on-click-node="true"
                    v-model="share.category"
                    @check="handleCheckChange">
            </el-tree>
        </el-form-item>

        <el-form-item>
            <el-button type="primary" @click="submitForm('share')">提交</el-button>
            <el-button @click="resetForm('share')">重置</el-button>
        </el-form-item>
    </el-form>
</template>

<script>
    export default {
        name: "addShare",
        props:{
            categories:Array,
        },
        data(){
            return {
                share: {
                    description: '',
                    reason: '',
                    solution: '',
                    category: {},
                    createTime:{},
                    updateTime:{}
                },
                handbook: '具体解决办法，可用的代码',
                props: {
                    label: 'name',
                    children: 'items'
                },
                rules:{
                    description:[
                        { required: true, message: '请输入问题描述', trigger: 'blur' },
                    ],
                    reason:[
                        { required: true, message: '请输入原因', trigger: 'blur' },
                    ],
                    solution:[
                        { required: true, message: '请输入具体解决方案', trigger: 'blur' },
                    ],
                    category:[
                        { required: true, message: '请选择分类', trigger: 'blur' },
                    ]

                }
            }
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.share.createTime = new Date();
                        this.share.updateTime = this.share.createTime;
                        this.share.author = this.$user._id;
                        this.$axios.put(this.$service+"/api/share",this.share,
                            {
                                headers:{
                                    'Content-Type':'application/json; charset=utf-8'
                                }
                            })
                            .then((resp)=>{
                                this.$emit("add-share-success",this.share.category);
                                this.$message({
                                    message: '添加成功!'+resp.data._id,
                                    type: 'success'
                                });
                            })
                            .catch((error)=>{
                                this.$message({
                                    message: '添加失败！'+error,
                                    type: error
                                });
                            })
                    } else {
                        this.$message({
                            message: '提交失败!',
                            type: 'error'
                        });
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            handleCheckChange(data, checked) {
                // 单选树，点击有子节点的节点时不选中，并且不能取消已选择节点
                if(data.items){
                    let checkedNodes = new Set(this.$refs.tree.getCheckedNodes());
                    let currentNodes = new Set(data.items).add(data);
                    if (currentNodes.size>0 && checkedNodes.size>0) {
                        let originNodes = removeAddedData(checkedNodes,currentNodes);
                        this.$refs.tree.setCheckedNodes(originNodes);
                    }
                    return;

                }

                // 单选树组件
                if(checked && (data.item==undefined|| data.items.length==0)){
                    this.$refs.tree.setCheckedNodes([data]);
                    let halfCheckedNodes = this.$refs.tree.getHalfCheckedNodes();
                    let category = '';
                    halfCheckedNodes.forEach((n)=>{
                        category = (category+n.name+'-');
                    });
                    this.share.category = category+data.name;
                }
            },

        },
    }

    /**
     * element树组件check触发时已经选中节点，把新增的节点去掉，得到以前的选中节点
     * @param selectedNodes tree组件当前已选中节点
     * @param currentNodes 当前点击的节点
     * @returns {Array}
     */
    function removeAddedData(selectedNodes,currentNodes){
        let origin = [];
        for(let key of selectedNodes.keys()){
            if (!currentNodes.has(key)){
                origin.push(key);
            }
        }
        return origin;
    }

</script>

<style scoped>
    .text-left{
        text-align: left;
    }
</style>