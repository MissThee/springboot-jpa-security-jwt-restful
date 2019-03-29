package com.github.missthee.db.primary.tool;

import org.hibernate.SQLQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.transform.Transformers;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.*;

/**
 * 仅支持createNativeQuery创建的原生SQL语句返回的Query
 */
public class QueryTool {
    //查询返回List<Map>
    public static List<Map<String, Object>> list(Query query) {
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> result = query.getResultList();
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (Map<String, Object> row : result) {
            Set<String> columnNameSet = row.keySet();
            Map<String, Object> rowMap = new HashMap<>();
            for (String columnName : columnNameSet) {
                rowMap.put(transToLowerCamel(columnName), row.get(columnName));
            }
            dataList.add(rowMap);
        }
        return dataList;
    }

    //查询返回map
    public static Map map(Query query) {
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Map<String, Object> dataMap = new HashMap<>();
        Object resultObj;
        try {
            resultObj = query.getSingleResult();
        } catch (NoResultException noResultException) {
            return dataMap;
        }
        Map<String, Object> map = (Map<String, Object>) resultObj;
        Set<String> set = map.keySet();
        for (String columnName : set) {
            dataMap.put(transToLowerCamel(columnName), map.get(columnName));
        }
        return dataMap;
    }
//    此方法转map有误，不应这样使用
//    public static List openJpaQueryList(Query query) {
//        query.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//        List<Map<String, Object>> result = query.getResultList();
//        List<Map<String, Object>> dataList = new ArrayList<>();
//        for (Map<String, Object> row : result) {
//            Set<String> columnNameSet = row.keySet();
//            Map<String, Object> rowMap = new HashMap<>();
//            for (String columnName : columnNameSet) {
//                rowMap.put(transToLowerCamel(columnName), row.get(columnName));
//            }
//            dataList.add(rowMap);
//        }
//        return dataList;
//    }

    //下划线转小驼峰格式
    private static String transToLowerCamel(String key) {
        StringBuilder keySB = new StringBuilder();
        boolean needRetainUpperCase = true;
        for (int i = 0; i < key.length(); i++) {
            char chr = key.charAt(i);
            if (chr == '_') {
                needRetainUpperCase = false;
            } else {
                //大写转小写
                if (needRetainUpperCase && Character.isUpperCase(chr)) {
                    chr = (char) (chr + 32);
                }
                keySB.append(chr);
                needRetainUpperCase = true;
            }
        }
        return keySB.toString();
    }
}
