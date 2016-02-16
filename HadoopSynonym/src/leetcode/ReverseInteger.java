package leetcode;

import java.util.HashMap;

/**
 * Created by leaf on 16/2/15.
 */
public class ReverseInteger {
    public int reverse(int x) {
        int flag = 1;
        if (x < 0) {
            x = -x;
            flag = -1;
        }
        int ret = 0;
        while(true) {
            int yu= x % 10;
            int chu = x /10;
            int lastret = ret;
            ret = ret * 10 + yu;
            if (ret < 0) return 0;
            if (ret / 10 != lastret) return 0;
            x = chu;
            if (x == 0) break;
        }

        return ret * flag;
    }
}
