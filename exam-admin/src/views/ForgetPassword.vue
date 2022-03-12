<template>
  <Verify mode="pop" @success="success" captchaType='blockPuzzle' :imgSize="{width:'400px',height:'200px'}" ref="verify"></Verify>
  <!--  captchaType='blockPuzzle' 滑动验证 clickWord 文字验证  -->
  <div style="width:100%;height:100vh;overflow: hidden" class="login">
    <!-- overflow:hidden的作用（溢出隐藏、清除浮动、解决外边距塌陷等等） -->
    <div style="width:400px;margin:0px auto">
      <div style="color: #cccccc;font-size: 30px;text-align: center;padding:30px 0">重置密码</div>
      <el-form ref="ruleForm" :model="ruleForm" :rules="rules">
        <el-form-item prop="nickName">
          <el-input prefix-icon="iconfont icon-yonghu" v-model="ruleForm.nickName" placeholder="请输入真实姓名"></el-input>
        </el-form-item>
        <el-form-item prop="phone">
          <el-input prefix-icon="iconfont icon-shoujihao" v-model="ruleForm.phone" placeholder="请输入手机号">
            <!-- <template #prepend>+86</template> -->
          </el-input>
        </el-form-item>
        <el-form-item prop="yzm">
          <el-input prefix-icon="iconfont icon-duanxin" v-model="ruleForm.yzm" placeholder="6位短信验证码">
            <template #append>
              <el-button @click="onShow()" :disabled="disabled">{{yzmBtn}}</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width:49%" type="primary" @click="submit()">提交</el-button>
          <el-button style="width:48%" @click="resetForm('ruleForm')">重置</el-button>
          <el-button @click="login()" type="text">返回登录页面</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>

</template>

<script>
import Verify from "./../components/verifition/Verify";
import { ElMessage } from "element-plus";
import axios from "axios";
import request from "@/utils/request";
import { defineComponent } from "vue"; // 参数取值
import { EluiChinaAreaDht } from "elui-china-area-dht";
const chinaData = new EluiChinaAreaDht.ChinaArea().chinaAreaflat;
export default {
  name: "ForgetPassword",
  components: { Verify, EluiChinaAreaDht },
  data() {
    return {
      ruleForm: {
        phone: "",
        password: "",
        yzm: "",
        name: "",
        nickName: "",
        age: "",
        sex: "",
        address: "",
      },
      city: "",
      rules: {
        phone: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          { pattern: /^1[3-9]\d{9}$/, message: "手机号格式错误" },
        ],
        yzm: [
          { required: true, message: "请输入验证码", trigger: "blur" },
          { min: 6, max: 6, message: "请输入6位字符验证码", trigger: "blur" },
        ],
        nickName: [
          { required: true, message: "请输入真实姓名", trigger: "blur" },
          { min: 2, max: 18, message: "请输入2-10位字符", trigger: "blur" },
        ],
      },
      msg: "",
      flag: false,
      yzmBtn: "获取短信验证码",
      disabled: false,
    };
  },
  methods: {
    submit() {
      //重置密码
      //表单验证
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //加密
          let param = new URLSearchParams();
          param.append("phone", this.$encryption(this.ruleForm.phone));
          param.append("code", this.$encryption(this.ruleForm.yzm));
          axios
            .post("api/user/userResetPassword", param)
            .then((res) => {
              console.log(res);
              if (res.data.code == 200) {
                ElMessage.success({
                  message: "重置密码成功,跳转至登录页面！",
                  type: "success",
                });
                this.$router.push("/login");
              } else {
                ElMessage.warning({
                  message: res.data.msg,
                  type: "warning",
                });
              }
            })
            .catch((res) => {
              ElMessage.error("系统异常,发送失败！");
            });
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    login() {
      this.$router.push("/login");
    },
    onShow() {
      //校验表单是否正确
      this.$refs["ruleForm"].validateField(["phone", "nickName"], (err) => {
        if (err) {
          return;
        } else {
          this.$refs.verify.show();
        }
      });
    },
    tackBtn() {
      //验证码倒数60秒
      let time = 60;
      let timer = setInterval(() => {
        if (time == 0) {
          clearInterval(timer);
          this.yzmBtn = "重新获取验证码";
          this.disabled = false;
        } else {
          this.disabled = true;
          this.yzmBtn = time + "秒后重试";
          time--;
        }
      }, 1000);
    },
    success(params) {
      console.log("ok", params);
      //方式一
      let param = new URLSearchParams();
      param.append("captchaVerification", params.captchaVerification);
      param.append("phone", this.$encryption(this.ruleForm.phone));
      param.append("nickName", this.$encryption(this.ruleForm.nickName));
      axios
        .post("api/captcha/forgetPasswordSendMsg", param)
        .then((res) => {
          console.log(res);
          if (res.data.code == 200) {
            this.tackBtn();
            ElMessage.success({
              message: "短信发送成功,请注意查收！",
              type: "success",
            });
          } else {
            ElMessage.warning({
              message: res.data.msg,
              type: "warning",
            });
          }
        })
        .catch((res) => {
          ElMessage.error("系统异常,发送失败！");
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
