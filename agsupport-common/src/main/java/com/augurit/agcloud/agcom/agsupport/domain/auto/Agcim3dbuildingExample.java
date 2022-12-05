package com.augurit.agcloud.agcom.agsupport.domain.auto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Author: libc
 * @Date: 2020/12/10
 * @tips: example类
 */
public class Agcim3dbuildingExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public Agcim3dbuildingExample() {
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

        public Criteria andProjectidIsNull() {
            addCriterion("projectid is null");
            return (Criteria) this;
        }

        public Criteria andProjectidIsNotNull() {
            addCriterion("projectid is not null");
            return (Criteria) this;
        }

        public Criteria andProjectidEqualTo(String value) {
            addCriterion("projectid =", value, "projectid");
            return (Criteria) this;
        }

        public Criteria andProjectidNotEqualTo(String value) {
            addCriterion("projectid <>", value, "projectid");
            return (Criteria) this;
        }

        public Criteria andProjectidGreaterThan(String value) {
            addCriterion("projectid >", value, "projectid");
            return (Criteria) this;
        }

        public Criteria andProjectidGreaterThanOrEqualTo(String value) {
            addCriterion("projectid >=", value, "projectid");
            return (Criteria) this;
        }

        public Criteria andProjectidLessThan(String value) {
            addCriterion("projectid <", value, "projectid");
            return (Criteria) this;
        }

        public Criteria andProjectidLessThanOrEqualTo(String value) {
            addCriterion("projectid <=", value, "projectid");
            return (Criteria) this;
        }

        public Criteria andProjectidLike(String value) {
            addCriterion("projectid like", value, "projectid");
            return (Criteria) this;
        }

        public Criteria andProjectidNotLike(String value) {
            addCriterion("projectid not like", value, "projectid");
            return (Criteria) this;
        }

        public Criteria andProjectidIn(List<String> values) {
            addCriterion("projectid in", values, "projectid");
            return (Criteria) this;
        }

        public Criteria andProjectidNotIn(List<String> values) {
            addCriterion("projectid not in", values, "projectid");
            return (Criteria) this;
        }

        public Criteria andProjectidBetween(String value1, String value2) {
            addCriterion("projectid between", value1, value2, "projectid");
            return (Criteria) this;
        }

        public Criteria andProjectidNotBetween(String value1, String value2) {
            addCriterion("projectid not between", value1, value2, "projectid");
            return (Criteria) this;
        }

        public Criteria andBuildingnameIsNull() {
            addCriterion("buildingname is null");
            return (Criteria) this;
        }

        public Criteria andBuildingnameIsNotNull() {
            addCriterion("buildingname is not null");
            return (Criteria) this;
        }

        public Criteria andBuildingnameEqualTo(String value) {
            addCriterion("buildingname =", value, "buildingname");
            return (Criteria) this;
        }

        public Criteria andBuildingnameNotEqualTo(String value) {
            addCriterion("buildingname <>", value, "buildingname");
            return (Criteria) this;
        }

        public Criteria andBuildingnameGreaterThan(String value) {
            addCriterion("buildingname >", value, "buildingname");
            return (Criteria) this;
        }

        public Criteria andBuildingnameGreaterThanOrEqualTo(String value) {
            addCriterion("buildingname >=", value, "buildingname");
            return (Criteria) this;
        }

        public Criteria andBuildingnameLessThan(String value) {
            addCriterion("buildingname <", value, "buildingname");
            return (Criteria) this;
        }

        public Criteria andBuildingnameLessThanOrEqualTo(String value) {
            addCriterion("buildingname <=", value, "buildingname");
            return (Criteria) this;
        }

        public Criteria andBuildingnameLike(String value) {
            addCriterion("buildingname like", value, "buildingname");
            return (Criteria) this;
        }

        public Criteria andBuildingnameNotLike(String value) {
            addCriterion("buildingname not like", value, "buildingname");
            return (Criteria) this;
        }

        public Criteria andBuildingnameIn(List<String> values) {
            addCriterion("buildingname in", values, "buildingname");
            return (Criteria) this;
        }

        public Criteria andBuildingnameNotIn(List<String> values) {
            addCriterion("buildingname not in", values, "buildingname");
            return (Criteria) this;
        }

        public Criteria andBuildingnameBetween(String value1, String value2) {
            addCriterion("buildingname between", value1, value2, "buildingname");
            return (Criteria) this;
        }

