package com.example.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DecorationViewModel
import com.example.ui.PriceSortOption
import com.example.ui.theme.MarigoldOrange
import com.example.ui.theme.RoseDarkRed

@Composable
fun FilterDialog(
    viewModel: DecorationViewModel,
    onDismiss: () -> Unit
) {
    val currentSort by viewModel.priceSort.collectAsState()
    val currentMaxPrice by viewModel.maxPriceLimit.collectAsState()
    val currentCategory by viewModel.selectedCategory.collectAsState()
    val categories = viewModel.categories

    var tempSort by remember { mutableStateOf(currentSort) }
    var tempMaxPrice by remember { mutableStateOf(currentMaxPrice) }
    var tempCategory by remember { mutableStateOf(currentCategory) }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Filter",
                tint = RoseDarkRed
            )
        },
        title = {
            Text(
                text = "फ़िल्टर एवं सॉर्टिंग (Filter & Sort)",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // 1. Sort Order
                Text(
                    text = "1. सॉर्टिंग क्रम (Sort Order)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = RoseDarkRed,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = tempSort == PriceSortOption.NONE,
                        onClick = { tempSort = PriceSortOption.NONE },
                        colors = RadioButtonDefaults.colors(selectedColor = RoseDarkRed)
                    )
                    Text("डिफ़ॉल्ट क्रम (Default)", fontSize = 13.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = tempSort == PriceSortOption.LOW_TO_HIGH,
                        onClick = { tempSort = PriceSortOption.LOW_TO_HIGH },
                        colors = RadioButtonDefaults.colors(selectedColor = RoseDarkRed)
                    )
                    Text("कीमत: कम से अधिक (Price Low to High)", fontSize = 13.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = tempSort == PriceSortOption.HIGH_TO_LOW,
                        onClick = { tempSort = PriceSortOption.HIGH_TO_LOW },
                        colors = RadioButtonDefaults.colors(selectedColor = RoseDarkRed)
                    )
                    Text("कीमत: अधिक से कम (Price High to Low)", fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 2. Max Price Limit
                Text(
                    text = "2. अधिकतम बजट कीमत (Max Price Limit)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = RoseDarkRed,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                val priceLimits = listOf(
                    0.0 to "सभी कीमतें (All)",
                    5000.0 to "₹5,000 तक",
                    15000.0 to "₹15,000 तक",
                    30000.0 to "₹30,000 तक",
                    50000.0 to "₹50,000 तक"
                )

                priceLimits.forEach { (limit, label) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = tempMaxPrice == limit,
                            onClick = { tempMaxPrice = limit },
                            colors = RadioButtonDefaults.colors(selectedColor = MarigoldOrange)
                        )
                        Text(label, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 3. Category Filter
                Text(
                    text = "3. कैटेगरी (Category)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = RoseDarkRed,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                categories.chunked(2).forEach { rowCats ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        rowCats.forEach { cat ->
                            FilterChip(
                                selected = tempCategory == cat,
                                onClick = { tempCategory = cat },
                                label = { Text(cat, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = RoseDarkRed,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier
                                    .padding(end = 6.dp)
                                    .weight(1f)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.onPriceSortChange(tempSort)
                    viewModel.setMaxPriceLimit(tempMaxPrice)
                    viewModel.onCategorySelect(tempCategory)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoseDarkRed)
            ) {
                Text("लागू करें", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    viewModel.resetFilters()
                    onDismiss()
                }
            ) {
                Text("रीसेट करें", color = Color.DarkGray)
            }
        },
        shape = RoundedCornerShape(18.dp)
    )
}
