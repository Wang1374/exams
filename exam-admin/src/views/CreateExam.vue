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
  <div class="createTest testPaper " @click.self="edit(-1,-1)" style="margin: 10px 0;width: 100%">
    <div>
      <!-- 试卷信息 -->
      <div class="title">
        <el-row :gutter="20">
          <el-col style="max-width:100px">
            试卷名称:
          </el-col>
          <el-col style="max-width:300px">
            <el-input clearable v-model="testData.examName" placeholder="请输入试卷名称" class="inputs"></el-input>
          </el-col>
          <el-col style="max-width:100px">
            试卷类型:
          </el-col>
          <el-col style="max-width:300px">
            <el-select v-model="value" clearable placeholder="请选择" class="inputs">
              <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-col>
          <el-col style="max-width:100px">
            是否发布:
          </el-col>
          <el-col style="max-width:300px">
            <el-switch v-model="releaseYn" active-color="#13ce66" inactive-color="#ff4949" />
          </el-col>
        </el-row>

        <ul>
          <li class="test-info">出卷者: {{ testData.creatorName }}</li>
          <li class="test-info">
            答题时间:
            <el-input-number v-model="testData.time" controls-position="right" :step="10" size="mini" :min="1" />
            分钟
          </li>
          <li class="test-info">题目数量: 共 {{ topicNavIndex_mixin(5,sortedTopics[5].topic_content.length-1) }} 道</li>
          <li class="test-info">总分: {{ totalScore }} 分</li>
          <li class="test-info">
            及格分数:
            <el-input-number v-model="testData.passMark" controls-position="right" :step="1" size="mini" :min="1" />
            分
          </li>
          <li style=" list-style-type: none;">
            <el-button size="mini" type="success" @click="submit(0)">保存试卷</el-button>
            <el-button size="mini" type="info" @click="reset()">重置试卷</el-button>
          </li>
        </ul>
      </div>
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
                  <button :class="s_topics.showAllScore? 'active':''" @click="s_topics.showAllScore = !s_topics.showAllScore">统一设置题目分数</button>
                  <el-input v-if="s_topics.showAllScore" @change="setAllScore($event,s_topics.type)" v-model="s_topics.score" placeholder="请输入分数"></el-input>
                </div>
              </div>

              <div v-for="(t, tIndex) in s_topics.topic_content" :key="tIndex" @click="edit(s_topics.type,tIndex)">
                <div class="topic-content" :class="isEdit(s_topics.type,tIndex)?'isEdit':''">
                  <!-- 题目 -->
                  <div class="question">
                    <el-row>
                      <el-col style="max-width: 30px;">
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
                          <el-button class="delRadios" size="mini" type="danger" v-if="t.optionss.length>2" @click="delRadios(0,tIndex,index)" :disabled="t.disableds[index]">
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
                            <el-button class="delRadios" size="mini" type="danger" v-if="t.optionss.length>2" @click="delRadios(1,tIndex,index)" :disabled="t.disableds[index]">
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
                      <el-col style="max-width: 80px;">
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
                  <!-- 下划线 -->
                </div>
                <el-divider></el-divider>
              </div>

            </div>
          </div>

        </div>

        <!-- 题目导航 -->

        <div class="topic-nav" :class="isFixed ? 'isFixed' : ''" :style="topic_nav_style">
          <!-- 工具 -->
          <div class="tool">
            <transition name="el-zoom-in-top">
              <div>
                <el-button v-show="!isEdit(-1,-1)" icon="el-icon-top" title="上移" @click="moveTopic(-1)"></el-button>
                <el-button v-show="!isEdit(-1,-1)" icon="el-icon-bottom" title="下移" @click="moveTopic(1)"></el-button>
                <el-button v-show="!isEdit(-1,-1)" type="danger" icon="el-icon-delete" title="删除" @click="delTopic()"></el-button>
                <!-- <el-button type="success" icon="el-icon-folder-add" title="保存" @click="submit(1)"></el-button> -->
              </div>
            </transition>
          </div>

          <!-- 导航按钮 右侧添加题型-->
          <ul v-for="(topics, Topics_index) in sortedTopics" :key="Topics_index">
            <li class="topic-nav-item" @click.self="edit(-1,-1)">
              <div class="nav-title">{{topicTypeName_mixin(topics.type)}}</div>
              <span class="topic-nav-button" :class="editInedx.type==topics.type" v-for="(item , index) in topics.topic_content" :key="index" @click="topicNav(Topics_index,index)">
                {{topicNavIndex_mixin(Topics_index,index)}}
              </span>

              <span class="topic-nav-button" @click="newTopic(topics.type)">
                <i class="el-icon-plus"></i>
              </span>
            </li>
          </ul>
        </div>

        <!-- <div style="clear:both;"></div> -->
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
        time: 120, //答题时间
        topicNum: 0, //题目数量
        passMark: 60,
        repeatTest: 1, //用户可重复考试次数
        disruptOrder: 0, //是否打乱题目顺序
        creatorName: "",
        subjectId: 0, //类型id
      },

      //当前编辑的题目索引
      editInedx: {
        type: "-1",
        index: "-1",
        input: "-2",
      },

      topicDifficultyOptions: ["简单", "中等", "困难"], //题目难度选项

      //侧导航栏是否悬浮
      isFixed: false,
      topic_nav_style: "top:0px",
      options: [],
      value: "",
      releaseYn: false,
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
    let phone = sessionStorage.getItem("phone");
    request.post("/exam/createExamDefault",phone).then((res) => {
      //初始化出卷者
      this.testData.creatorName = res.date.user.name;
      this.testData.creatorPhone = res.date.user.phone;
      //初始化类型
      for (let i = 0; i < res.date.subjects.length; i++) {
        this.options.push({
          value: res.date.subjects[i].id,
          label: res.date.subjects[i].subjectName,
        });
      }
    });
  },

  mounted() {
    // 监听滚动事件，然后用handleScroll这个方法进行相应的处理
    window.addEventListener("scroll", this.handleScroll);
  },

  methods: {
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
    //选项选中就锁定
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
    //重置试卷
    reset() {
      //刷新页面
      this.reload();
    },
    //提交试卷
    submit(value) {
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
        for (let i = 0; i < item.optionss.length; i++) {
          if (i == item.optionss.length - 1) {
            optionss += item.optionss[i];
          } else {
            optionss += item.optionss[i] + "|";
          }
        }
        item.optionss = optionss;
        item.required = item.required === true ? 1 : 0;
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
      testData.subjectId = this.value;
      testData.topicNum = topicData.length;
      if (this.releaseYn) {
        testData.releasing = 0;
      } else {
        testData.releasing = 1;
      }

      var data = {
        ...testData,
      };
      //判断是否有题
      if (topicData.length != 0) {
        request
          .post("/exam/createExam", data)
          .then((res) => {
            //判断token是否过期
            this.isToken(res);
            if (res.code == 200) {
              ElMessage.success({
                message: "保存成功！",
                type: "success",
              });
              //只有保存试卷才跳转
              if (value == 0) {
                this.$router.push("/examList");
              }
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
      let params = {
        examId: this.$route.params.tp_id,
      };
      await this.$http.get("/getTestPaperByTp_id", { params }).then((res) => {
        if (res.code == 200) {
          var testData = res.data;
        } else {
          return;
        }

        //处理试卷的题目数据
        testData.topicTchDTOList.forEach((item) => {
          if (
            item.topicType == 4 ||
            item.topicType == 3 ||
            item.topicType == 1
          ) {
            item.answer = item.answer.split(/[\n]/g);
          }
          //按换行符分割字符串
          item.optionss = item.optionss.split(/[\n]/g);
          item.required = item.required === 1 ? true : false;
        });
        testData.autoMack = testData.autoMack == 1 ? true : false;
        testData.disruptOrder = testData.disruptOrder == 1 ? true : false;
        testData.permitCopy = testData.permitCopy == 1 ? true : false;

        this.testData = testData;

        //按题目类型分类并保存
        var topics = this.testData.topicTchDTOList;
        for (let i = 0; i < topics.length; i++) {
          for (let item of this.sortedTopics) {
            if (topics[i].topicType == item.type) {
              item.topic_content.push(topics[i]);
            }
          }
        }
      });
    },

    //统一设置单个类型题目分数
    setAllScore(val, type) {
      val = parseInt(val);
      this.sortedTopics[type].topic_content.forEach((item) => {
        item.score = val;
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
        question: "<p>题目<br></p>",
        optionss: ["选项1", "选项2", "选项3", "选项4"],
        answer: [],
        analysis: "<p>答案分析<br></p>",
        difficulty: "简单",
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