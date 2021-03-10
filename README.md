
#  前言

>最近的项目中使用了MQTT来接收后端推送过来的一些数据，这篇文章来介绍下Android端如何集成使用，关于MQTT相关介绍将不再阐述。
>
>由于光写代码不实践的接收下数据很难验证我们写的是否正确，所以我将简单介绍下如何配置个MQTT服务端，并使用工具来发送数据到服务端，使得手机端能接收到数据。话不多说直接看。

# 1. MQTT服务器配置

## 1.1 下载EMQX

[下载地址](https://www.emqx.cn/products/broker)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210310103122774.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L015Zml0dGluZ2xpZmU=,size_16,color_FFFFFF,t_70#pic_center)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210310103145686.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L015Zml0dGluZ2xpZmU=,size_16,color_FFFFFF,t_70#pic_center)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210310103810599.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L015Zml0dGluZ2xpZmU=,size_16,color_FFFFFF,t_70#pic_center)


## 1.2 启动EMQX

在解压后的bin目录下打开cmd命令，输入`emqx.cmd start`即可启动。

如果你在启动时遇到could't load module...，那就是因为你的路径中包含中文名导致启动不了，将该文件夹放到纯英文目录下即可启动。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210310103250408.png#pic_center)

完事后在浏览器内输入`http://127.0.0.1:18083`即可打开web管理界面，帐号为admin,密码为public

按如图方式将语言改为中文
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210310103309294.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L015Zml0dGluZ2xpZmU=,size_16,color_FFFFFF,t_70#pic_center)


## 1.3 界面说明

左侧的Clients标签下可以看到当前连接的客户端
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210310103349874.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L015Zml0dGluZ2xpZmU=,size_16,color_FFFFFF,t_70#pic_center)
左侧的Topics标签下可以看到当前订阅的主题
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210310103412479.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L015Zml0dGluZ2xpZmU=,size_16,color_FFFFFF,t_70#pic_center)


## 1.4 个人理解

到这服务端就算是配置完成了，你可能会问，服务端就是这，那我手机客户端怎么接收消息呢，服务端从哪里发送消息呢？其实EMQX服务是消息中间件服务，有点像是转发。一个客户端发送消息并指定主题，该消息发送到服务端，那么连接了服务端并且订阅了该主题的所有客户端就都能接收到该消息，所以我们手机客户端想要接收到消息，还需要有一端来给EMQX服务端来发送消息才行。



# 2. MQTT客户端软件 Paho

## 2.1 下载MQTT客户端软件

[下载地址](https://repo.eclipse.org/content/repositories/paho-releases/org/eclipse/paho/org.eclipse.paho.ui.app/1.1.1/)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210310103435763.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L015Zml0dGluZ2xpZmU=,size_16,color_FFFFFF,t_70#pic_center)



下载勾选中的那个文件即可，下载完后解压得到paho.exe，即我们需要的客户端软件。

## 2.2 MQTT客户端使用

###  2.2.1 连接服务器
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210310103501165.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L015Zml0dGluZ2xpZmU=,size_16,color_FFFFFF,t_70#pic_center)


按如图所示步骤进行点击，1、新增一个连接，2、填写服务器地址和客户标识，这里的标识为自己定义的，服务器地址可在该[地址](http://127.0.0.1:18083/#/listeners)那查看，可以看到是本地地址，端口号是1883或者11883 点击连接后可以看到连接状态变为已连接，就代表我们客户端已经连接到了EMQX。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210310103523868.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L015Zml0dGluZ2xpZmU=,size_16,color_FFFFFF,t_70#pic_center)



### 2.2.2 发送消息

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210310103541685.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L015Zml0dGluZ2xpZmU=,size_16,color_FFFFFF,t_70#pic_center)


在1处填写主题名，2处填写消息然后3处点击发布，然后可以看到4处显示已发布，代表我们已经发送到服务端了。

### 2.2.3 订阅主题

订阅我们刚才发送消息的那个主题

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210310103605599.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L015Zml0dGluZ2xpZmU=,size_16,color_FFFFFF,t_70#pic_center)


点击1处来新增订阅，点击2处输入我们要订阅的主题，这里我们设置为刚才发布消息的那个主题，然后点击3处的订阅，可以看到历史记录那里显示已订阅。

接下来我们再发送一次该主题消息，观察历史记录

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210310103623184.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L015Zml0dGluZ2xpZmU=,size_16,color_FFFFFF,t_70#pic_center)


可以看到，当我们发布后，由于我们订阅了该主题，所以就接收到了该主题消息。

在MQTT服务端配置完成以及MQTT客户端软件测试可行后，现在来看我们的安卓端如何订阅并接收消息。



# 3. Andoird端集成使用

## 3.1 添加依赖、权限等配置

```kotlin
//MQTT
implementation 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.1.0'
implementation 'org.eclipse.paho:org.eclipse.paho.android.service:1.1.1'
implementation 'androidx.localbroadcastmanager:localbroadcastmanager:1.0.0'
```

**AndroidManifest文件配置**

```kotlin
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.myfittinglife.mqttdemo">

<!--必要的三个权限-->
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.WAKE_LOCK"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

<application
	...>
	...
	<!--添加该Service-->
	<service android:name="org.eclipse.paho.android.service.MqttService"/>
</application>
```

## 3.2 使用

### 3.2.1 创建MqttAndroidClient对象

```kotlin
var mClient: MqttAndroidClient? = null

private fun createClient() {

    //1、创建接口回调
    //以下回调都在非主线程中
    val mqttCallback: MqttCallbackExtended = object : MqttCallbackExtended {
        override fun connectComplete(reconnect: Boolean, serverURI: String) {
            //连接成功
            Log.i(TAG, "connectComplete: ")
            showToast("连接成功")
        }

        override fun connectionLost(cause: Throwable) {
            //断开连接
            Log.i(TAG, "connectionLost: ")
            showToast("断开连接")

        }

        @Throws(Exception::class)
        override fun messageArrived(topic: String, message: MqttMessage) {
            //得到的消息
            var msg = message.payload
            var str = String(msg)
            Log.i(TAG, "messageArrived: $str")
            showToast("接收到的消息为：$str")

        }

        override fun deliveryComplete(token: IMqttDeliveryToken) {
            //发送消息成功后的回调
            Log.i(TAG, "deliveryComplete: ")
            showToast("发送成功")

        }
    }

    //2、创建Client对象
    try {
        mClient = MqttAndroidClient(this, "tcp://192.168.14.57:1883", "客户端名称，可随意")
        mClient?.setCallback(mqttCallback) //设置回调函数
    } catch (e: MqttException) {
        Log.e(TAG, "createClient: ", e)
    }
}
```

### 3.2.2 设置MQTT连接的配置信息

```kotlin
val mOptions = MqttConnectOptions()
mOptions.isAutomaticReconnect = false //断开后，是否自动连接
mOptions.isCleanSession = true //是否清空客户端的连接记录。若为true，则断开后，broker将自动清除该客户端连接信息
mOptions.connectionTimeout = 60 //设置超时时间，单位为秒
//mOptions.userName = "Admin" //设置用户名。跟Client ID不同。用户名可以看做权限等级
//mOptions.setPassword("Admin") //设置登录密码
mOptions.keepAliveInterval = 60 //心跳时间，单位为秒。即多长时间确认一次Client端是否在线
mOptions.maxInflight = 10 //允许同时发送几条消息（未收到broker确认信息）
mOptions.mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1_1 //选择MQTT版本
```

### 3.2.3 建立连接

```kotlin
try {
    mClient?.connect(mOptions, this, object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Log.i(TAG, "onSuccess:连接成功 ")
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Log.i(TAG, "onFailure: " + exception?.message)
        }

    })
} catch (e: MqttException) {
    Log.e(TAG, "onCreate: ", e)
}
```

### 3.2.4 订阅主题

```kotlin
//设置监听的topic
try {
    mClient?.subscribe("topicName", 0)
} catch (e: MqttException) {
    Log.e(TAG, "onCreate: ", e)
}
```

### 3.2.5 发送消息

```kotlin
try {
    var str = "要发送的消息"
    var msg = MqttMessage()
    msg.payload =str.toByteArray()
    mClient?.publish(Const.Subscribe.mTopic,msg)
} catch (e: MqttException) {
    Log.e(TAG, "onCreate: ",e )
}
```

## 3.3 最终效果

在我们的Paho MQTT Utility软件发送消息后，我们的手机端由于订阅了该主题，所以就可以接收到该消息。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210310103649549.gif#pic_center)

# 4. 注意事项

* 别忘记在manifest中添加service，否则在`connect()`的时候会报mClient为空。

  ```xml
  <service android:name="org.eclipse.paho.android.service.MqttService"/>
  ```

* 别忘记添加localbroadcastmanager依赖，否则会报`Failed resolution of: Landroidx/localbroadcastmanager/content/LocalBroadcastManager`错误。

  ```kotlin
  implementation 'androidx.localbroadcastmanager:localbroadcastmanager:1.0.0'
  ```

* 启动emqx服务时，一定要将该文件目录放到纯英文的目录下，不能包含中文，否则会出现`could't load module`的错误。



# 5. 总结

按以上步骤即可完成最基本的功能，以上只是简单的使用，其实还可以设置用户登录名和密码、设置服务质量、重连的操作等。关于MQTT的相关内容可以看这篇文章[MQTT](https://my.oschina.net/u/3553496/blog/4253897)。



[项目Github地址](https://github.com/myfittinglife/MQTTDemo)

 如果本文对你有帮助，请别忘记三连，如果有不恰当的地方也请提出来，下篇文章见。 

# 6. 参考文章

[在线API文档](https://www.eclipse.org/paho/files/android-javadoc/index.html)

[免费的MQTT测试服务器](https://jmqtt.io/)，实测发现用不了

[paho网站](https://www.eclipse.org/paho/index.php?page=clients/android/index.php)

[阿里使用文档](https://www.alibabacloud.com/help/zh/doc-detail/146630.htm)



