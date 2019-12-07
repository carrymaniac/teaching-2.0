package com.gdou.teaching.mbg.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserReExperimentExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserReExperimentExample() {
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

        public Criteria andUserExperimentIdIsNull() {
            addCriterion("user_experiment_id is null");
            return (Criteria) this;
        }

        public Criteria andUserExperimentIdIsNotNull() {
            addCriterion("user_experiment_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserExperimentIdEqualTo(Integer value) {
            addCriterion("user_experiment_id =", value, "userExperimentId");
            return (Criteria) this;
        }

        public Criteria andUserExperimentIdNotEqualTo(Integer value) {
            addCriterion("user_experiment_id <>", value, "userExperimentId");
            return (Criteria) this;
        }

        public Criteria andUserExperimentIdGreaterThan(Integer value) {
            addCriterion("user_experiment_id >", value, "userExperimentId");
            return (Criteria) this;
        }

        public Criteria andUserExperimentIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_experiment_id >=", value, "userExperimentId");
            return (Criteria) this;
        }

        public Criteria andUserExperimentIdLessThan(Integer value) {
            addCriterion("user_experiment_id <", value, "userExperimentId");
            return (Criteria) this;
        }

        public Criteria andUserExperimentIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_experiment_id <=", value, "userExperimentId");
            return (Criteria) this;
        }

        public Criteria andUserExperimentIdIn(List<Integer> values) {
            addCriterion("user_experiment_id in", values, "userExperimentId");
            return (Criteria) this;
        }

        public Criteria andUserExperimentIdNotIn(List<Integer> values) {
            addCriterion("user_experiment_id not in", values, "userExperimentId");
            return (Criteria) this;
        }

        public Criteria andUserExperimentIdBetween(Integer value1, Integer value2) {
            addCriterion("user_experiment_id between", value1, value2, "userExperimentId");
            return (Criteria) this;
        }

        public Criteria andUserExperimentIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_experiment_id not between", value1, value2, "userExperimentId");
            return (Criteria) this;
        }

        public Criteria andExperimentIdIsNull() {
            addCriterion("experiment_id is null");
            return (Criteria) this;
        }

        public Criteria andExperimentIdIsNotNull() {
            addCriterion("experiment_id is not null");
            return (Criteria) this;
        }

        public Criteria andExperimentIdEqualTo(Integer value) {
            addCriterion("experiment_id =", value, "experimentId");
            return (Criteria) this;
        }

        public Criteria andExperimentIdNotEqualTo(Integer value) {
            addCriterion("experiment_id <>", value, "experimentId");
            return (Criteria) this;
        }

        public Criteria andExperimentIdGreaterThan(Integer value) {
            addCriterion("experiment_id >", value, "experimentId");
            return (Criteria) this;
        }

        public Criteria andExperimentIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("experiment_id >=", value, "experimentId");
            return (Criteria) this;
        }

        public Criteria andExperimentIdLessThan(Integer value) {
            addCriterion("experiment_id <", value, "experimentId");
            return (Criteria) this;
        }

        public Criteria andExperimentIdLessThanOrEqualTo(Integer value) {
            addCriterion("experiment_id <=", value, "experimentId");
            return (Criteria) this;
        }

        public Criteria andExperimentIdIn(List<Integer> values) {
            addCriterion("experiment_id in", values, "experimentId");
            return (Criteria) this;
        }

        public Criteria andExperimentIdNotIn(List<Integer> values) {
            addCriterion("experiment_id not in", values, "experimentId");
            return (Criteria) this;
        }

        public Criteria andExperimentIdBetween(Integer value1, Integer value2) {
            addCriterion("experiment_id between", value1, value2, "experimentId");
            return (Criteria) this;
        }

        public Criteria andExperimentIdNotBetween(Integer value1, Integer value2) {
            addCriterion("experiment_id not between", value1, value2, "experimentId");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Integer value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Integer value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Integer value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Integer value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Integer> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Integer> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Integer value1, Integer value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andClassIdIsNull() {
            addCriterion("class_id is null");
            return (Criteria) this;
        }

        public Criteria andClassIdIsNotNull() {
            addCriterion("class_id is not null");
            return (Criteria) this;
        }

        public Criteria andClassIdEqualTo(Integer value) {
            addCriterion("class_id =", value, "classId");
            return (Criteria) this;
        }

        public Criteria andClassIdNotEqualTo(Integer value) {
            addCriterion("class_id <>", value, "classId");
            return (Criteria) this;
        }

        public Criteria andClassIdGreaterThan(Integer value) {
            addCriterion("class_id >", value, "classId");
            return (Criteria) this;
        }

        public Criteria andClassIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("class_id >=", value, "classId");
            return (Criteria) this;
        }

        public Criteria andClassIdLessThan(Integer value) {
            addCriterion("class_id <", value, "classId");
            return (Criteria) this;
        }

        public Criteria andClassIdLessThanOrEqualTo(Integer value) {
            addCriterion("class_id <=", value, "classId");
            return (Criteria) this;
        }

        public Criteria andClassIdIn(List<Integer> values) {
            addCriterion("class_id in", values, "classId");
            return (Criteria) this;
        }

        public Criteria andClassIdNotIn(List<Integer> values) {
            addCriterion("class_id not in", values, "classId");
            return (Criteria) this;
        }

        public Criteria andClassIdBetween(Integer value1, Integer value2) {
            addCriterion("class_id between", value1, value2, "classId");
            return (Criteria) this;
        }

        public Criteria andClassIdNotBetween(Integer value1, Integer value2) {
            addCriterion("class_id not between", value1, value2, "classId");
            return (Criteria) this;
        }

        public Criteria andExperimentNameIsNull() {
            addCriterion("experiment_name is null");
            return (Criteria) this;
        }

        public Criteria andExperimentNameIsNotNull() {
            addCriterion("experiment_name is not null");
            return (Criteria) this;
        }

        public Criteria andExperimentNameEqualTo(String value) {
            addCriterion("experiment_name =", value, "experimentName");
            return (Criteria) this;
        }

        public Criteria andExperimentNameNotEqualTo(String value) {
            addCriterion("experiment_name <>", value, "experimentName");
            return (Criteria) this;
        }

        public Criteria andExperimentNameGreaterThan(String value) {
            addCriterion("experiment_name >", value, "experimentName");
            return (Criteria) this;
        }

        public Criteria andExperimentNameGreaterThanOrEqualTo(String value) {
            addCriterion("experiment_name >=", value, "experimentName");
            return (Criteria) this;
        }

        public Criteria andExperimentNameLessThan(String value) {
            addCriterion("experiment_name <", value, "experimentName");
            return (Criteria) this;
        }

        public Criteria andExperimentNameLessThanOrEqualTo(String value) {
            addCriterion("experiment_name <=", value, "experimentName");
            return (Criteria) this;
        }

        public Criteria andExperimentNameLike(String value) {
            addCriterion("experiment_name like", value, "experimentName");
            return (Criteria) this;
        }

        public Criteria andExperimentNameNotLike(String value) {
            addCriterion("experiment_name not like", value, "experimentName");
            return (Criteria) this;
        }

        public Criteria andExperimentNameIn(List<String> values) {
            addCriterion("experiment_name in", values, "experimentName");
            return (Criteria) this;
        }

        public Criteria andExperimentNameNotIn(List<String> values) {
            addCriterion("experiment_name not in", values, "experimentName");
            return (Criteria) this;
        }

        public Criteria andExperimentNameBetween(String value1, String value2) {
            addCriterion("experiment_name between", value1, value2, "experimentName");
            return (Criteria) this;
        }

        public Criteria andExperimentNameNotBetween(String value1, String value2) {
            addCriterion("experiment_name not between", value1, value2, "experimentName");
            return (Criteria) this;
        }

        public Criteria andExperimentAchievementIsNull() {
            addCriterion("experiment_achievement is null");
            return (Criteria) this;
        }

        public Criteria andExperimentAchievementIsNotNull() {
            addCriterion("experiment_achievement is not null");
            return (Criteria) this;
        }

        public Criteria andExperimentAchievementEqualTo(Double value) {
            addCriterion("experiment_achievement =", value, "experimentAchievement");
            return (Criteria) this;
        }

        public Criteria andExperimentAchievementNotEqualTo(Double value) {
            addCriterion("experiment_achievement <>", value, "experimentAchievement");
            return (Criteria) this;
        }

        public Criteria andExperimentAchievementGreaterThan(Double value) {
            addCriterion("experiment_achievement >", value, "experimentAchievement");
            return (Criteria) this;
        }

        public Criteria andExperimentAchievementGreaterThanOrEqualTo(Double value) {
            addCriterion("experiment_achievement >=", value, "experimentAchievement");
            return (Criteria) this;
        }

        public Criteria andExperimentAchievementLessThan(Double value) {
            addCriterion("experiment_achievement <", value, "experimentAchievement");
            return (Criteria) this;
        }

        public Criteria andExperimentAchievementLessThanOrEqualTo(Double value) {
            addCriterion("experiment_achievement <=", value, "experimentAchievement");
            return (Criteria) this;
        }

        public Criteria andExperimentAchievementIn(List<Double> values) {
            addCriterion("experiment_achievement in", values, "experimentAchievement");
            return (Criteria) this;
        }

        public Criteria andExperimentAchievementNotIn(List<Double> values) {
            addCriterion("experiment_achievement not in", values, "experimentAchievement");
            return (Criteria) this;
        }

        public Criteria andExperimentAchievementBetween(Double value1, Double value2) {
            addCriterion("experiment_achievement between", value1, value2, "experimentAchievement");
            return (Criteria) this;
        }

        public Criteria andExperimentAchievementNotBetween(Double value1, Double value2) {
            addCriterion("experiment_achievement not between", value1, value2, "experimentAchievement");
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

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andHaveCheckAnswerIsNull() {
            addCriterion("have_check_answer is null");
            return (Criteria) this;
        }

        public Criteria andHaveCheckAnswerIsNotNull() {
            addCriterion("have_check_answer is not null");
            return (Criteria) this;
        }

        public Criteria andHaveCheckAnswerEqualTo(Boolean value) {
            addCriterion("have_check_answer =", value, "haveCheckAnswer");
            return (Criteria) this;
        }

        public Criteria andHaveCheckAnswerNotEqualTo(Boolean value) {
            addCriterion("have_check_answer <>", value, "haveCheckAnswer");
            return (Criteria) this;
        }

        public Criteria andHaveCheckAnswerGreaterThan(Boolean value) {
            addCriterion("have_check_answer >", value, "haveCheckAnswer");
            return (Criteria) this;
        }

        public Criteria andHaveCheckAnswerGreaterThanOrEqualTo(Boolean value) {
            addCriterion("have_check_answer >=", value, "haveCheckAnswer");
            return (Criteria) this;
        }

        public Criteria andHaveCheckAnswerLessThan(Boolean value) {
            addCriterion("have_check_answer <", value, "haveCheckAnswer");
            return (Criteria) this;
        }

        public Criteria andHaveCheckAnswerLessThanOrEqualTo(Boolean value) {
            addCriterion("have_check_answer <=", value, "haveCheckAnswer");
            return (Criteria) this;
        }

        public Criteria andHaveCheckAnswerIn(List<Boolean> values) {
            addCriterion("have_check_answer in", values, "haveCheckAnswer");
            return (Criteria) this;
        }

        public Criteria andHaveCheckAnswerNotIn(List<Boolean> values) {
            addCriterion("have_check_answer not in", values, "haveCheckAnswer");
            return (Criteria) this;
        }

        public Criteria andHaveCheckAnswerBetween(Boolean value1, Boolean value2) {
            addCriterion("have_check_answer between", value1, value2, "haveCheckAnswer");
            return (Criteria) this;
        }

        public Criteria andHaveCheckAnswerNotBetween(Boolean value1, Boolean value2) {
            addCriterion("have_check_answer not between", value1, value2, "haveCheckAnswer");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

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