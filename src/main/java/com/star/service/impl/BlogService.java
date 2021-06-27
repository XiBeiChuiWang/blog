package com.star.service.impl;

import com.star.domain.Blog;
import com.star.mapper.BlogMapper;
import com.star.queryvo.*;
import com.star.redis.BlogRedis;
import com.star.service.IBlogService;
import com.star.util.MarkdownUtils;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BlogService implements IBlogService {

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    BlogRedis blogRedis;

    @Override
    public int saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);
        blog.setCommentCount(0);
        return blogMapper.saveBlog(blog);
    }

    @Override
    public List<BlogQuery> getAllBlog(long id) {
        return blogMapper.getAllBlogQuery(id);
    }

    @Override
    public List<BlogQuery> getAllBlogSuper() {
        return blogMapper.getAllBlogQuerySuper();
    }

    @Override
    public void deleteBlog(Long id) {
        blogMapper.deleteBlog(id);
    }

    //查询编辑修改的文章
    @Override
    public ShowBlog getBlogById(Long id) {
        return blogMapper.getBlogById(id);
    }

    //编辑修改文章
    @Override
    public int updateBlog(ShowBlog showBlog) {
        showBlog.setUpdateTime(new Date());
        return blogMapper.updateBlog(showBlog);
    }

    //搜索博客管理列表
    @Override
    public List<BlogQuery> getBlogBySearch(SearchBlog searchBlog) {
        return blogMapper.searchByTitleAndType(searchBlog);
    }


    //查询首页最新博客列表信息
    @Override
    public List<FirstPageBlog> getAllFirstPageBlog() {
        return blogMapper.getFirstPageBlog();
//        return blogRedis.getAllFirstPageBlog();
    }

    //查询首页最新推荐信息
    @Override
    public List<RecommendBlog> getRecommendedBlog() {
        List<RecommendBlog> allRecommendBlog = blogMapper.getAllRecommendBlog();
        return allRecommendBlog;
    }

    //搜索博客列表
    @Override
    public List<FirstPageBlog> getSearchBlog(String query) {
        return blogMapper.getSearchBlog(query);
    }

    //统计博客总数
    @Override
    public Integer getBlogTotal() {
        return blogMapper.getBlogTotal();
    }

    //统计访问总数
    @Override
    public Integer getBlogViewTotal() {
        return blogMapper.getBlogViewTotal();
    }

    //统计评论总数
    @Override
    public Integer getBlogCommentTotal() {
        return blogMapper.getBlogCommentTotal();
    }

    //统计留言总数
    @Override
    public Integer getBlogMessageTotal() {
        return blogMapper.getBlogMessageTotal();
    }

    @Override
    public DetailedBlog getDetailedBlog(Long id) throws NotFoundException {
        DetailedBlog detailedBlog = blogMapper.getDetailedBlog(id);
//        DetailedBlog detailedBlog = blogRedis.getDetailedBlog(id);
        if (detailedBlog == null) {
            throw new NotFoundException("该博客不存在");
        }
        String content = detailedBlog.getContent();
        detailedBlog.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        //文章访问数量自增
        blogMapper.updateViews(id);
        //文章评论数量更新
        blogMapper.getCommentCountById(id);
        return detailedBlog;
    }

    //分类页面查询
    @Override
    public List<FirstPageBlog> getByTypeId(Long typeId) {
        return blogMapper.getByTypeId(typeId);
    }
}
