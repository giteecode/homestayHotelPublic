<!--  -->
<template>
  <!-- 自定义容器布局,<Breadcrumb />组件不再进行开发 -->
  <!-- <div>
    <HeaderBar />
    <div style="display:flex">
      <Aside />
      <div style="flex:1">
        <Tabs />
        <router-view style="flex:1" />
      </div>
    </div>
  </div> -->

  <!-- element-plus容器布局 -->
  <div class="common-layout">
    <el-container >
      <el-header>
        <HeaderBar />
      </el-header>
      <el-container>
        <el-aside width="auto">
          <Aside />
        </el-aside>
        <el-container>
          <el-main>
            <Tabs />
            <router-view style="flex:1" />
          </el-main>
        </el-container>
      </el-container>
    </el-container>
  </div>

</template>

<script lang='ts'>
import { defineComponent } from 'vue';
import HeaderBar from '@/components/HeaderBar.vue'
import Aside from '@/components/Aside.vue';
import Tabs from '@/components/Tabs.vue';
import Breadcrumb from '@/components/Breadcrumb.vue'
export default defineComponent({
  name: 'Layout',
  components: {
    HeaderBar,
    Aside,
    Tabs,
    Breadcrumb
  }
});
</script>
<style scoped>
/* element-plus容器布局 */
.el-main { 
  height: calc(100vh - 4rem);
}
</style>