package com.tanmu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tanmu.entity.Menu;
import com.tanmu.entity.User;
import com.tanmu.mapper.MenuMapper;
import com.tanmu.mapper.RankMapper;
import com.tanmu.mapper.UserMapper;
import com.tanmu.vo.MenuDataVo;
import com.tanmu.vo.MenuVo;
import com.tanmu.vo.RankVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
class SpringbootExamAdminApplicationTests {
   @Autowired
   private UserMapper userMapper;
   @Autowired
   private RankMapper rankMapper;

//   @Test
//       void testSelect() {
//      User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, "19829216685").eq(User::getDeleteYn,1));
//      System.out.println("user = " + user);
//    }
//    @Test
//    //测试查询所有
//    void testSelect() {
//        List<User> list = userMapper.selectList(null);
//        for (User user : list) {
//            System.out.println("user = " + user);
//        }
//    }
//    @Test
//    void testAutoFill(){
//        //测试时间自动填充
//        User user = new User();
//        user.setName("王6");
//        user.setAge(18);
//        user.setNickName("三三");
//        user.setPassword("123456");
//        user.setAddress("测试地址");
//        user.setPhone("13325368978");
//        user.setSex(1);
//        user.setRole(1);
//        user.setDeleteYn(0);
//        user.setNumber(0);
//        user.setUseYn(0);
//        userMapper.insert(user);
//    }
////测试删除  0---->1
//    @Test
//    void testDelete(){
//        int i = userMapper.deleteById(1);
//        System.out.println("i = " + i);
//    }
//    //根据条件定向删除
//    @Test
//    void testDeleteByMap(){
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("phone","13325368978");
//        map.put("name","王6");
//        userMapper.deleteByMap(map);
//    }
//    @Test
//    void getCount(){
//        //查询用户条数 自动过滤掉删除用户
//        Integer integer = userMapper.selectCount(null);
//        System.out.println("integer = " + integer);
//    }
//    @Test
//    void testDemo(){
//        //测试 queryWrapper
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        //构造条件
//        queryWrapper.select("id","name","password")
//                .eq("age",26)
//                .like("name","檀");
//
//        //执行查询
//        List<User> users = userMapper.selectList(queryWrapper);
//        for (User user : users) {
//            System.out.println("user = " + user);
//        }
//    }
//    @Test
//    //测试排名
//    void getRank(){
//        List<RankVo> rankVos = rankMapper.GetRank();
//        for (RankVo rankVo : rankVos) {
//            System.out.println("rankVo = " + rankVo);
//        }
//    }
//    @Autowired
//     private MenuMapper menuMapper;
//    //测试菜单数据
//    @Test
//    void getMenu(){
////        List<MenuDataVo> menu = menuMapper.getMenu(2);
////        for (MenuDataVo menuDataVo : menu) {
////            System.out.println(menuDataVo);
////        }
//        List<Menu> menus = menuMapper.selectList(null);
//        for (Menu menu : menus) {
//            System.out.println("menu = " + menu);
//        }
//    }
}
