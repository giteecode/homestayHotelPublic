import { createStore } from 'vuex'
// import {storage , sessionStorage} from '@/utils/storage';
import router from '@/router'
export default createStore({
  state: {
    //读取state值
    // console.log(store.state.counter)
    counter: 9,
    hasRoutes: false,
    activePath: '/dashboard',
    editableTabsValue: '/dashboard',
    editableTabs: [
      {
        title: '控制台',
        name: '/dashboard'
      }
    ]
  },
  getters: {
    // 触发getters中的某个方法
    // console.log(store.getters.GET_INFO)
    GET_INFO: (state) => {
      return state.counter * state.counter
    },
    GET_TOKEN: state => {
      return JSON.parse(sessionStorage.getItem("token") || '0');
    },
    GET_MENULIST: state => {
      return JSON.parse(sessionStorage.getItem("menuList") || '0');
    },
    GET_USERINFO: state => {
      return JSON.parse(sessionStorage.getItem("userInfo") || '0');
    },
    GET_FRONTUSER: state => {
      return JSON.parse(sessionStorage.getItem("frontUser") || '0');
    }
  },
  mutations: {
    // 触发mutations中的某个方法
    // store.commit('GET_ADMININFO')
    GET_ADMININFO: () => {
      console.log('我是store');

    },
    SET_TOKEN: (state, token) => {
      sessionStorage.setItem("token", JSON.stringify(token));
    },
    SET_ACTIVEPATH: (state, path) => {
      state.activePath = path
    },
    SET_MENULIST: (state, menuList) => {
      sessionStorage.setItem("menuList", JSON.stringify(menuList));
    },
    SET_USERINFO: (state, userInfo) => {
      sessionStorage.setItem("userInfo", JSON.stringify(userInfo));
    },
    SET_FRONTUSER: (state, userInfo) => {
      sessionStorage.setItem("frontUser", JSON.stringify(userInfo));
    },
    SET_ROUTES_STATE: (state, hasRoutes) => {
      state.hasRoutes = hasRoutes
    },
    ADD_TABS: (state, tab) => {
      if (state.editableTabs.findIndex(e => e.name === tab.menuUrl) === -1) {
        // console.log(tab);

        state.editableTabs.push({
          title: tab.menuName,
          name: tab.menuUrl
        })
      }
      state.editableTabsValue = tab.menuUrl
    },
    RESET_TABS: (state) => {
      state.editableTabsValue = '/dashboard';
      state.editableTabs = [
        {
          title: '控制台',
          name: '/dashboard'
        }
      ]
    },
  },
  actions: {
    // 触发action中的方法
    // store.dispatch('logout')
    // 安全退出
    logout() {
      window.sessionStorage.clear();
      router.go(0)
      // router.push("/login")
      // router.push({
      //   path: '/login',
      //   query: {
      //     date: new Date().getTime()
      //   }
      // })
    }
  },
  modules: {
  }
})
