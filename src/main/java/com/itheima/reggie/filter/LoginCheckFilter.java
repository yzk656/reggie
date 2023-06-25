package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: LoginCheckFilter
 * @Description: 检查用户是否登录
 * @Author: 杨振坤
 * @date: 2023/5/9 1:17
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    /*路径匹配器,支持通配符写法*/
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        /*1. 获取本次请求uri*/
        String requestURI = request.getRequestURI();

        /*定义不需要处理的请求路径*/
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };

        /*2. 判断本次请求是否需要处理*/
        boolean check = check(urls, requestURI);

        /*3. 如果不需要处理，直接放行*/
        if (check) {
            filterChain.doFilter(request, response);
            return;
        }

        /*4-1. 判断登录状态，如果已经登录，则直接放行*/
        if (request.getSession().getAttribute("employee") != null) {
/*            long id=Thread.currentThread().getId();
            log.info("线程ID：{}",id);*/

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }

        /*4-2. 判断移动端登录状态，如果已经登录，则直接放行*/
        if (request.getSession().getAttribute("user") != null) {
/*            long id=Thread.currentThread().getId();
            log.info("线程ID：{}",id);*/

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);
            return;
        }

        /*5. 如果未登录返回到登录界面,通过输出流方式向客户端响应数据*/
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }

        return false;
    }
}
