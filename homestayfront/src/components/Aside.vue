<!--  -->
<template>
  <div style="margin-left: 18px">
    <!-- 展开、收缩 -->
    <el-radio-group v-model="isCollapse" style="margin: 10px">
      <el-radio-button :label="false">展开</el-radio-button>
      <el-radio-button :label="true">收起</el-radio-button>
    </el-radio-group>
    <el-row class="tac">
      <el-col :span="12">
        <el-menu
          :default-active="activeIndex"
          router
          class="el-menu-vertical-demo"
          :collapse="isCollapse"
          @open="handleOpen"
          @close="handleClose"
        >
          <el-menu-item index="/dashboard">
            <el-icon>
              <Promotion />
            </el-icon>
            <span>控制台</span>
          </el-menu-item>

          <!-- 测试遍历动态menu -->
          <template v-for="v in menuList" :key="v.menuId">
            <!-- 一级菜单没有url的绑定ID，防止和其他有url的一级菜单重名，导致出现点击一个子菜单会展开所有多级菜单 -->
            <el-sub-menu v-if="v.children" :index="v.menuId">
              <template #title>
                <el-icon> <component :is="v.menuIcon"></component> </el-icon
                ><span>{{ v.menuName }}</span>
              </template>
              <el-menu-item
                v-for="vitem in v.children"
                :key="vitem.menuId"
                :index="vitem.menuUrl"
                @click="openTab(vitem)"
              >
                <template #title>
                  <el-icon>
                    <component :is="vitem.menuIcon"></component> </el-icon
                  >{{ vitem.menuName }}
                </template>
              </el-menu-item>
            </el-sub-menu>
            <el-menu-item v-else :index="v.menuUrl" @click="openTab(v)">
              <el-icon>
                <component :is="v.menuIcon"></component>
              </el-icon>
              <template #title>
                <span>{{ v.menuName }}</span>
              </template>
            </el-menu-item>
          </template>
        </el-menu>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  onBeforeMount,
  onMounted,
  reactive,
  ref,
  shallowRef,
  watch,
} from 'vue'
import {
  Menu as IconMenu,
  Avatar,
  Document,
  Notebook,
  Picture,
  Promotion,
  Postcard,
  Tools,
  Operation,
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import router from '@/router'
import request from '@/utils/request'
import store from '@/store'

// import {storage , sessionStorage} from '@/utils/storage';

const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}

export default defineComponent({
  name: 'Aside',
  components: {
    // IconMenu,
    // Avatar,
    Document,
    Notebook,
    Picture,
    // Promotion,
    // Postcard,
    // Tools,
    // Operation
  },
  setup() {
    // 获取路由地址，控制高亮区域
    const activePath = router.currentRoute.value.fullPath
    const activeIndex = ref('/index')
    watch(
      store.state,
      () => {
        // console.log("editableTabsValue=" + store.state.editableTabsValue)
        activeIndex.value = store.state.editableTabsValue
      },
      { deep: true, immediate: true }
    )

    onMounted(() => {
      // load()
    })

    // 折叠面板
    const isCollapse = ref(false)
    const handleOpen = (key: string, keyPath: string[]) => {
      // console.log(key, keyPath)
    }
    const handleClose = (key: string, keyPath: string[]) => {
      // console.log(key, keyPath)
    }

    // 动态标签页
    // const editableTabsValue = ref(store.)
    // const elmenuItem = ref()
    const openTab = (item: any) => {
      // console.log(item);
      store.commit('ADD_TABS', item)
    }
    //这里用ref的话，vue给出警告Vue接收到一个组件，该组件被制成反应对象。这可能会导致不必要的性能开销，应该通过将组件标记为“markRaw”或使用“shallowRef”而不是“ref”来避免。
    // 如果使用 markRaw 那么currentComp将不永远不会再成为响应式对象。 所以得使用 shallowRef
    // ref 替换成 shallowRef。跟报警告的意思是一样的。

    const menuList = shallowRef(store.getters.GET_MENULIST)
    return {
      activePath,
      // admin,
      // role,
      isCollapse,
      handleOpen,
      handleClose,
      openTab,
      // elmenuItem,
      menuList,
      activeIndex,
    }
  },
})
</script>

<style scoped>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: calc(100vh - 110px);
}
</style>
