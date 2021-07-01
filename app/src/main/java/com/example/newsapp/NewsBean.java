package com.example.newsapp;

import java.util.List;

public class NewsBean {
    private int code;
    private String message;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private String path;
        private String image;
        private String title;
        private String passtime;

        public ResultBean(String path,String image,String title,String passtime){
            this.path = path;
            this.image = image;
            this.title = title;
            this.passtime = passtime;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPasstime() {
            return passtime;
        }

        public void setPasstime(String passtime) {
            this.passtime = passtime;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "path:'" + path + '\'' +
                    ", image:'" + image + '\'' +
                    ", title:'" + title + '\'' +
                    ", passtime:'" + passtime + '\'' +
                    '}';
        }
    }
}


