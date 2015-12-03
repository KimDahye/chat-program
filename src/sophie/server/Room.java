package sophie.server;

/**
 * Created by sophie on 2015. 12. 2..
 */
class Room {
    private final int number;
    private final String name;

    Room(int number, String name) {
        this.number = number;
        this.name = name;
    }

    int getNumber() {
        return number;
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Room{" +
                "number=" + number +
                ", name='" + name + '\'' +
                '}';
    }
}
