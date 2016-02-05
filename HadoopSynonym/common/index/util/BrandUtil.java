package index.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import common.lib.Log;
import common.lib.Number;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BrandUtil {

	public static final String CATEGORY_SEPERATOR = " : ";
	public static final String DATAID_SEPERATOR = " ";
	public static final String SMALL_LEFT_BRACKET = "(";
	public static final String SMALL_RIGHT_BRACKET = ")";
	private static final String MIDDLE_LEFT_BRACKET = "[";
	private static final String FILENAME = "keys.txt";
	private static final String BRAND_SEPERATOR = ";"; 
	public static final String DATAID_PREFIX = "1_";
	private static final String BRAND_AND_POI_SEPERATOR = ":";

	@NotNull
    private Map<String, Set<String>> brandToIds = new HashMap<String, Set<String>>();
	@NotNull
    private Map<String, String> idToBrand = new HashMap<String, String>();
	
	@NotNull
    private static BrandUtil instance = new BrandUtil();
	
	@NotNull
    public static BrandUtil getInstance() {
		return instance;
	}
	
	@Nullable
    private String fileName = null;
	private BrandUtil() {
		fileName = ResourceBundle.getBundle("index").getString("synonymPath");
		initBrandInfo();
	}
	private void initBrandInfo() {
		
		File file = new File(fileName, FILENAME);
		System.out.println("start to process brand");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = reader.readLine()) != null) {
				String[] parts = line.split(CATEGORY_SEPERATOR);
				if(parts.length >= 5) {
					String[] allBrands = parts[2].split(BRAND_SEPERATOR);
					if(allBrands == null || allBrands.length == 0) {
						System.out.println("error data" + line);
						continue;
					}
					Set<String> validBrands = new HashSet<String>();
					for(String bInfo : allBrands) {
						//巴乡鱼头(0);巴乡鱼头火锅(1)
						String validBrand = getValidBrand(bInfo);
						int count = getCount(bInfo);
						if(count > 0 && validBrand != null) {
							validBrands.add(validBrand);
						}
					}
					String brand = dropBrackets(parts[1], SMALL_LEFT_BRACKET);
					String[] uids = parts[parts.length-1].split(DATAID_SEPERATOR);
					if(uids.length > 0) {
						Set<String> ids = new HashSet<String>();
						for(String i : uids) {
							//D_KUXUN_998_shanghai_guangxinlu[格林豪泰:格林豪泰(上海光新路店)]
							String rawId = dropBrackets(i, MIDDLE_LEFT_BRACKET);
							String poiBrand = getBrandFromInfo(i);
							if(poiBrand == null) {
								System.out.println("error data in brand uitl and line is:" + line);
								continue;
							}
							if(validBrands.contains(poiBrand)) {
								String finalId = DATAID_PREFIX+rawId;
								idToBrand.put(finalId, poiBrand);
								ids.add(finalId);
							}
						}
						brandToIds.put(brand, ids);
					}
				}
				else {
					Log.logger.error("wrong line and line is:" + line);
				}
			}
		} catch (FileNotFoundException e) {
			Log.logger.error("process brand util exception", e);
		} catch (IOException e) {
			Log.logger.error("process brand util exception", e);
		}
		finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("end to process brand");
	}
	
	private static int getCount(@Nullable String bInfo) {
		// 巴乡鱼头(0)
		try {
			if(bInfo == null || bInfo.trim().length() == 0) {
				return -1;
			}
			int startIndex = bInfo.indexOf(SMALL_LEFT_BRACKET);
			int endIndex = bInfo.indexOf(SMALL_RIGHT_BRACKET);
			return Integer.parseInt(bInfo.substring(startIndex+1, endIndex));
		}
		catch(Exception e) {
			System.out.println("try to get count in brand util error and bInfo is:" + bInfo);
		}
		return -1;
	}
	@Nullable
    private static String getValidBrand(@Nullable String bInfo) {
		//巴乡鱼头(0)
		if(bInfo == null || bInfo.trim().length() == 0) {
			return null;
		}
		int index = bInfo.indexOf(SMALL_LEFT_BRACKET);
		if(index != -1) {
			return bInfo.substring(0, index);
		}
		else {
			return null;
		}
	}
	@Nullable
    private static String getBrandFromInfo(@Nullable String brandInfo) {
		// 20004945632[卓越培訓發展中心:卓越培訓發展中心(環球商貿)] 
		if(brandInfo == null || brandInfo.trim().length() == 0) {
			return null;
		}
		int startIndex = brandInfo.indexOf(MIDDLE_LEFT_BRACKET);
		int endIndex = brandInfo.indexOf(BRAND_AND_POI_SEPERATOR);
		if(startIndex != -1 && endIndex != -1) {
			return brandInfo.substring(startIndex+1, endIndex);
		}
		else {
			return null;
		}
	}
	@Nullable
    public static String dropBrackets(@Nullable String brand, @NotNull String bracket) {
		if(brand == null || brand.trim().length() == 0) {
			return brand;
		}
		int index = -1;
		if((index = brand.lastIndexOf(bracket)) != -1) {
			brand = brand.substring(0, index);
		}
		return brand;
	}
	@Nullable
    public Set<String> getDataIds(@Nullable String brand) {
		if(brand == null || brand.trim().length() == 0) {
			return null;
		}
		return brandToIds.get(brand);
	}
	
	@Nullable
    public String getBrand(@Nullable String id) {
		if(id == null || id.trim().length() == 0) {
			return null;
		}
		return idToBrand.get(id);
	}
	
	@NotNull
    public Set<String> getIds() {
		return idToBrand.keySet();
	}
	
	@Nullable
    public static String processCaption(@Nullable String caption, @Nullable String brand) {
		try {
			if(caption == null || caption.trim().length() == 0) {
				return caption;
			}
			if(brand == null || brand.trim().length() == 0) {
				return caption;
			}
			String origCaption = caption;
			caption = caption.replaceAll("\\(", "").replaceAll("\\)", "");
			int start = caption.indexOf(brand);	
			int length = brand.length();
			if(start == -1) {
				caption = caption.toLowerCase();
				brand = brand.toLowerCase();
				//brand may translate from number to character
				String tempCaption = Number.transDigital(caption);
				start = tempCaption.indexOf(brand);
				if(start != -1) {
					length += (caption.length()-tempCaption.length());
				}
			}
			if(start != -1) {
				if(start+length == caption.length()) {
					return caption;
				}
				StringBuilder ret = new StringBuilder(caption.length()+2);
				ret.append(caption.substring(0, start+length)).append("(").append(caption.substring(start+length)).append(")");
				return ret.toString();
			}
			else {
				return origCaption;
			}
		}
		catch(Exception e) {
			Log.logger.error("process caption error:" + caption + "," + brand);
		}
		return caption;
	}
	
	@NotNull
    public Set<String> getBrands() {
		return brandToIds.keySet();
	}
	
	public static void main(String[] args) {
//		String caption = "北京清华大学东门";
//		String brand = "清华大学";
//		System.out.println(processCaption(caption, brand));
//		
//		caption = "北京清华大学东门";
//		brand = "北京清华大学";
//		System.out.println(processCaption(caption, brand));
//		
//		caption = "北京清华大学东门";
//		brand = "北京清华大学东门";
//		System.out.println(processCaption(caption, brand));
//		
//		caption = "北京清华大学东门";
//		brand = "清华东门大学";
//		System.out.println(processCaption(caption, brand));
//		
//		brand = "团结乡盛情园农家乐(3)";
//		System.out.println(dropBrackets(brand, SMALL_LEFT_BRACKET));
//		
//		brand = "团结乡盛情园农家乐";
//		System.out.println(dropBrackets(brand, SMALL_LEFT_BRACKET));
//		
//		brand = "12004154036[宁夏医科大学中医消化病研究所]";
//		System.out.println(dropBrackets(brand, MIDDLE_LEFT_BRACKET));
//		
//		caption = "黑龙江科技大学(松北校区)";
//		brand = "黑龙江科技大学";
//		System.out.println(processCaption(caption, brand));
		
//		String info = "20004945632[卓越培訓發展中心:卓越培訓發展中心(環球商貿)]";
//		System.out.println(getBrandFromInfo(info));
//		
//		info = "巴乡鱼头(0)";
//		System.out.println(getValidBrand(info));
//		System.out.println(getCount(info));
		
		processCaption("北京市第三十五中学", "第35中学");
	}
}
