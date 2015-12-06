package sophie.test;

import java.io.Serializable;

/**
 * Created by sophie on 2015. 12. 3..
 */
public class Child extends Parent implements Serializable{
    private String b = "Child";
    public Child(){
        super();
    }

    @Override
    public void print() {
        System.out.println(b);
    }

}
