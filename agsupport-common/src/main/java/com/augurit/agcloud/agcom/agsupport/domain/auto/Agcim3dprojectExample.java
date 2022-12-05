package com.augurit.agcloud.agcom.agsupport.domain.auto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Author: libc
 * @Date: 2020/12/10
 * @tips: example类
 */
public class Agcim3dprojectExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public Agcim3dprojectExample() {
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

        public Criteria andUnitsIsNull() {
            addCriterion("units is null");
            return (Criteria) this;
        }

        public Criteria andUnitsIsNotNull() {
            addCriterion("units is not null");
            return (Criteria) this;
        }

        public Criteria andUnitsEqualTo(String value) {
            addCriterion("units =", value, "units");
            return (Criteria) this;
        }

        public Criteria andUnitsNotEqualTo(String value) {
            addCriterion("units <>", value, "units");
            return (Criteria) this;
        }

        public Criteria andUnitsGreaterThan(String value) {
            addCriterion("units >", value, "units");
            return (Criteria) this;
        }

        public Criteria andUnitsGreaterThanOrEqualTo(String value) {
            addCriterion("units >=", value, "units");
            return (Criteria) this;
        }

        public Criteria andUnitsLessThan(String value) {
            addCriterion("units <", value, "units");
            return (Criteria) this;
        }

        public Criteria andUnitsLessThanOrEqualTo(String value) {
            addCriterion("units <=", value, "units");
            return (Criteria) this;
        }

        public Criteria andUnitsLike(String value) {
            addCriterion("units like", value, "units");
            return (Criteria) this;
        }

        public Criteria andUnitsNotLike(String value) {
            addCriterion("units not like", value, "units");
            return (Criteria) this;
        }

        public Criteria andUnitsIn(List<String> values) {
            addCriterion("units in", values, "units");
            return (Criteria) this;
        }

        public Criteria andUnitsNotIn(List<String> values) {
            addCriterion("units not in", values, "units");
            return (Criteria) this;
        }

        public Criteria andUnitsBetween(String value1, String value2) {
            addCriterion("units between", value1, value2, "units");
            return (Criteria) this;
        }

        public Criteria andUnitsNotBetween(String value1, String value2) {
            addCriterion("units not between", value1, value2, "units");
            return (Criteria) this;
        }

        public Criteria andAddressIsNull() {
            addCriterion("address is null");
            return (Criteria) this;
        }

        public Criteria andAddressIsNotNull() {
            addCriterion("address is not null");
            return (Criteria) this;
        }

