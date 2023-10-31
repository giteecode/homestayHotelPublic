<template>
    <div class="registerData">
        <!-- <countTo startVal='0' endVal='999' :duration='3000'></countTo> -->
        <div class="registerData">本年新增注册用户数：
            <CountTo :end="yearVal" />
        </div>
        <div class="registerData">本月新增注册用户数：
            <CountTo :end="monthVal" />
        </div>
        <div class="registerData">本周新增注册用户数：
            <CountTo :end="weekVal" />
        </div>
    </div>
    <div class="echarts-box">
        <div id="RoomEchart" :style="{ width: '700px', height: '60vh' }" ref="RoomEchart"></div>
        <!-- <div id="TestEchart" :style="{ width: '700px', height: '60vh' }" ref="TestEchart"></div> -->
    </div>
    <!-- <div></div> -->
</template>
    
<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import * as echarts from 'echarts';
// 防止typescript报错d.ts
// @ts-ignore
// import countTo from 'vue-count-to';
import CountTo from '@/components/CountTo.vue'
import request from '@/utils/request';
export default defineComponent({
    name: 'DashboardView',
    components: {
        // countTo 
        CountTo
    },
    setup() {

        onMounted(() => {
            getRoomData()
            getData()
            initRoomChart()

            // TestChart()
        })
        // 初始化注册数据
        const yearVal = ref()
        const monthVal = ref()
        const weekVal = ref()
        const getData = () => {
            request.get('/h/user/registerData').then((res: any) => {
                if (res.code == 200) {
                    yearVal.value = res.data.year
                    monthVal.value = res.data.month
                    weekVal.value = res.data.week
                }
            })
        }
        // 房间状态图表展示
        const resData = ref()
        const getRoomData = async () => {
            await request.get('/h/room/roomData').then((res: any) => {
                if (res.code == 200) {
                    resData.value = res.data
                }
            })
        }
        const RoomEchart: any = ref<HTMLElement | null>(null)
        const initRoomChart = async () => {
            await getRoomData();

            const chart = ref<any>()
            chart.value = echarts.init(RoomEchart.value!)
            chart.value.setOption({
                title: {
                    text: '房间使用率',
                    subtext: new Date,
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical',
                    left: 'left'
                },
                series: [
                    {
                        name: '民宿房间使用率',
                        type: 'pie',
                        radius: '60%',
                        data: [
                            { value: resData.value['预订'], name: '预订', itemstyle: { color: '#73c0de' } },
                            { value: resData.value['入住'], name: '入住', itemStyle: { color: '#fac858' } },
                            { value: resData.value['空闲'], name: '空闲', itemStyle: { color: '#91cc75' } },
                        ],
                        emphasis: {
                            itemStyle: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            });
        }
        // 测试用图表（遍历数组）
        // const testData = [
        //     { name: '一月', value: '55' },
        //     { name: '二月', value: '55' },
        //     { name: '三月', value: '55' },
        // ]
        // const TestEchart: any = ref<HTMLElement | null>(null)
        // const TestChart = () => {

        //     const chart = ref<any>()
        //     chart.value = echarts.init(TestEchart.value!)
        //     chart.value.setOption({
        //         title: {
        //             text: '测试',
        //             subtext: new Date,
        //             left: 'center'
        //         },
        //         tooltip: {
        //             trigger: 'none',
        //             axisPointer: {
        //                 type: 'cross'
        //             }
        //         },
        //         legend: {
        //             orient: 'vertical',
        //             left: 'left'
        //         },
        //         series: [
        //             {
        //                 name: '测试',
        //                 type: 'pie',
        //                 radius: '60%',
        //                 data: testData,
        //                 emphasis: {
        //                     itemStyle: {
        //                         shadowBlur: 10,
        //                         shadowOffsetX: 0,
        //                         shadowColor: 'rgba(0, 0, 0, 0.5)'
        //                     }
        //                 }
        //             }
        //         ]
        //     });
        // }
        return {
            RoomEchart,
            yearVal,
            monthVal,
            weekVal,
            // TestEchart
        }
    }

});
</script>
<style scoped>
.registerData {
    margin: 10px;
}
</style>