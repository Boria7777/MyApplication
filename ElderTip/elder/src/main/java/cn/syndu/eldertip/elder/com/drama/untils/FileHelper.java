package cn.syndu.eldertip.elder.com.drama.untils;


import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

	public class FileHelper {
		private Context context;
		/** SD卡是否存在 **/
		private boolean hasSD = false;
		/** SD卡的路径 **/
		private String SDPATH;
		/** 当前程序包的路径 **/
		private String FILESPATH;

		public FileHelper(Context context) {
			this.context = context;
			hasSD = Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED);
			SDPATH = Environment.getExternalStorageDirectory().getPath();
			FILESPATH = this.context.getFilesDir().getPath();
		}

		/**
		 * 在SD卡上创建文件
		 *
		 * @throws IOException
		 */
		public File createSDFile(String fileName) throws IOException {
			File file = new File(SDPATH + "//" + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}else{
				System.out.println("文件已存在");
			}
			return file;
		}

		/**
		 * 删除SD卡上的文件
		 *
		 * @param fileName
		 */
		public boolean deleteSDFile(String fileName) {
			File file = new File(SDPATH + "//" + fileName);
			if (file == null || !file.exists() || file.isDirectory())
				return false;
			return file.delete();
		}

		/**
		 * 写入内容到SD卡中的txt文本中 str为内容
		 */
		public void writeSDFile(String fileName,String dramaUrl) {
			try {
				FileWriter fw = new FileWriter(SDPATH + "//" + fileName);
				File f = new File(SDPATH + "//" + fileName);
				InputStream is = null;
				OutputStream os = null;
				URL url = null;
				url = new URL(dramaUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				//文件输入流
				is = conn.getInputStream();
				//输出流
				os = new FileOutputStream(f);
				System.out.println("hehehe");
				System.out.println("输入流"+is);
				System.out.println("输出流"+os);
				byte buffer[] = new byte[1024];
				int len =0 ;
				while( (len = is.read(buffer))!= -1){
					os.write(buffer,0,len);
				}
				os.flush();
				System.out.println(fw);
			} catch (Exception e) {
			}
		}

		/**
		 * 读取SD卡中文本文件
		 *
		 * @param fileName
		 * @return
		 */
		public String readSDFile(String fileName) {
			StringBuffer sb = new StringBuffer();
			File file = new File(SDPATH + "//" + fileName);
			try {
				FileInputStream fis = new FileInputStream(file);
				int c;
				while ((c = fis.read()) != -1) {
					sb.append((char) c);
				}
				fis.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return sb.toString();
		}

		public String getFILESPATH() {
			return FILESPATH;
		}

		public String getSDPATH() {
			return SDPATH;
		}

		public boolean hasSD() {
			return hasSD;
		}
}
