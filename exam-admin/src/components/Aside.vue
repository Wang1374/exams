<template>
  <el-menu router style="min-height: calc(100vh - 50px)" :default-active="path" class="el-menu-vertical-demo" background-color="#545c64" text-color="#fff" active-text-color="#ffd04b" :collapse="getCollapse">
    <!-- default-active="2-1"  代表激活 index 2-1的菜单  显示高亮 -->
    <!-- :default-openeds="[2]" 默认展开 用户管理 -->
    <el-submenu :index="index" v-for="(item,index) in lists" :key="index">
      <template #title>
        <i :class="item.icon"></i>
        <span>{{item.menuTitle}}</span>
      </template>
      <template v-for="(v,i) in item.menu" :key="i">
        <el-menu-item v-if="v.listShow" :index="v.url">{{v.listTitle}}</el-menu-item>
      </template>
    </el-submenu>
  </el-menu>
</template>

<script>
import request from "@/utils/request";
import { ElMessage } from "element-plus";
export default {
  name: "Aside",
  data() {
    return {
      path: this.$route.path, //设置默认高亮菜单
      user: {},
      //动态菜单
      lists: [],
      value: "",
      collapse: false,
    };
  },
  created() {
    this.getMenu();
  },
  computed: {
    getCollapse() {
      return this.$store.state.isCollapse;
    },
  },
  methods: {
    getMenu() {
      request.get("/menu/getMenu").then((res) => {
        this.lists = res.date;
      });
    },
    isToken(res) {
      if (res === "") {
        ElMessage.error("token已过期,请重新登录!");
        //清空token
        localStorage.removeItem("token");
        //清除session
        sessionStorage.removeItem("phone");
        //跳转登录页面
        this.$router.push("/");
      }
    },
  },
};
</script>

<style scoped>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 230px;
  min-height: 400px;
}
</style>