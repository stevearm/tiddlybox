/*
 * Pulled from http://www.jguru.com/faq/view.jsp?EID=735674
 *
 * Created on 21 janvier 2002, 12:14 by guisset
 */
package com.horsefire.tiddly.tiddlybox;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class TestMultipartPost {

	private static void writeParam(String name, String value,
			DataOutputStream out, String boundary) {
		try {
			out.writeBytes("content-disposition: form-data; name=\"" + name
					+ "\"\r\n\r\n");
			out.writeBytes(value);
			out.writeBytes("\r\n" + "--" + boundary + "\r\n");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private static void writeFile(String name, String filePath,
			DataOutputStream out, String boundary) {
		try {
			out.writeBytes("content-disposition: form-data; name=\"" + name
					+ "\"; filename=\"" + filePath + "\"\r\n");
			out.writeBytes("content-type: application/octet-stream"
					+ "\r\n\r\n");
			FileInputStream fis = new FileInputStream(filePath);
			while (true) {
				synchronized (buffer) {
					int amountRead = fis.read(buffer);
					if (amountRead == -1) {
						break;
					}
					out.write(buffer, 0, amountRead);
				}
			}
			fis.close();
			out.writeBytes("\r\n" + "--" + boundary + "\r\n");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	static final int BUFF_SIZE = 1024;
	static final byte[] buffer = new byte[BUFF_SIZE];

	/** Creates a new instance of EssaiPostURLConnection */
	public TestMultipartPost() {
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		try {
			URL servlet = new URL("http://127.0.0.1/pu");
			URLConnection conn = servlet.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			String boundary = "---------------------------7d226f700d0";
			conn.setRequestProperty("Content-type",
					"multipart/form-data; boundary=" + boundary);
			conn.setRequestProperty("Referer", "http://127.0.0.1/index.jsp");
			conn.setRequestProperty("Cache-Control", "no-cache");

			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.writeBytes("--" + boundary + "\r\n");
			writeParam("nom", "julien", out, boundary);
			writeParam("poste", "stagiaire", out, boundary);
			writeFile("superFichier", "C:\\tmpfoo.txt", out, boundary);
			out.flush();
			out.close();

			InputStream stream = conn.getInputStream();
			BufferedInputStream in = new BufferedInputStream(stream);
			int i = 0;
			while ((i = in.read()) != -1) {
				System.out.write(i);
			}
			in.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}