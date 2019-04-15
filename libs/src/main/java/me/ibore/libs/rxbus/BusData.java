package me.ibore.libs.rxbus;

/**
 * description:
 * author: Ibore Xie
 * date: 2018-01-18 23:55
 * website: ibore.me
 */

public class BusData {

    private String id = "1";
    private String status;

    public BusData() {}

    public BusData(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
