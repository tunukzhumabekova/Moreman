package com.ORT.model.response;

import java.time.LocalDateTime;

public class Payment {
    private Integer id;
    private Integer month;
    private Integer price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer userQuidsId;

    public Payment(Integer id, Integer month, Integer price, LocalDateTime startDate, LocalDateTime endDate, Integer userQuidsId) {
        this.id = id;
        this.month = month;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userQuidsId = userQuidsId;
    }

    public Payment(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public Integer getUserQuidsId() { return userQuidsId; }
    public void setUserQuidsId(Integer userQuidsId) { this.userQuidsId = userQuidsId; }
}
