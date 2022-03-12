import {
  createStore
} from 'vuex'

export default createStore({
  state: {
    //赋值
    isCollapse: false,
    Asidemenu: []
  },
  mutations: {
    setCollapse(state) {
      state.isCollapse = !state.isCollapse
    },
    setMenu(state, value) {
      state.Asidemenu = value;
    }
  },
  actions: {},
  modules: {}
})