package search.util;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import secret.com.SecretConstants;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import common.lib.Log;

/**
 * Created by yds on 2017/5/19.
 */
public class DetailInfo {
	
	public PoiInfo getPoiInfo(String dataid) {
		PoiInfo ret = new PoiInfo();
		String acURL = SecretConstants.detailPageURL + dataid;
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(acURL).openStream(), "GBK", acURL);
        } catch (IOException e) {
            e.printStackTrace();
            Log.logger.error(Log.getErrorInfoFromException(e));
        }
		String context =  doc.toString();
		int f = context.indexOf("(");
		int e = context.lastIndexOf(")");
//		System.out.println("[ " + f + " , " + e + " ]");
//		System.out.println(context.substring(f + 1, e));
		String jsonString = context.substring(f + 1, e);
		JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
		if (jsonObject.get("caption") == null) {
			return ret;
		}
		String caption = jsonObject.get("caption").getAsString(); //John
		String city = jsonObject.get("city").getAsString(); //John
		String retdataid = jsonObject.get("id").getAsString(); //John

		String category = jsonObject.get("category").getAsString(); //John

		String subcategory = jsonObject.get("subcategory").getAsString(); //John

		
		Float x = jsonObject.get("x").getAsFloat(); //John
		Float y = jsonObject.get("y").getAsFloat();
		
		ret.setCaption(caption);
		ret.setCity(city);
		ret.setDataid(dataid);
		ret.setX(x);
		ret.setY(y);
		ret.setCategory(category);
		ret.setSubcategory(subcategory);
		return ret;
	}
	
	public static void main(String[] args) {
		String dataid = "1_D1000055245373";
		DetailInfo di = new DetailInfo();
		PoiInfo tmp = di.getPoiInfo(dataid);
		System.out.println(tmp);
	}
	
}
