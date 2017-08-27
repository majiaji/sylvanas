package com.fantasy.sylvanas.client.domain;

import java.util.Map;

/**
 * Created by jiaji on 2017/8/24.
 */
public class CountRule {
    /**
     * 统计字段(与groupBy互斥)
     */
    private String field;
    /**
     * 统计维度(group by)
     */
    private String groupBy;
    /**
     * 统计条件(可选)
     */
    private Map<String, String> conditionMap;
    /**
     * 聚合规则（必选）
     */
    private String rule;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Map<String, String> getConditionMap() {
        return conditionMap;
    }

    public void setConditionMap(Map<String, String> conditionMap) {
        this.conditionMap = conditionMap;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public static class Rule {
        public static final String MAX = "MAX";
        public static final String MIN = "MIN";
        public static final String SUM = "SUM";
        public static final String AVG = "AVG";
    }
}
