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
    Map<String, DeliveryPartner> deliveryPartnerMap = new TreeMap<>();
    Map<String,List<Order>> deliveryPartnerOrderPairMap = new TreeMap<>();
    Map<String,DeliveryPartner> OrderDeliveryPartnerPairMap = new TreeMap<>();


    public void addOrder(Order order){
        orderMap.put(order.getId(),order);
    }

    public void addPartner( String partnerId){
        deliveryPartnerMap.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair( String orderId,  String partnerId){
        List<Order> order= new ArrayList<>();
        if(deliveryPartnerOrderPairMap.containsKey(partnerId)){
            order= deliveryPartnerOrderPairMap.get(partnerId);
        }
        order.add(getOrderById(orderId));
        deliveryPartnerOrderPairMap.put(partnerId,order);

        OrderDeliveryPartnerPairMap.put(orderId,getPartnerById(partnerId));

        // set numbers of order for delivery partner
        int orderCount = getPartnerById(partnerId).getNumberOfOrders()+1;
        getPartnerById(partnerId).setNumberOfOrders(orderCount);

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
        List<String> orders = new ArrayList<>();
        if(deliveryPartnerOrderPairMap.containsKey(partnerId)) {
            for (Order order : deliveryPartnerOrderPairMap.get(partnerId)) {
                orders.add(order.getId());
            }
        }
        return  orders;
    }

    public List<String> getAllOrders(){
        return new ArrayList<>(orderMap.keySet());
    }

    public int getCountOfUnassignedOrders(){
        int totalOrder = orderMap.size();
        int assignedOrder = 0;
        for(String partners : deliveryPartnerOrderPairMap.keySet()){
            assignedOrder += getPartnerById(partners).getNumberOfOrders();
        }
        return totalOrder - assignedOrder;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId( String time, String partnerId){
        List <Integer> orderTimeList = new ArrayList<>();
        for (Order order: deliveryPartnerOrderPairMap.get(partnerId)){
            orderTimeList.add(order.getDeliveryTime());
        }

//        Collections.sort(orderTimeList); orders will be already sorted
        int actualTime = Integer.parseInt(time.substring(0,2)) * 60 + Integer.parseInt(time.substring(3));

        // find next index of given time in list
        int timeIdx = -1;
        int s = 0;
        int e = orderTimeList.size()-1;
        while(s <= e){
            int mid =  (s+e)/2;
             if(orderTimeList.get(mid) <= actualTime ){
                s = mid+1;
            } else if (orderTimeList.get(mid) > actualTime) {
                e = mid-1;
            }
        }

        timeIdx = s;
        return orderTimeList.size() - timeIdx;
    }

    public String getLastDeliveryTimeByPartnerId( String partnerId){
        int lastDelivery = deliveryPartnerOrderPairMap.get(partnerId).size()-1;
        int time = deliveryPartnerOrderPairMap.get(partnerId).get(lastDelivery).getDeliveryTime(); //in minutes

        String actualTime = new String();
        actualTime += time/60 + ':' + time % 60;

        return actualTime;
    }

    public void deletePartnerById( String partnerId){
        if(deliveryPartnerOrderPairMap.containsKey(partnerId)){
            List<Order> orders = deliveryPartnerOrderPairMap.get(partnerId);

            for(Order order: orders){
                if(OrderDeliveryPartnerPairMap.containsKey(order.getId())){
                    OrderDeliveryPartnerPairMap.remove(order.getId());
                }
            }
            deliveryPartnerOrderPairMap.remove(partnerId);
        }
        if(deliveryPartnerMap.containsKey(partnerId))deliveryPartnerMap.remove(partnerId);

    }

    public void deleteOrderById( String orderId){

        if(OrderDeliveryPartnerPairMap.containsKey(orderId)) {
            String partnersId = OrderDeliveryPartnerPairMap.get(orderId).getId();
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
