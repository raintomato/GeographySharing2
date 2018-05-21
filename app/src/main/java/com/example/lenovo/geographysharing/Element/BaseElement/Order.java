package com.example.lenovo.geographysharing.Element.BaseElement;

import com.example.lenovo.geographysharing.Element.EquipmentOrder;
import com.example.lenovo.geographysharing.Element.User;

import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class Order {
    private String order_id;
    private User payer;
    private User payee;
    private Thing product;
    private String pay_time;
    private String payment_method;
    private String payer_acount;
    private String payee_acount;
    private Double amount;
    private Double damages;
    private Double poundage;
    private Double receipt;
    private String note;
    private String comment;

    protected Order(String order_id, User payer, User payee, Thing product, String pay_time,
                    String payment_method, String payer_acount, String payee_acount, Double amount,
                    Double damages, Double poundage, Double receipt, String note, String comment) {
        this.order_id = order_id;
        this.payer = payer;
        this.payee = payee;
        this.product = product;
        this.pay_time = pay_time;
        this.payment_method = payment_method;
        this.payer_acount = payer_acount;
        this.payee_acount = payee_acount;
        this.amount = amount;
        this.damages = damages;
        this.poundage = poundage;
        this.receipt = receipt;
        this.note = note;
        this.comment = comment;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
        this.payer = payer;
    }

    public User getPayee() {
        return payee;
    }

    public void setPayee(User payee) {
        this.payee = payee;
    }

    public Thing getProduct() {
        return product;
    }

    public void setProduct(Thing product) {
        this.product = product;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPayer_acount() {
        return payer_acount;
    }

    public void setPayer_acount(String payer_acount) {
        this.payer_acount = payer_acount;
    }

    public String getPayee_acount() {
        return payee_acount;
    }

    public void setPayee_acount(String payee_acount) {
        this.payee_acount = payee_acount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getDamages() {
        return damages;
    }

    public void setDamages(Double damages) {
        this.damages = damages;
    }

    public Double getPoundage() {
        return poundage;
    }

    public void setPoundage(Double poundage) {
        this.poundage = poundage;
    }

    public Double getReceipt() {
        return receipt;
    }

    public void setReceipt(Double receipt) {
        this.receipt = receipt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public static List<Order> findAllOrder(String phone){
        List<Order> list=null;
        list.addAll(EquipmentOrder.findEquipmentOrdersForPayee(phone));
        return list;
    }
}
