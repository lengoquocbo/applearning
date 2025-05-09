package com.example.apphoctap.view
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import com.example.apphoctap.R


class SplashActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Nếu bạn muốn thay đổi màu của logo trong file lottie (nếu cần)
        val lottieView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)
        lottieView.addValueCallback(
            KeyPath("**"),
            LottieProperty.COLOR,
            LottieValueCallback(Color.parseColor("#2196F3"))
        )

        // Hoặc bạn có thể thay đổi tốc độ của animation
        lottieView.speed = 1.5f

        // Hiệu ứng khác cho TextView và ProgressBar
        playTextAnimation()

        // Chuyển đến MainActivity sau khi hoàn thành
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }, 3000)
    }

    private fun playTextAnimation() {
        val appName = findViewById<View>(R.id.appNameTextView)
        val progressBar = findViewById<View>(R.id.loadingProgressBar)

        // Hiệu ứng hiện dần tên ứng dụng
        val fadeInAppName = ObjectAnimator.ofFloat(appName, "alpha", 0f, 1f).apply {
            duration = 800
            startDelay = 1000
        }

        // Hiệu ứng hiện dần thanh tiến trình
        val fadeInProgressBar = ObjectAnimator.ofFloat(progressBar, "alpha", 0f, 1f).apply {
            duration = 800
            startDelay = 1500
        }

        AnimatorSet().apply {
            playSequentially(fadeInAppName, fadeInProgressBar)
            start()
        }
    }
}

