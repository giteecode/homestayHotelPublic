<template>
    <!-- <div style="margin-bottom: 20px">
        <el-button size="small" @click="addTab(editableTabsValue)">
            add tab
        </el-button>
    </div> -->
    <el-tabs v-model="editableTabsValue" type="card" class="demo-tabs" closable @tab-remove="removeTab"
        @tab-click="clickTab">
        <el-tab-pane v-for="item in editableTabs" :key="item.name" :label="item.title" :name="item.name">


        </el-tab-pane>
    </el-tabs>
</template>
    
<script lang="ts">
import store from '@/store';
import { ref, defineComponent, watch } from 'vue';
import router from "@/router";


export default defineComponent({
    name: 'Tabs',
    components: {

    },
    setup() {

        const editableTabsValue = ref(store.state.editableTabsValue)
        const editableTabs = ref(store.state.editableTabs)

        const removeTab = (targetName: string) => {
            const tabs = editableTabs.value
            let activeName = editableTabsValue.value
            // console.log(activeName);

            if (activeName === '/dashboard') {
                return
            }

            if (activeName === targetName) {
                tabs.forEach((tab, index) => {
                    if (tab.name === targetName) {
                        const nextTab = tabs[index + 1] || tabs[index - 1]
                        if (nextTab) {
                            activeName = nextTab.name
                        }
                    }
                })
            }

            editableTabsValue.value = activeName
            editableTabs.value = tabs.filter((tab) => tab.name !== targetName)

            store.state.editableTabsValue = editableTabsValue.value;
            store.state.editableTabs = editableTabs.value;

            router.push({ path: activeName })
        }
        const refreshTabs = () => {
            editableTabsValue.value = store.state.editableTabsValue;
            editableTabs.value = store.state.editableTabs;
        }

        const clickTab = (target: any) => {
            // console.log("target.props.label=" + target.props.label)
            // console.log(target.props)
            // router.push({name:target.props.label})
            // store.commit('SET_ACTIVEPATH',target.props.name)
            router.push(target.props.name)
        }
        watch(store.state, () => {
            refreshTabs();
        }, { deep: true, immediate: true })
        
        


        return {
            // addTab,
            editableTabsValue,
            editableTabs,
            removeTab,
            clickTab,


        }
    }

});
</script>
<style scoped>
.tabs {
    z-index: 999;
    height: 50px;
    /* width: 100%; */
    background-color: blue;
    display: flex;
    padding-left: 30px;
}

.demo-tabs>.el-tabs__content {
    padding: 32px;
    color: #6b778c;
    font-size: 32px;
    font-weight: 600;
}
</style>
    