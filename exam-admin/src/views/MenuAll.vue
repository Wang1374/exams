<template>
  <div style="padding:10px;" class="kd">
    <div style="margin: 10px 0">
      <!--功能区域-->
      <el-button @click="addMenu">添加</el-button>
      <el-input v-model="search" placeholder="请输入名称" style="width: 200px;margin-left: 5px" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load" icon="el-icon-search">查询</el-button>
    </div>
    <el-table :data="tableData" stripe style="width: 99%" row-key="id" v-loading="loading" element-loading-text="拼命加载中" :tree-props="{children: 'menu', hasChildren: false }">
      <el-table-column :width="cloumWidth.id" label="序号" type="index" shows-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="id" label="ID" v-if="false">
      </el-table-column>
      <el-table-column prop="title" label="名称" :width="cloumWidth.title">
      </el-table-column>
      <el-table-column prop="icon" label="图标" :width="cloumWidth.icon">
        <template v-slot="scope">
          <i :class="scope.row.icon || ''"></i>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="类型" :width="cloumWidth.type">
        <template v-slot="scope">
          <el-tag v-if="scope.row.type===0" size="medium" type="info">目录</el-tag>
          <el-tag v-else size="medium" type="success">菜单</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="shows" label="是否显示" :width="cloumWidth.shows">
        <template v-slot="scope">
          <el-switch v-model="scope.row.shows" active-color="#13ce66" inactive-color="#ff4949" @change="editActive(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="priority" label="排序" :width="cloumWidth.priority">
      </el-table-column>
      <el-table-column prop="menus" label="上级菜单" :width="cloumWidth.menus">
      </el-table-column>
      <el-table-column prop="role" label="访问权限" :width="cloumWidth.role">
        <template v-slot="scope">
          <el-tag v-if="scope.row.role===0" size="medium" type="danger">管理员</el-tag>
          <el-tag v-else-if="scope.row.role===1" size="medium" type="primary">教师</el-tag>
          <el-tag v-else-if="scope.row.role===2" size="medium" type="info">学生</el-tag>
          <el-tag v-else size="medium" type="success">所有者</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="url" label="URL" :width="cloumWidth.url">
      </el-table-column>
      <el-table-column prop="name" label="路由名称" :width="cloumWidth.name">
      </el-table-column>
      <el-table-column label="操作" :width="cloumWidth.cz">
        <template #default="scope">
          <el-button size="small" type="text" @click="handleEdit(scope.row)" icon="el-icon-edit">编辑</el-button>
          <el-popconfirm title="你确定要删除吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="small" type="text" icon="el-icon-delete">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin: 10px 0">
      <!--弹出框-->
      <el-dialog :title="title" v-model="dialogVisible" width="35%">
        <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
          <el-tab-pane label="目录" name="catalogue">
            <el-form ref="catalogueForm" :model='catalogueForm' label-width="80px" :rules="catalogue">
              <el-form-item label="目录名称" prop="title">
                <el-input v-model="catalogueForm.title" style="width: 90%"></el-input>
              </el-form-item>
              <el-form-item label="目录图标" prop="icon">
                <el-input v-model="catalogueForm.icon" style="width: 90%"></el-input>
              </el-form-item>
              <el-form-item label="目录显示" prop="shows">
                <el-switch v-model="catalogueForm.shows" active-color="#13ce66" inactive-color="#ff4949" style="width: 90%"></el-switch>
              </el-form-item>
              <el-form-item label="目录排序" prop="priority">
                <el-input-number v-model="catalogueForm.priority" style="width: 90%"></el-input-number>
              </el-form-item>
              <el-form-item label="目录权限" prop="role">
                <el-select v-model="catalogueForm.role" placeholder="请选择" style="width: 90%">
                  <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="菜单" name="menu">
            <el-form ref="menuForm" :model='menuForm' label-width="80px" :rules="menu">
              <el-form-item label="菜单名称" prop="title">
                <el-input v-model="menuForm.title" style="width: 90%"></el-input>
              </el-form-item>
              <el-form-item label="上级目录" prop="parentId">
                <el-select v-model="menuForm.parentId" placeholder="请选择" style="width: 90%">
                  <el-option v-for="item in catalogues" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="菜单URL" prop="url">
                <el-input v-model="menuForm.url" style="width: 90%"></el-input>
              </el-form-item>
              <el-form-item label="路由名称" prop="name">
                <el-input v-model="menuForm.name" style="width: 90%"></el-input>
              </el-form-item>
              <el-form-item label="菜单显示" prop="shows">
                <el-switch v-model="menuForm.shows" active-color="#13ce66" inactive-color="#ff4949" style="width: 90%"></el-switch>
              </el-form-item>
              <el-form-item label="菜单排序" prop="priority">
                <el-input-number v-model="menuForm.priority" style="width: 90%"></el-input-number>
              </el-form-item>
              <el-form-item label="菜单权限" prop="role">
                <el-select v-model="menuForm.role" placeholder="请选择" style="width: 90%">
                  <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="dialog()">确 定</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import request from "@/utils/request";
