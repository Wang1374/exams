<template>
  <div style="padding:10px;" class="kd">
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入题目名称" style="width: 200px;margin-left: 5px" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load" icon="el-icon-search">查询</el-button>
    </div>
    <el-table :data="tableData" border stripe style="width: 99%" v-loading="loading">
      <el-table-column :width="cloumWidth.id" label="序号" type="index" show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="id" label="ID" v-if="false">
      </el-table-column>
      <el-table-column prop="topicType" label="类别" :width="cloumWidth.topicType">
        <template v-slot="scope">
          <!-- /0 单选题  1 多选题  2判断题 3填空题 4 简答题 5 代码题 -->
          <span v-if="scope.row.topicType===0">单选题</span>
          <span v-if="scope.row.topicType===1">多选题</span>
          <span v-if="scope.row.topicType===2">判断题</span>
          <span v-if="scope.row.topicType===3">填空题</span>
          <span v-if="scope.row.topicType===4">简答题</span>
          <span v-if="scope.row.topicType===5">代码题</span>
        </template>
      </el-table-column>
      <el-table-column prop="examName" label="试卷名称" :width="cloumWidth.examName">
      </el-table-column>
      <el-table-column prop="topicName" label="题目名称" :width="cloumWidth.topicName" :show-overflow-tooltip="true">
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
        topicType: "70",
        examName: "50",
        topicName: "50",
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
      let kd = [100, 150, 300];
      let data = this.cloumWidth;
      var i = 0;
      for (var key in data) {
        this.cloumWidth[key] = parseInt((kd[i] / 1295) * width);
        i++;
      }
    },
    load() {
      request
        .get("/reation/getReationAll", {
          params: {
            pageNumber: this.currentPage,
            pageSize: this.pageSize,
            search: this.search,
          },
        })
        .then((res) => {
          this.isToken(res);
          this.tableData = res.date.records; //初始化表格数据
          this.total = res.date.total; //初始化总条数
        });
    },
    update(id, yn) {
      //更新试卷状态
      request
        .post("/exam/updateReleaseYn/" + id + "/" + yn)
        .then((res) => {
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
    formatterType(row, column) {
      let type;
      let types = row.topicType;
      if (types === 0) {
        type = "单选题";
      } else if (types === 1) {
        type = "多选题";
      }
      return row.topicType === 1 ? "男" : "女";
    },
  },
};
</script>
<style scoped>
.kd {
  width: 100%;
}
</style>