package com.example.buituananh.lesson6_jetpackcompose_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buituananh.R


@Preview(showBackground = false, backgroundColor = 0xFF000000)
@Composable
fun Practice2(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(90.dp)
            .padding(8.dp)
            .padding(8.dp)
    ) {
        Box {
            Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center)
            )
            Box(
                modifier = Modifier
                    .background(Color.Green, shape = CircleShape)
                    .size(20.dp)
                    .align(Alignment.BottomEnd)
                    .border(
                        width = 1.5.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Nguyen Thuc Thuy Tien",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "Active",
                fontSize = 18.sp,
                modifier = Modifier.alpha(0.5f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Practice1(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomButton(
            content = "Composable_1"
        )
        CustomButton(
            content = "Composable_2",
            backgroundColor = Color.Blue
        )
        CustomButton(
            content = "Composable_3",
            backgroundColor = Color(0xFF0041c2)
        )

    }
}

@Composable
private fun CustomButton(
    modifier: Modifier = Modifier,
    content: String = "",
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        contentColor = contentColor,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(48.dp)
    ) {
        Text(
            content,
            modifier = Modifier.padding(horizontal = 130.dp, vertical = 15.dp)
        )
    }
}

