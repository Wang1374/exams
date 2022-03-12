<template>
  <div style="height:50px;line-height:50px; border-bottom:0px solid #ccc;display: flex;background:#545c64">
    <el-button type="info" :icon="icon" @click="Collapse()" style="color: #fff;background-color: #545c64;border-color: #545c64;font-size: 30px;"></el-button>
    <div style="width:200px;font-weight: bold;color:#e1e3e4">檀木考试管理系统</div>

    <div style="flex:1">
    </div>
    <div style="width:100px">
      <el-dropdown>
        <span class="el-dropdown-link" style="color:#e1e3e4 ">
          {{name}}<i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/userInfo')">个人信息</el-dropdown-item>
            <el-dropdown-item @click="this.logOut()">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>

</template>

<script>
import request from "@/utils/request";
import { ElMessage } from "element-plus";
export default {
  name: "Header",
  data() {
    return {
      name: "",
      icon: "el-icon-s-fold",
    };
  },
  created() {
    let phone = sessionStorage.getItem("phone");
    request.post("/user/getUserByPhone", phone).then((res) => {
      if (res === "") {
        ElMessage.error("token已过期,请重新登录!");
        this.logOut();
      }
      this.name = res.date.name;
    });
  },
  methods: {
    logOut() {
      //退出系统
      //清空token
      localStorage.removeItem("token");
      //清除session
      sessionStorage.removeItem("phone");
      //跳转登录页面
      this.$router.push("/");
    },
    Collapse() {
      if (this.$store.state.isCollapse) {
        this.icon = "el-icon-s-unfold";
      } else {
        this.icon = "el-icon-s-fold";
      }
      this.$store.commit("setCollapse");
    },
  },
};
</script>
<style>
.item {
  margin-top: 10px;
  margin-right: 40px;
}
</style>