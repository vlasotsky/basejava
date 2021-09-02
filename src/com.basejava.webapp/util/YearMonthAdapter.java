package com.basejava.webapp.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.YearMonth;

public class YearMonthAdapter extends XmlAdapter<String, YearMonth> {
    @Override
    public YearMonth unmarshal(String s) throws Exception {
        return YearMonth.parse(s);
    }

    @Override
    public String marshal(YearMonth yearMonth) throws Exception {
        return yearMonth.toString();
    }
}
