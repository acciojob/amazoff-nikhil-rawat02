package com.driver;

import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    /*
     orderMap stores ==> key : orderId; value : Order
     deliveryPartnerMap stores ==> key : partnerId, value: DeliveryPartner
     deliveryPartnerOrderPairMap stores ==> key : partnerId, value: order
     OrderDeliveryPartnerPairMap stores ==> key : orderId, value : deliveryPartner
     */
    Map<String,Order> orderMap;
    Map<String, DeliveryPartner> deliveryPartnerMap;
    Map<String,List<String>> deliveryPartnerOrderPairMap;
    Map<String,String> OrderDeliveryPartnerPairMap;

    OrderRepository(){
        orderMap = new HashMap<>();
        deliveryPartnerMap = new HashMap<>();
        deliveryPartnerOrderPairMap = new HashMap<>();
        OrderDeliveryPartnerPairMap = new HashMap<>();
    }
    public void addOrder(Order order){
        orderMap.put(order.getId(),order);
    }

    public void addPartner( String partnerId){
        deliveryPartnerMap.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair( String orderId,  String partnerId){
        if(deliveryPartnerMap.containsKey(partnerId) && orderMap.containsKey(orderId)) {
            List<String> order = deliveryPartnerOrderPairMap.getOrDefault(partnerId, new ArrayList<>());
            order.add(orderId);
            deliveryPartnerOrderPairMap.put(partnerId, order);

            OrderDeliveryPartnerPairMap.put(orderId, partnerId);

            // set numbers of order for delivery partner
            int orderCount = getPartnerById(partnerId).getNumberOfOrders();
            getPartnerById(partnerId).setNumberOfOrders(order.size());
        }
    }

    public Order getOrderById( String orderId){
        return orderMap.get(orderId);

    }

    public DeliveryPartner getPartnerById( String partnerId){
        return deliveryPartnerMap.get(partnerId);
    }

    public int getOrderCountByPartnerId( String partnerId){
        return getPartnerById(partnerId).getNumberOfOrders();

    }

    public List<String> getOrdersByPartnerId(String partnerId){
        return  deliveryPartnerOrderPairMap.get(partnerId);
    }

    public List<String> getAllOrders(){
        return new ArrayList<>(orderMap.keySet());
    }

    public int getCountOfUnassignedOrders(){
        return orderMap.size() - OrderDeliveryPartnerPairMap.size();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId( String time, String partnerId){
        int actualTime = Integer.parseInt(time.substring(0,2)) * 60 + Integer.parseInt(time.substring(3));
        int countOrder = 0;
        for (String order: deliveryPartnerOrderPairMap.get(partnerId)){
           if(getOrderById(order).getDeliveryTime() > actualTime){
               countOrder++;
           }
        }
        return countOrder;
    }

    public String getLastDeliveryTimeByPartnerId( String partnerId){
        int lastDelivery = Integer.MIN_VALUE;
        for (String order: deliveryPartnerOrderPairMap.get(partnerId)){
            lastDelivery = Math.max(getOrderById(order).getDeliveryTime(),lastDelivery);
        }
        int hrs = lastDelivery /60;
        int min = lastDelivery %60;
        String actualTimeHrs = "";
        String actualTimeMin = "";
        if(hrs < 10) actualTimeHrs += "0" + hrs;
        else{
            actualTimeHrs += hrs;
        }
        if (min < 10)actualTimeMin += "0" + min;
        else actualTimeMin += min;

        return  actualTimeHrs + ':' + actualTimeMin;
    }

    public void deletePartnerById( String partnerId){
        if(deliveryPartnerOrderPairMap.containsKey(partnerId)){
            List<String> orders = deliveryPartnerOrderPairMap.get(partnerId);
            for(String orderId: orders){
                OrderDeliveryPartnerPairMap.remove(orderId);
            }
            deliveryPartnerOrderPairMap.remove(partnerId);
        }

        deliveryPartnerMap.remove(partnerId);
    }

    public void deleteOrderById( String orderId){

        if(OrderDeliveryPartnerPairMap.containsKey(orderId)) {
            String partnersId = OrderDeliveryPartnerPairMap.get(orderId);

            OrderDeliveryPartnerPairMap.remove(orderId);
            deliveryPartnerOrderPairMap.get(partnersId).remove(orderId);

            getPartnerById(partnersId).setNumberOfOrders(deliveryPartnerOrderPairMap.get(partnersId).size());
        }

        orderMap.remove(orderId);
    }
}
