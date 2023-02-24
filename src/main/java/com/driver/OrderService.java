package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public void addOrder( Order order){
        try {
            orderRepository.addOrder(order);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addPartner( String partnerId){
        try {
            orderRepository.addPartner(partnerId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addOrderPartnerPair( String orderId,  String partnerId){
         try {
             orderRepository.addOrderPartnerPair(orderId,partnerId);
         }catch (Exception e){
             e.printStackTrace();
         }
    }

    public Order getOrderById( String orderId){
        try {
            return orderRepository.getOrderById(orderId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public DeliveryPartner getPartnerById( String partnerId){
        try {
            return orderRepository.getPartnerById(partnerId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public int getOrderCountByPartnerId( String partnerId){
        try {
            return orderRepository.getOrderCountByPartnerId(partnerId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public List<String> getOrdersByPartnerId( String partnerId){
        try {
            return orderRepository.getOrdersByPartnerId(partnerId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getAllOrders(){
        try {
            return  orderRepository.getAllOrders();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public int getCountOfUnassignedOrders(){
        try {
            return orderRepository.getCountOfUnassignedOrders();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId( String time, String partnerId){
        try {
            return orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(time,partnerId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public String getLastDeliveryTimeByPartnerId( String partnerId){
        try {
            return orderRepository.getLastDeliveryTimeByPartnerId(partnerId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void deletePartnerById( String partnerId){
        try {
            orderRepository.deletePartnerById(partnerId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteOrderById( String orderId){
        try {
            orderRepository.deleteOrderById(orderId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
