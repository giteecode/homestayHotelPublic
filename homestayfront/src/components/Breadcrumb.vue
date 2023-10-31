<template>
    <div style="display:flex;margin:12px 0px">
        <el-breadcrumb :separator-icon="ArrowRight">
            <!-- <el-breadcrumb-item :to="{ path: '/' }">homepage</el-breadcrumb-item>
            <el-breadcrumb-item @click="add($event)">promotion management</el-breadcrumb-item>
            <el-breadcrumb-item>promotion list</el-breadcrumb-item>
            <el-breadcrumb-item>promotion detail</el-breadcrumb-item> -->
            <!-- <el-breadcrumb-item v-for="(item,index) in breadcrumbList">
                <span class="root" v-if="parentName && index>0">{{parentName}}&nbsp;&nbsp;/&nbsp;&nbsp;</span>
                <span v-if="index==breadcrumbList.length-1">{{item.name}}</span>
                <span class="root" v-else>{{item.name}}</span>
            </el-breadcrumb-item> -->
            <el-breadcrumb-item v-for="(item, index) in breadcrumbList" :key="index">
                <span class="no-redirect" v-if="index === breadcrumbList.length - 1">{{item.name}}</span>
                <span class="redirect" v-else @click="handleRedirect(item.path)">{{item.name}}</span>
            </el-breadcrumb-item>

        </el-breadcrumb>

    </div>
</template>

<script lang="ts">
import { defineComponent, ref, watch } from 'vue'
import { ArrowRight } from '@element-plus/icons-vue'
import { useRoute, useRouter } from 'vue-router'
export default defineComponent({
    name: 'Breadcrumb',
    components: {

    },
    setup() {
        // 测试event
        const add = (evt: any) => {
            console.log(evt);
        }
        const route = useRoute();
        const breadcrumbList: any = ref([])
        const parentName: any = ref("")

        const initBreadcrumbList = () => {
            breadcrumbList.value = route.matched;

            parentName.value = route.meta.parentName;



        }

        watch(route, () => {
            initBreadcrumbList();
        }, { deep: true, immediate: true })
        
        const router = useRouter()
        const handleRedirect = (path:any) => {
            router.push(path)
        }
        return {
            ArrowRight,
            add,
            breadcrumbList,
            parentName,
            handleRedirect
        }
    }
})
</script>

<style scoped>

</style>