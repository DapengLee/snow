package com.snow.down;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


import java.text.DecimalFormat;

public class DownloadDbHandler extends Handler {
	private ProgressDialog pd;
	private Context context;

	public DownloadDbHandler(Context context) {
		this.context = context;
		pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setTitle("下载题库");
		pd.setCancelable(false);
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		//Out.println("DownloadApkHandler.what:" + msg.what);
		super.handleMessage(msg);
		switch (msg.what) {
		case 110:// 强行下载开始
			pd.show();
			break;
		case 120:// 设置最大值
			int max = (Integer) msg.obj;
			pd.setMax(max);
			float m = (float) (max / (1024.0 * 1024.0));
			// 构造方法的字符格式这里如果小数不足2位,会以0补足.
			DecimalFormat decimalFormat = new DecimalFormat(".00");
			String p = decimalFormat.format(m);// format 返回的是字符串
			pd.setTitle("下载题库(" + p + "M)");
			break;
		case 130:// 更新进度
			int progress = (Integer) msg.obj;
			pd.setProgress(progress);
			break;
		case 140:// 下载完成
			if (pd != null) {
				pd.dismiss();
			}
			break;
		case 150:// 下载出错
			if (pd != null) {
				pd.dismiss();
			}
			Toast.makeText(context, "下载失败，请重新尝试。", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
