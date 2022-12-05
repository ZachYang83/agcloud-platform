package com.augurit.agcloud.agcom.agsupport.sc.externalService.service.impl;

import com.augurit.agcloud.agcom.agsupport.sc.externalService.service.AbstractChangeRule;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateChangeRuleImpl extends AbstractChangeRule {

    @Override
    public String handle(String content) {
        String result = null;
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
        try {
            Date date = simpleDateFormat.parse(content);
            if("time".contains(this.getFormat())){
                result = String.valueOf(date.getTime());
            }
        }
        catch(Exception e){
            result = content;
        }

        return result;
    }
}
