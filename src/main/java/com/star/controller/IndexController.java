package com.star.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.star.queryvo.DetailedBlog;
import com.star.queryvo.FirstPageBlog;
import com.star.queryvo.RecommendBlog;
import com.star.service.impl.BlogService;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Controller
@RequestMapping("blog")
public class IndexController {

    @Autowired
    private BlogService blogService;

    //分页查询博客列表
    @GetMapping("/index")
    public String index(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, RedirectAttributes attributes){
        PageHelper.startPage(pageNum,6);
        List<FirstPageBlog> allFirstPageBlog = blogService.getAllFirstPageBlog();
        List<RecommendBlog> recommendedBlog = blogService.getRecommendedBlog();

        PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(allFirstPageBlog);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("recommendedBlogs", recommendedBlog);
        return "index";
    }

    //搜索博客
    @PostMapping("/search")
    public String search(Model model,
                         @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
                         @RequestParam String query) {
        PageHelper.startPage(pageNum, 1000);
        List<FirstPageBlog> searchBlog = blogService.getSearchBlog(query);

        PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(searchBlog);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("query", query);
        return "search";
    }

    //博客信息统计
    @GetMapping("/footer/blogmessage")
    public String blogMessage(Model model){
        int blogTotal = blogService.getBlogTotal();
        int blogViewTotal = blogService.getBlogViewTotal();
        int blogCommentTotal = blogService.getBlogCommentTotal();
        int blogMessageTotal = blogService.getBlogMessageTotal();

        model.addAttribute("blogTotal",blogTotal);
        model.addAttribute("blogViewTotal",blogViewTotal);
        model.addAttribute("blogCommentTotal",blogCommentTotal);
        model.addAttribute("blogMessageTotal",blogMessageTotal);
        return "index :: blogMessage";
    }


    //跳转博客详情页面
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) throws NotFoundException {
        DetailedBlog detailedBlog = blogService.getDetailedBlog(id);
        model.addAttribute("blog", detailedBlog);
        return "blog";
    }
}
