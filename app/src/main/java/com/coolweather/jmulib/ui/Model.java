package com.coolweather.jmulib.ui;

/**
 * Created by ZongJie on 2016/8/30.
 */
public class Model {
    //借阅情况的Model
    private String book_name;
    private int imageid;
    private String rawValue;
    private boolean renewable=false;
    private String outTime;

    //搜索页
    private String author;
  //  private String location;
    private String bookNumber;//索引号
    private String publisher;
    private String total;
    private String nowbooks;

    //馆藏情况的Model
    private String book_No;
    private String status_book;

    //馆藏情况
    public Model(String book_No, String status_book) {
        this.book_No = book_No;
        this.status_book = status_book;
    }
    //借阅
    public Model(String book_name, int imageid, String rawValue, boolean renewable, String outTime) {
        this.book_name = book_name;
        this.imageid = imageid;
        this.rawValue = rawValue;
        this.renewable = renewable;
        this.outTime = outTime;
    }
    //催还
    public Model(String book_name, int imageid, String rawValue, String outTime) {
        this.book_name = book_name;
        this.imageid = imageid;
        this.rawValue = rawValue;
        this.outTime = outTime;
    }
    //搜索页
    public Model(String book_name, int imageid, String bookNumber, String publisher, String total, String nowbooks, String author) {
        this.book_name = book_name;
        this.imageid = imageid;
   //     this.location = location;
        this.bookNumber = bookNumber;
        this.publisher = publisher;
        this.total = total;
        this.nowbooks = nowbooks;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

//    public String getLocation() {
//        return location;
//    }

    public String getBookNumber() {
        return bookNumber;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getTotal() {
        return total;
    }

    public String getNowbooks() {
        return nowbooks;
    }

    public String getBook_name() {
        return book_name;
    }

    public int getImageid() {
        return imageid;
    }

    public String getRawValue() {
        return rawValue;
    }

    public boolean getRenewable() {
        return renewable;
    }

    public String getOutTime() {
        return outTime;
    }

    public String getBook_No() {
        return book_No;
    }

    public String getStatus_book() {
        return status_book;
    }
}
