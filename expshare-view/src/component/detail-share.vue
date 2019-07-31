<template>
    <el-form ref="share" class="text-left" :model="share" :rules="rules" label-position="top" align="left">

        <el-row>
            <el-col :span="15" :offset="9">
                <h4 v-if="detail">问题详情</h4>
                <h4 v-else>更新问题</h4>
            </el-col>
        </el-row>

        <el-form-item label="问题" prop="description">
            <el-input placeholder="简要描述问题" v-model="share.description" :required="update" v-if="update"/>
            <div v-else class="marginLeft">
                {{share.description}}
            </div>
        </el-form-item>

        <el-form-item label="原因" prop="reason">
            <div v-if="detail" v-html="parseMd(share.reason)" class="marginLeft"/>
            <el-input v-else type="textarea" placeholder="描述原因，支持markdown" rows="5" v-model="share.reason"/>
        </el-form-item>

        <el-form-item label="解决办法" prop="solution">
            <!--详情页-->
<!--            <template v-if="detail">-->
<!--                &lt;!&ndash;                <div v-html="parseMd(share.solution)" class="marginLeft"/>&ndash;&gt;-->
<!--                <mavon-editor-->
<!--                        :value="share.solution"-->
<!--                        :subfield="false"-->
<!--                        :defaultOpen="defaultOpen"-->
<!--                        :toolbarsFlag="update"-->
<!--                        :editable="update"-->
<!--                        :scrollStyle="true"-->
<!--                        :required="update"-->
<!--                ></mavon-editor>-->
<!--            </template>-->
            <!-- 更新页-->
            <template>
                <div class="mavonEditor">
                    <mavon-editor :subfield="false" placeholder="具体解决办法" v-model="share.solution"
                                  :editable="update" :toolbarsFlag="update" :defaultOpen="defaultOpen" :ishljs="true"
                                  :boxShadow="update"
                                  :required="update"/>
                </div>
            </template>

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
                    :default-expanded-keys="share.defaultExpandedKeys"
                    :default-checked-keys="share.defaultCheckedKeys"
                    @check="handleCheckChange">
            </el-tree>
        </el-form-item>

        <el-form-item v-if="update">
            <el-button type="primary" @click="submitForm('share')">更新</el-button>
            <!--            <el-button @click="resetForm('share')">重置</el-button>-->
        </el-form-item>
    </el-form>
</template>

<script>
    let marked = require('marked');
    export default {
        name: "detailShare",
        props: {
            categories: Array,
            mode: String,//模式，update为更新，detail为详情
            share: {
                // _id:'',
                // description: '',
                // reason: '',
                // solution: '',
                // category: {},
                // createTime: {},
                // updateTime: {},
                // defaultExpandedKeys: [],
                // defaultCheckedKeys:[]
            },
        },
        computed: {
            update: function () {
                return this.mode == 'update';
            },
            detail: function () {
                return this.mode == 'detail';
            },
            defaultOpen(){
                if (this.update){
                    return 'edit';
                } else{
                    return 'preview';
                }
            }
        },
        data() {
            return {
                handbook: '具体解决办法，可用的代码',
                props: {
                    label: 'name',
                    children: 'items'
                },
                rules: {
                    description: [
                        {required: true, message: '请输入问题描述', trigger: 'blur'},
                    ],
                    reason: [
                        {required: true, message: '请输入原因', trigger: 'blur'},
                    ],
                    solution: [
                        {required: true, message: '请输入具体解决方案', trigger: 'blur'},
                    ],
                    category: [
                        {required: true, message: '请选择分类', trigger: 'blur'},
                    ]

                }
            }
        },
        methods: {
            parseMd(src) {
                return marked(src);
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    let id = this.share._id;
                    if (valid) {
                        this.share.updateTime = new Date();
                        if (!this.share.createTime) {
                            this.share.createTime = this.share.updateTime;
                        }

                        let obj = this.$rmfunc(this.share, ['_id', 'defaultCheckedKeys', 'defaultExpandedKeys']);
                        console.log(obj);

                        this.$axios.put(this.$service + "/api/share/" + id, obj,
                            {
                                headers: {
                                    'Content-Type': 'application/json; charset=utf-8'
                                }
                            })
                            .then((resp) => {
                                this.$emit("add-share-success", resp.data.category);
                                this.$message({
                                    message: '更新成功！' + resp.data._id,
                                    type: 'success'
                                });
                            })
                            .catch((error) => {
                                console.log(error);
                            });
                        // return false;
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            // resetForm(formName) {
            //     console.log("reset:"+formName);
            //     this.$refs[formName].resetFields();
            // },
            handleCheckChange(data, checked) {
                // 单选树，点击有子节点的节点时不选中，并且不能取消已选择节点
                if (data.items) {
                    let checkedNodes = new Set(this.$refs.tree.getCheckedNodes());
                    let currentNodes = new Set(data.items).add(data);
                    if (currentNodes.size > 0 && checkedNodes.size > 0) {
                        let originNodes = removeAddedData(checkedNodes, currentNodes);
                        this.$refs.tree.setCheckedNodes(originNodes);
                    }
                    return;

                }

                /**
                 * 详情页面不允许选择
                 * 直接返回
                 */
                if (this.detail) {
                    let checkedNodes = new Set(this.$refs.tree.getCheckedNodes());
                    let currentNodes = new Set().add(data);
                    if (currentNodes.size > 0 && checkedNodes.size > 0) {
                        let originNodes = removeAddedData(checkedNodes, currentNodes);
                        this.$refs.tree.setCheckedNodes(originNodes);
                    }
                    return;
                }

                // 单选树组件
                if (checked && (data.item == undefined || data.items.length == 0)) {
                    this.$refs.tree.setCheckedNodes([data]);
                    let halfCheckedNodes = this.$refs.tree.getHalfCheckedNodes();
                    let category = '';
                    halfCheckedNodes.forEach((n) => {
                        category = (category + n.name + '-');
                    });
                    this.share.category = category + data.name;
                }
            },

        },
        // mounted之前无法使用this.$refs引用组件
        mounted() {
            // 详情页面tree禁止编辑
            if (this.detail) {
                let nodes = this.$refs.tree.getCheckedNodes();
                // let nodesHalf = this.$refs.tree.getCheckedNodes();
                nodes.forEach((n) => {
                    n.disabled = true;
                });

            }
            if (this.update) {
                let nodes = this.$refs.tree.getCheckedNodes();
                nodes.forEach((n) => {
                    n.disabled = false;
                });
            }
        }
    }

    function removeAddedData(selectedNodes, currentNodes) {
        let origin = [];
        for (let key of selectedNodes.keys()) {
            if (!currentNodes.has(key)) {
                origin.push(key);
            }
        }
        return origin;
    }

</script>

<style scoped>
    .marginLeft {
        margin-left: 30px;
    }

    .text-left {
        text-align: left;
    }
</style>