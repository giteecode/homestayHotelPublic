<template>

  <div class="home">
    <span @click.stop="toggleDark()">暗黑模式</span>
    <el-switch size="large" v-model="isDark" inline-prompt :active-icon="Moon" :inactive-icon="Sunny"
      style="--el-switch-on-color: #2c2c2c; --el-switch-off-color: #f2f2f2" />
  </div>
  <h1>nihao 你好啊</h1>
</template>

<script lang="ts">
import { ref, defineComponent } from 'vue';
// import HelloWorld from '@/components/HelloWorld.vue'; // @ is an alias to /src
import { useDark, useToggle } from '@vueuse/core'
import { Moon, Sunny } from '@element-plus/icons-vue';


export default defineComponent({
  name: 'HomeView',
  components: {

  },
  setup() {
    const isDark = useDark()
    const toggleDark = useToggle(isDark)
    return {
      isDark,
      toggleDark,
      Sunny,
      Moon
    }
  }
});
</script>
