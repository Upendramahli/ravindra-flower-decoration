package com.example.ui.dialogs

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.DecorationItem
import com.example.ui.DecorationViewModel
import com.example.ui.theme.MarigoldOrange
import com.example.ui.theme.RoseDarkRed
import com.example.utils.ImageUploader
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditDecorationDialog(
    viewModel: DecorationViewModel,
    editingItem: DecorationItem?,
    categories: List<String>,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isEditing = editingItem != null

    var title by remember { mutableStateOf(editingItem?.title ?: "") }
    var selectedCategory by remember { mutableStateOf(editingItem?.category ?: "Stage Decor") }
    var priceText by remember { mutableStateOf(editingItem?.price?.toInt()?.toString() ?: "15000") }
    var imageUrl by remember { mutableStateOf(editingItem?.imageUrl ?: "https://images.unsplash.com/photo-1519225421980-715cb0215aed?auto=format&fit=crop&w=800&q=80") }
    var description by remember { mutableStateOf(editingItem?.description ?: "") }
    var flowersUsed by remember { mutableStateOf(editingItem?.flowersUsed ?: "") }
    var isPopular by remember { mutableStateOf(editingItem?.isPopular ?: false) }

    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var isUploadingImage by remember { mutableStateOf(false) }

    val validCategories = categories.filter { it != "सभी" && it != "All" }

    var selectedCropUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher for selecting photo directly from Gallery
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedCropUri = uri
        }
    }

    // Quick Image Preset Options for easy click selection
    val presetImages = listOf(
        "https://images.unsplash.com/photo-1519225421980-715cb0215aed?auto=format&fit=crop&w=800&q=80" to "स्टेज (Stage)",
        "https://images.unsplash.com/photo-1526047932273-341f2a7631f9?auto=format&fit=crop&w=800&q=80" to "हल्दी (Haldi)",
        "https://images.unsplash.com/photo-1511795409834-ef04bbd61622?auto=format&fit=crop&w=800&q=80" to "एंट्रेंस (Gate)",
        "https://images.unsplash.com/photo-1532712938310-34cb3982ef74?auto=format&fit=crop&w=800&q=80" to "कार (Car)",
        "https://images.unsplash.com/photo-1544078751-58fee2d8a03b?auto=format&fit=crop&w=800&q=80" to "मण्डप (Mandap)",
        "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=800&q=80" to "रूम (Room)"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isEditing) Icons.Default.Edit else Icons.Default.Add,
                    contentDescription = null,
                    tint = RoseDarkRed
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEditing) "डेकोरेशन एडिट करें (Edit Item)" else "नया डेकोरेशन जोड़ें (Add Item)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
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
                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("पैकेज का नाम (Title)") },
                    placeholder = { Text("उदा: रॉयल वेडिंग स्टेज सजावट") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Title, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseDarkRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_item_title_input")
                )

                // Category Selector Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryDropdownExpanded,
                    onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("कैटेगरी (Category)") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Category, contentDescription = null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseDarkRed),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("add_item_category_input")
                    )

                    ExposedDropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false }
                    ) {
                        validCategories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    selectedCategory = cat
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Price Input
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("प्राइस ₹ (Price in INR)") },
                    placeholder = { Text("15000") },
                    leadingIcon = { Icon(imageVector = Icons.Default.LocalOffer, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseDarkRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_item_price_input")
                )

                // Gallery Image Picker Button + Auto Host API
                Button(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RoseDarkRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("pick_gallery_image_btn")
                ) {
                    if (isUploadingImage) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("इमेज अपलोड हो रही है...", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = "Gallery",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("🖼️ गैलरी से फोटो चुनें (Auto URL)", fontWeight = FontWeight.Bold)
                    }
                }

                // Live Image Preview Container
                if (imageUrl.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(4f / 3f)
                            .clip(RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Image Preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                        Surface(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "इमेज लाइव प्रिव्यू (Preview)",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Image URL Input Field (Manual edit or auto-filled)
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("इमेज ऑनलाइन लिंक (Direct Image URL)") },
                    placeholder = { Text("https://example.com/flower_photo.jpg") },
                    leadingIcon = { Icon(imageVector = Icons.Default.Image, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseDarkRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_item_image_url_input")
                )

                // Image Size Guidance Card
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "📐 अनुशंसित इमेज साइज़ व गाइडलाइन:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoseDarkRed
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "• आस्पेक्ट रेशियो (Aspect Ratio): 4:3 क्रॉप व ज़ूम एडिटर सपोर्टेड\n• अधिकतम फ़ाइल साइज़ (Max File Size): 10 MB तक (JPG, PNG, WEBP)\n• गैलरी से फोटो चुनते ही क्रॉप एडिटर खुलेगा व ऑनलाइन URL बन जाएगा!",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    }
                }

                // Quick Preset Image Selector
                Text(
                    text = "या तैयार सैम्पल इमेज लिंक चुनें:",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(presetImages) { (url, label) ->
                        Surface(
                            onClick = { imageUrl = url },
                            shape = RoundedCornerShape(16.dp),
                            color = if (imageUrl == url) MarigoldOrange else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (imageUrl == url) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // Flowers Used
                OutlinedTextField(
                    value = flowersUsed,
                    onValueChange = { flowersUsed = it },
                    label = { Text("उपयोग किए जाने वाले फूल (Flowers Used)") },
                    placeholder = { Text("लाल गुलाब, गेंदा, रजनीगंधा...") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseDarkRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_item_flowers_input")
                )

                // Description Input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("विस्तृत विवरण (Description)") },
                    placeholder = { Text("सजावट की मुख्य विशेषताएं...") },
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseDarkRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_item_desc_input")
                )

                // Popular Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isPopular = !isPopular }
                ) {
                    Checkbox(
                        checked = isPopular,
                        onCheckedChange = { isPopular = it },
                        colors = CheckboxDefaults.colors(checkedColor = RoseDarkRed),
                        modifier = Modifier.testTag("add_item_popular_checkbox")
                    )
                    Text(
                        text = "होमपेज पर 'लोकप्रिय (Popular)' टैग दिखाएं",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = priceText.toDoubleOrNull() ?: 10000.0
                    viewModel.saveDecoration(
                        title = title,
                        category = selectedCategory,
                        price = p,
                        imageUrl = imageUrl,
                        description = description,
                        flowersUsed = flowersUsed,
                        isPopular = isPopular
                    )
                },
                enabled = !isUploadingImage,
                colors = ButtonDefaults.buttonColors(
                    containerColor = RoseDarkRed,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("save_decoration_btn")
            ) {
                Text(
                    text = if (isEditing) "अपडेट सेव करें (Save)" else "नया जोड़ें (Add Item)",
                    fontWeight = FontWeight.Bold
                )
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

    selectedCropUri?.let { uri ->
        ImageCropZoomDialog(
            imageUri = uri,
            onCroppedAndUploaded = { hostedUrl ->
                if (hostedUrl.isNotBlank()) {
                    imageUrl = hostedUrl
                }
                selectedCropUri = null
            },
            onDismiss = { selectedCropUri = null }
        )
    }
}
