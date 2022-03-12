<template >
  <el-dialog :title="titles" v-model="centerDialogVisible" width="40%">
    <div ref="editorContainer"></div>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="centerDialogVisible = false">确 定</el-button>
        <el-button type="danger" @click="centerDialogVisible = false,clearFwb()">清除</el-button>
      </span>
    </template>
  </el-dialog>
  <div class="createTest testPaper " @click.self="edit(-1,-1)" style="margin: 10px 0">
    <div class="w">

      <!-- sortedTopics 题库数组  click.self 进行阻止事件冒泡 -->
      <div class="test-content" @click.self="edit(-1,-1)">
        <div class="topics">
          <!-- 所有题目 -->
          <div class="topic" v-for="(s_topics, index) in sortedTopics" :key="index">
            <!-- 按类型分类好的题目 -->
            <div class="topicsType" v-if="s_topics.topic_content.length != 0">
              <div class="bigQuestionName">
                <h4> {{bigQuestionName_mixin(s_topics.type,index)}} </h4>
                <!-- 题目类型名称 -->
                <div class="allScore">
                  <el-button type="success" @click="submit()">保存试题</el-button>
                </div>
              </div>

              <div v-for="(t, tIndex) in s_topics.topic_content" :key="tIndex" @click="edit(s_topics.type,tIndex)">
                <div class="topic-content" :class="isEdit(s_topics.type,tIndex)?'isEdit':''">
                  <!-- 题目 -->
                  <div class="question">
                    <el-row>
                      <el-col :span="2" style="max-width: 4.4%;">
                        <strong class="question_nunber">{{ topicNavIndex_mixin(s_topics.type,tIndex) }}、</strong>
                      </el-col>
                      <el-col :span="22">
                        <span v-if="isEdit(s_topics.type,tIndex)">
                          <el-input v-model="t.question" type="textarea" autosize @click="codeFwbEdit(tIndex,s_topics.type,0)"></el-input>
                        </span>
                        <span v-else>
                          <div v-html="t.question "></div>
                        </span>
                      </el-col>
                    </el-row>
                  </div>

                  <!-- 单项选择题 -->
                  <div class="userAnswer" v-if="s_topics.type==0">
                    <div class="radios">
                      <el-radio @change="checked=>disableRow(checked,0,tIndex,index)" v-for="(item, index) in t.optionss" :key="index" v-model="t.answer" :label="item">
                        <span class="topicNavIndex">{{String.fromCharCode(65+index)}}、</span>
                        <span v-if="editInedx.type==0&&editInedx.index==tIndex">
                          <el-input v-model="t.optionss[index]" type="textarea" autosize :disabled="t.disableds[index]"></el-input>
                          <el-button class="delRadios" size="mini" type="danger" v-if="t.optionss.length>2" @click="delRadios(0,tIndex,index)">
                            <i class="el-icon-close"></i>
                          </el-button>
                        </span>
                        <span v-else>{{item}}</span>
                      </el-radio>
                    </div>
                    <el-button class="addRadios" size="mini" icon="el-icon-plus" @click="addRadios(s_topics.type,tIndex)">添加选项</el-button>
                  </div>

                  <!-- 多项选择题 -->
                  <div class="userAnswer" v-if="s_topics.type==1">
                    <div class="checkbox">
                      <el-checkbox-group v-model="t.answer">
                        <el-checkbox @change="checked=>disableRow(checked,1,tIndex,index)" :label="item" v-for="(item, index) in t.optionss" :key="index">
                          <span class="topicNavIndex">{{String.fromCharCode(65+index)}}、</span>
                          <span v-if="editInedx.type==1&&editInedx.index==tIndex">
                            <el-input v-model="t.optionss[index]" type="textarea" autosize :disabled="t.disableds[index]"></el-input>
                            <el-button class="delRadios" size="mini" type="danger" v-if="t.optionss.length>2" @click="delRadios(1,tIndex,index)">
                              <i class="el-icon-close"></i>
                            </el-button>
                          </span>
                          <span v-else>{{item}}</span>
                        </el-checkbox>
                      </el-checkbox-group>
                    </div>
                    <el-button class="addRadios" size="mini" icon="el-icon-plus" @click="addRadios(s_topics.type,tIndex)">添加选项</el-button>
                  </div>

                  <!-- 判断题 -->
                  <div class="userAnswer" v-if="s_topics.type==2">
                    <div class="TrueOrFalse">
                      <el-radio v-model="t.answer" label="true">正确</el-radio>
                      <el-radio v-model="t.answer" label="false">错误</el-radio>
                    </div>
                  </div>

                  <!-- 填空题 -->
                  <div class="userAnswer" v-if="s_topics.type==3">
                    <el-button size="mini" @click="addFillSymbol(tIndex)">插入填空符</el-button>

                    <div class="fillInBlank">
                      <div v-for="(q, index) in fillSymbolStr(t.question)" :key="index">
                        <el-input type="textarea" autosize placeholder="请回答" v-if="index!=fillSymbolStr(t.question).length-1" v-model="t.answer[index]">
                        </el-input>
                      </div>
                    </div>
                  </div>

                  <!-- 简答题 -->
                  <div class="userAnswer" v-if="s_topics.type==4">
                    <div class="text">
                      <strong>&nbsp;&nbsp;关键字</strong>
                      <div v-for="(q, index) in t.answer" :key="index">
                        <el-input type="textarea" autosize placeholder="请输入关键字" v-model="t.answer[index]"> </el-input>
                      </div>
                      <el-button class="addRadios" size="mini" icon="el-icon-plus" @click="addKeyWord(tIndex)">添加关键字</el-button>
                      <!-- {{t.answer}} -->
                    </div>
                  </div>

                  <!-- 代码题 -->
                  <div class="userAnswer" v-if="s_topics.type==5">
                    <el-input style="margin-left: 26px" type="textarea" placeholder="请输入答案" autosize @click="codeFwbEdit(tIndex,s_topics.type,1)"></el-input>
                  </div>
                  <!-- 正确答案 -->
                  <div class="answer">
                    <el-row>
                      <el-col :span="2">
                        <strong>正确答案: </strong>
                      </el-col>
                      <el-col :span="22">
                        <div v-html="t.answer"></div>
                      </el-col>
                    </el-row>
                  </div>
                  <div>
                    <div class="difficulty">
                      <strong>难度: </strong>
                      <span v-if="isEdit(s_topics.type,tIndex)">
                        <el-select v-model="t.difficulty" placeholder="请选择" size="mini">
                          <el-option v-for="item in topicDifficultyOptions" :key="item" :label="item" :value="item">
                          </el-option>
                        </el-select>
                      </span>
                      <span v-else>{{t.difficulty}}</span>
                    </div>
                    <div class="topicScore">
                      <strong>分值: </strong>
                      <span v-if="isEdit(s_topics.type,tIndex)">
                        <el-input-number v-model="t.score" controls-position="right" :step="1" size="mini" :min="0" />
                      </span>
                      <span v-else>{{t.score}}</span>
                      (分)
                    </div>
                  </div>
                  <div class="analysis">
                    <el-row>
                      <el-col :span="2">
                        <strong>题目解析: </strong>
                      </el-col>
                      <el-col :span="23">
                        <span v-if="isEdit(s_topics.type,tIndex)">
                          <el-input type="textarea" autosize @click="codeFwbEdit(tIndex,s_topics.type,2)" :value='correctAnswer(t.analysis)'></el-input>
                        </span>
                        <span v-else>
                          <div v-html="t.analysis "></div>
                        </span>
                      </el-col>
                    </el-row>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- <div style="clear:both;"></div> -->
      </div>
    </div>
  </div>
