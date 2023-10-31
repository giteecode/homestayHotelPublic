<!-- 登录 -->
<template>
  <div class="BackgroundColor" style="margin:0px;width:100%;height:100vh;overflow: hidden;">
    <div class="Glassmorphism" style="width:350px; margin:50px auto;padding: 10px 30px; ">
      <div style="font-size:26px;text-align: center;padding: 30px 0">欢迎登录民宿信息管理系统</div>
      <el-form :model="loginForm" :rules="rules" size="normal" @keyup.enter.native="login()">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" :prefix-icon="User" placeholder="请输入用户名" style="margin:10px auto">
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" :prefix-icon="Unlock" type="password" show-password placeholder="密码"
            style="margin:10px auto"></el-input>
        </el-form-item>
        <el-form-item style="margin:10px auto">
          <div style="display: flex">
            <el-input :prefix-icon="Key" v-model="loginForm.validCode"
              style="width: 40%;height: 40px;margin-right:10px;" placeholder="验证码"></el-input>
            <ValidCode @imgCode="createImgCode" />
          </div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="login()">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script lang='ts'>
import request from '@/utils/request';
import ValidCode from '@/components/ValidCode.vue';
import { defineComponent, reactive, ref, onMounted, shallowRef, watch, onActivated, computed } from 'vue';
import store from '@/store';
// import { useRoute, useRouter } from 'vue-router'
import { User, Unlock, Key } from '@element-plus/icons-vue'
import router from "@/router/index";
import { ElMessage, FormRules } from "element-plus";
import { useDark, useToggle } from '@vueuse/core'
export default defineComponent({
  name: 'Login',
  components: {
    ValidCode
  },
  setup() {
    const formItem: any = reactive({
      username: '',
      password: '',
      validCode: ''
    })
    const loginForm = ref(formItem)

    //判断是否输入
    const rules = reactive<FormRules>({
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
      ],
      password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
    })

    //验证码生成并获取子级组件中的验证码
    const imgCode = ref('')
    const createImgCode = (data: any) => {  //获取子级组件返回的验证码，进行比对
      // console.log(data)
      imgCode.value = data
    }
    const login = () => {
      if (!loginForm.value.validCode) {
        // console.log(ValidCode.draw())
        ElMessage.error('验证码为空')
        return
      } else {
        if (loginForm.value.validCode.toLowerCase() == imgCode.value.toLowerCase()) {  //全部转小写在进行比对
          // console.log('正确') 
          request.post('/sys/user/login', loginForm.value).then((res: any) => {
            if (res.code === '0' || res.code === '200') {
              ElMessage({
                message: '登录成功',
                type: 'success',
              })
              store.commit('SET_TOKEN', res.data.tokenInfo.tokenValue)
              store.commit('SET_MENULIST', res.data.menuList)
              store.commit('SET_USERINFO', res.data.user)
              router.push('/back') //登陆成功跳转到主页
            } else {
              ElMessage.error(res.msg)
            }
          }).catch(err => {
            ElMessage.error('服务器连接失败')

          })

        } else {
          ElMessage.error('验证码错误')
        }


      }

    }
    // window.location.reload()
    onMounted(() => {
      isLogin()
      testDev()

    })
    // 关闭暗黑模式，防止input样式第一次进来被黑暗模式覆盖
    const isDark = useDark()
    isDark.value = false
    const toggleDark = useToggle(isDark)
    const testDev = () => {
      // const theme = computed(() => isDark.value ? 'dark' : 'light')
      // console.log(isDark.value);

      loginForm.value.username = 'admin'
      loginForm.value.password = 'admin'
      loginForm.value.validCode = imgCode.value
    }

    //本地有token和后台用户信息则转发到后台首页
    const isLogin = () => {
      const user = store.getters.GET_USERINFO
      const token = store.getters.GET_TOKEN
      if (user != 0 &&  token != 0) {
        router.push('/back')
      }
    }


    return {
      loginForm,
      rules,
      User,
      Unlock,
      Key,
      login,
      imgCode,
      createImgCode,
      // radioList
    };
  }

});
</script>
<style scoped>
body {
  margin: 0px;
}

.BackgroundColor {
  /* background: #2193b0;
    background: -webkit-linear-gradient(to right, #6dd5ed, #2193b0);
    background: linear-gradient(to right, #6dd5ed, #2193b0); */
  margin: 0px;

  background: linear-gradient(45deg, #C7F5FE 10%, #C7F5FE 40%, #FCC8F8 40%, #FCC8F8 60%, #EAB4F8 60%, #EAB4F8 65%, #F3F798 65%, #F3F798 90%);
}

.Glassmorphism {
  background: rgba(80, 227, 194, 0.25);
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
  backdrop-filter: blur(4px);
  -webkit-backdrop-filter: blur(4px);
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.18);
}
</style>


