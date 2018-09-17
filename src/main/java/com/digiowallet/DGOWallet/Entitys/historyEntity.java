package com.digiowallet.DGOWallet.Entitys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name="historypayment")
public class historyEntity {

    @Id
    @Column(name = "datetime")
    private LocalDateTime datetime;
    @Column(name = "action")
    private String action;
    @Column(name = "username")
    private String username;
    @Column(name = "receiver")
    private String receiver;
    @Column(name = "addmoney")
    private double addmoney;


    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String payer) {
        this.username = payer;
    }


    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }


    public double getMoney() {
        return addmoney;
    }

    public void setMoney(double addmoney) {
        this.addmoney = addmoney;
    }

}
