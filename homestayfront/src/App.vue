<template>
  <!-- <nav>
    <router-link to="/">Home</router-link> |
    <router-link to="/about">About</router-link>
  </nav> -->
  <router-view />
  <!-- <keep-alive exclude="searchResult">
    <router-view :key="$route.fullPath"></router-view>
  </keep-alive> -->

</template>

<script lang="ts" setup>
import store from '@/store'
import { onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

// 路由与导航动态绑定
const route = useRoute();
const router = useRouter();
const whitePath = ['/login', '/dashboard', '/']
watch(route, (to: any, from: any) => {
  // console.log("to" + to.name)
  // console.log(to.path)
  // console.log('from:' + from);
  // console.log("to：" + to.path)
  if (whitePath.indexOf(to.path) === -1) {
    // console.log("to.path=" + to.path)
    let obj = {
      menuName: to.name,
      menuUrl: to.path
    }

    store.commit("ADD_TABS", obj)
  }

}, { deep: true, immediate: true })
// onMounted(() => {
//   getIp()
// })
// const getIp = () => {
 
//   sessionStorage.setItem('ip', returnCitySN['cip'] || '0.0.0.0')
//   sessionStorage.setItem('city', returnCitySN['cname'] || '未知')
// }
</script>



<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  /* color: #2c3e50; */
  padding: 0px;
}

nav {
  padding: 30px;
}
body{
  margin: 0px;
}
nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
