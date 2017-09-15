package com.inno.dao.Impl;

import com.inno.bean.OnlyidBean;
import com.inno.dao.OnlyIdDao;
import com.inno.utils.mybatis_factory.MybatisUtils;

import java.util.Map;

public class OnlyIdDaoImpl implements OnlyIdDao {
    @Override
    public OnlyidBean select(String cname) {
        return MybatisUtils.getInstance().selectOne("mapping.OnlyidMapper.selectid",cname);
    }

    @Override
    public void insert(Map<String, String> map) {
        MybatisUtils.getInstance().insert("mapping.OnlyidMapper.insertid",map);
    }
}
