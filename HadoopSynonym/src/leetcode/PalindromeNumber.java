package leetcode;

/**
 * Created by suyedong on 16/2/15.
 */
public class PalindromeNumber {
    public boolean isPalindrome(int x) {
        if (x == 0) return true;
        if (x < 0) return false;
//        if (x == -2147483648)  return false;
        int c = x;
        int count = 1;
        int n10 = 1;
        while(c >= 10) {
            c = c / 10;
            count ++;
            n10 *= 10;
        }

        c = x;
        int idx = 1;
        while (idx < count) {
            int r = c % 10;
            int l = c / n10;
            if (r != l) return false;

            c = c % n10;
            c = c / 10;
            n10 = n10 / 100;
            idx ++ ;
            count --;
        }


//        int halfc = count / 2;
//        if (halfc == 0) return true;
//        int half = x;
//        int r10 = 1;
//        for (int i = 0; i <= halfc; i ++) {
//            half = half / 10;
//            r10 *= 10;
//        }
//        int right = x % r10;
//        int allsame = half + right ;
//
//
//        int sum = half % 10 + x % 10;
//        while(allsame > 0) {
//            if (allsame % 10 == sum) {
//                allsame = allsame / 10;
//                continue;
//            } else {
//                return false;
//            }
//        }



            return true;
    }
}
