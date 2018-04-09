package com.snow.down;

import android.app.Activity;
import android.content.Context;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 网络相关及线程池管理
 */
public class HttpTool {

	// public static final String HOST = "http://192.168.0.101:8080/jiaxiao/";
	// public static final String ImageHost =
	// "http://192.168.0.101:8080/jiaxiao";
	//public static final String HOST = "http://123.56.135.173:8080/jiaxiao/dt/";



	public static void downloadDatabase(final Activity activity,
                                        final String dbPath, final String dbName,
                                        final LoadDbInterface loadDbInterface) {
		final DownloadDbHandler handler = new DownloadDbHandler(activity);

		Runnable run = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("download start........................");
				try {
					// String name = "ks.db";
					String name = dbName + ".db";
					String tempName = name + ".temp";
					File file = activity.getDatabasePath(name);
					File fileParent = file.getParentFile();
					if (!fileParent.exists()) {
						fileParent.mkdirs();
					}
					File tempFile = new File(file.getParentFile(), tempName);
					if (tempFile.exists()) {
						tempFile.delete();
					}
					tempFile.createNewFile();

					String path = null;
					path = dbPath;
					System.out.println("dbPath:" + path);

					// 下载开始
					handler.sendMessage(handler.obtainMessage(110));

					URL url = new URL(path);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					System.out.println("connection...........................");

						// 以下为java IO部分，大体来说就是先检查文件夹是否存在，不存在则创建,然后的文件名重复问题，没有考虑
					InputStream is = connection.getInputStream();
					OutputStream os = new FileOutputStream(tempFile);
					byte[] buffer = new byte[1024];
					int index = 0;
					int sum = 0;
					int max = connection.getContentLength();

					if (handler != null) {
						handler.sendMessage(handler.obtainMessage(120, max));
					}

					long a, b;
					a = System.currentTimeMillis();

					while ((index = is.read(buffer)) != -1) {
						sum += index;
						System.out.println("sum:" + sum);
						os.write(buffer, 0, index);

						b = System.currentTimeMillis();
						if (b - a > 200 || sum == max) {
							a = b;
							if (handler != null) {
								handler.sendMessage(handler.obtainMessage(130,
										sum));
							}
						}
					}

					tempFile.renameTo(file);
					System.out.println("下完了  sum:" + sum);

					os.close();
					is.close();

					if (handler != null) {
						handler.sendMessage(handler.obtainMessage(140));
					}

					loadDbInterface.success();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if (handler != null) {
						handler.sendMessage(handler.obtainMessage(150));
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if (handler != null) {
						handler.sendMessage(handler.obtainMessage(150));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if (handler != null) {
						handler.sendMessage(handler.obtainMessage(150));
					}
				}
			}

		};

		execInPool(run);
	}
	/**
	 * 在线程池中运行任务
	 *
	 * @param runnable
	 *            待运行的任务
	 */
	public static void execInPool(Runnable runnable) {
		executor.execute(runnable);
	}

	private static final ThreadPoolExecutor executor;

	static {
		// Out.println("系统版本号:" + Build.VERSION.SDK_INT);
		executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	}

}
