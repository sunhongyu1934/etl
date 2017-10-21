package com.inno.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface GudongService {
    public List<Map<String,Object>> find(String lim);
    public List<Map<String,Object>> findshang(List<String> list,String a);
    public void clean(List<Map<String,Object>> list) throws IOException, InterruptedException, ParseException;
}
