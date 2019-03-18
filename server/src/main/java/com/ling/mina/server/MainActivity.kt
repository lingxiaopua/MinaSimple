package com.ling.mina.server

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.Button
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var mServer: MinaServer
    private lateinit var chatAdapter: ChatAdapter
    var chatList = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_server.setOnClickListener { v ->
            bt_server.isEnabled = false
            Thread {
                mServer = MinaServer()
                var ret = mServer.connect(2333)
                bt_server.post {
                    bt_server.isEnabled = true
                    if(ret){
                        bt_server.text = "开启成功"
                    }else{
                        bt_server.text = "开启失败"
                    }
                }

            }.start()
        }
        bt_send.setOnClickListener{
            var message = editText.text.trim().toString()
            if (message.isEmpty()){
                Toast.makeText(applicationContext,"不能发送空消息",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mServer.let {
                it.sendText(message)
            }
            chatList.add(message)
            chatAdapter.notifyDataSetChanged()
        }
        var manager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true)
        chatAdapter = ChatAdapter()
        recyclerView.layoutManager = manager
        recyclerView.adapter = chatAdapter

    }

    inner class ChatAdapter : RecyclerView.Adapter<BaseHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat, null)
            var baseHolder = BaseHolder(view)
            return baseHolder
        }

        override fun getItemCount(): Int {
            return chatList.size
        }

        override fun onBindViewHolder(holder: BaseHolder, position: Int) {
            holder.setText(R.id.tv_chat,chatList.get(position))
        }
    }
}
