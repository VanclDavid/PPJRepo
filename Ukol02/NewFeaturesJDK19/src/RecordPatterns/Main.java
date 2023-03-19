package RecordPatterns;

import java.awt.*;
import java.util.ArrayList;

public class Main {
    // Vytvoření jednoduché struktury pro vozidla a výpis pro auta. Struktura je v VehicleFactory
    public static void main(String[] args) {
        ArrayList<VehicleFactory.Car> carMeeting = new ArrayList<VehicleFactory.Car>();

        carMeeting.add(new VehicleFactory.Car(
                new VehicleFactory.VehicleType("Skoda", "Octavia", new VehicleFactory.VehicleGroup('C', 3.2)),
                new VehicleFactory.Plate("AABBCCDD", "CZ"),
                Color.BLACK,
                new VehicleFactory.Point(50, 300),
                new VehicleFactory.Parameters(2700, 2555,4600),
                "Moje auto"
        ));

        carMeeting.add(new VehicleFactory.Car(
                new VehicleFactory.VehicleType("Audi", "A2", new VehicleFactory.VehicleGroup('C', 3.1)),
                new VehicleFactory.Plate("ABCDEF", "PL"),
                Color.BLACK,
                new VehicleFactory.Point(51, 301),
                new VehicleFactory.Parameters(2700, 2555,4600),
                "Srot"
        ));

        for (VehicleFactory.Car car : carMeeting) {
            printPoints(car);
        }

    }

    public static void printPoints(Object obj) {
        if(obj instanceof VehicleFactory.Car(
                VehicleFactory.VehicleType(String producer, String car, VehicleFactory.VehicleGroup(char group, double groupVersion)),
                VehicleFactory.Plate(String data, String country),
                Color color,
                VehicleFactory.Point(int gpsX, int gpsY),
                VehicleFactory.Parameters(int width, int height, int length),
                String customName)
        ) {
            System.out.format(
                    "[Type] %s %s %s %s \n [Plate] %s (%s) \n [Color] %s \n [GPS] x: %s y: %s \n [Param] w: %s l: %s h: %s \n [Desc] %s",
                    producer, car, group, groupVersion, data, country, color.toString(), gpsX, gpsY, width, height, length, customName
            );
        }

//        if(obj instanceof VehicleFactory.Plane() {
//           ALTERNATIVA PRO LETADLO, ....
//        }
    }
}