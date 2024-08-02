package com.book.config;

import com.book.controller.IndexController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 登录拦截器
 * 检查会话中，用户是否已经登录，没有登录，则返回错误。
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private VMConfig config;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("key");
        if (user != null) {
            String key = user.toString();
            user = request.getSession().getAttribute(key);
        }
        // 当前访问路径：
        String uri = request.getRequestURI();
        System.out.println("请求：" + uri);
        // 未登录状态下允许访问的路径
        String[] uris = config.loginInterceptorIgnoreUri;
        if (user == null) {
            for (String s : uris) {
                if (Objects.equals(uri, s) || uri.matches(s)) {
                    // 允许访问。 路径不存在，依旧404
                    return true;
                }
            }
            // 返回 身份未验证错误，错误码：401
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "拒绝访问：你还未登录！");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}