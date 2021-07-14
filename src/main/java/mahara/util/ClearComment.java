package mahara.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ClearComment {

	private static int count = 0;

	/**
	 * 删除文件中的各种注释，包含//、/* * /等
	 * 
	 * @param charset 文件编码
	 * @param file    文件
	 */
	public static void clear(File file, String charset) {
		BufferedReader reader = null;
		try {
			if (!file.exists()) {
				return;
			}

			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File f : files) {
					clear(f, charset); // 递归调用
				}
				return;
			} else if (!file.getName().endsWith(".java")) {
				return;
			}
			System.out.println("-----开始处理文件：" + file.getAbsolutePath());

			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
			StringBuffer content = new StringBuffer();
			String tmp = null;
			while ((tmp = reader.readLine()) != null) {
				content.append(tmp);
				content.append("\n");
			}
			String target = content.toString();
			String s = target.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", "");
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
			out.write(s);
			out.flush();
			out.close();
			count++;
			System.out.println("-----文件处理完成---" + count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void clear(String filePath, String charset) {
		clear(new File(filePath), charset);
	}

	public static void clear(String filePath) {
		clear(new File(filePath), "UTF-8");
	}

	public static void clear(File file) {
		clear(file, "UTF-8");
	}

	public static void main(String[] args) {
		clear("C:\\work\\sts\\dmt\\src\\main\\java\\com\\cit\\dmt\\core\\model"); // 删除目录下所有java文件注释
	}
}
