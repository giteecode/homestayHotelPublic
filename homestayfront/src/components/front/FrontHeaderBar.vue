<template>
  <div>
    <el-menu
      :default-active="activeIndex"
      router
      class="el-menu-demo"
      mode="horizontal"
      :ellipsis="false"
      @select="handleSelect"
    >
      <el-menu-item index="/">民宿预订管理系统</el-menu-item>
      <div class="flex-grow" />
      <el-menu-item index="/">首页</el-menu-item>
      <el-menu-item
        index
        @click="dialogRegister = true"
        v-show="loginedShow == false"
        >注册</el-menu-item
      >
      <el-menu-item
        index
        @click="dialogLogin = true"
        v-show="loginedShow == false"
        >登录</el-menu-item
      >

      <el-sub-menu v-show="loginedShow" index>
        <template #title>
          <el-avatar :size="36" :src="user.avatar" @error="errorHandler">
            <img
              src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
            /> </el-avatar
          >个人中心
        </template>
        <el-menu-item index="/front/userInfo">个人信息</el-menu-item>
        <el-menu-item index="/front/myorder">我的订单</el-menu-item>
        <el-menu-item index @click="$router.push('/'), tuichu()"
          >安全退出</el-menu-item
        >
        <!-- <el-sub-menu index="2-4">
                    <template #title>item four</template>
                    <el-menu-item index="2-4-1">item one</el-menu-item>
                    <el-menu-item index="2-4-2">item two</el-menu-item>
                    <el-menu-item index="2-4-3">item three</el-menu-item>
                </el-sub-menu> -->
      </el-sub-menu>
      <el-menu-item index>
        <el-switch
          size="large"
          v-model="isDark"
          inline-prompt
          :active-icon="Moon"
          :inactive-icon="Sunny"
          active-text="Open"
          inactive-text="Close"
          style="--el-switch-on-color: #2c2c2c; --el-switch-off-color: #f2f2f2"
      /></el-menu-item>
      <el-menu-item index="/login">后台管理</el-menu-item>
    </el-menu>

    <!-- 注册弹窗 -->
    <el-dialog v-model="dialogRegister" title="注册" width="30%">
      <register @res="closeDialog($event)" />
      <div>
        已有账号
        <el-button
          link
          type="primary"
          size="large"
          @click=";(dialogRegister = false), (dialogLogin = true)"
          >去登录</el-button
        >
      </div>
    </el-dialog>
    <!-- 登录弹窗 -->
    <el-dialog v-model="dialogLogin" title="登录" width="30%">
      <login @res="closeDialog($event)" />
      <div>
        没有账号
        <el-button
          link
          type="primary"
          size="large"
          @click=";(dialogLogin = false), (dialogRegister = true)"
          >去注册</el-button
        >
      </div>
      <div style="margin: 10px 0">
        忘记密码
        <el-button
          link
          type="primary"
          size="large"
          @click="
            ;(dialogLogin = false),
              (dialogRegister = false),
              $router.push('/front/forget')
          "
          >去找回</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ArrowDown, Moon, Sunny, House } from '@element-plus/icons-vue'
import { useDark, useToggle } from '@vueuse/core'
import { onMounted, ref } from 'vue'
import store from '@/store'
import register from '@/components/front/register.vue'
import login from '@/components/front/login.vue'
import router from '@/router'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'
const route = useRoute()
// 动态切换主题
const isDark = useDark()
const toggleDark = useToggle(isDark)
//顶部导航栏
const activeIndex = ref('1')
const handleSelect = (key: string, keyPath: string[]) => {
  // console.log(key, keyPath)
}
// 登陆之后显示的菜单栏

const user = store.getters.GET_FRONTUSER
const token = store.getters.GET_TOKEN
const loginedShow = ref(false)
const load = () => {
  if (user != 0 && token != 0) {
    loginedShow.value = true
  }
}
onMounted(() => {
  // console.log(user);
  load()
})
// 弹窗
const dialogRegister = ref(false)
const dialogLogin = ref(false)
const closeDialog = (res: any) => {
  // console.log(res);
  dialogRegister.value = res
  dialogLogin.value = res
}

const tuichu = () => {
  window.sessionStorage.clear()
  ElMessage.success('退出成功')
  // ElMessage.success(route.path)
  // router.push('/index')
  // router.go(0)
  if (route.path === '/' || route.path === '/index') {
    router.go(0)
  } else {
    router.push('/index')
    setTimeout(() => {
      router.go(0)
    }, 1000)
  }
}
// 头像
const errorHandler = () => true
</script>

<style scoped>
.el-menu {
  height: 60px;
  overflow: hidden;
  /* align-items: center;垂直
    justify-content: center; 水平*/
}

.flex-grow {
  flex-grow: 1;
}
</style>
