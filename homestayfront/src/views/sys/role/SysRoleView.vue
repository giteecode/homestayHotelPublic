<!-- 角色权限管理 -->
<template>
    <div style="padding:20px">
        <div class="roleTable">
            <!-- 功能区 -->
            <div style="margin:10px 0">
                <el-button type="primary" size="larg" @click="dialogVisible = true, dialogForm = {}, updateCon = false">
                    新增</el-button>
                <!-- <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button> -->
            </div>
            <el-table :data="tableData" style="width: 100%; margin-bottom: 20px" row-key="menuId" border
                default-expand-all:false v-loading="loading">
                <el-table-column prop="id" label="id" sortable />
                <el-table-column label="状态" width="65" sortable>
                    <template #default="scope">
                        <span v-if="scope.row.status === 0">
                            <el-button type="success" text bg @click="statusHandle(scope.row)">启用</el-button>
                        </span>
                        <span v-if="scope.row.status === 1">
                            <el-button type="danger" text bg @click="statusHandle(scope.row)">封禁</el-button>
                        </span>
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="角色名" />
                <el-table-column prop="code" label="角色代码" />

                <el-table-column prop="createTime" label="创建时间" sortable />
                <el-table-column prop="updateTime" label="更新时间" sortable />
                <el-table-column prop="remark" show-overflow-tooltip label="备注" />
                <el-table-column fixed="right" label="Operations" width="150">
                    <template #default="scope">
                        <el-button type="primary" size="small" @click="handleEdit(scope.row), updateCon = true">
                            编辑
                        </el-button>
                        <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)" @cancel="handleCancel()">
                            <template #reference>
                                <el-button type="danger" size="small">删除</el-button>
                            </template>
                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <div class="dialog">
            <!--    弹窗提醒：新增or修改角色-->
            <el-dialog v-model="dialogVisible" title="提示" width="30%">
                <el-form :model="dialogForm" label-width="120px">
                    <el-form-item label="状态">
                        <el-radio-group v-model="dialogForm.status">
                            <el-radio :label="0">开启</el-radio>
                            <el-radio :label="1">封禁</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="角色名">
                        <el-input v-model="dialogForm.name" style="width: 80%" maxlength="16"></el-input>
                    </el-form-item>

                    <el-form-item label="角色代码">
                        <el-input v-model="dialogForm.code" style="width: 80%" maxlength="50"></el-input>
                    </el-form-item>
                    <el-form-item label="备注">
                        <el-input type="textarea" v-model="dialogForm.remark" style="width: 80%"></el-input>
                    </el-form-item>
                    <el-form-item label="可访问菜单">
                        <el-tree :data="dataTree" :props="defaultProps"  node-key="menuId" :default-checked-keys="dialogForm.roleHasMenuIds" show-checkbox
                            @check="handlechecked" default-expand-all />
                    </el-form-item>
                </el-form>
                <template #footer="target">
                    <span class="dialog-footer">
                        <el-button @click="dialogVisible = false">取消</el-button>
                        <el-button type="primary" @click="dialogVisible = false, menuHandle()">确认</el-button>
                    </span>
                </template>
            </el-dialog>
        </div>
    </div>
</template>


<script lang='ts'>
import { defineComponent, ref, onMounted } from 'vue';
import request from '@/utils/request';
import { ElMessage } from 'element-plus'
import type Node from 'element-plus/es/components/tree/src/model/node'
export default defineComponent({
    name: 'SysRoleView',
    components: {

    },
    setup() {
        //加载动画
        const loading = ref(true)
        // 弹窗控制和数据
        const dialogVisible = ref(false)
        const dialogForm = ref()
        const updateCon = ref(false)
        // 数据
        const tableLayout = ref('fixed')
        const tableData = ref([])
        //初始化表格数据
        const initTableData = () => {
            request.get('/sys/role/roleAndMenus', {

            }).then((res: any) => {
                console.log(res)
                if (res.code == 200) {
                    tableData.value = res.data
                } else {
                    ElMessage.error(res.msg)
                }
                loading.value = false
            })
        }
        onMounted(() => {
            initTableData()
            getRoleList()
            initTree()
        })

        //用于判断弹窗中的操作是新增or修改
        const menuHandle = () => {
            if (updateCon.value == true) { //判断是否是更新操作
                console.log('edit:true')
                request.put('/sys/role', dialogForm.value).then((res: any) => {
                    if (res.code === '0' || res.code === '200') {
                        ElMessage({
                            message: '更新成功',
                            type: 'success',
                        })
                    } else {
                        ElMessage.error('更新失败')
                    }
                    initTableData()
                }).catch(err => {
                    // console.log(err)
                    ElMessage.error('更新失败')
                })
                dialogVisible.value = false
            } else {
                console.log('edit:false , is:add')
                console.log(dialogForm.value);
                
                request.post('/sys/role', dialogForm.value).then((res: any) => {
                    // console.log(res)
                    if (res.code === '0' || res.code === '200') {
                        ElMessage({
                            message: '添加成功',
                            type: 'success',
                        })
                    } else {
                        ElMessage.error('添加失败')
                    }
                    initTableData()
                }).catch(err => {
                    // console.log(err)
                    ElMessage.error('添加失败')
                })
                dialogVisible.value = false
            }
            // console.log(choseRoleValues.value);

        }

        const handleEdit = (row: any) => {
            //把操作对象放进dialog中
            //跟表中数据隔离开，深拷贝，独立对象，避免点击取消数据丢失
            // console.log(row.parentId);
            dialogForm.value = JSON.parse(JSON.stringify(row))
            dialogVisible.value = true

        }

        const handleDelete = (id: number) => {
            // console.log('handleDelete');
            request.delete('/sys/role/' + id).then((res: any) => {
                if (res.code === '200') {
                    ElMessage({
                        message: '删除成功',
                        type: 'success',
                    })
                    initTableData()
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

        //status状态切换
        const statusHandle = (row: any) => {

            request.put('/sys/role/status', row)
                .then((res: any) => {
                    if (res.code == 200) {
                        initTableData()
                        ElMessage.success('状态切换成功')
                    }
                })
                .catch(() => {
                    ElMessage.error('状态切换失败')
                })
        }
        // 获取权限列表
        const roleList: any = ref([])
        const getRoleList = () => {
            request.get('/sys/role', {})
                .then((res: any) => {
                    if (res.code == 200) {
                        roleList.value = res.data
                        // console.log(roleList.value);

                    } else {
                        ElMessage.error(res.msg)
                    }
                })
        }

        // 菜单选择树
        const dataTree: any = ref([])
        const defaultProps = {
            children: dataTree.value.children,
            label: "menuName",
        }
        
        const initTree = () => {
            request.get('/sys/menu', {

            }).then((res: any) => {
                // console.log(res)
                if (res.code == 200) {
                    dataTree.value = res.data
                    console.log(dataTree.value);

                } else {
                    ElMessage.error(res.msg)
                }

            })
        }
        const handleCheckChange = (
            data: any,
            checked: boolean,
            indeterminate: boolean
        ) => {
            console.log(data, checked, indeterminate)
        }
        const handlechecked = (data:any,checkedNodes:any) =>{
            // console.log(data);
            console.log(checkedNodes);
            dialogForm.value.roleMenus = checkedNodes.checkedNodes
            
        }
        return {
            loading,
            tableLayout,
            tableData,
            handleEdit,
            handleDelete,
            handleCancel,
            dialogVisible,
            dialogForm,
            updateCon,
            menuHandle,
            statusHandle,
            roleList,
            handleCheckChange,
            dataTree,
            defaultProps,
            handlechecked
        }
    }
});

</script>