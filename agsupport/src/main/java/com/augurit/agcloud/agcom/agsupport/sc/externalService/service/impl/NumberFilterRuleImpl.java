package com.augurit.agcloud.agcom.agsupport.sc.externalService.service.impl;

import com.augurit.agcloud.agcom.agsupport.sc.externalService.service.AbstractFilterRule;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NumberFilterRuleImpl extends AbstractFilterRule {

    @Override
    public boolean handle(String content) {
        content=content.trim();
        boolean result = true;

        try {
            float floatContent = Float.parseFloat(content);
            String expression = getCompareExpression();
            if(expression.contains(" and ")){
                String[] arrExpression = expression.split(" and ");
                for(String expr : arrExpression){
                    result= result && compare(floatContent,expr);
                }
            }
            else if(expression.contains(" or ")){
                String[] arrExpression = expression.split(" or ");
                for(String expr : arrExpression){
                    result= result || compare(floatContent,expr);
                }
            }
            else {
                result = compare(floatContent,expression);
            }
        }
        catch(Exception e){
        }
        return result;
    }

    private boolean compare(float floatContent,String expression){
        boolean result =false;
        if(expression.startsWith(">=")){
            String compareValue = expression.substring(2).trim();//截取比较的内容
            float floatCompareValue = Float.parseFloat(compareValue);
            result = floatContent>=floatCompareValue;
        }
        else if(expression.startsWith("<=")){
            String compareValue = expression.substring(2).trim();//截取比较的内容
            float floatCompareValue = Float.parseFloat(compareValue);
            result = floatContent<=floatCompareValue;
        }
        else if(expression.startsWith(">")){
            String compareValue = expression.substring(1).trim();//截取比较的内容
            float floatCompareValue = Float.parseFloat(compareValue);
            result = floatContent>floatCompareValue;
        }
        else if(expression.startsWith("<")){
            String compareValue = expression.substring(1).trim();//截取比较的内容
            float floatCompareValue = Float.parseFloat(compareValue);
            result = floatContent<floatCompareValue;
        }
        else if(expression.startsWith("=")){
            String compareValue = expression.substring(1).trim();//截取比较的内容
            float floatCompareValue = Float.parseFloat(compareValue);
            result = floatContent==floatCompareValue;
        }
        else if(expression.startsWith("!=")){
            String compareValue = expression.substring(2).trim();//截取比较的内容
            float floatCompareValue = Float.parseFloat(compareValue);
            result = floatContent!=floatCompareValue;
        }
        return result;
    }
}
