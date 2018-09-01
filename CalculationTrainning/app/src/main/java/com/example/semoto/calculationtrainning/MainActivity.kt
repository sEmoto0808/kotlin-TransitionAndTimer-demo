package com.example.semoto.calculationtrainning

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 選択肢をセットするためのArrayAdapterを生成
//        val arrayAdapter = ArrayAdapter<Int>(this, android.R.layout.simple_spinner_item)
//        arrayAdapter.add(10)
//        arrayAdapter.add(20)
//        arrayAdapter.add(30)

        val arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.number_of_question,
                android.R.layout.simple_spinner_item)
        // SpinnerとAdapterを紐付ける
        spinner.adapter = arrayAdapter

        button.setOnClickListener {

            // 選択した問題数を取得する
            val numberOfQuestion = spinner.selectedItem.toString().toInt()

            // テスト画面を開く（問題数を渡す）
            val intent = Intent(this@MainActivity, TestActivity::class.java)
            intent.putExtra("numberOfQuestion", numberOfQuestion)
            startActivity(intent)
        }
    }
}
