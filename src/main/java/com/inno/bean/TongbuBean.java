package com.inno.bean;

public class TongbuBean {

    public static class Read{
        private String dimname;
        private String tablename;
        private String time;
        private String loadtime;
        private String sleep;
        private int a;
        private String tt;

        public String getDimname() {
            return dimname;
        }

        public void setDimname(String dimname) {
            this.dimname = dimname;
        }

        public String getTablename() {
            return tablename;
        }

        public void setTablename(String tablename) {
            this.tablename = tablename;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getLoadtime() {
            return loadtime;
        }

        public void setLoadtime(String loadtime) {
            this.loadtime = loadtime;
        }

        public String getSleep() {
            return sleep;
        }

        public void setSleep(String sleep) {
            this.sleep = sleep;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public String getTt() {
            return tt;
        }

        public void setTt(String tt) {
            this.tt = tt;
        }
    }

    public static class Write{
        private String dimname;
        private String tablename;
        private String onid;

        public String getDimname() {
            return dimname;
        }

        public void setDimname(String dimname) {
            this.dimname = dimname;
        }

        public String getTablename() {
            return tablename;
        }

        public void setTablename(String tablename) {
            this.tablename = tablename;
        }

        public String getOnid() {
            return onid;
        }

        public void setOnid(String onid) {
            this.onid = onid;
        }
    }

}
