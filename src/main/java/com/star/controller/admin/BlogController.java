package com.star.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.star.domain.Blog;
import com.star.domain.Type;
import com.star.domain.User;
import com.star.mapper.CommentMapper;
import com.star.queryvo.BlogQuery;
import com.star.queryvo.SearchBlog;
import com.star.queryvo.ShowBlog;
import com.star.service.impl.BlogService;
import com.star.service.impl.CommentService;
import com.star.service.impl.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/blog/admin")
public class BlogController {

    @Autowired
    TypeService typeService;

    @Autowired
    BlogService blogService;

    @Autowired
    CommentService commentService;

    //跳转博客新增页面
    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("types",typeService.getAllType());
        model.addAttribute("blog", new Blog());
        return "admin/blogs-input";
    }

    //博客新增
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        //新增的时候需要传递blog对象，blog对象需要有user
        blog.setUser((User) session.getAttribute("user"));
        //设置blog的type
        blog.setType(typeService.getType(blog.getType().getId()));
        //设置blog中typeId属性
        blog.setTypeId(blog.getType().getId());
        //设置用户id
        blog.setUserId(blog.getUser().getId());

        int b = blogService.saveBlog(blog);
        if(b == 0){
            attributes.addFlashAttribute("message", "新增失败");
        }else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/blog/admin/blogs";
    }

    //博客列表
    @RequestMapping("/blogs")
    public String blogs(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,HttpSession session){

        //按照排序字段 倒序 排序
        String orderBy = "update_time desc";
        PageHelper.startPage(pageNum,10,orderBy);
        User user = (User) session.getAttribute("user");
        List<BlogQuery> list = null;
        if (user.getType() == 1){
            list = blogService.getAllBlogSuper();
        }else {
            list = blogService.getAllBlog(user.getId());
        }
        PageInfo<BlogQuery> pageInfo = new PageInfo<BlogQuery>(list);
        model.addAttribute("types",typeService.getAllType());
        model.addAttribute("pageInfo",pageInfo);
        return "admin/blogs";
    }

    //删除博客
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        commentService.deleteCommentByBlogId(id);
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/blog/admin/blogs";
    }

    //跳转编辑修改文章
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        ShowBlog blogById = blogService.getBlogById(id);
        List<Type> allType = typeService.getAllType();
        model.addAttribute("blog", blogById);
        model.addAttribute("types", allType);
        return "admin/blogs-input";
    }

    //编辑修改文章
    @PostMapping("/blogs/{id}")
    public String editPost(ShowBlog showBlog, RedirectAttributes attributes) {
        int b = blogService.updateBlog(showBlog);
        if(b == 0){
            attributes.addFlashAttribute("message", "修改失败");
        }else {
            attributes.addFlashAttribute("message", "修改成功");
        }
        return "redirect:/blog/admin/blogs";
    }

    //搜索博客管理列表
    @PostMapping("/blogs/search")
    public String search(SearchBlog searchBlog, Model model,
                         @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,HttpSession session) {
        User user = (User) session.getAttribute("user");
        searchBlog.setUserType(user.getType());
        searchBlog.setUserId(user.getId());
        List<BlogQuery> blogBySearch = blogService.getBlogBySearch(searchBlog);
        System.out.println(blogService.getBlogBySearch(searchBlog));
        PageHelper.startPage(pageNum, 10);
        PageInfo<BlogQuery> pageInfo = new PageInfo<>(blogBySearch);
        model.addAttribute("pageInfo", pageInfo);
        return "admin/blogs :: blogList";
    }
}
