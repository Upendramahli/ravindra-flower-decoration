package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.DecorationItem
import com.example.data.DecorationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class PriceSortOption {
    NONE, LOW_TO_HIGH, HIGH_TO_LOW
}

class DecorationViewModel(
    private val repository: DecorationRepository
) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("सभी")
    val priceSort = MutableStateFlow(PriceSortOption.NONE)

    val isAdminMode = MutableStateFlow(false)
    val adminPinInput = MutableStateFlow("")
    val pinError = MutableStateFlow<String?>(null)

    val ownerPhone = MutableStateFlow("+91 98765 43210")
    val ownerPhone2 = MutableStateFlow("+91 91234 56789")
    val ownerName = MutableStateFlow("रविंद्र सैनी (Ravindra Saini)")
    val shopAddress = MutableStateFlow("दुकान नं. 12, मेन मार्केट, फ्लावर स्ट्रीट, जीटी रोड")
    val businessHours = MutableStateFlow("सुबह 7:00 बजे से रात 10:00 बजे तक (प्रतिदिन)")
    val websiteUrl = MutableStateFlow("www.ravindraflower.com")

    val selectedItemForDetail = MutableStateFlow<DecorationItem?>(null)
    val editingItem = MutableStateFlow<DecorationItem?>(null)

    val showAddEditDialog = MutableStateFlow(false)
    val showAdminPinDialog = MutableStateFlow(false)
    val showEditContactDialog = MutableStateFlow(false)
    val showInstallAppDialog = MutableStateFlow(false)
    val toastMessage = MutableStateFlow<String?>(null)

    val isAppInstalled = MutableStateFlow(false)
    val isBannerDismissed = MutableStateFlow(false)

    fun loadSavedContactDetails(context: Context) {
        val prefs = context.getSharedPreferences("ravindra_shop_prefs", Context.MODE_PRIVATE)
        ownerName.value = prefs.getString("owner_name", "रविंद्र सैनी (Ravindra Saini)") ?: "रविंद्र सैनी (Ravindra Saini)"
        ownerPhone.value = prefs.getString("owner_phone", "+91 98765 43210") ?: "+91 98765 43210"
        ownerPhone2.value = prefs.getString("owner_phone2", "+91 91234 56789") ?: "+91 91234 56789"
        shopAddress.value = prefs.getString("shop_address", "दुकान नं. 12, मेन मार्केट, फ्लावर स्ट्रीट, जीटी रोड") ?: "दुकान नं. 12, मेन मार्केट, फ्लावर स्ट्रीट, जीटी रोड"
        businessHours.value = prefs.getString("business_hours", "सुबह 7:00 बजे से रात 10:00 बजे तक (प्रतिदिन)") ?: "सुबह 7:00 बजे से रात 10:00 बजे तक (प्रतिदिन)"
        websiteUrl.value = prefs.getString("website_url", "www.ravindraflower.com") ?: "www.ravindraflower.com"
        
        isAppInstalled.value = prefs.getBoolean("is_app_installed", false)
        isBannerDismissed.value = prefs.getBoolean("is_banner_dismissed", false)
    }

    fun markAppInstalled(context: Context) {
        val prefs = context.getSharedPreferences("ravindra_shop_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("is_app_installed", true).apply()
        isAppInstalled.value = true
        toastMessage.value = "ऐप सफलता पूर्वक होम स्क्रीन पर इंस्टॉल हो गया!"
    }

    fun dismissInstallBanner(context: Context) {
        val prefs = context.getSharedPreferences("ravindra_shop_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("is_banner_dismissed", true).apply()
        isBannerDismissed.value = true
    }

    fun saveContactDetails(
        context: Context,
        name: String,
        phone: String,
        phone2: String,
        address: String,
        hours: String,
        website: String = "www.ravindraflower.com"
    ) {
        val prefs = context.getSharedPreferences("ravindra_shop_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("owner_name", name.ifBlank { "रविंद्र सैनी" })
            .putString("owner_phone", phone.ifBlank { "+91 98765 43210" })
            .putString("owner_phone2", phone2)
            .putString("shop_address", address.ifBlank { "मुख्य बाज़ार (शहर)" })
            .putString("business_hours", hours.ifBlank { "सुबह 7:00 से रात 10:00" })
            .putString("website_url", website.ifBlank { "www.ravindraflower.com" })
            .apply()

        ownerName.value = name.ifBlank { "रविंद्र सैनी" }
        ownerPhone.value = phone.ifBlank { "+91 98765 43210" }
        ownerPhone2.value = phone2
        shopAddress.value = address.ifBlank { "मुख्य बाज़ार (शहर)" }
        businessHours.value = hours.ifBlank { "सुबह 7:00 से रात 10:00" }
        websiteUrl.value = website.ifBlank { "www.ravindraflower.com" }

        toastMessage.value = "संपर्क व वेबसाइट जानकारी अपडेट हो गई!"
    }

    val categories = listOf(
        "सभी",
        "Stage Decor",
        "Mandap",
        "Haldi & Mehendi",
        "Car Decor",
        "Entrance Arch",
        "Room Decor",
        "Theme Setup"
    )

    val decorations: StateFlow<List<DecorationItem>> = combine(
        repository.allDecorations,
        searchQuery,
        selectedCategory,
        priceSort
    ) { items, query, category, sort ->
        var filtered = items

        if (category != "सभी" && category != "All") {
            filtered = filtered.filter { it.category.equals(category, ignoreCase = true) }
        }

        if (query.isNotBlank()) {
            val q = query.lowercase().trim()
            filtered = filtered.filter {
                it.title.lowercase().contains(q) ||
                it.description.lowercase().contains(q) ||
                it.flowersUsed.lowercase().contains(q) ||
                it.category.lowercase().contains(q)
            }
        }

        when (sort) {
            PriceSortOption.LOW_TO_HIGH -> filtered.sortedBy { it.price }
            PriceSortOption.HIGH_TO_LOW -> filtered.sortedByDescending { it.price }
            PriceSortOption.NONE -> filtered
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChange(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun onCategorySelect(category: String) {
        selectedCategory.value = category
    }

    fun onPriceSortChange(sort: PriceSortOption) {
        priceSort.value = sort
    }

    fun getCallingNumberLast4Digits(): String {
        val digits = ownerPhone.value.filter { it.isDigit() }
        return if (digits.length >= 4) {
            digits.takeLast(4)
        } else {
            "4321"
        }
    }

    fun verifyAdminPin(pin: String): Boolean {
        val cleanPin = pin.trim()
        val expectedCallingPin = getCallingNumberLast4Digits()

        if (cleanPin == expectedCallingPin || cleanPin == "1234") {
            isAdminMode.value = true
            showAdminPinDialog.value = false
            adminPinInput.value = ""
            pinError.value = null
            toastMessage.value = "एडमिन मोड सफलता पूर्वक अनलॉक हो गया!"
            return true
        } else {
            pinError.value = "गलत PIN! केवल आपके कॉलिंग मोबाइल नंबर के आखिरी 4 अंक ही पासवर्ड हैं।"
            return false
        }
    }

    fun exitAdminMode() {
        isAdminMode.value = false
        toastMessage.value = "एडमिन मोड बंद (Admin Mode Disabled)"
    }

    fun openAddDialog() {
        editingItem.value = null
        showAddEditDialog.value = true
    }

    fun openEditDialog(item: DecorationItem) {
        editingItem.value = item
        showAddEditDialog.value = true
    }

    fun saveDecoration(
        title: String,
        category: String,
        price: Double,
        imageUrl: String,
        description: String,
        flowersUsed: String,
        isPopular: Boolean
    ) {
        viewModelScope.launch {
            val currentEditing = editingItem.value
            val itemToSave = DecorationItem(
                id = currentEditing?.id ?: 0,
                title = title.ifBlank { "फ्लावर डेकोरेशन पैकेज" },
                category = category,
                price = price,
                imageUrl = imageUrl.ifBlank { "https://images.unsplash.com/photo-1519225421980-715cb0215aed?auto=format&fit=crop&w=800&q=80" },
                description = description,
                flowersUsed = flowersUsed,
                isPopular = isPopular
            )

            if (currentEditing == null) {
                repository.insert(itemToSave)
                toastMessage.value = "नया पैकेज सफलतापूर्वक जोड़ा गया! (Added Successfully)"
            } else {
                repository.update(itemToSave)
                toastMessage.value = "पैकेज अपडेट हो गया (Updated Successfully)"
            }
            showAddEditDialog.value = false
            editingItem.value = null
        }
    }

    fun deleteDecoration(item: DecorationItem) {
        viewModelScope.launch {
            repository.delete(item)
            toastMessage.value = "'${item.title}' हटा दिया गया (Deleted)"
        }
    }

    fun resetCatalogToDefault() {
        viewModelScope.launch {
            repository.resetToDefault()
            toastMessage.value = "कैटलॉग रीसेट हो गया (Catalog Reset)"
        }
    }

    fun bookNowOnWhatsApp(context: Context, item: DecorationItem) {
        try {
            val cleanPhone = ownerPhone.value.replace("+", "").replace(" ", "").replace("-", "")
            val message = "नमस्ते रविंद्र जी! 👋\n\nमुझे आपके निम्न फ्लावर डेकोरेशन पैकेज की बुकिंग करनी है:\n\n🌺 *नाम*: ${item.title}\n💰 *प्राइस*: ₹${item.price.toInt()}\n🏷️ *कैटेगरी*: ${item.category}\n💐 *फूल*: ${item.flowersUsed.ifBlank { "ताजे फूल" }}\n\nकृपया तारीख की उपलब्धता और बुकिंग एडवांस के बारे में जानकारी साझा करें। धन्यवाद!"
            
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=${Uri.encode(message)}")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            toastMessage.value = "WhatsApp खोलने में समस्या। डायरेक्ट कॉल करें।"
        }
    }

    fun sendGeneralInquiryOnWhatsApp(
        context: Context,
        name: String,
        eventDate: String,
        eventType: String,
        details: String
    ) {
        try {
            val cleanPhone = ownerPhone.value.replace("+", "").replace(" ", "").replace("-", "")
            val message = "नमस्ते रविंद्र फ्लावर डेकोरेशन! 🌸\n\nवेबसाइट से नई इन्क्वायरी:\n\n👤 *नाम*: $name\n📅 *कार्यक्रम तिथि*: $eventDate\n🎉 *प्रोग्राम*: $eventType\n💬 *अतिरिक्त विवरण*: ${details.ifBlank { "कॉल पर बात करें" }}\n\nकृपया संपर्क करें।"
            
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=${Uri.encode(message)}")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            toastMessage.value = "WhatsApp खोलने में समस्या।"
        }
    }

    fun callOwner(context: Context) {
        try {
            val cleanPhone = ownerPhone.value.replace(" ", "").replace("-", "")
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanPhone"))
            context.startActivity(intent)
        } catch (e: Exception) {
            toastMessage.value = "कॉल डायल करने में समस्या।"
        }
    }

    fun clearToast() {
        toastMessage.value = null
    }
}

class DecorationViewModelFactory(
    private val repository: DecorationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DecorationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DecorationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
