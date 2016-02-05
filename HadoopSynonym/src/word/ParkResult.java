package word;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;

public class ParkResult {

	/**
	 * 用于结果后缀词过滤
	 */
	public static String[] filterSuffix = { "山", "峰", "岭", "峡", "水", "河", "池",
			"湖", "湾", "海", "泉", "沟", "沙", "渡", "港", "洞", "滩", "潭", "源", "湿地",
			"草原", "瀑布", "水库", "塘", "塔", "坝", "顶", "关", "寨", "岛", "岩", "崖",
			"绝壁", "石", "寺", "庵", "庙", "遗址", "林", "树", "棚", "台", "道", "窟" };

	/**
	 * 可缩减后缀词表
	 */
	public static String[] reduce_suffixs = { "古镇", "公园", "小公园", "海洋公园",
			"滨海公园", "山顶公园", "国际公园", "乡野公园", "郊野公园", "水上公园", "文化公园", "森林公园",
			"植物公园", "生态公园", "国家森林公园", "省级地质公园", "国家地质公园", "地质公园", "国家城市湿地公园",
			"国家湿地公园", "城市湿地公园", "生态湿地公园", "省级湿地公园", "国际湿地公园", "风景区", "休闲旅游风景区",
			"森林度假景区", "旅游度假风景区", "红色生态风景区", "生态旅游风景区", "旅游风景区", "红荷旅游风景区",
			"生态自然风景区", "原生态景区", "名胜风景区", "生态风景区", "自然风景区", "景区", "园林景区",
			"森林景区", "旅游景区", "生态旅游景区", "生态农业景区", "生态景区", "文化旅游景区", "国家水利风景区",
			"国家级森林公园", "城郊森林公园", "国家示范森林公园", "城市森林公园", "省级森林公园", "世界地质公园",
			"国家级水利风景区", "自然生态景区", "城市中央生态公园", "国家级风景区", "国家矿山公园", "旅游区",
			"生态旅游区", "自然旅游区", "文化旅游区", "风景旅游区", "军事旅游区", "历史文化旅游区", "自然风景旅游区",
			"国际滨海旅游区", "假日海滩旅游区", "省级森林生态旅游区", "国家AAA级旅游区", "生态农业旅游区",
			"生态休闲旅游区", "原生态黎苗文化旅游区", "旅游度假区" };

	/**
	 * 后缀词黑名单,遇到以该词结尾的数据，不作处理
	 */
	public static String[] endBlacks = { "鲁迅公园", "嘉庚公园", "自然公园", "中央公园",
			"中山公园", "中心公园", "长征公园", "人民公园", "老人公园", "青年公园", "智力公园", "城郊公园",
			"艺术公园", "世纪公园", "文体公园", "迎宾公园", "世界公园", "健身公园", "广场公园", "农民公园",
			"文化公园", "体育公园", "主题公园", "纪念公园", "音乐公园", "儿童公园", "文化景区", "国学公园",
			"街心公园", "市民公园", "民生公园", "科技公园", "长江公园", "王府景区", "纪念馆景区", "环城公园",
			"雕塑公园", "休闲公园", "运动公园", "商业公园", "国防公园", "城市公园", "自然公园", "昆仑文化旅游景区",
			"少儿休闲旅游区" };
	/**
	 * 前缀黑名单,用于结果过滤
	 */
	public static String[] startBlacks = { "旅游", "地质", "生态", "城市", "自然", "桃花",
			"大湿地", "江滨", "山顶", "千年", "中心", "城北", "城西", "城东", "华北", "城南", "音乐",
			"世界", "城市", "西门", "长城", "古镇", "石油", "动植物", "原始", "博物馆", "古文化",
			"恐龙", "国家" };

	public static String[] decorateSuffix = { "湿地", "生态湿地", "海滨", "游猎", "漂流",
			"沙漠", "农家乐", "水利枢纽", "地震遗址", "国家考古遗址", "热带雨林", "文物古迹", "茶文化园",
			"温泉", "遗址", "瀑布", "彩虹瀑布", "农场", "景观", "桃园", "公园", "自然风景区" };

	/**
	 * 可省略的后缀词列表
	 */
	public static ArrayList<String> suffixList = new ArrayList<String>();
	/**
	 * 黑名单(不可省略)的后缀词列表
	 */
	public static ArrayList<String> blackSuffixList = new ArrayList<String>();

	// /**
	// * 省、市、区列表，用于缩减前缀词
	// */
	// public static ArrayList<String> zoneList = new ArrayList<String>();

	public static HashSet<String> allZoneSet = new HashSet<String>();// 记录所有的省、市、区名称

	/**
	 * 改进版，只对当前POI的所属省市区做前缀缩减
	 */
	public static Hashtable<String, HashSet<String>> zoneTable = new Hashtable<String, HashSet<String>>();

	/**
	 * 前缀黑名单，用于过滤结果
	 */
	public static ArrayList<String> preBlackList = new ArrayList<String>();

