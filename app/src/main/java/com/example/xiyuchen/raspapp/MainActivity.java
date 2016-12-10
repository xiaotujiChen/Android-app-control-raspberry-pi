package com.example.xiyuchen.raspapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import android.util.Log;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;



public class MainActivity extends Activity {
    private Socket socket;
    private static final int SERVERPORT = 8888;
    private static String serve_ip = "";
    private static ObjectOutputStream msgOutput;
    private static ObjectInputStream msgInput;
    private static BufferedReader input;
    private static PrintWriter output;
    final String tag = "xiyuchen.raspapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton button_time = (ImageButton)findViewById(R.id.button_time);
        button_time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Time Button Clicked", Toast.LENGTH_SHORT).show();
                new Thread(new ClientThread("time")).start();
            }
        });

        ImageButton button_weather = (ImageButton)findViewById(R.id.button_weather);
        button_weather.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Weather Button Clicked", Toast.LENGTH_SHORT).show();
                new Thread(new ClientThread("weather")).start();
            }
        });

        ImageButton button_alarm = (ImageButton)findViewById(R.id.button_alarm);
        button_alarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Alarm Button Clicked", Toast.LENGTH_SHORT).show();
                new Thread(new ClientThread("alarm")).start();
                //sendSocketData("0003");
            }
        });

        ImageButton button_music = (ImageButton)findViewById(R.id.button_music);
        button_music.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Music Button Clicked", Toast.LENGTH_SHORT).show();
                new Thread(new ClientThread("music")).start();
                //sendSocketData("0004");
            }
        });

        ImageButton button_wifi = (ImageButton)findViewById(R.id.button_calendar);
        button_wifi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Calendar Button Clicked", Toast.LENGTH_SHORT).show();
                new Thread(new ClientThread("calendar")).start();
                //sendSocketData("0005");
            }
        });

        ImageButton button_camera = (ImageButton)findViewById(R.id.button_gesture);
        button_camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Gesture Button Clicked", Toast.LENGTH_SHORT).show();
                new Thread(new ClientThread("gesture")).start();
                //sendSocketData("0006");
            }
        });

        final Handler myhandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage (Message msg) {
                super.handleMessage(msg);
                String a = (String)msg.obj;
                if (!serve_ip.equals(a)) {
                //if (false) {
                    serve_ip = a;
                    Toast.makeText(getApplicationContext(), "get IP address of Raspberry Pi", Toast.LENGTH_SHORT).show();
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket socket = new DatagramSocket(8888);
                    socket.setBroadcast(true);
                    //System.out.println("Listen on " + socket.getLocalAddress() + " from " + socket.getInetAddress() + " port " + socket.getBroadcast());
                    byte[] buf = new byte[512];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    System.out.println("server is on，waiting for client to send data......");
                    while (true) {
                        Log.i(tag,"Waiting for data");

                        //System.out.println("Waiting for data");
                        socket.receive(packet);
                        //System.out.println("server received data from client：");
                        Log.i(tag,"server received data from client：");
                        String str_receive = new String(packet.getData(),0,packet.getLength()) +
                                " from " + packet.getAddress().getHostAddress() + ":" + packet.getPort();
                        Log.i(tag,str_receive);
                        Log.i(tag,"Data received");
                        //System.out.println(str_receive);
                        //System.out.println("Data received");

                        Message e = myhandler.obtainMessage(0, packet.getAddress().getHostAddress());
                        e.sendToTarget();
                    }
                }catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
  }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    class ClientThread implements Runnable {
        private String msg;
        ClientThread(String msg) {
            this.msg = msg;
        }
        public void run() {
            try {
                InetAddress serverAddress = InetAddress.getByName(serve_ip);
                socket = new Socket(serverAddress, SERVERPORT);
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                output.println(msg);
                System.out.println(input.readLine());
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}