        public Criteria andAddressEqualTo(String value) {
            addCriterion("address =", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotEqualTo(String value) {
            addCriterion("address <>", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThan(String value) {
            addCriterion("address >", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressGreaterThanOrEqualTo(String value) {
            addCriterion("address >=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThan(String value) {
            addCriterion("address <", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLessThanOrEqualTo(String value) {
            addCriterion("address <=", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressLike(String value) {
            addCriterion("address like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotLike(String value) {
            addCriterion("address not like", value, "address");
            return (Criteria) this;
        }

        public Criteria andAddressIn(List<String> values) {
            addCriterion("address in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotIn(List<String> values) {
            addCriterion("address not in", values, "address");
            return (Criteria) this;
        }

        public Criteria andAddressBetween(String value1, String value2) {
            addCriterion("address between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andAddressNotBetween(String value1, String value2) {
            addCriterion("address not between", value1, value2, "address");
            return (Criteria) this;
        }

        public Criteria andOwnerIsNull() {
            addCriterion("owner is null");
            return (Criteria) this;
        }

        public Criteria andOwnerIsNotNull() {
            addCriterion("owner is not null");
            return (Criteria) this;
        }

        public Criteria andOwnerEqualTo(String value) {
            addCriterion("owner =", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerNotEqualTo(String value) {
            addCriterion("owner <>", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerGreaterThan(String value) {
            addCriterion("owner >", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerGreaterThanOrEqualTo(String value) {
            addCriterion("owner >=", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerLessThan(String value) {
            addCriterion("owner <", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerLessThanOrEqualTo(String value) {
            addCriterion("owner <=", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerLike(String value) {
            addCriterion("owner like", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerNotLike(String value) {
            addCriterion("owner not like", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerIn(List<String> values) {
            addCriterion("owner in", values, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerNotIn(List<String> values) {
            addCriterion("owner not in", values, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerBetween(String value1, String value2) {
            addCriterion("owner between", value1, value2, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerNotBetween(String value1, String value2) {
            addCriterion("owner not between", value1, value2, "owner");
            return (Criteria) this;
        }

        public Criteria andCreattimeIsNull() {
            addCriterion("creattime is null");
            return (Criteria) this;
        }

        public Criteria andCreattimeIsNotNull() {
            addCriterion("creattime is not null");
            return (Criteria) this;
        }

        public Criteria andCreattimeEqualTo(String value) {
            addCriterion("creattime =", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeNotEqualTo(String value) {
            addCriterion("creattime <>", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeGreaterThan(String value) {
            addCriterion("creattime >", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeGreaterThanOrEqualTo(String value) {
            addCriterion("creattime >=", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeLessThan(String value) {
            addCriterion("creattime <", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeLessThanOrEqualTo(String value) {
            addCriterion("creattime <=", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeLike(String value) {
            addCriterion("creattime like", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeNotLike(String value) {
            addCriterion("creattime not like", value, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeIn(List<String> values) {
            addCriterion("creattime in", values, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeNotIn(List<String> values) {
            addCriterion("creattime not in", values, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeBetween(String value1, String value2) {
            addCriterion("creattime between", value1, value2, "creattime");
            return (Criteria) this;
        }

        public Criteria andCreattimeNotBetween(String value1, String value2) {
            addCriterion("creattime not between", value1, value2, "creattime");
            return (Criteria) this;
        }

        public Criteria andProjectphaseIsNull() {
            addCriterion("projectphase is null");
            return (Criteria) this;
        }

        public Criteria andProjectphaseIsNotNull() {
            addCriterion("projectphase is not null");
            return (Criteria) this;
        }

        public Criteria andProjectphaseEqualTo(String value) {
            addCriterion("projectphase =", value, "projectphase");
            return (Criteria) this;
        }

        public Criteria andProjectphaseNotEqualTo(String value) {
            addCriterion("projectphase <>", value, "projectphase");
            return (Criteria) this;
        }

        public Criteria andProjectphaseGreaterThan(String value) {
            addCriterion("projectphase >", value, "projectphase");
            return (Criteria) this;
        }

        public Criteria andProjectphaseGreaterThanOrEqualTo(String value) {
            addCriterion("projectphase >=", value, "projectphase");
            return (Criteria) this;
        }

        public Criteria andProjectphaseLessThan(String value) {
            addCriterion("projectphase <", value, "projectphase");
            return (Criteria) this;
        }

        public Criteria andProjectphaseLessThanOrEqualTo(String value) {
            addCriterion("projectphase <=", value, "projectphase");
            return (Criteria) this;
        }

        public Criteria andProjectphaseLike(String value) {
            addCriterion("projectphase like", value, "projectphase");
            return (Criteria) this;
        }

        public Criteria andProjectphaseNotLike(String value) {
            addCriterion("projectphase not like", value, "projectphase");
            return (Criteria) this;
        }

        public Criteria andProjectphaseIn(List<String> values) {
            addCriterion("projectphase in", values, "projectphase");
            return (Criteria) this;
        }

        public Criteria andProjectphaseNotIn(List<String> values) {
            addCriterion("projectphase not in", values, "projectphase");
            return (Criteria) this;
        }

        public Criteria andProjectphaseBetween(String value1, String value2) {
            addCriterion("projectphase between", value1, value2, "projectphase");
            return (Criteria) this;
        }

        public Criteria andProjectphaseNotBetween(String value1, String value2) {
            addCriterion("projectphase not between", value1, value2, "projectphase");
            return (Criteria) this;
        }

        public Criteria andConstructiontypeIsNull() {
            addCriterion("constructiontype is null");
            return (Criteria) this;
        }

        public Criteria andConstructiontypeIsNotNull() {
            addCriterion("constructiontype is not null");
            return (Criteria) this;
        }

        public Criteria andConstructiontypeEqualTo(String value) {
            addCriterion("constructiontype =", value, "constructiontype");
            return (Criteria) this;
        }

        public Criteria andConstructiontypeNotEqualTo(String value) {
            addCriterion("constructiontype <>", value, "constructiontype");
            return (Criteria) this;
        }

        public Criteria andConstructiontypeGreaterThan(String value) {
            addCriterion("constructiontype >", value, "constructiontype");
            return (Criteria) this;
        }

        public Criteria andConstructiontypeGreaterThanOrEqualTo(String value) {
            addCriterion("constructiontype >=", value, "constructiontype");
            return (Criteria) this;
        }

        public Criteria andConstructiontypeLessThan(String value) {
            addCriterion("constructiontype <", value, "constructiontype");
            return (Criteria) this;
        }

        public Criteria andConstructiontypeLessThanOrEqualTo(String value) {
            addCriterion("constructiontype <=", value, "constructiontype");
            return (Criteria) this;
        }

        public Criteria andConstructiontypeLike(String value) {
            addCriterion("constructiontype like", value, "constructiontype");
            return (Criteria) this;
        }

        public Criteria andConstructiontypeNotLike(String value) {
            addCriterion("constructiontype not like", value, "constructiontype");
            return (Criteria) this;
        }

        public Criteria andConstructiontypeIn(List<String> values) {
            addCriterion("constructiontype in", values, "constructiontype");
            return (Criteria) this;
        }

        public Criteria andConstructiontypeNotIn(List<String> values) {
            addCriterion("constructiontype not in", values, "constructiontype");
            return (Criteria) this;
        }

        public Criteria andConstructiontypeBetween(String value1, String value2) {
            addCriterion("constructiontype between", value1, value2, "constructiontype");
            return (Criteria) this;
        }

        public Criteria andConstructiontypeNotBetween(String value1, String value2) {
            addCriterion("constructiontype not between", value1, value2, "constructiontype");
            return (Criteria) this;
        }

        public Criteria andEcoindexIsNull() {
            addCriterion("ecoindex is null");
            return (Criteria) this;
        }

        public Criteria andEcoindexIsNotNull() {
            addCriterion("ecoindex is not null");
            return (Criteria) this;
        }

        public Criteria andEcoindexEqualTo(String value) {
            addCriterion("ecoindex =", value, "ecoindex");
            return (Criteria) this;
        }

        public Criteria andEcoindexNotEqualTo(String value) {
            addCriterion("ecoindex <>", value, "ecoindex");
            return (Criteria) this;
        }

        public Criteria andEcoindexGreaterThan(String value) {
            addCriterion("ecoindex >", value, "ecoindex");
            return (Criteria) this;
        }

        public Criteria andEcoindexGreaterThanOrEqualTo(String value) {
            addCriterion("ecoindex >=", value, "ecoindex");
            return (Criteria) this;
        }

        public Criteria andEcoindexLessThan(String value) {
            addCriterion("ecoindex <", value, "ecoindex");
            return (Criteria) this;
        }

        public Criteria andEcoindexLessThanOrEqualTo(String value) {
            addCriterion("ecoindex <=", value, "ecoindex");
            return (Criteria) this;
        }

        public Criteria andEcoindexLike(String value) {
            addCriterion("ecoindex like", value, "ecoindex");
            return (Criteria) this;
        }

        public Criteria andEcoindexNotLike(String value) {
            addCriterion("ecoindex not like", value, "ecoindex");
            return (Criteria) this;
        }

        public Criteria andEcoindexIn(List<String> values) {
            addCriterion("ecoindex in", values, "ecoindex");
            return (Criteria) this;
        }

        public Criteria andEcoindexNotIn(List<String> values) {
            addCriterion("ecoindex not in", values, "ecoindex");
            return (Criteria) this;
        }

        public Criteria andEcoindexBetween(String value1, String value2) {
            addCriterion("ecoindex between", value1, value2, "ecoindex");
            return (Criteria) this;
        }

        public Criteria andEcoindexNotBetween(String value1, String value2) {
            addCriterion("ecoindex not between", value1, value2, "ecoindex");
            return (Criteria) this;
        }

        public Criteria andEcoindextableIsNull() {
            addCriterion("ecoindextable is null");
            return (Criteria) this;
        }

        public Criteria andEcoindextableIsNotNull() {
            addCriterion("ecoindextable is not null");
            return (Criteria) this;
        }

        public Criteria andEcoindextableEqualTo(String value) {
            addCriterion("ecoindextable =", value, "ecoindextable");
            return (Criteria) this;
        }

        public Criteria andEcoindextableNotEqualTo(String value) {
            addCriterion("ecoindextable <>", value, "ecoindextable");
            return (Criteria) this;
        }

        public Criteria andEcoindextableGreaterThan(String value) {
            addCriterion("ecoindextable >", value, "ecoindextable");
            return (Criteria) this;
        }

        public Criteria andEcoindextableGreaterThanOrEqualTo(String value) {
            addCriterion("ecoindextable >=", value, "ecoindextable");
            return (Criteria) this;
        }

        public Criteria andEcoindextableLessThan(String value) {
            addCriterion("ecoindextable <", value, "ecoindextable");
            return (Criteria) this;
        }

        public Criteria andEcoindextableLessThanOrEqualTo(String value) {
            addCriterion("ecoindextable <=", value, "ecoindextable");
            return (Criteria) this;
        }

        public Criteria andEcoindextableLike(String value) {
            addCriterion("ecoindextable like", value, "ecoindextable");
            return (Criteria) this;
        }

        public Criteria andEcoindextableNotLike(String value) {
            addCriterion("ecoindextable not like", value, "ecoindextable");
            return (Criteria) this;
        }

        public Criteria andEcoindextableIn(List<String> values) {
            addCriterion("ecoindextable in", values, "ecoindextable");
            return (Criteria) this;
        }

        public Criteria andEcoindextableNotIn(List<String> values) {
            addCriterion("ecoindextable not in", values, "ecoindextable");
            return (Criteria) this;
        }

        public Criteria andEcoindextableBetween(String value1, String value2) {
            addCriterion("ecoindextable between", value1, value2, "ecoindextable");
            return (Criteria) this;
        }

        public Criteria andEcoindextableNotBetween(String value1, String value2) {
            addCriterion("ecoindextable not between", value1, value2, "ecoindextable");
            return (Criteria) this;
        }

        public Criteria andMetadataIsNull() {
            addCriterion("metadata is null");
            return (Criteria) this;
        }

        public Criteria andMetadataIsNotNull() {
            addCriterion("metadata is not null");
            return (Criteria) this;
        }

        public Criteria andMetadataEqualTo(String value) {
            addCriterion("metadata =", value, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataNotEqualTo(String value) {
            addCriterion("metadata <>", value, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataGreaterThan(String value) {
            addCriterion("metadata >", value, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataGreaterThanOrEqualTo(String value) {
            addCriterion("metadata >=", value, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataLessThan(String value) {
            addCriterion("metadata <", value, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataLessThanOrEqualTo(String value) {
            addCriterion("metadata <=", value, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataLike(String value) {
            addCriterion("metadata like", value, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataNotLike(String value) {
            addCriterion("metadata not like", value, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataIn(List<String> values) {
            addCriterion("metadata in", values, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataNotIn(List<String> values) {
            addCriterion("metadata not in", values, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataBetween(String value1, String value2) {
            addCriterion("metadata between", value1, value2, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataNotBetween(String value1, String value2) {
            addCriterion("metadata not between", value1, value2, "metadata");
            return (Criteria) this;
        }

        public Criteria andServerurlIsNull() {
            addCriterion("serverurl is null");
            return (Criteria) this;
        }

        public Criteria andServerurlIsNotNull() {
            addCriterion("serverurl is not null");
            return (Criteria) this;
        }

        public Criteria andServerurlEqualTo(String value) {
            addCriterion("serverurl =", value, "serverurl");
            return (Criteria) this;
        }

        public Criteria andServerurlNotEqualTo(String value) {
            addCriterion("serverurl <>", value, "serverurl");
            return (Criteria) this;
        }

        public Criteria andServerurlGreaterThan(String value) {
            addCriterion("serverurl >", value, "serverurl");
            return (Criteria) this;
        }

        public Criteria andServerurlGreaterThanOrEqualTo(String value) {
            addCriterion("serverurl >=", value, "serverurl");
            return (Criteria) this;
        }

        public Criteria andServerurlLessThan(String value) {
            addCriterion("serverurl <", value, "serverurl");
            return (Criteria) this;
        }

        public Criteria andServerurlLessThanOrEqualTo(String value) {
            addCriterion("serverurl <=", value, "serverurl");
            return (Criteria) this;
        }

        public Criteria andServerurlLike(String value) {
            addCriterion("serverurl like", value, "serverurl");
            return (Criteria) this;
        }

        public Criteria andServerurlNotLike(String value) {
            addCriterion("serverurl not like", value, "serverurl");
            return (Criteria) this;
        }

        public Criteria andServerurlIn(List<String> values) {
            addCriterion("serverurl in", values, "serverurl");
            return (Criteria) this;
        }

        public Criteria andServerurlNotIn(List<String> values) {
            addCriterion("serverurl not in", values, "serverurl");
            return (Criteria) this;
        }

        public Criteria andServerurlBetween(String value1, String value2) {
            addCriterion("serverurl between", value1, value2, "serverurl");
            return (Criteria) this;
        }

        public Criteria andServerurlNotBetween(String value1, String value2) {
            addCriterion("serverurl not between", value1, value2, "serverurl");
            return (Criteria) this;
        }

        public Criteria andServertypeIsNull() {
            addCriterion("servertype is null");
            return (Criteria) this;
        }

        public Criteria andServertypeIsNotNull() {
            addCriterion("servertype is not null");
            return (Criteria) this;
        }

        public Criteria andServertypeEqualTo(String value) {
            addCriterion("servertype =", value, "servertype");
            return (Criteria) this;
        }

        public Criteria andServertypeNotEqualTo(String value) {
            addCriterion("servertype <>", value, "servertype");
            return (Criteria) this;
        }

        public Criteria andServertypeGreaterThan(String value) {
            addCriterion("servertype >", value, "servertype");
            return (Criteria) this;
        }

        public Criteria andServertypeGreaterThanOrEqualTo(String value) {
            addCriterion("servertype >=", value, "servertype");
            return (Criteria) this;
        }

        public Criteria andServertypeLessThan(String value) {
            addCriterion("servertype <", value, "servertype");
            return (Criteria) this;
        }

        public Criteria andServertypeLessThanOrEqualTo(String value) {
            addCriterion("servertype <=", value, "servertype");
            return (Criteria) this;
        }

        public Criteria andServertypeLike(String value) {
            addCriterion("servertype like", value, "servertype");
            return (Criteria) this;
        }

        public Criteria andServertypeNotLike(String value) {
            addCriterion("servertype not like", value, "servertype");
            return (Criteria) this;
        }

        public Criteria andServertypeIn(List<String> values) {
            addCriterion("servertype in", values, "servertype");
            return (Criteria) this;
        }

        public Criteria andServertypeNotIn(List<String> values) {
            addCriterion("servertype not in", values, "servertype");
            return (Criteria) this;
        }

        public Criteria andServertypeBetween(String value1, String value2) {
            addCriterion("servertype between", value1, value2, "servertype");
            return (Criteria) this;
        }

        public Criteria andServertypeNotBetween(String value1, String value2) {
            addCriterion("servertype not between", value1, value2, "servertype");
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