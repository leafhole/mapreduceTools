package mr;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.util.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import format.MRUtils;


public class AliasSplit {
	private static final Log log = LogFactory.getLog(SynonymRun.class);

	public AliasSplit() {
		// TODO Auto-generated constructor stub
	}
	
	public static ArrayList<String> parse(String line) throws UnsupportedEncodingException {
		ArrayList<String> ret = new ArrayList<String>();
		//line = MRUtils.getGBKString(line).trim();// ��ȡGBK�����Դ�ļ�
		Document data = null;
		try {
			data = DocumentHelper.parseText(line);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("parse error : " + line);
			return ret;
		}
		Element root = data.getRootElement();
		String id = root.elementTextTrim("DATAID").trim();
		if (id == null || id.trim().equals("")) {
			log.error("empty id : " + line);
			return ret;
		}
		
		String alias = root.elementTextTrim("ALIAS").trim();
		if (alias == null || alias.trim().equals("")) {
			log.error("empty alias : " + line);
			return ret;
		}
		
		if (!alias.contains("|")) {
			return ret;
		}
		
		for(String word:alias.split("##")) {
			
			if (word.contains("|")) {
				ArrayList<String> tmpSet = new ArrayList<String>();
				for(String token : word.split("\\|")) {
					tmpSet.add(token);
				}
				Collections.sort(tmpSet);
				String tmp = StringUtils.join("|", tmpSet);
				ret.add(tmp);
			}
		}
		
		return ret;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String line = "<POI><DATAID>10011252848</DATAID><NAME>�㽭ʡ�����н������޹�˾</NAME><CITY>����</CITY><ALIAS>�㽭ʡ|�㽭|null##������|����|null##����##���޹�˾|��˾|��|�������ι�˾</ALIAS></POI>";

		AliasSplit as = new AliasSplit();
		ArrayList<String> ret = new ArrayList<String>();
		try {
			ret = as.parse(line);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(ret);
	}

}