        public Criteria andBuildingnameNotBetween(String value1, String value2) {
            addCriterion("buildingname not between", value1, value2, "buildingname");
            return (Criteria) this;
        }

        public Criteria andLocationIsNull() {
            addCriterion("location is null");
            return (Criteria) this;
        }

        public Criteria andLocationIsNotNull() {
            addCriterion("location is not null");
            return (Criteria) this;
        }

        public Criteria andLocationEqualTo(String value) {
            addCriterion("location =", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationNotEqualTo(String value) {
            addCriterion("location <>", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationGreaterThan(String value) {
            addCriterion("location >", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationGreaterThanOrEqualTo(String value) {
            addCriterion("location >=", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationLessThan(String value) {
            addCriterion("location <", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationLessThanOrEqualTo(String value) {
            addCriterion("location <=", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationLike(String value) {
            addCriterion("location like", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationNotLike(String value) {
            addCriterion("location not like", value, "location");
            return (Criteria) this;
        }

        public Criteria andLocationIn(List<String> values) {
            addCriterion("location in", values, "location");
            return (Criteria) this;
        }

        public Criteria andLocationNotIn(List<String> values) {
            addCriterion("location not in", values, "location");
            return (Criteria) this;
        }

        public Criteria andLocationBetween(String value1, String value2) {
            addCriterion("location between", value1, value2, "location");
            return (Criteria) this;
        }

        public Criteria andLocationNotBetween(String value1, String value2) {
            addCriterion("location not between", value1, value2, "location");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNull() {
            addCriterion("createtime is null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNotNull() {
            addCriterion("createtime is not null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeEqualTo(String value) {
            addCriterion("createtime =", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotEqualTo(String value) {
            addCriterion("createtime <>", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThan(String value) {
            addCriterion("createtime >", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThanOrEqualTo(String value) {
            addCriterion("createtime >=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThan(String value) {
            addCriterion("createtime <", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThanOrEqualTo(String value) {
            addCriterion("createtime <=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLike(String value) {
            addCriterion("createtime like", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotLike(String value) {
            addCriterion("createtime not like", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIn(List<String> values) {
            addCriterion("createtime in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotIn(List<String> values) {
            addCriterion("createtime not in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeBetween(String value1, String value2) {
            addCriterion("createtime between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotBetween(String value1, String value2) {
            addCriterion("createtime not between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andBuildingtypeIsNull() {
            addCriterion("buildingtype is null");
            return (Criteria) this;
        }

        public Criteria andBuildingtypeIsNotNull() {
            addCriterion("buildingtype is not null");
            return (Criteria) this;
        }

        public Criteria andBuildingtypeEqualTo(String value) {
            addCriterion("buildingtype =", value, "buildingtype");
            return (Criteria) this;
        }

        public Criteria andBuildingtypeNotEqualTo(String value) {
            addCriterion("buildingtype <>", value, "buildingtype");
            return (Criteria) this;
        }

        public Criteria andBuildingtypeGreaterThan(String value) {
            addCriterion("buildingtype >", value, "buildingtype");
            return (Criteria) this;
        }

        public Criteria andBuildingtypeGreaterThanOrEqualTo(String value) {
            addCriterion("buildingtype >=", value, "buildingtype");
            return (Criteria) this;
        }

        public Criteria andBuildingtypeLessThan(String value) {
            addCriterion("buildingtype <", value, "buildingtype");
            return (Criteria) this;
        }

        public Criteria andBuildingtypeLessThanOrEqualTo(String value) {
            addCriterion("buildingtype <=", value, "buildingtype");
            return (Criteria) this;
        }

        public Criteria andBuildingtypeLike(String value) {
            addCriterion("buildingtype like", value, "buildingtype");
            return (Criteria) this;
        }

        public Criteria andBuildingtypeNotLike(String value) {
            addCriterion("buildingtype not like", value, "buildingtype");
            return (Criteria) this;
        }

        public Criteria andBuildingtypeIn(List<String> values) {
            addCriterion("buildingtype in", values, "buildingtype");
            return (Criteria) this;
        }

        public Criteria andBuildingtypeNotIn(List<String> values) {
            addCriterion("buildingtype not in", values, "buildingtype");
            return (Criteria) this;
        }

        public Criteria andBuildingtypeBetween(String value1, String value2) {
            addCriterion("buildingtype between", value1, value2, "buildingtype");
            return (Criteria) this;
        }

        public Criteria andBuildingtypeNotBetween(String value1, String value2) {
            addCriterion("buildingtype not between", value1, value2, "buildingtype");
            return (Criteria) this;
        }

        public Criteria andUsageIsNull() {
            addCriterion("usage is null");
            return (Criteria) this;
        }

        public Criteria andUsageIsNotNull() {
            addCriterion("usage is not null");
            return (Criteria) this;
        }

        public Criteria andUsageEqualTo(String value) {
            addCriterion("usage =", value, "usage");
            return (Criteria) this;
        }

        public Criteria andUsageNotEqualTo(String value) {
            addCriterion("usage <>", value, "usage");
            return (Criteria) this;
        }

        public Criteria andUsageGreaterThan(String value) {
            addCriterion("usage >", value, "usage");
            return (Criteria) this;
        }

        public Criteria andUsageGreaterThanOrEqualTo(String value) {
            addCriterion("usage >=", value, "usage");
            return (Criteria) this;
        }

        public Criteria andUsageLessThan(String value) {
            addCriterion("usage <", value, "usage");
            return (Criteria) this;
        }

        public Criteria andUsageLessThanOrEqualTo(String value) {
            addCriterion("usage <=", value, "usage");
            return (Criteria) this;
        }

        public Criteria andUsageLike(String value) {
            addCriterion("usage like", value, "usage");
            return (Criteria) this;
        }

        public Criteria andUsageNotLike(String value) {
            addCriterion("usage not like", value, "usage");
            return (Criteria) this;
        }

        public Criteria andUsageIn(List<String> values) {
            addCriterion("usage in", values, "usage");
            return (Criteria) this;
        }

        public Criteria andUsageNotIn(List<String> values) {
            addCriterion("usage not in", values, "usage");
            return (Criteria) this;
        }

        public Criteria andUsageBetween(String value1, String value2) {
            addCriterion("usage between", value1, value2, "usage");
            return (Criteria) this;
        }

        public Criteria andUsageNotBetween(String value1, String value2) {
            addCriterion("usage not between", value1, value2, "usage");
            return (Criteria) this;
        }

        public Criteria andBuiltupareaIsNull() {
            addCriterion("builtuparea is null");
            return (Criteria) this;
        }

        public Criteria andBuiltupareaIsNotNull() {
            addCriterion("builtuparea is not null");
            return (Criteria) this;
        }

        public Criteria andBuiltupareaEqualTo(String value) {
            addCriterion("builtuparea =", value, "builtuparea");
            return (Criteria) this;
        }

        public Criteria andBuiltupareaNotEqualTo(String value) {
            addCriterion("builtuparea <>", value, "builtuparea");
            return (Criteria) this;
        }

        public Criteria andBuiltupareaGreaterThan(String value) {
            addCriterion("builtuparea >", value, "builtuparea");
            return (Criteria) this;
        }

        public Criteria andBuiltupareaGreaterThanOrEqualTo(String value) {
            addCriterion("builtuparea >=", value, "builtuparea");
            return (Criteria) this;
        }

        public Criteria andBuiltupareaLessThan(String value) {
            addCriterion("builtuparea <", value, "builtuparea");
            return (Criteria) this;
        }

        public Criteria andBuiltupareaLessThanOrEqualTo(String value) {
            addCriterion("builtuparea <=", value, "builtuparea");
            return (Criteria) this;
        }

        public Criteria andBuiltupareaLike(String value) {
            addCriterion("builtuparea like", value, "builtuparea");
            return (Criteria) this;
        }

        public Criteria andBuiltupareaNotLike(String value) {
            addCriterion("builtuparea not like", value, "builtuparea");
            return (Criteria) this;
        }

        public Criteria andBuiltupareaIn(List<String> values) {
            addCriterion("builtuparea in", values, "builtuparea");
            return (Criteria) this;
        }

        public Criteria andBuiltupareaNotIn(List<String> values) {
            addCriterion("builtuparea not in", values, "builtuparea");
            return (Criteria) this;
        }

        public Criteria andBuiltupareaBetween(String value1, String value2) {
            addCriterion("builtuparea between", value1, value2, "builtuparea");
            return (Criteria) this;
        }

        public Criteria andBuiltupareaNotBetween(String value1, String value2) {
            addCriterion("builtuparea not between", value1, value2, "builtuparea");
            return (Criteria) this;
        }

        public Criteria andHeightIsNull() {
            addCriterion("height is null");
            return (Criteria) this;
        }

        public Criteria andHeightIsNotNull() {
            addCriterion("height is not null");
            return (Criteria) this;
        }

        public Criteria andHeightEqualTo(String value) {
            addCriterion("height =", value, "height");
            return (Criteria) this;
        }

        public Criteria andHeightNotEqualTo(String value) {
            addCriterion("height <>", value, "height");
            return (Criteria) this;
        }

        public Criteria andHeightGreaterThan(String value) {
            addCriterion("height >", value, "height");
            return (Criteria) this;
        }

        public Criteria andHeightGreaterThanOrEqualTo(String value) {
            addCriterion("height >=", value, "height");
            return (Criteria) this;
        }

        public Criteria andHeightLessThan(String value) {
            addCriterion("height <", value, "height");
            return (Criteria) this;
        }

        public Criteria andHeightLessThanOrEqualTo(String value) {
            addCriterion("height <=", value, "height");
            return (Criteria) this;
        }

        public Criteria andHeightLike(String value) {
            addCriterion("height like", value, "height");
            return (Criteria) this;
        }

        public Criteria andHeightNotLike(String value) {
            addCriterion("height not like", value, "height");
            return (Criteria) this;
        }

        public Criteria andHeightIn(List<String> values) {
            addCriterion("height in", values, "height");
            return (Criteria) this;
        }

        public Criteria andHeightNotIn(List<String> values) {
            addCriterion("height not in", values, "height");
            return (Criteria) this;
        }

        public Criteria andHeightBetween(String value1, String value2) {
            addCriterion("height between", value1, value2, "height");
            return (Criteria) this;
        }

        public Criteria andHeightNotBetween(String value1, String value2) {
            addCriterion("height not between", value1, value2, "height");
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

        public Criteria andBaselineIsNull() {
            addCriterion("baseline is null");
            return (Criteria) this;
        }

        public Criteria andBaselineIsNotNull() {
            addCriterion("baseline is not null");
            return (Criteria) this;
        }

        public Criteria andBaselineEqualTo(String value) {
            addCriterion("baseline =", value, "baseline");
            return (Criteria) this;
        }

        public Criteria andBaselineNotEqualTo(String value) {
            addCriterion("baseline <>", value, "baseline");
            return (Criteria) this;
        }

        public Criteria andBaselineGreaterThan(String value) {
            addCriterion("baseline >", value, "baseline");
            return (Criteria) this;
        }

        public Criteria andBaselineGreaterThanOrEqualTo(String value) {
            addCriterion("baseline >=", value, "baseline");
            return (Criteria) this;
        }

        public Criteria andBaselineLessThan(String value) {
            addCriterion("baseline <", value, "baseline");
            return (Criteria) this;
        }

        public Criteria andBaselineLessThanOrEqualTo(String value) {
            addCriterion("baseline <=", value, "baseline");
            return (Criteria) this;
        }

        public Criteria andBaselineLike(String value) {
            addCriterion("baseline like", value, "baseline");
            return (Criteria) this;
        }

        public Criteria andBaselineNotLike(String value) {
            addCriterion("baseline not like", value, "baseline");
            return (Criteria) this;
        }

        public Criteria andBaselineIn(List<String> values) {
            addCriterion("baseline in", values, "baseline");
            return (Criteria) this;
        }

        public Criteria andBaselineNotIn(List<String> values) {
            addCriterion("baseline not in", values, "baseline");
            return (Criteria) this;
        }

        public Criteria andBaselineBetween(String value1, String value2) {
            addCriterion("baseline between", value1, value2, "baseline");
            return (Criteria) this;
        }

        public Criteria andBaselineNotBetween(String value1, String value2) {
            addCriterion("baseline not between", value1, value2, "baseline");
            return (Criteria) this;
        }

        public Criteria andEntitytableIsNull() {
            addCriterion("entitytable is null");
            return (Criteria) this;
        }

        public Criteria andEntitytableIsNotNull() {
            addCriterion("entitytable is not null");
            return (Criteria) this;
        }

        public Criteria andEntitytableEqualTo(String value) {
            addCriterion("entitytable =", value, "entitytable");
            return (Criteria) this;
        }

        public Criteria andEntitytableNotEqualTo(String value) {
            addCriterion("entitytable <>", value, "entitytable");
            return (Criteria) this;
        }

        public Criteria andEntitytableGreaterThan(String value) {
            addCriterion("entitytable >", value, "entitytable");
            return (Criteria) this;
        }

        public Criteria andEntitytableGreaterThanOrEqualTo(String value) {
            addCriterion("entitytable >=", value, "entitytable");
            return (Criteria) this;
        }

        public Criteria andEntitytableLessThan(String value) {
            addCriterion("entitytable <", value, "entitytable");
            return (Criteria) this;
        }

        public Criteria andEntitytableLessThanOrEqualTo(String value) {
            addCriterion("entitytable <=", value, "entitytable");
            return (Criteria) this;
        }

        public Criteria andEntitytableLike(String value) {
            addCriterion("entitytable like", value, "entitytable");
            return (Criteria) this;
        }

        public Criteria andEntitytableNotLike(String value) {
            addCriterion("entitytable not like", value, "entitytable");
            return (Criteria) this;
        }

        public Criteria andEntitytableIn(List<String> values) {
            addCriterion("entitytable in", values, "entitytable");
            return (Criteria) this;
        }

        public Criteria andEntitytableNotIn(List<String> values) {
            addCriterion("entitytable not in", values, "entitytable");
            return (Criteria) this;
        }

        public Criteria andEntitytableBetween(String value1, String value2) {
            addCriterion("entitytable between", value1, value2, "entitytable");
            return (Criteria) this;
        }

        public Criteria andEntitytableNotBetween(String value1, String value2) {
            addCriterion("entitytable not between", value1, value2, "entitytable");
            return (Criteria) this;
        }

        public Criteria andEntitycountIsNull() {
            addCriterion("entitycount is null");
            return (Criteria) this;
        }

        public Criteria andEntitycountIsNotNull() {
            addCriterion("entitycount is not null");
            return (Criteria) this;
        }

        public Criteria andEntitycountEqualTo(String value) {
            addCriterion("entitycount =", value, "entitycount");
            return (Criteria) this;
        }

        public Criteria andEntitycountNotEqualTo(String value) {
            addCriterion("entitycount <>", value, "entitycount");
            return (Criteria) this;
        }

        public Criteria andEntitycountGreaterThan(String value) {
            addCriterion("entitycount >", value, "entitycount");
            return (Criteria) this;
        }

        public Criteria andEntitycountGreaterThanOrEqualTo(String value) {
            addCriterion("entitycount >=", value, "entitycount");
            return (Criteria) this;
        }

        public Criteria andEntitycountLessThan(String value) {
            addCriterion("entitycount <", value, "entitycount");
            return (Criteria) this;
        }

        public Criteria andEntitycountLessThanOrEqualTo(String value) {
            addCriterion("entitycount <=", value, "entitycount");
            return (Criteria) this;
        }

        public Criteria andEntitycountLike(String value) {
            addCriterion("entitycount like", value, "entitycount");
            return (Criteria) this;
        }

        public Criteria andEntitycountNotLike(String value) {
            addCriterion("entitycount not like", value, "entitycount");
            return (Criteria) this;
        }

        public Criteria andEntitycountIn(List<String> values) {
            addCriterion("entitycount in", values, "entitycount");
            return (Criteria) this;
        }

        public Criteria andEntitycountNotIn(List<String> values) {
            addCriterion("entitycount not in", values, "entitycount");
            return (Criteria) this;
        }

        public Criteria andEntitycountBetween(String value1, String value2) {
            addCriterion("entitycount between", value1, value2, "entitycount");
            return (Criteria) this;
        }

        public Criteria andEntitycountNotBetween(String value1, String value2) {
            addCriterion("entitycount not between", value1, value2, "entitycount");
            return (Criteria) this;
        }

        public Criteria andMaterialtableIsNull() {
            addCriterion("materialtable is null");
            return (Criteria) this;
        }

        public Criteria andMaterialtableIsNotNull() {
            addCriterion("materialtable is not null");
            return (Criteria) this;
        }

        public Criteria andMaterialtableEqualTo(String value) {
            addCriterion("materialtable =", value, "materialtable");
            return (Criteria) this;
        }

        public Criteria andMaterialtableNotEqualTo(String value) {
            addCriterion("materialtable <>", value, "materialtable");
            return (Criteria) this;
        }

        public Criteria andMaterialtableGreaterThan(String value) {
            addCriterion("materialtable >", value, "materialtable");
            return (Criteria) this;
        }

        public Criteria andMaterialtableGreaterThanOrEqualTo(String value) {
            addCriterion("materialtable >=", value, "materialtable");
            return (Criteria) this;
        }

        public Criteria andMaterialtableLessThan(String value) {
            addCriterion("materialtable <", value, "materialtable");
            return (Criteria) this;
        }

        public Criteria andMaterialtableLessThanOrEqualTo(String value) {
            addCriterion("materialtable <=", value, "materialtable");
            return (Criteria) this;
        }

        public Criteria andMaterialtableLike(String value) {
            addCriterion("materialtable like", value, "materialtable");
            return (Criteria) this;
        }

        public Criteria andMaterialtableNotLike(String value) {
            addCriterion("materialtable not like", value, "materialtable");
            return (Criteria) this;
        }

        public Criteria andMaterialtableIn(List<String> values) {
            addCriterion("materialtable in", values, "materialtable");
            return (Criteria) this;
        }

        public Criteria andMaterialtableNotIn(List<String> values) {
            addCriterion("materialtable not in", values, "materialtable");
            return (Criteria) this;
        }

        public Criteria andMaterialtableBetween(String value1, String value2) {
            addCriterion("materialtable between", value1, value2, "materialtable");
            return (Criteria) this;
        }

        public Criteria andMaterialtableNotBetween(String value1, String value2) {
            addCriterion("materialtable not between", value1, value2, "materialtable");
            return (Criteria) this;
        }

        public Criteria andGeometrytableIsNull() {
            addCriterion("geometrytable is null");
            return (Criteria) this;
        }

        public Criteria andGeometrytableIsNotNull() {
            addCriterion("geometrytable is not null");
            return (Criteria) this;
        }

        public Criteria andGeometrytableEqualTo(String value) {
            addCriterion("geometrytable =", value, "geometrytable");
            return (Criteria) this;
        }

        public Criteria andGeometrytableNotEqualTo(String value) {
            addCriterion("geometrytable <>", value, "geometrytable");
            return (Criteria) this;
        }

        public Criteria andGeometrytableGreaterThan(String value) {
            addCriterion("geometrytable >", value, "geometrytable");
            return (Criteria) this;
        }

        public Criteria andGeometrytableGreaterThanOrEqualTo(String value) {
            addCriterion("geometrytable >=", value, "geometrytable");
            return (Criteria) this;
        }

        public Criteria andGeometrytableLessThan(String value) {
            addCriterion("geometrytable <", value, "geometrytable");
            return (Criteria) this;
        }

        public Criteria andGeometrytableLessThanOrEqualTo(String value) {
            addCriterion("geometrytable <=", value, "geometrytable");
            return (Criteria) this;
        }

        public Criteria andGeometrytableLike(String value) {
            addCriterion("geometrytable like", value, "geometrytable");
            return (Criteria) this;
        }

        public Criteria andGeometrytableNotLike(String value) {
            addCriterion("geometrytable not like", value, "geometrytable");
            return (Criteria) this;
        }

        public Criteria andGeometrytableIn(List<String> values) {
            addCriterion("geometrytable in", values, "geometrytable");
            return (Criteria) this;
        }

        public Criteria andGeometrytableNotIn(List<String> values) {
            addCriterion("geometrytable not in", values, "geometrytable");
            return (Criteria) this;
        }

        public Criteria andGeometrytableBetween(String value1, String value2) {
            addCriterion("geometrytable between", value1, value2, "geometrytable");
            return (Criteria) this;
        }

        public Criteria andGeometrytableNotBetween(String value1, String value2) {
            addCriterion("geometrytable not between", value1, value2, "geometrytable");
            return (Criteria) this;
        }

        public Criteria andGeometrytypeIsNull() {
            addCriterion("geometrytype is null");
            return (Criteria) this;
        }

        public Criteria andGeometrytypeIsNotNull() {
            addCriterion("geometrytype is not null");
            return (Criteria) this;
        }

        public Criteria andGeometrytypeEqualTo(String value) {
            addCriterion("geometrytype =", value, "geometrytype");
            return (Criteria) this;
        }

        public Criteria andGeometrytypeNotEqualTo(String value) {
            addCriterion("geometrytype <>", value, "geometrytype");
            return (Criteria) this;
        }

        public Criteria andGeometrytypeGreaterThan(String value) {
            addCriterion("geometrytype >", value, "geometrytype");
            return (Criteria) this;
        }

        public Criteria andGeometrytypeGreaterThanOrEqualTo(String value) {
            addCriterion("geometrytype >=", value, "geometrytype");
            return (Criteria) this;
        }

        public Criteria andGeometrytypeLessThan(String value) {
            addCriterion("geometrytype <", value, "geometrytype");
            return (Criteria) this;
        }

        public Criteria andGeometrytypeLessThanOrEqualTo(String value) {
            addCriterion("geometrytype <=", value, "geometrytype");
            return (Criteria) this;
        }

        public Criteria andGeometrytypeLike(String value) {
            addCriterion("geometrytype like", value, "geometrytype");
            return (Criteria) this;
        }

        public Criteria andGeometrytypeNotLike(String value) {
            addCriterion("geometrytype not like", value, "geometrytype");
            return (Criteria) this;
        }

        public Criteria andGeometrytypeIn(List<String> values) {
            addCriterion("geometrytype in", values, "geometrytype");
            return (Criteria) this;
        }

        public Criteria andGeometrytypeNotIn(List<String> values) {
            addCriterion("geometrytype not in", values, "geometrytype");
            return (Criteria) this;
        }

        public Criteria andGeometrytypeBetween(String value1, String value2) {
            addCriterion("geometrytype between", value1, value2, "geometrytype");
            return (Criteria) this;
        }

        public Criteria andGeometrytypeNotBetween(String value1, String value2) {
            addCriterion("geometrytype not between", value1, value2, "geometrytype");
            return (Criteria) this;
        }

        public Criteria andBuildingecoindexIsNull() {
            addCriterion("buildingecoindex is null");
            return (Criteria) this;
        }

        public Criteria andBuildingecoindexIsNotNull() {
            addCriterion("buildingecoindex is not null");
            return (Criteria) this;
        }

        public Criteria andBuildingecoindexEqualTo(String value) {
            addCriterion("buildingecoindex =", value, "buildingecoindex");
            return (Criteria) this;
        }

        public Criteria andBuildingecoindexNotEqualTo(String value) {
            addCriterion("buildingecoindex <>", value, "buildingecoindex");
            return (Criteria) this;
        }

        public Criteria andBuildingecoindexGreaterThan(String value) {
            addCriterion("buildingecoindex >", value, "buildingecoindex");
            return (Criteria) this;
        }

        public Criteria andBuildingecoindexGreaterThanOrEqualTo(String value) {
            addCriterion("buildingecoindex >=", value, "buildingecoindex");
            return (Criteria) this;
        }

        public Criteria andBuildingecoindexLessThan(String value) {
            addCriterion("buildingecoindex <", value, "buildingecoindex");
            return (Criteria) this;
        }

        public Criteria andBuildingecoindexLessThanOrEqualTo(String value) {
            addCriterion("buildingecoindex <=", value, "buildingecoindex");
            return (Criteria) this;
        }

        public Criteria andBuildingecoindexLike(String value) {
            addCriterion("buildingecoindex like", value, "buildingecoindex");
            return (Criteria) this;
        }

        public Criteria andBuildingecoindexNotLike(String value) {
            addCriterion("buildingecoindex not like", value, "buildingecoindex");
            return (Criteria) this;
        }

        public Criteria andBuildingecoindexIn(List<String> values) {
            addCriterion("buildingecoindex in", values, "buildingecoindex");
            return (Criteria) this;
        }

        public Criteria andBuildingecoindexNotIn(List<String> values) {
            addCriterion("buildingecoindex not in", values, "buildingecoindex");
            return (Criteria) this;
        }

        public Criteria andBuildingecoindexBetween(String value1, String value2) {
            addCriterion("buildingecoindex between", value1, value2, "buildingecoindex");
            return (Criteria) this;
        }

        public Criteria andBuildingecoindexNotBetween(String value1, String value2) {
            addCriterion("buildingecoindex not between", value1, value2, "buildingecoindex");
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