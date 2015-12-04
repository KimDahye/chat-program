package sophie.test;

/**
 * Created by sophie on 2015. 12. 4..
 */
public class StringSplit {
    public static void main(String[] args) {
        String str = "hi i'm   dahye";
        String[] splited = str.split("\\s+");
        for(String s : splited) {
            System.out.println(s);
        }
    }

}