import { ElMessage } from "element-plus";
export default {
  inject: ["flushAside"],
  name: "MenuAll",
  components: {},
  data() {
    return {
      activeName: "catalogue",
      loading: false,
      catalogueForm: {},
      catalogues: [],
      menuForm: {},
      dialogVisible: false,
      tableData: [],
      useYnValue: "",
      currentPage: 1,
      pageSize: 10,
      search: "",
      cloumWidth: {
        id: "50",
        title: "120",
        icon: "40",
        type: "50",
        shows: "100",
        priority: "170",
        menus: "100",
        role: "70",
        url: "70",
        name: "70",
        cz: "180",
      },
      options: [
        {
          value: 3,
          label: "所有者",
        },
        {
          value: 2,
          label: "学生",
        },
        {
          value: 1,
          label: "老师",
        },
        {
          value: 0,
          label: "管理员",
        },
      ],
      catalogue: {
        title: [
          { required: true, message: "请输入目录名称", trigger: "blur" },
          { min: 2, max: 10, message: "请输入2-10位字符", trigger: "blur" },
        ],
        icon: [{ required: true, message: "请输入图标代码", trigger: "blur" }],
        priority: [
          { required: true, message: "请输入目录排序序号", trigger: "blur" },
        ],
        role: [
          { required: true, message: "请选择目录权限所属者", trigger: "blur" },
        ],
      },
      menu: {
        title: [
          { required: true, message: "请输入菜单名称", trigger: "blur" },
          { min: 2, max: 10, message: "请输入2-10位字符", trigger: "blur" },
        ],
        parentId: [
          { required: true, message: "请选择上级目录", trigger: "blur" },
        ],
        name: [{ required: true, message: "请输入路由名称", trigger: "blur" }],
        url: [{ required: true, message: "请输入菜单URL", trigger: "blur" }],
        priority: [
          { required: true, message: "请输入菜单排序序号", trigger: "blur" },
        ],
        role: [
          { required: true, message: "请选择菜单权限所属者", trigger: "blur" },
        ],
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
      let kd = [50, 140, 60, 80, 80, 80, 100, 120, 200, 200, 170];
      let data = this.cloumWidth;
      var i = 0;
      for (var key in data) {
        this.cloumWidth[key] = parseInt((kd[i] / 1295) * width);
        i++;
      }
    },
    load() {
      this.loading = true;
      if (!this.search) {
        this.search = null;
      }
      request
        .get("/menu/getMenuAll/" + this.search)
        .then((res) => {
          //判断token是否过期
          this.isToken(res);
          if (res.code == 200) {
            //封装数据
            const menuData = [];
            res.date.forEach((item) => {
              const list = [];
              item.menu.forEach((value) => {
                //拿到菜单列表
                list.push({
                  id: value.listId,
                  title: value.listTitle,
                  icon: "",
                  type: 1,
                  shows: value.listShow,
                  priority: value.listPriority,
                  role: value.listRole,
                  url: value.url,
                  menus: item.menuTitle,
                  name: value.name,
                  parentId: item.menuId,
                });
              });
              menuData.push({
                id: item.menuId,
                title: item.menuTitle,
                icon: item.icon,
                type: 0,
                shows: item.menuShow,
                priority: item.menuPriority,
                role: item.menuRole,
                url: "",
                menus: "",
                name: "",
                menu: list,
              });
              //封装上级目录
              this.catalogues.push({
                value: item.menuId,
                label: item.menuTitle,
              });
            });
            //打印最终数据
            this.tableData = menuData;
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
      this.loading = false;
    },
    addMenu() {
      this.title = "添加";
      this.dialogVisible = true;
      //更新前清空数据
      if (this.$refs.catalogueForm) {
        this.catalogueForm = {};
        this.$refs.catalogueForm.resetFields();
      }
      if (this.$refs.menuForm) {
        this.menuForm = {};
        this.$refs.menuForm.resetFields();
      }
    },
    dialog() {
      if (this.catalogueForm.id) {
        //存在id则更新
        this.$refs["catalogueForm"].validate((valid) => {
          if (valid) {
            if (!this.catalogueForm.shows) {
              this.catalogueForm.shows = false;
            }
            this.catalogueForm.level = 1;
            this.update(this.catalogueForm);
          }
        });
      } else {
        this.$refs["catalogueForm"].validate((valid) => {
          if (valid) {
            //新增目录
            if (!this.catalogueForm.shows) {
              this.catalogueForm.shows = false;
            }
            this.catalogueForm.level = 1;
            this.add(this.catalogueForm);
          }
        });
      }

      if (this.menuForm.id) {
        //存在id则更新
        this.$refs["menuForm"].validate((valid) => {
          if (valid) {
            if (!this.menuForm.shows) {
              this.menuForm.shows = false;
            }
            this.menuForm.level = 2;
            this.update(this.menuForm);
          }
        });
      } else {
        this.$refs["menuForm"].validate((valid) => {
          if (valid) {
            if (!this.menuForm.shows) {
              this.menuForm.shows = false;
            }
            this.menuForm.level = 2;
            this.add(this.menuForm);
          }
        });
      }
    },
    //新增目录
    add(value) {
      this.dialogVisible = false;
      request
        .post("/menu/add", value)
        .then((res) => {
          //判断token是否过期
          this.isToken(res);
          if (res.code == 200) {
            this.dialogVisible = false;
            ElMessage.success({
              message: "添加成功！",
              type: "success",
            });
            this.load();
            this.flushAside();
          } else {
            ElMessage.warning({
              message: res.msg,
              type: "warning",
            });
          }
        })
        .catch((res) => {
          ElMessage.error("系统异常,添加失败！");
        });
    },
    update(value) {
      this.dialogVisible = false;
      //更新数据
      request
        .put("/menu/update", value)
        .then((res) => {
          //判断token是否过期
          this.isToken(res);
          if (res.code == 200) {
            ElMessage.success({
              message: "更新成功！",
              type: "success",
            });
            this.load();
            this.flushAside();
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
    handleDelete(id) {
      //删除数据
      request
        .delete("/menu/delete/" + id)
        .then((res) => {
          //判断token是否过期
          this.isToken(res);
          if (res.code == 200) {
            ElMessage.success({
              message: "删除成功！",
              type: "success",
            });
            this.load();
            this.flushAside();
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
    handleEdit(row) {
      //更新前清空数据
      if (this.$refs.catalogueForm) {
        this.catalogueForm = {};
        this.$refs.catalogueForm.resetFields();
      }
      if (this.$refs.menuForm) {
        this.menuForm = {};
        this.$refs.menuForm.resetFields();
      }
      this.title = "修改";
      this.dialogVisible = true;
      //判断是目录还是菜单
      const type = row.type;
      if (type == 0) {
        //切换到目录
        this.activeName = "catalogue";
        //封装数据
        this.catalogueForm = JSON.parse(JSON.stringify(row));
      } else {
        //切换到菜单
        this.activeName = "menu";
        //封装数据
        this.menuForm = JSON.parse(JSON.stringify(row));
      }
    },
    editActive(row) {
      this.update(row);
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
.kd {
  width: 100%;
}
</style>