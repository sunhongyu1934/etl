package com.inno.service.ServiceImpl;

import com.inno.bean.ReadTableBean;
import com.inno.dao.Impl.ReadDaoImpl;
import com.inno.dao.ReadDao;
import com.inno.service.ReadService;
import com.inno.utils.ConfigureUtils.company_parse;
import org.dom4j.DocumentException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadServiceImpl implements ReadService {
    ReadDao re=new ReadDaoImpl();

    @Override
    public List<Map<String, Object>> find(ReadTableBean be,String source) throws DocumentException {
        Map<String,String> map;
        map=new HashMap<String,String>();
        map.put("tablename",be.getTablename()+"_"+source);
        map.put("loadtime",be.getLoadtime());
        map.put("befortime",be.getTime());
        return re.findTimeList(map);

    }
}
