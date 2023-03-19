package StructuredConcurrency;

import java.util.ArrayList;

public class Response {
    private String user;
    private ArrayList<Integer> orders;

    public Response(String user, ArrayList<Integer> orders){
        this.orders = orders;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public ArrayList<Integer> getOrders() {
        return orders;
    }
}
