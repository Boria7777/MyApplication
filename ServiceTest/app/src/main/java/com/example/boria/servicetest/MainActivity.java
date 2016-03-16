package com.example.boria.servicetest;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private Button start;
    private Button stop;
    private Intent intent = null;

    private Button btnbind;
    private Button btncancel;
    private Button btnstatus;


    TestService2.MyBinder binder;
    private ServiceConnection conn = new ServiceConnection() {

        //Activity与Service断开连接时回调该方法
        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("------Service DisConnected-------");
        }

        //Activity与Service连接成功时回调该方法
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("------Service Connected-------");
            binder = (TestService2.MyBinder) service;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        StartSer2();
        SetClick2();
    }


    public void init() {
        setContentView(R.layout.activity_main);
        start = (Button) findViewById(R.id.btnstart);
        stop = (Button) findViewById(R.id.btnstop);
        btnbind = (Button) findViewById(R.id.btnbind);
        btncancel = (Button) findViewById(R.id.btncancel);
        btnstatus = (Button) findViewById(R.id.btnstatus);
    }

    public void StartServ() {
        intent = new Intent();
        intent.setAction("com.example.boria.servicetest.TEST_SERVICE1");
    }

    public void StartSer2() {
        intent = new Intent();
        intent.setAction("com.example.boria.servicetest.TEST_SERVICE2");
    }

    private void SetClick() {
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
    }

    private void SetClick2() {
        btnbind.setOnClickListener(this);
        btncancel.setOnClickListener(this);
        btnstatus.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnstart) {
            startService(intent);
        }
        if (id == R.id.btnstop) {
            stopService(intent);
        }
        if (id == R.id.btnbind) {
            bindService(intent, conn, Service.BIND_AUTO_CREATE);
        }
        if (id == R.id.btncancel) {
            unbindService(conn);
        }
        if (id == R.id.btnstatus) {
            Toast.makeText(getApplicationContext(), "Service的count的值为:"
                    + binder.getCount(), Toast.LENGTH_SHORT).show();
        }
    }
}
