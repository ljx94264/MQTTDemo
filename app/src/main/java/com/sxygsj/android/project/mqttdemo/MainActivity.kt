package com.sxygsj.android.project.mqttdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

/**
 @Author LD
 @Time 2021/3/9 20:56
 @Describe MQTT的基本使用
 @Modify
*/

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ceshi"
    }

    var mClient: MqttAndroidClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /**
         * 创建Client对象
         */
        btnCreate.setOnClickListener {
            createClient()
        }

        /**
         * 建立连接
         */
        btnConnect.setOnClickListener {
            val mOptions = initOption()

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
        }

        /**
         * 订阅主题
         */
        btnSubscribe.setOnClickListener {
            //设置监听的topic
            try {
                mClient?.subscribe(Const.Subscribe.mTopic, 0)
            } catch (e: MqttException) {
                Log.e(TAG, "onCreate: ", e)
            }
        }
        /**
         * 发送消息
         */
        btnSendMsg.setOnClickListener {
            try {
                var str = "要发送的消息"
                var msg = MqttMessage()
                msg.payload =str.toByteArray()
                mClient?.publish(Const.Subscribe.mTopic,msg)
            } catch (e: MqttException) {
                Log.e(TAG, "onCreate: ",e )
            }
        }
    }

    /**
     * 创建MQTTAndroidClient对象
     */
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
            mClient = MqttAndroidClient(this, Const.Url.SERVER_URL, "客户端名称，可随意")
            mClient?.setCallback(mqttCallback) //设置回调函数
        } catch (e: MqttException) {
            Log.e(TAG, "createClient: ", e)
        }
    }

    private fun initOption(): MqttConnectOptions {
        val mOptions = MqttConnectOptions()
        mOptions.isAutomaticReconnect = false //断开后，是否自动连接
        mOptions.isCleanSession = true //是否清空客户端的连接记录。若为true，则断开后，broker将自动清除该客户端连接信息
        mOptions.connectionTimeout = 60 //设置超时时间，单位为秒
        //mOptions.userName = "Admin" //设置用户名。跟Client ID不同。用户名可以看做权限等级
        //mOptions.setPassword("Admin") //设置登录密码
        mOptions.keepAliveInterval = 60 //心跳时间，单位为秒。即多长时间确认一次Client端是否在线
        mOptions.maxInflight = 10 //允许同时发送几条消息（未收到broker确认信息）
        mOptions.mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1_1 //选择MQTT版本
        return mOptions
    }

    fun showToast(msg:String){
        runOnUiThread {
            Toast.makeText(this@MainActivity,msg,Toast.LENGTH_SHORT).show()
        }
    }

}