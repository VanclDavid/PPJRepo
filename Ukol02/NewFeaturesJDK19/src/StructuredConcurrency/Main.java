package StructuredConcurrency;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class Main {
    public static ArrayList<String> users = new ArrayList<String>();
    public static Map<Integer, String> orders = new HashMap<Integer, String>();
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Uživatele
        users.add("Pepa");
        users.add("Tomas");
        users.add("Jarda");

        //Objednavky
        orders.put(1, "Pepa");
        orders.put(2, "Jarda");
        orders.put(3, "Jarda");
        orders.put(4, "Tomas");

        Response response = Main.handle("Jarda");
        System.out.format("User: %s Orser: %s", response.getUser(), response.getOrders().toString());
    }

    public static String findUser(String value) {
        for (String user : Main.users) {
            if (user.equals(value)) {
                return user;
            }
        }
        return null;
    }

    public static ArrayList<Integer> fetchOrder(String value) {
        ArrayList<Integer> orders = new ArrayList<Integer>();
        for (Map.Entry<Integer, String> order : Main.orders.entrySet()) {
            if (order.getValue().equals(value)) {
                orders.add(order.getKey());
            }
        }
        return orders;
    }

    public static Response handle(String value) throws ExecutionException, InterruptedException {
        try (var esvc = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<String> user  = esvc.submit(() -> findUser(value));
            Future<ArrayList<Integer>> order = esvc.submit(() -> fetchOrder(value));
            String theUser = user.get();   // Join findUser
            ArrayList<Integer> theOrder = order.get();  // Join fetchOrder
            return new Response(theUser, theOrder);
        }
    }
}