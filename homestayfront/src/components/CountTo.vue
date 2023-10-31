<template>
  <!-- 数字滚动动画组件，来源地址：https://github.com/kinoaa/kinoaa-components 博客地址：http://www.soiiy.com/Vue-js/6113779.html-->
</template>

<script lang="ts">
import {
  defineComponent, reactive, computed, onMounted, watch, onUnmounted
} from 'vue'
const props = {
// start	开始值	Number	0
// end	结束值	Number	0
// duration	持续时间	Number	0
// autoplay	自动播放	Boolean	true
// decimals	要显示的小数位数	Number	0
// decimal	十进制分割	String	,
// separator	分隔符	String	,
// prefix	前缀	String	''
// suffix	后缀	String	''
// useEasing	使用缓和功能	Boolean	true
// easingFn	缓和回调	Function	-
  start: {
    type: Number,
    required: false,
    default: 0
  },
  end: {
    type: Number,
    required: false,
    default: 0
  },
  duration: {
    type: Number,
    required: false,
    default: 5000
  },
  autoPlay: {
    type: Boolean,
    required: false,
    default: true
  },
  decimals: {
    type: Number,
    required: false,
    default: 0,
    validator(value:any) {
      return value >= 0
    }
  },
  decimal: {
    type: String,
    required: false,
    default: '.'
  },
  separator: {
    type: String,
    required: false,
    default: ','
  },
  prefix: {
    type: String,
    required: false,
    default: ''
  },
  suffix: {
    type: String,
    required: false,
    default: ''
  },
  useEasing: {
    type: Boolean,
    required: false,
    default: true
  },
  easingFn: {
    type: Function,
    default(t:any, b:any, c:any, d:any) {
      return c * (-Math.pow(2, -10 * t / d) + 1) * 1024 / 1023 + b
    }
  }
}
export default defineComponent({
  name: 'CountTo',
  props: props,
  emits: ['onMountedcallback', 'callback'],
  setup(props, {emit}) {
    const isNumber = (val:any) => {
      return !isNaN(parseFloat(val))
    }
    // 格式化数据，返回想要展示的数据格式
    const formatNumber = (val:any) => {
      val = val.toFixed(props.start)
      val += ''
      const x = val.split('.')
      let x1 = x[0]
      const x2 = x.length > 1 ? props.decimal + x[1] : ''
      const rgx = /(\d+)(\d{3})/
      if (props.separator && !isNumber(props.separator)) {
        while (rgx.test(x1)) {
          x1 = x1.replace(rgx, '$1' + props.separator + '$2')
        }
      }
      return props.prefix + x1 + x2 + props.suffix
    }
    const state = reactive<{
      localStart: number
      displayValue: number|string
      printVal: any
      paused: boolean
      localDuration: any
      startTime: any
      timestamp: any
      remaining: any
      rAF: any
    }>({
      localStart: props.start,
      displayValue: formatNumber(props.start),
      printVal: null,
      paused: false,
      localDuration: props.duration,
      startTime: null,
      timestamp: null,
      remaining: null,
      rAF: null
    })
    // 定义一个计算属性，当开始数字大于结束数字时返回true
    const stopCount = computed(() => {
      return props.start > props.end
    })
    const startCount = () => {
      state.localStart = props.start
      state.startTime = null
      state.localDuration = props.duration
      state.paused = false
      state.rAF = requestAnimationFrame(count)
    }

    watch(() => props.start, () => {
      if (props.autoPlay) {
        startCount()
      }
    })

    watch(() => props.end, () => {
      if (props.autoPlay) {
        startCount()
      }
    })
    // dom挂在完成后执行一些操作
    onMounted(() => {
      if (props.autoPlay) {
        startCount()
      }
      emit('onMountedcallback')
    })
    const count = (timestamp:any) => {
      if (!state.startTime) state.startTime = timestamp
      state.timestamp = timestamp
      const progress = timestamp - state.startTime
      state.remaining = state.localDuration - progress
      // 是否使用速度变化曲线
      if (props.useEasing) {
        if (stopCount.value) {
          state.printVal = state.localStart - props.easingFn(progress, 0, state.localStart - props.end, state.localDuration)
        } else {
          state.printVal = props.easingFn(progress, state.localStart, props.end - state.localStart, state.localDuration)
        }
      } else {
        if (stopCount.value) {
          state.printVal = state.localStart - ((state.localStart - props.end) * (progress / state.localDuration))
        } else {
          state.printVal = state.localStart + (props.end - state.localStart) * (progress / state.localDuration)
        }
      }
      if (stopCount.value) {
        state.printVal = state.printVal < props.end ? props.end : state.printVal
      } else {
        state.printVal = state.printVal > props.end ? props.end : state.printVal
      }

      state.displayValue = formatNumber(state.printVal)
      if (progress < state.localDuration) {
        state.rAF = requestAnimationFrame(count)
      } else {
        emit('callback')
      }
    }
    // 组件销毁时取消动画
    onUnmounted(() => {
      cancelAnimationFrame(state.rAF)
    })
    return () => (
      state.displayValue
    )
  }
})
</script>

<style scoped>

</style>