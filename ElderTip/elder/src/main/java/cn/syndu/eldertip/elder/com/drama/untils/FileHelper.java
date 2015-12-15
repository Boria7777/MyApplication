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
	private boolean hasSD = false;
	private String SDPATH;
	private String FILESPATH;

	public FileHelper(Context context) {
		this.context = context;
		hasSD = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		SDPATH = Environment.getExternalStorageDirectory().getPath();
		FILESPATH = this.context.getFilesDir().getPath();
	}

	/**
	 *
	 * 
	 * @throws IOException
	 */
	public File createSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + "//" + fileName);
		if (!file.exists()) {
			file.createNewFile();
		}else{
			System.out.println("�ļ��Ѵ���");
		}
		return file;
	}

	/**
	 *
	 * 
	 * @param fileName
	 */
	public boolean deleteSDFile(String fileName) {
		File file = new File(SDPATH + "//" + fileName);
		if (file == null || !file.exists() || file.isDirectory())
			return false;
		return file.delete();
	}


	public void writeSDFile(String str, String fileName) {
		try {
			FileWriter fw = new FileWriter(SDPATH + "//" + fileName);
			File f = new File(SDPATH + "//" + fileName);
			InputStream is = null;
			OutputStream os = null;
			URL url = null;
			url = new URL("http://192.168.1.100:8080/NoteDemo/video/123123.3gp");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			is = conn.getInputStream();
			os = new FileOutputStream(f);
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
	 *
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
