package ru.suleymanovtat.graphics.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.suleymanovtat.graphics.R
import ru.suleymanovtat.graphics.presentation.graphics.GraphicsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, GraphicsFragment.newInstance()).commit()
        }
    }
}
