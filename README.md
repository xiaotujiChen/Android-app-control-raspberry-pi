# Smart Remote Control 
###Introduction
An Android App designed to control smart alarm clock(base on Raspberry Pi,[ learn more](https://github.com/xiaotujiChen/Smart-Alarm-Clock)). It could trigger different functions of alarm clock without touch it
###System Design
Use Android Studio to design the APP and build the connection between Android and Raspberry Pi.
![GitHub](http://coderxiaoyu.com/alarmclock/img/appsystem.jpg)
###UI Design
![GitHub](http://coderxiaoyu.com/alarmclock/img/ui.jpg)
###Communication
Use TCP/IP protocol to build communication between Android App and alarm clock. The App and clock need connect with same WiFi.      

- Alarm clock broadcast its’ IP address to all the devices in this LAN at a certain port and the App will receive and locate this IP address in server.
      
![GitHub](http://coderxiaoyu.com/alarmclock/img/getip.jpg)&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;![GitHub](http://coderxiaoyu.com/alarmclock/img/click.jpg)

- Start server on the alarm clock and click the button through android App, a session will be established to build communication between clock and Android using TCP socket.

![GitHub](http://coderxiaoyu.com/alarmclock/img/remotecontrol.jpg)

###License
The MIT License (MIT)