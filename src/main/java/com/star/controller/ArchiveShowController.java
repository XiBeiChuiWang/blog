package com.star.controller;

import com.star.domain.User;
import com.star.queryvo.BlogQuery;
import com.star.service.impl.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("blog")
public class ArchiveShowController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archive(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        List<BlogQuery> blogs = blogService.getAllBlogSuper();
        model.addAttribute("blogs", blogs);
        return "archives";
    }

}
