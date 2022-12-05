package com.augurit.agcloud.agcom.agsupport.domain.auto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @Author: qinyg
 * @Date: 2020/12/17
 * @tips: example类
 */
public class AgHouseExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AgHouseExample() {
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

        public Criteria andSourceNameIsNull() {
            addCriterion("source_name is null");
            return (Criteria) this;
        }

        public Criteria andSourceNameIsNotNull() {
            addCriterion("source_name is not null");
            return (Criteria) this;
        }

        public Criteria andSourceNameEqualTo(String value) {
            addCriterion("source_name =", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameNotEqualTo(String value) {
            addCriterion("source_name <>", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameGreaterThan(String value) {
            addCriterion("source_name >", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameGreaterThanOrEqualTo(String value) {
            addCriterion("source_name >=", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameLessThan(String value) {
            addCriterion("source_name <", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameLessThanOrEqualTo(String value) {
            addCriterion("source_name <=", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameLike(String value) {
            addCriterion("source_name like", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameNotLike(String value) {
            addCriterion("source_name not like", value, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameIn(List<String> values) {
            addCriterion("source_name in", values, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameNotIn(List<String> values) {
            addCriterion("source_name not in", values, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameBetween(String value1, String value2) {
            addCriterion("source_name between", value1, value2, "sourceName");
            return (Criteria) this;
        }

        public Criteria andSourceNameNotBetween(String value1, String value2) {
            addCriterion("source_name not between", value1, value2, "sourceName");
            return (Criteria) this;
        }

        public Criteria andStoreFullPathIsNull() {
            addCriterion("store_full_path is null");
            return (Criteria) this;
        }

        public Criteria andStoreFullPathIsNotNull() {
            addCriterion("store_full_path is not null");
            return (Criteria) this;
        }

        public Criteria andStoreFullPathEqualTo(String value) {
            addCriterion("store_full_path =", value, "storeFullPath");
            return (Criteria) this;
        }

        public Criteria andStoreFullPathNotEqualTo(String value) {
            addCriterion("store_full_path <>", value, "storeFullPath");
            return (Criteria) this;
        }

        public Criteria andStoreFullPathGreaterThan(String value) {
            addCriterion("store_full_path >", value, "storeFullPath");
            return (Criteria) this;
        }

        public Criteria andStoreFullPathGreaterThanOrEqualTo(String value) {
            addCriterion("store_full_path >=", value, "storeFullPath");
            return (Criteria) this;
        }

        public Criteria andStoreFullPathLessThan(String value) {
            addCriterion("store_full_path <", value, "storeFullPath");
            return (Criteria) this;
        }

        public Criteria andStoreFullPathLessThanOrEqualTo(String value) {
            addCriterion("store_full_path <=", value, "storeFullPath");
            return (Criteria) this;
        }

        public Criteria andStoreFullPathLike(String value) {
            addCriterion("store_full_path like", value, "storeFullPath");
            return (Criteria) this;
        }

        public Criteria andStoreFullPathNotLike(String value) {
            addCriterion("store_full_path not like", value, "storeFullPath");
            return (Criteria) this;
        }

        public Criteria andStoreFullPathIn(List<String> values) {
            addCriterion("store_full_path in", values, "storeFullPath");
            return (Criteria) this;
        }

        public Criteria andStoreFullPathNotIn(List<String> values) {
            addCriterion("store_full_path not in", values, "storeFullPath");
            return (Criteria) this;
        }

        public Criteria andStoreFullPathBetween(String value1, String value2) {
            addCriterion("store_full_path between", value1, value2, "storeFullPath");
            return (Criteria) this;
        }

        public Criteria andStoreFullPathNotBetween(String value1, String value2) {
            addCriterion("store_full_path not between", value1, value2, "storeFullPath");
            return (Criteria) this;
        }

        public Criteria andSuffixIsNull() {
            addCriterion("suffix is null");
            return (Criteria) this;
        }

        public Criteria andSuffixIsNotNull() {
            addCriterion("suffix is not null");
            return (Criteria) this;
        }

        public Criteria andSuffixEqualTo(String value) {
            addCriterion("suffix =", value, "suffix");
            return (Criteria) this;
        }

        public Criteria andSuffixNotEqualTo(String value) {
            addCriterion("suffix <>", value, "suffix");
            return (Criteria) this;
        }

        public Criteria andSuffixGreaterThan(String value) {
            addCriterion("suffix >", value, "suffix");
            return (Criteria) this;
        }

        public Criteria andSuffixGreaterThanOrEqualTo(String value) {
            addCriterion("suffix >=", value, "suffix");
            return (Criteria) this;
        }

        public Criteria andSuffixLessThan(String value) {
            addCriterion("suffix <", value, "suffix");
            return (Criteria) this;
        }

        public Criteria andSuffixLessThanOrEqualTo(String value) {
            addCriterion("suffix <=", value, "suffix");
            return (Criteria) this;
        }

        public Criteria andSuffixLike(String value) {
            addCriterion("suffix like", value, "suffix");
            return (Criteria) this;
        }

        public Criteria andSuffixNotLike(String value) {
            addCriterion("suffix not like", value, "suffix");
            return (Criteria) this;
        }

        public Criteria andSuffixIn(List<String> values) {
            addCriterion("suffix in", values, "suffix");
            return (Criteria) this;
        }

        public Criteria andSuffixNotIn(List<String> values) {
            addCriterion("suffix not in", values, "suffix");
            return (Criteria) this;
        }

        public Criteria andSuffixBetween(String value1, String value2) {
            addCriterion("suffix between", value1, value2, "suffix");
            return (Criteria) this;
        }

        public Criteria andSuffixNotBetween(String value1, String value2) {
            addCriterion("suffix not between", value1, value2, "suffix");
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

        public Criteria andCategoryIdIsNull() {
            addCriterion("category_id is null");
            return (Criteria) this;
        }

        public Criteria andCategoryIdIsNotNull() {
            addCriterion("category_id is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryIdEqualTo(String value) {
            addCriterion("category_id =", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdNotEqualTo(String value) {
            addCriterion("category_id <>", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdGreaterThan(String value) {
            addCriterion("category_id >", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdGreaterThanOrEqualTo(String value) {
            addCriterion("category_id >=", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdLessThan(String value) {
            addCriterion("category_id <", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdLessThanOrEqualTo(String value) {
            addCriterion("category_id <=", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdLike(String value) {
            addCriterion("category_id like", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdNotLike(String value) {
            addCriterion("category_id not like", value, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdIn(List<String> values) {
            addCriterion("category_id in", values, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdNotIn(List<String> values) {
            addCriterion("category_id not in", values, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdBetween(String value1, String value2) {
            addCriterion("category_id between", value1, value2, "categoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryIdNotBetween(String value1, String value2) {
            addCriterion("category_id not between", value1, value2, "categoryId");
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

        public Criteria andOldNameIsNull() {
            addCriterion("old_name is null");
            return (Criteria) this;
        }

        public Criteria andOldNameIsNotNull() {
            addCriterion("old_name is not null");
            return (Criteria) this;
        }

        public Criteria andOldNameEqualTo(String value) {
            addCriterion("old_name =", value, "oldName");
            return (Criteria) this;
        }

        public Criteria andOldNameNotEqualTo(String value) {
            addCriterion("old_name <>", value, "oldName");
            return (Criteria) this;
        }

        public Criteria andOldNameGreaterThan(String value) {
            addCriterion("old_name >", value, "oldName");
            return (Criteria) this;
        }

        public Criteria andOldNameGreaterThanOrEqualTo(String value) {
            addCriterion("old_name >=", value, "oldName");
            return (Criteria) this;
        }

        public Criteria andOldNameLessThan(String value) {
            addCriterion("old_name <", value, "oldName");
            return (Criteria) this;
        }

        public Criteria andOldNameLessThanOrEqualTo(String value) {
            addCriterion("old_name <=", value, "oldName");
            return (Criteria) this;
        }

        public Criteria andOldNameLike(String value) {
            addCriterion("old_name like", value, "oldName");
            return (Criteria) this;
        }

        public Criteria andOldNameNotLike(String value) {
            addCriterion("old_name not like", value, "oldName");
            return (Criteria) this;
        }

        public Criteria andOldNameIn(List<String> values) {
            addCriterion("old_name in", values, "oldName");
            return (Criteria) this;
        }

        public Criteria andOldNameNotIn(List<String> values) {
            addCriterion("old_name not in", values, "oldName");
            return (Criteria) this;
        }

        public Criteria andOldNameBetween(String value1, String value2) {
            addCriterion("old_name between", value1, value2, "oldName");
            return (Criteria) this;
        }

        public Criteria andOldNameNotBetween(String value1, String value2) {
            addCriterion("old_name not between", value1, value2, "oldName");
            return (Criteria) this;
        }

        public Criteria andHourseNameIsNull() {
            addCriterion("hourse_name is null");
            return (Criteria) this;
        }

        public Criteria andHourseNameIsNotNull() {
            addCriterion("hourse_name is not null");
            return (Criteria) this;
        }

        public Criteria andHourseNameEqualTo(String value) {
            addCriterion("hourse_name =", value, "hourseName");
            return (Criteria) this;
        }

        public Criteria andHourseNameNotEqualTo(String value) {
            addCriterion("hourse_name <>", value, "hourseName");
            return (Criteria) this;
        }

        public Criteria andHourseNameGreaterThan(String value) {
            addCriterion("hourse_name >", value, "hourseName");
            return (Criteria) this;
        }

        public Criteria andHourseNameGreaterThanOrEqualTo(String value) {
            addCriterion("hourse_name >=", value, "hourseName");
            return (Criteria) this;
        }

        public Criteria andHourseNameLessThan(String value) {
            addCriterion("hourse_name <", value, "hourseName");
            return (Criteria) this;
        }

        public Criteria andHourseNameLessThanOrEqualTo(String value) {
            addCriterion("hourse_name <=", value, "hourseName");
            return (Criteria) this;
        }

        public Criteria andHourseNameLike(String value) {
            addCriterion("hourse_name like", value, "hourseName");
            return (Criteria) this;
        }

        public Criteria andHourseNameNotLike(String value) {
            addCriterion("hourse_name not like", value, "hourseName");
            return (Criteria) this;
        }

        public Criteria andHourseNameIn(List<String> values) {
            addCriterion("hourse_name in", values, "hourseName");
            return (Criteria) this;
        }

        public Criteria andHourseNameNotIn(List<String> values) {
            addCriterion("hourse_name not in", values, "hourseName");
            return (Criteria) this;
        }

        public Criteria andHourseNameBetween(String value1, String value2) {
            addCriterion("hourse_name between", value1, value2, "hourseName");
            return (Criteria) this;
        }

        public Criteria andHourseNameNotBetween(String value1, String value2) {
            addCriterion("hourse_name not between", value1, value2, "hourseName");
            return (Criteria) this;
        }

        public Criteria andHomesteadAreaIsNull() {
            addCriterion("homestead_area is null");
            return (Criteria) this;
        }

        public Criteria andHomesteadAreaIsNotNull() {
            addCriterion("homestead_area is not null");
            return (Criteria) this;
        }

        public Criteria andHomesteadAreaEqualTo(String value) {
            addCriterion("homestead_area =", value, "homesteadArea");
            return (Criteria) this;
        }

        public Criteria andHomesteadAreaNotEqualTo(String value) {
            addCriterion("homestead_area <>", value, "homesteadArea");
            return (Criteria) this;
        }

        public Criteria andHomesteadAreaGreaterThan(String value) {
            addCriterion("homestead_area >", value, "homesteadArea");
            return (Criteria) this;
        }

        public Criteria andHomesteadAreaGreaterThanOrEqualTo(String value) {
            addCriterion("homestead_area >=", value, "homesteadArea");
            return (Criteria) this;
        }

        public Criteria andHomesteadAreaLessThan(String value) {
            addCriterion("homestead_area <", value, "homesteadArea");
            return (Criteria) this;
        }

        public Criteria andHomesteadAreaLessThanOrEqualTo(String value) {
            addCriterion("homestead_area <=", value, "homesteadArea");
            return (Criteria) this;
        }

        public Criteria andHomesteadAreaLike(String value) {
            addCriterion("homestead_area like", value, "homesteadArea");
            return (Criteria) this;
        }

        public Criteria andHomesteadAreaNotLike(String value) {
            addCriterion("homestead_area not like", value, "homesteadArea");
            return (Criteria) this;
        }

        public Criteria andHomesteadAreaIn(List<String> values) {
            addCriterion("homestead_area in", values, "homesteadArea");
            return (Criteria) this;
        }

        public Criteria andHomesteadAreaNotIn(List<String> values) {
            addCriterion("homestead_area not in", values, "homesteadArea");
            return (Criteria) this;
        }

        public Criteria andHomesteadAreaBetween(String value1, String value2) {
            addCriterion("homestead_area between", value1, value2, "homesteadArea");
            return (Criteria) this;
        }

        public Criteria andHomesteadAreaNotBetween(String value1, String value2) {
            addCriterion("homestead_area not between", value1, value2, "homesteadArea");
            return (Criteria) this;
        }

        public Criteria andFloorAreaIsNull() {
            addCriterion("floor_area is null");
            return (Criteria) this;
        }

        public Criteria andFloorAreaIsNotNull() {
            addCriterion("floor_area is not null");
            return (Criteria) this;
        }

        public Criteria andFloorAreaEqualTo(String value) {
            addCriterion("floor_area =", value, "floorArea");
            return (Criteria) this;
        }

        public Criteria andFloorAreaNotEqualTo(String value) {
            addCriterion("floor_area <>", value, "floorArea");
            return (Criteria) this;
        }

        public Criteria andFloorAreaGreaterThan(String value) {
            addCriterion("floor_area >", value, "floorArea");
            return (Criteria) this;
        }

        public Criteria andFloorAreaGreaterThanOrEqualTo(String value) {
            addCriterion("floor_area >=", value, "floorArea");
            return (Criteria) this;
        }

        public Criteria andFloorAreaLessThan(String value) {
            addCriterion("floor_area <", value, "floorArea");
            return (Criteria) this;
        }

        public Criteria andFloorAreaLessThanOrEqualTo(String value) {
            addCriterion("floor_area <=", value, "floorArea");
            return (Criteria) this;
        }

        public Criteria andFloorAreaLike(String value) {
            addCriterion("floor_area like", value, "floorArea");
            return (Criteria) this;
        }

        public Criteria andFloorAreaNotLike(String value) {
            addCriterion("floor_area not like", value, "floorArea");
            return (Criteria) this;
        }

        public Criteria andFloorAreaIn(List<String> values) {
            addCriterion("floor_area in", values, "floorArea");
            return (Criteria) this;
        }

        public Criteria andFloorAreaNotIn(List<String> values) {
            addCriterion("floor_area not in", values, "floorArea");
            return (Criteria) this;
        }

        public Criteria andFloorAreaBetween(String value1, String value2) {
            addCriterion("floor_area between", value1, value2, "floorArea");
            return (Criteria) this;
        }

        public Criteria andFloorAreaNotBetween(String value1, String value2) {
            addCriterion("floor_area not between", value1, value2, "floorArea");
            return (Criteria) this;
        }

        public Criteria andCoveredAreaIsNull() {
            addCriterion("covered_area is null");
            return (Criteria) this;
        }

        public Criteria andCoveredAreaIsNotNull() {
            addCriterion("covered_area is not null");
            return (Criteria) this;
        }

        public Criteria andCoveredAreaEqualTo(String value) {
            addCriterion("covered_area =", value, "coveredArea");
            return (Criteria) this;
        }

        public Criteria andCoveredAreaNotEqualTo(String value) {
            addCriterion("covered_area <>", value, "coveredArea");
            return (Criteria) this;
        }

        public Criteria andCoveredAreaGreaterThan(String value) {
            addCriterion("covered_area >", value, "coveredArea");
            return (Criteria) this;
        }

        public Criteria andCoveredAreaGreaterThanOrEqualTo(String value) {
            addCriterion("covered_area >=", value, "coveredArea");
            return (Criteria) this;
        }

        public Criteria andCoveredAreaLessThan(String value) {
            addCriterion("covered_area <", value, "coveredArea");
            return (Criteria) this;
        }

        public Criteria andCoveredAreaLessThanOrEqualTo(String value) {
            addCriterion("covered_area <=", value, "coveredArea");
            return (Criteria) this;
        }

        public Criteria andCoveredAreaLike(String value) {
            addCriterion("covered_area like", value, "coveredArea");
            return (Criteria) this;
        }

        public Criteria andCoveredAreaNotLike(String value) {
            addCriterion("covered_area not like", value, "coveredArea");
            return (Criteria) this;
        }

        public Criteria andCoveredAreaIn(List<String> values) {
            addCriterion("covered_area in", values, "coveredArea");
            return (Criteria) this;
        }

        public Criteria andCoveredAreaNotIn(List<String> values) {
            addCriterion("covered_area not in", values, "coveredArea");
            return (Criteria) this;
        }

        public Criteria andCoveredAreaBetween(String value1, String value2) {
            addCriterion("covered_area between", value1, value2, "coveredArea");
            return (Criteria) this;
        }

        public Criteria andCoveredAreaNotBetween(String value1, String value2) {
            addCriterion("covered_area not between", value1, value2, "coveredArea");
            return (Criteria) this;
        }

        public Criteria andCostEstimatesIsNull() {
            addCriterion("cost_estimates is null");
            return (Criteria) this;
        }

        public Criteria andCostEstimatesIsNotNull() {
            addCriterion("cost_estimates is not null");
            return (Criteria) this;
        }

        public Criteria andCostEstimatesEqualTo(String value) {
            addCriterion("cost_estimates =", value, "costEstimates");
            return (Criteria) this;
        }

        public Criteria andCostEstimatesNotEqualTo(String value) {
            addCriterion("cost_estimates <>", value, "costEstimates");
            return (Criteria) this;
        }

        public Criteria andCostEstimatesGreaterThan(String value) {
            addCriterion("cost_estimates >", value, "costEstimates");
            return (Criteria) this;
        }

        public Criteria andCostEstimatesGreaterThanOrEqualTo(String value) {
            addCriterion("cost_estimates >=", value, "costEstimates");
            return (Criteria) this;
        }

        public Criteria andCostEstimatesLessThan(String value) {
            addCriterion("cost_estimates <", value, "costEstimates");
            return (Criteria) this;
        }

        public Criteria andCostEstimatesLessThanOrEqualTo(String value) {
            addCriterion("cost_estimates <=", value, "costEstimates");
            return (Criteria) this;
        }

        public Criteria andCostEstimatesLike(String value) {
            addCriterion("cost_estimates like", value, "costEstimates");
            return (Criteria) this;
        }

        public Criteria andCostEstimatesNotLike(String value) {
            addCriterion("cost_estimates not like", value, "costEstimates");
            return (Criteria) this;
        }

        public Criteria andCostEstimatesIn(List<String> values) {
            addCriterion("cost_estimates in", values, "costEstimates");
            return (Criteria) this;
        }

        public Criteria andCostEstimatesNotIn(List<String> values) {
            addCriterion("cost_estimates not in", values, "costEstimates");
            return (Criteria) this;
        }

        public Criteria andCostEstimatesBetween(String value1, String value2) {
            addCriterion("cost_estimates between", value1, value2, "costEstimates");
            return (Criteria) this;
        }

        public Criteria andCostEstimatesNotBetween(String value1, String value2) {
            addCriterion("cost_estimates not between", value1, value2, "costEstimates");
            return (Criteria) this;
        }

        public Criteria andIsShowIsNull() {
            addCriterion("is_show is null");
            return (Criteria) this;
        }

        public Criteria andIsShowIsNotNull() {
            addCriterion("is_show is not null");
            return (Criteria) this;
        }

        public Criteria andIsShowEqualTo(String value) {
            addCriterion("is_show =", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowNotEqualTo(String value) {
            addCriterion("is_show <>", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowGreaterThan(String value) {
            addCriterion("is_show >", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowGreaterThanOrEqualTo(String value) {
            addCriterion("is_show >=", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowLessThan(String value) {
            addCriterion("is_show <", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowLessThanOrEqualTo(String value) {
            addCriterion("is_show <=", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowLike(String value) {
            addCriterion("is_show like", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowNotLike(String value) {
            addCriterion("is_show not like", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowIn(List<String> values) {
            addCriterion("is_show in", values, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowNotIn(List<String> values) {
            addCriterion("is_show not in", values, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowBetween(String value1, String value2) {
            addCriterion("is_show between", value1, value2, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowNotBetween(String value1, String value2) {
            addCriterion("is_show not between", value1, value2, "isShow");
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

        public Criteria andThumbIsNull() {
            addCriterion("thumb is null");
            return (Criteria) this;
        }

        public Criteria andThumbIsNotNull() {
            addCriterion("thumb is not null");
            return (Criteria) this;
        }

        public Criteria andThumbEqualTo(String value) {
            addCriterion("thumb =", value, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbNotEqualTo(String value) {
            addCriterion("thumb <>", value, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbGreaterThan(String value) {
            addCriterion("thumb >", value, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbGreaterThanOrEqualTo(String value) {
            addCriterion("thumb >=", value, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbLessThan(String value) {
            addCriterion("thumb <", value, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbLessThanOrEqualTo(String value) {
            addCriterion("thumb <=", value, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbLike(String value) {
            addCriterion("thumb like", value, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbNotLike(String value) {
            addCriterion("thumb not like", value, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbIn(List<String> values) {
            addCriterion("thumb in", values, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbNotIn(List<String> values) {
            addCriterion("thumb not in", values, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbBetween(String value1, String value2) {
            addCriterion("thumb between", value1, value2, "thumb");
            return (Criteria) this;
        }

        public Criteria andThumbNotBetween(String value1, String value2) {
            addCriterion("thumb not between", value1, value2, "thumb");
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

        public Criteria andStructureTypeIsNull() {
            addCriterion("structure_type is null");
            return (Criteria) this;
        }

        public Criteria andStructureTypeIsNotNull() {
            addCriterion("structure_type is not null");
            return (Criteria) this;
        }

        public Criteria andStructureTypeEqualTo(String value) {
            addCriterion("structure_type =", value, "structureType");
            return (Criteria) this;
        }

        public Criteria andStructureTypeNotEqualTo(String value) {
            addCriterion("structure_type <>", value, "structureType");
            return (Criteria) this;
        }

        public Criteria andStructureTypeGreaterThan(String value) {
            addCriterion("structure_type >", value, "structureType");
            return (Criteria) this;
        }

        public Criteria andStructureTypeGreaterThanOrEqualTo(String value) {
            addCriterion("structure_type >=", value, "structureType");
            return (Criteria) this;
        }

        public Criteria andStructureTypeLessThan(String value) {
            addCriterion("structure_type <", value, "structureType");
            return (Criteria) this;
        }

        public Criteria andStructureTypeLessThanOrEqualTo(String value) {
            addCriterion("structure_type <=", value, "structureType");
            return (Criteria) this;
        }

        public Criteria andStructureTypeLike(String value) {
            addCriterion("structure_type like", value, "structureType");
            return (Criteria) this;
        }

        public Criteria andStructureTypeNotLike(String value) {
            addCriterion("structure_type not like", value, "structureType");
            return (Criteria) this;
        }

        public Criteria andStructureTypeIn(List<String> values) {
            addCriterion("structure_type in", values, "structureType");
            return (Criteria) this;
        }

        public Criteria andStructureTypeNotIn(List<String> values) {
            addCriterion("structure_type not in", values, "structureType");
            return (Criteria) this;
        }

        public Criteria andStructureTypeBetween(String value1, String value2) {
            addCriterion("structure_type between", value1, value2, "structureType");
            return (Criteria) this;
        }

        public Criteria andStructureTypeNotBetween(String value1, String value2) {
            addCriterion("structure_type not between", value1, value2, "structureType");
            return (Criteria) this;
        }

        public Criteria andModelSizeIsNull() {
            addCriterion("model_size is null");
            return (Criteria) this;
        }

        public Criteria andModelSizeIsNotNull() {
            addCriterion("model_size is not null");
            return (Criteria) this;
        }

        public Criteria andModelSizeEqualTo(String value) {
            addCriterion("model_size =", value, "modelSize");
            return (Criteria) this;
        }

        public Criteria andModelSizeNotEqualTo(String value) {
            addCriterion("model_size <>", value, "modelSize");
            return (Criteria) this;
        }

        public Criteria andModelSizeGreaterThan(String value) {
            addCriterion("model_size >", value, "modelSize");
            return (Criteria) this;
        }

        public Criteria andModelSizeGreaterThanOrEqualTo(String value) {
            addCriterion("model_size >=", value, "modelSize");
            return (Criteria) this;
        }

        public Criteria andModelSizeLessThan(String value) {
            addCriterion("model_size <", value, "modelSize");
            return (Criteria) this;
        }

        public Criteria andModelSizeLessThanOrEqualTo(String value) {
            addCriterion("model_size <=", value, "modelSize");
            return (Criteria) this;
        }

        public Criteria andModelSizeLike(String value) {
            addCriterion("model_size like", value, "modelSize");
            return (Criteria) this;
        }

        public Criteria andModelSizeNotLike(String value) {
            addCriterion("model_size not like", value, "modelSize");
            return (Criteria) this;
        }

        public Criteria andModelSizeIn(List<String> values) {
            addCriterion("model_size in", values, "modelSize");
            return (Criteria) this;
        }

        public Criteria andModelSizeNotIn(List<String> values) {
            addCriterion("model_size not in", values, "modelSize");
            return (Criteria) this;
        }

        public Criteria andModelSizeBetween(String value1, String value2) {
            addCriterion("model_size between", value1, value2, "modelSize");
            return (Criteria) this;
        }

        public Criteria andModelSizeNotBetween(String value1, String value2) {
            addCriterion("model_size not between", value1, value2, "modelSize");
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

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andComponentCodeIsNull() {
            addCriterion("component_code is null");
            return (Criteria) this;
        }

        public Criteria andComponentCodeIsNotNull() {
            addCriterion("component_code is not null");
            return (Criteria) this;
        }

        public Criteria andComponentCodeEqualTo(String value) {
            addCriterion("component_code =", value, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNotEqualTo(String value) {
            addCriterion("component_code <>", value, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeGreaterThan(String value) {
            addCriterion("component_code >", value, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeGreaterThanOrEqualTo(String value) {
            addCriterion("component_code >=", value, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeLessThan(String value) {
            addCriterion("component_code <", value, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeLessThanOrEqualTo(String value) {
            addCriterion("component_code <=", value, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeLike(String value) {
            addCriterion("component_code like", value, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNotLike(String value) {
            addCriterion("component_code not like", value, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeIn(List<String> values) {
            addCriterion("component_code in", values, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNotIn(List<String> values) {
            addCriterion("component_code not in", values, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeBetween(String value1, String value2) {
            addCriterion("component_code between", value1, value2, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNotBetween(String value1, String value2) {
            addCriterion("component_code not between", value1, value2, "componentCode");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNameIsNull() {
            addCriterion("component_code_name is null");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNameIsNotNull() {
            addCriterion("component_code_name is not null");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNameEqualTo(String value) {
            addCriterion("component_code_name =", value, "componentCodeName");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNameNotEqualTo(String value) {
            addCriterion("component_code_name <>", value, "componentCodeName");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNameGreaterThan(String value) {
            addCriterion("component_code_name >", value, "componentCodeName");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNameGreaterThanOrEqualTo(String value) {
            addCriterion("component_code_name >=", value, "componentCodeName");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNameLessThan(String value) {
            addCriterion("component_code_name <", value, "componentCodeName");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNameLessThanOrEqualTo(String value) {
            addCriterion("component_code_name <=", value, "componentCodeName");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNameLike(String value) {
            addCriterion("component_code_name like", value, "componentCodeName");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNameNotLike(String value) {
            addCriterion("component_code_name not like", value, "componentCodeName");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNameIn(List<String> values) {
            addCriterion("component_code_name in", values, "componentCodeName");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNameNotIn(List<String> values) {
            addCriterion("component_code_name not in", values, "componentCodeName");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNameBetween(String value1, String value2) {
            addCriterion("component_code_name between", value1, value2, "componentCodeName");
            return (Criteria) this;
        }

        public Criteria andComponentCodeNameNotBetween(String value1, String value2) {
            addCriterion("component_code_name not between", value1, value2, "componentCodeName");
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