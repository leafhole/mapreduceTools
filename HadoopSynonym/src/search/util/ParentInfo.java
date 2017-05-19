package search.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import common.lib.Log;

public class ParentInfo {

    public class ParentInfoItem implements java.io.Serializable{
        @Override
        public String toString() {
            return "ParentInfoItem [pitype=" + pitype + ", dataid=" + dataid
                    + ", name=" + name + ", extraMap=" + extraMap + "]";
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
//			result = prime * result + getOuterType().hashCode();
            result = prime * result
                    + ((dataid == null) ? 0 : dataid.hashCode());
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result
                    + ((pitype == null) ? 0 : pitype.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ParentInfoItem other = (ParentInfoItem) obj;
//			if (!getOuterType().equals(other.getOuterType()))
//				return false;
            if (dataid == null) {
                if (other.dataid != null)
                    return false;
            } else if (!dataid.equals(other.dataid))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (pitype == null) {
                if (other.pitype != null)
                    return false;
            } else if (!pitype.equals(other.pitype))
                return false;
            return true;
        }
        public String pitype;
        public String dataid;
        public String name;
        public Map<String, String> extraMap;
        public ParentInfoItem(String pitype, String id, String nm, Map<String, String> extraMap) {
            dataid = id;
            name = nm;
            this.pitype = pitype;
            this.extraMap = extraMap;
        }
//		private ParentInfo getOuterType() {
//			return ParentInfo.this;
//		}
    }
    public static final String SEP = "@@@";
    // $$PSQ:$id:D1000511263265##$name:五道口@@@$id:D1000511266801##$name:中关村$$
    public ParentInfo() {
        // TODO Auto-generated constructor stub
    }

    public List<ParentInfoItem> process(String line) {
//		line = line.substring("$$PSQ:".length());
//		if (line.endsWith("$$")) {
//			line = line.substring(0, line.length() - 2);
//		}
        if (StringUtils.isEmpty(line)) {
            return new ArrayList<ParentInfoItem>();
        }
        List<ParentInfoItem> ret = new ArrayList<ParentInfoItem>();
        String[] groups = line.split(",");
        for(String group : groups) {
            if (!group.contains(":")) {
                Log.logger.error("errFmt:\t" + line);
                continue;
            }
            String groupName = group.split(":")[0];
            group = group.substring(groupName.length() + 1);
            groupName = groupName.substring(2);

            if (group.endsWith("$$")) {
                group = group.substring(0, group.length() - 2);
            }
            String[] items = group.split("@@@");
            for (String itemStr: items) {
                Map<String, String> extraMap = new HashMap<String, String>();
                String[] words = itemStr.split("##");
                String idStr = "";
                String nameStr = "";
                for(String word : words) {
//					System.out.println(word);
                    String[] pairs = word.split(":");
                    if (pairs.length != 2) {
                        Log.logger.error("error Fmt:\t" + line);
                        continue;
                    }
                    String first = pairs[0];
                    String second = pairs[1];
                    if (first.equals("$id")) {
//						if (second.charAt(0) == 'D') {
                        second = "1_" + second;
                        idStr = second;
//						}
                    }

                    first = first.substring(1);
                    if (first.equals("name")) {
                        nameStr = second;
                    }
                    extraMap.put(first, second);
                }
//				String idStr = words[0];
//				String nameStr = words[1];
//				idStr = idStr.substring("$id:".length());
//				if (idStr.charAt(0) == 'D') {
//					idStr = "1_" + idStr;
//				}
//				nameStr = nameStr.substring("$name:".length());
                ret.add(new ParentInfoItem(groupName, idStr, nameStr, extraMap));
            }
        }

        return ret;
    }



    public static void main(String[] args) {
        ParentInfo pi = new ParentInfo();
        String line = "$$PSQ:$id:D1000511265283##$name:北下关@@@$id:D1000511266801##$name:中关村$$,$$PVGR:$id:DVGR_00000001##$name:北京理工大学$$";
        line = "$$PRQ:$id:D_FOCUS_0_0_1_10001180##$name:财源国际中心##$sn:北京财源国际中心A座##$st:写字楼$$,$$PPQ:$id:D1000149388930##$name:北京IFC大厦B座@@@$id:D1000149388801##$name:北京国际财源中心B座$$,$$PSQ:$id:D1000513880726##$name:国贸@@@$id:D1000513880610##$name:建国门@@@$id:D1000513880746##$name:建外大街@@@$id:D1000513880632##$name:CBD$$";
        line = "$$PADD:$id:D_FOCUS_0_0_5_10085651##$name:财富大厦$$,$$PRQ:$id:D_FOCUS_0_0_5_10085651##$name:财富大厦##$st:简称不可信$$,$$PSQ:$id:D1000536030642##$name:西场@@@$id:D1000513880346##$name:松洲@@@$id:D1000513880287##$name:罗冲围@@@$id:D1000513880351##$name:二三路$$,$$PLW:$id:null##$name:增槎路$$";
        List<ParentInfoItem> piis = pi.process(line);
        for(ParentInfoItem pii:piis) {
            System.out.println(pii);
        }

    }

}
