package com.augurit.agcloud.agcom.agsupport.domain.auto;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @Author: qinyg
 * @Date: 2020/10/25
 * @tips: example类
 */
public class AgUserDesignMaterialsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AgUserDesignMaterialsExample() {
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

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(String value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(String value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(String value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(String value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(String value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(String value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLike(String value) {
            addCriterion("type like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotLike(String value) {
            addCriterion("type not like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<String> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<String> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(String value1, String value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(String value1, String value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andPositionIsNull() {
            addCriterion("position is null");
            return (Criteria) this;
        }

        public Criteria andPositionIsNotNull() {
            addCriterion("position is not null");
            return (Criteria) this;
        }

        public Criteria andPositionEqualTo(String value) {
            addCriterion("position =", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionNotEqualTo(String value) {
            addCriterion("position <>", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionGreaterThan(String value) {
            addCriterion("position >", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionGreaterThanOrEqualTo(String value) {
            addCriterion("position >=", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionLessThan(String value) {
            addCriterion("position <", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionLessThanOrEqualTo(String value) {
            addCriterion("position <=", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionLike(String value) {
            addCriterion("position like", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionNotLike(String value) {
            addCriterion("position not like", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionIn(List<String> values) {
            addCriterion("position in", values, "position");
            return (Criteria) this;
        }

        public Criteria andPositionNotIn(List<String> values) {
            addCriterion("position not in", values, "position");
            return (Criteria) this;
        }

        public Criteria andPositionBetween(String value1, String value2) {
            addCriterion("position between", value1, value2, "position");
            return (Criteria) this;
        }

        public Criteria andPositionNotBetween(String value1, String value2) {
            addCriterion("position not between", value1, value2, "position");
            return (Criteria) this;
        }

        public Criteria andOrientationIsNull() {
            addCriterion("orientation is null");
            return (Criteria) this;
        }

        public Criteria andOrientationIsNotNull() {
            addCriterion("orientation is not null");
            return (Criteria) this;
        }

        public Criteria andOrientationEqualTo(String value) {
            addCriterion("orientation =", value, "orientation");
            return (Criteria) this;
        }

        public Criteria andOrientationNotEqualTo(String value) {
            addCriterion("orientation <>", value, "orientation");
            return (Criteria) this;
        }

        public Criteria andOrientationGreaterThan(String value) {
            addCriterion("orientation >", value, "orientation");
            return (Criteria) this;
        }

        public Criteria andOrientationGreaterThanOrEqualTo(String value) {
            addCriterion("orientation >=", value, "orientation");
            return (Criteria) this;
        }

        public Criteria andOrientationLessThan(String value) {
            addCriterion("orientation <", value, "orientation");
            return (Criteria) this;
        }

        public Criteria andOrientationLessThanOrEqualTo(String value) {
            addCriterion("orientation <=", value, "orientation");
            return (Criteria) this;
        }

        public Criteria andOrientationLike(String value) {
            addCriterion("orientation like", value, "orientation");
            return (Criteria) this;
        }

        public Criteria andOrientationNotLike(String value) {
            addCriterion("orientation not like", value, "orientation");
            return (Criteria) this;
        }

        public Criteria andOrientationIn(List<String> values) {
            addCriterion("orientation in", values, "orientation");
            return (Criteria) this;
        }

        public Criteria andOrientationNotIn(List<String> values) {
            addCriterion("orientation not in", values, "orientation");
            return (Criteria) this;
        }

        public Criteria andOrientationBetween(String value1, String value2) {
            addCriterion("orientation between", value1, value2, "orientation");
            return (Criteria) this;
        }

        public Criteria andOrientationNotBetween(String value1, String value2) {
            addCriterion("orientation not between", value1, value2, "orientation");
            return (Criteria) this;
        }

        public Criteria andUrlIsNull() {
            addCriterion("url is null");
            return (Criteria) this;
        }

        public Criteria andUrlIsNotNull() {
            addCriterion("url is not null");
            return (Criteria) this;
        }

        public Criteria andUrlEqualTo(String value) {
            addCriterion("url =", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotEqualTo(String value) {
            addCriterion("url <>", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThan(String value) {
            addCriterion("url >", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlGreaterThanOrEqualTo(String value) {
            addCriterion("url >=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThan(String value) {
            addCriterion("url <", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLessThanOrEqualTo(String value) {
            addCriterion("url <=", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlLike(String value) {
            addCriterion("url like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotLike(String value) {
            addCriterion("url not like", value, "url");
            return (Criteria) this;
        }

        public Criteria andUrlIn(List<String> values) {
            addCriterion("url in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotIn(List<String> values) {
            addCriterion("url not in", values, "url");
            return (Criteria) this;
        }

        public Criteria andUrlBetween(String value1, String value2) {
            addCriterion("url between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andUrlNotBetween(String value1, String value2) {
            addCriterion("url not between", value1, value2, "url");
            return (Criteria) this;
        }

        public Criteria andModelMatrixIsNull() {
            addCriterion("model_matrix is null");
            return (Criteria) this;
        }

        public Criteria andModelMatrixIsNotNull() {
            addCriterion("model_matrix is not null");
            return (Criteria) this;
        }

        public Criteria andModelMatrixEqualTo(String value) {
            addCriterion("model_matrix =", value, "modelMatrix");
            return (Criteria) this;
        }

        public Criteria andModelMatrixNotEqualTo(String value) {
            addCriterion("model_matrix <>", value, "modelMatrix");
            return (Criteria) this;
        }

        public Criteria andModelMatrixGreaterThan(String value) {
            addCriterion("model_matrix >", value, "modelMatrix");
            return (Criteria) this;
        }

        public Criteria andModelMatrixGreaterThanOrEqualTo(String value) {
            addCriterion("model_matrix >=", value, "modelMatrix");
            return (Criteria) this;
        }

        public Criteria andModelMatrixLessThan(String value) {
            addCriterion("model_matrix <", value, "modelMatrix");
            return (Criteria) this;
        }

        public Criteria andModelMatrixLessThanOrEqualTo(String value) {
            addCriterion("model_matrix <=", value, "modelMatrix");
            return (Criteria) this;
        }

        public Criteria andModelMatrixLike(String value) {
            addCriterion("model_matrix like", value, "modelMatrix");
            return (Criteria) this;
        }

        public Criteria andModelMatrixNotLike(String value) {
            addCriterion("model_matrix not like", value, "modelMatrix");
            return (Criteria) this;
        }

        public Criteria andModelMatrixIn(List<String> values) {
            addCriterion("model_matrix in", values, "modelMatrix");
            return (Criteria) this;
        }

        public Criteria andModelMatrixNotIn(List<String> values) {
            addCriterion("model_matrix not in", values, "modelMatrix");
            return (Criteria) this;
        }

        public Criteria andModelMatrixBetween(String value1, String value2) {
            addCriterion("model_matrix between", value1, value2, "modelMatrix");
            return (Criteria) this;
        }

        public Criteria andModelMatrixNotBetween(String value1, String value2) {
            addCriterion("model_matrix not between", value1, value2, "modelMatrix");
            return (Criteria) this;
        }

        public Criteria andPropertyUrlIsNull() {
            addCriterion("property_url is null");
            return (Criteria) this;
        }

        public Criteria andPropertyUrlIsNotNull() {
            addCriterion("property_url is not null");
            return (Criteria) this;
        }

        public Criteria andPropertyUrlEqualTo(String value) {
            addCriterion("property_url =", value, "propertyUrl");
            return (Criteria) this;
        }

        public Criteria andPropertyUrlNotEqualTo(String value) {
            addCriterion("property_url <>", value, "propertyUrl");
            return (Criteria) this;
        }

        public Criteria andPropertyUrlGreaterThan(String value) {
            addCriterion("property_url >", value, "propertyUrl");
            return (Criteria) this;
        }

        public Criteria andPropertyUrlGreaterThanOrEqualTo(String value) {
            addCriterion("property_url >=", value, "propertyUrl");
            return (Criteria) this;
        }

        public Criteria andPropertyUrlLessThan(String value) {
            addCriterion("property_url <", value, "propertyUrl");
            return (Criteria) this;
        }

        public Criteria andPropertyUrlLessThanOrEqualTo(String value) {
            addCriterion("property_url <=", value, "propertyUrl");
            return (Criteria) this;
        }

        public Criteria andPropertyUrlLike(String value) {
            addCriterion("property_url like", value, "propertyUrl");
            return (Criteria) this;
        }

        public Criteria andPropertyUrlNotLike(String value) {
            addCriterion("property_url not like", value, "propertyUrl");
            return (Criteria) this;
        }

        public Criteria andPropertyUrlIn(List<String> values) {
            addCriterion("property_url in", values, "propertyUrl");
            return (Criteria) this;
        }

        public Criteria andPropertyUrlNotIn(List<String> values) {
            addCriterion("property_url not in", values, "propertyUrl");
            return (Criteria) this;
        }

        public Criteria andPropertyUrlBetween(String value1, String value2) {
            addCriterion("property_url between", value1, value2, "propertyUrl");
            return (Criteria) this;
        }

        public Criteria andPropertyUrlNotBetween(String value1, String value2) {
            addCriterion("property_url not between", value1, value2, "propertyUrl");
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

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("user_id not between", value1, value2, "userId");
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

        public Criteria andDesignSchemeIdIsNull() {
            addCriterion("design_scheme_id is null");
            return (Criteria) this;
        }

        public Criteria andDesignSchemeIdIsNotNull() {
            addCriterion("design_scheme_id is not null");
            return (Criteria) this;
        }

        public Criteria andDesignSchemeIdEqualTo(String value) {
            addCriterion("design_scheme_id =", value, "designSchemeId");
            return (Criteria) this;
        }

        public Criteria andDesignSchemeIdNotEqualTo(String value) {
            addCriterion("design_scheme_id <>", value, "designSchemeId");
            return (Criteria) this;
        }

        public Criteria andDesignSchemeIdGreaterThan(String value) {
            addCriterion("design_scheme_id >", value, "designSchemeId");
            return (Criteria) this;
        }

        public Criteria andDesignSchemeIdGreaterThanOrEqualTo(String value) {
            addCriterion("design_scheme_id >=", value, "designSchemeId");
            return (Criteria) this;
        }

        public Criteria andDesignSchemeIdLessThan(String value) {
            addCriterion("design_scheme_id <", value, "designSchemeId");
            return (Criteria) this;
        }

        public Criteria andDesignSchemeIdLessThanOrEqualTo(String value) {
            addCriterion("design_scheme_id <=", value, "designSchemeId");
            return (Criteria) this;
        }

        public Criteria andDesignSchemeIdLike(String value) {
            addCriterion("design_scheme_id like", value, "designSchemeId");
            return (Criteria) this;
        }

        public Criteria andDesignSchemeIdNotLike(String value) {
            addCriterion("design_scheme_id not like", value, "designSchemeId");
            return (Criteria) this;
        }

        public Criteria andDesignSchemeIdIn(List<String> values) {
            addCriterion("design_scheme_id in", values, "designSchemeId");
            return (Criteria) this;
        }

        public Criteria andDesignSchemeIdNotIn(List<String> values) {
            addCriterion("design_scheme_id not in", values, "designSchemeId");
            return (Criteria) this;
        }

        public Criteria andDesignSchemeIdBetween(String value1, String value2) {
            addCriterion("design_scheme_id between", value1, value2, "designSchemeId");
            return (Criteria) this;
        }

        public Criteria andDesignSchemeIdNotBetween(String value1, String value2) {
            addCriterion("design_scheme_id not between", value1, value2, "designSchemeId");
            return (Criteria) this;
        }

        public Criteria andAngleIsNull() {
            addCriterion("angle is null");
            return (Criteria) this;
        }

        public Criteria andAngleIsNotNull() {
            addCriterion("angle is not null");
            return (Criteria) this;
        }

        public Criteria andAngleEqualTo(String value) {
            addCriterion("angle =", value, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleNotEqualTo(String value) {
            addCriterion("angle <>", value, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleGreaterThan(String value) {
            addCriterion("angle >", value, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleGreaterThanOrEqualTo(String value) {
            addCriterion("angle >=", value, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleLessThan(String value) {
            addCriterion("angle <", value, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleLessThanOrEqualTo(String value) {
            addCriterion("angle <=", value, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleLike(String value) {
            addCriterion("angle like", value, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleNotLike(String value) {
            addCriterion("angle not like", value, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleIn(List<String> values) {
            addCriterion("angle in", values, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleNotIn(List<String> values) {
            addCriterion("angle not in", values, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleBetween(String value1, String value2) {
            addCriterion("angle between", value1, value2, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleNotBetween(String value1, String value2) {
            addCriterion("angle not between", value1, value2, "angle");
            return (Criteria) this;
        }

        public Criteria andTableNameIsNull() {
            addCriterion("table_name is null");
            return (Criteria) this;
        }

        public Criteria andTableNameIsNotNull() {
            addCriterion("table_name is not null");
            return (Criteria) this;
        }

        public Criteria andTableNameEqualTo(String value) {
            addCriterion("table_name =", value, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameNotEqualTo(String value) {
            addCriterion("table_name <>", value, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameGreaterThan(String value) {
            addCriterion("table_name >", value, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameGreaterThanOrEqualTo(String value) {
            addCriterion("table_name >=", value, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameLessThan(String value) {
            addCriterion("table_name <", value, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameLessThanOrEqualTo(String value) {
            addCriterion("table_name <=", value, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameLike(String value) {
            addCriterion("table_name like", value, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameNotLike(String value) {
            addCriterion("table_name not like", value, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameIn(List<String> values) {
            addCriterion("table_name in", values, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameNotIn(List<String> values) {
            addCriterion("table_name not in", values, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameBetween(String value1, String value2) {
            addCriterion("table_name between", value1, value2, "tableName");
            return (Criteria) this;
        }

        public Criteria andTableNameNotBetween(String value1, String value2) {
            addCriterion("table_name not between", value1, value2, "tableName");
            return (Criteria) this;
        }

        public Criteria andComponentsIsNull() {
            addCriterion("components is null");
            return (Criteria) this;
        }

        public Criteria andComponentsIsNotNull() {
            addCriterion("components is not null");
            return (Criteria) this;
        }

        public Criteria andComponentsEqualTo(String value) {
            addCriterion("components =", value, "components");
            return (Criteria) this;
        }

        public Criteria andComponentsNotEqualTo(String value) {
            addCriterion("components <>", value, "components");
            return (Criteria) this;
        }

        public Criteria andComponentsGreaterThan(String value) {
            addCriterion("components >", value, "components");
            return (Criteria) this;
        }

        public Criteria andComponentsGreaterThanOrEqualTo(String value) {
            addCriterion("components >=", value, "components");
            return (Criteria) this;
        }

        public Criteria andComponentsLessThan(String value) {
            addCriterion("components <", value, "components");
            return (Criteria) this;
        }

        public Criteria andComponentsLessThanOrEqualTo(String value) {
            addCriterion("components <=", value, "components");
            return (Criteria) this;
        }

        public Criteria andComponentsLike(String value) {
            addCriterion("components like", value, "components");
            return (Criteria) this;
        }

        public Criteria andComponentsNotLike(String value) {
            addCriterion("components not like", value, "components");
            return (Criteria) this;
        }

        public Criteria andComponentsIn(List<String> values) {
            addCriterion("components in", values, "components");
            return (Criteria) this;
        }

        public Criteria andComponentsNotIn(List<String> values) {
            addCriterion("components not in", values, "components");
            return (Criteria) this;
        }

        public Criteria andComponentsBetween(String value1, String value2) {
            addCriterion("components between", value1, value2, "components");
            return (Criteria) this;
        }

        public Criteria andComponentsNotBetween(String value1, String value2) {
            addCriterion("components not between", value1, value2, "components");
            return (Criteria) this;
        }

        public Criteria andStyleIsNull() {
            addCriterion("style is null");
            return (Criteria) this;
        }

        public Criteria andStyleIsNotNull() {
            addCriterion("style is not null");
            return (Criteria) this;
        }

        public Criteria andStyleEqualTo(String value) {
            addCriterion("style =", value, "style");
            return (Criteria) this;
        }

        public Criteria andStyleNotEqualTo(String value) {
            addCriterion("style <>", value, "style");
            return (Criteria) this;
        }

        public Criteria andStyleGreaterThan(String value) {
            addCriterion("style >", value, "style");
            return (Criteria) this;
        }

        public Criteria andStyleGreaterThanOrEqualTo(String value) {
            addCriterion("style >=", value, "style");
            return (Criteria) this;
        }

        public Criteria andStyleLessThan(String value) {
            addCriterion("style <", value, "style");
            return (Criteria) this;
        }

        public Criteria andStyleLessThanOrEqualTo(String value) {
            addCriterion("style <=", value, "style");
            return (Criteria) this;
        }

        public Criteria andStyleLike(String value) {
            addCriterion("style like", value, "style");
            return (Criteria) this;
        }

        public Criteria andStyleNotLike(String value) {
            addCriterion("style not like", value, "style");
            return (Criteria) this;
        }

        public Criteria andStyleIn(List<String> values) {
            addCriterion("style in", values, "style");
            return (Criteria) this;
        }

        public Criteria andStyleNotIn(List<String> values) {
            addCriterion("style not in", values, "style");
            return (Criteria) this;
        }

        public Criteria andStyleBetween(String value1, String value2) {
            addCriterion("style between", value1, value2, "style");
            return (Criteria) this;
        }

        public Criteria andStyleNotBetween(String value1, String value2) {
            addCriterion("style not between", value1, value2, "style");
            return (Criteria) this;
        }

        public Criteria andTileUrlIsNull() {
            addCriterion("tile_url is null");
            return (Criteria) this;
        }

        public Criteria andTileUrlIsNotNull() {
            addCriterion("tile_url is not null");
            return (Criteria) this;
        }

        public Criteria andTileUrlEqualTo(String value) {
            addCriterion("tile_url =", value, "tileUrl");
            return (Criteria) this;
        }

        public Criteria andTileUrlNotEqualTo(String value) {
            addCriterion("tile_url <>", value, "tileUrl");
            return (Criteria) this;
        }

        public Criteria andTileUrlGreaterThan(String value) {
            addCriterion("tile_url >", value, "tileUrl");
            return (Criteria) this;
        }

        public Criteria andTileUrlGreaterThanOrEqualTo(String value) {
            addCriterion("tile_url >=", value, "tileUrl");
            return (Criteria) this;
        }

        public Criteria andTileUrlLessThan(String value) {
            addCriterion("tile_url <", value, "tileUrl");
            return (Criteria) this;
        }

        public Criteria andTileUrlLessThanOrEqualTo(String value) {
            addCriterion("tile_url <=", value, "tileUrl");
            return (Criteria) this;
        }

        public Criteria andTileUrlLike(String value) {
            addCriterion("tile_url like", value, "tileUrl");
            return (Criteria) this;
        }

        public Criteria andTileUrlNotLike(String value) {
            addCriterion("tile_url not like", value, "tileUrl");
            return (Criteria) this;
        }

        public Criteria andTileUrlIn(List<String> values) {
            addCriterion("tile_url in", values, "tileUrl");
            return (Criteria) this;
        }

        public Criteria andTileUrlNotIn(List<String> values) {
            addCriterion("tile_url not in", values, "tileUrl");
            return (Criteria) this;
        }

        public Criteria andTileUrlBetween(String value1, String value2) {
            addCriterion("tile_url between", value1, value2, "tileUrl");
            return (Criteria) this;
        }

        public Criteria andTileUrlNotBetween(String value1, String value2) {
            addCriterion("tile_url not between", value1, value2, "tileUrl");
            return (Criteria) this;
        }

        public Criteria andComponentTypeIsNull() {
            addCriterion("component_type is null");
            return (Criteria) this;
        }

        public Criteria andComponentTypeIsNotNull() {
            addCriterion("component_type is not null");
            return (Criteria) this;
        }

        public Criteria andComponentTypeEqualTo(String value) {
            addCriterion("component_type =", value, "componentType");
            return (Criteria) this;
        }

        public Criteria andComponentTypeNotEqualTo(String value) {
            addCriterion("component_type <>", value, "componentType");
            return (Criteria) this;
        }

        public Criteria andComponentTypeGreaterThan(String value) {
            addCriterion("component_type >", value, "componentType");
            return (Criteria) this;
        }

        public Criteria andComponentTypeGreaterThanOrEqualTo(String value) {
            addCriterion("component_type >=", value, "componentType");
            return (Criteria) this;
        }

        public Criteria andComponentTypeLessThan(String value) {
            addCriterion("component_type <", value, "componentType");
            return (Criteria) this;
        }

        public Criteria andComponentTypeLessThanOrEqualTo(String value) {
            addCriterion("component_type <=", value, "componentType");
            return (Criteria) this;
        }

        public Criteria andComponentTypeLike(String value) {
            addCriterion("component_type like", value, "componentType");
            return (Criteria) this;
        }

        public Criteria andComponentTypeNotLike(String value) {
            addCriterion("component_type not like", value, "componentType");
            return (Criteria) this;
        }

        public Criteria andComponentTypeIn(List<String> values) {
            addCriterion("component_type in", values, "componentType");
            return (Criteria) this;
        }

        public Criteria andComponentTypeNotIn(List<String> values) {
            addCriterion("component_type not in", values, "componentType");
            return (Criteria) this;
        }

        public Criteria andComponentTypeBetween(String value1, String value2) {
            addCriterion("component_type between", value1, value2, "componentType");
            return (Criteria) this;
        }

        public Criteria andComponentTypeNotBetween(String value1, String value2) {
            addCriterion("component_type not between", value1, value2, "componentType");
            return (Criteria) this;
        }

        public Criteria andComponentIdIsNull() {
            addCriterion("component_id is null");
            return (Criteria) this;
        }

        public Criteria andComponentIdIsNotNull() {
            addCriterion("component_id is not null");
            return (Criteria) this;
        }

        public Criteria andComponentIdEqualTo(String value) {
            addCriterion("component_id =", value, "componentId");
            return (Criteria) this;
        }

        public Criteria andComponentIdNotEqualTo(String value) {
            addCriterion("component_id <>", value, "componentId");
            return (Criteria) this;
        }

        public Criteria andComponentIdGreaterThan(String value) {
            addCriterion("component_id >", value, "componentId");
            return (Criteria) this;
        }

        public Criteria andComponentIdGreaterThanOrEqualTo(String value) {
            addCriterion("component_id >=", value, "componentId");
            return (Criteria) this;
        }

        public Criteria andComponentIdLessThan(String value) {
            addCriterion("component_id <", value, "componentId");
            return (Criteria) this;
        }

        public Criteria andComponentIdLessThanOrEqualTo(String value) {
            addCriterion("component_id <=", value, "componentId");
            return (Criteria) this;
        }

        public Criteria andComponentIdLike(String value) {
            addCriterion("component_id like", value, "componentId");
            return (Criteria) this;
        }

        public Criteria andComponentIdNotLike(String value) {
            addCriterion("component_id not like", value, "componentId");
            return (Criteria) this;
        }

        public Criteria andComponentIdIn(List<String> values) {
            addCriterion("component_id in", values, "componentId");
            return (Criteria) this;
        }

        public Criteria andComponentIdNotIn(List<String> values) {
            addCriterion("component_id not in", values, "componentId");
            return (Criteria) this;
        }

        public Criteria andComponentIdBetween(String value1, String value2) {
            addCriterion("component_id between", value1, value2, "componentId");
            return (Criteria) this;
        }

        public Criteria andComponentIdNotBetween(String value1, String value2) {
            addCriterion("component_id not between", value1, value2, "componentId");
            return (Criteria) this;
        }

        public Criteria andBoundingboxIsNull() {
            addCriterion("boundingbox is null");
            return (Criteria) this;
        }

        public Criteria andBoundingboxIsNotNull() {
            addCriterion("boundingbox is not null");
            return (Criteria) this;
        }

        public Criteria andBoundingboxEqualTo(String value) {
            addCriterion("boundingbox =", value, "boundingbox");
            return (Criteria) this;
        }

        public Criteria andBoundingboxNotEqualTo(String value) {
            addCriterion("boundingbox <>", value, "boundingbox");
            return (Criteria) this;
        }

        public Criteria andBoundingboxGreaterThan(String value) {
            addCriterion("boundingbox >", value, "boundingbox");
            return (Criteria) this;
        }

        public Criteria andBoundingboxGreaterThanOrEqualTo(String value) {
            addCriterion("boundingbox >=", value, "boundingbox");
            return (Criteria) this;
        }

        public Criteria andBoundingboxLessThan(String value) {
            addCriterion("boundingbox <", value, "boundingbox");
            return (Criteria) this;
        }

        public Criteria andBoundingboxLessThanOrEqualTo(String value) {
            addCriterion("boundingbox <=", value, "boundingbox");
            return (Criteria) this;
        }

        public Criteria andBoundingboxLike(String value) {
            addCriterion("boundingbox like", value, "boundingbox");
            return (Criteria) this;
        }

        public Criteria andBoundingboxNotLike(String value) {
            addCriterion("boundingbox not like", value, "boundingbox");
            return (Criteria) this;
        }

        public Criteria andBoundingboxIn(List<String> values) {
            addCriterion("boundingbox in", values, "boundingbox");
            return (Criteria) this;
        }

        public Criteria andBoundingboxNotIn(List<String> values) {
            addCriterion("boundingbox not in", values, "boundingbox");
            return (Criteria) this;
        }

        public Criteria andBoundingboxBetween(String value1, String value2) {
            addCriterion("boundingbox between", value1, value2, "boundingbox");
            return (Criteria) this;
        }

        public Criteria andBoundingboxNotBetween(String value1, String value2) {
            addCriterion("boundingbox not between", value1, value2, "boundingbox");
            return (Criteria) this;
        }

        public Criteria andTopologyelementsIsNull() {
            addCriterion("topologyelements is null");
            return (Criteria) this;
        }

        public Criteria andTopologyelementsIsNotNull() {
            addCriterion("topologyelements is not null");
            return (Criteria) this;
        }

        public Criteria andTopologyelementsEqualTo(String value) {
            addCriterion("topologyelements =", value, "topologyelements");
            return (Criteria) this;
        }

        public Criteria andTopologyelementsNotEqualTo(String value) {
            addCriterion("topologyelements <>", value, "topologyelements");
            return (Criteria) this;
        }

        public Criteria andTopologyelementsGreaterThan(String value) {
            addCriterion("topologyelements >", value, "topologyelements");
            return (Criteria) this;
        }

        public Criteria andTopologyelementsGreaterThanOrEqualTo(String value) {
            addCriterion("topologyelements >=", value, "topologyelements");
            return (Criteria) this;
        }

        public Criteria andTopologyelementsLessThan(String value) {
            addCriterion("topologyelements <", value, "topologyelements");
            return (Criteria) this;
        }

        public Criteria andTopologyelementsLessThanOrEqualTo(String value) {
            addCriterion("topologyelements <=", value, "topologyelements");
            return (Criteria) this;
        }

        public Criteria andTopologyelementsLike(String value) {
            addCriterion("topologyelements like", value, "topologyelements");
            return (Criteria) this;
        }

        public Criteria andTopologyelementsNotLike(String value) {
            addCriterion("topologyelements not like", value, "topologyelements");
            return (Criteria) this;
        }

        public Criteria andTopologyelementsIn(List<String> values) {
            addCriterion("topologyelements in", values, "topologyelements");
            return (Criteria) this;
        }

        public Criteria andTopologyelementsNotIn(List<String> values) {
            addCriterion("topologyelements not in", values, "topologyelements");
            return (Criteria) this;
        }

        public Criteria andTopologyelementsBetween(String value1, String value2) {
            addCriterion("topologyelements between", value1, value2, "topologyelements");
            return (Criteria) this;
        }

        public Criteria andTopologyelementsNotBetween(String value1, String value2) {
            addCriterion("topologyelements not between", value1, value2, "topologyelements");
            return (Criteria) this;
        }

        public Criteria andObbCenterIsNull() {
            addCriterion("obb_center is null");
            return (Criteria) this;
        }

        public Criteria andObbCenterIsNotNull() {
            addCriterion("obb_center is not null");
            return (Criteria) this;
        }

        public Criteria andObbCenterEqualTo(String value) {
            addCriterion("obb_center =", value, "obbCenter");
            return (Criteria) this;
        }

        public Criteria andObbCenterNotEqualTo(String value) {
            addCriterion("obb_center <>", value, "obbCenter");
            return (Criteria) this;
        }

        public Criteria andObbCenterGreaterThan(String value) {
            addCriterion("obb_center >", value, "obbCenter");
            return (Criteria) this;
        }

        public Criteria andObbCenterGreaterThanOrEqualTo(String value) {
            addCriterion("obb_center >=", value, "obbCenter");
            return (Criteria) this;
        }

        public Criteria andObbCenterLessThan(String value) {
            addCriterion("obb_center <", value, "obbCenter");
            return (Criteria) this;
        }

        public Criteria andObbCenterLessThanOrEqualTo(String value) {
            addCriterion("obb_center <=", value, "obbCenter");
            return (Criteria) this;
        }

        public Criteria andObbCenterLike(String value) {
            addCriterion("obb_center like", value, "obbCenter");
            return (Criteria) this;
        }

        public Criteria andObbCenterNotLike(String value) {
            addCriterion("obb_center not like", value, "obbCenter");
            return (Criteria) this;
        }

        public Criteria andObbCenterIn(List<String> values) {
            addCriterion("obb_center in", values, "obbCenter");
            return (Criteria) this;
        }

        public Criteria andObbCenterNotIn(List<String> values) {
            addCriterion("obb_center not in", values, "obbCenter");
            return (Criteria) this;
        }

        public Criteria andObbCenterBetween(String value1, String value2) {
            addCriterion("obb_center between", value1, value2, "obbCenter");
            return (Criteria) this;
        }

        public Criteria andObbCenterNotBetween(String value1, String value2) {
            addCriterion("obb_center not between", value1, value2, "obbCenter");
            return (Criteria) this;
        }

        public Criteria andSubtractIsNull() {
            addCriterion("subtract is null");
            return (Criteria) this;
        }

        public Criteria andSubtractIsNotNull() {
            addCriterion("subtract is not null");
            return (Criteria) this;
        }

        public Criteria andSubtractEqualTo(String value) {
            addCriterion("subtract =", value, "subtract");
            return (Criteria) this;
        }

        public Criteria andSubtractNotEqualTo(String value) {
            addCriterion("subtract <>", value, "subtract");
            return (Criteria) this;
        }

        public Criteria andSubtractGreaterThan(String value) {
            addCriterion("subtract >", value, "subtract");
            return (Criteria) this;
        }

        public Criteria andSubtractGreaterThanOrEqualTo(String value) {
            addCriterion("subtract >=", value, "subtract");
            return (Criteria) this;
        }

        public Criteria andSubtractLessThan(String value) {
            addCriterion("subtract <", value, "subtract");
            return (Criteria) this;
        }

        public Criteria andSubtractLessThanOrEqualTo(String value) {
            addCriterion("subtract <=", value, "subtract");
            return (Criteria) this;
        }

        public Criteria andSubtractLike(String value) {
            addCriterion("subtract like", value, "subtract");
            return (Criteria) this;
        }

        public Criteria andSubtractNotLike(String value) {
            addCriterion("subtract not like", value, "subtract");
            return (Criteria) this;
        }

        public Criteria andSubtractIn(List<String> values) {
            addCriterion("subtract in", values, "subtract");
            return (Criteria) this;
        }

        public Criteria andSubtractNotIn(List<String> values) {
            addCriterion("subtract not in", values, "subtract");
            return (Criteria) this;
        }

        public Criteria andSubtractBetween(String value1, String value2) {
            addCriterion("subtract between", value1, value2, "subtract");
            return (Criteria) this;
        }

        public Criteria andSubtractNotBetween(String value1, String value2) {
            addCriterion("subtract not between", value1, value2, "subtract");
            return (Criteria) this;
        }

        public Criteria andRelationIdsIsNull() {
            addCriterion("relation_ids is null");
            return (Criteria) this;
        }

        public Criteria andRelationIdsIsNotNull() {
            addCriterion("relation_ids is not null");
            return (Criteria) this;
        }

        public Criteria andRelationIdsEqualTo(String value) {
            addCriterion("relation_ids =", value, "relationIds");
            return (Criteria) this;
        }

        public Criteria andRelationIdsNotEqualTo(String value) {
            addCriterion("relation_ids <>", value, "relationIds");
            return (Criteria) this;
        }

        public Criteria andRelationIdsGreaterThan(String value) {
            addCriterion("relation_ids >", value, "relationIds");
            return (Criteria) this;
        }

        public Criteria andRelationIdsGreaterThanOrEqualTo(String value) {
            addCriterion("relation_ids >=", value, "relationIds");
            return (Criteria) this;
        }

        public Criteria andRelationIdsLessThan(String value) {
            addCriterion("relation_ids <", value, "relationIds");
            return (Criteria) this;
        }

        public Criteria andRelationIdsLessThanOrEqualTo(String value) {
            addCriterion("relation_ids <=", value, "relationIds");
            return (Criteria) this;
        }

        public Criteria andRelationIdsLike(String value) {
            addCriterion("relation_ids like", value, "relationIds");
            return (Criteria) this;
        }

        public Criteria andRelationIdsNotLike(String value) {
            addCriterion("relation_ids not like", value, "relationIds");
            return (Criteria) this;
        }

        public Criteria andRelationIdsIn(List<String> values) {
            addCriterion("relation_ids in", values, "relationIds");
            return (Criteria) this;
        }

        public Criteria andRelationIdsNotIn(List<String> values) {
            addCriterion("relation_ids not in", values, "relationIds");
            return (Criteria) this;
        }

        public Criteria andRelationIdsBetween(String value1, String value2) {
            addCriterion("relation_ids between", value1, value2, "relationIds");
            return (Criteria) this;
        }

        public Criteria andRelationIdsNotBetween(String value1, String value2) {
            addCriterion("relation_ids not between", value1, value2, "relationIds");
            return (Criteria) this;
        }

        public Criteria andMeasureIsNull() {
            addCriterion("measure is null");
            return (Criteria) this;
        }

        public Criteria andMeasureIsNotNull() {
            addCriterion("measure is not null");
            return (Criteria) this;
        }

        public Criteria andMeasureEqualTo(String value) {
            addCriterion("measure =", value, "measure");
            return (Criteria) this;
        }

        public Criteria andMeasureNotEqualTo(String value) {
            addCriterion("measure <>", value, "measure");
            return (Criteria) this;
        }

        public Criteria andMeasureGreaterThan(String value) {
            addCriterion("measure >", value, "measure");
            return (Criteria) this;
        }

        public Criteria andMeasureGreaterThanOrEqualTo(String value) {
            addCriterion("measure >=", value, "measure");
            return (Criteria) this;
        }

        public Criteria andMeasureLessThan(String value) {
            addCriterion("measure <", value, "measure");
            return (Criteria) this;
        }

        public Criteria andMeasureLessThanOrEqualTo(String value) {
            addCriterion("measure <=", value, "measure");
            return (Criteria) this;
        }

        public Criteria andMeasureLike(String value) {
            addCriterion("measure like", value, "measure");
            return (Criteria) this;
        }

        public Criteria andMeasureNotLike(String value) {
            addCriterion("measure not like", value, "measure");
            return (Criteria) this;
        }

        public Criteria andMeasureIn(List<String> values) {
            addCriterion("measure in", values, "measure");
            return (Criteria) this;
        }

        public Criteria andMeasureNotIn(List<String> values) {
            addCriterion("measure not in", values, "measure");
            return (Criteria) this;
        }

        public Criteria andMeasureBetween(String value1, String value2) {
            addCriterion("measure between", value1, value2, "measure");
            return (Criteria) this;
        }

        public Criteria andMeasureNotBetween(String value1, String value2) {
            addCriterion("measure not between", value1, value2, "measure");
            return (Criteria) this;
        }

        public Criteria andSizeIsNull() {
            addCriterion("size is null");
            return (Criteria) this;
        }

        public Criteria andSizeIsNotNull() {
            addCriterion("size is not null");
            return (Criteria) this;
        }

        public Criteria andSizeEqualTo(String value) {
            addCriterion("size =", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotEqualTo(String value) {
            addCriterion("size <>", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeGreaterThan(String value) {
            addCriterion("size >", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeGreaterThanOrEqualTo(String value) {
            addCriterion("size >=", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLessThan(String value) {
            addCriterion("size <", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLessThanOrEqualTo(String value) {
            addCriterion("size <=", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLike(String value) {
            addCriterion("size like", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotLike(String value) {
            addCriterion("size not like", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeIn(List<String> values) {
            addCriterion("size in", values, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotIn(List<String> values) {
            addCriterion("size not in", values, "size");
            return (Criteria) this;
        }

        public Criteria andSizeBetween(String value1, String value2) {
            addCriterion("size between", value1, value2, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotBetween(String value1, String value2) {
            addCriterion("size not between", value1, value2, "size");
            return (Criteria) this;
        }

        public Criteria andClipMatrixIsNull() {
            addCriterion("clip_matrix is null");
            return (Criteria) this;
        }

        public Criteria andClipMatrixIsNotNull() {
            addCriterion("clip_matrix is not null");
            return (Criteria) this;
        }

        public Criteria andClipMatrixEqualTo(String value) {
            addCriterion("clip_matrix =", value, "clipMatrix");
            return (Criteria) this;
        }

        public Criteria andClipMatrixNotEqualTo(String value) {
            addCriterion("clip_matrix <>", value, "clipMatrix");
            return (Criteria) this;
        }

        public Criteria andClipMatrixGreaterThan(String value) {
            addCriterion("clip_matrix >", value, "clipMatrix");
            return (Criteria) this;
        }

        public Criteria andClipMatrixGreaterThanOrEqualTo(String value) {
            addCriterion("clip_matrix >=", value, "clipMatrix");
            return (Criteria) this;
        }

        public Criteria andClipMatrixLessThan(String value) {
            addCriterion("clip_matrix <", value, "clipMatrix");
            return (Criteria) this;
        }

        public Criteria andClipMatrixLessThanOrEqualTo(String value) {
            addCriterion("clip_matrix <=", value, "clipMatrix");
            return (Criteria) this;
        }

        public Criteria andClipMatrixLike(String value) {
            addCriterion("clip_matrix like", value, "clipMatrix");
            return (Criteria) this;
        }

        public Criteria andClipMatrixNotLike(String value) {
            addCriterion("clip_matrix not like", value, "clipMatrix");
            return (Criteria) this;
        }

        public Criteria andClipMatrixIn(List<String> values) {
            addCriterion("clip_matrix in", values, "clipMatrix");
            return (Criteria) this;
        }

        public Criteria andClipMatrixNotIn(List<String> values) {
            addCriterion("clip_matrix not in", values, "clipMatrix");
            return (Criteria) this;
        }

        public Criteria andClipMatrixBetween(String value1, String value2) {
            addCriterion("clip_matrix between", value1, value2, "clipMatrix");
            return (Criteria) this;
        }

        public Criteria andClipMatrixNotBetween(String value1, String value2) {
            addCriterion("clip_matrix not between", value1, value2, "clipMatrix");
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