	/**
	 * 修饰词尾
	 */
	public static ArrayList<String> decorateSuffixList = new ArrayList<String>();

	static {
		loadSuffix();
		loadZone("./word/zone.txt");
	}

	public static void loadZone(String input) {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(new File(
					input)));
			String line = null;
			while ((line = bf.readLine()) != null) {
				if (line.trim().equals(""))
					continue;
				String[] strs = line.trim().split("#");
				if (strs.length < 2) {
					System.out.println("格式有误:" + line);
					continue;
				}
				String key = strs[0];
				HashSet<String> set = new HashSet<String>();
				for (String str : strs) {
					if (str.trim().equals(""))
						continue;
					set.add(str.trim());
					allZoneSet.add(str.trim());
				}
				set.add("中国");
				allZoneSet.add("中国");
				if (zoneTable.containsKey(key)) {
					// System.out.println("重复的key，请检查文件:" + line);
					continue;
				}
				zoneTable.put(key, set);
			}
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public static void loadZone2(String input) {
	// try {
	// BufferedReader bf = new BufferedReader(new FileReader(new File(
	// input)));
	// String line = null;
	// while ((line = bf.readLine()) != null) {
	// if (line.trim().equals(""))
	// continue;
	// // 广西壮族自治区#广西
	// String[] strs = line.trim().split("#");
	// for (String str : strs) {
	// if (str.trim().equals(""))
	// continue;
	// if (!zoneList.contains(str.trim())) {
	// zoneList.add(str.trim());
	// }
	// }
	// zoneList.add("中国");
	// }
	//
	// // 从长到短排序
	// Collections.sort(zoneList, new Comparator() {
	// public int compare(Object o1, Object o2) {
	// String s1 = (String) o1;
	// String s2 = (String) o2;
	// if (s1.length() > s2.length())
	// return -1;
	// else if (s1.length() == s2.length())
	// return 0;
	// else
	// return 1;
	// }
	// });
	//
	// bf.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public static void loadSuffix() {

		// 加载前缀黑名单
		for (String str : startBlacks) {
			if (str.trim().equals(""))
				continue;
			if (preBlackList.contains(str.trim()))
				continue;
			preBlackList.add(str.trim());
		}
		for (String str : decorateSuffix) {
			if (str.trim().equals(""))
				continue;
			if (preBlackList.contains(str.trim()))
				continue;
			preBlackList.add(str.trim());
		}

		// 加载修饰词
		for (String str : decorateSuffix) {
			if (str.trim().equals(""))
				continue;
			if (decorateSuffixList.contains(str.trim()))
				continue;
			decorateSuffixList.add(str.trim());
		}

		// 加载可省略的后缀词
		for (String str : reduce_suffixs) {
			if (str.trim().equals(""))
				continue;
			if (suffixList.contains(str.trim()))
				continue;
			suffixList.add(str.trim());
		}

		// 加载黑名单后缀词
		for (String str : endBlacks) {
			if (str.trim().equals(""))
				continue;
			if (blackSuffixList.contains(str.trim()))
				continue;
			blackSuffixList.add(str.trim());
		}

		// 从长到短排序
		Collections.sort(suffixList, new Comparator() {
			public int compare(Object o1, Object o2) {
				String s1 = (String) o1;
				String s2 = (String) o2;
				if (s1.length() > s2.length())
					return -1;
				else if (s1.length() == s2.length())
					return 0;
				else
					return 1;
			}
		});
		// 从长到短排序
		Collections.sort(decorateSuffixList, new Comparator() {
			public int compare(Object o1, Object o2) {
				String s1 = (String) o1;
				String s2 = (String) o2;
				if (s1.length() > s2.length())
					return -1;
				else if (s1.length() == s2.length())
					return 0;
				else
					return 1;
			}
		});
		// 从长到短排序
		Collections.sort(preBlackList, new Comparator() {
			public int compare(Object o1, Object o2) {
				String s1 = (String) o1;
				String s2 = (String) o2;
				if (s1.length() > s2.length())
					return -1;
				else if (s1.length() == s2.length())
					return 0;
				else
					return 1;
			}
		});
	}

	public boolean hasShortName = false;
	public String name = "";
	public String nameWithoutBracket = "";

	public String key = "";// 原始名称
	public String normalizeKey = "";// 去掉省、市、区前缀的名称
	public String value = "";
	public HashSet<String> valueSet = new HashSet<String>();

	public ParkResult(String name) {
		this.name = name;
		this.nameWithoutBracket = removeBracket(name);
	}

	public String removeBracket(String name) {
		if (name == null) {
			return "";
		}
		name = name.trim();
		if (!name.contains("(")) {
			return name;
		}
		int idx = name.indexOf("(");
		return name.substring(0, idx);
	}

