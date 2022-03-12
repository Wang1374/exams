<template>
  <div style="padding:10px;" class="kd">
    <div style="margin: 10px 0">
      <!--功能区域-->
      <el-button @click="addUser">添加</el-button>
      <el-input v-model="search" placeholder="请输入关键词" style="width: 200px;margin-left: 5px" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load" icon="el-icon-search">查询</el-button>
    </div>
    <el-table :data="tableData" border stripe style="width: 99%;" v-loading="loading">
      <el-table-column :width="cloumWidth.id" label="序号" type="index" show-overflow-tooltip class="tableBox">
      </el-table-column>
      <el-table-column prop="id" label="ID" v-if="false">
      </el-table-column>
      <el-table-column prop="nickName" label="昵称" :width="cloumWidth.nickName">
      </el-table-column>
      <el-table-column prop="name" label="姓名" :width="cloumWidth.name">
      </el-table-column>
      <el-table-column prop="age" label="年龄" :width="cloumWidth.age">
      </el-table-column>
      <el-table-column prop="sex" label="性别" :width="cloumWidth.sex" :formatter="formatterSex">
      </el-table-column>
      <el-table-column prop="phone" label="手机号" :width="cloumWidth.phone">
      </el-table-column>
      <el-table-column prop="address" label="地址" :formatter="formatterAddress" :show-overflow-tooltip="true">
      </el-table-column>
      <el-table-column prop="role" label="角色" :width="cloumWidth.role" :formatter="formatterRole">
      </el-table-column>
      <el-table-column prop="useYn" label="状态" :width="cloumWidth.status">
        <template v-slot="scope">
          <el-tag @click="useYn(scope.row.id,scope.row.useYn)" v-if="scope.row.useYn===0" size="medium" type="success">正常</el-tag>
          <el-tag @click="useYn(scope.row.id,scope.row.useYn)" v-else size="medium" type="danger">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="registrationTime" label="注册时间" :width="cloumWidth.time">
      </el-table-column>
      <el-table-column label="操作" :width="cloumWidth.cz">
        <template #default="scope">
          <el-button size="small" type="text" @click="handleEdit(scope.row)" icon="el-icon-edit">编辑</el-button>
          <el-popconfirm title="你确定要删除吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="small" type="text" icon="el-icon-delete">删除</el-button>
            </template>
          </el-popconfirm>
          <el-button size="small" type="text" @click="viewScore(scope.row)" icon="el-icon-data-analysis">查看成绩</el-button>
          <el-popconfirm title="你确定要重置密码吗？" @confirm="handlePassword(scope.row.phone)">
            <template #reference>
              <el-button size="small" type="text" icon="el-icon-refresh-right">重置密码</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin: 10px 0">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="[5, 10, 15, 20]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
      <!--弹出框-->
      <el-dialog :title="title" v-model="dialogVisible" width="30%">
        <el-form ref="form" :model='form' label-width="70px" :rules="rules">
          <el-form-item label="昵称" prop="nickName">
            <el-input v-model="form.nickName" style="width: 90%"></el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" style="width: 90%"></el-input>
          </el-form-item>
          <el-form-item label="年龄" prop="age">
            <el-input v-model="form.age" style="width: 90%"></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio v-model="form.sex" :label="1">男</el-radio>
            <el-radio v-model="form.sex" :label="2">女</el-radio>
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="form.phone" style="width: 90%"></el-input>
          </el-form-item>
          <el-form-item label="地址" prop="address">
            <el-row :gutter="20">
              <el-col :span="12">
                <elui-china-area-dht v-model="addrValue"></elui-china-area-dht>
              </el-col>
              <el-col :span="12">
                <el-input v-model="form.address" style="width: 90%;margin-left: -10%;"></el-input>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item label="角色">
            <el-select v-model="form.role" placeholder="请选择" style="width: 90%">
              <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="form.useYn" placeholder="请选择" style="width: 90%">
              <el-option v-for="item in useYns" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="dialog()">确 定</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import request from "@/utils/request";
