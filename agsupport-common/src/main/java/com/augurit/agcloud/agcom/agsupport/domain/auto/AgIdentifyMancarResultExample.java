package com.augurit.agcloud.agcom.agsupport.domain.auto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @Author: qinyg
 * @Date: 2020/12/28
 * @tips: example类
 */
public class AgIdentifyMancarResultExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AgIdentifyMancarResultExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andSourceIdIsNull() {
            addCriterion("source_id is null");
            return (Criteria) this;
        }

        public Criteria andSourceIdIsNotNull() {
            addCriterion("source_id is not null");
            return (Criteria) this;
        }

        public Criteria andSourceIdEqualTo(String value) {
            addCriterion("source_id =", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotEqualTo(String value) {
            addCriterion("source_id <>", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdGreaterThan(String value) {
            addCriterion("source_id >", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdGreaterThanOrEqualTo(String value) {
            addCriterion("source_id >=", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdLessThan(String value) {
            addCriterion("source_id <", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdLessThanOrEqualTo(String value) {
            addCriterion("source_id <=", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdLike(String value) {
            addCriterion("source_id like", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotLike(String value) {
            addCriterion("source_id not like", value, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdIn(List<String> values) {
            addCriterion("source_id in", values, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotIn(List<String> values) {
            addCriterion("source_id not in", values, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdBetween(String value1, String value2) {
            addCriterion("source_id between", value1, value2, "sourceId");
            return (Criteria) this;
        }

        public Criteria andSourceIdNotBetween(String value1, String value2) {
            addCriterion("source_id not between", value1, value2, "sourceId");
            return (Criteria) this;
        }

        public Criteria andIdentifyTimeIsNull() {
            addCriterion("identify_time is null");
            return (Criteria) this;
        }

        public Criteria andIdentifyTimeIsNotNull() {
            addCriterion("identify_time is not null");
            return (Criteria) this;
        }

        public Criteria andIdentifyTimeEqualTo(Date value) {
            addCriterion("identify_time =", value, "identifyTime");
            return (Criteria) this;
        }

        public Criteria andIdentifyTimeNotEqualTo(Date value) {
            addCriterion("identify_time <>", value, "identifyTime");
            return (Criteria) this;
        }

        public Criteria andIdentifyTimeGreaterThan(Date value) {
            addCriterion("identify_time >", value, "identifyTime");
            return (Criteria) this;
        }

        public Criteria andIdentifyTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("identify_time >=", value, "identifyTime");
            return (Criteria) this;
        }

        public Criteria andIdentifyTimeLessThan(Date value) {
            addCriterion("identify_time <", value, "identifyTime");
            return (Criteria) this;
        }

        public Criteria andIdentifyTimeLessThanOrEqualTo(Date value) {
            addCriterion("identify_time <=", value, "identifyTime");
            return (Criteria) this;
        }

        public Criteria andIdentifyTimeIn(List<Date> values) {
            addCriterion("identify_time in", values, "identifyTime");
            return (Criteria) this;
        }

        public Criteria andIdentifyTimeNotIn(List<Date> values) {
            addCriterion("identify_time not in", values, "identifyTime");
            return (Criteria) this;
        }

        public Criteria andIdentifyTimeBetween(Date value1, Date value2) {
            addCriterion("identify_time between", value1, value2, "identifyTime");
            return (Criteria) this;
        }

        public Criteria andIdentifyTimeNotBetween(Date value1, Date value2) {
            addCriterion("identify_time not between", value1, value2, "identifyTime");
            return (Criteria) this;
        }

        public Criteria andNumPeopleIsNull() {
            addCriterion("num_people is null");
            return (Criteria) this;
        }

        public Criteria andNumPeopleIsNotNull() {
            addCriterion("num_people is not null");
            return (Criteria) this;
        }

        public Criteria andNumPeopleEqualTo(Integer value) {
            addCriterion("num_people =", value, "numPeople");
            return (Criteria) this;
        }

        public Criteria andNumPeopleNotEqualTo(Integer value) {
            addCriterion("num_people <>", value, "numPeople");
            return (Criteria) this;
        }

        public Criteria andNumPeopleGreaterThan(Integer value) {
            addCriterion("num_people >", value, "numPeople");
            return (Criteria) this;
        }

        public Criteria andNumPeopleGreaterThanOrEqualTo(Integer value) {
            addCriterion("num_people >=", value, "numPeople");
            return (Criteria) this;
        }

        public Criteria andNumPeopleLessThan(Integer value) {
            addCriterion("num_people <", value, "numPeople");
            return (Criteria) this;
        }

        public Criteria andNumPeopleLessThanOrEqualTo(Integer value) {
            addCriterion("num_people <=", value, "numPeople");
            return (Criteria) this;
        }

        public Criteria andNumPeopleIn(List<Integer> values) {
            addCriterion("num_people in", values, "numPeople");
            return (Criteria) this;
        }

        public Criteria andNumPeopleNotIn(List<Integer> values) {
            addCriterion("num_people not in", values, "numPeople");
            return (Criteria) this;
        }

        public Criteria andNumPeopleBetween(Integer value1, Integer value2) {
            addCriterion("num_people between", value1, value2, "numPeople");
            return (Criteria) this;
        }

        public Criteria andNumPeopleNotBetween(Integer value1, Integer value2) {
            addCriterion("num_people not between", value1, value2, "numPeople");
            return (Criteria) this;
        }

        public Criteria andNumCarIsNull() {
            addCriterion("num_car is null");
            return (Criteria) this;
        }

        public Criteria andNumCarIsNotNull() {
            addCriterion("num_car is not null");
            return (Criteria) this;
        }

        public Criteria andNumCarEqualTo(Integer value) {
            addCriterion("num_car =", value, "numCar");
            return (Criteria) this;
        }

        public Criteria andNumCarNotEqualTo(Integer value) {
            addCriterion("num_car <>", value, "numCar");
            return (Criteria) this;
        }

        public Criteria andNumCarGreaterThan(Integer value) {
            addCriterion("num_car >", value, "numCar");
            return (Criteria) this;
        }

        public Criteria andNumCarGreaterThanOrEqualTo(Integer value) {
            addCriterion("num_car >=", value, "numCar");
            return (Criteria) this;
        }

        public Criteria andNumCarLessThan(Integer value) {
            addCriterion("num_car <", value, "numCar");
            return (Criteria) this;
        }

        public Criteria andNumCarLessThanOrEqualTo(Integer value) {
            addCriterion("num_car <=", value, "numCar");
            return (Criteria) this;
        }

        public Criteria andNumCarIn(List<Integer> values) {
            addCriterion("num_car in", values, "numCar");
            return (Criteria) this;
        }

        public Criteria andNumCarNotIn(List<Integer> values) {
            addCriterion("num_car not in", values, "numCar");
            return (Criteria) this;
        }

        public Criteria andNumCarBetween(Integer value1, Integer value2) {
            addCriterion("num_car between", value1, value2, "numCar");
            return (Criteria) this;
        }

        public Criteria andNumCarNotBetween(Integer value1, Integer value2) {
            addCriterion("num_car not between", value1, value2, "numCar");
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

        public Criteria andModifyTimeIsNull() {
            addCriterion("modify_time is null");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIsNotNull() {
            addCriterion("modify_time is not null");
            return (Criteria) this;
        }

        public Criteria andModifyTimeEqualTo(Date value) {
            addCriterion("modify_time =", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotEqualTo(Date value) {
            addCriterion("modify_time <>", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeGreaterThan(Date value) {
            addCriterion("modify_time >", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("modify_time >=", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeLessThan(Date value) {
            addCriterion("modify_time <", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeLessThanOrEqualTo(Date value) {
            addCriterion("modify_time <=", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIn(List<Date> values) {
            addCriterion("modify_time in", values, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotIn(List<Date> values) {
            addCriterion("modify_time not in", values, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeBetween(Date value1, Date value2) {
            addCriterion("modify_time between", value1, value2, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotBetween(Date value1, Date value2) {
            addCriterion("modify_time not between", value1, value2, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
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