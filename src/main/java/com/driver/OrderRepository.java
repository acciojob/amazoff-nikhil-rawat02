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
    Map<String,List<Order>> deliveryPartnerOrderPairMap;
    Map<String,String> OrderDeliveryPartnerPairMap;

    OrderRepository(){
        orderMap = new HashMap<>();
        deliveryPartnerMap = new HashMap<>();
        deliveryPartnerOrderPairMap = new HashMap<>();
        OrderDeliveryPartnerPairMap = new HashMap<>();
    }
    public void addOrder(Order order)throws NullPointerException{
        orderMap.put(order.getId(),order);
    }

    public void addPartner( String partnerId)throws NullPointerException{
        deliveryPartnerMap.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair( String orderId,  String partnerId)throws NullPointerException{
        List<Order> order= deliveryPartnerOrderPairMap.getOrDefault(partnerId, new ArrayList<>());
        order.add(getOrderById(orderId));
        deliveryPartnerOrderPairMap.put(partnerId,order);

        OrderDeliveryPartnerPairMap.put(orderId,partnerId);

        // set numbers of order for delivery partner
        int orderCount = getPartnerById(partnerId).getNumberOfOrders()+1;
        getPartnerById(partnerId).setNumberOfOrders(orderCount);

    }

    public Order getOrderById( String orderId)throws NullPointerException{
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById( String partnerId)throws NullPointerException{
        return deliveryPartnerMap.get(partnerId);
    }

    public int getOrderCountByPartnerId( String partnerId)throws NullPointerException{
        return getPartnerById(partnerId).getNumberOfOrders();

    }

    public List<String> getOrdersByPartnerId(String partnerId)throws NullPointerException{
        List<String> orders = new ArrayList<>();
        if(deliveryPartnerOrderPairMap.containsKey(partnerId)) {
            for (Order order : deliveryPartnerOrderPairMap.get(partnerId)) {
                orders.add(order.getId());
            }
        }
        return  orders;
    }

    public List<String> getAllOrders()throws NullPointerException{
        return new ArrayList<>(orderMap.keySet());
    }

    public int getCountOfUnassignedOrders()throws NullPointerException{
        int totalOrder = orderMap.size();
        int assignedOrder = 0;
        for(String partners : deliveryPartnerOrderPairMap.keySet()){
            assignedOrder += getPartnerById(partners).getNumberOfOrders();
        }
        return totalOrder - assignedOrder;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId( String time, String partnerId)throws NullPointerException{
        int actualTime = Integer.parseInt(time.substring(0,2)) * 60 + Integer.parseInt(time.substring(3));
        int countOrder = 0;
        for (Order order: deliveryPartnerOrderPairMap.get(partnerId)){
           if(order.getDeliveryTime() > actualTime){
               countOrder++;
           }
        }
        return countOrder;
    }

    public String getLastDeliveryTimeByPartnerId( String partnerId)throws NullPointerException{
        int lastDelivery = Integer.MAX_VALUE;
        for (Order order: deliveryPartnerOrderPairMap.get(partnerId)){
            lastDelivery = Math.min(order.getDeliveryTime(),lastDelivery);
        }

        String actualTime = new String();
        actualTime += lastDelivery/60 + ':' + lastDelivery % 60;

        return actualTime;
    }

    public void deletePartnerById( String partnerId)throws NullPointerException{
        if(deliveryPartnerOrderPairMap.containsKey(partnerId)){
            List<Order> orders = deliveryPartnerOrderPairMap.get(partnerId);
            for(Order order: orders){
                OrderDeliveryPartnerPairMap.remove(order.getId());
            }

            deliveryPartnerOrderPairMap.remove(partnerId);
        }

        deliveryPartnerMap.remove(partnerId);
    }

    public void deleteOrderById( String orderId)throws NullPointerException{

        if(OrderDeliveryPartnerPairMap.containsKey(orderId)) {
            String partnersId = OrderDeliveryPartnerPairMap.get(orderId);
            List<Order> orderList = deliveryPartnerOrderPairMap.get(partnersId);
            // index of orderId in orderList
            int orderIdx = -1;
            for(int i =0; i < orderList.size(); i++){
                if(orderList.get(i).getId().equals(orderId)){
                    orderIdx = i;
                }
            }
            orderList.remove(orderIdx);
            deliveryPartnerOrderPairMap.put(partnersId,orderList);

            int getOrder = getPartnerById(partnersId).getNumberOfOrders();
            getPartnerById(partnersId).setNumberOfOrders(getOrder-1);
        }

        orderMap.remove(orderId);
    }
}
