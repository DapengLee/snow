package com.snow.down;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoadDbInterface {
    public static final String ImageHost = "http://192.168.1.140:8888/db/accountant.db";
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpTool.downloadDatabase(this, ImageHost, "ks", this);


        final ClearEditText username = (ClearEditText) findViewById(R.id.username);
        final ClearEditText password = (ClearEditText) findViewById(R.id.password);
    }
        /**
         * 显示Toast消息
         * @param msg
         */
        private void showToast(String msg) {
            if (mToast == null){
                mToast = Toast.makeText(this,msg, Toast.LENGTH_SHORT);
            }else{
                mToast.setText(msg);
            }
            mToast.show();
        }


    @Override
    public void success() {

    }
}
