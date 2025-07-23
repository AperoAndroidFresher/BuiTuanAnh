package com.example.buituananh.lesson7_state_management

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.buituananh.R

@Composable
fun Tesst(modifier: Modifier = Modifier) {

    Column {
        ImageExtension()
    }

}
@Composable
fun ColumnScope.ImageExtension() {
    Image(
        painter = painterResource(R.drawable.avatar),
        contentDescription = null,
        modifier = Modifier.align(Alignment.CenterHorizontally)
    )
}

