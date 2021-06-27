package com.star.redis;

import com.star.mapper.BlogMapper;
import com.star.queryvo.DetailedBlog;
import com.star.queryvo.FirstPageBlog;
import com.star.queryvo.RecommendBlog;
import com.star.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlogRedis {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    BlogMapper blogMapper;

    public DetailedBlog getDetailedBlog(long id){
        String s = "DetailedBlog";
        Object o = redisUtils.hget(s,String.valueOf(id));
        if (o == null){
            DetailedBlog detailedBlog = blogMapper.getDetailedBlog(id);
            redisUtils.hset(s,String.valueOf(id),detailedBlog);
            return detailedBlog;
        }else {
            return (DetailedBlog)o;
        }
    }

    public List<RecommendBlog> getRecommendedBlog() {
        List<RecommendBlog> allRecommendBlog = blogMapper.getAllRecommendBlog();
        return allRecommendBlog;
    }
}
