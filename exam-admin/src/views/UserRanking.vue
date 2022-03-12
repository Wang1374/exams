<template>
  <div class="main">
    <div class="title">
      <div>总分成绩天梯榜</div>
    </div>
    <div class="list-box">
      <div class="list" v-for="item in list" :key="item.rank" style="    padding: 10px;">
        <div class="number">{{ item.rank }}</div>
        <div class="user">
          <span class="head-url"></span>
          <img src="../assets/image/金牌.png" class="level" v-if="item.rank==1" alt="" />
          <img src="../assets/image/银牌.png" class="level" v-if="item.rank==2" alt="" />
          <img src="../assets/image/铜牌.png" class="level" v-if="item.rank==3" alt="" />
          <div>
            <p :class="{ good: item.good == 1 }">{{ item.nickName }}</p>
            <p class="jk-num">
              <span v-if="item.alias">{{ item.alias }}</span>试卷量:{{ item.examNumber }} 题量:{{item.topicNumber}} 正确率:{{item.accuracy}}
            </p>
          </div>
        </div>
        <div class="score">总得分:{{ item.score }} 试卷总分:{{ item.examScore }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import { ElMessage } from "element-plus";
export default {
  data() {
    return {
      list: [],
    };
  },
  created() {
    //获取排名数据
    this.load();
  },
  methods: {
    load() {
      request
        .get("/rank/userGetRank")
        .then((res) => {
          //判断token是否过期
          this.isToken(res);
          if (res.code == 200) {
            //将数据存储在集合中
            this.list = res.date;
          } else {
            ElMessage.warning({
              message: res.msg,
              type: "warning",
            });
          }
        })
        .catch((res) => {
          ElMessage.error("系统异常,查询失败！");
        });
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
<style>
* {
  margin: 0;
  padding: 0;
}

.main {
  width: 100%;
  overflow: hidden;
  padding: 20px;
  box-sizing: border-box;
  color: rgb(0 0 0 / 80%);
}

.main .title {
  width: 100%;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
}

.main .title img {
  width: 50px;
  height: 50px;
  margin-right: 10px;
}

.main .list-box {
  margin: 25px 0;
  background-color: rgb(229 234 239);
  border-radius: 16px;
}

.main .list-box .list {
  display: flex;
  align-items: center;
  padding: 20px 10px;
  color: rgb(0 0 0 / 80%);
  box-sizing: border-box;
}

.main .list-box .list .number {
  width: 25px;
  text-align: left;
  color: rgb(0 0 0 / 80%);
  font-size: 17px;
}

.main .list-box .list .user {
  display: flex;
  align-items: center;
  position: relative;
}

.main .list-box .list .user .head-url {
  margin-right: 50px;
}

.main .list-box .list .user .level {
  display: block;
  position: absolute;
  top: -3.5px;
  left: -7.5px;
  width: 50px;
  z-index: 10;
}

.main .list-box .list .user .good {
  font-weight: bold;
}

.main .list-box .list .user div {
  min-width: 0;
  flex: 1;
}

.main .list-box .list .user div .jk-num {
  margin-top: 10px;
  font-size: 10px;
  color: rgb(32 149 63);
}

.main .list-box .list .user div span {
  margin-right: 10px;
  color: rgb(238, 76, 76);
}

.main .list-box .list .score {
  margin-left: auto;
  font-size: 14px;
}
</style>