import router from "@/router/index"
import store from "@/store"

/**路由守卫，每次请求都要先在这里进行判断 */
router.beforeEach((to,from,next)=>{
    const whiteList=['/','/index','/detail/*','/login','/front/forget'] // 白名单
    // 正则匹配/*
    const path1 = /detail*/
    const zhengze = () => {
        if(path1.test(to.path)){return true}
    }
    
    const token=store.getters.GET_TOKEN;
    const hasRoutes=store.state.hasRoutes;
    // const menuList=store.getters.GET_MENULIST;
    if(token){
        if(!hasRoutes){
            // bindRoute(menuList);
            store.commit("SET_ROUTES_STATE",true);
        }
        next();
    }else{
        // to.meta.keepAlive = false
        if(whiteList.includes(to.path) || zhengze()){
            // to.meta.keepAlive = false
            
            next();
        }else{
            // to.meta.keepAlive = false
            next("/login")
            // next()
            // console.log('我是router规则，转发到/login');
            // router.push('/login')
            // router.push({
            //     path:'/login',
            //     query: {
            //         date:new Date().getTime()
            //       }
            // })
            // router.go(0)
        }
    }
})

// 动态绑定路由
const bindRoute=(menuList:any)=>{
    const newRoutes:any=router.options.routes;
    menuList.forEach((menu:any)=>{
        if(menu.children){
           menu.children.forEach((m:any)=>{
               const route=menuToRoute(m,menu.name);
               if(route){
                   newRoutes[0].children.push(route);
               }
           })
        }
    })
    // 重新添加到路由
    newRoutes.forEach((route:any)=>{
        router.addRoute(route)
    })
}

// 菜单对象转成路由对象
const menuToRoute=(menu:any,parentName:any)=>{
    if(!menu.component){
        return null;
    }else{
        const route:any={
            name:menu.name,
            path:menu.path,
            meta:{
                parentName:parentName
            }
        }
        route.component=()=>import('@/views/'+menu.component+'.vue');
        
        return route;
    }
}
