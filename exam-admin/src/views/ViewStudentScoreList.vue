<template>
  <div style="padding:10px;" class="kd">
    <div style="margin: 10px 0">
      <!--功能区域-->
      <el-row>
        <el-col :span="4" style="max-width: 211px">
          <el-input v-model="search" placeholder="请输入试卷名称" style="width: 200px;margin-left: 5px" clearable></el-input>
        </el-col>
        <el-col :span="3">
          <el-button type="primary" style="margin-left: 5px" @click="load" icon="el-icon-search">查询</el-button>
        </el-col>
        <el-col :span="2" style="margin-top: 8px;max-width: 80px;">试卷类型:</el-col>
        <el-col :span="5">
          <el-select v-model="value" placeholder="请选择" @change="examType()">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="2" style="margin-top: 8px;max-width: 80px;">试卷状态:</el-col>
        <el-col :span="4">
          <el-select v-model="examStatusValue" placeholder="请选择" @change="examStatus()">
            <el-option v-for="item in examStatusOptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
    </div>
    <el-table :data="tableData" border stripe style="width: 99%" v-loading="loading">
      <el-table-column :width="cloumWidth.id" label="序号" type="index" show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="id" label="ID" v-if="false">
      </el-table-column>
      <el-table-column prop="examName" label="试卷名称" :show-overflow-tooltip="true" :width="cloumWidth.examName">
      </el-table-column>
      <el-table-column prop="answerTime" label="考试时间" :width="cloumWidth.AnswerTime">
      </el-table-column>
      <el-table-column prop="subjectName" label="试卷类型" :width="cloumWidth.subjectName">
      </el-table-column>
      <el-table-column prop="examScore" label="总分" :width="cloumWidth.examScore">
      </el-table-column>
      <el-table-column prop="passingScore" label="及格分" :width="cloumWidth.passingScore">
      </el-table-column>
      <el-table-column prop="grade" label="得分" :width="cloumWidth.grade">
      </el-table-column>
      <el-table-column prop="examHsTime" label="答题耗时" :width="cloumWidth.examHsTime">
      </el-table-column>
      <el-table-column prop="topicNum" label="题目数量" :width="cloumWidth.topicNum">
      </el-table-column>
      <el-table-column prop="answerDate" label="提交时间" :width="cloumWidth.answerDate">
      </el-table-column>
      <el-table-column prop="markDate" label="批改时间" :width="cloumWidth.markDate">
      </el-table-column>
      <el-table-column prop="examStatus" label="试卷状态" :width="cloumWidth.examStatus">
        <template v-slot="scope">
          <el-tag v-if="scope.row.examStatus===0" size="medium" type="danger">未完成</el-tag>
          <el-tag v-else-if="scope.row.examStatus===1" size="medium" type="">批改中</el-tag>
          <el-tag v-else size="medium" type="success">已完成</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" :width="cloumWidth.cz">
        <template #default="scope">
          <el-tag size="medium" v-if="scope.row.examStatus===0" type="danger">未考试</el-tag>
          <el-tag v-else-if="scope.row.examStatus===1" size="medium" type="info">无操作</el-tag>
          <el-button size="mini" v-else type="info" icon="el-icon-view" @click="examQueryGrade(scope.row.userGradeId)">查看成绩</el-button>
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
import { Contents } from "@textbus/core";
export default {
  name: "TestPaperList",
  components: {},
  data() {
    return {
      options: [],
      value: "",
      examStatusOptions: [
        { value: "", label: "请选择" },
        { value: 0, label: "未完成" },
        { value: 1, label: "批改中" },
        { value: 2, label: "已完成" },
      ],
      examStatusValue: "",
      form: {},
      loading: false,
      dialogVisible: false,
      tableData: [],
      tableDatas: [],
      useYnValue: "",
      currentPage: 1,
      pageSize: 10,
      total: 0,
      search: "",
      cloumWidth: {
        id: "50",
        examName: "120",
        AnswerTime: "50",
        subjectName: "70",
        examScore: "50",
        passingScore: "50",
        grade: "50",
        examHsTime: "50",
        topicNum: "50",
        answerDate: "100",
        markDate: "170",
        examStatus: "82",
        cz: "180",
      },
    };
  },
  created() {
    this.loading = true;
    this.load();
    this.defaultTableWith();
    //初始化试卷类型
    let phone = sessionStorage.getItem("phone");
    request.post("/exam/createExamDefault", phone).then((res) => {
      //初始化类型
      this.options.push({
        value: "",
        label: "请选择",
      });
      for (let i = 0; i < res.date.subjects.length; i++) {
        this.options.push({
          value: res.date.subjects[i].id,
          label: res.date.subjects[i].subjectName,
        });
      }
    });
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
      let kd = [50, 180, 80, 90, 60, 70, 60, 80, 80, 170, 170, 90];
      let data = this.cloumWidth;
      var i = 0;
      for (var key in data) {
        this.cloumWidth[key] = parseInt((kd[i] / 1295) * width);
        i++;
      }
    },
    load() {
      let userId = sessionStorage.getItem("userId");
      //试卷类型初始化
      this.value = "";
      //试卷状态初始化
      this.examStatusValue = "";
      request
        .get("/exam/getUserExamAll", {
          params: {
            pageNumber: this.currentPage,
            pageSize: this.pageSize,
            search: this.search,
            userId: userId,
          },
        })
        .then((res) => {
          //判断token是否过期
          this.isToken(res);
          this.tableData = res.date.records; //初始化表格数据
          //初始化时间数据
          for (let i = 0; i < res.date.records.length; i++) {
            if (this.tableData[i].examHsTime != null) {
              this.tableData[i].examHsTime = this.getExamHsTime(
                this.tableData[i].examHsTime
              );
            }
          }
          this.total = res.date.total; //初始化总条数
          this.tableDatas = this.tableData;
          this.loading = false;
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
    examQueryGrade(id) {
      this.$router.push("/viewStudentTestGrade");
      const examQueryGradeUserId = this.$encryption(JSON.stringify(id));
      sessionStorage.setItem("examQueryGradeUserId", examQueryGradeUserId);
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
    //处理时间
    getExamHsTime(time) {
      var times = "";
      if (time < 60) {
        let s = time;
        if (s < 10) {
          s = "0" + s;
        }
        times = "00:00:" + s;
      } else if (time < 3600) {
        let s = time % 60;
        let m = (time - s) / 60;
        if (m < 10) {
          m = "0" + m;
        }
        if (s < 10) {
          s = "0" + s;
        }
        times = "00:" + m + ":" + s;
      } else {
        let s = time % 60;
        let m = ((time - s) / 60) % 60;
        let h = parseInt(time / 3600);
        if (h < 10) {
          h = "0" + h;
        }
        if (m < 10) {
          m = "0" + m;
        }
        if (s < 10) {
          s = "0" + s;
        }
        times = h + ":" + m + ":" + s;
      }
      return times;
    },
    examType() {
      //试卷状态初始化
      this.examStatusValue = "";
      //选择试卷类型
      const data = [];
      this.tableData = [];
      this.tableData = this.tableDatas;
      if (this.value != "") {
        for (let i = 0; i < this.tableData.length; i++) {
          if (this.tableData[i].subjectId == this.value) {
            data.push(this.tableData[i]);
          }
        }
        this.tableData = data;
      }
    },
    examStatus() {
      //试卷类型初始化
      this.value = "";
      //选择试卷状态
      const data = [];
      this.tableData = [];
      this.tableData = this.tableDatas;
      if (
        this.examStatusValue === 0 ||
        this.examStatusValue === 1 ||
        this.examStatusValue === 2
      ) {
        for (let i = 0; i < this.tableData.length; i++) {
          if (this.tableData[i].examStatus === this.examStatusValue) {
            data.push(this.tableData[i]);
          }
        }
        this.tableData = data;
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