package com.inno.service;

import com.inno.bean.ReadTableBean;
import org.dom4j.DocumentException;

import java.util.List;
import java.util.Map;

public interface ReadService {
    public List<Map<String,Object>> find(ReadTableBean be,String source) throws DocumentException;
}
