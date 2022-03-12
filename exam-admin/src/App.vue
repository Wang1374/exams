<template>
  <el-config-provider :locale="locale">
    <div id="nav">
      <router-view v-if="isRouterAlive"></router-view>
    </div>
  </el-config-provider>
</template>

<script>
//引入中文
import { ElConfigProvider } from "element-plus";
import zhCn from "element-plus/lib/locale/lang/zh-cn";
export default {
  name: "APP",
  components: {
    [ElConfigProvider.name]: ElConfigProvider,
  },
  provide() {
    return {
      reload: this.reload,
    };
  },
  data() {
    return {
      locale: zhCn,
      isRouterAlive: true,
    };
  },
  methods: {
    reload() {
      this.isRouterAlive = false;
      this.$nextTick(function () {
        this.isRouterAlive = true;
      });
    },
  },
};
</script>

<style scoped>
</style>