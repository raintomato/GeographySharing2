package com.example.lenovo.geographysharing.Element.BaseElement;

import com.example.lenovo.geographysharing.Element.EquipmentIssue;
import com.example.lenovo.geographysharing.Element.SoftwareIssue;
import com.example.lenovo.geographysharing.Element.TechnologyIssue;
import com.example.lenovo.geographysharing.Element.User;

import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public abstract class Issue {
    private int id;
    private User issuer;
    private Thing product;
    private String add_time;

    protected Issue(int id, User issuer, Thing product, String add_time) {
        this.id = id;
        this.issuer = issuer;
        this.product = product;
        this.add_time = add_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getIssuer() {
        return issuer;
    }

    public void setIssuer(User issuer) {
        this.issuer = issuer;
    }

    public Thing getProduct() {
        return product;
    }

    public void setProduct(Thing product) {
        this.product = product;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
    public static List<Issue> findAllIssue(String phone){
        List <Issue> list=null;
        list.addAll(EquipmentIssue.findEquipmentIssues(phone));
        list.addAll(SoftwareIssue.findSoftwareIssues(phone));
        list.addAll(TechnologyIssue.findTechnologyIssues(phone));
        return list;
    }
}
