<template>
    <div>
        <el-breadcrumb separator="/" class="marginBottom">
            <el-breadcrumb-item><i class="el-icon-date"></i>  {{category || '全部'}}</el-breadcrumb-item>
        </el-breadcrumb>
        <el-table
                :data="shares.slice((currentPage-1)*pagesize,currentPage*pagesize)"
                stripe
                border
                align="left"
                style="width: 100%">
            <el-table-column
                    type="index"
                    label="序号"
                    :index="indexMethod"
                    width="50">
            </el-table-column>
            <el-table-column
                    prop="description"
                    label="问题"
                    width="180">
                <template slot-scope="scope">
                    <a :href="'#'" class="link-primary" @click="showShareDetail(scope.row,'detail')">{{scope.row.description}}</a>
                </template>
            </el-table-column>
            <el-table-column
                    prop="reason"
                    label="原因">
                <template slot-scope="scope">
                    <me-markdown :src="scope.row.reason"/>
                </template>
            </el-table-column>
            <!--        <el-table-column-->
            <!--                prop="solution"-->
            <!--                label="解决方案">-->
            <!--            <template slot-scope="scope">-->
            <!--                <me-markdown :src="scope.row.solution"/>-->
            <!--            </template>-->
            <!--        </el-table-column>-->
            <el-table-column
                    prop="createTime"
                    label="创建日期"
                    sortable
                    :formatter="dateFormat"
                    width="180">
            </el-table-column>
            <el-table-column
                    label="操作"
                    width="360">
                <template slot-scope="scope">
                    <el-button type="primary" icon="el-icon-edit" size="mini"
                               @click="showShareDetail(scope.row,'update')">更新
                    </el-button>
                    <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteShare(scope.row)">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                align="left"
                class="marginTop"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[5, 10, 20, 40]"
                :page-size="pagesize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="shares.length">
        </el-pagination>
    </div>
</template>

<script>

    export default {
        name: 'shares',
        data() {
            return {
                currentPage: 1,
                pagesize: 5,
                shares: [],
                mdProp: {
                    subfield: false,
                    defaultOpen: 'preview',
                    editable: false,
                    toolbarsFlag: false,
                    scrollStyle: true
                },
            }
        },
        props: {
            category: String,
            categoryLevel: String,
            searchedShares: Array
        },
        watch: {
            category() {
                if(this.category.indexOf('搜索结果')!=-1){
                    this.shares = this.searchedShares;
                    return ;
                }
                if (this.category == '') {
                    this.$axios.get(this.$service + "/api/share")
                        .then((resp) => {
                            this.shares = resp.data;
                        })
                        .catch((error) => {
                            alert(error);
                        });
                } else {
                    this.$axios.get(this.$service + "/api/share/" + this.category + "/" + this.categoryLevel).then((resp) => {
                        this.shares = resp.data;
                    }).catch((error) => {
                        console.log(error);
                    });
                }

            },
        },
        /**
         * 动态组件每次切换时都会调用相应组件的
         * created生命周期钩子，重新切换到该组件时需要重新请求数据
         */
        created() {
            if (this.category == '') {
                this.$axios.get(this.$service + "/api/share")
                    .then((resp) => {
                        this.shares = resp.data;
                    })
                    .catch((error) => {
                        alert(error);
                    });
            } else {
                this.$axios.get(this.$service + "/api/share/" + this.category + "/" + this.categoryLevel).then((resp) => {
                    this.shares = resp.data;
                }).catch((error) => {
                    console.log(error);
                });
            }
        },
        methods: {
            // 格式化时间 yyyy-MM-dd hh:mm:ss
            dateFormat: function (row) {
                let t = new Date(row.createTime);
                let year = t.getFullYear(),
                    month = t.getMonth() + 1,
                    day = t.getDate(),
                    hour = t.getHours(),
                    min = t.getMinutes(),
                    sec = t.getSeconds();
                let newTime = year + '-' +
                    (month < 10 ? '0' + month : month) + '-' +
                    (day < 10 ? '0' + day : day) + ' ' +
                    (hour < 10 ? '0' + hour : hour) + ':' +
                    (min < 10 ? '0' + min : min) + ':' +
                    (sec < 10 ? '0' + sec : sec);
                return newTime;
            },
            // 详情页，emit传递动态组件名称和待展示的share
            showShareDetail: function (share, mode) {
                let obj = {};
                obj.templateName = 'me-detail-share';
                // share.defaultExpandedKeys = this.$refs.tree.getHalfCheckedKeys();
                if (share.category) {
                    let multiLevelCategory = share.category.split("-");
                    if (multiLevelCategory.length > 0) {
                        // 设置tree默认展开节点
                        let defaultExpandedKeys = [];
                        for (let i = 0; i < multiLevelCategory.length - 1; i++) {
                            defaultExpandedKeys.push(multiLevelCategory[i]);
                        }
                        share.defaultExpandedKeys = defaultExpandedKeys;
                        // 默认选中节点
                        share.defaultCheckedKeys = [multiLevelCategory[multiLevelCategory.length - 1]];
                    }
                }
                obj.mode = mode;
                obj.share = share;

                this.$emit("change-template", obj);
            },
            deleteShare: function (data) {
                this.$confirm('确定删除?', '删除问题', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.delete(this.$service + "/api/share/" + data._id).then((resp) => {
                        this.$message({
                            message: '问题删除成功!' + data._id,
                            type: 'success'
                        });
                        for (let i in this.shares) {
                            if (this.shares[i]._id === data._id) {

                                this.shares.splice(i, 1);
                            }
                        }
                    }).catch((error) => {
                        this.$message({
                            message: '删除失败！' + error,
                            type: 'error'
                        });
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });

            },
            handleSizeChange: function (size) {
                this.pagesize = size;
            },
            handleCurrentChange: function (currentPage) {
                this.currentPage = currentPage;
            },
            indexMethod(index){
                return this.pagesize*(this.currentPage-1)+index+1;
            }
        }
    }
</script>

<style>
    .link-primary {
        color: #409EFF;
        text-decoration: none;
    }
    .marginBottom {
        margin-bottom:20px;
    }
    .marginTop{
        margin-top:30px;
    }
</style>