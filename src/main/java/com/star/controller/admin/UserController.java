package com.star.controller.admin;

import com.star.domain.User;
import com.star.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/blog/admin")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public String loginPage(){
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes){
        User user = userService.checkUser(username, password);
        if (user != null){
            user.setPassword(null);
            session.setAttribute("user",user);
            return "admin/index";
        }else {
            attributes.addFlashAttribute("message", "用户名和密码错误");
            return "redirect:/blog/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/blog/admin";
    }

    @GetMapping("/update")
    public String update(HttpSession session) {
        User user = (User) session.getAttribute("user");
        System.out.println(user);
        return "admin/update";
    }

    @PostMapping("/updateuser")
    public String update1(User user,HttpSession session,RedirectAttributes attributes) {
        user.setUpdateTime(new Date());
        userService.updateUser(user);
        session.removeAttribute("user");
        attributes.addFlashAttribute("message", "修改成功，请重新登录");
        return "redirect:/blog/admin";
    }
}
