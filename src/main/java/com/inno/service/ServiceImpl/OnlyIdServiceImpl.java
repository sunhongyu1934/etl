package com.inno.service.ServiceImpl;

import com.inno.bean.OnlyidBean;
import com.inno.dao.Impl.OnlyIdDaoImpl;
import com.inno.dao.OnlyIdDao;
import com.inno.service.OnlyIdService;

import java.util.Map;

public class OnlyIdServiceImpl implements OnlyIdService {
    OnlyIdDao on=new OnlyIdDaoImpl();
    @Override
    public OnlyidBean select(String cname) {
        return on.select(cname);
    }

    @Override
    public void insert(Map<String, String> map) {
        on.insert(map);
    }
}
