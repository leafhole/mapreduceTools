package test.leetcode;

import leetcode.PalindromeNumber;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * PalindromeNumber Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>二月 15, 2016</pre>
 */
public class PalindromeNumberTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: isPalindrome(int x)
     */
    @Test
    public void testIsPalindrome() throws Exception {
        PalindromeNumber pn = new PalindromeNumber();
        Assert.assertEquals(pn.isPalindrome(0) ,true);
        Assert.assertEquals(pn.isPalindrome(-1) ,false);
        Assert.assertEquals(pn.isPalindrome(121) ,true);
        Assert.assertEquals(pn.isPalindrome(123) ,false);
        Assert.assertEquals(pn.isPalindrome(1234) ,false);
        Assert.assertTrue(pn.isPalindrome(12321));
        Assert.assertFalse(pn.isPalindrome(-2147483648));
        Assert.assertFalse(pn.isPalindrome(100));

    }


} 
