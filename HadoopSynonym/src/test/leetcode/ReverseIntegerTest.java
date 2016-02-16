package test.leetcode;

import com.sun.tools.javac.util.Assert;
import leetcode.ReverseInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import static org.junit.Assert.assertEquals;

/**
 * ReverseInteger Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>二月 15, 2016</pre>
 */
public class ReverseIntegerTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: reverse(int x)
     */
    @Test
    public void testReverse() throws Exception {
        ReverseInteger ri = new ReverseInteger();
        HashMap<Integer, Integer> testCases = new HashMap<Integer, Integer>();
        testCases.put(0, 0);

        testCases.put(100, 1);
        testCases.put(123, 321);
        testCases.put(-123, -321);
        testCases.put(1000000003, 0);
        testCases.put(1534236469, 0);

        for (Integer input : testCases.keySet()) {
            Integer vl = testCases.get(input);
            int v = ri.reverse(input);
            assertEquals(vl.intValue() , v);
        }
    }


}
