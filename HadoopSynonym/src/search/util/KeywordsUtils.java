package search.util;

/**
 * Created by yds on 2017/5/17.
 */

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


public class KeywordsUtils {

    public static Set<String> categoryUse = new TreeSet<String> ();
    static {
        String words = "SEARCH|LS|ALS|AFB";
        categoryUse.add("$$SEARCH");
        categoryUse.add("$$LS");
        categoryUse.add("$$ALS");
        categoryUse.add("$$AFB");

    }
    /**
     *
     */
    public KeywordsUtils() {
        // TODO Auto-generated constructor stub
    }


    public static String getPinpai(String keywords) {
        String pinpai = "";
        if (keywords == null) {
            return pinpai;
        }
        for (String keyword: keywords.split(",")) {
            if (keyword.contains("$$")) {
                keyword = keyword.substring(0, keyword.length() - 2);
            }
            if (keyword.contains(":")) {
                String flag = keyword.split(":")[0];
                if (flag.equals("$$LS")) {
                    pinpai = keyword.split(":")[1];
                    break;
                }
            }
        }

        return pinpai;
    }

    public static Set<String> toCategoryWords(String keywords) {
        if (keywords == null) {
            return null;
        }
        Set<String> wordSet = new HashSet<String> ();
        for (String keyword: keywords.split(",")) {
            if (keyword.contains("$$")) {
                keyword = keyword.substring(0, keyword.length() - 2);
            }
            if (keyword.contains(":")) {
                String flag = keyword.split(":")[0];
                if (!categoryUse.contains(flag)) {
                    continue;
                }
                if (!keyword.contains(":")) {
                    System.err.println(keyword);
                }
                String[] ws = keyword.split(":");
                if (ws.length == 2) {
                    keyword = ws[1];
                    String[] words = keyword.split("-");
                    for (String word: words) {
                        if (word.isEmpty()) continue;
                        wordSet.add(word);
                    }
                } else {
//					System.out.println(keyword);
                }
            }
        }
        return wordSet;
//		for (String word: wordSet) {
//			sinfo += word + " ";
//		}
//
//		return sinfo;
    }



    public static String toSinfo(String keywords) {
        if (keywords == null) {
            return "";
        }
        String sinfo = "";
        HashSet<String> wordSet = new HashSet<String> ();
        for (String keyword: keywords.split(",")) {
            if (keyword.contains("$$")) {
                keyword = keyword.substring(0, keyword.length() - 2);
            }
            if (keyword.contains(":")) {
                String flag = keyword.split(":")[0];
                keyword = keyword.split(":")[1];
                String[] words = keyword.split("-");
                for (String word: words) {
                    wordSet.add(word);
                }
            }
        }
        for (String word: wordSet) {
            sinfo += word + " ";
        }

        return sinfo;
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String input = "$$DG:�Ѻ�����-4S��-����-��������-���ڱ���-����M$$,$$FB:��������-4S��$$,$$FB:��������-4S��-����Ʒ��-��������Ʒ��$$,$$GAODE:����ά��-������Լά��-����$$,$$NAV:����-���ֳ����׳���-���ֳ�$$,$$RC:4S��-����4S��-4S-��������$$";

        String sinfo = KeywordsUtils.toSinfo(input);
        System.out.println(sinfo);
    }

}
