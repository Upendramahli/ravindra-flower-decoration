package com.example.ui.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DecorationViewModel
import com.example.ui.theme.MarigoldOrange
import com.example.ui.theme.RoseDarkRed
import com.example.ui.theme.WhatsAppGreen

@Composable
fun InstallAppDialog(
    viewModel: DecorationViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sharedAppUrl = "https://ais-pre-6ob35dfl6dtp6jzk5emtwb-970149925789.asia-southeast1.run.app"

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.PhoneAndroid,
                contentDescription = "Add to Home Screen",
                tint = RoseDarkRed,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = "📲 होम स्क्रीन पर ऐप की तरह सेव करें",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 17.sp,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "वेबसाइट को बार-बार सर्च करने की जरूरत नहीं पड़ेगी! इसे बिना Play Store के सीधे अपने मोबाइल में ऐप की तरह सेव करें:",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                // PWA Add to Home Screen Guidance Card
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "⚡ ब्राउज़र (Chrome / Safari) में आसान तरीका:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoseDarkRed
                        )
                        Text(
                            text = "1. Chrome ब्राउज़र में ऊपर दिए 3-डॉट्स (⋮) पर क्लिक करें।\n2. 'Add to Home Screen' या 'होम स्क्रीन पर जोड़ें' चुनें।\n3. आपके मोबाइल में 'Ravindra Flower Decor' का ऐप आइकॉन बन जाएगा।\n4. अब हर बार 1-क्लिक में आपकी कैटलॉग वेबसाइट खुल जाएगी!",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // WhatsApp Share Link Button
                Button(
                    onClick = {
                        val shareMessage = "🌸 *रविंद्र फ्लावर डेकोरेशन (Ravindra Flower Decor)* 🌸\n\nशादी, हल्दी-मेहंदी व पार्टी फ्लावर सजावट के लिए हमारी कैटलॉग वेबसाइट देखें और मोबाइल होम स्क्रीन पर ऐप की तरह सेव करें:\n🔗 $sharedAppUrl"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareMessage)
                        }
                        context.startActivity(Intent.createChooser(intent, "वेबसाइट लिंक शेयर करें"))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WhatsAppGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("share_app_whatsapp_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "WhatsApp पर लिंक शेयर करें",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(sharedAppUrl))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = RoseDarkRed,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("वेबसाइट लिंक खोलें", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("ठीक है")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
