package com.example.ui.components

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DecorationViewModel
import com.example.ui.theme.MarigoldAmber
import com.example.ui.theme.MarigoldOrange
import com.example.ui.theme.RoseDarkRed
import com.example.ui.theme.WhatsAppGreen

@Composable
fun ContactSection(
    viewModel: DecorationViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var customerName by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var eventType by remember { mutableStateOf("") }
    var inquiryMessage by remember { mutableStateOf("") }

    val ownerName by viewModel.ownerName.collectAsState()
    val ownerPhone by viewModel.ownerPhone.collectAsState()
    val ownerPhone2 by viewModel.ownerPhone2.collectAsState()
    val shopAddress by viewModel.shopAddress.collectAsState()
    val businessHours by viewModel.businessHours.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 900.dp)
            .padding(16.dp)
            .testTag("contact_us_section"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = RoseDarkRed,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Contact",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "संपर्क करें (Contact Us)",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = RoseDarkRed
                            )
                        )
                        Text(
                            text = "रविंद्र फ्लावर डेकोरेशन सेवा केंद्र",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                if (isAdminMode) {
                    Button(
                        onClick = { viewModel.showEditContactDialog.value = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MarigoldOrange,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("edit_contact_btn")
                    ) {
                        Text("✏️ पता बदलें", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(16.dp))

            // Details List
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Owner Info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MarigoldOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "प्रोपराइटर: $ownerName",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Phone Numbers
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = null,
                        tint = MarigoldOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "मोबाइल/WhatsApp: $ownerPhone${if (ownerPhone2.isNotBlank()) ", $ownerPhone2" else ""}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Address
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MarigoldOrange,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "पता: $shopAddress",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Timings
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = MarigoldOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "समय: $businessHours",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(16.dp))

            // Quick Inquiry Form Header
            Text(
                text = "ऑनलाइन बुकिंग / इन्क्वायरी फॉर्म (Quick Enquiry)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "नीचे फॉर्म भरें और सीधे WhatsApp पर बुकिंग जानकारी भेजें:",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Name Input
            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("आपका नाम (Your Name)") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MarigoldOrange,
                    unfocusedBorderColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("inquiry_name_input"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date & Event Type Row
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = eventDate,
                    onValueChange = { eventDate = it },
                    label = { Text("तारीख (Date)") },
                    placeholder = { Text("जैसे: 25 नव) ") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MarigoldOrange,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("inquiry_date_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = eventType,
                    onValueChange = { eventType = it },
                    label = { Text("कार्यक्रम (Event)") },
                    placeholder = { Text("शादी / हल्दी / कार") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MarigoldOrange,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("inquiry_event_type_input"),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Extra Note
            OutlinedTextField(
                value = inquiryMessage,
                onValueChange = { inquiryMessage = it },
                label = { Text("सजावट का प्रकार या संदेश (Details)") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MarigoldOrange,
                    unfocusedBorderColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("inquiry_message_input"),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Form Submit Button -> WhatsApp
            Button(
                onClick = {
                    viewModel.sendGeneralInquiryOnWhatsApp(
                        context = context,
                        name = customerName.ifBlank { "ग्राहक" },
                        eventDate = eventDate.ifBlank { "तारीख तय नहीं" },
                        eventType = eventType.ifBlank { "सामान्य बुकिंग" },
                        details = inquiryMessage
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = WhatsAppGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_inquiry_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "WhatsApp पर संदेश भेजें (Send Inquiry)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
