package com.example.data

import android.content.Context
import com.example.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreRepository(
    private val context: Context,
    private val dao: DecorationDao
) {
    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance(context.getString(R.string.firestore_database_id))
    }

    private val collectionRef by lazy {
        firestore.collection("decorations")
    }

    fun getRealtimeDecorations(): Flow<List<DecorationItem>> = callbackFlow {
        val listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                android.util.Log.e("FirestoreRepository", "Error getting decorations", error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val items = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    DecorationItem(
                        docId = doc.id,
                        id = when (val idVal = data["id"]) {
                            is Number -> idVal.toInt()
                            is String -> idVal.toIntOrNull() ?: doc.id.hashCode()
                            else -> doc.id.hashCode()
                        },
                        title = data["title"] as? String ?: "",
                        category = data["category"] as? String ?: "",
                        price = (data["price"] as? Number)?.toDouble() ?: 0.0,
                        imageUrl = data["imageUrl"] as? String ?: "",
                        description = data["description"] as? String ?: "",
                        flowersUsed = data["flowersUsed"] as? String ?: "",
                        isPopular = data["isPopular"] as? Boolean ?: false,
                        isAvailable = data["isAvailable"] as? Boolean ?: true
                    )
                }
                trySend(items)
            }
        }

        awaitClose {
            listenerRegistration.remove()
        }
    }

    fun getRealtimeContactInfo(): Flow<Map<String, String>> = callbackFlow {
        val listener = firestore.collection("app_config").document("contact")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e("FirestoreRepository", "Error getting contact info", error)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val data = snapshot.data ?: emptyMap()
                    val result = mapOf(
                        "ownerName" to (data["ownerName"] as? String ?: ""),
                        "ownerPhone" to (data["ownerPhone"] as? String ?: ""),
                        "ownerPhone2" to (data["ownerPhone2"] as? String ?: ""),
                        "shopAddress" to (data["shopAddress"] as? String ?: ""),
                        "businessHours" to (data["businessHours"] as? String ?: ""),
                        "websiteUrl" to (data["websiteUrl"] as? String ?: "")
                    )
                    trySend(result)
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    suspend fun saveContactInfo(
        ownerName: String,
        ownerPhone: String,
        ownerPhone2: String,
        shopAddress: String,
        businessHours: String,
        websiteUrl: String
    ) {
        val map = mapOf(
            "ownerName" to ownerName,
            "ownerPhone" to ownerPhone,
            "ownerPhone2" to ownerPhone2,
            "shopAddress" to shopAddress,
            "businessHours" to businessHours,
            "websiteUrl" to websiteUrl
        )
        try {
            firestore.collection("app_config").document("contact").set(map).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun saveDecoration(item: DecorationItem) {
        val docId = if (item.docId.isNotBlank()) item.docId else collectionRef.document().id
        val itemToSave = item.copy(docId = docId)

        val map = mapOf(
            "id" to itemToSave.id,
            "docId" to docId,
            "title" to itemToSave.title,
            "category" to itemToSave.category,
            "price" to itemToSave.price,
            "imageUrl" to itemToSave.imageUrl,
            "description" to itemToSave.description,
            "flowersUsed" to itemToSave.flowersUsed,
            "isPopular" to itemToSave.isPopular,
            "isAvailable" to itemToSave.isAvailable
        )

        collectionRef.document(docId).set(map).await()
        dao.insertDecoration(itemToSave)
    }

    suspend fun deleteDecoration(item: DecorationItem) {
        if (item.docId.isNotBlank()) {
            collectionRef.document(item.docId).delete().await()
        }
        dao.deleteDecoration(item)
    }

    suspend fun seedInitialDataIfEmpty() {
        // Do nothing to ensure existing user uploaded data is never overwritten or reset
        return
    }
}
