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
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val items = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    DecorationItem(
                        docId = doc.id,
                        id = (data["id"] as? Long)?.toInt() ?: doc.id.hashCode(),
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
        try {
            val snapshot = collectionRef.get().await()
            if (snapshot == null || snapshot.isEmpty) {
                DefaultData.initialDecorations.forEach { item ->
                    val docId = item.docId.ifBlank { "decor_${item.id}" }
                    val map = mapOf(
                        "id" to item.id,
                        "docId" to docId,
                        "title" to item.title,
                        "category" to item.category,
                        "price" to item.price,
                        "imageUrl" to item.imageUrl,
                        "description" to item.description,
                        "flowersUsed" to item.flowersUsed,
                        "isPopular" to item.isPopular,
                        "isAvailable" to item.isAvailable
                    )
                    collectionRef.document(docId).set(map).await()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
