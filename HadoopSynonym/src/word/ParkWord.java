package word;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * ��԰��ʸ��ھ�
 * 
 * @author xujianmin
 * 
 */
public class ParkWord {

	// ������
	public ParkWord() {

	}

	/**
	 * ����A����������
	 * 
	 * @param input
	 * @param output1
	 */
	public void analyze1(String input, String output1, String output2) {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(new File(
					input)));
			BufferedWriter writer = new BufferedWriter(new FileWriter(output1,
					false));
			BufferedWriter writer2 = new BufferedWriter(new FileWriter(output2,
					false));
			String line = null;
			while ((line = bf.readLine()) != null) {
				// 20038955081000 �˴�ũ��԰ ���ξ��� �ȼٴ� ����ʡ �人�� ������
				if (line.trim().equals(""))
					continue;
				line = line.trim();
				String[] strs = line.split("\t");
				if (strs.length != 7) {
					System.out.println("���ݸ�ʽ����:" + line);
					continue;
				}
				String name = strs[1].trim();
				// if (name.equals("��Ͽ��������ζȼٷ羰��")) {
				// System.out.println(name);
				// }
				String category = strs[2].trim();
				String subCategory = strs[3].trim();
				if (!category.equals("���ξ���"))
					continue;
				if (subCategory.equals("����") || subCategory.equals("ͣ����"))
					continue;
				if (!subCategory.contains("A����"))
					continue;
				String province = strs[4].trim();
				String city = strs[5].trim();
				String county = strs[6].trim();

				ParkResult pr = new ParkResult(name);
				pr.getPair(province, city, county);
				if (!pr.hasShortName) {
					continue;
				}

				String strValue = "";
				boolean isFirst = true;
				for (String value : pr.valueSet) {
					if (!isFirst) {
						strValue += "#";
					}
					strValue += value;
					isFirst = false;
				}
				writer.write(pr.key + "##" + strValue + "\t" + line);
				writer.newLine();
				writer.flush();

				// ��׺����
				boolean hasFilterSuffix = pr.judgeFilter(pr.valueSet);
				// 5A4A���ӹ���,�����ӹ���
				if (subCategory.contains("5A4A"))
					hasFilterSuffix = true;
				if (hasFilterSuffix) {
					writer2.write(pr.normalizeKey + "##" + strValue);
					writer2.newLine();
					writer2.flush();
					// writer2.write(pr.key + "##" + strValue + "\t" + line);
					// writer2.newLine();
					// writer2.flush();
				}
			}

			writer2.flush();
			writer2.close();
			writer.flush();
			writer.close();
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����A����������
	 * 
	 * @param input
	 * @param output2
	 */
	public void analyze2(String input, String output2, String output3) {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(new File(
					input)));
			BufferedWriter writer = new BufferedWriter(new FileWriter(output2,
					false));
			BufferedWriter writer2 = new BufferedWriter(new FileWriter(output3,
					false));
			String line = null;
			while ((line = bf.readLine()) != null) {
				if (line.trim().equals(""))
					continue;
				String[] strs = line.split("\t");
				if (strs.length != 7) {
					System.out.println("���ݸ�ʽ����:" + line);
					continue;
				}

				String name = strs[1].trim();
				String category = strs[2].trim();
				String subCategory = strs[3].trim();
				if (!category.equals("���ξ���"))
					continue;
				if (subCategory.equals("����") || subCategory.equals("ͣ����"))
					continue;
				if (subCategory.contains("A����"))
					continue;
				String province = strs[4].trim();
				String city = strs[5].trim();
				String county = strs[6].trim();

				ParkResult pr = new ParkResult(name);
				pr.getPair(province, city, county);
				if (!pr.hasShortName) {
					continue;
				}
				String strValue = "";
				boolean isFirst = true;
				for (String value : pr.valueSet) {
					if (!isFirst) {
						strValue += "#";
					}
					strValue += value;
					isFirst = false;
				}
				writer.write(pr.key + "##" + strValue + "\t" + line);
				writer.newLine();
				writer.flush();

				// ��׺����
				boolean hasFilterSuffix = pr.judgeFilter(pr.valueSet);
				if (hasFilterSuffix) {
					writer2.write(pr.normalizeKey + "##" + strValue);
					writer2.newLine();
					writer2.flush();
					// writer2.write(pr.key + "##" + strValue + "\t" + line);
					// writer2.newLine();
					// writer2.flush();
				}
			}

			writer2.flush();
			writer2.close();
			writer.flush();
			writer.close();
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		File file = new File("./word");
		if (!file.isDirectory()) {
			file.mkdir();
		}
		// System.out.println("�������ξ�������...");
		// DataLoader loader = new DataLoader();
		// loader.getParkData("./word/park_data.txt");
		System.out.println("��԰��ʸ��ھ�...");
		ParkWord pw = new ParkWord();
		pw.analyze1("./word/park_data.txt", "./word/park_result1.txt",
				"./word/park_result1_filter.txt");
		pw.analyze2("./word/park_data.txt", "./word/park_result2.txt",
				"./word/park_result2_filter.txt");
		System.out.println("�ʸ��ھ���ɡ�");
	}

}
