<template>
  <div style="padding:20px">
    <!-- 功能区 -->
    <div style="margin:10px 0">
      <el-button type="primary" @click="dialogVisible = true, form = {}, updateCon = false, bCheckUserName = false">新增
      </el-button>
      <!-- <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button> -->
    </div>
    <!-- 搜索区 -->
    <div style="margin:10px 0">
      <el-input placeholder="搜索用户名" v-model="search" clearable style="width:200px;margin-right: 10px;"></el-input>
      <el-button type="primary" @click="load">搜索</el-button>
    </div>

    <!-- 列表 -->
    <el-table :data="tableData" border style="width: 100%" v-loading="loading" stripe="true">
      <el-table-column prop="id" label="ID" width="80" sortable />
      <el-table-column label="状态" width="65">
        <template #default="scope">
          <span v-if="scope.row.status === 0">
            <el-button type="success" text bg @click="statusHandle(scope.row)">启用</el-button>
          </span>
          <span v-if="scope.row.status === 1">
            <el-button type="danger" text bg @click="statusHandle(scope.row)">封禁</el-button>
          </span>
        </template>
      </el-table-column>

      <el-table-column prop="username" label="用户名" width="100" />
      <el-table-column prop="realName" label="真实姓名" width="100" />
      <el-table-column prop="idCard" label="身份证" width="100" show-overflow-tooltip/>
      <el-table-column label="角色" width="100">
        <template #default="scope">
          <!-- <span v-if="scope.row.role === 1">管理员</span>
          <span v-if="scope.row.role === 2">普通用户</span> -->
          <span v-for="item in roleList" :key="item.id">
            <span v-if="scope.row.role === item.id" style="color:#409EFF">{{ item.name }}</span>
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="gender" label="性别" width="50" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="phonenumber" label="电话" />
      <el-table-column prop="loginDate" label="最后登陆" />
      <el-table-column prop="createTime" label="创建日期" />
      <el-table-column prop="updateTime" label="最后修改日期" />
      <el-table-column prop="address" show-overflow-tooltip label="地址" />
      <el-table-column prop="remark" show-overflow-tooltip label="备注" />


      <el-table-column fixed="right" label="Operations" width="150">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row), updateCon = true ,bCheckUserName = false">
            编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)" @cancel="handleCancel()">
            <template #reference>
              <el-button type="danger" size="small">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <div style="margin:10px 0">
      <el-pagination v-model:currentPage="currentPage" v-model:page-size="pageSize" :page-sizes="[50, 100, 200, 300]"
        :small="small" :disabled="disabled" :background="background" layout="total, sizes, prev, pager, next, jumper"
        :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </div>

    <!--    弹窗提醒：新增or修改用户-->
    <el-dialog v-model="dialogVisible" title="提示" width="30%">
      <el-form :model="form" label-width="120px">
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">开启</el-radio>
            <el-radio :label="1">封禁</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input @blur="checkUserName" v-model="form.username" style="width: 50%" maxlength="20"></el-input>
          <span v-if="bCheckUserName" style="color:red;margin-left:10px">用户名重复!!</span>
        </el-form-item>

        <el-form-item label="权限分类">
          <div style="display: inline-block">
            <el-select v-model="form.role" placeholder="请选择权限">
              <el-option v-for="item in roleList" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </div>
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" style="width: 80%" maxlength="20"></el-input>
        </el-form-item>
        <el-form-item label="身份证号码">
          <el-input v-model="form.idCard" style="width: 80%" maxlength="20"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" style="width: 80%" placeholder="不填则默认：123456"></el-input>
        </el-form-item>

        <el-form-item label="性别">
          <el-radio v-model="form.gender" label="男">男</el-radio>
          <el-radio v-model="form.gender" label="女">女</el-radio>
          <el-radio v-model="form.gender" label="未知">未知</el-radio>
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input v-model="form.email" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phonenumber" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input type="textarea" v-model="form.address" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="form.remark" style="width: 80%"></el-input>
        </el-form-item>
      </el-form>
      <template #footer="target">
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="dialogVisible = false, userHandle()">确认</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>
    
<script lang="ts">
import { defineComponent, onMounted, reactive, ref } from 'vue';
import router from "@/router";
import request from '@/utils/request';
import { ElMessage } from 'element-plus'


export default defineComponent({
  name: 'SysUserView',
  components: {

  },
  setup() {
    //加载动画
    const loading = ref(true)

    //表中数据
    const tableData = ref([])
    // 分页
    const currentPage = ref(1)
    const pageSize = ref(50)
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
    // 弹窗控制和数据
    const dialogVisible = ref(false)
    const form: any = ref()
    // 新增用户操作
    const updateCon = ref(false)  //这里我实在解决不了form.id的问题了，只好加了变量控制请求路径，是新增还是更新
    const userHandle = () => {
      if (updateCon.value == true) { //判断是否是更新操作
        // console.log('edit:true')
        request.put('/sys/user', form.value).then((res: any) => {
          if (res.code === '0' || res.code === '200') {
            ElMessage({
              message: '更新成功',
              type: 'success',
            })
          } else {
            ElMessage.error('更新失败')
          }
          load()
        }).catch(err => {
          // console.log(err)
          ElMessage.error('更新失败')
        })
        dialogVisible.value = false
      } else {
        // console.log('edit:false')
        request.post('/sys/user', form.value).then((res: any) => {
          // console.log(res)
          if (res.code === '0' || res.code === '200') {
            ElMessage({
              message: '添加成功',
              type: 'success',
            })
          } else {
            ElMessage.error('添加失败')
          }
          load()
        }).catch(err => {
          // console.log(err)
          ElMessage.error('添加失败')
        })
        dialogVisible.value = false
      }
    }
    // table数据初始化
    const search = ref()
    const load = () => {
      request.get('/sys/user', {
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
      getRoleList()
    })
    // 修改操作
    const handleEdit = (row: any) => {
      // console.log('edit');
      // console.log(row.id)
      //跟表中数据隔离开，深拷贝，独立对象，避免点击取消数据丢失
      form.value = JSON.parse(JSON.stringify(row))
      dialogVisible.value = true
    }
    const handleDelete = (id: number) => {
      console.log(id)
      request.delete('/sys/user/' + id).then(res => {
        ElMessage({
          message: '删除成功',
          type: 'success',
        })
        load()
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

      request.put('/sys/user/status', row)
        .then((res: any) => {
          if (res.code == 200) {
            load()
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
    // 检查用户名重复
    const bCheckUserName = ref(false)
    const checkUserName = () => {
      // console.log(form.value.username);
      if (updateCon.value == true) {
        //更新操作
        bCheckUserName.value = false
      } else {
        //新增操作
        request.get('/sys/user/checkUserName', {
          params: {
            username: form.value.username,
          }
        })
          .then((res: any) => {
            console.log(res);

            if (res.code == 200) {
              bCheckUserName.value = false
            } else {
              bCheckUserName.value = true
              ElMessage.error(res.msg)
            }
          })
      }


    }
    return {
      loading,
      tableData,
      search,
      handleEdit,
      handleDelete,
      currentPage,
      pageSize,
      total,
      small,
      background,
      disabled,
      handleSizeChange,
      handleCurrentChange,
      dialogVisible,
      form,
      updateCon,
      userHandle,
      handleCancel,
      load,
      statusHandle,
      roleList,
      bCheckUserName,
      checkUserName
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
    