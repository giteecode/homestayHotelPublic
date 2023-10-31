

package com.homestay.homestay.utils;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class WebUtils {
    static final String[] ipHeaders = new String[]{"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP"};

    public WebUtils() {
    }


    public static Map<String, String> queryStringToMap(String queryString, String charset) {
        try {
            Map<String, String> map = new HashMap();
            String[] decode = URLDecoder.decode(queryString, charset).split("&");
            String[] var4 = decode;
            int var5 = decode.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                String keyValue = var4[var6];
                String[] kv = keyValue.split("[=]", 2);
                map.put(kv[0], kv.length > 1 ? kv[1] : "");
            }

            return map;
        } catch (UnsupportedEncodingException var9) {
            throw new UnsupportedOperationException(var9);
        }
    }

    public static HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception var1) {
            return null;
        }
    }

    public static Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap();
        Enumeration enumeration = request.getParameterNames();

        while (enumeration.hasMoreElements()) {
            String name = String.valueOf(enumeration.nextElement());
            parameters.put(name, request.getParameter(name));
        }

        return parameters;
    }

    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap();
        Enumeration enumeration = request.getHeaderNames();

        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String[] var1 = ipHeaders;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            String ipHeader = var1[var3];
            String ip = request.getHeader(ipHeader);
            if (!StringUtils.isEmpty(ip) && !ip.contains("unknown")) {
                return ip;
            }
        }

        return request.getRemoteAddr();
    }

    public static String getBasePath(HttpServletRequest request) {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        return basePath;
    }

    /**
     * 获取访问者IP
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用Request .getRemoteAddr()。
     *
     * @param request
     * @return
     */
    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        System.out.println(ip);
        if (StringUtils.hasLength(ip) && !"unknown".equalsIgnoreCase(ip)) {
            if (ip.contains("../") || ip.contains("..\\")) {
                return "";
            }
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasLength(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                ip = ip.substring(0, index);
            }
            if (ip.contains("../") || ip.contains("..\\")) {
                return "";
            }
            return ip;
        } else {
            ip = request.getRemoteAddr();
            if (ip.contains("../") || ip.contains("..\\")) {
                return "";
            }
            if (ip.equals("0:0:0:0:0:0:0:1")) {
                ip = "127.0.0.1";
            }
            return ip;
        }

    }
}
