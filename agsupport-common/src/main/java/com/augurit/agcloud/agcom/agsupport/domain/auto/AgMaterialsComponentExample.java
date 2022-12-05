package com.augurit.agcloud.agcom.agsupport.domain.auto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @Author: qinyg
 * @Date: 2020/10/20
 * @tips: example类
 */
public class AgMaterialsComponentExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AgMaterialsComponentExample() {
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

        public Criteria andObjectidIsNull() {
            addCriterion("objectid is null");
            return (Criteria) this;
        }

        public Criteria andObjectidIsNotNull() {
            addCriterion("objectid is not null");
            return (Criteria) this;
        }

        public Criteria andObjectidEqualTo(String value) {
            addCriterion("objectid =", value, "objectid");
            return (Criteria) this;
        }

        public Criteria andObjectidNotEqualTo(String value) {
            addCriterion("objectid <>", value, "objectid");
            return (Criteria) this;
        }

        public Criteria andObjectidGreaterThan(String value) {
            addCriterion("objectid >", value, "objectid");
            return (Criteria) this;
        }

        public Criteria andObjectidGreaterThanOrEqualTo(String value) {
            addCriterion("objectid >=", value, "objectid");
            return (Criteria) this;
        }

        public Criteria andObjectidLessThan(String value) {
            addCriterion("objectid <", value, "objectid");
            return (Criteria) this;
        }

        public Criteria andObjectidLessThanOrEqualTo(String value) {
            addCriterion("objectid <=", value, "objectid");
            return (Criteria) this;
        }

        public Criteria andObjectidLike(String value) {
            addCriterion("objectid like", value, "objectid");
            return (Criteria) this;
        }

        public Criteria andObjectidNotLike(String value) {
            addCriterion("objectid not like", value, "objectid");
            return (Criteria) this;
        }

        public Criteria andObjectidIn(List<String> values) {
            addCriterion("objectid in", values, "objectid");
            return (Criteria) this;
        }

        public Criteria andObjectidNotIn(List<String> values) {
            addCriterion("objectid not in", values, "objectid");
            return (Criteria) this;
        }

        public Criteria andObjectidBetween(String value1, String value2) {
            addCriterion("objectid between", value1, value2, "objectid");
            return (Criteria) this;
        }

