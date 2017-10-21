package com.inno.service.ServiceImpl;

import com.inno.bean.ReadTableBean;
import com.inno.bean.TongbuBean;
import com.inno.bean.WriteTableBean;
import com.inno.dao.Impl.TongbuDaoImpl;
import com.inno.dao.TongbuDao;
import com.inno.service.TongbuService;
import org.dom4j.DocumentException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TongbuServiceImpl implements TongbuService {
    TongbuDao t=new TongbuDaoImpl();


    @Override
    public List<Map<String, Object>> find(TongbuBean.Read be) throws DocumentException {
        Map<String, String> map;
        map = new HashMap<String, String>();
        map.put("tablename", be.getTablename());
        map.put("loadtime", be.getLoadtime());
        map.put("betime", be.getTt());
        map.put("lim", String.valueOf(be.getA()));

        return t.findTimeList(map);
    }

    @Override
    public void insert(List<Map<String, Object>> map, TongbuBean.Write w) {
        t.insertList(map,w);
    }

    @Override
    public void delete(List<Map<String, Object>> map, TongbuBean.Write w) {
        t.delete(map,w);
    }
}
