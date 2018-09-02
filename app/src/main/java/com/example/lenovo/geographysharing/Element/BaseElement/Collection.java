package com.example.lenovo.geographysharing.Element.BaseElement;

import com.example.lenovo.geographysharing.Element.*;

import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public abstract class Collection {
    private int id;
    private Thing product;
    private User collector;
    private String add_time;



    public Collection(int id, Thing product, User collector, String add_time) {
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

    public static List<Collection> findAllCollection(String phone) {
        List<Collection> list = null;
        list.addAll(EquipmentCollection.findEquipmentCollections(phone));
        list.addAll(SoftwareCollection.findSoftwareCollections(phone));
        list.addAll(TechnologyCollection.findTechnologyCollections(phone));
        return list;
    }
}
