 
<template>
  <el-dialog title="修改密码" v-model="centerDialogVisible" width="40%">
    <el-form :model="fromPassword" :rules="rules" label-width="80px">
      <el-form-item label="旧密码" prop="oldPassWord">
        <el-input v-model="fromPassword.oldPassWord" style="width: 80%" show-password></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassWord">
        <el-input v-model="fromPassword.newPassWord" style="width: 80%" show-password></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPassword">
        <el-input v-model="fromPassword.checkPassword" style="width: 80%" show-password></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible = false,cancel()">取 消</el-button>
        <el-button type="primary" @click="centerDialogVisible = false,updatePass()">确 定</el-button>
      </span>
    </template>
  </el-dialog>
  <el-card class="box-card" style="margin:10px;">
    <template #header>
      <div style="text-align: center">
        个人信息 | {{userType}}
      </div>
    </template>
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="手机号">
        <el-input v-model="form.phone" disabled></el-input>
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="form.nickName" disabled></el-input>
      </el-form-item>
      <el-form-item label="昵称" prop="name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input v-model="form.age"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-radio v-model="form.sex" :label="1">男</el-radio>
        <el-radio v-model="form.sex" :label="2">女</el-radio>
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <elui-china-area-dht @change="onChange" v-model="value" style="width: 17%;"></elui-china-area-dht>
        <el-input style="width: 83%;" v-model="form.address"></el-input>
      </el-form-item>
    </el-form>
    <div style="text-align:center">
      <el-button type="primary" @click="updateUser()">保存信息</el-button>
      <el-button type="primary" @click="updatePassword()">修改密码</el-button>
    </div>
  </el-card>
</template>

<script>
import request from "@/utils/request";
import { ElMessage } from "element-plus";
import { inject } from "@vue/runtime-core";
import { defineComponent } from "vue"; // 参数取值
import { EluiChinaAreaDht } from "elui-china-area-dht";
const chinaData = new EluiChinaAreaDht.ChinaArea().chinaAreaflat;
export default {
  inject: ["reload"],
  name: "userInfo",
  components: { EluiChinaAreaDht },
  data() {
    return {
      value: [],
      form: {},
      fromPassword: {
        phone: "",
        oldPassWord: "",
        newPassWord: "",
        checkPassword: "",
      },
      userType: "",
      rules: {
        name: [
          { required: true, message: "请输入昵称", trigger: "blur" },
          { min: 2, max: 18, message: "请输入2-18位字符", trigger: "blur" },
        ],
        age: [
          { required: true, message: "请输入年龄", trigger: "blur" },
          {
            pattern: /^(?:[1-9][0-9]?|1[01][0-9]|120)$/,
            message: "年龄输入不合法",
          },
        ],
        address: [
          { required: true, message: "请输入家庭地址", trigger: "blur" },
          { min: 2, max: 18, message: "请输入5-50位字符", trigger: "blur" },
        ],
        oldPassWord: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            pattern:
              /^(?![a-zA-Z]+$)(?![0-9]+$)(?![._~!@#$^&*]+$)(?![0-9a-zA-Z]+$)(?![0-9._~!@#$^&*]+$)(?![a-zA-Z._~!@#$^&*]+$)[A-Za-z0-9._~!@#$^&*]{6,20}$/,
            message: "密码必须由字母、数字、特殊符号组成6-20位字符，区分大小写",
          },
        ],
        newPassWord: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            pattern:
              /^(?![a-zA-Z]+$)(?![0-9]+$)(?![._~!@#$^&*]+$)(?![0-9a-zA-Z]+$)(?![0-9._~!@#$^&*]+$)(?![a-zA-Z._~!@#$^&*]+$)[A-Za-z0-9._~!@#$^&*]{6,20}$/,
            message: "密码必须由字母、数字、特殊符号组成6-20位字符，区分大小写",
          },
        ],
        checkPassword: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            pattern:
              /^(?![a-zA-Z]+$)(?![0-9]+$)(?![._~!@#$^&*]+$)(?![0-9a-zA-Z]+$)(?![0-9._~!@#$^&*]+$)(?![a-zA-Z._~!@#$^&*]+$)[A-Za-z0-9._~!@#$^&*]{6,20}$/,
            message: "密码必须由字母、数字、特殊符号组成6-20位字符，区分大小写",
          },
        ],
      },
      centerDialogVisible: false,
    };
  },
  created() {
    let phone = sessionStorage.getItem("phone");
    this.load(phone);
  },
  methods: {
    load(phone) {
      request.post("/user/getUserByPhone",phone).then((res) => {
        this.form = res.date;
        let addr = res.date.address;
        this.form.address = addr.split("|")[1];
        let city = addr.split("|")[0];
        this.value = city.split(",");
        if (this.form.role == "0") {
          this.userType = "管理员";
        } else if (this.form.role == "1") {
          this.userType = "老师";
        } else {
          this.userType = "学生";
        }
      });
    },
    updateUser() {
      if (this.value.length < 1) {
        ElMessage.warning({ message: "地址未选择!", type: "warning" });
        return;
      }
      //校验表单是否正确
      this.$refs["form"].validate((valid) => {
        if (valid) {
          //补全地址信息
          let addr = this.value;
          this.form.address =
            addr[0] + "," + addr[1] + "," + addr[2] + "|" + this.form.address;
          //更新用户信息
          request
            .put("/user/updateUser", this.form)
            .then((res) => {
              //判断token是否过期
              this.isToken(res);
              if (res.code == 200) {
                ElMessage.success({
                  message: "更新成功！",
                  type: "success",
                });
                //刷新数据
                sessionStorage.removeItem("user"); //初始化清除session
                sessionStorage.setItem("user", JSON.stringify(res.date));
                //刷新页面
                this.reload();
              } else {
                ElMessage.warning({
                  message: res.msg,
                  type: "warning",
                });
              }
            })
            .catch((res) => {
              ElMessage.error("系统异常,更新失败！");
            });
        }
      });
    },
    updatePassword() {
      this.centerDialogVisible = true;
      this.cancel();
    },
    cancel() {
      this.fromPassword = {};
    },
    updatePass() {
      //判断密码是否为空
      let oldPassWord = this.fromPassword.oldPassWord;
      let newPassWord = this.fromPassword.newPassWord;
      let checkPassword = this.fromPassword.checkPassword;
      this.fromPassword.phone = this.form.phone;
      if (
        oldPassWord === undefined ||
        newPassWord === undefined ||
        checkPassword === undefined ||
        oldPassWord === "" ||
        newPassWord === "" ||
        checkPassword === ""
      ) {
        return false;
      }
      //判断两个密码是否一致
      if (this.fromPassword.newPassWord != this.fromPassword.checkPassword) {
        return false;
      }
      //判断密码是否重复

      request
        .put("/user/updatePass", this.fromPassword)
        .then((res) => {
          //判断token是否过期
          this.isToken(res);
          if (res.code == 200) {
            ElMessage.success({
              message: "密码修改成功,请重新登录！",
              type: "success",
            });
            this.logOut();
          } else {
            ElMessage.warning({
              message: res.msg,
              type: "warning",
            });
          }
        })
        .catch((res) => {
          ElMessage.error("系统异常,修改失败！");
        });
    },
    logOut() {
      //退出系统
      //清空token
      localStorage.removeItem("token");
      //清除session
      sessionStorage.removeItem("phone");
      //跳转登录页面
      this.$router.push("/");
    },
    isToken(res) {
      if (res === "") {
        ElMessage.error("token已过期,请重新登录!");
        this.logOut();
      }
    },
  },
};
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 100%;
}
</style>
