package com.juxinma.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 字符串工具类
 * @Author 黄名富
 * @Date 2022/7/25 00:23
 * @Version 1.0
 **/
public class StringUtils {

    public static Map<String,String> strToMap(String mapStr) {
        if(!mapStr.startsWith("{") || !mapStr.endsWith("}")) {
            throw new RuntimeException("格式错误");
        }
        mapStr = mapStr.substring(1,mapStr.length() - 1);
        String[] strings = mapStr.split(",");
        Map<String, String> map = new HashMap<>();
        for(String str : strings) {
            String[] groups = str.split("=");
            if(groups.length != 2) {
                throw new RuntimeException("格式错误");
            }
            map.put(groups[0].trim(),groups[1].trim());
        }
        return map;
    }

}
