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
	 * ���ڽ����׺�ʹ���
	 */
	public static String[] filterSuffix = { "ɽ", "��", "��", "Ͽ", "ˮ", "��", "��",
			"��", "��", "��", "Ȫ", "��", "ɳ", "��", "��", "��", "̲", "̶", "Դ", "ʪ��",
			"��ԭ", "�ٲ�", "ˮ��", "��", "��", "��", "��", "��", "կ", "��", "��", "��",
			"����", "ʯ", "��", "��", "��", "��ַ", "��", "��", "��", "̨", "��", "��" };

	/**
	 * ��������׺�ʱ�
	 */
	public static String[] reduce_suffixs = { "����", "��԰", "С��԰", "����԰",
			"������԰", "ɽ����԰", "���ʹ�԰", "��Ұ��԰", "��Ұ��԰", "ˮ�Ϲ�԰", "�Ļ���԰", "ɭ�ֹ�԰",
			"ֲ�﹫԰", "��̬��԰", "����ɭ�ֹ�԰", "ʡ�����ʹ�԰", "���ҵ��ʹ�԰", "���ʹ�԰", "���ҳ���ʪ�ع�԰",
			"����ʪ�ع�԰", "����ʪ�ع�԰", "��̬ʪ�ع�԰", "ʡ��ʪ�ع�԰", "����ʪ�ع�԰", "�羰��", "�������η羰��",
			"ɭ�ֶȼپ���", "���ζȼٷ羰��", "��ɫ��̬�羰��", "��̬���η羰��", "���η羰��", "������η羰��",
			"��̬��Ȼ�羰��", "ԭ��̬����", "��ʤ�羰��", "��̬�羰��", "��Ȼ�羰��", "����", "԰�־���",
			"ɭ�־���", "���ξ���", "��̬���ξ���", "��̬ũҵ����", "��̬����", "�Ļ����ξ���", "����ˮ���羰��",
			"���Ҽ�ɭ�ֹ�԰", "�ǽ�ɭ�ֹ�԰", "����ʾ��ɭ�ֹ�԰", "����ɭ�ֹ�԰", "ʡ��ɭ�ֹ�԰", "������ʹ�԰",
			"���Ҽ�ˮ���羰��", "��Ȼ��̬����", "����������̬��԰", "���Ҽ��羰��", "���ҿ�ɽ��԰", "������",
			"��̬������", "��Ȼ������", "�Ļ�������", "�羰������", "����������", "��ʷ�Ļ�������", "��Ȼ�羰������",
			"���ʱ���������", "���պ�̲������", "ʡ��ɭ����̬������", "����AAA��������", "��̬ũҵ������",
			"��̬����������", "ԭ��̬�����Ļ�������", "���ζȼ���" };

	/**
	 * ��׺�ʺ�����,�����Ըôʽ�β�����ݣ���������
	 */
	public static String[] endBlacks = { "³Ѹ��԰", "�θ���԰", "��Ȼ��԰", "���빫԰",
			"��ɽ��԰", "���Ĺ�԰", "������԰", "����԰", "���˹�԰", "���깫԰", "������԰", "�ǽ���԰",
			"������԰", "���͹�԰", "���幫԰", "ӭ����԰", "���繫԰", "����԰", "�㳡��԰", "ũ��԰",
			"�Ļ���԰", "������԰", "���⹫԰", "���԰", "���ֹ�԰", "��ͯ��԰", "�Ļ�����", "��ѧ��԰",
			"���Ĺ�԰", "����԰", "������԰", "�Ƽ���԰", "������԰", "��������", "����ݾ���", "���ǹ�԰",
			"���ܹ�԰", "���й�԰", "�˶���԰", "��ҵ��԰", "������԰", "���й�԰", "��Ȼ��԰", "�����Ļ����ξ���",
			"�ٶ�����������" };
	/**
	 * ǰ׺������,���ڽ������
	 */
	public static String[] startBlacks = { "����", "����", "��̬", "����", "��Ȼ", "�һ�",
			"��ʪ��", "����", "ɽ��", "ǧ��", "����", "�Ǳ�", "����", "�Ƕ�", "����", "����", "����",
			"����", "����", "����", "����", "����", "ʯ��", "��ֲ��", "ԭʼ", "�����", "���Ļ�",
			"����", "����" };

	public static String[] decorateSuffix = { "ʪ��", "��̬ʪ��", "����", "����", "Ư��",
			"ɳĮ", "ũ����", "ˮ����Ŧ", "������ַ", "���ҿ�����ַ", "�ȴ�����", "����ż�", "���Ļ�԰",
			"��Ȫ", "��ַ", "�ٲ�", "�ʺ��ٲ�", "ũ��", "����", "��԰", "��԰", "��Ȼ�羰��" };

	/**
	 * ��ʡ�Եĺ�׺���б�
	 */
	public static ArrayList<String> suffixList = new ArrayList<String>();
	/**
	 * ������(����ʡ��)�ĺ�׺���б�
	 */
	public static ArrayList<String> blackSuffixList = new ArrayList<String>();

	// /**
	// * ʡ���С����б���������ǰ׺��
	// */
	// public static ArrayList<String> zoneList = new ArrayList<String>();

	public static HashSet<String> allZoneSet = new HashSet<String>();// ��¼���е�ʡ���С�������

	/**
	 * �Ľ��棬ֻ�Ե�ǰPOI������ʡ������ǰ׺����
	 */
	public static Hashtable<String, HashSet<String>> zoneTable = new Hashtable<String, HashSet<String>>();

	/**
	 * ǰ׺�����������ڹ��˽��
	 */
	public static ArrayList<String> preBlackList = new ArrayList<String>();

	/**
	 * ���δ�β
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
					System.out.println("��ʽ����:" + line);
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
				set.add("�й�");
				allZoneSet.add("�й�");
				if (zoneTable.containsKey(key)) {
					// System.out.println("�ظ���key�������ļ�:" + line);
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
	// // ����׳��������#����
	// String[] strs = line.trim().split("#");
	// for (String str : strs) {
	// if (str.trim().equals(""))
	// continue;
	// if (!zoneList.contains(str.trim())) {
	// zoneList.add(str.trim());
	// }
	// }
	// zoneList.add("�й�");
	// }
	//
	// // �ӳ���������
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

		// ����ǰ׺������
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

		// �������δ�
		for (String str : decorateSuffix) {
			if (str.trim().equals(""))
				continue;
			if (decorateSuffixList.contains(str.trim()))
				continue;
			decorateSuffixList.add(str.trim());
		}

		// ���ؿ�ʡ�Եĺ�׺��
		for (String str : reduce_suffixs) {
			if (str.trim().equals(""))
				continue;
			if (suffixList.contains(str.trim()))
				continue;
			suffixList.add(str.trim());
		}

		// ���غ�������׺��
		for (String str : endBlacks) {
			if (str.trim().equals(""))
				continue;
			if (blackSuffixList.contains(str.trim()))
				continue;
			blackSuffixList.add(str.trim());
		}

		// �ӳ���������
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
		// �ӳ���������
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
		// �ӳ���������
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

	public String key = "";// ԭʼ����
	public String normalizeKey = "";// ȥ��ʡ���С���ǰ׺������
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
	 * ��ȡ��԰��ʸ�
	 */
	public void getPair(String province, String city, String county) {

		for (String suffix : blackSuffixList) {
			// �Ժ�������׺�ʽ�β�ģ�����ȡ���
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

		// ��ǰPOI���ڵ�ʡ���С������Ƽ����
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
		// �ӳ���������
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
			// ȥ��ʡ����ǰ׺
			String old = this.value;
			while (hasPreZone(old, zoneList)) {
				String pre = getPreZone(old, zoneList);
				// ����POI����ʡ���С���
				if (pre.equals(""))
					break;
				old = normalize(old.substring(pre.length()));
			}
			this.value = old;
			if (this.value.equals("") || this.value.length() < 2) {
				// ���Ϊ�գ��򳤶�С��2������
				hasShortName = false;
			}
			if (hasShortName) {
				if (decorateSuffixList.contains(this.value)) {
					// ��Ƶ������δʣ�����
					hasShortName = false;
				}
			}

			if (hasShortName) {
				// ǰ׺����������
				for (String pre : preBlackList) {
					if (this.value.startsWith(pre)) {
						hasShortName = false;
						break;
					}
				}
				// �Ѵ塢�硢���β�ģ�����
				if (this.value.endsWith("��") || this.value.endsWith("��")
						|| this.value.endsWith("��"))
					hasShortName = false;
			}
		}

		// �������δ�
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

		// ������м�λ�ð���"����,������,��԰"��������
		if (hasShortName) {
			String[] tmps = { "��ʤ��", "������", "����", "��԰" };
			for (String tmp : tmps) {
				if (this.value.contains(tmp)) {
					if (!this.value.endsWith(tmp)) {
						hasShortName = false;
						break;
					}
				}
			}
		}

		// ���Ľ���������ʡ���С���ʱ������
		// if (hasShortName) {
		// if (allZoneSet.contains(this.value)) {
		// hasShortName = false;
		// }
		// }

	}

	/**
	 * ȥ��ʡ���С���ǰ׺
	 * 
	 * @param key
	 * @param zoneList
	 * @return
	 */
	public String getNormalizeKey(String key, ArrayList<String> zoneList) {
		String old = key;
		while (hasPreZone(old, zoneList)) {
			String pre = getPreZone(old, zoneList);
			// ����POI����ʡ���С���
			if (pre.equals(""))
				break;
			old = normalize(old.substring(pre.length()));
		}
		return old;
	}

	public String normalize(String name) {
		if (name.startsWith("��") || name.startsWith("-"))
			return name.substring(1);
		return name;
	}

	/**
	 * �ж��Ƿ����ʡ���С���ǰ׺
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
		ParkResult pr = new ParkResult("�������ɹŲ��Թ�԰(����)");
		// System.out.println(pr.nameWithoutBracket);
		pr.getPair("������", "������", "");
		System.out.println(pr.value);
	}

}
