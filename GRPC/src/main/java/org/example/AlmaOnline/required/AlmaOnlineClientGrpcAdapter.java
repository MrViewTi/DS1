package org.example.AlmaOnline.required;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.Timestamp;
import org.example.AlmaOnline.provided.client.*;
import org.example.AlmaOnline.provided.service.Menu;
import org.example.AlmaOnline.server.*;

import java.time.Instant;
import java.util.*;

// AlmaOnlineClientGrpcAdapter provides your own implementation of the AlmaOnlineClientAdapter
public class AlmaOnlineClientGrpcAdapter implements AlmaOnlineClientAdapter {
    // getRestaurants should retrieve the information on all the available restaurants.
    @Override
    public List<RestaurantInfo> getRestaurants(AlmaOnlineGrpc.AlmaOnlineBlockingStub stub) {
        GetRestaurantsResponse list = stub.getRestaurants(GetRestaurantsRequest.newBuilder().build());
        List<RestaurantInfo> infos = new ArrayList<RestaurantInfo>();
        for(GetRestaurantResponse response : list.getRestaurantInfoListList()){
            infos.add(new RestaurantInfo(response.getId(), response.getName()));
        }
        return infos;
    }

    // getMenu should return the menu of a given restaurant
    @Override
    public MenuInfo getMenu(AlmaOnlineGrpc.AlmaOnlineBlockingStub stub, String restaurantId) {
        GetMenuResponse list = stub.getMenu(GetMenuRequest.newBuilder().setRestaurantId(restaurantId).build());
        Map<String, Double> items = new HashMap<>();
        for(GetMenuItem response: list.getRestaurantItemsList()){
            items.put(response.getItemname(), response.getItemprice());
        }
        return new MenuInfo(items);
    }

    // createDineInOrder should create the given dine-in order at the AlmaOnline server
    @Override
    public ListenableFuture<?> createDineInOrder(AlmaOnlineGrpc.AlmaOnlineFutureStub stub, DineInOrderQuote order) {
        //assuming reservation date and creation are the same for simplicity
        ListenableFuture<CreateDineInOrderResponse> list = stub.createDineInOrder(CreateDineInOrderRequest.newBuilder()
                .setRestaurantId(order.getRestaurantId())
                .setCustomer(order.getCustomer()).setOrderId(order.getOrderId()).addAllItems(order.getItems()).
                setReservationDate(order.getReservationDate().toString()).setCreationDate(order.getReservationDate().toString()).build());
        return list;
    }

    // createDeliveryOrder should create the given delivery order at the AlmaOnline server
    @Override
    public ListenableFuture<?> createDeliveryOrder(AlmaOnlineGrpc.AlmaOnlineFutureStub stub, DeliveryOrder order) {
        return null;
    }

    // getOrder should retrieve the order information at the AlmaOnline server given the restaurant the order is
    // placed at and the id of the order.
    @Override
    public BaseOrderInfo getOrder(AlmaOnlineGrpc.AlmaOnlineBlockingStub stub, String restaurantId, String orderId) {
        return null;
    }

    // getScript returns the script the application will run during testing.
    // You can leave the default implementation, as it will test most of the functionality.
    // Alternatively, you can provide your own implementation to test your own edge-cases.
    @Override
    public AppScript getScript() {
        return AlmaOnlineClientAdapter.super.getScript();
    }
}
