package com.example.yuan.qianhouduanjiaohu;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;



public class MainActivity extends AppCompatActivity {
    private Button loginbtn,register,applybtn,lookapplybtn,uploadlocation,getlocation,sendalarmbtn,dealalarmbtn;
    private EditText et,pwd,identifypwd;
    private TextView location_x,location_y;
    private static String urlstr="http://192.168.43.173/gongcheng/public/php/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginbtn= (Button) findViewById(R.id.login);
        //register= (Button) findViewById(R.id.register);
        //applybtn= (Button) findViewById(R.id.apply);
        /*上传本机位置信息*/
        //uploadlocation= (Button) findViewById(R.id.uploadlocation);
        /*获取好友位置信息*/
        //getlocation= (Button) findViewById(R.id.getlocation);
        et= (EditText) findViewById(R.id.user);
        pwd= (EditText) findViewById(R.id.pass);
        //identifypwd= (EditText) findViewById(R.id.identifypwd);
        //lookapplybtn= (Button) findViewById(R.id.lookapply);
        //tv= (TextView) findViewById(R.id.textView);
        //location_x= (TextView) findViewById(R.id.location_x);
        //location_y= (TextView) findViewById(R.id.location_y);

        loginbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {//登陆按钮监听事件
/*                ((App)getApplicationContext()).setTextData(et.getText().toString());
                location_x.setText(((App)getApplicationContext()).getTextData());*/
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int result = login();
                            //login()为向php服务器提交请求的函数，返回数据类型为int
                            if (result == 1) {
                                Log.e("log_tag", "登陆成功！");
                                //Toast toast=null;
                                Looper.prepare();
                                Toast.makeText(MainActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            } else if (result == -2) {
                                Log.e("log_tag", "密码错误！");
                                //Toast toast=null;
                                Looper.prepare();
                                Toast.makeText(MainActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            } else if (result == -1) {
                                Log.e("log_tag", "不存在该用户！");
                                //Toast toast=null;
                                Looper.prepare();
                                Toast.makeText(MainActivity.this, "不存在该用户！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }).start();
            }
        });

    }


    /*
    *用户登录提交post请求
    * 向服务器提交数据1.user_id用户名，2.input_pwd密码
    * 返回JSON数据{"status":"1","info":"login success","sex":"0","nicename":""}
    */
    private int login() throws IOException {
        int returnResult=0;
        /*获取用户名和密码*/
        String user_id=et.getText().toString();
        String input_pwd=pwd.getText().toString();
        if(user_id==null||user_id.length()<=0){
            Looper.prepare();
            Toast.makeText(MainActivity.this,"请输入账号", Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;

        }
        if(input_pwd==null||input_pwd.length()<=0){
            Looper.prepare();
            Toast.makeText(MainActivity.this,"请输入密码", Toast.LENGTH_LONG).show();
            Looper.loop();
            return 0;
        }
        String urlstr=this.urlstr+"login.php";
        //建立网络连接
        URL url = new URL(urlstr);
        HttpURLConnection http= (HttpURLConnection) url.openConnection();
        //往网页写入POST数据，和网页POST方法类似，参数间用‘&’连接
        String params="uid="+user_id+'&'+"pwd="+input_pwd;
        http.setDoOutput(true);
        http.setRequestMethod("POST");
        OutputStream out=http.getOutputStream();
        out.write(params.getBytes());//post提交参数
        out.flush();
        out.close();

        //读取网页返回的数据
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(http.getInputStream()));//获得输入流
        String line="";
        StringBuilder sb=new StringBuilder();//建立输入缓冲区
        while (null!=(line=bufferedReader.readLine())){//结束会读入一个null值
            sb.append(line);//写缓冲区
        }
        String result= sb.toString();//返回结果

        try {
            /*获取服务器返回的数据*/
            JSONObject jsonObject= new JSONObject(result);
            returnResult=jsonObject.getInt("status");
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "the Error parsing data "+e.toString());
        }
        return returnResult;
    }

}


/*import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}*/

