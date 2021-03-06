package com.gdou.teaching.mbg.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExperimentAnswerExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ExperimentAnswerExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andExperimentAnswerIdIsNull() {
            addCriterion("experiment_answer_id is null");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerIdIsNotNull() {
            addCriterion("experiment_answer_id is not null");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerIdEqualTo(Integer value) {
            addCriterion("experiment_answer_id =", value, "experimentAnswerId");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerIdNotEqualTo(Integer value) {
            addCriterion("experiment_answer_id <>", value, "experimentAnswerId");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerIdGreaterThan(Integer value) {
            addCriterion("experiment_answer_id >", value, "experimentAnswerId");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("experiment_answer_id >=", value, "experimentAnswerId");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerIdLessThan(Integer value) {
            addCriterion("experiment_answer_id <", value, "experimentAnswerId");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerIdLessThanOrEqualTo(Integer value) {
            addCriterion("experiment_answer_id <=", value, "experimentAnswerId");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerIdIn(List<Integer> values) {
            addCriterion("experiment_answer_id in", values, "experimentAnswerId");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerIdNotIn(List<Integer> values) {
            addCriterion("experiment_answer_id not in", values, "experimentAnswerId");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerIdBetween(Integer value1, Integer value2) {
            addCriterion("experiment_answer_id between", value1, value2, "experimentAnswerId");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerIdNotBetween(Integer value1, Integer value2) {
            addCriterion("experiment_answer_id not between", value1, value2, "experimentAnswerId");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerStatusIsNull() {
            addCriterion("experiment_answer_status is null");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerStatusIsNotNull() {
            addCriterion("experiment_answer_status is not null");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerStatusEqualTo(Byte value) {
            addCriterion("experiment_answer_status =", value, "experimentAnswerStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerStatusNotEqualTo(Byte value) {
            addCriterion("experiment_answer_status <>", value, "experimentAnswerStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerStatusGreaterThan(Byte value) {
            addCriterion("experiment_answer_status >", value, "experimentAnswerStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("experiment_answer_status >=", value, "experimentAnswerStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerStatusLessThan(Byte value) {
            addCriterion("experiment_answer_status <", value, "experimentAnswerStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerStatusLessThanOrEqualTo(Byte value) {
            addCriterion("experiment_answer_status <=", value, "experimentAnswerStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerStatusIn(List<Byte> values) {
            addCriterion("experiment_answer_status in", values, "experimentAnswerStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerStatusNotIn(List<Byte> values) {
            addCriterion("experiment_answer_status not in", values, "experimentAnswerStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerStatusBetween(Byte value1, Byte value2) {
            addCriterion("experiment_answer_status between", value1, value2, "experimentAnswerStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentAnswerStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("experiment_answer_status not between", value1, value2, "experimentAnswerStatus");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private final String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private final String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}