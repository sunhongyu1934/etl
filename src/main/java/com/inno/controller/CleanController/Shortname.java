package com.inno.controller.CleanController;

import com.inno.service.ServiceImpl.ShortnameServiceImpl;
import com.inno.service.ShortnameService;

import java.text.ParseException;

public class Shortname {
    private static ShortnameService s=new ShortnameServiceImpl();
    public static void main(String args[]) throws ParseException, InterruptedException {
        String ta2 = args[0];
        s.upshort(ta2);
    }
}