        public Criteria andObjectidNotBetween(String value1, String value2) {
            addCriterion("objectid not between", value1, value2, "objectid");
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

        public Criteria andVersionIsNull() {
            addCriterion("version is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("version is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(Long value) {
            addCriterion("version =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(Long value) {
            addCriterion("version <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(Long value) {
            addCriterion("version >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(Long value) {
            addCriterion("version >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(Long value) {
            addCriterion("version <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(Long value) {
            addCriterion("version <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<Long> values) {
            addCriterion("version in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<Long> values) {
            addCriterion("version not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(Long value1, Long value2) {
            addCriterion("version between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(Long value1, Long value2) {
            addCriterion("version not between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andInfotypeIsNull() {
            addCriterion("infotype is null");
            return (Criteria) this;
        }

        public Criteria andInfotypeIsNotNull() {
            addCriterion("infotype is not null");
            return (Criteria) this;
        }

        public Criteria andInfotypeEqualTo(String value) {
            addCriterion("infotype =", value, "infotype");
            return (Criteria) this;
        }

        public Criteria andInfotypeNotEqualTo(String value) {
            addCriterion("infotype <>", value, "infotype");
            return (Criteria) this;
        }

        public Criteria andInfotypeGreaterThan(String value) {
            addCriterion("infotype >", value, "infotype");
            return (Criteria) this;
        }

        public Criteria andInfotypeGreaterThanOrEqualTo(String value) {
            addCriterion("infotype >=", value, "infotype");
            return (Criteria) this;
        }

        public Criteria andInfotypeLessThan(String value) {
            addCriterion("infotype <", value, "infotype");
            return (Criteria) this;
        }

        public Criteria andInfotypeLessThanOrEqualTo(String value) {
            addCriterion("infotype <=", value, "infotype");
            return (Criteria) this;
        }

        public Criteria andInfotypeLike(String value) {
            addCriterion("infotype like", value, "infotype");
            return (Criteria) this;
        }

        public Criteria andInfotypeNotLike(String value) {
            addCriterion("infotype not like", value, "infotype");
            return (Criteria) this;
        }

        public Criteria andInfotypeIn(List<String> values) {
            addCriterion("infotype in", values, "infotype");
            return (Criteria) this;
        }

        public Criteria andInfotypeNotIn(List<String> values) {
            addCriterion("infotype not in", values, "infotype");
            return (Criteria) this;
        }

        public Criteria andInfotypeBetween(String value1, String value2) {
            addCriterion("infotype between", value1, value2, "infotype");
            return (Criteria) this;
        }

        public Criteria andInfotypeNotBetween(String value1, String value2) {
            addCriterion("infotype not between", value1, value2, "infotype");
            return (Criteria) this;
        }

        public Criteria andProfessionIsNull() {
            addCriterion("profession is null");
            return (Criteria) this;
        }

        public Criteria andProfessionIsNotNull() {
            addCriterion("profession is not null");
            return (Criteria) this;
        }

        public Criteria andProfessionEqualTo(String value) {
            addCriterion("profession =", value, "profession");
            return (Criteria) this;
        }

        public Criteria andProfessionNotEqualTo(String value) {
            addCriterion("profession <>", value, "profession");
            return (Criteria) this;
        }

        public Criteria andProfessionGreaterThan(String value) {
            addCriterion("profession >", value, "profession");
            return (Criteria) this;
        }

        public Criteria andProfessionGreaterThanOrEqualTo(String value) {
            addCriterion("profession >=", value, "profession");
            return (Criteria) this;
        }

        public Criteria andProfessionLessThan(String value) {
            addCriterion("profession <", value, "profession");
            return (Criteria) this;
        }

        public Criteria andProfessionLessThanOrEqualTo(String value) {
            addCriterion("profession <=", value, "profession");
            return (Criteria) this;
        }

        public Criteria andProfessionLike(String value) {
            addCriterion("profession like", value, "profession");
            return (Criteria) this;
        }

        public Criteria andProfessionNotLike(String value) {
            addCriterion("profession not like", value, "profession");
            return (Criteria) this;
        }

        public Criteria andProfessionIn(List<String> values) {
            addCriterion("profession in", values, "profession");
            return (Criteria) this;
        }

        public Criteria andProfessionNotIn(List<String> values) {
            addCriterion("profession not in", values, "profession");
            return (Criteria) this;
        }

        public Criteria andProfessionBetween(String value1, String value2) {
            addCriterion("profession between", value1, value2, "profession");
            return (Criteria) this;
        }

        public Criteria andProfessionNotBetween(String value1, String value2) {
            addCriterion("profession not between", value1, value2, "profession");
            return (Criteria) this;
        }

        public Criteria andLevelIsNull() {
            addCriterion("level is null");
            return (Criteria) this;
        }

        public Criteria andLevelIsNotNull() {
            addCriterion("level is not null");
            return (Criteria) this;
        }

        public Criteria andLevelEqualTo(String value) {
            addCriterion("level =", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotEqualTo(String value) {
            addCriterion("level <>", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThan(String value) {
            addCriterion("level >", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThanOrEqualTo(String value) {
            addCriterion("level >=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThan(String value) {
            addCriterion("level <", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThanOrEqualTo(String value) {
            addCriterion("level <=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLike(String value) {
            addCriterion("level like", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotLike(String value) {
            addCriterion("level not like", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelIn(List<String> values) {
            addCriterion("level in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotIn(List<String> values) {
            addCriterion("level not in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelBetween(String value1, String value2) {
            addCriterion("level between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotBetween(String value1, String value2) {
            addCriterion("level not between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andCatagoryIsNull() {
            addCriterion("catagory is null");
            return (Criteria) this;
        }

        public Criteria andCatagoryIsNotNull() {
            addCriterion("catagory is not null");
            return (Criteria) this;
        }

        public Criteria andCatagoryEqualTo(String value) {
            addCriterion("catagory =", value, "catagory");
            return (Criteria) this;
        }

        public Criteria andCatagoryNotEqualTo(String value) {
            addCriterion("catagory <>", value, "catagory");
            return (Criteria) this;
        }

        public Criteria andCatagoryGreaterThan(String value) {
            addCriterion("catagory >", value, "catagory");
            return (Criteria) this;
        }

        public Criteria andCatagoryGreaterThanOrEqualTo(String value) {
            addCriterion("catagory >=", value, "catagory");
            return (Criteria) this;
        }

        public Criteria andCatagoryLessThan(String value) {
            addCriterion("catagory <", value, "catagory");
            return (Criteria) this;
        }

        public Criteria andCatagoryLessThanOrEqualTo(String value) {
            addCriterion("catagory <=", value, "catagory");
            return (Criteria) this;
        }

        public Criteria andCatagoryLike(String value) {
            addCriterion("catagory like", value, "catagory");
            return (Criteria) this;
        }

        public Criteria andCatagoryNotLike(String value) {
            addCriterion("catagory not like", value, "catagory");
            return (Criteria) this;
        }

        public Criteria andCatagoryIn(List<String> values) {
            addCriterion("catagory in", values, "catagory");
            return (Criteria) this;
        }

        public Criteria andCatagoryNotIn(List<String> values) {
            addCriterion("catagory not in", values, "catagory");
            return (Criteria) this;
        }

        public Criteria andCatagoryBetween(String value1, String value2) {
            addCriterion("catagory between", value1, value2, "catagory");
            return (Criteria) this;
        }

        public Criteria andCatagoryNotBetween(String value1, String value2) {
            addCriterion("catagory not between", value1, value2, "catagory");
            return (Criteria) this;
        }

        public Criteria andFamilynameIsNull() {
            addCriterion("familyname is null");
            return (Criteria) this;
        }

        public Criteria andFamilynameIsNotNull() {
            addCriterion("familyname is not null");
            return (Criteria) this;
        }

        public Criteria andFamilynameEqualTo(String value) {
            addCriterion("familyname =", value, "familyname");
            return (Criteria) this;
        }

        public Criteria andFamilynameNotEqualTo(String value) {
            addCriterion("familyname <>", value, "familyname");
            return (Criteria) this;
        }

        public Criteria andFamilynameGreaterThan(String value) {
            addCriterion("familyname >", value, "familyname");
            return (Criteria) this;
        }

        public Criteria andFamilynameGreaterThanOrEqualTo(String value) {
            addCriterion("familyname >=", value, "familyname");
            return (Criteria) this;
        }

        public Criteria andFamilynameLessThan(String value) {
            addCriterion("familyname <", value, "familyname");
            return (Criteria) this;
        }

        public Criteria andFamilynameLessThanOrEqualTo(String value) {
            addCriterion("familyname <=", value, "familyname");
            return (Criteria) this;
        }

        public Criteria andFamilynameLike(String value) {
            addCriterion("familyname like", value, "familyname");
            return (Criteria) this;
        }

        public Criteria andFamilynameNotLike(String value) {
            addCriterion("familyname not like", value, "familyname");
            return (Criteria) this;
        }

        public Criteria andFamilynameIn(List<String> values) {
            addCriterion("familyname in", values, "familyname");
            return (Criteria) this;
        }

        public Criteria andFamilynameNotIn(List<String> values) {
            addCriterion("familyname not in", values, "familyname");
            return (Criteria) this;
        }

        public Criteria andFamilynameBetween(String value1, String value2) {
            addCriterion("familyname between", value1, value2, "familyname");
            return (Criteria) this;
        }

        public Criteria andFamilynameNotBetween(String value1, String value2) {
            addCriterion("familyname not between", value1, value2, "familyname");
            return (Criteria) this;
        }

        public Criteria andFamilytypeIsNull() {
            addCriterion("familytype is null");
            return (Criteria) this;
        }

        public Criteria andFamilytypeIsNotNull() {
            addCriterion("familytype is not null");
            return (Criteria) this;
        }

        public Criteria andFamilytypeEqualTo(String value) {
            addCriterion("familytype =", value, "familytype");
            return (Criteria) this;
        }

        public Criteria andFamilytypeNotEqualTo(String value) {
            addCriterion("familytype <>", value, "familytype");
            return (Criteria) this;
        }

        public Criteria andFamilytypeGreaterThan(String value) {
            addCriterion("familytype >", value, "familytype");
            return (Criteria) this;
        }

        public Criteria andFamilytypeGreaterThanOrEqualTo(String value) {
            addCriterion("familytype >=", value, "familytype");
            return (Criteria) this;
        }

        public Criteria andFamilytypeLessThan(String value) {
            addCriterion("familytype <", value, "familytype");
            return (Criteria) this;
        }

        public Criteria andFamilytypeLessThanOrEqualTo(String value) {
            addCriterion("familytype <=", value, "familytype");
            return (Criteria) this;
        }

        public Criteria andFamilytypeLike(String value) {
            addCriterion("familytype like", value, "familytype");
            return (Criteria) this;
        }

        public Criteria andFamilytypeNotLike(String value) {
            addCriterion("familytype not like", value, "familytype");
            return (Criteria) this;
        }

        public Criteria andFamilytypeIn(List<String> values) {
            addCriterion("familytype in", values, "familytype");
            return (Criteria) this;
        }

        public Criteria andFamilytypeNotIn(List<String> values) {
            addCriterion("familytype not in", values, "familytype");
            return (Criteria) this;
        }

        public Criteria andFamilytypeBetween(String value1, String value2) {
            addCriterion("familytype between", value1, value2, "familytype");
            return (Criteria) this;
        }

        public Criteria andFamilytypeNotBetween(String value1, String value2) {
            addCriterion("familytype not between", value1, value2, "familytype");
            return (Criteria) this;
        }

        public Criteria andMaterialidIsNull() {
            addCriterion("materialid is null");
            return (Criteria) this;
        }

        public Criteria andMaterialidIsNotNull() {
            addCriterion("materialid is not null");
            return (Criteria) this;
        }

        public Criteria andMaterialidEqualTo(String value) {
            addCriterion("materialid =", value, "materialid");
            return (Criteria) this;
        }

        public Criteria andMaterialidNotEqualTo(String value) {
            addCriterion("materialid <>", value, "materialid");
            return (Criteria) this;
        }

        public Criteria andMaterialidGreaterThan(String value) {
            addCriterion("materialid >", value, "materialid");
            return (Criteria) this;
        }

        public Criteria andMaterialidGreaterThanOrEqualTo(String value) {
            addCriterion("materialid >=", value, "materialid");
            return (Criteria) this;
        }

        public Criteria andMaterialidLessThan(String value) {
            addCriterion("materialid <", value, "materialid");
            return (Criteria) this;
        }

        public Criteria andMaterialidLessThanOrEqualTo(String value) {
            addCriterion("materialid <=", value, "materialid");
            return (Criteria) this;
        }

        public Criteria andMaterialidLike(String value) {
            addCriterion("materialid like", value, "materialid");
            return (Criteria) this;
        }

        public Criteria andMaterialidNotLike(String value) {
            addCriterion("materialid not like", value, "materialid");
            return (Criteria) this;
        }

        public Criteria andMaterialidIn(List<String> values) {
            addCriterion("materialid in", values, "materialid");
            return (Criteria) this;
        }

        public Criteria andMaterialidNotIn(List<String> values) {
            addCriterion("materialid not in", values, "materialid");
            return (Criteria) this;
        }

        public Criteria andMaterialidBetween(String value1, String value2) {
            addCriterion("materialid between", value1, value2, "materialid");
            return (Criteria) this;
        }

        public Criteria andMaterialidNotBetween(String value1, String value2) {
            addCriterion("materialid not between", value1, value2, "materialid");
            return (Criteria) this;
        }

        public Criteria andElementattributesIsNull() {
            addCriterion("elementattributes is null");
            return (Criteria) this;
        }

        public Criteria andElementattributesIsNotNull() {
            addCriterion("elementattributes is not null");
            return (Criteria) this;
        }

        public Criteria andElementattributesEqualTo(String value) {
            addCriterion("elementattributes =", value, "elementattributes");
            return (Criteria) this;
        }

        public Criteria andElementattributesNotEqualTo(String value) {
            addCriterion("elementattributes <>", value, "elementattributes");
            return (Criteria) this;
        }

        public Criteria andElementattributesGreaterThan(String value) {
            addCriterion("elementattributes >", value, "elementattributes");
            return (Criteria) this;
        }

        public Criteria andElementattributesGreaterThanOrEqualTo(String value) {
            addCriterion("elementattributes >=", value, "elementattributes");
            return (Criteria) this;
        }

        public Criteria andElementattributesLessThan(String value) {
            addCriterion("elementattributes <", value, "elementattributes");
            return (Criteria) this;
        }

        public Criteria andElementattributesLessThanOrEqualTo(String value) {
            addCriterion("elementattributes <=", value, "elementattributes");
            return (Criteria) this;
        }

        public Criteria andElementattributesLike(String value) {
            addCriterion("elementattributes like", value, "elementattributes");
            return (Criteria) this;
        }

        public Criteria andElementattributesNotLike(String value) {
            addCriterion("elementattributes not like", value, "elementattributes");
            return (Criteria) this;
        }

        public Criteria andElementattributesIn(List<String> values) {
            addCriterion("elementattributes in", values, "elementattributes");
            return (Criteria) this;
        }

        public Criteria andElementattributesNotIn(List<String> values) {
            addCriterion("elementattributes not in", values, "elementattributes");
            return (Criteria) this;
        }

        public Criteria andElementattributesBetween(String value1, String value2) {
            addCriterion("elementattributes between", value1, value2, "elementattributes");
            return (Criteria) this;
        }

        public Criteria andElementattributesNotBetween(String value1, String value2) {
            addCriterion("elementattributes not between", value1, value2, "elementattributes");
            return (Criteria) this;
        }

        public Criteria andCategorypathIsNull() {
            addCriterion("categorypath is null");
            return (Criteria) this;
        }

        public Criteria andCategorypathIsNotNull() {
            addCriterion("categorypath is not null");
            return (Criteria) this;
        }

        public Criteria andCategorypathEqualTo(String value) {
            addCriterion("categorypath =", value, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathNotEqualTo(String value) {
            addCriterion("categorypath <>", value, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathGreaterThan(String value) {
            addCriterion("categorypath >", value, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathGreaterThanOrEqualTo(String value) {
            addCriterion("categorypath >=", value, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathLessThan(String value) {
            addCriterion("categorypath <", value, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathLessThanOrEqualTo(String value) {
            addCriterion("categorypath <=", value, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathLike(String value) {
            addCriterion("categorypath like", value, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathNotLike(String value) {
            addCriterion("categorypath not like", value, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathIn(List<String> values) {
            addCriterion("categorypath in", values, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathNotIn(List<String> values) {
            addCriterion("categorypath not in", values, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathBetween(String value1, String value2) {
            addCriterion("categorypath between", value1, value2, "categorypath");
            return (Criteria) this;
        }

        public Criteria andCategorypathNotBetween(String value1, String value2) {
            addCriterion("categorypath not between", value1, value2, "categorypath");
            return (Criteria) this;
        }

        public Criteria andGeometryIsNull() {
            addCriterion("geometry is null");
            return (Criteria) this;
        }

        public Criteria andGeometryIsNotNull() {
            addCriterion("geometry is not null");
            return (Criteria) this;
        }

        public Criteria andGeometryEqualTo(String value) {
            addCriterion("geometry =", value, "geometry");
            return (Criteria) this;
        }

        public Criteria andGeometryNotEqualTo(String value) {
            addCriterion("geometry <>", value, "geometry");
            return (Criteria) this;
        }

        public Criteria andGeometryGreaterThan(String value) {
            addCriterion("geometry >", value, "geometry");
            return (Criteria) this;
        }

        public Criteria andGeometryGreaterThanOrEqualTo(String value) {
            addCriterion("geometry >=", value, "geometry");
            return (Criteria) this;
        }

        public Criteria andGeometryLessThan(String value) {
            addCriterion("geometry <", value, "geometry");
            return (Criteria) this;
        }

        public Criteria andGeometryLessThanOrEqualTo(String value) {
            addCriterion("geometry <=", value, "geometry");
            return (Criteria) this;
        }

        public Criteria andGeometryLike(String value) {
            addCriterion("geometry like", value, "geometry");
            return (Criteria) this;
        }

        public Criteria andGeometryNotLike(String value) {
            addCriterion("geometry not like", value, "geometry");
            return (Criteria) this;
        }

        public Criteria andGeometryIn(List<String> values) {
            addCriterion("geometry in", values, "geometry");
            return (Criteria) this;
        }

        public Criteria andGeometryNotIn(List<String> values) {
            addCriterion("geometry not in", values, "geometry");
            return (Criteria) this;
        }

        public Criteria andGeometryBetween(String value1, String value2) {
            addCriterion("geometry between", value1, value2, "geometry");
            return (Criteria) this;
        }

        public Criteria andGeometryNotBetween(String value1, String value2) {
            addCriterion("geometry not between", value1, value2, "geometry");
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

        public Criteria andTextureIsNull() {
            addCriterion("texture is null");
            return (Criteria) this;
        }

        public Criteria andTextureIsNotNull() {
            addCriterion("texture is not null");
            return (Criteria) this;
        }

        public Criteria andTextureEqualTo(String value) {
            addCriterion("texture =", value, "texture");
            return (Criteria) this;
        }

        public Criteria andTextureNotEqualTo(String value) {
            addCriterion("texture <>", value, "texture");
            return (Criteria) this;
        }

        public Criteria andTextureGreaterThan(String value) {
            addCriterion("texture >", value, "texture");
            return (Criteria) this;
        }

        public Criteria andTextureGreaterThanOrEqualTo(String value) {
            addCriterion("texture >=", value, "texture");
            return (Criteria) this;
        }

        public Criteria andTextureLessThan(String value) {
            addCriterion("texture <", value, "texture");
            return (Criteria) this;
        }

        public Criteria andTextureLessThanOrEqualTo(String value) {
            addCriterion("texture <=", value, "texture");
            return (Criteria) this;
        }

        public Criteria andTextureLike(String value) {
            addCriterion("texture like", value, "texture");
            return (Criteria) this;
        }

        public Criteria andTextureNotLike(String value) {
            addCriterion("texture not like", value, "texture");
            return (Criteria) this;
        }

        public Criteria andTextureIn(List<String> values) {
            addCriterion("texture in", values, "texture");
            return (Criteria) this;
        }

        public Criteria andTextureNotIn(List<String> values) {
            addCriterion("texture not in", values, "texture");
            return (Criteria) this;
        }

        public Criteria andTextureBetween(String value1, String value2) {
            addCriterion("texture between", value1, value2, "texture");
            return (Criteria) this;
        }

        public Criteria andTextureNotBetween(String value1, String value2) {
            addCriterion("texture not between", value1, value2, "texture");
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

        public Criteria andVendorIsNull() {
            addCriterion("vendor is null");
            return (Criteria) this;
        }

        public Criteria andVendorIsNotNull() {
            addCriterion("vendor is not null");
            return (Criteria) this;
        }

        public Criteria andVendorEqualTo(String value) {
            addCriterion("vendor =", value, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorNotEqualTo(String value) {
            addCriterion("vendor <>", value, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorGreaterThan(String value) {
            addCriterion("vendor >", value, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorGreaterThanOrEqualTo(String value) {
            addCriterion("vendor >=", value, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorLessThan(String value) {
            addCriterion("vendor <", value, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorLessThanOrEqualTo(String value) {
            addCriterion("vendor <=", value, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorLike(String value) {
            addCriterion("vendor like", value, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorNotLike(String value) {
            addCriterion("vendor not like", value, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorIn(List<String> values) {
            addCriterion("vendor in", values, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorNotIn(List<String> values) {
            addCriterion("vendor not in", values, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorBetween(String value1, String value2) {
            addCriterion("vendor between", value1, value2, "vendor");
            return (Criteria) this;
        }

        public Criteria andVendorNotBetween(String value1, String value2) {
            addCriterion("vendor not between", value1, value2, "vendor");
            return (Criteria) this;
        }

        public Criteria andSinglePriceIsNull() {
            addCriterion("single_price is null");
            return (Criteria) this;
        }

        public Criteria andSinglePriceIsNotNull() {
            addCriterion("single_price is not null");
            return (Criteria) this;
        }

        public Criteria andSinglePriceEqualTo(String value) {
            addCriterion("single_price =", value, "singlePrice");
            return (Criteria) this;
        }

        public Criteria andSinglePriceNotEqualTo(String value) {
            addCriterion("single_price <>", value, "singlePrice");
            return (Criteria) this;
        }

        public Criteria andSinglePriceGreaterThan(String value) {
            addCriterion("single_price >", value, "singlePrice");
            return (Criteria) this;
        }

        public Criteria andSinglePriceGreaterThanOrEqualTo(String value) {
            addCriterion("single_price >=", value, "singlePrice");
            return (Criteria) this;
        }

        public Criteria andSinglePriceLessThan(String value) {
            addCriterion("single_price <", value, "singlePrice");
            return (Criteria) this;
        }

        public Criteria andSinglePriceLessThanOrEqualTo(String value) {
            addCriterion("single_price <=", value, "singlePrice");
            return (Criteria) this;
        }

        public Criteria andSinglePriceLike(String value) {
            addCriterion("single_price like", value, "singlePrice");
            return (Criteria) this;
        }

        public Criteria andSinglePriceNotLike(String value) {
            addCriterion("single_price not like", value, "singlePrice");
            return (Criteria) this;
        }

        public Criteria andSinglePriceIn(List<String> values) {
            addCriterion("single_price in", values, "singlePrice");
            return (Criteria) this;
        }

        public Criteria andSinglePriceNotIn(List<String> values) {
            addCriterion("single_price not in", values, "singlePrice");
            return (Criteria) this;
        }

        public Criteria andSinglePriceBetween(String value1, String value2) {
            addCriterion("single_price between", value1, value2, "singlePrice");
            return (Criteria) this;
        }

        public Criteria andSinglePriceNotBetween(String value1, String value2) {
            addCriterion("single_price not between", value1, value2, "singlePrice");
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

        public Criteria andThumbFileNameIsNull() {
            addCriterion("thumb_file_name is null");
            return (Criteria) this;
        }

        public Criteria andThumbFileNameIsNotNull() {
            addCriterion("thumb_file_name is not null");
            return (Criteria) this;
        }

        public Criteria andThumbFileNameEqualTo(String value) {
            addCriterion("thumb_file_name =", value, "thumbFileName");
            return (Criteria) this;
        }

        public Criteria andThumbFileNameNotEqualTo(String value) {
            addCriterion("thumb_file_name <>", value, "thumbFileName");
            return (Criteria) this;
        }

        public Criteria andThumbFileNameGreaterThan(String value) {
            addCriterion("thumb_file_name >", value, "thumbFileName");
            return (Criteria) this;
        }

        public Criteria andThumbFileNameGreaterThanOrEqualTo(String value) {
            addCriterion("thumb_file_name >=", value, "thumbFileName");
            return (Criteria) this;
        }

        public Criteria andThumbFileNameLessThan(String value) {
            addCriterion("thumb_file_name <", value, "thumbFileName");
            return (Criteria) this;
        }

        public Criteria andThumbFileNameLessThanOrEqualTo(String value) {
            addCriterion("thumb_file_name <=", value, "thumbFileName");
            return (Criteria) this;
        }

        public Criteria andThumbFileNameLike(String value) {
            addCriterion("thumb_file_name like", value, "thumbFileName");
            return (Criteria) this;
        }

        public Criteria andThumbFileNameNotLike(String value) {
            addCriterion("thumb_file_name not like", value, "thumbFileName");
            return (Criteria) this;
        }

        public Criteria andThumbFileNameIn(List<String> values) {
            addCriterion("thumb_file_name in", values, "thumbFileName");
            return (Criteria) this;
        }

        public Criteria andThumbFileNameNotIn(List<String> values) {
            addCriterion("thumb_file_name not in", values, "thumbFileName");
            return (Criteria) this;
        }

        public Criteria andThumbFileNameBetween(String value1, String value2) {
            addCriterion("thumb_file_name between", value1, value2, "thumbFileName");
            return (Criteria) this;
        }

        public Criteria andThumbFileNameNotBetween(String value1, String value2) {
            addCriterion("thumb_file_name not between", value1, value2, "thumbFileName");
            return (Criteria) this;
        }

        public Criteria andGlbFileNameIsNull() {
            addCriterion("glb_file_name is null");
            return (Criteria) this;
        }

        public Criteria andGlbFileNameIsNotNull() {
            addCriterion("glb_file_name is not null");
            return (Criteria) this;
        }

        public Criteria andGlbFileNameEqualTo(String value) {
            addCriterion("glb_file_name =", value, "glbFileName");
            return (Criteria) this;
        }

        public Criteria andGlbFileNameNotEqualTo(String value) {
            addCriterion("glb_file_name <>", value, "glbFileName");
            return (Criteria) this;
        }

        public Criteria andGlbFileNameGreaterThan(String value) {
            addCriterion("glb_file_name >", value, "glbFileName");
            return (Criteria) this;
        }

        public Criteria andGlbFileNameGreaterThanOrEqualTo(String value) {
            addCriterion("glb_file_name >=", value, "glbFileName");
            return (Criteria) this;
        }

        public Criteria andGlbFileNameLessThan(String value) {
            addCriterion("glb_file_name <", value, "glbFileName");
            return (Criteria) this;
        }

        public Criteria andGlbFileNameLessThanOrEqualTo(String value) {
            addCriterion("glb_file_name <=", value, "glbFileName");
            return (Criteria) this;
        }

        public Criteria andGlbFileNameLike(String value) {
            addCriterion("glb_file_name like", value, "glbFileName");
            return (Criteria) this;
        }

        public Criteria andGlbFileNameNotLike(String value) {
            addCriterion("glb_file_name not like", value, "glbFileName");
            return (Criteria) this;
        }

        public Criteria andGlbFileNameIn(List<String> values) {
            addCriterion("glb_file_name in", values, "glbFileName");
            return (Criteria) this;
        }

        public Criteria andGlbFileNameNotIn(List<String> values) {
            addCriterion("glb_file_name not in", values, "glbFileName");
            return (Criteria) this;
        }

        public Criteria andGlbFileNameBetween(String value1, String value2) {
            addCriterion("glb_file_name between", value1, value2, "glbFileName");
            return (Criteria) this;
        }

        public Criteria andGlbFileNameNotBetween(String value1, String value2) {
            addCriterion("glb_file_name not between", value1, value2, "glbFileName");
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

        public Criteria andSpecificationIsNull() {
            addCriterion("specification is null");
            return (Criteria) this;
        }

        public Criteria andSpecificationIsNotNull() {
            addCriterion("specification is not null");
            return (Criteria) this;
        }

        public Criteria andSpecificationEqualTo(String value) {
            addCriterion("specification =", value, "specification");
            return (Criteria) this;
        }

        public Criteria andSpecificationNotEqualTo(String value) {
            addCriterion("specification <>", value, "specification");
            return (Criteria) this;
        }

        public Criteria andSpecificationGreaterThan(String value) {
            addCriterion("specification >", value, "specification");
            return (Criteria) this;
        }

        public Criteria andSpecificationGreaterThanOrEqualTo(String value) {
            addCriterion("specification >=", value, "specification");
            return (Criteria) this;
        }

        public Criteria andSpecificationLessThan(String value) {
            addCriterion("specification <", value, "specification");
            return (Criteria) this;
        }

        public Criteria andSpecificationLessThanOrEqualTo(String value) {
            addCriterion("specification <=", value, "specification");
            return (Criteria) this;
        }

        public Criteria andSpecificationLike(String value) {
            addCriterion("specification like", value, "specification");
            return (Criteria) this;
        }

        public Criteria andSpecificationNotLike(String value) {
            addCriterion("specification not like", value, "specification");
            return (Criteria) this;
        }

        public Criteria andSpecificationIn(List<String> values) {
            addCriterion("specification in", values, "specification");
            return (Criteria) this;
        }

        public Criteria andSpecificationNotIn(List<String> values) {
            addCriterion("specification not in", values, "specification");
            return (Criteria) this;
        }

        public Criteria andSpecificationBetween(String value1, String value2) {
            addCriterion("specification between", value1, value2, "specification");
            return (Criteria) this;
        }

        public Criteria andSpecificationNotBetween(String value1, String value2) {
            addCriterion("specification not between", value1, value2, "specification");
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