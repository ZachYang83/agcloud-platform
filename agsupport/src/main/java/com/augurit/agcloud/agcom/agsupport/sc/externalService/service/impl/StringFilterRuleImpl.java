package com.augurit.agcloud.agcom.agsupport.sc.externalService.service.impl;

import com.augurit.agcloud.agcom.agsupport.sc.externalService.service.AbstractFilterRule;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringFilterRuleImpl extends AbstractFilterRule {

    @Override
    public boolean handle(String content) {
        content=content.trim();
        boolean result = true;

        try {
            String expression = getCompareExpression().trim();
            if(expression.contains(" and ")){
                String[] arrExpression = expression.split(" and ");
                for(String expr : arrExpression){
                    result= result && compare(content,expr);
                }
            }
            else if(expression.contains(" or ")){
                String[] arrExpression = expression.split(" or ");
                for(String expr : arrExpression){
                    result= result || compare(content,expr);
                }
            }
            else{
                result= compare(content,expression);
            }

        }
        catch(Exception e){
        }
        return result;
    }

    private boolean compare(String content,String expression){
        boolean result = false;
        if(expression.toLowerCase().startsWith("like")){
            String compareValue = expression.substring(4).trim();//截取比较的内容
            result = content.contains(compareValue);
        }
        else if(expression.startsWith("=")){
            String compareValue = expression.substring(1).trim();//截取比较的内容
            result = content.equals(compareValue);
        }
        else if(expression.startsWith("!=")){
            String compareValue = expression.substring(2).trim();//截取比较的内容
            result = !content.equals(compareValue);
        }
        return  result;
    }
}