	/**
	 * 获取公园类词根
	 */
	public void getPair(String province, String city, String county) {

		for (String suffix : blackSuffixList) {
			// 以黑名单后缀词结尾的，不提取简称
			if (nameWithoutBracket.endsWith(suffix)) {
				return;
			}
		}

		for (String suffix : suffixList) {
			if (nameWithoutBracket.endsWith(suffix)) {
				int idx = nameWithoutBracket.lastIndexOf(suffix);
				this.key = nameWithoutBracket;
				this.value = nameWithoutBracket.substring(0, idx);
				if (!this.value.trim().equals(""))
					hasShortName = true;
				break;
			}
		}

		// 当前POI所在的省、市、区名称及简称
		ArrayList<String> zoneList = new ArrayList<String>();
		if (zoneTable.containsKey(province)) {
			for (String str : zoneTable.get(province)) {
				if (!zoneList.contains(str))
					zoneList.add(str);
			}
		}
		if (zoneTable.containsKey(city)) {
			for (String str : zoneTable.get(city)) {
				if (!zoneList.contains(str))
					zoneList.add(str);
			}
		}
		if (zoneTable.containsKey(county)) {
			for (String str : zoneTable.get(county)) {
				if (!zoneList.contains(str))
					zoneList.add(str);
			}
		}
		// 从长到短排序
		Collections.sort(zoneList, new Comparator() {
			public int compare(Object o1, Object o2) {
				String s1 = (String) o1;
				String s2 = (String) o2;
				if (s1.length() > s2.length())
					return -1;
				else if (s1.length() == s2.length())
					return 0;
				else
					return 1;
			}
		});

		this.normalizeKey = getNormalizeKey(this.key, zoneList);

		if (hasShortName) {
			// 去掉省市区前缀
			String old = this.value;
			while (hasPreZone(old, zoneList)) {
				String pre = getPreZone(old, zoneList);
				// 考虑POI所在省、市、区
				if (pre.equals(""))
					break;
				old = normalize(old.substring(pre.length()));
			}
			this.value = old;
			if (this.value.equals("") || this.value.length() < 2) {
				// 简称为空，或长度小于2，舍弃
				hasShortName = false;
			}
			if (hasShortName) {
				if (decorateSuffixList.contains(this.value)) {
					// 简称等于修饰词，舍弃
					hasShortName = false;
				}
			}

			if (hasShortName) {
				// 前缀黑名单过滤
				for (String pre : preBlackList) {
					if (this.value.startsWith(pre)) {
						hasShortName = false;
						break;
					}
				}
				// 已村、乡、镇结尾的，舍弃
				if (this.value.endsWith("村") || this.value.endsWith("乡")
						|| this.value.endsWith("镇"))
					hasShortName = false;
			}
		}

		// 处理修饰词
		if (hasShortName) {
			this.valueSet.add(normalize(this.value));
			for (String decorate : decorateSuffixList) {
				if (this.value.endsWith(decorate)) {
					int idx = this.value.lastIndexOf(decorate);
					String newValue = this.value.substring(0, idx);
					if (newValue.length() >= 2) {
						this.valueSet.add(normalize(newValue));
					}
					break;
				}
			}
		}

		// 若结果中间位置包含"景区,旅游区,公园"，则舍弃
		if (hasShortName) {
			String[] tmps = { "名胜区", "旅游区", "景区", "公园" };
			for (String tmp : tmps) {
				if (this.value.contains(tmp)) {
					if (!this.value.endsWith(tmp)) {
						hasShortName = false;
						break;
					}
				}
			}
		}

		// 最后的结果，如果是省、市、区时，舍弃
		// if (hasShortName) {
		// if (allZoneSet.contains(this.value)) {
		// hasShortName = false;
		// }
		// }

	}

	/**
	 * 去掉省、市、区前缀
	 * 
	 * @param key
	 * @param zoneList
	 * @return
	 */
	public String getNormalizeKey(String key, ArrayList<String> zoneList) {
		String old = key;
		while (hasPreZone(old, zoneList)) {
			String pre = getPreZone(old, zoneList);
			// 考虑POI所在省、市、区
			if (pre.equals(""))
				break;
			old = normalize(old.substring(pre.length()));
		}
		return old;
	}

	public String normalize(String name) {
		if (name.startsWith("·") || name.startsWith("-"))
			return name.substring(1);
		return name;
	}

	/**
	 * 判断是否包含省、市、区前缀
	 * 
	 * @param name
	 * @return
	 */
	public boolean hasPreZone(String name, ArrayList<String> zoneList) {
		for (String pre : zoneList) {
			if (name.startsWith(pre)) {
				return true;
			}
		}
		return false;
	}

	public String getPreZone(String name, ArrayList<String> zoneList) {
		for (String pre : zoneList) {
			if (name.startsWith(pre)) {
				return pre;
			}
		}
		return "";
	}

	public boolean judgeFilter(HashSet<String> set) {
		for (String name : set) {
			for (String suffix : filterSuffix) {
				if (name.endsWith(suffix))
					return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		ParkResult pr = new ParkResult("北京内蒙古测试公园(哈哈)");
		// System.out.println(pr.nameWithoutBracket);
		pr.getPair("北京市", "北京市", "");
		System.out.println(pr.value);
	}

}
