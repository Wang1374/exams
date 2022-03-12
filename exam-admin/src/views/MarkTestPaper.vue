<template>
  <div class="markTestPaper testPaper" style="width:100%">
    <div class="">
      <!-- 考试信息 -->
      <div class="title">
        <h3 class="testName">{{ testData.examName }}</h3>
        <ul>
          <li class="test-info">试卷Id: {{ testData.examId }}</li>
          <li class="test-info">类型: {{testData.subjectName}}</li>
          <li class="test-info">出卷者: {{testData.creatorName}}</li>
          <li class="test-info">答题时间: {{ testData.answerTime }} 分钟</li>
          <li class="test-info">题目数量: 共 {{ testData.topicNum }} 道</li>
          <li class="test-info">总分: {{ testData.totalScore }} 分</li>
        </ul>

        <ul>
          <li class="test-info">答题人: {{ testData.userName}}</li>
          <li class="test-info">答题人Id: U{{ testData.userId}}</li>
          <li class="test-info">答题耗时: {{getExamHsTime(testData.examHsTime)}}</li>
          <li class="test-info">自动评改得分: {{ testData.gradeAuto}} 分</li>
          <li class="test-info">最终得分: {{ testData.grade }} 分</li>
          <li class="test-info">
            <el-button type="primary" size="mini" @click="submitTestpaper">提交试卷</el-button>
          </li>
        </ul>
      </div>

      <div class="title fixed" v-if="isFixed">
        <ul>
          <li class="test-info"><strong class="testName">{{ testData.name }}</strong></li>
          <li class="test-info">总分: {{testData.totalScore }} 分</li>
          <li class="test-info">答题人: {{ testData.userName }}</li>
          <li class="test-info">最终得分: {{ testData.grade }} 分</li>
          <li class="test-info">
            <el-button type="primary" size="mini" @click="submitTestpaper">提交试卷</el-button>
          </li>
        </ul>
      </div>

      <div class="test-content">
        <!-- 题目内容 -->
        <div class="topics">
          <div class="topic" v-for="(topics, index) in sortedTopics" :key="index">

            <div class="topicsType" v-if="topics.topic_content.length != 0 ">
              <h4>{{bigQuestionName_mixin(topics.topic_type,index)}}</h4>
              <!-- 题目类型名称 -->
              <ul>
                <li class="topic-content" v-for="(t, index) in topics.topic_content" :key="index">
                  <div class="_location"></div>
                  <!-- 题目 -->
                  <div class="left">
                    <div class="question">
                      <el-row>
                        <el-col :span="2" style="max-width: 4.4%;">
                          <span class="question_nunber">{{ topicNavIndex_mixin(topics.topic_type,index) }}、</span>
                        </el-col>
                        <el-col :span="18">
                          <div v-html="t.topic.question"></div>
                        </el-col>
                        <el-col :span="2">
                          <span class="score">
                            <el-tag v-if="t.topic.difficulty==='简单'" size="medium" type="success">简单</el-tag>
                            <el-tag v-if="t.topic.difficulty==='中等'" size="medium" type="">中等</el-tag>
                            <el-tag v-if="t.topic.difficulty==='困难'" size="medium" type="danger">困难</el-tag>
                          </span>
                        </el-col>
                        <el-col :span="2">
                          <span class="score">({{ t.topic.score }}分)</span>
                        </el-col>
                      </el-row>
                    </div>

                    <!-- 单选题 -->
                    <div class="radio" v-if="t.topicType==0">
                      <el-radio v-for="(item, index) in t.topic.optionss" :key="index" :class="item == t.topic.answer? 'correct':'error'" v-model="t.userAnswer" :label="item" :disabled="isRead">
                        {{String.fromCharCode(65+index)}}、{{ item }}
                      </el-radio>
                    </div>

                    <!-- 多选题 -->
                    <div class="checkbox" v-if="t.topicType == 1">
                      <el-checkbox-group v-model="t.userAnswer">
                        <el-checkbox :label="item" v-for="(item, index) in t.topic.optionss" :key="index" :disabled="isRead" :class="isCheckboxCorrect(t,item)">
                          {{String.fromCharCode(65+index)}}、{{ item }}
                        </el-checkbox>
                      </el-checkbox-group>
                    </div>

                    <!-- 判断题 -->
                    <div class="TrueOrFalse" v-if="t.topicType == 2">
                      <el-radio v-model="t.userAnswer" label="true" :class="'true' == t.topic.answer? 'correct':'error'" :disabled="isRead">正确</el-radio>
                      <el-radio v-model="t.userAnswer" label="false" :class="'false' ==  t.topic.answer? 'correct':'error'" :disabled="isRead">错误</el-radio>
                    </div>

                    <!-- 填空题 -->
                    <div class="fillInBlank" v-if="t.topicType == 3">
                      <div v-for="(q, index) in t.topic.answer" :key="index">
                        <el-input type="textarea" autosize placeholder="(学生没有回答)" :disabled="isRead" v-model="t.userAnswer[index]">
                        </el-input>
                      </div>
                    </div>

                    <!-- 简答题 -->
                    <div class="text" v-if="t.topicType == 4">
                      <el-input type="textarea" v-model="t.userAnswer" :autosize="{ minRows: 3, maxRows: 10 }" placeholder="(学生没有回答)" :disabled="isRead">
                      </el-input>
                    </div>

                    <!-- 代码题 -->
                    <div class="text" v-if="t.topicType == 5">
                      学生答案:
                      <div v-html="t.userAnswer"></div>
                    </div>
                    <div v-if="t.topicType == 3||t.topicType == 4||t.topicType == 5" class="correctAnswer">
                      参考答案:
                      <div v-html="t.topic.answer"></div>
                    </div>
                  </div>
                  <div class="right">
                    <div v-if="t.topicType == 0||t.topicType == 1||t.topicType == 2">
                      <div class="correctAnswer" style="margin-top: -11px;">参考答案: {{correctOptions(t)}}</div>
                      <div class="userScore">得分: {{t.userScore}} 分</div>
                    </div>
                    <div v-else>
                      <div class="userScore">
                        得分:
                        <el-input-number v-model="t.userScore" controls-position="right" :min="-1" :max="t.topic.score"></el-input-number>
                      </div>
                    </div>
                  </div>
                </li>
              </ul>
            </div>

          </div>
        </div>

        <!-- 题目导航 -->
        <div class="topic-nav " :class="isFixed?'isFixed':''" :style="topic_nav_style">
          <div class="topic-nav-describe" style="width: 194px">
            <span class="topic-nav-but correct"> </span> 正确
            <span class="space"></span>
            <span class="topic-nav-but waring"> </span> 半对
            <span class="space"></span>
            <span class="topic-nav-but error"> </span> 错误
          </div>

          <div v-for="(topics, Topics_index) in sortedTopics" :key="Topics_index">

            <div class="topic-nav-item" v-if="topics.topic_content.length != 0">
              <div class="nav-title">{{topicTypeName_mixin(topics.topic_type)}}</div>

              <span class="topic-nav-button" @click="topicNav(topics.topic_type,index)" v-for="(item , index) in topics.topic_content" :key="index" :class="isMarkTopic(item)">
                {{topicNavIndex_mixin(topics.topic_type,index)}}
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

