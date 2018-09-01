package com.example.semoto.calculationtrainning

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*
import kotlin.concurrent.schedule

class TestActivity : AppCompatActivity(), View.OnClickListener {

    //問題数
    var numberOfQuestion:Int = 0
    //残り問題数
    var numberOfRemaining: Int = 0
    //正解数
    var numberOfCorrrect : Int = 0
    //効果音
    lateinit var soundPool: SoundPool
    //サウンドID
    var soundID_Correct: Int = 0
    var soundID_Incorrect: Int = 0

    //タイマー
    lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        // 画面が開いたら
        // 1.前の画面から渡された問題数を表示する
        val bundle = intent.extras
        numberOfQuestion = bundle.getInt("numberOfQuestion")
        textViewRemaining.text = numberOfQuestion.toString()
        numberOfRemaining = numberOfQuestion
        numberOfCorrrect = 0

        // 「答え合わせ」ボタンが押されたら
        buttonAnswerCheck.setOnClickListener {

            if (textViewAnswer.text.toString() != "" && textViewAnswer.text.toString() != "-") {
                answerCheck()
            }
        }

        // 「戻る」ボタンが押されたら
        buttonBack.setOnClickListener {

//            //Intentを使うとTestActivityが残ったまま、新しい画面MainActivityが生成される
//            val intent = Intent(this@TestActivity, MainActivity::class.java)
//            startActivity(intent)
            finish()
        }


        button0.setOnClickListener(this)
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        button0.setOnClickListener(this)
        buttonMinus.setOnClickListener(this)
        buttonClear.setOnClickListener(this)

        question()
    }

    override fun onResume() {
        super.onResume()

        //soundPoolの準備
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder().setAudioAttributes(AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA).build())
                    .setMaxStreams(1)
                    .build()
        } else {
            SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        }

        //効果音ファイルをメモリロード
        soundID_Correct = soundPool.load(this, R.raw.sound_correct, 1)
        soundID_Incorrect = soundPool.load(this, R.raw.sound_incorrect, 1)

        //タイマー準備
        timer = Timer()
    }

    override fun onPause() {
        super.onPause()

        //効果音をメモリから削除
        soundPool.release()

        //タイマーをキャンセルする
        timer.cancel()
    }

    // 問題を出すメソッド
    // 問題が出されたら（questionメソッド）
    private fun question() {
        // 1.「戻る」ボタンを使えなくする
        buttonBack.isEnabled = false
        // 2.「答え合わせ」ボタンと電卓を使えるようにする
        buttonAnswerCheck.isEnabled = true
        button0.isEnabled = true
        button1.isEnabled = true
        button2.isEnabled = true
        button3.isEnabled = true
        button4.isEnabled = true
        button5.isEnabled = true
        button6.isEnabled = true
        button7.isEnabled = true
        button8.isEnabled = true
        button9.isEnabled = true
        buttonMinus.isEnabled = true
        buttonClear.isEnabled = true

        // 3.問題の２つの数字を1〜100からランダムに設定して表示
        val random = Random()
        val intQuestionLeft = random.nextInt(100) + 1
        val intQuestionRight = random.nextInt(100) + 1
        textViewLeft.text = intQuestionLeft.toString()
        textViewRight.text = intQuestionRight.toString()

        // 4.計算方法を「＋」「ー」からランダムに設定して表示
        when (random.nextInt(2) + 1) {
            1 -> textViewOperator.text = "+"
            2 -> textViewOperator.text = "-"
        }

        // 5.前の問題で入力した答えを消す
        textViewAnswer.text = ""
        // 6.◯×画像を見えないようにする
        imageView.visibility = View.INVISIBLE
    }

    // 答え合わせをするメソッド
    // 答え合わせ処理（answerCheckメソッド）
    private fun answerCheck() {
        // 1.「戻る」「答え合わせ」「電卓」ボタンを押せなくする
        buttonBack.isEnabled = false
        buttonAnswerCheck.isEnabled = false
        button0.isEnabled = false
        button1.isEnabled = false
        button2.isEnabled = false
        button3.isEnabled = false
        button4.isEnabled = false
        button5.isEnabled = false
        button6.isEnabled = false
        button7.isEnabled = false
        button8.isEnabled = false
        button9.isEnabled = false
        buttonMinus.isEnabled = false
        buttonClear.isEnabled = false

        // 2.残りの問題数を１つ減らす
        numberOfRemaining --
        textViewRemaining.text = numberOfRemaining.toString()
        // 3.◯×画像を表示する
        imageView.visibility = View.VISIBLE
        // 4.入力した答えと正解を比較する
        //自分の答え
        val intMyAnswer:Int = textViewAnswer.text.toString().toInt()
        //正解
        val intRealAnswer:Int =
            if (textViewOperator.text.toString() == "+") {
                textViewLeft.text.toString().toInt() + textViewRight.text.toString().toInt()
            } else {
                textViewLeft.text.toString().toInt() - textViewRight.text.toString().toInt()
            }
        //比較
        if (intMyAnswer == intRealAnswer) {
            // 5.合ってる場合→　正解数を１つ増やす・◯画像・ピンポン音を鳴らす
            numberOfCorrrect ++
            textViewCorrect.text = numberOfCorrrect.toString()

            imageView.setImageResource(R.drawable.pic_correct)

            soundPool.play(soundID_Correct, 1.0f, 1.0f, 0, 0, 1.0f)
        } else {
            // 6.間違っている場合→　x画像・ぶー音を鳴らす
            imageView.setImageResource(R.drawable.pic_incorrect)

            soundPool.play(soundID_Incorrect, 1.0f, 1.0f, 0, 0, 1.0f)
        }

        // 7.正答率を計算して表示する（正解数 / 出題済みの問題数 x 100）
        val intPoint = (numberOfCorrrect.toDouble() / (numberOfQuestion - numberOfRemaining).toDouble() * 100).toInt()
        textViewPoint.text = intPoint.toString()

        if (numberOfRemaining == 0) {
            // 8.残りの問題がなくなったら、→　戻るボタン◯・答え合わせボタンx・テスト終了表示
            buttonBack.isEnabled = true
            buttonAnswerCheck.isEnabled = false
            textViewMessage.text = "テスト終了"
        } else {
            // 9.残りの問題がある場合→　1秒後に次の問題を出す（questionメソッド）
            timer.schedule(1000, { runOnUiThread { question() } })
        }
    }

    // 電卓ボタンが押されたら
    override fun onClick(p0: View?) {

        val  button: Button = p0 as Button

        when (p0?.id) {
            // クリアボタン→消す
            R.id.buttonClear
                -> textViewAnswer.text = ""
            // マイナスボタン→先頭だけ
            R.id.buttonMinus
                -> if (textViewAnswer.text.toString() == "")
                    textViewAnswer.text = "-"
            // ０ボタン
            R.id.button0
                -> if (textViewAnswer.text.toString() != "0" && textViewAnswer.text.toString() != "-")
                    textViewAnswer.append(button.text)
            // 1〜9
            else
                -> if (textViewAnswer.text.toString() == "0")
                    textViewAnswer.text = button.text
                    else textViewAnswer.append(button.text)
        }
    }
}
