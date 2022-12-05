package com.augurit.agcloud.agcom.agsupport.sc.spatial.util;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 将sql语句转换成mongodb语句
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-05-11.
 */
public class FormatSQLUtil {
    public static BasicDBObject formatSelect(List selectList, String byName) {
        BasicDBObject selectField = new BasicDBObject();
        try {
            for (Object selectObj : selectList) {
                String selectStr = selectObj.toString();
                if (selectStr.indexOf("*") >= 0) {
                    selectField = new BasicDBObject();
                    break;
                }
                if (byName != null) {
                    selectStr = selectStr.replaceAll(byName + "\\.", "");
                }
                String str = selectStr.split("AS")[0].replaceAll(" ", "");
                selectField.put(str, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectField;
    }

    private static List formatInValues(String inWhere) {
        List inList = new ArrayList();
        String[] values = inWhere.substring(inWhere.indexOf("(") + 1, inWhere.indexOf(")"))
                .replaceAll(", ", ",").replaceAll(" ,", ",").replaceAll("'", "").split(",");
        for (String v : values) {
            inList.add(v);
            try {
                inList.add(Double.valueOf(v));
            } catch (Exception e) {
            }
        }
        return inList;
    }

    private static BasicDBObject formatWhereChild(String whereChild) {
        BasicDBObject childField = new BasicDBObject();
        try {
            if (whereChild.startsWith("(")) {
                childField = formatWhere(whereChild);
            } else {
                String[] strs = whereChild.replaceAll("'", "").split(" ");
                if (strs.length >= 3) {
                    //因为mongodb查询区分字符串和数字 所以在组装语句时 考虑两种情况
                    if (strs[1].equals("=")) {
                        // a = b
                        try {
                            BasicDBObject dbObj1 = new BasicDBObject(strs[0], strs[2]);
                            BasicDBObject dbObj2 = new BasicDBObject(strs[0], Double.valueOf(strs[2]));
                            childField = new BasicDBObject(QueryOperators.OR, Arrays.asList(dbObj1, dbObj2));
                        } catch (Exception e) {
                            childField = new BasicDBObject(strs[0], strs[2]);
                        }
                        if (strs[0].equals(strs[2])) {
                            childField = new BasicDBObject();
                        }
                    } else if (strs[1].equals("!=")) {
                        // a != b
                        try {
                            BasicDBObject dbObj1 = new BasicDBObject(strs[0], new BasicDBObject(QueryOperators.NE, strs[2]));
                            BasicDBObject dbObj2 = new BasicDBObject(strs[0], new BasicDBObject(QueryOperators.NE, Double.valueOf(strs[2])));
                            childField = new BasicDBObject(QueryOperators.AND, Arrays.asList(dbObj1, dbObj2));
                        } catch (Exception e) {
                            childField = new BasicDBObject(strs[0], new BasicDBObject(QueryOperators.NE, strs[2]));
                        }
                    } else if (strs[1].equals(">")) {
                        // a > b
                        try {
                            childField = new BasicDBObject(strs[0], new BasicDBObject(QueryOperators.GT, Double.valueOf(strs[2])));
                        } catch (Exception e) {
                            childField = new BasicDBObject(strs[0], new BasicDBObject(QueryOperators.GT, strs[2]));
                        }
                    } else if (strs[1].equals("<")) {
                        // a < b
                        try {
                            childField = new BasicDBObject(strs[0], new BasicDBObject(QueryOperators.LT, Double.valueOf(strs[2])));
                        } catch (Exception e) {
                            childField = new BasicDBObject(strs[0], new BasicDBObject(QueryOperators.LT, strs[2]));
                        }
                    } else if (strs[1].equals(">=")) {
                        // a >= b
                        try {
                            childField = new BasicDBObject(strs[0], new BasicDBObject(QueryOperators.GTE, Double.valueOf(strs[2])));
                        } catch (Exception e) {
                            childField = new BasicDBObject(strs[0], new BasicDBObject(QueryOperators.GTE, strs[2]));
                        }
                    } else if (strs[1].equals("<=")) {
                        // a <= b
                        try {
                            childField = new BasicDBObject(strs[0], new BasicDBObject(QueryOperators.LTE, Double.valueOf(strs[2])));
                        } catch (Exception e) {
                            childField = new BasicDBObject(strs[0], new BasicDBObject(QueryOperators.LTE, strs[2]));
                        }
                    } else if (strs[1].equals("IN")) {
                        // a IN (b,c,d)
                        childField = new BasicDBObject(strs[0], new BasicDBObject(QueryOperators.IN, formatInValues(whereChild)));
                    } else if (strs[1].equals("NOT") && strs[2].equals("IN")) {
                        // a NOT IN (b,c,d)
                        childField = new BasicDBObject(strs[0], new BasicDBObject(QueryOperators.NIN, formatInValues(whereChild)));
                    } else if (strs[1].equals("LIKE")) {
                        // a LIKE b
                        Pattern pattern = Pattern.compile("^" + strs[2].replaceAll("%", ".*") + "$", Pattern.CASE_INSENSITIVE);
                        childField.put(strs[0], pattern);
                    } else if (strs[1].equals("IS") && strs[2].equals("NULL")) {
                        // a IS NULL
                        childField = new BasicDBObject(strs[0], null);
                    } else if (strs[1].equals("IS") && strs[2].equals("NOT") && strs[3].equals("NULL")) {
                        // a IS NOT NULL
                        childField = new BasicDBObject(strs[0], new BasicDBObject(QueryOperators.NE, null));
                    } else if (strs[1].equals("EXISTS")) {
                        // a exists
                        childField = new BasicDBObject(strs[0], new BasicDBObject(QueryOperators.EXISTS, true));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return childField;
    }

    public static BasicDBObject formatWhere(String where) {
        BasicDBObject whereField = new BasicDBObject();
        try {
            if (where != null && !where.equals("")) {
                String sql = "select * from a where " + where;
                where = new MySqlStatementParser(sql).parseStatementList().get(0).toString();
                String[] wheres = where.substring(where.indexOf("WHERE") + 6).replaceAll("\\n[\\t]{2,}", " ").split("\\n[\\t]{0,1}");
                for (int i = 0; i < wheres.length; i++) {
                    String str = wheres[i];
                    if (str.startsWith("AND")) {
                        str = str.replace("AND ", "");
                        whereField = new BasicDBObject(QueryOperators.AND, Arrays.asList(whereField, formatWhereChild(str)));
                    } else if (str.startsWith("OR")) {
                        str = str.replace("OR ", "");
                        whereField = new BasicDBObject(QueryOperators.OR, Arrays.asList(whereField, formatWhereChild(str)));
                    } else {
                        whereField = formatWhereChild(str);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return whereField;
    }

    public static BasicDBObject formatSort(List<SQLSelectOrderByItem> orderByItemList, String byName) {
        BasicDBObject sortField = new BasicDBObject();
        try {
            for (SQLSelectOrderByItem sortItem : orderByItemList) {
                String item = sortItem.getExpr().toString();
                if (byName != null) {
                    item = item.replaceAll(byName + "\\.", "");
                }
                if ("DESC".equals(sortItem.getType() == null ? "" : sortItem.getType().name)) {
                    sortField.put(item, -1);
                } else {
                    sortField.put(item, 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sortField;
    }

    public static String getWhereString(SQLExpr sqlWhere, String byName) {
        String where = "";
        try {
            try {
                SQLInListExpr sqlInListExpr = (SQLInListExpr) sqlWhere;
                where = sqlInListExpr.getExpr().toString() + (sqlInListExpr.isNot() ? " NOT IN (" : " IN (");
                for (SQLExpr value : sqlInListExpr.getTargetList()) {
                    where += value.toString() + ",";
                }
                where = where.substring(0, where.length() - 1) + ")";
            } catch (Exception e) {
                where = sqlWhere.toString();
            }
        } catch (Exception e) {

        }
        if (byName != null) {
            where = where.replaceAll(byName + "\\.", "");
        }
        return where;
    }
}
