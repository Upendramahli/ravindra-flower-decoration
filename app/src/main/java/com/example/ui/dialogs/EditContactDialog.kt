package com.example.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DecorationViewModel
import com.example.ui.theme.RoseDarkRed

@Composable
fun EditContactDialog(
    viewModel: DecorationViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val currentName by viewModel.ownerName.collectAsState()
    val currentPhone by viewModel.ownerPhone.collectAsState()
    val currentPhone2 by viewModel.ownerPhone2.collectAsState()
    val currentAddress by viewModel.shopAddress.collectAsState()
    val currentHours by viewModel.businessHours.collectAsState()
    val currentWebsite by viewModel.websiteUrl.collectAsState()

    var nameText by remember { mutableStateOf(currentName) }
    var phoneText by remember { mutableStateOf(currentPhone) }
    var phone2Text by remember { mutableStateOf(currentPhone2) }
    var addressText by remember { mutableStateOf(currentAddress) }
    var hoursText by remember { mutableStateOf(currentHours) }
    var websiteText by remember { mutableStateOf(currentWebsite) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = RoseDarkRed
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "दुकान का पता व संपर्क बदलें (Edit Contact)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "यहाँ आप अपना नाम, मोबाइल/WhatsApp नंबर, वेबसाइट और दुकान का सटीक पता अपडेट कर सकते हैं:",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                // Owner Name
                OutlinedTextField(
                    value = nameText,
                    onValueChange = { nameText = it },
                    label = { Text("मालिक का नाम (Owner Name)") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseDarkRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_owner_name_input")
                )

                // Primary Phone / WhatsApp
                OutlinedTextField(
                    value = phoneText,
                    onValueChange = { phoneText = it },
                    label = { Text("प्राथमिक मोबाइल / WhatsApp नंबर") },
                    placeholder = { Text("+91 9876543210") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Call, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseDarkRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_owner_phone_input")
                )

                // Secondary Phone
                OutlinedTextField(
                    value = phone2Text,
                    onValueChange = { phone2Text = it },
                    label = { Text("दूसरा मोबाइल नंबर (वैकल्पिक)") },
                    placeholder = { Text("+91 9123456789") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Call, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseDarkRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_owner_phone2_input")
                )

                // Website URL
                OutlinedTextField(
                    value = websiteText,
                    onValueChange = { websiteText = it },
                    label = { Text("वेबसाइट लिंक (Website Domain)") },
                    placeholder = { Text("www.ravindraflower.com") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Language, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseDarkRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_website_url_input")
                )

                // Address Field
                OutlinedTextField(
                    value = addressText,
                    onValueChange = { addressText = it },
                    label = { Text("दुकान का पूरा पता (Full Shop Address)") },
                    placeholder = { Text("दुकान नं. 12, मेन मार्केट, जीटी रोड, शहर...") },
                    leadingIcon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = null) },
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseDarkRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_shop_address_input")
                )

                // Business Hours
                OutlinedTextField(
                    value = hoursText,
                    onValueChange = { hoursText = it },
                    label = { Text("दुकान खुलने का समय (Timings)") },
                    placeholder = { Text("सुबह 7:00 बजे से रात 10:00 बजे तक") },
                    leadingIcon = { Icon(imageVector = Icons.Default.AccessTime, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseDarkRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_business_hours_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.saveContactDetails(
                        context = context,
                        name = nameText,
                        phone = phoneText,
                        phone2 = phone2Text,
                        address = addressText,
                        hours = hoursText,
                        website = websiteText
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = RoseDarkRed,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("save_contact_details_btn")
            ) {
                Text("पता व नंबर सेव करें", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("रद्द करें")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
