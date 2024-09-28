package com.itheima.mp.service;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.po.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IUserServiceTest {
    @Autowired
    private IUserService userService;

    @Test
    void testSaveUser(){
        User user = new User();
        user.setId(5L);
        user.setUsername("LiLei");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
//        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setInfo(UserInfo.of(24,"英文老师","female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userService.save(user);
    }

    private User buildUser(int i){
        User user = new User();
        user.setUsername("user_"+i);
        user.setPassword("123");
        user.setPhone("1868899000L"+i);
        user.setBalance(2000);
//        user.setInfo("{\"age\":24,\"intro\":\"英文老师\", \"gender\":\"female\"}");
        user.setInfo(UserInfo.of(24,"英文老师","female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(user.getCreateTime());
        return user;
    }

    @Test
    void testSaveOneByOne() {
        long b = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            userService.save(buildUser(i));
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时："+(e-b)+"ms");
    }

    @Test
    void testSaveBatch(){
        ArrayList<User> list = new ArrayList<>(1000);
        long b = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            list.add(buildUser(i));
            if(i%1000 == 0){
                userService.saveBatch(list);
                list.clear();
            }
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时："+(e-b)+"ms");
    }

    @Test
    void testPageQuery(){
        int pageNo = 1, pageSize = 10;
        //1.准备分页条件
        //1.1分页条件
        Page<User> page = Page.of(pageNo, pageSize);
        //1.2排序条件
        page.addOrder(new OrderItem("balance",true));
        page.addOrder(new OrderItem("id",true));
        //2.分页查询
        Page<User> p = userService.page(page);
        //3.解析
        long total = p.getTotal();
        System.out.println("total:"+total);
        long pages = p.getPages();
        System.out.println("pages:"+pages);
        p.getRecords().forEach(System.out::println);
    }

}