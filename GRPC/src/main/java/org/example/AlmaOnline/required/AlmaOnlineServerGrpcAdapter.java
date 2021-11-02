package org.example.AlmaOnline.required;


import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import org.example.AlmaOnline.defaults.Initializer;
import org.example.AlmaOnline.provided.client.AlmaOnlineClientAdapter;
import org.example.AlmaOnline.provided.client.RestaurantInfo;
import org.example.AlmaOnline.provided.server.AlmaOnlineServerAdapter;
import org.example.AlmaOnline.provided.service.*;
import org.example.AlmaOnline.provided.service.Menu;
import org.example.AlmaOnline.server.*;
import org.example.AlmaOnline.provided.service.exceptions.OrderException;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

// AlmaOnlineServerGrpcAdapter implements the grpc-server side of the application.
// The implementation should not contain any additional business logic, only implement
// the code here that is required to couple your IDL definitions to the provided business logic.
public class AlmaOnlineServerGrpcAdapter extends AlmaOnlineGrpc.AlmaOnlineImplBase implements AlmaOnlineServerAdapter {

    // the service field contains the AlmaOnline service that the server will
    // call during testing.
    private final AlmaOnlineService service;

    public AlmaOnlineServerGrpcAdapter() {
        this.service = this.getInitializer().initialize();
    }

    // -- Put the code for your implementation down below -- //
    @Override
    public void getRestaurants(GetRestaurantsRequest request, StreamObserver<GetRestaurantsResponse> responseObserver){

        GetRestaurantsResponse.Builder builder = GetRestaurantsResponse.newBuilder();
        int i = 0;
        for(Restaurant restaurant : service.getRestaurants()){
            RestaurantInfo info = new RestaurantInfo(restaurant.getId(), restaurant.getName());
            GetRestaurantResponse resp = GetRestaurantResponse.newBuilder().setId(info.getId()).setName(info.getName()).build();
            builder.addRestaurantInfoList(resp);
            i++;
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getMenu(GetMenuRequest request, StreamObserver<GetMenuResponse> responseObserver){
        GetMenuResponse.Builder builder = GetMenuResponse.newBuilder();
        Optional<Menu> menu = service.getRestaurantMenu(request.getRestaurantId());
        if(menu.isPresent()){
            for(Item item : menu.get().getItems()){
//                MenuItem info = new MenuItem(item.getName(), item.getPrice());
                GetMenuItem resp = GetMenuItem.newBuilder().setItemname(item.getName()).setItemprice(item.getPrice()).build();
                builder.addRestaurantItems(resp);
            }
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void createDineInOrder(CreateDineInOrderRequest request, StreamObserver<CreateDineInOrderResponse> responseObserver) {
        try{
            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(request.getCreationDate());
            Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(request.getReservationDate());
            service.createDineInOrder(request.getRestaurantId(), new DineInOrderQuote(request.getOrderId(),
                    date1, request.getCustomer(), request.getItemsList(),date2));
        }
        catch (OrderException | ParseException e){
            System.out.print("order exception generated");
        }
        CreateDineInOrderResponse resp = CreateDineInOrderResponse.newBuilder().build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }
}