<script>
import "@/assets/less/testPaper.less";
import testPaperMixin from "@/mixins/testPaper-mixin";
import "@/assets/less/markTestPaper.less";
import request from "@/utils/request";
import { ElMessage } from "element-plus";
export default {
  name: "MarkTestPaper",
  mixins: [testPaperMixin],
  data() {
    return {
      //按题目类型分类好的题目数据
      //题目类型==>  0:单选题  1:多选题  2:判断题  3:填空题  4:简答题 5 代码题
      sortedTopics: [
        { topic_type: 0, topic_content: [] },
        { topic_type: 1, topic_content: [] },
        { topic_type: 2, topic_content: [] },
        { topic_type: 3, topic_content: [] },
        { topic_type: 4, topic_content: [] },
        { topic_type: 5, topic_content: [] },
      ],
      //试卷数据
      testData: {
        examClasses: {},
        userGrade: {},
      },
      isRead: true, //是否为只读模式
      //侧导航栏是否悬浮
      isFixed: false,
      topic_nav_style: "top:0px",
    };
  },

  created() {
    this.getTestPaperData();
  },

  mounted() {
    // 监听滚动事件，然后用handleScroll这个方法进行相应的处理
    window.addEventListener("scroll", this.handleScroll);
  },

  methods: {
    //提交试卷
    submitTestpaper() {
      let userTopicList = [];
      let grade = 0;
      this.testData.userTopicVos.forEach((item) => {
        grade += item.userScore;
        userTopicList.push({
          index: item.index,
          userId: item.userId,
          examId: item.examId,
          topicId: item.topicId,
          userScore: item.userScore,
          topicType: item.topicType,
        });
      });
      let param = {
        userId: this.testData.userId,
        examId: this.testData.examId,
        grade: grade,
        userTopicList: userTopicList,
      };
      //提交前检查是否批改完成
      let sorteds = this.sortedTopics;
      for (let i = 0; i < sorteds.length; i++) {
        if (sorteds[i].topic_type >= 3) {
          //再次判断分数是否为零
          let topics = sorteds[i].topic_content;
          for (let j = 0; j < topics.length; j++) {
            if (topics[j].userScore === undefined) {
              //没有批改
              let xh = this.th(sorteds[i].topic_type, j);
              this.$message.warning("第" + xh + "题目没有批改");
              return;
            }
          }
        }
      }
      request
        .post("/markTestPaper/submitExam", param)
        .then((res) => {
          //判断token是否过期
          this.isToken(res);
          if (res.code == 200) {
            ElMessage.success({
              message: "提交成功！",
              type: "success",
            });
            this.$router.push("/gradingOfPapersList");
          } else {
            ElMessage.warning({
              message: res.msg,
              type: "warning",
            });
          }
        })
        .catch((res) => {
          ElMessage.error("系统异常,获取数据失败！");
        });
    },

    //获取试卷数据
    getTestPaperData() {
      let id = sessionStorage.getItem("markTestPaperId");
      request
        .get("/markTestPaper/adminGetUserExamMark", {
          params: {
            id: id,
          },
        })
        .then((res) => {
          //判断token是否过期
          this.isToken(res);
          if (res.code == 200) {
            this.processTestPaperData(res.date);
          } else {
            ElMessage.warning({
              message: res.msg,
              type: "warning",
            });
          }
        })
        .catch((res) => {
          ElMessage.error("系统异常,获取数据失败！");
        });
    },

    //处理试卷的题目数据
    processTestPaperData(testData) {
      //处理用户答案数据
      testData.userTopicVos.forEach((item, index) => {
        //处理填空题
        if (item.userAnswer.indexOf("|") == -1 && item.topicType == 3) {
          item.topic.answer = item.topic.answer.split("|");
          testData.userTopicVos[index].topic.answer = item.topic.answer;
        }
        //按换行符分割字符串 切分用户答案数据
        if (item.userAnswer.indexOf("|") != -1) {
          item.userAnswer = item.userAnswer.split("|");
          testData.userTopicVos[index].userAnswer = item.userAnswer;
          item.topic.answer = item.topic.answer.split("|");
          testData.userTopicVos[index].topic.answer = item.topic.answer;
        }
        //处理掉 填空题
        if (item.topicType == 0 || item.topicType == 1) {
          item.topic.optionss = item.topic.optionss.split("|");
          testData.userTopicVos[index].topic.optionss = item.topic.optionss;
        }
        //处理简答题答案
        if (item.topicType == 4) {
          let answer = "";
          let value = item.topic.answer.split("|");
          for (let i = 0; i < value.length; i++) {
            answer += value[i] + "<br/>";
          }
          testData.userTopicVos[index].topic.answer = answer;
        }
      });

      //根据题目id写入用户答案
      this.testData = testData;
      //按题目类型分类并保存
      var topics = this.testData.userTopicVos;
      var topicsIndex = 1;
      for (let i = 0; i < topics.length; i++) {
        for (let item of this.sortedTopics) {
          if (topics[i].topicType == item.topic_type) {
            //添加
            topics[i].index = topicsIndex++;
            item.topic_content.push(topics[i]);
          }
        }
      }
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

    //判断题目是否批改与正确
    isMarkTopic(topic) {
      //单选题/多选题/判断题/
      let examStatus = sessionStorage.getItem("markTestPaperStatus");
      if (examStatus == 2) {
        if (topic.topic.score == topic.userScore) {
          return "correct";
        } else if (topic.userScore > 0 && topic.userScore < topic.topic.score) {
          return "waring";
        } else {
          return "error";
        }
      } else {
        if (
          topic.topicType == 0 ||
          topic.topicType == 2 ||
          topic.topicType == 1
        ) {
          if (topic.topic.score == topic.userScore) {
            return "correct";
          } else if (
            topic.userScore > 0 &&
            topic.userScore < topic.topic.score
          ) {
            return "waring";
          } else {
            return "error";
          }
        }
      }
    },

    isCheckboxCorrect(topic, val) {
      let is = false;
      topic.topic.answer.forEach((item) => {
        if (item === val) {
          is = true;
        }
      });
      if (is) {
        return "correct";
      } else {
        return "error";
      }
    },

    //正确选项参考答案选项字母化
    correctOptions(topic) {
      var correctOptions = "";
      switch (topic.topicType) {
        case 0:
          for (let i = 0; i < topic.topic.optionss.length; i++) {
            if (topic.topic.optionss[i] == topic.topic.answer) {
              correctOptions = String.fromCharCode(65 + i);
              break;
            }
          }
          break;

        case 1:
          for (let i = 0; i < topic.topic.optionss.length; i++) {
            for (let j = 0; j < topic.topic.answer.length; j++) {
              if (topic.topic.optionss[i] == topic.topic.answer[j]) {
                correctOptions += String.fromCharCode(65 + i);
                continue;
              }
            }
          }
          break;

        case 2:
          if (topic.topic.answer === "true") {
            correctOptions = "正确";
          } else {
            correctOptions = "错误";
          }
          break;
      }

      return correctOptions;
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
    th(Topics_index, index) {
      var navIndex = 0;
      for (let i = 0; i < Topics_index; i++) {
        navIndex = navIndex + this.sortedTopics[i].topic_content.length;
      }
      return navIndex + index + 1;
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
<style>
.topic-nav-button.waring,
.topic-nav-but.waring {
  background: #eed817;
  border: 1px solid #eed817 !important;
  color: #fff !important;
}
</style>