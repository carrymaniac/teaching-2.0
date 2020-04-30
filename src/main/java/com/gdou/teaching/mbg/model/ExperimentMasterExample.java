package com.gdou.teaching.mbg.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExperimentMasterExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ExperimentMasterExample() {
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

        public Criteria andExperimentIntroIsNull() {
            addCriterion("experiment_intro is null");
            return (Criteria) this;
        }

        public Criteria andExperimentIntroIsNotNull() {
            addCriterion("experiment_intro is not null");
            return (Criteria) this;
        }

        public Criteria andExperimentIntroEqualTo(String value) {
            addCriterion("experiment_intro =", value, "experimentIntro");
            return (Criteria) this;
        }

        public Criteria andExperimentIntroNotEqualTo(String value) {
            addCriterion("experiment_intro <>", value, "experimentIntro");
            return (Criteria) this;
        }

        public Criteria andExperimentIntroGreaterThan(String value) {
            addCriterion("experiment_intro >", value, "experimentIntro");
            return (Criteria) this;
        }

        public Criteria andExperimentIntroGreaterThanOrEqualTo(String value) {
            addCriterion("experiment_intro >=", value, "experimentIntro");
            return (Criteria) this;
        }

        public Criteria andExperimentIntroLessThan(String value) {
            addCriterion("experiment_intro <", value, "experimentIntro");
            return (Criteria) this;
        }

        public Criteria andExperimentIntroLessThanOrEqualTo(String value) {
            addCriterion("experiment_intro <=", value, "experimentIntro");
            return (Criteria) this;
        }

        public Criteria andExperimentIntroLike(String value) {
            addCriterion("experiment_intro like", value, "experimentIntro");
            return (Criteria) this;
        }

        public Criteria andExperimentIntroNotLike(String value) {
            addCriterion("experiment_intro not like", value, "experimentIntro");
            return (Criteria) this;
        }

        public Criteria andExperimentIntroIn(List<String> values) {
            addCriterion("experiment_intro in", values, "experimentIntro");
            return (Criteria) this;
        }

        public Criteria andExperimentIntroNotIn(List<String> values) {
            addCriterion("experiment_intro not in", values, "experimentIntro");
            return (Criteria) this;
        }

        public Criteria andExperimentIntroBetween(String value1, String value2) {
            addCriterion("experiment_intro between", value1, value2, "experimentIntro");
            return (Criteria) this;
        }

        public Criteria andExperimentIntroNotBetween(String value1, String value2) {
            addCriterion("experiment_intro not between", value1, value2, "experimentIntro");
            return (Criteria) this;
        }

        public Criteria andExperimentDetailIdIsNull() {
            addCriterion("experiment_detail_id is null");
            return (Criteria) this;
        }

        public Criteria andExperimentDetailIdIsNotNull() {
            addCriterion("experiment_detail_id is not null");
            return (Criteria) this;
        }

        public Criteria andExperimentDetailIdEqualTo(Integer value) {
            addCriterion("experiment_detail_id =", value, "experimentDetailId");
            return (Criteria) this;
        }

        public Criteria andExperimentDetailIdNotEqualTo(Integer value) {
            addCriterion("experiment_detail_id <>", value, "experimentDetailId");
            return (Criteria) this;
        }

        public Criteria andExperimentDetailIdGreaterThan(Integer value) {
            addCriterion("experiment_detail_id >", value, "experimentDetailId");
            return (Criteria) this;
        }

        public Criteria andExperimentDetailIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("experiment_detail_id >=", value, "experimentDetailId");
            return (Criteria) this;
        }

        public Criteria andExperimentDetailIdLessThan(Integer value) {
            addCriterion("experiment_detail_id <", value, "experimentDetailId");
            return (Criteria) this;
        }

        public Criteria andExperimentDetailIdLessThanOrEqualTo(Integer value) {
            addCriterion("experiment_detail_id <=", value, "experimentDetailId");
            return (Criteria) this;
        }

        public Criteria andExperimentDetailIdIn(List<Integer> values) {
            addCriterion("experiment_detail_id in", values, "experimentDetailId");
            return (Criteria) this;
        }

        public Criteria andExperimentDetailIdNotIn(List<Integer> values) {
            addCriterion("experiment_detail_id not in", values, "experimentDetailId");
            return (Criteria) this;
        }

        public Criteria andExperimentDetailIdBetween(Integer value1, Integer value2) {
            addCriterion("experiment_detail_id between", value1, value2, "experimentDetailId");
            return (Criteria) this;
        }

        public Criteria andExperimentDetailIdNotBetween(Integer value1, Integer value2) {
            addCriterion("experiment_detail_id not between", value1, value2, "experimentDetailId");
            return (Criteria) this;
        }

        public Criteria andCourseIdIsNull() {
            addCriterion("course_id is null");
            return (Criteria) this;
        }

        public Criteria andCourseIdIsNotNull() {
            addCriterion("course_id is not null");
            return (Criteria) this;
        }

        public Criteria andCourseIdEqualTo(Integer value) {
            addCriterion("course_id =", value, "courseId");
            return (Criteria) this;
        }

        public Criteria andCourseIdNotEqualTo(Integer value) {
            addCriterion("course_id <>", value, "courseId");
            return (Criteria) this;
        }

        public Criteria andCourseIdGreaterThan(Integer value) {
            addCriterion("course_id >", value, "courseId");
            return (Criteria) this;
        }

        public Criteria andCourseIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("course_id >=", value, "courseId");
            return (Criteria) this;
        }

        public Criteria andCourseIdLessThan(Integer value) {
            addCriterion("course_id <", value, "courseId");
            return (Criteria) this;
        }

        public Criteria andCourseIdLessThanOrEqualTo(Integer value) {
            addCriterion("course_id <=", value, "courseId");
            return (Criteria) this;
        }

        public Criteria andCourseIdIn(List<Integer> values) {
            addCriterion("course_id in", values, "courseId");
            return (Criteria) this;
        }

        public Criteria andCourseIdNotIn(List<Integer> values) {
            addCriterion("course_id not in", values, "courseId");
            return (Criteria) this;
        }

        public Criteria andCourseIdBetween(Integer value1, Integer value2) {
            addCriterion("course_id between", value1, value2, "courseId");
            return (Criteria) this;
        }

        public Criteria andCourseIdNotBetween(Integer value1, Integer value2) {
            addCriterion("course_id not between", value1, value2, "courseId");
            return (Criteria) this;
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

        public Criteria andExperimentCommitNumIsNull() {
            addCriterion("experiment_commit_num is null");
            return (Criteria) this;
        }

        public Criteria andExperimentCommitNumIsNotNull() {
            addCriterion("experiment_commit_num is not null");
            return (Criteria) this;
        }

        public Criteria andExperimentCommitNumEqualTo(Integer value) {
            addCriterion("experiment_commit_num =", value, "experimentCommitNum");
            return (Criteria) this;
        }

        public Criteria andExperimentCommitNumNotEqualTo(Integer value) {
            addCriterion("experiment_commit_num <>", value, "experimentCommitNum");
            return (Criteria) this;
        }

        public Criteria andExperimentCommitNumGreaterThan(Integer value) {
            addCriterion("experiment_commit_num >", value, "experimentCommitNum");
            return (Criteria) this;
        }

        public Criteria andExperimentCommitNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("experiment_commit_num >=", value, "experimentCommitNum");
            return (Criteria) this;
        }

        public Criteria andExperimentCommitNumLessThan(Integer value) {
            addCriterion("experiment_commit_num <", value, "experimentCommitNum");
            return (Criteria) this;
        }

        public Criteria andExperimentCommitNumLessThanOrEqualTo(Integer value) {
            addCriterion("experiment_commit_num <=", value, "experimentCommitNum");
            return (Criteria) this;
        }

        public Criteria andExperimentCommitNumIn(List<Integer> values) {
            addCriterion("experiment_commit_num in", values, "experimentCommitNum");
            return (Criteria) this;
        }

        public Criteria andExperimentCommitNumNotIn(List<Integer> values) {
            addCriterion("experiment_commit_num not in", values, "experimentCommitNum");
            return (Criteria) this;
        }

        public Criteria andExperimentCommitNumBetween(Integer value1, Integer value2) {
            addCriterion("experiment_commit_num between", value1, value2, "experimentCommitNum");
            return (Criteria) this;
        }

        public Criteria andExperimentCommitNumNotBetween(Integer value1, Integer value2) {
            addCriterion("experiment_commit_num not between", value1, value2, "experimentCommitNum");
            return (Criteria) this;
        }

        public Criteria andExperimentParticipationNumIsNull() {
            addCriterion("experiment_participation_num is null");
            return (Criteria) this;
        }

        public Criteria andExperimentParticipationNumIsNotNull() {
            addCriterion("experiment_participation_num is not null");
            return (Criteria) this;
        }

        public Criteria andExperimentParticipationNumEqualTo(Integer value) {
            addCriterion("experiment_participation_num =", value, "experimentParticipationNum");
            return (Criteria) this;
        }

        public Criteria andExperimentParticipationNumNotEqualTo(Integer value) {
            addCriterion("experiment_participation_num <>", value, "experimentParticipationNum");
            return (Criteria) this;
        }

        public Criteria andExperimentParticipationNumGreaterThan(Integer value) {
            addCriterion("experiment_participation_num >", value, "experimentParticipationNum");
            return (Criteria) this;
        }

        public Criteria andExperimentParticipationNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("experiment_participation_num >=", value, "experimentParticipationNum");
            return (Criteria) this;
        }

        public Criteria andExperimentParticipationNumLessThan(Integer value) {
            addCriterion("experiment_participation_num <", value, "experimentParticipationNum");
            return (Criteria) this;
        }

        public Criteria andExperimentParticipationNumLessThanOrEqualTo(Integer value) {
            addCriterion("experiment_participation_num <=", value, "experimentParticipationNum");
            return (Criteria) this;
        }

        public Criteria andExperimentParticipationNumIn(List<Integer> values) {
            addCriterion("experiment_participation_num in", values, "experimentParticipationNum");
            return (Criteria) this;
        }

        public Criteria andExperimentParticipationNumNotIn(List<Integer> values) {
            addCriterion("experiment_participation_num not in", values, "experimentParticipationNum");
            return (Criteria) this;
        }

        public Criteria andExperimentParticipationNumBetween(Integer value1, Integer value2) {
            addCriterion("experiment_participation_num between", value1, value2, "experimentParticipationNum");
            return (Criteria) this;
        }

        public Criteria andExperimentParticipationNumNotBetween(Integer value1, Integer value2) {
            addCriterion("experiment_participation_num not between", value1, value2, "experimentParticipationNum");
            return (Criteria) this;
        }

        public Criteria andValveIsNull() {
            addCriterion("valve is null");
            return (Criteria) this;
        }

        public Criteria andValveIsNotNull() {
            addCriterion("valve is not null");
            return (Criteria) this;
        }

        public Criteria andValveEqualTo(Float value) {
            addCriterion("valve =", value, "valve");
            return (Criteria) this;
        }

        public Criteria andValveNotEqualTo(Float value) {
            addCriterion("valve <>", value, "valve");
            return (Criteria) this;
        }

        public Criteria andValveGreaterThan(Float value) {
            addCriterion("valve >", value, "valve");
            return (Criteria) this;
        }

        public Criteria andValveGreaterThanOrEqualTo(Float value) {
            addCriterion("valve >=", value, "valve");
            return (Criteria) this;
        }

        public Criteria andValveLessThan(Float value) {
            addCriterion("valve <", value, "valve");
            return (Criteria) this;
        }

        public Criteria andValveLessThanOrEqualTo(Float value) {
            addCriterion("valve <=", value, "valve");
            return (Criteria) this;
        }

        public Criteria andValveIn(List<Float> values) {
            addCriterion("valve in", values, "valve");
            return (Criteria) this;
        }

        public Criteria andValveNotIn(List<Float> values) {
            addCriterion("valve not in", values, "valve");
            return (Criteria) this;
        }

        public Criteria andValveBetween(Float value1, Float value2) {
            addCriterion("valve between", value1, value2, "valve");
            return (Criteria) this;
        }

        public Criteria andValveNotBetween(Float value1, Float value2) {
            addCriterion("valve not between", value1, value2, "valve");
            return (Criteria) this;
        }

        public Criteria andPunishmentIsNull() {
            addCriterion("punishment is null");
            return (Criteria) this;
        }

        public Criteria andPunishmentIsNotNull() {
            addCriterion("punishment is not null");
            return (Criteria) this;
        }

        public Criteria andPunishmentEqualTo(Float value) {
            addCriterion("punishment =", value, "punishment");
            return (Criteria) this;
        }

        public Criteria andPunishmentNotEqualTo(Float value) {
            addCriterion("punishment <>", value, "punishment");
            return (Criteria) this;
        }

        public Criteria andPunishmentGreaterThan(Float value) {
            addCriterion("punishment >", value, "punishment");
            return (Criteria) this;
        }

        public Criteria andPunishmentGreaterThanOrEqualTo(Float value) {
            addCriterion("punishment >=", value, "punishment");
            return (Criteria) this;
        }

        public Criteria andPunishmentLessThan(Float value) {
            addCriterion("punishment <", value, "punishment");
            return (Criteria) this;
        }

        public Criteria andPunishmentLessThanOrEqualTo(Float value) {
            addCriterion("punishment <=", value, "punishment");
            return (Criteria) this;
        }

        public Criteria andPunishmentIn(List<Float> values) {
            addCriterion("punishment in", values, "punishment");
            return (Criteria) this;
        }

        public Criteria andPunishmentNotIn(List<Float> values) {
            addCriterion("punishment not in", values, "punishment");
            return (Criteria) this;
        }

        public Criteria andPunishmentBetween(Float value1, Float value2) {
            addCriterion("punishment between", value1, value2, "punishment");
            return (Criteria) this;
        }

        public Criteria andPunishmentNotBetween(Float value1, Float value2) {
            addCriterion("punishment not between", value1, value2, "punishment");
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

        public Criteria andExperimentStatusIsNull() {
            addCriterion("experiment_status is null");
            return (Criteria) this;
        }

        public Criteria andExperimentStatusIsNotNull() {
            addCriterion("experiment_status is not null");
            return (Criteria) this;
        }

        public Criteria andExperimentStatusEqualTo(Byte value) {
            addCriterion("experiment_status =", value, "experimentStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentStatusNotEqualTo(Byte value) {
            addCriterion("experiment_status <>", value, "experimentStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentStatusGreaterThan(Byte value) {
            addCriterion("experiment_status >", value, "experimentStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("experiment_status >=", value, "experimentStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentStatusLessThan(Byte value) {
            addCriterion("experiment_status <", value, "experimentStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentStatusLessThanOrEqualTo(Byte value) {
            addCriterion("experiment_status <=", value, "experimentStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentStatusIn(List<Byte> values) {
            addCriterion("experiment_status in", values, "experimentStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentStatusNotIn(List<Byte> values) {
            addCriterion("experiment_status not in", values, "experimentStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentStatusBetween(Byte value1, Byte value2) {
            addCriterion("experiment_status between", value1, value2, "experimentStatus");
            return (Criteria) this;
        }

        public Criteria andExperimentStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("experiment_status not between", value1, value2, "experimentStatus");
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