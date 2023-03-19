package RecordPatterns;

import java.awt.*;

public class VehicleFactory {
    record VehicleGroup(char group, double groupVersion) {}
    record VehicleType(String producer, String car, VehicleGroup group) {}
    record Point(int x, int y) {}
    record Plate(String data, String country) {}
    record Parameters(int width, int height, int length) {}
    record Car(VehicleType type, Plate plate, Color color, Point gps, Parameters parameters, String customName) {}
    record Motorcycle(VehicleType type, Plate plate, Color color, Point gps, Parameters parameters) {}
    record Plane(VehicleType type, int seats, Parameters parameters, int maxSpeed) {}
}
