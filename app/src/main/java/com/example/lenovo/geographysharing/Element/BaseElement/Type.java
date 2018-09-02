package com.example.lenovo.geographysharing.Element.BaseElement;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class Type implements Serializable {
    private int id;
    private String type_name;
    private Integer type_grade;
    private boolean is_tab;
    private String add_time;
    private Integer parent_grade;
    public List<String> getSub_type() {
        return sub_type;
    }

    public void setSub_type(List<String> sub_type) {
        this.sub_type = sub_type;
    }

    private List<String> sub_type;

    protected Type(int id, String type_name, Integer type_grade, boolean is_tab, String add_time, Integer parent_grade,List<String> sub_type) {
        this.id = id;
        this.type_name = type_name;
        this.type_grade = type_grade;
        this.is_tab = is_tab;
        this.add_time = add_time;
        this.parent_grade = parent_grade;
        this.sub_type=sub_type;
    }

    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }

    public final String getType_name() {
        return type_name;
    }

    public final void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public final Integer getType_grade() {
        return type_grade;
    }

    public final void setType_grade(Integer type_grade) {
        this.type_grade = type_grade;
    }

    public final boolean is_tab() {
        return is_tab;
    }

    public final void setIs_tab(boolean is_tab) {
        this.is_tab = is_tab;
    }

    public final String getAdd_time() {
        return add_time;
    }

    public final void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public final Integer getParent_grade() {
        return parent_grade;
    }

    public final void setParent_grade(Integer parent_grade) {
        this.parent_grade = parent_grade;
    }
}
