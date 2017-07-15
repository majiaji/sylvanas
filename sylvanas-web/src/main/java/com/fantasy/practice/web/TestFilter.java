package com.fantasy.sylvanas.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by jiaji on 2017/6/21.
 */
public class TestFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(TestFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.error("test filter befroe do");
        Boolean haha = null;
        logger.error("haha:{}", haha);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
