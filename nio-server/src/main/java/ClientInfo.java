/**
 * Created by sophie on 2015. 12. 15..
 */
class ClientInfo {
    private String name;
    private int roomNum;

    ClientInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }
}
