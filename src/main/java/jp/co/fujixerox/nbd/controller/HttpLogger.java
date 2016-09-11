package jp.co.fujixerox.nbd.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

public class HttpLogger extends HandlerInterceptorAdapter {
    private static final Logger logger = LogManager.getLogger(HttpLogger.class);


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StringBuffer log = new StringBuffer();

        log.append("\n***HTTP REQUEST*************************************\n");
        // method and endpoint
        log.append(request.getMethod() + ": " + request.getServletPath() + "\n");
        // qeuery params
        log.append(request.getQueryString() + "\n");

        // header
        log.append("[header]\n");
        Enumeration headerKeys = request.getHeaderNames();
        while(headerKeys.hasMoreElements()) {
            String headerKey = (String) headerKeys.nextElement();
            String headerValue = request.getHeader(headerKey);

            log.append(headerKey);
            log.append("=");
            log.append(headerValue + "\n");
        }

        // parameter
        log.append("[params]\n");
        Map<String, String[]> headers = request.getParameterMap();
        for(Map.Entry<String, String[]> header : headers.entrySet()) {
            log.append(header.getKey()+ "=" + header.getValue().toString() +"\n");
        }

        logger.info(log.toString());
        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {
        StringBuffer log = new StringBuffer();
        log.append("\n***HTTP RESPONSE*************************************\n");
        // resource method and class
        log.append(request.getMethod() + "\n");
        // method and endpoint
        // status code
        log.append(response.getStatus() + "\n");

        // response header
        log.append("[response header]\n");
        Collection<String> headerKeys = response.getHeaderNames();
        for(String headerKey: headerKeys){
            String headerValue = request.getHeader(headerKey);

            log.append(headerKey);
            log.append("=");
            log.append(headerValue + "\n");
        }

        logger.info(log.toString());
    }
}