import { ElMessage } from "element-plus";
import { defineComponent } from "vue"; // 参数取值
import { EluiChinaAreaDht } from "elui-china-area-dht";
const chinaData = new EluiChinaAreaDht.ChinaArea().chinaAreaflat;
export default {
  name: "Home",
  components: {
    EluiChinaAreaDht,
  },
  data() {
    return {
      with: 0,
      //下拉三栏
      addrValue: [],
      //加载
      loading: false,
      form: {},
      dialogVisible: false,
      tableData: [],
      options: [
        {
          value: 2,
          label: "学生",
        },
        {
          value: 1,
          label: "老师",
        },
        {
          value: 0,
          label: "管理员",
        },
      ],
      value: "",
      useYns: [
        {
          value: 0,
          label: "正常",
        },
        {
          value: 1,
          label: "禁用",
        },
      ],
      useYnValue: "",
      currentPage: 1,
      pageSize: 10,
      total: 0,
      search: "",
      title: "提示",
      cloumWidth: {
        id: "50",
        name: "120",
        nickName: "120",
        age: "50",
        sex: "50",
        phone: "120",
        role: "100",
        status: "70",
        time: "180",
        cz: "200",
      },
      rules: {
        phone: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          { pattern: /^1[3-9]\d{9}$/, message: "手机号格式错误" },
        ],
        name: [
          { required: true, message: "请输入真实姓名", trigger: "blur" },
          { min: 2, max: 18, message: "请输入2-10位字符", trigger: "blur" },
        ],
        nickName: [
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
      },
    };
  },
  created() {
    this.with = document.body.clientWidth - 210;
    this.loading = true;
    this.load();
    //初始化表格宽度
    this.defaultTableWith();
  },
  mounted() {
    this.$nextTick(() => {
      window.addEventListener("resize", () => {
        //监听浏览器窗口大小改变
        //浏览器变化执行动作
        //初始化表格宽度
        this.defaultTableWith();
      });
    });
  },
  methods: {
    //初始化表格宽度
    defaultTableWith() {
      let width = document.body.clientWidth - 241; //表格宽度
      let kd = [50, 100, 100, 50, 50, 120, 100, 70, 180, 280];
      let data = this.cloumWidth;
      var i = 0;
      for (var key in data) {
        this.cloumWidth[key] = parseInt((kd[i] / 1295) * width);
        i++;
      }
    },
    load() {
      request
        .get("/user/getUserAll", {
          params: {
            pageNumber: this.currentPage,
            pageSize: this.pageSize,
            search: this.search,
          },
        })
        .then((ren) => {
          //判断token是否过期
          this.isToken(ren);
          this.tableData = ren.date.records; //初始化表格数据
          this.total = ren.date.total; //初始化总条数
          this.loading = false;
        });
    },
    formatterRole(row, column) {
      if (row.role === 0) {
        return "管理员";
      } else if (row.role === 1) {
        return "老师";
      } else {
        return "学生";
      }
    },
    formatterSex(row, column) {
      return row.sex === 1 ? "男" : "女";
    },
    formatterUseYn(row, column) {
      return row.useYn === 0 ? "正常" : "禁用";
    },
    formatterAddress(row, column) {
      const e = row.address.split("|")[0].split(",");
      const addr = row.address.split("|")[1];
      const province = chinaData[e[0]].label; //省
      const city = chinaData[e[1]].label; //城市
      const county = chinaData[e[2]].label; //县
      return province + city + county + addr;
    },
    dialog() {
      if (this.form.id) {
        //存在id则更新
        this.update();
      } else {
        this.saveUser();
      }
    },
    //添加用户
    addUser() {
      this.title = "添加用户";
      this.dialogVisible = true;
      if (this.$refs["form"] != undefined) {
        this.$refs["form"].resetFields();
      }
      this.form = {};
      this.addrValue = [];
    },
    //保存用戶
    saveUser() {
      //表单验证
      this.$refs["form"].validate((valid) => {
        if (valid) {
          //判断地址是否勾选
          if (this.addrValue.length == 0) {
            ElMessage.warning({ message: "地址未选择!", type: "warning" });
            return;
          }
          //判断性别是否勾选
          if (this.form.sex == undefined) {
            ElMessage.warning({ message: "性别未选择!", type: "warning" });
            return;
          }
          //判断角色是否选择
          if (this.form.role == undefined) {
            ElMessage.warning({ message: "角色未选择!", type: "warning" });
            return;
          }
          //判断状态是否选择
          if (this.form.useYn == undefined) {
            ElMessage.warning({ message: "状态未选择!", type: "warning" });
            return;
          }

          const addr = this.addrValue;
          this.form.address =
            addr[0] + "," + addr[1] + "," + addr[2] + "|" + this.form.address;
          request
            .post("/user/save", this.form)
            .then((res) => {
              //判断token是否过期
              this.isToken(res);
              if (res.code == 200) {
                this.dialogVisible = false;
                ElMessage.success({
                  message: "添加成功！",
                  type: "success",
                });
                this.load();
              } else {
                ElMessage.warning({
                  message: res.msg,
                  type: "warning",
                });
              }
            })
            .catch((res) => {
              ElMessage.error("系统异常,添加失败！");
            });
        }
      });
    },
    handleEdit(row) {
      this.title = "编辑用户";
      this.dialogVisible = true;
      this.form = JSON.parse(JSON.stringify(row));
      this.addrValue = this.form.address.split("|")[0].split(",");
      this.form.address = this.form.address.split("|")[1];
    },
    update() {
      //表单验证
      this.$refs["form"].validate((valid) => {
        if (valid) {
          //判断地址是否勾选
          if (this.addrValue.length == 0) {
            ElMessage.warning({ message: "地址未选择!", type: "warning" });
            return;
          }
          //判断性别是否勾选
          if (this.form.sex == undefined) {
            ElMessage.warning({ message: "性别未选择!", type: "warning" });
            return;
          }
          //判断角色是否选择
          if (this.form.role == undefined) {
            ElMessage.warning({ message: "角色未选择!", type: "warning" });
            return;
          }
          //判断状态是否选择
          if (this.form.useYn == undefined) {
            ElMessage.warning({ message: "状态未选择!", type: "warning" });
            return;
          }
          const addr = this.addrValue;
          this.form.address =
            addr[0] + "," + addr[1] + "," + addr[2] + "|" + this.form.address;
          //更新用户信息
          request
            .put("/user/updateUser", this.form)
            .then((res) => {
              //判断token是否过期
              this.isToken(res);
              if (res.code == 200) {
                this.dialogVisible = false;
                ElMessage.success({
                  message: "更新成功！",
                  type: "success",
                });
                this.load();
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
    handleSizeChange(pageSize) {
      //改变当前每页的个数触发
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNumber) {
      //改变当前页码触发
      this.currentPage = pageNumber;
      this.load();
    },
    handleDelete(id) {
      //删除用户
      request
        .delete("/user/deleteUser/" + id)
        .then((res) => {
          //判断token是否过期
          this.isToken(res);
          if (res.code == 200) {
            ElMessage.success({
              message: "删除成功！",
              type: "success",
            });
            this.load();
          } else {
            ElMessage.warning({
              message: res.msg,
              type: "warning",
            });
          }
        })
        .catch((res) => {
          ElMessage.error("系统异常,删除失败！");
        });
    },
    //重置用户密码
    handlePassword(value) {
      //加密
      const phone = this.$encryption(value);
      request
        .put("/user/resetPassword", phone)
        .then((res) => {
          //判断token是否过期
          this.isToken(res);
          if (res.code == 200) {
            ElMessage.success({
              message: "重置密码成功！",
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
          ElMessage.error("系统异常,重置密码失败！");
        });
    },
    useYn(id, yn) {
      let j = 0;
      if (yn == 0) {
        j = 1;
      }
      let data = this.tableData;
      for (let i = 0; i < data.length; i++) {
        if (data[i].id == id) {
          this.tableData[i].useYn = j;
          this.updateUseYn(id, j);
        }
      }
    },
    updateUseYn(id, yn) {
      //更新试卷状态
      request
        .post("/user/updateUserYn/" + id + "/" + yn)
        .then((res) => {
          //判断token是否过期
          this.isToken(res);
          if (res.code == 200) {
            ElMessage.success({
              message: "更新成功！",
              type: "success",
            });
            this.load();
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
    },
    viewScore(row) {
      //查看成绩
      this.$router.push("/viewStudentScoreList");
      const userId = this.$encryption(JSON.stringify(row.id));
      sessionStorage.setItem("userId", userId);
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
.kd {
  width: 100%;
  height: 100px;
}
</style>
<style lang="scss">
</style>