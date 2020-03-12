package com.jsc.zao.util;

import com.jsc.zao.bean.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 获取当前正在登录的用户
 */
public class Util {
    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
