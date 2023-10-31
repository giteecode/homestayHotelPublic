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

        <!-- 列表 -->
        <el-table :data="tableData" border style="width: 98%" v-loading="loading" stripe="true"
            @selection-change="handleSelectionChange" height="60vh">
            <el-table-column type="selection" width="40">

            </el-table-column>
            <el-table-column prop="id" label="ID" width="100" sortable />
            <el-table-column label="返回结果" width="70">
                <template #default="scope">
                    <el-button v-if="(scope.row.status===0)" type="success" plain style="cursor:default">成功</el-button>
                    <el-button v-else type="danger" plain style="cursor:default">失败</el-button>
                </template>
            </el-table-column>
            <el-table-column prop="operation" label="模块名" />
            <el-table-column prop="uri" label="请求路径uri" show-overflow-tooltip />
            <el-table-column prop="ip" label="操作者IP" />
            <el-table-column prop="operatorId" label="操作者用户ID" />
            <el-table-column prop="operatorName" label="操作者用户名" />
            <el-table-column prop="method" label="函数位置" show-overflow-tooltip />
            <el-table-column prop="params" label="请求参数" show-overflow-tooltip />
            <el-table-column prop="useTime" label="消耗时间" width="50"/>
            <el-table-column prop="createTime" label="创建日期" />



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
        <!-- 分页 -->
        <div style="margin:10px 0">
            <el-pagination v-model:currentPage="currentPage" v-model:page-size="pageSize" :page-sizes="[100, 200, 500]"
                :small="small" :disabled="disabled" :background="background"
                layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" />
        </div>

    </div>
</template>
      
<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import request from '@/utils/request';
import { ElMessage } from 'element-plus'


export default defineComponent({
    name: 'SysLogView',
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
            request.get('/sys/log', {
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
            request.delete('/sys/log/' + id).then((res: any) => {
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
            request.post("/sys/log/deleteBatch", ids.value).then((res: any) => {
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
      