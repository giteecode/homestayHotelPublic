<template>
  <div class="container">
    <div class="userInfo">
      <div>
        <el-form :model="updateForm" label-width="120px">
          <el-form-item label="更新头像">
            <!-- <el-avatar :size="70" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
                            style="cursor: pointer;" /> -->
            <el-upload
              class="avatar-uploader"
              :action="filesUploadUrl"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
            >
              <!-- <img v-if="imageUrl" :src="imageUrl" class="avatar" /> -->
              <el-image
                v-if="imageUrl"
                style="width: 100px; height: 100px"
                :src="imageUrl"
              ></el-image>
              <el-icon v-else class="avatar-uploader-icon">
                <Plus />
              </el-icon>
            </el-upload>
          </el-form-item>
          <el-form-item label="用户名">
            <el-input
              v-model="updateForm.username"
              style="width: 60%"
              maxlength="16"
            >
            </el-input>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input
              v-model="updateForm.nickName"
              style="width: 60%"
              maxlength="16"
            >
            </el-input>
          </el-form-item>
          <el-form-item label="手机号">
            <el-input
              v-model="updateForm.phonenumber"
              style="width: 60%"
              maxlength="15"
            >
            </el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="updateForm.email" style="width: 60%"> </el-input>
          </el-form-item>
          <el-form-item label="新密码">
            <el-input
              type="password"
              v-model="newPassword1"
              style="width: 60%"
              maxlength="16"
            >
            </el-input>
          </el-form-item>
          <el-form-item label="确认新密码">
            <el-input
              type="password"
              v-model="newPassword2"
              style="width: 60%"
              maxlength="16"
            >
            </el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio v-model="updateForm.gender" label="男">男</el-radio>
            <el-radio v-model="updateForm.gender" label="女">女</el-radio>
            <el-radio v-model="updateForm.gender" label="未知">未知</el-radio>
          </el-form-item>
          <el-form-item label="地址">
            <el-input
              type="textarea"
              v-model="updateForm.address"
              style="width: 60%"
            ></el-input>
          </el-form-item>
          <el-form-item label="备注">
            <el-input
              type="textarea"
              v-model="updateForm.remark"
              style="width: 60%"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="resetForm()">重置</el-button>
            <el-button type="primary" @click="updateInfo()">保存修改</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="loginLog">
      <div style="margin: 20px">登录日志</div>
      <el-timeline>
        <el-timeline-item
          v-for="(activity, index) in activities"
          :key="index"
          :timestamp="activity.createTime"
        >
          {{ activity.operation }}
        </el-timeline-item>
      </el-timeline>
      <!-- 分页 -->
      <div style="margin: 10px 0">
        <el-pagination
          v-model:currentPage="currentPage"
          v-model:page-size="pageSize"
          :small="small"
          :disabled="disabled"
          :background="background"
          layout="total,  prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'
import type { UploadProps } from 'element-plus'
export default defineComponent({
  name: 'UserInfoView',
  components: {},
  setup() {
    // 分页
    const currentPage = ref(1)
    const pageSize = ref(10) //分页默认显示条数
    const total = ref(0)
    const small = ref(false)
    const background = ref(false)
    const disabled = ref(false)
    const handleSizeChange = (val: number) => {
      //改变每页数量
      console.log(`${val} items per page`)
      initActivities()
    }
    const handleCurrentChange = (val: number) => {
      //改变当前页码位置
      console.log(`current page: ${val}`)
      initActivities()
    }

    // 时间线初始化
    const activities: any = ref([])
    const initActivities = () => {
      request
        .get('/sys/log/getLoginInfo', {
          params: {
            pageNum: currentPage.value,
            pageSize: pageSize.value,
          },
        })
        .then((res) => {
          activities.value = res.data.records
          // activities.value.reverse() 倒排序
          total.value = res.data.total
          // loading.value = false
        })
    }
    onMounted(() => {
      initActivities()
      getUserInfo()
    })
    // onBeforeMount(() =>{
    //     getUserInfo()
    // })

    // 个人信息修改，数据初始化
    const updateForm: any = ref({})
    const getUserInfo = () => {
      request
        .get('/sys/user/getUserInfo', {})
        .then((res: any) => {
          if (res.code == 200) {
            updateForm.value = res.data
            imageUrl.value = updateForm.value.avatar
          } else {
            ElMessage.error(res.msg)
          }
        })
        .catch(() => {
          ElMessage.error('请求出错')
        })
    }
    // watch(updateForm,() =>{
    //     getUserInfo()
    // },{deep:true})

    // 判断密码是否输入一致
    const newPassword1 = ref()
    const newPassword2 = ref()
    const checkPassword = () => {
      if (newPassword1.value || newPassword2.value) {
        if (newPassword1.value === newPassword2.value) {
          // console.log('我进来了修改了');
          if (newPassword2.value.length < 5) {
            ElMessage.error('密码不能少于6位')
            return false
          } else {
            updateForm.value.password = newPassword2
            return true
          }
        } else {
          ElMessage.error('密码不一致')
          return false
        }
      } else {
        return true
      }
    }
    //更新个人信息操作
    const updateInfo = () => {
      // console.log(updateForm.value);
      if (newPassword1.value || newPassword2.value) {
        if (checkPassword() == false) {
          //两次密码不符合规则
          return
        }
      }
      request
        .put('/sys/user/updateInfo', updateForm.value)
        .then((res: any) => {
          if (res.code === '0' || res.code === '200') {
            ElMessage.success('更新成功')
          } else {
            ElMessage.error('更新失败')
          }
          getUserInfo()
        })
        .catch((err) => {
          ElMessage.error('更新失败')
        })
    }
    // 重置按钮
    const resetForm = () => {
      getUserInfo()
    }
    // 上传头像图片区
    const filesUploadUrl = 'http://localhost:8091/files/upload'
    const imageUrl = ref('')

    const handleAvatarSuccess: UploadProps['onSuccess'] = (
      response,
      uploadFile
    ) => {
      // console.log(response);

      imageUrl.value = URL.createObjectURL(uploadFile.raw!)
      updateForm.value.avatar = response.data
      // console.log('我是头像地址：'+updateForm.value.avatar);
    }

    const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
      if (rawFile.type !== 'image/jpeg') {
        ElMessage.error('头像必须是JPG格式!')
        return false
      } else if (rawFile.size / 1024 / 1024 > 2) {
        ElMessage.error('头像不可以大于2MB!')
        return false
      }
      return true
    }
    return {
      currentPage,
      pageSize,
      total,
      small,
      background,
      disabled,
      handleSizeChange,
      handleCurrentChange,
      activities,
      updateForm,
      newPassword1,
      newPassword2,
      checkPassword,
      updateInfo,
      resetForm,
      filesUploadUrl,
      imageUrl,
      handleAvatarSuccess,
      beforeAvatarUpload,
    }
  },
})
</script>

<style scoped>
.container {
  padding: 20px;
  /* width: 100%; */
  display: flex;
}

.userInfo {
  width: 60%;
  margin-right: 20px;
}

.loginLog {
  width: 30%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>
