package com.controllma.ui.main

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.controllma.data.model.NewResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ItemNew(noticeResponse: NewResponse, itemSelected: (NewResponse) -> Unit) {
    Card(
        onClick = { },
        border = BorderStroke(0.1.dp, color = Color.Gray),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                //itemSelected(noticeResponse)
                Log.e("rv", "super click en  el item ${noticeResponse.title}")
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 12.dp)
        ) {
            /*Image(
                painter = painterResource(id = noticeResponse.urlImage),
                contentDescription = "avatar",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )*/
            Text(
                text = noticeResponse.title.toString(),
                modifier = Modifier
            )
            Text(
                text = noticeResponse.description.toString(),
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier
            )
            Text(
                text = convertTime(noticeResponse.timestamp),
                fontSize = 10.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp)
            )
        }
    }
}

fun convertTime(timestamp: Long?): String {
    val date = Date(timestamp ?: 0)
    val format = SimpleDateFormat("dd/MMM/yyyy HH:mm:ss", Locale.getDefault())
    return format.format(date)
}