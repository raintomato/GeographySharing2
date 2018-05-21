package com.example.lenovo.geographysharing.Element.BaseElement;

import com.example.lenovo.geographysharing.Element.Equipment;
import com.example.lenovo.geographysharing.Element.EquipmentCollection;
import com.example.lenovo.geographysharing.Element.User;

import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public abstract class Collection {
    private int id;
    private Equipment product;
    private User collector;
    private String add_time;



    public Collection(int id, Equipment product, User collector, String add_time) {
        this.id = id;
        this.product = product;
        this.collector = collector;
        this.add_time = add_time;
    }

    public int getId() {
        return id;
    }

    public User getCollector() {
        return collector;
    }

    public void setCollector(User collector) {
        this.collector = collector;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Equipment getProduct() {
        return product;
    }

    public void setProduct(Equipment product) {
        this.product = product;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public static List<Collection> findAllCollection(String phone) {
        List<Collection> list = null;
        list.addAll(EquipmentCollection.findEquipmentCollections(phone));
        return list;
    }
}
