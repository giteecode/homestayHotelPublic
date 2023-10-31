import router from "@/router/index"
import store from "@/store"

router.beforeEach((to,from,next)=>{
    const whiteList=['/login'] // 白名单
    let token=store.getters.GET_TOKEN;
    let hasRoutes=store.state.hasRoutes;
    let menuList=store.getters.GET_MENULIST;
    if(token){
        if(!hasRoutes){
            bindRoute(menuList);
            store.commit("SET_ROUTES_STATE",true);
        }
        next();
    }else{
        if(whiteList.includes(to.path)){
            next();
        }else{
            next("/login")
        }
    }
})

// 动态绑定路由
const bindRoute=(menuList)=>{
    let newRoutes=router.options.routes;
    menuList.forEach(menu=>{
        if(menu.children){
           menu.children.forEach(m=>{
               let route=menuToRoute(m,menu.name);
               if(route){
                   newRoutes[0].children.push(route);
               }
           })
        }
    })
    // 重新添加到路由
    newRoutes.forEach(route=>{
        router.addRoute(route)
    })
}

// 菜单对象转成路由对象
const menuToRoute=(menu,parentName)=>{
    if(!menu.component){
        return null;
    }else{
        let route={
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