</template>

<script>
import "@/assets/less/testPaper.less";
import testPaperMixin from "@/mixins/testPaper-mixin";
import { ElMessage } from "element-plus";
import request from "@/utils/request";
import { createEditor } from "@textbus/textbus";
import "@textbus/textbus/bundles/textbus.min.css";
export default {
  inject: ["reload"],
  name: "TestPaperTch",
  mixins: [testPaperMixin],
  data() {
    return {
      titles: "提示",
      //富文本对象
      editors: null,
      //富文本模态框
      centerDialogVisible: false,
      //按题目类型分类好的题目数据
      //题目类型==>  0:单选题  1:多选题  2:判断题  3:填空题  4:简答题 5:代码题
      sortedTopics: [
        { type: 0, topic_content: [], score: 0, showAllScore: false },
        { type: 1, topic_content: [], score: 0, showAllScore: false },
        { type: 2, topic_content: [], score: 0, showAllScore: false },
        { type: 3, topic_content: [], score: 0, showAllScore: false },
        { type: 4, topic_content: [], score: 0, showAllScore: false },
        { type: 5, topic_content: [], score: 0, showAllScore: false },
      ],

      // //试卷数据
      testData: {
        examId: -1,
        examName: "", //试卷名称
        time: "", //答题时间
        topicNum: "", //题目数量
        passMark: "", //及格分数
        creatorName: "", //创建者
        releasing: "", //是否发布
        subjectId: "", //类型id
      },

      //当前编辑的题目索引
      editInedx: {
        type: "-1",
        index: "-1",
        input: "-2",
      },

      topicDifficultyOptions: ["简单", "中等", "困难"], //题目难度选项
    };
  },

  computed: {
    params() {
      return this.$route.params;
    },
    //试卷总分
    totalScore() {
      var score = 0;
      this.sortedTopics.forEach((element) => {
        element.topic_content.forEach((item) => {
          score += parseInt(item.score);
        });
      });
      this.testData.totalScore = score;
      return score;
    },

    //按填空符
    fillSymbolStr() {
      return function (str) {
        var q = str.split("[]");
        return q;
      };
    },
  },

  created() {
    this.getTestPaper();
  },

  mounted() {},

  methods: {
    disableRow(checked, type, tIndex, index) {
      if (type === 0) {
        let dis = this.sortedTopics[type].topic_content[tIndex].disableds;
        for (let k = 0; k < dis.length; k++) {
          this.sortedTopics[type].topic_content[tIndex].disableds[k] = false;
        }
        this.sortedTopics[type].topic_content[tIndex].disableds[index] = true;
      } else {
        this.sortedTopics[type].topic_content[tIndex].disableds[index] =
          checked;
      }
    },
    //使用取消按钮清空富文本
    clearFwb() {
      this.editors.setContents("");
    },
    codeFwbEdit(tIndex, type, flage) {
      let that = this;
      //如果富文本不为空就销毁
      if (this.editors != null) {
        this.editors.destroy();
      }
      //打开模态框
      this.centerDialogVisible = true;
      //创建富文本
      if (flage === 1) {
        this.titles = "代码题答案";
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
          let html = this.sortedTopics[type].topic_content[tIndex].answer;
          if (html != null || html != undefined) {
            editor.setContents(html);
          }
          editor.onChange.subscribe(() => {
            this.sortedTopics[5].topic_content[tIndex].answer =
              editor.getContents().content;
          });
          this.editors = editor;
        });
      } else if (flage === 2) {
        this.titles = "题目解析";
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
          let html = this.sortedTopics[type].topic_content[tIndex].analysis;
          if (html != null || html != undefined) {
            editor.setContents(html);
          }
          editor.onChange.subscribe(() => {
            this.sortedTopics[type].topic_content[tIndex].analysis =
              editor.getContents().content;
          });
          this.editors = editor;
        });
      } else {
        //题目
        this.titles = "题目编辑(填空题[]代表一个填空)";
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
          let html = this.sortedTopics[type].topic_content[tIndex].question;
          if (html != null || html != undefined) {
            editor.setContents(html);
          }
          editor.onChange.subscribe(() => {
            this.sortedTopics[type].topic_content[tIndex].question =
              editor.getContents().content;
          });
          this.editors = editor;
        });
      }
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
    //提交试题
    submit() {
      /*  处理题目信息 */
      var topicData = [];
      this.sortedTopics.forEach((element) => {
        if (element.topic_content.length != 0) {
          element.topic_content.forEach((item) => {
            //深拷贝
            var newItem = JSON.parse(JSON.stringify(item));
            topicData.push(newItem);
          });
        }
      });
      //处理选择题选项
      topicData.forEach((item) => {
        var optionss = "";
        if (item.optionss != null) {
          for (let i = 0; i < item.optionss.length; i++) {
            if (i == item.optionss.length - 1) {
              optionss += item.optionss[i];
            } else {
              optionss += item.optionss[i] + "|";
            }
          }
          item.optionss = optionss; //选项
        }
      });

      //处理正确答案
      for (let item of topicData) {
        if (item.answer.length === 0) {
          ElMessage.error("有题目未选答案！");
          return;
        }
        //判断是数组或者普通答案
        var answer = "";
        if (item.answer instanceof Array) {
          for (let i = 0; i < item.answer.length; i++) {
            if (i == item.answer.length - 1) {
              answer += item.answer[i];
            } else {
              answer += item.answer[i] + "|";
            }
          }
        } else {
          answer = item.answer;
        }
        item.answer = answer;
      }

      //处理试卷信息
      var testData = JSON.parse(JSON.stringify(this.testData));

      testData.topics = topicData;
      var data = {
        ...testData,
      };
      //判断是否有题
      if (topicData.length != 0) {
        request
          .put("topic/updateTopic", data)
          .then((res) => {
            //判断token是否过期
            this.isToken(res);
            if (res.code == 200) {
              ElMessage.success({
                message: "保存成功！",
                type: "success",
              });
              this.$router.push("/topicList");
            } else {
              ElMessage.warning({
                message: res.msg,
                type: "warning",
              });
            }
          })
          .catch((res) => {
            ElMessage.error("系统异常,保存失败！");
          });
      } else {
        ElMessage.warning({
          message: "请先添加题目",
          type: "warning",
        });
      }
    },

    //编辑试卷---获取试卷信息
    async getTestPaper() {
      let id = sessionStorage.getItem("topicId"); //题目id
      request.post("/topic/getTopicById",id).then((res) => {
        //判断token是否过期
        this.isToken(res);
        var topic = res.date;
        //处理试卷的题目数据
        if (
          topic.topicType == 4 ||
          topic.topicType == 3 ||
          topic.topicType == 1
        ) {
          topic.answer = topic.answer.split("|");
        }
        //按换行符分割字符串
        if (topic.optionss != null) {
          topic.optionss = topic.optionss.split("|");
        }

        //题目数组  sortedTopics
        for (let item of this.sortedTopics) {
          let disables = [];
          if (topic.topicType == 0) {
            for (let i = 0; i < topic.optionss.length; i++) {
              if (topic.answer === topic.optionss[i]) {
                disables[i] = true;
              } else {
                disables[i] = false;
              }
            }
          } else if (topic.topicType == 1) {
            for (let k = 0; k < topic.answer.length; k++) {
              for (let i = 0; i < topic.optionss.length; i++) {
                if (topic.answer[k] === topic.optionss[i]) {
                  disables[i] = true;
                  break;
                }
              }
            }
          }
          topic.disableds = disables;
          if (topic.topicType == item.type) {
            //添加题目
            item.topic_content.push(topic);
          }
        }
      });
    },

    //设置当前编辑中的题目
    edit(type, index) {
      if (this.editInedx.type == type && this.editInedx.index == index) {
        return;
      }
      this.editInedx.type = type;
      this.editInedx.index = index;
    },

    //是否在编辑状态
    isEdit(type, index, input = -2) {
      if (
        this.editInedx.type == type &&
        this.editInedx.index == index &&
        this.editInedx.input == input
      ) {
        return true;
      } else {
        return false;
      }
    },

    //滚动事件
    handleScroll() {
      let scrollTop =
        window.pageYOffset ||
        document.documentElement.scrollTop ||
        document.body.scrollTop; // 滚动条偏移量
      if (scrollTop > 154) {
        this.topic_nav_style = "top:" + (scrollTop + 20) + "px";
        this.isFixed = true;
      } else {
        this.isFixed = false;
      }
    },

    //点击题号导航跳转
    topicNav(type, index) {
      var totalIndex = this.topicNavIndex_mixin(type, index) - 1;
      setTimeout(() => {
        document
          .getElementsByClassName("topic-content")
          [totalIndex].scrollIntoView({ behavior: "smooth" });
      }, 100);
      this.edit(type, index);
    },

    //新建题目
    newTopic(type) {
      this.sortedTopics[type].topic_content.push({
        topicType: type,
        question: "题目",
        optionss: ["选项1", "选项2", "选项3", "选项4"],
        answer: [],
        analysis: "答案分析",
        difficulty: "中等",
        score: 10,
        subjectId: "1",
        disableds: [false, false, false, false],
      });

      var time = setTimeout(() => {
        this.topicNav(type, this.sortedTopics[type].topic_content.length - 1);
        clearInterval(time);
      }, 100);
    },

    //删除当前正在编辑的题目
    delTopic() {
      var type = this.editInedx.type;
      var index = this.editInedx.index;
      this.sortedTopics[type].topic_content.splice(index, 1);
    },

    //上移或下移当前正在编辑的题目  -1:上移   1:下移
    moveTopic(n) {
      var type = this.editInedx.type;
      var index = this.editInedx.index;

      if (index == 0 && n == -1) {
        return;
      }
      if (index == this.sortedTopics[type].topic_content.length - 1 && n == 1) {
        return;
      }

      var topic = this.sortedTopics[type].topic_content[index];
      var upTopic = this.sortedTopics[type].topic_content[index + n];

      this.sortedTopics[type].topic_content.splice(index, 1, upTopic);
      this.sortedTopics[type].topic_content.splice(index + n, 1, topic);

      var time = setTimeout(() => {
        this.topicNav(type, index + n, 10);
        clearInterval(time);
      }, 100);
    },

    //删除选项
    delRadios(type, tIndex, index) {
      this.sortedTopics[type].topic_content[tIndex].optionss.splice(index, 1);
    },

    //添加选项
    addRadios(type, tIndex) {
      var optionLength =
        this.sortedTopics[type].topic_content[tIndex].optionss.length + 1;
      if (optionLength > 10) {
        this.$message({
          message: "不能再添加选项了喔!",
          type: "warning",
        });
        return;
      }
      this.sortedTopics[type].topic_content[tIndex].optionss.push(
        "选项" + optionLength
      );
      //添加禁用
      this.sortedTopics[type].topic_content[tIndex].disableds.push(false);
    },

    //添加填空符
    addFillSymbol(tIndex) {
      var str = this.sortedTopics[3].topic_content[tIndex].question;
      this.sortedTopics[3].topic_content[tIndex].question = str + "[]";
    },

    //添加关键字
    addKeyWord(tIndex) {
      this.sortedTopics[4].topic_content[tIndex].answer.push("");
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