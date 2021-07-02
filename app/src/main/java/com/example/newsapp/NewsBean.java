package com.example.newsapp;

import java.util.List;

public class NewsBean {
    private int code;
    private String message;
    private List<ResultBean> newslist;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultBean> getNewslist() {
        return newslist;
    }

    public void setNewslist(List<ResultBean> newslist) {
        this.newslist = newslist;
    }

    public static class ResultBean {
        private String url;
        private String picUrl;
        private String title;
        private String ctime;
        private String description;
        private String source;

        public ResultBean(String url, String picUrl, String title, String cime, String description, String source) {
            this.url = url;
            this.picUrl = picUrl;
            this.title = title;
            this.ctime = ctime;
            this.description = description;
            this.source = source;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    ",ctime:'" + ctime + '\'' +
                    ",title:'" + title + '\'' +
                    ",description:'" + description + '\'' +
                    ",picUrl:'" + picUrl + '\'' +
                    ",url:'" + url + '\'' +
                    ",source:'" + source + '\''+
                    '}';
        }
    }
}


