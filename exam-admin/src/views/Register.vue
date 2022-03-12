<template>
  <Verify mode="pop" @success="success" captchaType='blockPuzzle' :imgSize="{width:'400px',height:'200px'}" ref="verify"></Verify>
  <!--  captchaType='blockPuzzle' 滑动验证 clickWord 文字验证  -->
  <div style="width:100%;height:100vh;overflow: hidden" class="login">
    <!-- overflow:hidden的作用（溢出隐藏、清除浮动、解决外边距塌陷等等） -->
    <div style="width:400px;margin:0px auto">
      <div style="color: #cccccc;font-size: 30px;text-align: center;padding:30px 0">注册</div>
      <el-form ref="ruleForm" :model="ruleForm" :rules="rules">
        <el-form-item prop="name">
          <el-input prefix-icon="iconfont icon-nicheng" v-model="ruleForm.name" placeholder="请输入昵称"></el-input>
        </el-form-item>
        <el-form-item prop="nickName">
          <el-input prefix-icon="iconfont icon-yonghu" v-model="ruleForm.nickName" placeholder="请输入真实姓名"></el-input>
        </el-form-item>
        <el-form-item prop="age">
          <el-input prefix-icon="iconfont icon-nianling" v-model="ruleForm.age" placeholder="请输入年龄"></el-input>
        </el-form-item>

        <el-form-item prop="address">
          <el-row :gutter="20">
            <el-col :span="13" style="margin-right: -19px">
              <elui-china-area-dht @change="onChange" v-model="value"></elui-china-area-dht>
            </el-col>
            <el-col :span="11">
              <el-input style="width: 193px;" prefix-icon="iconfont icon-dizhi" v-model="ruleForm.address" placeholder="请输入家庭地址"></el-input>
            </el-col>
          </el-row>
        </el-form-item>

        <el-form-item prop="sex">
          <el-select prefix-icon="iconfont icon-nianling" v-model="ruleForm.sex" placeholder="请选择性别" style="width:100%">
            <el-option label="男" value="1"></el-option>
            <el-option label="女" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="phone">
          <el-input prefix-icon="iconfont icon-shoujihao" v-model="ruleForm.phone" placeholder="请输入手机号">
            <!-- <template #prepend>+86</template> -->
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="iconfont icon-mima" v-model="ruleForm.password" show-password placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item prop="yzm">
          <el-input prefix-icon="iconfont icon-duanxin" v-model="ruleForm.yzm" placeholder="6位短信验证码">
            <template #append>
              <el-button @click="onShow()" :disabled="disabled">{{yzmBtn}}</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width:49%" type="primary" @click="register()">注册</el-button>
          <el-button style="width:48%" @click="resetForm('ruleForm')">重置</el-button>
          <el-button @click="login()" type="text">已有账号?去登录</el-button>
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
  name: "register",
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
          { min: 2, max: 50, message: "请输入5-50位字符", trigger: "blur" },
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
      msg: "",
      flag: false,
      yzmBtn: "获取短信验证码",
      disabled: false,
      addr: "",
    };
  },
  methods: {
    register() {
      //存入地址
      this.addr = this.ruleForm.address;
      //注册用户
      //表单验证
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //补全地址
          this.ruleForm.address = this.city + "|" + this.ruleForm.address;
          let registerUser = {
            name: this.$encryption(this.ruleForm.name),
            nickName: this.$encryption(this.ruleForm.nickName),
            age: this.$encryption(this.ruleForm.age),
            address: this.$encryption(this.ruleForm.address),
            sex: this.$encryption(this.ruleForm.sex),
            phone: this.$encryption(this.ruleForm.phone),
            password: this.$encryption(this.ruleForm.password),
            code: this.$encryption(this.ruleForm.yzm),
          };
          request
            .post("/user/register", registerUser)
            .then((res) => {
              if (res.code == 200) {
                ElMessage.success({
                  message: "注册成功,请登录！",
                  type: "success",
                });
                //调整到登录界面
                this.login();
              } else {
                //注册失败
                ElMessage.warning({
                  message: res.msg,
                  type: "warning",
                });
                //恢复地址
                this.ruleForm.address = this.addr;
              }
            })
            .catch((res) => {
              ElMessage.error("系统异常,注册失败！");
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
      if (this.city === "") {
        ElMessage.warning({ message: "地址未选择!", type: "warning" });
        return;
      }
      //校验表单是否正确
      this.$refs["ruleForm"].validateField(
        ["phone", "password", "name", "age", "nickName", "address"],
        (err) => {
          if (err) {
            return;
          } else {
            this.$refs.verify.show();
          }
        }
      );
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
      //判断地址是否选择
      if (this.city === "") {
        ElMessage.warning({ message: "地址未选择!", type: "warning" });
        return;
      }
      //方式一
      let param = new URLSearchParams();
      param.append("captchaVerification", params.captchaVerification);
      param.append("phone", this.$encryption(this.ruleForm.phone));
      axios
        .post("api/captcha/sendMsg", param)
        .then((res) => {
          if (res.data.code == 200) {
            this.tackBtn();
            ElMessage.success({
              message: "短信发送成功,请注意查收！",
              type: "success",
            });
          } else {
            ElMessage.warning({
              message: res.msg,
              type: "warning",
            });
          }
        })
        .catch((res) => {
          ElMessage.error("系统异常,发送失败！");
        });
    },
    onChange(e) {
      const province = chinaData[e[0]].value; //省
      const city = chinaData[e[1]].value; //城市
      const county = chinaData[e[2]].value; //县
      this.city = province + "," + city + "," + county;
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
