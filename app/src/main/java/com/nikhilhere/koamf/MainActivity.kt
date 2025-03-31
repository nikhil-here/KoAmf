package com.nikhilhere.koamf

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nikhilhere.koamf.amf.AmfVersion
import com.nikhilhere.koamf.ui.theme.KoAmfTheme

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        test()
        setContent {
            KoAmfTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                test()
                            }
                        ) {
                            Text("TEST")
                        }
                    }
                }
            }
        }
    }

    private fun test() {


        try {
            val koAmf = KoAmf(AmfVersion.AMF0)
            koAmf.encode(1)
        } catch (e: Exception) {
            Log.e(TAG, "test: ", e)
        }
    }
}