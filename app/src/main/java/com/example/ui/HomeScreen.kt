package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DecorationItem
import com.example.ui.components.ContactSection
import com.example.ui.components.DecorationCard
import com.example.ui.components.HeroHeader
import com.example.ui.components.SearchBarAndFilters
import com.example.ui.dialogs.AddEditDecorationDialog
import com.example.ui.dialogs.AdminPinDialog
import com.example.ui.dialogs.DetailDialog
import com.example.ui.dialogs.EditContactDialog
import com.example.ui.dialogs.InstallAppDialog
import com.example.ui.theme.MarigoldAmber
import com.example.ui.theme.MarigoldOrange
import com.example.ui.theme.RoseDarkRed

@Composable
fun HomeScreen(
    viewModel: DecorationViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val decorations by viewModel.decorations.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val priceSort by viewModel.priceSort.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()
    val pinError by viewModel.pinError.collectAsState()

    val showAddEditDialog by viewModel.showAddEditDialog.collectAsState()
    val showAdminPinDialog by viewModel.showAdminPinDialog.collectAsState()
    val showEditContactDialog by viewModel.showEditContactDialog.collectAsState()
    val showInstallAppDialog by viewModel.showInstallAppDialog.collectAsState()
    val showFilterDialog by viewModel.showFilterDialog.collectAsState()

    val isAppInstalled by viewModel.isAppInstalled.collectAsState()
    val isBannerDismissed by viewModel.isBannerDismissed.collectAsState()
    val websiteUrl by viewModel.websiteUrl.collectAsState()

    val editingItem by viewModel.editingItem.collectAsState()
    val selectedDetailItem by viewModel.selectedItemForDetail.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.loadSavedContactDetails(context)
    }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (isAdminMode) {
                FloatingActionButton(
                    onClick = { viewModel.openAddDialog() },
                    containerColor = RoseDarkRed,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.testTag("admin_add_fab")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Item"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 290.dp),
                contentPadding = PaddingValues(
                    bottom = paddingValues.calculateBottomPadding() + 80.dp
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 1200.dp)
            ) {
            // 1a. Top Web App Install Banner (Matching Image 2 - Only shown if NOT installed & NOT dismissed)
            if (!isAppInstalled && !isBannerDismissed) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF23252E), // Dark sleek card as in Image 2
                        shadowElevation = 6.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Dismiss '✕' button
                                IconButton(
                                    onClick = { viewModel.dismissInstallBanner(context) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close Banner",
                                        tint = Color.LightGray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                // App Icon / Logo (Purple/Pink gradient with Flower symbol)
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            Brush.linearGradient(
                                                listOf(Color(0xFF8E24AA), Color(0xFFD81B60))
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalFlorist,
                                        contentDescription = "App Logo",
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                // App Title and Website URL
                                Column {
                                    Text(
                                        text = "Install Ravindra Flower",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = websiteUrl,
                                        color = Color(0xFFB0BEC5),
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            // "Install" clickable button
                            Button(
                                onClick = {
                                    viewModel.markAppInstalled(context)
                                    viewModel.showInstallAppDialog.value = true
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color(0xFF64B5F6) // Bright blue text as in Image 2
                                ),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Install",
                                    color = Color(0xFF64B5F6),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // 1b. Hero Header Section (Spans Full Grid Width)
            item(span = { GridItemSpan(maxLineSpan) }) {
                HeroHeader(
                    viewModel = viewModel,
                    isAdminMode = isAdminMode,
                    onToggleAdminPin = { viewModel.showAdminPinDialog.value = true }
                )
            }

            // 2. Admin Status Bar with Exit Button (only shown when Admin Mode is active)
            if (isAdminMode) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Surface(
                        color = MarigoldAmber,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "एडमिन मोड चालू (आप पैकेज जोड़, एडिट/डिलीट कर सकते हैं)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }

                            // Exit Admin Button
                            Button(
                                onClick = { viewModel.exitAdminMode() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = RoseDarkRed,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("exit_admin_btn")
                            ) {
                                Text("🚪 एडमिन बंद करें", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 3. Search and Category Filter Chips
            item(span = { GridItemSpan(maxLineSpan) }) {
                SearchBarAndFilters(
                    viewModel = viewModel,
                    searchQuery = searchQuery,
                    selectedCategory = selectedCategory,
                    priceSort = priceSort,
                    categories = viewModel.categories
                )
            }

            // 4. Catalog Items / Empty State
            if (decorations.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = "No Results",
                                tint = Color.Gray,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "कोई सजावट पैकेज नहीं मिला!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "कृपया सर्च या फिल्टर बदलकर फिर प्रयास करें।",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    viewModel.onSearchQueryChange("")
                                    viewModel.onCategorySelect("सभी")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MarigoldOrange)
                            ) {
                                Text("सभी पैकेज दिखाएं")
                            }
                        }
                    }
                }
            } else {
                items(
                    items = decorations,
                    key = { it.id }
                ) { item ->
                    Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        DecorationCard(
                            item = item,
                            isAdminMode = isAdminMode,
                            onBookNowClick = { viewModel.bookNowOnWhatsApp(context, item) },
                            onCardClick = { viewModel.selectedItemForDetail.value = item },
                            onEditClick = { viewModel.openEditDialog(item) },
                            onDeleteClick = { viewModel.deleteDecoration(item) }
                        )
                    }
                }
            }

            // 5. Contact Us Section at Bottom
            item(span = { GridItemSpan(maxLineSpan) }) {
                ContactSection(viewModel = viewModel)
            }

            // 6. Footer Branding
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    Text(
                        text = "🌸 Ravindra Flower Decoration 🌸",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Text(
                        text = "हर शुभ अवसर के लिए आपकी पहली पसंद",
                        fontSize = 11.sp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

    // Dialogs
    if (showAdminPinDialog) {
        AdminPinDialog(
            viewModel = viewModel,
            pinError = pinError,
            onDismiss = { viewModel.showAdminPinDialog.value = false }
        )
    }

    if (showAddEditDialog) {
        AddEditDecorationDialog(
            viewModel = viewModel,
            editingItem = editingItem,
            categories = viewModel.categories,
            onDismiss = { viewModel.showAddEditDialog.value = false }
        )
    }

    if (showEditContactDialog) {
        EditContactDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.showEditContactDialog.value = false }
        )
    }

    if (showInstallAppDialog) {
        InstallAppDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.showInstallAppDialog.value = false }
        )
    }

    if (showFilterDialog) {
        com.example.ui.dialogs.FilterDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.showFilterDialog.value = false }
        )
    }

    selectedDetailItem?.let { item ->
        DetailDialog(
            item = item,
            viewModel = viewModel,
            onDismiss = { viewModel.selectedItemForDetail.value = null }
        )
    }
}
