package com.example.lenovo.geographysharing.Element;

import com.example.lenovo.geographysharing.Element.BaseElement.Order;
import com.example.lenovo.geographysharing.Element.BaseElement.Thing;
import com.example.lenovo.geographysharing.Utils.JsonDataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/21.
 */
@Deprecated
public class EquipmentOrder extends Order {
    private EquipmentOrder(String order_id, User payer, User payee, Thing product, String pay_time, String payment_method,
                           String payer_acount, String payee_acount, Double amount, Double damages, Double poundage,
                           Double receipt, String note, String comment) {
        super(order_id, payer, payee, product, pay_time, payment_method,
                payer_acount, payee_acount, amount, damages, poundage,
                receipt, note, comment);
    }
    private static List<EquipmentOrder> getEquipmentOrderJSONArray(JSONArray array){
        List<EquipmentOrder> list=new ArrayList<EquipmentOrder>();
        if(array.length()<=0) {
            return null;
        }else {
            for (int i=0;i<array.length();i++) {
                try {
                    JSONObject json =array.getJSONObject(i);
                    list.add(new EquipmentOrder(
                            json.getString("order_id"),
                            User.findUser(json.getJSONObject("payer")),
                            User.findUser(json.getJSONObject("payee")),
                            Equipment.findEquipment(json.getJSONObject("product")),
                            json.getString("pay_time"),
                            json.getString("payment_method"),
                            json.getString("pay_account"),
                            json.getString("payee_account"),
                            json.getDouble("amount"),
                            json.getDouble("damages"),
                            json.getDouble("poundage"),
                            json.getDouble("receipt"),
                            json.getString("note"),
                            json.getString("comment")
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
    }

    /**
     *
     * @param phoneOfPayer
     * @return
     */
    public static List<EquipmentOrder> findEquipmentOrdersForPayer(String phoneOfPayer){
        JSONArray array= JsonDataUtil.getJSONArray(JsonDataUtil.RESOURCE_URL+"equipment_orders/?payer="+User.findUser(phoneOfPayer).getId());
        return getEquipmentOrderJSONArray(array);
    }
    public static List<EquipmentOrder> findEquipmentOrdersForPayee(String phoneOfPayee){
        JSONArray array= JsonDataUtil.getJSONArray(JsonDataUtil.RESOURCE_URL+"equipment_orders/?payee="+User.findUser(phoneOfPayee).getId());
        return getEquipmentOrderJSONArray(array);
    }
}
