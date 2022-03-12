<template>
  <div style="width:100%;height:100vh;overflow: hidden;" class="login">
    <!-- overflow:hidden的作用（溢出隐藏、清除浮动、解决外边距塌陷等等） -->
    <div style="width:400px;margin:130px auto">
      <div style="color: #cccccc;font-size: 30px;text-align: center;padding:30px 0">登录</div>
      <el-form ref="ruleForm" :model="ruleForm" :rules="rules">
        <el-form-item prop="phone">
          <el-input prefix-icon="el-icon-mobile-phone" v-model="ruleForm.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" v-model="ruleForm.password" show-password placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width:49%" type="primary" @click="login('ruleForm')">登录</el-button>
          <el-button style="width:48%" @click="resetForm('ruleForm')">重置</el-button>
          <el-button @click="register()" type="text">没有账号?去注册</el-button>
          <el-button @click="forgetPassword()" type="text" style="margin-left: 200px">忘记密码?</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>

</template>

<script>
import request from "@/utils/request";
import { ElMessage } from "element-plus";
import Layout from "@/layout/Layout.vue";
export default {
  name: "login",
  data() {
    return {
      routerValue: [],
      ruleForm: {
        phone: "",
        password: "",
      },
      rules: {
        phone: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          { pattern: /^1[3-9]\d{9}$/, message: "手机号格式错误" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            pattern:
              /^(?![a-zA-Z]+$)(?![0-9]+$)(?![._~!@#$^&*]+$)(?![0-9a-zA-Z]+$)(?![0-9._~!@#$^&*]+$)(?![a-zA-Z._~!@#$^&*]+$)[A-Za-z0-9._~!@#$^&*]{6,20}$/,
            message: "密码必须由字母、数字、特殊符号组成6-20位字符，区分大小写",
          },
        ],
      },
    };
  },
  created() {
    sessionStorage.removeItem("user"); //初始化清除session
  },
  methods: {
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    login(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //加密
          let userInfo = {
            phone: this.$encryption(this.ruleForm.phone),
            password: this.$encryption(this.ruleForm.password),
          };
          //登录方法
          request
            .post("/login/Login", userInfo)
            .then((res) => {
              if (res.code == 200) {
                ElMessage.success({
                  message: "登录成功！",
                  type: "success",
                });
                //登录成功 将信息存入session
                sessionStorage.setItem(
                  "phone",
                  this.$encryption(this.ruleForm.phone)
                );
                //将请求头存入location
                localStorage.setItem("token", res.date);
                //配置动态路由
                this.setRouter();
              } else if (res.code == 1002) {
                ElMessage.error(res.msg);
              } else {
                ElMessage.warning({
                  message: res.msg,
                  type: "warning",
                });
              }
            })
            .catch((res) => {
              ElMessage.error("系统异常,登录失败！");
            });
        }
      });
    },
    register() {
      this.$router.push("/register");
    },
    //跳转到忘记密码页面
    forgetPassword() {
      this.$router.push("/forgetPassword");
    },
    async setRouter() {
      request.get("/menu/getRouter").then((res) => {
        const children = [];
        res.date.forEach((item) => {
          children.push({
            path: item.url,
            name: item.name,
            component: () => import("@/views/" + item.name + ".vue"),
          });
        });
        let value = {
          path: "/",
          name: "Layout",
          component: Layout,
          redirect: "/login",
          children: children,
        };
        this.$router.addRoute(value);
        //动态路由存在本地
        localStorage.setItem("newRouterArr", JSON.stringify(res.date));
        this.$router.push("/home");
      });
    },
  },
};
</script>

<style scoped>
/* 背景图片*/
.login {
  background: url("../assets/image/xinghe.jpg");
  width: 100%;
  height: 100%;
  position: fixed;
  /*margin-top: -65px;上边距*/
  /*margin-left: -10px;左边距*/
  background-size: 100% 100%;
}
</style>