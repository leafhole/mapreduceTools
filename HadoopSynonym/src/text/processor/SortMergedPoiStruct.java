package text.processor;

import common.lib.Log;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import search.util.KeywordsUtils;
import search.util.ParentInfo;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by yds on 2017/5/17.
 */
public class SortMergedPoiStruct {


    public void init(String filename) {
        int lineNo = 0;
        File file = new File(filename);
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(file);
        } catch (IOException e) {
            Log.logger.error(filename + "\t" + Log.getErrorInfoFromException(e) );
            e.printStackTrace();
        }
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                if (line.startsWith("#")) {
                    continue;
                }
                processLine(line);
            }
        }catch (Exception e) {
            Log.logger.error(Log.getErrorInfoFromException(e));
        } finally {
        LineIterator.closeQuietly(it);
        Log.logger.info("loading " + filename +" finished. line:\t" + lineNo );
    }

}


    public void processLine(String line) {
//        System.out.println(line);

        String[] words = line.split("\t");
        int i = 0;
        String parentDataid = words[i++];
        String pCate = words[i++];
        String pSub = words[i++];
        i++;
        String dataid = words[i++];
        String caption = words[i++];
        String cate = words[i++];
        String sub = words[i++];
        String province = words[i++];
        String city = words[i++];
        String county = words[i++];
        String keywords = words[i++];
        String parentInfo = words[i++];

/*
        System.out.println(parentDataid + pCate + pSub);
        System.out.println(keywords);
        System.out.println(parentInfo);
*/
        String pinpai = KeywordsUtils.getPinpai(keywords);
        ParentInfo pi = new ParentInfo();
        List<ParentInfo.ParentInfoItem> piis = pi.process(parentInfo);
        String parentName = "";
        String shortName = "";
        String road = "";
        String shangquan = "";
        for(ParentInfo.ParentInfoItem pii:piis) {
//            System.out.println(pii);
            if (pii.pitype.equals("PRQ")) {
                parentName = pii.name;
                if (pii.extraMap.containsKey("sn")) {
                    shortName = pii.extraMap.get("sn");
                }
            }
            if (pii.pitype.equals("PLW")) {
                road = pii.name;
            }
            if (pii.pitype.equals("PSQ")) {
                shangquan = pii.name;
            }
        }

//        System.out.println(city+"\t" + county +"\t"+pCate  +"\t" + pSub + "\t"+ cate +"\t"+sub + "\t" + caption+"\t"+parentName + "\t" + shortName + "\t" + road + "\t" + shangquan + "\t" + pinpai);
    }

    public static void main(String[] args) {

        String line = "1_09000008972\t公司企业\t公司\t1000490238483\t1_D1000490238483\t北京七星博交通设施工程有限公司-西门\t 房地产\t大门\t北京市\t北京市\t朝阳区\t$$GAODE:通行设施-临街院门-临街院门$$\t$$PRQ:$id:09000008972##$name:北京七星博交通设施工程有限公司##$sn:西门##$st:门口$$,$$PLW:$id:null##$name:七星路$$\tnull    S       S01     Label011.2977319286E7   4816279.879     1.2977319286E7  4816279.879     miawWmx}dH      C       null";
        SortMergedPoiStruct smps = new SortMergedPoiStruct();
        String filename = args[0];
        Log.logger.info("loading " + filename +" finished. line:\t" + 0 );

        smps.init(filename);
//        smps.processLine(line);
    }
}
