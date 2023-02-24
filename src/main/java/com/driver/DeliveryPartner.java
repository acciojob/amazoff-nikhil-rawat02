package com.driver;

import java.util.HashMap;
import java.util.Map;

public class DeliveryPartner {

    private String id;
    private int numberOfOrders;

    public DeliveryPartner(String id) {
        this.id = id;
        this.numberOfOrders = 0;
    }

    public String getId() {
        return id;
    }

    public Integer getNumberOfOrders(){
        return numberOfOrders;
    }

    public void setNumberOfOrders(Integer numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }
    Map<Integer,Integer> map = new HashMap<>();

    @Override
    public String toString() {
        return "DeliveryPartner{" +
                "id='" + id + '\'' +
                ", numberOfOrders=" + numberOfOrders +
                '}';

    }
}