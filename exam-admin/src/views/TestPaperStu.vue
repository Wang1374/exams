<template>
  <el-dialog title="题目答案" v-model="centerDialogVisible" width="40%">
    <div ref="editorContainer"></div>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="centerDialogVisible = false">确 定</el-button>
        <el-button type="danger" @click="centerDialogVisible = false,clearFwb()">清除</el-button>
      </span>
    </template>
  </el-dialog>
  <div class="createTest testPaper" style="width:100%">
    <div class="">
      <!-- 考试信息 -->
      <div class="title">
        <!-- {{testData}} -->
        <h3 class="testName">{{ testData.examName }}</h3><br />
        <ul>
          <li class="test-info">试卷Id: E{{ testData.examId }}</li>
          <li class="test-info">类型: {{testData.subjectName}}</li>
          <li class="test-info">出卷者: {{testData.creatorName}}</li>
          <li class="test-info">答题时间: {{ testData.time }} 分钟</li>
          <li class="test-info">题目数量: 共 {{ testData.topicNum }} 道</li>
          <li class="test-info">总分: {{ testData.totalScore }} 分</li>
          <li class="test-info">及格分数: {{ testData.passMark }} 分</li>
        </ul>

        <ul>
          <li class="test-info">答题人: {{ user.userName }}</li>
          <li class="test-info" v-if="finishTest">得分: {{ testData.userGrade.grade + '分' }}</li>
          <li class="test-info" v-else>剩余时间: {{ remainTime }}</li>

          <li class="test-info">
            <el-button type="primary" size="mini" @click="submitTestpaper" :disabled="isRead">提交试卷</el-button>
          </li>
        </ul>
      </div>

      <div class="title fixed" v-if="isFixed">
        <ul>
          <li class="test-info"><strong class="testName">{{ testData.examName }}</strong></li>
          <li class="test-info">答题时间: {{ testData.time }} 分钟</li>
          <!-- <li class="test-info">截至时间: {{ testData.examClasses.deadline }}</li> -->
          <li class="test-info" v-if="finishTest">得分: {{ testData.userGrade.grade + '分' }}</li>
          <li class="test-info" v-else>剩余时间: {{ remainTime }}</li>
          <!-- {{expendTime}} -->
          <li class="test-info">
            <el-button type="primary" size="mini" @click="submitTestpaper" :disabled="isRead">提交试卷</el-button>
          </li>
        </ul>
      </div>
      <div class="test-content">
        <!-- 题目内容 -->
        <div class="topics">
          <div class="topic" v-for="(topics, index) in sortedTopics" :key="index">

            <div class="topicsType" v-if="topics.topic_content.length != 0 ">
              <h4>{{bigQuestionName_mixin(topics.topicType,index)}}</h4><!-- 题目类型名称 -->

              <div class="topic-content" v-for="(t, index) in topics.topic_content" :key="index">
                <div class="_location"></div>
                <!-- 题目 -->
                <div class="question">
                  <el-row>
                    <el-col :span="1"> <span class="question_nunber">{{ t.index }}、</span></el-col>
                    <el-col :span="18">
                      <div class="ql-editor" v-html="t.question "></div>
                    </el-col>
                    <el-col :span="3"><span class="score">
                        <el-tag v-if="t.difficulty==='简单'" size="medium" type="success">简单</el-tag>
                        <el-tag v-if="t.difficulty==='中等'" size="medium" type="">中等</el-tag>
                        <el-tag v-if="t.difficulty==='困难'" size="medium" type="danger">困难</el-tag>
                      </span></el-col>
                    <el-col :span="2"><span class="score">({{ t.score }}分)</span></el-col>
                  </el-row>

                </div>

                <!-- 单选题 -->
                <div class="radio" v-if="t.topicType==0">
                  <!-- 遍历选项 -->
                  <el-radio v-for="(item, index) in t.optionss" :key="index" v-model="t.userAnswer" :label="item" :disabled="isRead">
                    {{String.fromCharCode(65+index)}}、{{ item }}
                  </el-radio>
                  <!-- {{ t.userAnswer }} -->
                </div>

                <!-- 多选题 -->
                <div class="checkbox" v-if="t.topicType == 1">
                  <el-checkbox-group v-model="t.userAnswer">
                    <el-checkbox :label="item" v-for="(item, index) in t.optionss" :key="index" :disabled="isRead" :class="isCheckboxCorrect(t,item)">
                      {{String.fromCharCode(65+index)}}、{{ item }}
                    </el-checkbox>
                  </el-checkbox-group>
                  <!-- {{ t.userAnswer }} -->
                </div>

                <!-- 判断题 -->
                <div class="TrueOrFalse" v-if="t.topicType == 2">
                  <el-radio v-model="t.userAnswer" label="true" :disabled="isRead" :class="'true' == t.correctAnswer? 'correct':'error'">正确</el-radio>
                  <el-radio v-model="t.userAnswer" label="false" :disabled="isRead" :class="'false' == t.correctAnswer? 'correct':'error'">错误</el-radio>
                  <!-- {{ t.userAnswer }} -->
                </div>

                <!-- 填空题 -->
                <div class="fillInBlank" v-if="t.topicType == 3">
                  <div v-for="(q, index) in fillSymbolStr(t.question)" :key="index">
                    <el-input type="textarea" autosize placeholder="请回答" :disabled="isRead" v-if="index!=fillSymbolStr(t.question).length-1" v-model="t.userAnswer[index]">
                    </el-input>
                  </div>
                  <!-- {{ t.userAnswer }} -->
                </div>

                <!-- 简答题 -->
                <div class="" v-if="t.topicType == 4">
                  <el-input type="textarea" v-model="t.userAnswer" :autosize="{ minRows: 3, maxRows: 10 }" :disabled="isRead" placeholder="请回答"></el-input>
                </div>

                <!-- 代码题 -->
                <div class="text" v-if="t.topicType == 5">
                  <el-input type="textarea" autosize :disabled="isRead" placeholder="点击输入框,填写答案" @click="createCodeFwb(index,t.topicType)"></el-input>
                  <br />
                  <div v-html="t.userAnswer"></div>
                </div>
              </div>
            </div>

          </div>
        </div>

        <!-- 题目导航 -->
        <div class="topic-nav " :class="isFixed?'isFixed':''" :style="topic_nav_style">
          <div class="topic-nav-describe" v-if="finishTest">
            <span class="topic-nav-but correct"> </span> 正确
            <span class="space"></span>
            <span class="topic-nav-but error"> </span> 错误
          </div>
          <div class="topic-nav-describe" v-else>
            <span class="topic-nav-but hasAnswer"> </span> 已答
            <span class="space"></span>
            <span class="topic-nav-but "> </span> 未答
          </div>

          <div v-for="(topics, Topics_index) in sortedTopics" :key="Topics_index">

            <div class="topic-nav-item" v-if="topics.topic_content.length != 0">
              <div class="nav-title">{{topicTypeName_mixin(topics.topicType)}}</div>

              <!-- <span class="topic-nav-button" @click="topicNav(topics.topicType,index)" v-for="(item , index) in topics.topic_content" :key="index" :class="emptyAnswer(item.userAnswer) ?'':'hasAnswer'">
                {{topicNavIndex_mixin(topics.topicType,index)}}
              </span> -->
              <span class="topic-nav-button" @click="topicNav(topics.topicType,index)" v-for="(item , index) in topics.topic_content" :key="index" :class="emptyAnswer(item)">
                {{topicNavIndex_mixin(topics.topicType,index)}}
              </span>
            </div>

          </div>
        </div>

      </div>

      <div class="back-top" @click="backTop_mixin()">
        <i class="el-icon-arrow-up"></i>
      </div>

    </div>

  </div>
