package com.inno.service;

import com.inno.bean.ReadTableBean;
import com.inno.bean.TongbuBean;
import com.inno.bean.WriteTableBean;
import com.inno.controller.CleanController.Tuichu;
import org.dom4j.DocumentException;

import java.util.List;
import java.util.Map;

public interface TongbuService {
    public List<Map<String,Object>> find(TongbuBean.Read be) throws DocumentException;
    public void insert(List<Map<String,Object>> map, TongbuBean.Write w);
    public void delete(List<Map<String,Object>> map, TongbuBean.Write w);
}
