<template>
  <div style="
      height: 50px;
      line-height: 50px;
      border-bottom: 1px solid #cccccc;
      display: flex;
    ">
    <div>
      <!-- style="
        width: 200px;
        padding-left: 30px;
        font-weight: bold;
        color: #0081cf;
        font-size: 26px;
      " -->
      <el-link :underline="false" :icon="House" href="/back" style="font-size: 22px;padding-left: 30px;">民宿信息管理系统</el-link>

    </div>

    <div style="flex: 1"></div>
    <div>
      <!-- <span @click.stop="toggleDark()">明暗切换</span> -->
      <el-switch size="large" v-model="isDark" inline-prompt :active-icon="Moon" :inactive-icon="Sunny"
        active-text="Open" inactive-text="Close"
        style="--el-switch-on-color: #2c2c2c; --el-switch-off-color: #f2f2f2" />
    </div>
    <div class="rightBar"
      style="width: 240px;height: 50px;line-height: 50px;display:flex;align-items: center;justify-content:center;flex-wrap: nowrap;">
      <el-avatar :size="30" :src="admin.avatar" @error="errorHandler">
        <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png" />
      </el-avatar>
      <el-dropdown style="height: 50px;line-height: 50px;">
        <span class="el-dropdown-link"
          style="min-width:120px;margin-left: 20pxdisplay:flex;align-items: center;justify-content:center;flex-wrap: nowrap;;">
          欢迎：
          <el-tooltip v-if="tipsShow" placement="left">
            <template #content> {{nickFullName}} </template>
            {{nickName}}
          </el-tooltip>
          <span v-else>{{nickName}}</span>
          
          <el-icon class="el-icon--right">
            <ArrowDown />
          </el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>
              <el-link :underline="false" style="font-size:18px;" href="/userInfo">个人信息</el-link>
            </el-dropdown-item>
            <!-- <el-dropdown-item style="font-size:18px;" @click="$router.push('/adminInfo')"></el-dropdown-item> -->
            <el-dropdown-item style="font-size:18px;margin-top: 5px;" @click="$router.push('/login'),tuichu()">退出系统
            </el-dropdown-item>
            <!-- <el-dropdown-item>Action 3</el-dropdown-item>
            <el-dropdown-item disabled>Action 4</el-dropdown-item>
            <el-dropdown-item divided>Action 5</el-dropdown-item> -->
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script lang="ts">
import { ArrowDown, Moon, Sunny, House } from '@element-plus/icons-vue'
import { useDark, useToggle } from '@vueuse/core'
import { onMounted, ref } from "vue";
import router from "@/router";
import store  from '@/store';


export default {
  name: "HeaderBar",
  components: {
    ArrowDown
  },
  setup() {
    const admin: any = ref({})
    const nickName = ref()
    const tipsShow = ref(true)
    const nickFullName =ref()
    const load = () => {
      admin.value = sessionStorage.getItem('userInfo')
      if (admin.value) {
        // 将JSON格式字符串转换为js对象
        admin.value = JSON.parse(admin.value)
        nickName.value = admin.value['username']
        if (nickName.value.length > 8) {
          // console.log('大于三');
          nickFullName.value = nickName.value
          nickName.value = nickName.value.substring(0, 8)
          nickName.value += '...'
          tipsShow.value = true
        }else{
          tipsShow.value = false
        }
      }
      else {
        console.log("我是headerbar过来的，转发到/login");
        
        router.replace('/login')
      }


    }
    onMounted(() => {
      load()
      // 触发mutations中的某个方法
      // store.commit('GET_ADMININFO')
      // store.getters('GET_INFO')
      //读取state值
      // console.log(store.state.counter)
      // 触发getters中的某个方法
      // console.log(store.getters.GET_INFO)
      // 触发action中的方法
      // store.dispatch('logout')
    })

    //退出系统，删除缓存
    const tuichu = () => {
      store.dispatch('logout')
      // let result = await requestUtil.get("/logout")
      // if (result.data.code == 200) {
      //   store.commit('SET_ROUTES_STATE', false);
      //   store.commit('RESET_TABS')
      //   store.dispatch('logout')
      // }
      // sessionStorage.clear()
      // router.go(0)
      // router.replace('/dashboard')
    }
    // 动态切换主题
    const isDark = useDark()
    const toggleDark = useToggle(isDark)

    // 头像
    const errorHandler = () => true
    return {
      admin,
      nickName,
      nickFullName,
      tuichu,
      isDark,
      toggleDark,
      Sunny,
      Moon,
      House,
      errorHandler,
      tipsShow
    }
  }
};
</script>

<style scoped>
.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;

}
</style>