</template>

<script type="text/javascript">
import "@/assets/less/testPaper.less";
import testPaperMixin from "@/mixins/testPaper-mixin";
import { ElMessage } from "element-plus";
import request from "@/utils/request";
import { createEditor } from "@textbus/textbus";
import "@textbus/textbus/bundles/textbus.min.css";
export default {
  inject: ["reload"],
  name: "TestPaperStu",
  mixins: [testPaperMixin],
  data() {
    return {
      //试卷状态
      examStatus: false,
      //模态框状态
      centerDialogVisible: false,
      //富文本对象
      editors: null,
      //用户名
      user: {
        id: 0,
        userName: "",
      },
      //按题目类型分类好的题目数据
      //题目类型==>  0:单选题  1:多选题  2:判断题  3:填空题  4:简答题 5:代码题
      sortedTopics: [
        { topicType: 0, topic_content: [] },
        { topicType: 1, topic_content: [] },
        { topicType: 2, topic_content: [] },
        { topicType: 3, topic_content: [] },
        { topicType: 4, topic_content: [] },
        { topicType: 5, topic_content: [] },
      ],
      //试卷数据
      testData: {
        examClasses: {},
        userGrade: {},
      },
      remainTime: "00:00:00", //考试剩余时间
      expendTime: 0, //考试用时(秒)
      isRead: false, //是否为只读模式
      switchPage: 0,

      //isPublishAnswer: false, //是否公布答案
      finishTest: false, //是否完成试卷
      //侧导航栏是否悬浮
      isFixed: false,
      topic_nav_style: "top:0px",
    };
  },
  computed: {
    //按填空符([])划分字符串
    fillSymbolStr() {
      return function (str) {
        var q = str.split("[]");
        return q;
      };
    },
  },

  created() {
    //初始化考试信息
    //初始化考生姓名
    let phone = sessionStorage.getItem("phone");
    request.post("/user/getUserByPhone", phone).then((res) => {
      this.isToken(res);
      this.user.id = res.date.id;
      this.user.userName = res.date.name;
    });
    //初始化试题
    this.getTestPaperData();
    //初始化富文本
    //this.createCodeFwb();
  },

  mounted() {
    window.addEventListener("scroll", this.handleScroll);
    // window.addEventListener("visibilitychange", this.visibilitychange);
  },

  methods: {
    //清除富文本内容
    clearFwb() {
      this.editors.setContents("");
    },
    //提交试卷
    submitTestpaper() {
      console.log("调用 提交试卷---------------")
      let examId = sessionStorage.getItem("examId");
      var topic = [];
      for (let i = 0; i < this.testData.topics.length; i++) {
        var item = JSON.parse(JSON.stringify(this.testData.topics[i]));
        // 判断是否选项为空
        if (item.userAnswer === null || item.userAnswer.length === 0) {
          this.$message.warning(`第${item.index}题目没有作答`);
          return;
        }
        //处理多选/填空答案
        if (item.topicType == 1 || item.topicType == 3) {
          //判断是否是数组
          if (item.userAnswer instanceof Array) {
            var userAnswer = "";
            item.userAnswer.forEach((c) => {
              userAnswer += c + "|";
            });
            //将最后一个|删除
            item.userAnswer = userAnswer.slice(0, -1);
          }
        }

        //习题添加数据
        topic.push({
          topicId: item.id,
          topicType: item.topicType,
          userId: this.user.id,
          examId: examId,
          userAnswer: item.userAnswer,
        });
      }

      var date = {
        examId: examId,
        examName: this.testData.examName,
        userName: this.user.userName,
        userId: this.user.id,
        answerTime: this.expendTime,
        userTopicList: topic,
      };
      //提交数据
      request
        .post("/userGrade/saveUserGrade/", date)
        .then((res) => {
          this.isToken(res);
          if (res.code == 200) {
            ElMessage.success({
              message: "提交成功！",
              type: "success",
            });
            //跳转到试卷列表页面
            this.$router.push("/testPaperList");
          } else {
            ElMessage.warning({
              message: res.msg,
              type: "warning",
            });
          }
        })
        .catch((res) => {
          ElMessage.error("系统异常,提交失败！");
        });
    },

    //获取试卷数据
    getTestPaperData() {
      let id = sessionStorage.getItem("StudentExamId");
      let examStatus = sessionStorage.getItem("StudentExamStatus");
      if (examStatus == 2) {
        //设置状态已完成考试
        this.examStatus = true;
      }
      request.post("/exam/editExamDefault", id).then((res) => {
        //判断token是否过期
        this.isToken(res);
        if (res.code == 200) {
          this.processTestPaperData(res.date);
        } else {
          return;
        }
      });
    },

    //处理试卷的题目数据
    processTestPaperData(testData) {
      /* 处理试卷的题目数据 */
      testData.topics.forEach((item) => {
        //按换行符分割字符串
        if (item.optionss != null) {
          item.optionss = item.optionss.split("|");
        }

        //初始化用户答案
        if (item.topicType == 1 || item.topicType == 3) {
          item.userAnswer = [];
        } else {
          item.userAnswer = "";
        }
      });

      /* 判断用户是否已经完成试卷 */
      if (!this.examStatus) {
        this.finishTest = false;
        this.isRead = false;
      } else {
        this.isRead = true;
      }

      this.testData = testData;
      if (this.finishTest === false) {
        //开始计时
        this.remainTimer();
      } else {
        this.isRead = true;
      }
      //按题目类型分类并保存
      var topics = this.testData.topics;
      var topicsIndex = 1;
      for (let item of this.sortedTopics) {
        for (let i = 0; i < topics.length; i++) {
          if (topics[i].topicType == item.topicType) {
            //添加
            topics[i].index = topicsIndex++;
            item.topic_content.push(topics[i]);
          }
        }
      }
    },

    //计算剩余时间
    remainTimer() {
      var time = this.testData.time * 60;
      var timer = setInterval(() => {
        this.remainTime = this.computateTime(time);
        time--;
        this.expendTime++; //考试用时 s
        if (time < 0) {
          //结束定时回调
          clearInterval(timer);
          ElMessage.error("考试结束!");
          console.log("考试----结束")
          this.submitTestpaper();
        }
      }, 1000);
    },

    //格式化考试剩余时间
    computateTime(time) {
      var sec = "00";
      var minute = "00";
      var hour = "00";

      if (time >= 0) {
        sec = time % 60;
        if (sec < 10) {
          sec = "0" + sec;
        }
      }
      if (time / 60 >= 0) {
        minute = parseInt((time / 60) % 60);
        if (minute < 10) {
          minute = "0" + minute;
        }
      }
      if (time / 60 / 60 >= 0) {
        hour = parseInt((time / 60 / 60) % 60);
        if (hour < 10) {
          hour = "0" + hour;
        }
      }
      return hour + ":" + minute + ":" + sec;
    },

    //滚动事件
    handleScroll() {
      let scrollTop =
        window.pageYOffset ||
        document.documentElement.scrollTop ||
        document.body.scrollTop; // 滚动条偏移量
      if (scrollTop > 154) {
        this.topic_nav_style = "top:" + (scrollTop + 62) + "px";
        this.isFixed = true;
      } else {
        this.isFixed = false;
      }
    },

    //点击题号定位到题目位置
    topicNav(type, index) {
      var i = this.topicNavIndex_mixin(type, index);
      document
        .getElementsByClassName("_location")
        [i - 1].scrollIntoView({ behavior: "smooth", block: "start" });
    },

    //题目导航按钮颜色
    emptyAnswer(val) {
      //已完成试卷 与 是否公布答案
      if (this.finishTest === true) {
        if (val.userScore == val.score) {
          return "correct";
        } else {
          return "error";
        }

        //未完成试卷
      } else {
        //多选题
        // if (val.topicType == 1) {
        //   if (val.userAnswer.join("") == "") {
        //     return "";
        //   }
        // }

        //填空题
        if (val.topicType == 3) {
          let q = val.question.split("[]");
          if (q.length - 1 != val.userAnswer.length) {
            return "";
          }

          for (let item of val.userAnswer) {
            if (item == "") {
              return "";
            }
          }
        }

        //单选/判断/简答
        if (val.userAnswer.length == 0) {
          return "";
        }

        return "hasAnswer";
      }
    },

    //判断选择题是否回答正确
    isCheckboxCorrect(topic, val) {
      if (this.finishTest === false) {
        return "";
      }
      let is = false;
      topic.answer.forEach((item) => {
        if (item == val) {
          is = true;
        }
      });
      if (is) {
        return "correct";
      } else {
        return "error";
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
    //创建富文本
    createCodeFwb(tIndex, type) {
      let that = this;
      //如果富文本不为空就销毁
      if (this.editors != null) {
        this.editors.destroy();
      }
      //打开模态框
      this.centerDialogVisible = true;
      //创建富文本
      this.$nextTick(() => {
        const editor = createEditor(this.$refs.editorContainer, {
          uploader(uploadType, prevValue) {
            switch (uploadType) {
              case "image": // 图片
                return that.uploadImage();
                break;
              case "video": // 视频
                ElMessage.error("暂不支持上传视频！");
                break;
              case "audio":
                ElMessage.error("暂不支持上传音频！");
                break;
            }
          },
        });
        let html = this.sortedTopics[type].topic_content[tIndex].userAnswer;
        if (html != null || html != undefined) {
          editor.setContents(html);
        }
        editor.onChange.subscribe(() => {
          this.sortedTopics[type].topic_content[tIndex].userAnswer =
            editor.getContents().content;
        });
        this.editors = editor;
      });
    },
    //上传图片
    uploadImage() {
      var that = this;
      var fileInput = document.createElement("input");
      fileInput.setAttribute("type", "file");
      fileInput.setAttribute(
        "accept",
        "image/png, image/jpg, image/jpeg, image/gif"
      );
      fileInput.style.cssText =
        "position: absolute; left: -9999px; top: -9999px; opacity: 0";
      var promise = new Promise(function (resolve) {
        fileInput.addEventListener("change", function (event) {
          var form = new FormData();
          var files = event.target.files;
          for (var i = 0; i < files.length; i++) {
            form.append("file", files[i]);
          }
          document.body.removeChild(fileInput);
          //上传接口
          request
            .post("/file/uploadImage", form)
            .then((res) => {
              //判断token是否过期
              that.isToken(res);
              if (res.code == 200) {
                resolve(res.date);
                ElMessage.success({
                  message: "上传成功！",
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
              ElMessage.error("系统异常,上传失败！");
            });
        });
      });
      document.body.appendChild(fileInput);
      fileInput.click();
      return promise;
    },
  },
};
</script>
<style scoped>
/* 富文本样式表 */
div /deep/body {
  word-break: break-word;
}
div /deep/blockquote {
  padding: 10px 15px;
  border-left: 10px solid #dddee1;
  background-color: #f8f8f9;
  margin: 1em 0;
  border-radius: 4px;
}
div /deep/code,
pre {
  background-color: #fefefe;
}
div /deep/pre code {
  padding: 0;
  border: none;
  background: none;
  border-radius: 0;
  vertical-align: inherit;
}
div /deep/code {
  padding: 1px 5px;
  border-radius: 3px;
  vertical-align: middle;
  border: 1px solid rgba(0, 0, 0, 0.08);
}
div /deep/pre {
  line-height: 1.418em;
  display: flex;
  border-radius: 5px;
  border: 1px solid #e9eaec;
  word-break: break-all;
  word-wrap: break-word;
  white-space: pre-wrap;
  overflow: hidden;
  position: relative;
}
div /deep/code,
kbd,
pre,
samp {
  font-family: Microsoft YaHei Mono, Menlo, Monaco, Consolas, Courier New,
    monospace;
}
div /deep/.tb-code-line-number-bg {
  background-color: #f9f9f9;
  border-right: 1px solid #ddd;
  width: 3em;
}
div /deep/.tb-code-content {
  flex: 1;
  padding: 15px 15px 15px 0.5em;
  counter-reset: codeNum;
}
div /deep/.tb-code-line {
  position: relative;
}
div /deep/.tb-code-line::before {
  counter-increment: codeNum;
  content: counter(codeNum);
  position: absolute;
  left: -3.5em;
  top: 0;
  width: 2em;
  text-align: right;
  padding: 0 0.5em;
  overflow: hidden;
  white-space: nowrap;
  color: #aeaeae;
}
div /deep/.tb-pre-lang {
  position: absolute;
  right: 0;
  top: 0;
  opacity: 0.5;
  pointer-events: none;
  font-size: 13px;
  padding: 4px 10px;
}
div /deep/.tb-hl-keyword {
  font-weight: bold;
}
div /deep/.tb-hl-string {
  color: rgb(221, 17, 68);
}
div /deep/.tb-hl-function {
  color: rgb(0, 134, 179);
}
div /deep/.tb-hl-number {
  color: #388138;
}
div /deep/.tb-hl-tag {
  color: rgb(0, 0, 128);
}
div /deep/.tb-hl-comment {
  color: rgb(153, 153, 136);
  font-style: italic;
}
div /deep/.tb-hl-boolean {
  color: #388138;
  font-weight: bold;
}
div /deep/.tb-hl-builtin {
  color: rgb(0, 134, 179);
}
div /deep/.tb-hl-regex {
  color: #f60;
}
div /deep/.tb-hl-attr-name {
  color: rgb(0, 134, 179);
}
div /deep/.tb-hl-attr-value {
  color: rgb(221, 17, 68);
}
div /deep/.tb-hl-class-name {
  color: rgb(0, 134, 179);
  font-weight: bold;
}
div /deep/.tb-hl-selector {
  color: rgb(0, 134, 179);
  font-weight: bold;
}
pre[theme="dark"] {
  color: #a9aeb2;
  background-color: #1c2838;
  border-color: #353535;
}
pre[theme="dark"] div /deep/.tb-hl-keyword {
  color: rgb(0, 134, 179);
}
pre[theme="dark"] div /deep/.tb-hl-tag {
  color: rgb(0, 134, 179);
}
pre[theme="dark"] div /deep/.tb-hl-comment {
  color: #4c5156;
}
pre[theme="dark"] div /deep/.tb-hl-string {
  color: #ce5a70;
}
pre[theme="dark"] div /deep/.tb-hl-attr-value {
  color: #ce5a70;
}
pre[theme="dark"] div /deep/.tb-hl-regex {
  color: #af741d;
}
pre[theme="dark"] div /deep/.tb-hl-selector {
  color: #ce5a70;
  font-weight: normal;
}
pre[theme="dark"] div /deep/.tb-code-line::before {
  color: #536171;
}
pre[theme="dark"] div /deep/.tb-code-line-number-bg {
  background-color: #2d3a48;
  border-right-color: #292929;
}
</style>
