package uvg.edu.gt;

import java.util.ArrayList;
import java.util.Arrays;

public class List extends ArrayList<Object> {
    public List(Object... elements) {
        super(Arrays.asList(elements));
    }
}