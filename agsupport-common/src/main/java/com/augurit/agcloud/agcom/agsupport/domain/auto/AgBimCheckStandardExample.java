package com.augurit.agcloud.agcom.agsupport.domain.auto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @Author: Zihui Li
 * @Date: 2020/12/24
 * @tips: example类
 */
public class AgBimCheckStandardExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AgBimCheckStandardExample() {
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

        public Criteria andClauseIsNull() {
            addCriterion("clause is null");
            return (Criteria) this;
        }

        public Criteria andClauseIsNotNull() {
            addCriterion("clause is not null");
            return (Criteria) this;
        }

        public Criteria andClauseEqualTo(String value) {
            addCriterion("clause =", value, "clause");
            return (Criteria) this;
        }

        public Criteria andClauseNotEqualTo(String value) {
            addCriterion("clause <>", value, "clause");
            return (Criteria) this;
        }

        public Criteria andClauseGreaterThan(String value) {
            addCriterion("clause >", value, "clause");
            return (Criteria) this;
        }

        public Criteria andClauseGreaterThanOrEqualTo(String value) {
            addCriterion("clause >=", value, "clause");
            return (Criteria) this;
        }

        public Criteria andClauseLessThan(String value) {
            addCriterion("clause <", value, "clause");
            return (Criteria) this;
        }

        public Criteria andClauseLessThanOrEqualTo(String value) {
            addCriterion("clause <=", value, "clause");
            return (Criteria) this;
        }

        public Criteria andClauseLike(String value) {
            addCriterion("clause like", value, "clause");
            return (Criteria) this;
        }

        public Criteria andClauseNotLike(String value) {
            addCriterion("clause not like", value, "clause");
            return (Criteria) this;
        }

        public Criteria andClauseIn(List<String> values) {
            addCriterion("clause in", values, "clause");
            return (Criteria) this;
        }

        public Criteria andClauseNotIn(List<String> values) {
            addCriterion("clause not in", values, "clause");
            return (Criteria) this;
        }

        public Criteria andClauseBetween(String value1, String value2) {
            addCriterion("clause between", value1, value2, "clause");
            return (Criteria) this;
        }

        public Criteria andClauseNotBetween(String value1, String value2) {
            addCriterion("clause not between", value1, value2, "clause");
            return (Criteria) this;
        }

        public Criteria andSerialIsNull() {
            addCriterion("serial is null");
            return (Criteria) this;
        }

        public Criteria andSerialIsNotNull() {
            addCriterion("serial is not null");
            return (Criteria) this;
        }

