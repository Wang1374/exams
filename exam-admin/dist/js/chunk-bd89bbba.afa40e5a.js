(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-bd89bbba"],{1276:function(e,t,c){"use strict";var o=c("d784"),n=c("44e7"),r=c("825a"),a=c("1d80"),i=c("4840"),s=c("8aa5"),l=c("50c4"),p=c("577e"),u=c("14c3"),d=c("9263"),b=c("9f7f"),O=c("d039"),f=b.UNSUPPORTED_Y,j=[].push,m=Math.min,v=4294967295,y=!O((function(){var e=/(?:)/,t=e.exec;e.exec=function(){return t.apply(this,arguments)};var c="ab".split(e);return 2!==c.length||"a"!==c[0]||"b"!==c[1]}));o("split",(function(e,t,c){var o;return o="c"=="abbc".split(/(b)*/)[1]||4!="test".split(/(?:)/,-1).length||2!="ab".split(/(?:ab)*/).length||4!=".".split(/(.?)(.?)/).length||".".split(/()()/).length>1||"".split(/.?/).length?function(e,c){var o=p(a(this)),r=void 0===c?v:c>>>0;if(0===r)return[];if(void 0===e)return[o];if(!n(e))return t.call(o,e,r);var i,s,l,u=[],b=(e.ignoreCase?"i":"")+(e.multiline?"m":"")+(e.unicode?"u":"")+(e.sticky?"y":""),O=0,f=new RegExp(e.source,b+"g");while(i=d.call(f,o)){if(s=f.lastIndex,s>O&&(u.push(o.slice(O,i.index)),i.length>1&&i.index<o.length&&j.apply(u,i.slice(1)),l=i[0].length,O=s,u.length>=r))break;f.lastIndex===i.index&&f.lastIndex++}return O===o.length?!l&&f.test("")||u.push(""):u.push(o.slice(O)),u.length>r?u.slice(0,r):u}:"0".split(void 0,0).length?function(e,c){return void 0===e&&0===c?[]:t.call(this,e,c)}:t,[function(t,c){var n=a(this),r=void 0==t?void 0:t[e];return void 0!==r?r.call(t,n,c):o.call(p(n),t,c)},function(e,n){var a=r(this),d=p(e),b=c(o,a,d,n,o!==t);if(b.done)return b.value;var O=i(a,RegExp),j=a.unicode,y=(a.ignoreCase?"i":"")+(a.multiline?"m":"")+(a.unicode?"u":"")+(f?"g":"y"),h=new O(f?"^(?:"+a.source+")":a,y),g=void 0===n?v:n>>>0;if(0===g)return[];if(0===d.length)return null===u(h,d)?[d]:[];var k=0,V=0,N=[];while(V<d.length){h.lastIndex=f?0:V;var T,x=u(h,f?d.slice(V):d);if(null===x||(T=m(l(h.lastIndex+(f?V:0)),d.length))===k)V=s(d,V,j);else{if(N.push(d.slice(k,V)),N.length===g)return N;for(var w=1;w<=x.length-1;w++)if(N.push(x[w]),N.length===g)return N;V=k=T}}return N.push(d.slice(k)),N}]}),!y,f)},1989:function(e,t,c){"use strict";c("fb6a");t["a"]={name:"testPaper-mixin",data:function(){return{}},methods:{topicTypeName_mixin:function(e){switch(e){case 0:return"单选题";case 1:return"多选题";case 2:return"判断题";case 3:return"填空题";case 4:return"简答题";case 5:return"代码题"}},bigQuestionIndex:function(e){for(var t=["一、","二、","三、","四、","五、","六、"],c=0,o=0;o<e;o++)0!=this.sortedTopics[o].topic_content.length&&(c+=1);return t[c]},bigQuestionName_mixin:function(e,t){var c=this.bigQuestionIndex(t);switch(e){case 0:return c+"单项选择题";case 1:return c+"多项选择题";case 2:return c+"判断题";case 3:return c+"填空题";case 4:return c+"简答题";case 5:return c+"代码题"}},correctAnswer:function(e){return e?e.length>200?e.slice(0,200)+"...":e:""},backTop_mixin:function(){document.body.scrollTop=document.documentElement.scrollTop=0},topicNavIndex_mixin:function(e,t){for(var c=0,o=0;o<e;o++)c+=this.sortedTopics[o].topic_content.length;return c+t+1}}}},"24a5":function(e,t,c){"use strict";c("64ab")},"44e7":function(e,t,c){var o=c("861d"),n=c("c6b6"),r=c("b622"),a=r("match");e.exports=function(e){var t;return o(e)&&(void 0!==(t=e[a])?!!t:"RegExp"==n(e))}},5295:function(e,t,c){},"64ab":function(e,t,c){},"990b":function(e,t,c){},a089:function(e,t,c){"use strict";c.r(t);c("b0c0");var o=c("7a23"),n=Object(o["withScopeId"])("data-v-1854855f");Object(o["pushScopeId"])("data-v-1854855f");var r={class:"markTestPaper testPaper",style:{width:"100%"}},a={class:""},i={class:"title"},s={class:"testName"},l={class:"test-info"},p={class:"test-info"},u={class:"test-info"},d={class:"test-info"},b={class:"test-info"},O={class:"test-info"},f={class:"test-info"},j={class:"test-info"},m={class:"test-info"},v={class:"test-info"},y={class:"test-info"},h={class:"test-info"},g=Object(o["createTextVNode"])("提交试卷"),k={key:0,class:"title fixed"},V={class:"test-info"},N={class:"testName"},T={class:"test-info"},x={class:"test-info"},w={class:"test-info"},_={class:"test-info"},B=Object(o["createTextVNode"])("提交试卷"),S={class:"test-content"},D={class:"topics"},C={key:0,class:"topicsType"},I=Object(o["createVNode"])("div",{class:"_location"},null,-1),A={class:"left"},U={class:"question"},E={class:"question_nunber"},L={class:"score"},P=Object(o["createTextVNode"])("简单"),F=Object(o["createTextVNode"])("中等"),R=Object(o["createTextVNode"])("困难"),M={class:"score"},H={key:0,class:"radio"},z={key:1,class:"checkbox"},Q={key:2,class:"TrueOrFalse"},q=Object(o["createTextVNode"])("正确"),$=Object(o["createTextVNode"])("错误"),G={key:3,class:"fillInBlank"},J={key:4,class:"text"},Y={key:5,class:"text"},K=Object(o["createTextVNode"])(" 学生答案: "),W={key:6,class:"correctAnswer"},X=Object(o["createTextVNode"])(" 参考答案: "),Z={class:"right"},ee={key:0},te={class:"correctAnswer",style:{"margin-top":"-11px"}},ce={class:"userScore"},oe={key:1},ne={class:"userScore"},re=Object(o["createTextVNode"])(" 得分: "),ae=Object(o["createStaticVNode"])('<div class="topic-nav-describe" style="width:194px;" data-v-1854855f><span class="topic-nav-but correct" data-v-1854855f></span> 正确 <span class="space" data-v-1854855f></span><span class="topic-nav-but waring" data-v-1854855f></span> 半对 <span class="space" data-v-1854855f></span><span class="topic-nav-but error" data-v-1854855f></span> 错误 </div>',1),ie={key:0,class:"topic-nav-item"},se={class:"nav-title"},le=Object(o["createVNode"])("i",{class:"el-icon-arrow-up"},null,-1);Object(o["popScopeId"])();var pe=n((function(e,t,c,pe,ue,de){var be=Object(o["resolveComponent"])("el-button"),Oe=Object(o["resolveComponent"])("el-col"),fe=Object(o["resolveComponent"])("el-tag"),je=Object(o["resolveComponent"])("el-row"),me=Object(o["resolveComponent"])("el-radio"),ve=Object(o["resolveComponent"])("el-checkbox"),ye=Object(o["resolveComponent"])("el-checkbox-group"),he=Object(o["resolveComponent"])("el-input"),ge=Object(o["resolveComponent"])("el-input-number");return Object(o["openBlock"])(),Object(o["createBlock"])("div",r,[Object(o["createVNode"])("div",a,[Object(o["createVNode"])("div",i,[Object(o["createVNode"])("h3",s,Object(o["toDisplayString"])(ue.testData.examName),1),Object(o["createVNode"])("ul",null,[Object(o["createVNode"])("li",l,"试卷Id: "+Object(o["toDisplayString"])(ue.testData.examId),1),Object(o["createVNode"])("li",p,"类型: "+Object(o["toDisplayString"])(ue.testData.subjectName),1),Object(o["createVNode"])("li",u,"出卷者: "+Object(o["toDisplayString"])(ue.testData.creatorName),1),Object(o["createVNode"])("li",d,"答题时间: "+Object(o["toDisplayString"])(ue.testData.answerTime)+" 分钟",1),Object(o["createVNode"])("li",b,"题目数量: 共 "+Object(o["toDisplayString"])(ue.testData.topicNum)+" 道",1),Object(o["createVNode"])("li",O,"总分: "+Object(o["toDisplayString"])(ue.testData.totalScore)+" 分",1)]),Object(o["createVNode"])("ul",null,[Object(o["createVNode"])("li",f,"答题人: "+Object(o["toDisplayString"])(ue.testData.userName),1),Object(o["createVNode"])("li",j,"答题人Id: U"+Object(o["toDisplayString"])(ue.testData.userId),1),Object(o["createVNode"])("li",m,"答题耗时: "+Object(o["toDisplayString"])(de.getExamHsTime(ue.testData.examHsTime)),1),Object(o["createVNode"])("li",v,"自动评改得分: "+Object(o["toDisplayString"])(ue.testData.gradeAuto)+" 分",1),Object(o["createVNode"])("li",y,"最终得分: "+Object(o["toDisplayString"])(ue.testData.grade)+" 分",1),Object(o["createVNode"])("li",h,[Object(o["createVNode"])(be,{type:"primary",size:"mini",onClick:de.submitTestpaper},{default:n((function(){return[g]})),_:1},8,["onClick"])])])]),ue.isFixed?(Object(o["openBlock"])(),Object(o["createBlock"])("div",k,[Object(o["createVNode"])("ul",null,[Object(o["createVNode"])("li",V,[Object(o["createVNode"])("strong",N,Object(o["toDisplayString"])(ue.testData.name),1)]),Object(o["createVNode"])("li",T,"总分: "+Object(o["toDisplayString"])(ue.testData.totalScore)+" 分",1),Object(o["createVNode"])("li",x,"答题人: "+Object(o["toDisplayString"])(ue.testData.userName),1),Object(o["createVNode"])("li",w,"最终得分: "+Object(o["toDisplayString"])(ue.testData.grade)+" 分",1),Object(o["createVNode"])("li",_,[Object(o["createVNode"])(be,{type:"primary",size:"mini",onClick:de.submitTestpaper},{default:n((function(){return[B]})),_:1},8,["onClick"])])])])):Object(o["createCommentVNode"])("",!0),Object(o["createVNode"])("div",S,[Object(o["createVNode"])("div",D,[(Object(o["openBlock"])(!0),Object(o["createBlock"])(o["Fragment"],null,Object(o["renderList"])(ue.sortedTopics,(function(t,c){return Object(o["openBlock"])(),Object(o["createBlock"])("div",{class:"topic",key:c},[0!=t.topic_content.length?(Object(o["openBlock"])(),Object(o["createBlock"])("div",C,[Object(o["createVNode"])("h4",null,Object(o["toDisplayString"])(e.bigQuestionName_mixin(t.topic_type,c)),1),Object(o["createVNode"])("ul",null,[(Object(o["openBlock"])(!0),Object(o["createBlock"])(o["Fragment"],null,Object(o["renderList"])(t.topic_content,(function(c,r){return Object(o["openBlock"])(),Object(o["createBlock"])("li",{class:"topic-content",key:r},[I,Object(o["createVNode"])("div",A,[Object(o["createVNode"])("div",U,[Object(o["createVNode"])(je,null,{default:n((function(){return[Object(o["createVNode"])(Oe,{span:2,style:{"max-width":"4.4%"}},{default:n((function(){return[Object(o["createVNode"])("span",E,Object(o["toDisplayString"])(e.topicNavIndex_mixin(t.topic_type,r))+"、",1)]})),_:2},1024),Object(o["createVNode"])(Oe,{span:18},{default:n((function(){return[Object(o["createVNode"])("div",{innerHTML:c.topic.question},null,8,["innerHTML"])]})),_:2},1024),Object(o["createVNode"])(Oe,{span:2},{default:n((function(){return[Object(o["createVNode"])("span",L,["简单"===c.topic.difficulty?(Object(o["openBlock"])(),Object(o["createBlock"])(fe,{key:0,size:"medium",type:"success"},{default:n((function(){return[P]})),_:1})):Object(o["createCommentVNode"])("",!0),"中等"===c.topic.difficulty?(Object(o["openBlock"])(),Object(o["createBlock"])(fe,{key:1,size:"medium",type:""},{default:n((function(){return[F]})),_:1})):Object(o["createCommentVNode"])("",!0),"困难"===c.topic.difficulty?(Object(o["openBlock"])(),Object(o["createBlock"])(fe,{key:2,size:"medium",type:"danger"},{default:n((function(){return[R]})),_:1})):Object(o["createCommentVNode"])("",!0)])]})),_:2},1024),Object(o["createVNode"])(Oe,{span:2},{default:n((function(){return[Object(o["createVNode"])("span",M,"("+Object(o["toDisplayString"])(c.topic.score)+"分)",1)]})),_:2},1024)]})),_:2},1024)]),0==c.topicType?(Object(o["openBlock"])(),Object(o["createBlock"])("div",H,[(Object(o["openBlock"])(!0),Object(o["createBlock"])(o["Fragment"],null,Object(o["renderList"])(c.topic.optionss,(function(e,t){return Object(o["openBlock"])(),Object(o["createBlock"])(me,{key:t,class:e==c.topic.answer?"correct":"error",modelValue:c.userAnswer,"onUpdate:modelValue":function(e){return c.userAnswer=e},label:e,disabled:ue.isRead},{default:n((function(){return[Object(o["createTextVNode"])(Object(o["toDisplayString"])(String.fromCharCode(65+t))+"、"+Object(o["toDisplayString"])(e),1)]})),_:2},1032,["class","modelValue","onUpdate:modelValue","label","disabled"])})),128))])):Object(o["createCommentVNode"])("",!0),1==c.topicType?(Object(o["openBlock"])(),Object(o["createBlock"])("div",z,[Object(o["createVNode"])(ye,{modelValue:c.userAnswer,"onUpdate:modelValue":function(e){return c.userAnswer=e}},{default:n((function(){return[(Object(o["openBlock"])(!0),Object(o["createBlock"])(o["Fragment"],null,Object(o["renderList"])(c.topic.optionss,(function(e,t){return Object(o["openBlock"])(),Object(o["createBlock"])(ve,{label:e,key:t,disabled:ue.isRead,class:de.isCheckboxCorrect(c,e)},{default:n((function(){return[Object(o["createTextVNode"])(Object(o["toDisplayString"])(String.fromCharCode(65+t))+"、"+Object(o["toDisplayString"])(e),1)]})),_:2},1032,["label","disabled","class"])})),128))]})),_:2},1032,["modelValue","onUpdate:modelValue"])])):Object(o["createCommentVNode"])("",!0),2==c.topicType?(Object(o["openBlock"])(),Object(o["createBlock"])("div",Q,[Object(o["createVNode"])(me,{modelValue:c.userAnswer,"onUpdate:modelValue":function(e){return c.userAnswer=e},label:"true",class:"true"==c.topic.answer?"correct":"error",disabled:ue.isRead},{default:n((function(){return[q]})),_:2},1032,["modelValue","onUpdate:modelValue","class","disabled"]),Object(o["createVNode"])(me,{modelValue:c.userAnswer,"onUpdate:modelValue":function(e){return c.userAnswer=e},label:"false",class:"false"==c.topic.answer?"correct":"error",disabled:ue.isRead},{default:n((function(){return[$]})),_:2},1032,["modelValue","onUpdate:modelValue","class","disabled"])])):Object(o["createCommentVNode"])("",!0),3==c.topicType?(Object(o["openBlock"])(),Object(o["createBlock"])("div",G,[(Object(o["openBlock"])(!0),Object(o["createBlock"])(o["Fragment"],null,Object(o["renderList"])(c.topic.answer,(function(e,t){return Object(o["openBlock"])(),Object(o["createBlock"])("div",{key:t},[Object(o["createVNode"])(he,{type:"textarea",autosize:"",placeholder:"(学生没有回答)",disabled:ue.isRead,modelValue:c.userAnswer[t],"onUpdate:modelValue":function(e){return c.userAnswer[t]=e}},null,8,["disabled","modelValue","onUpdate:modelValue"])])})),128))])):Object(o["createCommentVNode"])("",!0),4==c.topicType?(Object(o["openBlock"])(),Object(o["createBlock"])("div",J,[Object(o["createVNode"])(he,{type:"textarea",modelValue:c.userAnswer,"onUpdate:modelValue":function(e){return c.userAnswer=e},autosize:{minRows:3,maxRows:10},placeholder:"(学生没有回答)",disabled:ue.isRead},null,8,["modelValue","onUpdate:modelValue","disabled"])])):Object(o["createCommentVNode"])("",!0),5==c.topicType?(Object(o["openBlock"])(),Object(o["createBlock"])("div",Y,[K,Object(o["createVNode"])("div",{innerHTML:c.userAnswer},null,8,["innerHTML"])])):Object(o["createCommentVNode"])("",!0),3==c.topicType||4==c.topicType||5==c.topicType?(Object(o["openBlock"])(),Object(o["createBlock"])("div",W,[X,Object(o["createVNode"])("div",{innerHTML:c.topic.answer},null,8,["innerHTML"])])):Object(o["createCommentVNode"])("",!0)]),Object(o["createVNode"])("div",Z,[0==c.topicType||1==c.topicType||2==c.topicType?(Object(o["openBlock"])(),Object(o["createBlock"])("div",ee,[Object(o["createVNode"])("div",te,"参考答案: "+Object(o["toDisplayString"])(de.correctOptions(c)),1),Object(o["createVNode"])("div",ce,"得分: "+Object(o["toDisplayString"])(c.userScore)+" 分",1)])):(Object(o["openBlock"])(),Object(o["createBlock"])("div",oe,[Object(o["createVNode"])("div",ne,[re,Object(o["createVNode"])(ge,{modelValue:c.userScore,"onUpdate:modelValue":function(e){return c.userScore=e},"controls-position":"right",min:-1,max:c.topic.score},null,8,["modelValue","onUpdate:modelValue","max"])])]))])])})),128))])])):Object(o["createCommentVNode"])("",!0)])})),128))]),Object(o["createVNode"])("div",{class:["topic-nav ",ue.isFixed?"isFixed":""],style:ue.topic_nav_style},[ae,(Object(o["openBlock"])(!0),Object(o["createBlock"])(o["Fragment"],null,Object(o["renderList"])(ue.sortedTopics,(function(t,c){return Object(o["openBlock"])(),Object(o["createBlock"])("div",{key:c},[0!=t.topic_content.length?(Object(o["openBlock"])(),Object(o["createBlock"])("div",ie,[Object(o["createVNode"])("div",se,Object(o["toDisplayString"])(e.topicTypeName_mixin(t.topic_type)),1),(Object(o["openBlock"])(!0),Object(o["createBlock"])(o["Fragment"],null,Object(o["renderList"])(t.topic_content,(function(c,n){return Object(o["openBlock"])(),Object(o["createBlock"])("span",{class:["topic-nav-button",de.isMarkTopic(c)],onClick:function(e){return de.topicNav(t.topic_type,n)},key:n},Object(o["toDisplayString"])(e.topicNavIndex_mixin(t.topic_type,n)),11,["onClick"])})),128))])):Object(o["createCommentVNode"])("",!0)])})),128))],6)]),Object(o["createVNode"])("div",{class:"back-top",onClick:t[1]||(t[1]=function(t){return e.backTop_mixin()})},[le])])])})),ue=c("b85c"),de=(c("159b"),c("ac1f"),c("1276"),c("5295"),c("1989")),be=(c("b73d"),c("b775")),Oe=c("7864"),fe={name:"MarkTestPaper",mixins:[de["a"]],data:function(){return{sortedTopics:[{topic_type:0,topic_content:[]},{topic_type:1,topic_content:[]},{topic_type:2,topic_content:[]},{topic_type:3,topic_content:[]},{topic_type:4,topic_content:[]},{topic_type:5,topic_content:[]}],testData:{examClasses:{},userGrade:{}},isRead:!0,isFixed:!1,topic_nav_style:"top:0px"}},created:function(){this.getTestPaperData()},mounted:function(){window.addEventListener("scroll",this.handleScroll)},methods:{submitTestpaper:function(){var e=this,t=[],c=0;this.testData.userTopicVos.forEach((function(e){c+=e.userScore,t.push({index:e.index,userId:e.userId,examId:e.examId,topicId:e.topicId,userScore:e.userScore,topicType:e.topicType})}));for(var o={userId:this.testData.userId,examId:this.testData.examId,grade:c,userTopicList:t},n=this.sortedTopics,r=0;r<n.length;r++)if(n[r].topic_type>=3)for(var a=n[r].topic_content,i=0;i<a.length;i++)if(void 0===a[i].userScore){var s=this.th(n[r].topic_type,i);return void this.$message.warning("第"+s+"题目没有批改")}be["a"].post("/markTestPaper/submitExam",o).then((function(t){e.isToken(t),200==t.code?(Oe["b"].success({message:"提交成功！",type:"success"}),e.$router.push("/gradingOfPapersList")):Oe["b"].warning({message:t.msg,type:"warning"})})).catch((function(e){Oe["b"].error("系统异常,获取数据失败！")}))},getTestPaperData:function(){var e=this,t=sessionStorage.getItem("markTestPaperId");be["a"].get("/markTestPaper/adminGetUserExamMark",{params:{id:t}}).then((function(t){e.isToken(t),200==t.code?e.processTestPaperData(t.date):Oe["b"].warning({message:t.msg,type:"warning"})})).catch((function(e){Oe["b"].error("系统异常,获取数据失败！")}))},processTestPaperData:function(e){e.userTopicVos.forEach((function(t,c){if(-1==t.userAnswer.indexOf("|")&&3==t.topicType&&(t.topic.answer=t.topic.answer.split("|"),e.userTopicVos[c].topic.answer=t.topic.answer),-1!=t.userAnswer.indexOf("|")&&(t.userAnswer=t.userAnswer.split("|"),e.userTopicVos[c].userAnswer=t.userAnswer,t.topic.answer=t.topic.answer.split("|"),e.userTopicVos[c].topic.answer=t.topic.answer),0!=t.topicType&&1!=t.topicType||(t.topic.optionss=t.topic.optionss.split("|"),e.userTopicVos[c].topic.optionss=t.topic.optionss),4==t.topicType){for(var o="",n=t.topic.answer.split("|"),r=0;r<n.length;r++)o+=n[r]+"<br/>";e.userTopicVos[c].topic.answer=o}})),this.testData=e;for(var t=this.testData.userTopicVos,c=1,o=0;o<t.length;o++){var n,r=Object(ue["a"])(this.sortedTopics);try{for(r.s();!(n=r.n()).done;){var a=n.value;t[o].topicType==a.topic_type&&(t[o].index=c++,a.topic_content.push(t[o]))}}catch(i){r.e(i)}finally{r.f()}}},handleScroll:function(){var e=window.pageYOffset||document.documentElement.scrollTop||document.body.scrollTop;e>154?(this.topic_nav_style="top:"+(e+62)+"px",this.isFixed=!0):this.isFixed=!1},topicNav:function(e,t){var c=this.topicNavIndex_mixin(e,t);document.getElementsByClassName("_location")[c-1].scrollIntoView({behavior:"smooth",block:"start"})},isMarkTopic:function(e){var t=sessionStorage.getItem("markTestPaperStatus");return 2==t||0==e.topicType||2==e.topicType||1==e.topicType?e.topic.score==e.userScore?"correct":e.userScore>0&&e.userScore<e.topic.score?"waring":"error":void 0},isCheckboxCorrect:function(e,t){var c=!1;return e.topic.answer.forEach((function(e){e===t&&(c=!0)})),c?"correct":"error"},correctOptions:function(e){var t="";switch(e.topicType){case 0:for(var c=0;c<e.topic.optionss.length;c++)if(e.topic.optionss[c]==e.topic.answer){t=String.fromCharCode(65+c);break}break;case 1:for(var o=0;o<e.topic.optionss.length;o++)for(var n=0;n<e.topic.answer.length;n++)e.topic.optionss[o]!=e.topic.answer[n]||(t+=String.fromCharCode(65+o));break;case 2:t="true"===e.topic.answer?"正确":"错误";break}return t},isToken:function(e){""===e&&(Oe["b"].error("token已过期,请重新登录!"),localStorage.removeItem("token"),sessionStorage.removeItem("phone"),this.$router.push("/"))},getExamHsTime:function(e){var t="";if(e<60){var c=e;c<10&&(c="0"+c),t="00:00:"+c}else if(e<3600){var o=e%60,n=(e-o)/60;n<10&&(n="0"+n),o<10&&(o="0"+o),t="00:"+n+":"+o}else{var r=e%60,a=(e-r)/60%60,i=parseInt(e/3600);i<10&&(i="0"+i),a<10&&(a="0"+a),r<10&&(r="0"+r),t=i+":"+a+":"+r}return t},th:function(e,t){for(var c=0,o=0;o<e;o++)c+=this.sortedTopics[o].topic_content.length;return c+t+1}}},je=(c("24a5"),c("a319"),c("6b0d")),me=c.n(je);const ve=me()(fe,[["render",pe],["__scopeId","data-v-1854855f"]]);t["default"]=ve},a319:function(e,t,c){"use strict";c("990b")},b73d:function(e,t,c){},b85c:function(e,t,c){"use strict";c.d(t,"a",(function(){return n}));c("a4d3"),c("e01a"),c("d3b7"),c("d28b"),c("3ca3"),c("ddb0");var o=c("06c5");function n(e,t){var c="undefined"!==typeof Symbol&&e[Symbol.iterator]||e["@@iterator"];if(!c){if(Array.isArray(e)||(c=Object(o["a"])(e))||t&&e&&"number"===typeof e.length){c&&(e=c);var n=0,r=function(){};return{s:r,n:function(){return n>=e.length?{done:!0}:{done:!1,value:e[n++]}},e:function(e){throw e},f:r}}throw new TypeError("Invalid attempt to iterate non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")}var a,i=!0,s=!1;return{s:function(){c=c.call(e)},n:function(){var e=c.next();return i=e.done,e},e:function(e){s=!0,a=e},f:function(){try{i||null==c["return"]||c["return"]()}finally{if(s)throw a}}}}}}]);
//# sourceMappingURL=chunk-bd89bbba.afa40e5a.js.map