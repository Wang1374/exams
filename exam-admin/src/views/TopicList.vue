<template>
  <div style="padding:10px;" class="kd">
    <div style="margin: 10px 0">
      <!--功能区域-->
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
      <el-table-column prop="question" label="题目" :show-overflow-tooltip="true">
      </el-table-column>
      <el-table-column prop="optionss" label="选项" :show-overflow-tooltip="true" :width="cloumWidth.optionss">
      </el-table-column>
      <el-table-column prop="photoPath" label="图片" :show-overflow-tooltip="true" :width="cloumWidth.photoPath">
      </el-table-column>
      <el-table-column prop="answer" label="答案" :show-overflow-tooltip="true" :width="cloumWidth.answer">
      </el-table-column>
      <el-table-column prop="analysis" label="解析" :show-overflow-tooltip="true" :width="cloumWidth.analysis">
      </el-table-column>
      <el-table-column prop="score" label="分数" :width="cloumWidth.score">
      </el-table-column>
      <el-table-column prop="difficulty" label="难度" :width="cloumWidth.difficulty">
        <template v-slot="scope">
          <el-tag v-if="scope.row.difficulty==='简单'" size="medium" type="success">简单</el-tag>
          <el-tag v-if="scope.row.difficulty==='中等'" size="medium" type="">中等</el-tag>
          <el-tag v-if="scope.row.difficulty==='困难'" size="medium" type="danger">困难</el-tag>
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
        optionss: "50",
        photoPath: "50",
        answer: "50",
        analysis: "50",
        score: "50",
        difficulty: "70",
        createUserName: "80",
        createTime: "160",
        updateUserName: "80",
        updateTime: "160",
        cz: "177",
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
      let kd = [50, 70, 60, 60, 60, 70, 50, 70, 80, 170, 80, 170, 120];
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
        .get("/topic/getTopicAll", {
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
          this.loading = false;
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
    handleEdit(id) {
      //修改题目
      this.$router.push("/editTopic");
      sessionStorage.setItem("topicId", this.$encryption(JSON.stringify(id)));
    },
    handleDelete(id) {
      //删除题目
      request
        .delete("/topic/deleteTopic/" + id)
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