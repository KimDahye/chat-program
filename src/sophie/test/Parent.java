package sophie.test;

import java.util.Set;

/**
 * Created by sophie on 2015. 12. 3..
 */
public class Parent<E> {
    private String a = "parent";

    public Parent() {
        print();
    }

    public void test(Set<E> set) {
        if(set instanceof Set) {
            Set<?> m = (Set<?>) set;
        }
    }

    protected void print() {
        System.out.println(a);
    }
}
