package com.star;


import com.star.controller.AA;
import com.star.mapper.BlogMapper;
import com.star.util.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootTest
class DemoApplicationTests {

    @Test
    public void a(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("beans.xml");
        AA a = (AA)classPathXmlApplicationContext.getBean("a");
        System.out.println(a);
    }




}
