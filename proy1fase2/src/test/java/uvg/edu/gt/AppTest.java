package uvg.edu.gt;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testArithmeticEquation() {
        App interpreter = new App();
        List<Object> expression = new ArrayList<>();
        expression.add("+");
        expression.add(5);
        expression.add(3);
        Object result = interpreter.eval(expression);
        assertEquals(8, result); // Expected result is 8
    }
}