        public Criteria andSerialEqualTo(String value) {
            addCriterion("serial =", value, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialNotEqualTo(String value) {
            addCriterion("serial <>", value, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialGreaterThan(String value) {
            addCriterion("serial >", value, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialGreaterThanOrEqualTo(String value) {
            addCriterion("serial >=", value, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialLessThan(String value) {
            addCriterion("serial <", value, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialLessThanOrEqualTo(String value) {
            addCriterion("serial <=", value, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialLike(String value) {
            addCriterion("serial like", value, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialNotLike(String value) {
            addCriterion("serial not like", value, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialIn(List<String> values) {
            addCriterion("serial in", values, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialNotIn(List<String> values) {
            addCriterion("serial not in", values, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialBetween(String value1, String value2) {
            addCriterion("serial between", value1, value2, "serial");
            return (Criteria) this;
        }

        public Criteria andSerialNotBetween(String value1, String value2) {
            addCriterion("serial not between", value1, value2, "serial");
            return (Criteria) this;
        }

        public Criteria andEnforceIsNull() {
            addCriterion("enforce is null");
            return (Criteria) this;
        }

        public Criteria andEnforceIsNotNull() {
            addCriterion("enforce is not null");
            return (Criteria) this;
        }

        public Criteria andEnforceEqualTo(String value) {
            addCriterion("enforce =", value, "enforce");
            return (Criteria) this;
        }

        public Criteria andEnforceNotEqualTo(String value) {
            addCriterion("enforce <>", value, "enforce");
            return (Criteria) this;
        }

        public Criteria andEnforceGreaterThan(String value) {
            addCriterion("enforce >", value, "enforce");
            return (Criteria) this;
        }

        public Criteria andEnforceGreaterThanOrEqualTo(String value) {
            addCriterion("enforce >=", value, "enforce");
            return (Criteria) this;
        }

        public Criteria andEnforceLessThan(String value) {
            addCriterion("enforce <", value, "enforce");
            return (Criteria) this;
        }

        public Criteria andEnforceLessThanOrEqualTo(String value) {
            addCriterion("enforce <=", value, "enforce");
            return (Criteria) this;
        }

        public Criteria andEnforceLike(String value) {
            addCriterion("enforce like", value, "enforce");
            return (Criteria) this;
        }

        public Criteria andEnforceNotLike(String value) {
            addCriterion("enforce not like", value, "enforce");
            return (Criteria) this;
        }

        public Criteria andEnforceIn(List<String> values) {
            addCriterion("enforce in", values, "enforce");
            return (Criteria) this;
        }

        public Criteria andEnforceNotIn(List<String> values) {
            addCriterion("enforce not in", values, "enforce");
            return (Criteria) this;
        }

        public Criteria andEnforceBetween(String value1, String value2) {
            addCriterion("enforce between", value1, value2, "enforce");
            return (Criteria) this;
        }

        public Criteria andEnforceNotBetween(String value1, String value2) {
            addCriterion("enforce not between", value1, value2, "enforce");
            return (Criteria) this;
        }

        public Criteria andClauseContentIsNull() {
            addCriterion("clause_content is null");
            return (Criteria) this;
        }

        public Criteria andClauseContentIsNotNull() {
            addCriterion("clause_content is not null");
            return (Criteria) this;
        }

        public Criteria andClauseContentEqualTo(String value) {
            addCriterion("clause_content =", value, "clauseContent");
            return (Criteria) this;
        }

        public Criteria andClauseContentNotEqualTo(String value) {
            addCriterion("clause_content <>", value, "clauseContent");
            return (Criteria) this;
        }

        public Criteria andClauseContentGreaterThan(String value) {
            addCriterion("clause_content >", value, "clauseContent");
            return (Criteria) this;
        }

        public Criteria andClauseContentGreaterThanOrEqualTo(String value) {
            addCriterion("clause_content >=", value, "clauseContent");
            return (Criteria) this;
        }

        public Criteria andClauseContentLessThan(String value) {
            addCriterion("clause_content <", value, "clauseContent");
            return (Criteria) this;
        }

        public Criteria andClauseContentLessThanOrEqualTo(String value) {
            addCriterion("clause_content <=", value, "clauseContent");
            return (Criteria) this;
        }

        public Criteria andClauseContentLike(String value) {
            addCriterion("clause_content like", value, "clauseContent");
            return (Criteria) this;
        }

        public Criteria andClauseContentNotLike(String value) {
            addCriterion("clause_content not like", value, "clauseContent");
            return (Criteria) this;
        }

        public Criteria andClauseContentIn(List<String> values) {
            addCriterion("clause_content in", values, "clauseContent");
            return (Criteria) this;
        }

        public Criteria andClauseContentNotIn(List<String> values) {
            addCriterion("clause_content not in", values, "clauseContent");
            return (Criteria) this;
        }

        public Criteria andClauseContentBetween(String value1, String value2) {
            addCriterion("clause_content between", value1, value2, "clauseContent");
            return (Criteria) this;
        }

        public Criteria andClauseContentNotBetween(String value1, String value2) {
            addCriterion("clause_content not between", value1, value2, "clauseContent");
            return (Criteria) this;
        }

        public Criteria andAssociateModelIsNull() {
            addCriterion("associate_model is null");
            return (Criteria) this;
        }

        public Criteria andAssociateModelIsNotNull() {
            addCriterion("associate_model is not null");
            return (Criteria) this;
        }

        public Criteria andAssociateModelEqualTo(String value) {
            addCriterion("associate_model =", value, "associateModel");
            return (Criteria) this;
        }

        public Criteria andAssociateModelNotEqualTo(String value) {
            addCriterion("associate_model <>", value, "associateModel");
            return (Criteria) this;
        }

        public Criteria andAssociateModelGreaterThan(String value) {
            addCriterion("associate_model >", value, "associateModel");
            return (Criteria) this;
        }

        public Criteria andAssociateModelGreaterThanOrEqualTo(String value) {
            addCriterion("associate_model >=", value, "associateModel");
            return (Criteria) this;
        }

        public Criteria andAssociateModelLessThan(String value) {
            addCriterion("associate_model <", value, "associateModel");
            return (Criteria) this;
        }

        public Criteria andAssociateModelLessThanOrEqualTo(String value) {
            addCriterion("associate_model <=", value, "associateModel");
            return (Criteria) this;
        }

        public Criteria andAssociateModelLike(String value) {
            addCriterion("associate_model like", value, "associateModel");
            return (Criteria) this;
        }

        public Criteria andAssociateModelNotLike(String value) {
            addCriterion("associate_model not like", value, "associateModel");
            return (Criteria) this;
        }

        public Criteria andAssociateModelIn(List<String> values) {
            addCriterion("associate_model in", values, "associateModel");
            return (Criteria) this;
        }

        public Criteria andAssociateModelNotIn(List<String> values) {
            addCriterion("associate_model not in", values, "associateModel");
            return (Criteria) this;
        }

        public Criteria andAssociateModelBetween(String value1, String value2) {
            addCriterion("associate_model between", value1, value2, "associateModel");
            return (Criteria) this;
        }

        public Criteria andAssociateModelNotBetween(String value1, String value2) {
            addCriterion("associate_model not between", value1, value2, "associateModel");
            return (Criteria) this;
        }

        public Criteria andClauseCategoryIsNull() {
            addCriterion("clause_category is null");
            return (Criteria) this;
        }

        public Criteria andClauseCategoryIsNotNull() {
            addCriterion("clause_category is not null");
            return (Criteria) this;
        }

        public Criteria andClauseCategoryEqualTo(String value) {
            addCriterion("clause_category =", value, "clauseCategory");
            return (Criteria) this;
        }

        public Criteria andClauseCategoryNotEqualTo(String value) {
            addCriterion("clause_category <>", value, "clauseCategory");
            return (Criteria) this;
        }

        public Criteria andClauseCategoryGreaterThan(String value) {
            addCriterion("clause_category >", value, "clauseCategory");
            return (Criteria) this;
        }

        public Criteria andClauseCategoryGreaterThanOrEqualTo(String value) {
            addCriterion("clause_category >=", value, "clauseCategory");
            return (Criteria) this;
        }

        public Criteria andClauseCategoryLessThan(String value) {
            addCriterion("clause_category <", value, "clauseCategory");
            return (Criteria) this;
        }

        public Criteria andClauseCategoryLessThanOrEqualTo(String value) {
            addCriterion("clause_category <=", value, "clauseCategory");
            return (Criteria) this;
        }

        public Criteria andClauseCategoryLike(String value) {
            addCriterion("clause_category like", value, "clauseCategory");
            return (Criteria) this;
        }

        public Criteria andClauseCategoryNotLike(String value) {
            addCriterion("clause_category not like", value, "clauseCategory");
            return (Criteria) this;
        }

        public Criteria andClauseCategoryIn(List<String> values) {
            addCriterion("clause_category in", values, "clauseCategory");
            return (Criteria) this;
        }

        public Criteria andClauseCategoryNotIn(List<String> values) {
            addCriterion("clause_category not in", values, "clauseCategory");
            return (Criteria) this;
        }

        public Criteria andClauseCategoryBetween(String value1, String value2) {
            addCriterion("clause_category between", value1, value2, "clauseCategory");
            return (Criteria) this;
        }

        public Criteria andClauseCategoryNotBetween(String value1, String value2) {
            addCriterion("clause_category not between", value1, value2, "clauseCategory");
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