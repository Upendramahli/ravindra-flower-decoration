package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class DecorationRepository(
    private val dao: DecorationDao,
    private val firestoreRepository: FirestoreRepository? = null
) {

    val allDecorations: Flow<List<DecorationItem>> = if (firestoreRepository != null) {
        firestoreRepository.getRealtimeDecorations().onEach { items ->
            if (items.isNotEmpty()) {
                dao.deleteAll()
                dao.insertAll(items)
            }
        }
    } else {
        dao.getAllDecorations()
    }

    val contactInfoFlow: Flow<Map<String, String>>? = firestoreRepository?.getRealtimeContactInfo()

    suspend fun saveContactInfoToCloud(
        name: String,
        phone: String,
        phone2: String,
        address: String,
        hours: String,
        website: String
    ) {
        firestoreRepository?.saveContactInfo(name, phone, phone2, address, hours, website)
    }

    suspend fun insert(item: DecorationItem) {
        if (firestoreRepository != null) {
            firestoreRepository.saveDecoration(item)
        } else {
            dao.insertDecoration(item)
        }
    }

    suspend fun update(item: DecorationItem) {
        if (firestoreRepository != null) {
            firestoreRepository.saveDecoration(item)
        } else {
            dao.updateDecoration(item)
        }
    }

    suspend fun delete(item: DecorationItem) {
        if (firestoreRepository != null) {
            firestoreRepository.deleteDecoration(item)
        } else {
            dao.deleteDecoration(item)
        }
    }

    suspend fun resetToDefault() {
        dao.deleteAll()
        if (firestoreRepository != null) {
            firestoreRepository.seedInitialDataIfEmpty()
        } else {
            dao.insertAll(DefaultData.initialDecorations)
        }
    }

    suspend fun seedCloudIfEmpty() {
        firestoreRepository?.seedInitialDataIfEmpty()
    }
}
