<template>
    <div style="padding:20px">
        <!--    功能区域-->
        <div style="margin: 10px 0">

            <el-popconfirm title="确定删除吗？" @confirm="deleteBatch">
                <template #reference>
                    <el-button type="danger">批量删除</el-button>
                </template>
            </el-popconfirm>

        </div>
        <!-- 搜索区 -->
        <div style="margin:10px 0">
            <el-input placeholder="搜索用户id" v-model="search" clearable style="width:200px;margin-right: 10px;">
            </el-input>
            <el-button type="primary" @click="load">搜索</el-button>
        </div>
        <!-- 分页 -->
        <div style="margin:10px 0">
            <el-pagination v-model:currentPage="currentPage" v-model:page-size="pageSize" :page-sizes="[100, 200, 500]"
                :small="small" :disabled="disabled" :background="background"
                layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" />
        </div>

        <!-- 列表 -->
        <el-table :data="tableData" border style="width: 100%" height="60vh" v-loading="loading" stripe="true"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="40">

            </el-table-column>
            <el-table-column prop="id" label="ID" width="80" sortable />
            <el-table-column prop="uuid" label="uuid" show-overflow-tooltip />
            <el-table-column prop="name" label="文件名" show-overflow-tooltip />
            <el-table-column prop="type" label="文件类型" />
            <el-table-column label="预览">
                <template #default="scope">
                    <el-image style="width: 50px; height: 50px" :src="scope.row.path" fit="cover"
                        :preview-src-list="[scope.row.path]" preview-teleported>
                    </el-image>
                </template>
            </el-table-column>
            <el-table-column prop="path" label="文件位置" show-overflow-tooltip />
            <el-table-column prop="size" label="文件大小(B)" />
            <el-table-column prop="formatSize" label="格式化大小" />

            <el-table-column prop="createTime" label="创建日期" />
            <el-table-column prop="userId" label="上传者ID" />
            <el-table-column prop="username" label="上传者用户名" />


            <el-table-column fixed="right" label="操作" width="80">
                <template #default="scope">

                    <el-popconfirm title="确定删除选择数据吗？" @confirm="handleDelete(scope.row.id)" @cancel="handleCancel()">
                        <template #reference>
                            <el-button type="danger" size="small">删除</el-button>
                        </template>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <!-- 图片点击预览 -->
        <!-- <el-dialog v-model="dialogVisible2">
            <el-image style="width: 80vh; height: auto" :src="dialogImageUrl" fit="cover" />
        </el-dialog> -->
    </div>
</template>
      
<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import request from '@/utils/request';
import { ElMessage } from 'element-plus'
import { fill } from 'lodash';


export default defineComponent({
    name: 'SysFileView',
    components: {

    },
    setup() {
        //加载动画
        const loading = ref(true)

        //表中数据
        const tableData = ref([])
        // 分页
        const currentPage = ref(1)
        const pageSize = ref(100) //分页默认显示条数
        const total = ref(0)
        const small = ref(false)
        const background = ref(false)
        const disabled = ref(false)
        const handleSizeChange = (val: number) => { //改变每页数量
            console.log(`${val} items per page`)
            load()
        }
        const handleCurrentChange = (val: number) => { //改变当前页码位置
            console.log(`current page: ${val}`)
            load()
        }



        // table数据初始化
        const form: any = ref()
        const search = ref()
        const load = () => {
            request.get('/sys/file/pageFiles', {
                params: {
                    pageNum: currentPage.value,
                    pageSize: pageSize.value,
                    search: search.value
                }
            }).then(res => {
                // console.log(res.data)
                tableData.value = res.data.records
                total.value = res.data.total
                loading.value = false
            })
        }

        onMounted(() => {
            // roleHandle()
            load()

        })

        const handleDelete = (id: number) => {
            console.log(id)
            request.delete('/sys/file/' + id).then((res: any) => {
                if (res.code === '200') {
                    ElMessage.success("删除成功")
                    load()
                } else {
                    ElMessage.error(res.msg)
                }

            }).catch(err => {
                ElMessage.error('删除失败')
            })
        }
        const handleCancel = () => {
            ElMessage.error('取消操作')
        }

        //批量删除
        //全选
        const ids = ref([])
        const handleSelectionChange = (val: any) => {
            ids.value = val.map((v: any) => v.id)
        }
        const deleteBatch = () => {
            // console.log(ids);
            if (ids.value.length < 1) {
                ElMessage.error("请选择数据")
                return
            }
            request.post("/sys/file/deleteBatch", ids.value).then((res: any) => {
                if (res.code === '200') {
                    ElMessage.success("批量删除成功")
                    load()
                } else {
                    ElMessage.error('批量删除失败')
                }
            })
        }


        return {
            loading,
            tableData,
            search,
            handleDelete,
            currentPage,
            pageSize,
            total,
            small,
            background,
            disabled,
            handleSizeChange,
            handleCurrentChange,
            form,
            handleCancel,
            load,
            handleSelectionChange,
            deleteBatch
        }
    },
});
</script>
  
<style scoped>
body {
    margin: 0;
}

.example-showcase .el-loading-mask {
    z-index: 9;
}
</style>
      