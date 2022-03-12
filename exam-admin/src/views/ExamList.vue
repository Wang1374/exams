<template>
  <div style="padding:10px;" class="kd">
    <div style="margin: 10px 0">
      <!--功能区域-->
      <el-input v-model="search" placeholder="请输入试卷名称" style="width: 200px;margin-left: 5px" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load" icon="el-icon-search">查询</el-button>
    </div>
    <el-table :data="tableData" border stripe style="width: 99%" v-loading="loading">
      <el-table-column :width="cloumWidth.id" label="序号" type="index" show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="id" label="ID" v-if="false">
      </el-table-column>
      <el-table-column prop="examName" label="试卷名称" :show-overflow-tooltip="true">
      </el-table-column>
      <el-table-column prop="answerTime" label="时间" :width="cloumWidth.AnswerTime">
      </el-table-column>
      <el-table-column prop="subjectName" label="类型" :width="cloumWidth.subjectName">
      </el-table-column>
      <el-table-column prop="examScore" label="总分" :width="cloumWidth.examScore">
      </el-table-column>
      <el-table-column prop="passingScore" label="及格分数" :width="cloumWidth.passingScore">
      </el-table-column>
      <el-table-column prop="topicNum" label="数量" :width="cloumWidth.topicNum">
      </el-table-column>
      <el-table-column prop="releaseYn" label="状态" :width="cloumWidth.releaseYn">
        <template v-slot="scope">
          <el-tag @click="release(scope.row.id,scope.row.releaseYn)" v-if="scope.row.releaseYn===0" size="medium" type="success">已发布</el-tag>
          <el-tag @click="release(scope.row.id,scope.row.releaseYn)" v-else size="medium" type="info">未发布</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createUserName" label="创建人" :width="cloumWidth.createUserName">
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" :width="cloumWidth.createTime">
      </el-table-column>
      <el-table-column prop="updateUserName" label="修改人" :width="cloumWidth.updateUserName">
      </el-table-column>
      <el-table-column prop="updateTime" label="修改时间" :width="cloumWidth.updateTime">
      </el-table-column>
      <el-table-column label="操作" :width="cloumWidth.cz">
        <template #default="scope">
          <el-button size="small" type="text" @click="handleEdit(scope.row.id)" icon="el-icon-edit">编辑</el-button>
          <el-popconfirm title="你确定要删除吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="small" type="text" icon="el-icon-delete">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin: 10px 0">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="[5, 10, 15, 20]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import request from "@/utils/request";
import { ElMessage } from "element-plus";

export default {
  name: "Home",
  components: {},
  data() {
    return {
      loading: false,
      form: {},
      dialogVisible: false,
      tableData: [],
      useYnValue: "",
      currentPage: 1,
      pageSize: 10,
      total: 0,
      search: "",
      cloumWidth: {
        id: "50",
        AnswerTime: "50",
        subjectName: "70",
        examScore: "50",
        passingScore: "50",
        topicNum: "50",
        releaseYn: "82",
        createUserName: "100",
        createTime: "170",
        updateUserName: "100",
        updateTime: "170",
        cz: "180",
      },
    };
  },
  created() {
    this.load();
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
      let kd = [50, 50, 100, 50, 80, 50, 82, 100, 170, 100, 170, 120];
      let data = this.cloumWidth;
      var i = 0;
      for (var key in data) {
        this.cloumWidth[key] = parseInt((kd[i] / 1295) * width);
        i++;
      }
    },
    load() {
      this.loading = true;
      request
        .get("/exam/getExamAll", {
          params: {
            pageNumber: this.currentPage,
            pageSize: this.pageSize,
            search: this.search,
          },
        })
        .then((res) => {
          //判断token是否过期
          this.isToken(res);
          this.tableData = res.date.records; //初始化表格数据
          this.total = res.date.total; //初始化总条数
          this.loading = false;
        });
    },
    update(id, yn) {
      //更新试卷状态
      request
        .post("/exam/updateReleaseYn/" + id + "/" + yn)
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
        .delete("/exam/deleteExam/" + id)
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
    release(id, releaseYn) {
      let j = 0;
      if (releaseYn == 0) {
        j = 1;
      }
      let data = this.tableData;
      for (let i = 0; i < data.length; i++) {
        if (data[i].id == id) {
          this.tableData[i].releaseYn = j;
          this.update(id, j);
        }
      }
    },
    handleEdit(id) {
      this.$router.push("/editExam");
      sessionStorage.setItem("examId", this.$encryption(JSON.stringify(id)));
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
}
</style>