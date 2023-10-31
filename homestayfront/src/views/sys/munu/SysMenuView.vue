<!--  -->
<template>
    <div style="padding:20px">
        <div class="menuTable">
            <!-- 功能区 -->
            <div style="margin:10px 0">
                <el-button type="primary" size="larg" @click="dialogVisible = true, dialogForm = {}, updateCon = false">
                    新增</el-button>
                <!-- <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button> -->
            </div>
            <el-radio-group v-model="tableLayout" style="float:left;margin: 5px;">
                <el-radio-button label="fixed" />
                <el-radio-button label="auto" />
            </el-radio-group>
            <el-table :data="tableData"  style="width: 100%; margin-bottom: 20px" row-key="menuId" border
                default-expand-all:false :table-layout="tableLayout" v-loading="loading">
                <el-table-column prop="menuId" label="menuId" sortable />
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
                <el-table-column prop="menuName" label="菜单名" />
                <el-table-column prop="menuType" label="Type" width="50" />
                <el-table-column prop="menuUrl" label="路径" />
                <!-- <el-table-column prop="menuIcon" label="图标" /> -->

                <el-table-column label="图标" prop="menuIcon">
                    <template #default="scope">
                        <el-icon>
                            <component :is="scope.row.menuIcon"></component>
                        </el-icon>
                        {{scope.row.menuIcon}}
                    </template>
                </el-table-column>
                <el-table-column prop="orderNum" label="排序" />
                <el-table-column prop="createTime" label="创建时间" sortable />
                <el-table-column prop="updateTime" label="更新时间" sortable />
                <el-table-column fixed="right" label="Operations" width="150">
                    <template #default="scope">
                        <el-button type="primary" size="small" @click="handleEdit(scope.row), updateCon = true">
                            编辑
                        </el-button>
                        <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.menuId)"
                            @cancel="handleCancel()">
                            <template #reference>
                                <el-button type="danger" size="small">删除</el-button>
                            </template>
                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>
        </div>



        <div class="dialog">
            <!--    弹窗提醒：新增or修改菜单-->
            <el-dialog v-model="dialogVisible" title="提示" width="30%">
                <el-form :model="dialogForm" label-width="120px">
                    <el-form-item label="状态">
                        <el-radio-group v-model="dialogForm.status">
                            <el-radio :label="0">开启</el-radio>
                            <el-radio :label="1">封禁</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="菜单名">
                        <el-input v-model="dialogForm.menuName" style="width: 80%" maxlength="40"></el-input>
                    </el-form-item>
                    <el-form-item label="类型">
                        <el-radio-group v-model="dialogForm.menuType" @change="menuTypeChange($event)">
                            <el-radio-button label="M">目录</el-radio-button>
                            <el-radio-button label="C">菜单</el-radio-button>
                            <el-radio-button label="F">按钮</el-radio-button>
                        </el-radio-group>
                    </el-form-item>

                    <el-form-item label="父级目录">
                        <div style="display: inline-block">
                            <el-select :disabled="radioDisabled" v-model="dialogForm.parentId" placeholder="Select">

                                <el-option v-for="item in parentsList" :key="item.menuId" :label="item.menuName"
                                    :value="item.menuId" :disabled="item.disabled" />
                            </el-select>
                        </div>
                    </el-form-item>
                    <el-form-item label="路径">
                        <el-input v-model="dialogForm.menuUrl" style="width: 80%" maxlength="50"></el-input>
                    </el-form-item>
                    <el-form-item label="图标">
                        <el-input v-model="dialogForm.menuIcon" style="width: 50%" maxlength="20"></el-input>
                        
                        <a style="color:#409EFF" href="https://element-plus.gitee.io/zh-CN/component/icon.html#%E5%9B%BE%E6%A0%87%E9%9B%86%E5%90%88" target="_Blank">选图标</a>
                        <span style="font-size：0.8em">这里只填写图标名即可，填错会出现系统错误，谨慎填写</span>
                    </el-form-item>
                    <el-form-item label="排序">
                        <el-input type="number" v-model="dialogForm.orderNum" placeholder="此项无需填写" style="width: 80%" maxlength="6">
                        </el-input>
                    </el-form-item>
                    <el-form-item label="可访问的角色">
                        <div style="display: inline-block">
                            <el-select v-model="dialogForm.menuHasRoleIds" multiple placeholder="Select"
                                style="width: 240px">
                                <el-option v-for="item in roleList" :key="item.id" :label="item.name"
                                    :value="item.id" />
                            </el-select>
                        </div>
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

export default defineComponent({
    name: 'SysMenuView',
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
            request.get('/sys/menu', {

            }).then((res: any) => {
                // console.log(res)
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
            getparentsList()
        })

        //用于判断弹窗中的操作是新增or修改
        const menuHandle = () => {
            if (updateCon.value == true) { //判断是否是更新操作
                console.log('edit:true')
                request.put('/sys/menu', dialogForm.value).then((res: any) => {
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
                request.post('/sys/menu', dialogForm.value).then((res: any) => {
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
                    getparentsList()
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
            // 控制父级菜单选择框
            if (row.parentId == 0) {
                radioDisabled.value = true
            } else {
                radioDisabled.value = false
            }
        }

        const handleDelete = (id: number) => {
            // console.log('handleDelete');
            request.delete('/sys/menu/' + id).then((res: any) => {
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
            console.log(row);
            if (row.children.length > 0) {
                ElMessage.error('该菜单下有子菜单，不可禁用')
                return
            }
            request.put('/sys/menu/status', row)
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
        // const choseRoleValues: any = ref([])
        //获取父级（目录）菜单列表
        const parentsList: any = ref([])
        const getparentsList = () => {
            request.get('/sys/menu/getParents', {})
                .then((res: any) => {
                    if (res.code == 200) {
                        parentsList.value = res.data
                        // console.log(parentsList.value);

                    } else {
                        ElMessage.error(res.msg)
                    }
                })
        }
        //选中菜单类型为目录时禁用选择父级目录
        const radioDisabled = ref(true)
        const menuTypeChange = (row: any) => {
            if (row === 'M') {
                radioDisabled.value = true
                dialogForm.value.parentId = 0
            } else {
                radioDisabled.value = false
                //非目录菜单不可选择根目录
                parentsList.value[0].disabled = true
            }

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
            parentsList,
            radioDisabled,
            menuTypeChange
        }
    }
});

</script>