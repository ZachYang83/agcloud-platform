package com.augurit.agcloud.agcom.agsupport.sc.externalService.service.impl;

import com.augurit.agcloud.agcom.agsupport.sc.externalService.service.AbstractChangeRule;
import com.augurit.agcloud.agcom.agsupport.sc.externalService.service.AbstractFilterRule;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFilterRuleImpl extends AbstractFilterRule {

    @Override
    public boolean handle(String content) {
        content=content.trim();
        boolean result = true;
        try {
            Date dateFromContent = parseDate(content);
            String expression = getCompareExpression();
            if(expression.contains(" and ")){
                String[] arrExpression = expression.split(" and ");
                for(String expr : arrExpression){
                    result= result && compare(dateFromContent,expr);
                }
            }
            else if(expression.contains(" or ")){
                String[] arrExpression = expression.split(" or ");
                for(String expr : arrExpression){
                    result= result || compare(dateFromContent,expr);
                }
            }
            else{
                result = compare(dateFromContent,expression);
            }
        }
        catch(Exception e){
        }
        return result;
    }

    private boolean compare(Date dateFromContent,String expression) throws Exception{
        boolean result = false;
        if(expression.startsWith(">=")){
            String compareValue = expression.substring(2).trim();//截取比较的内容
            Date dateFromCompareValue = parseDate(compareValue);
            int compareResult = dateFromContent.compareTo(dateFromCompareValue);
            result = compareResult>=0;
        }
        else if(expression.startsWith("<=")){
            String compareValue = expression.substring(2).trim();//截取比较的内容
            Date dateFromCompareValue = parseDate(compareValue);
            int compareResult = dateFromContent.compareTo(dateFromCompareValue);
            result = compareResult<=0;
        }
        else if(expression.startsWith(">")){
            String compareValue = expression.substring(1).trim();//截取比较的内容
            Date dateFromCompareValue = parseDate(compareValue);
            int compareResult = dateFromContent.compareTo(dateFromCompareValue);
            result = compareResult>0;
        }
        else if(expression.startsWith("<")){
            String compareValue = expression.substring(1).trim();//截取比较的内容
            Date dateFromCompareValue = parseDate(compareValue);
            int compareResult = dateFromContent.compareTo(dateFromCompareValue);
            result = compareResult<0;
        }
        else if(expression.startsWith("=")){
            String compareValue = expression.substring(1).trim();//截取比较的内容
            Date dateFromCompareValue = parseDate(compareValue);
            int compareResult = dateFromContent.compareTo(dateFromCompareValue);
            result = compareResult==0;
        }
        else if(expression.startsWith("!=")){
            String compareValue = expression.substring(2).trim();//截取比较的内容
            Date dateFromCompareValue = parseDate(compareValue);
            int compareResult = dateFromContent.compareTo(dateFromCompareValue);
            result = compareResult!=0;
        }
        return result;
    }

    private String getFormateString(String content){
        String formatString = "";
        if(content.contains("/")){
            formatString += "yyyy/MM/dd";
        }
        else {
            formatString += "yyyy-MM-dd";
        }

        if(content.contains(" ")){
            formatString += " HH:mm:ss";
        }
        return formatString;
    }

    private Date parseDate(String content) throws Exception{
        String formatString = getFormateString(content);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
        Date date = simpleDateFormat.parse(content);
        return  date;
    }
}
