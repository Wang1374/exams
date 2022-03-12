<template>
  <el-config-provider :locale="locale">
    <div>
      <!--头部区域-->
      <Header />
      <!--侧边栏-->
      <div style="display: flex">
        <Aside v-if="isAsideAlive" />
        <!--主体区域-->
        <!-- 路由匹配到的组件将显示在这里 -->
        <router-view></router-view>
      </div>
    </div>
  </el-config-provider>
</template>

<script>
// 引入头部文件
import Header from "@/components/Header";
import Aside from "@/components/Aside";
//引入中文
import { ElConfigProvider } from "element-plus";
import zhCn from "element-plus/lib/locale/lang/zh-cn";
export default {
  name: "Layout",
  components: {
    [ElConfigProvider.name]: ElConfigProvider,
    Header,
    Aside,
  },
  provide() {
    return {
      flushAside: this.flushAside,
    };
  },
  data() {
    return {
      locale: zhCn,
      isAsideAlive: true,
    };
  },
  methods: {
    flushAside() {
      this.isAsideAlive = false;
      this.$nextTick(function () {
        this.isAsideAlive = true;
      });
    },
  },
};
</script>

