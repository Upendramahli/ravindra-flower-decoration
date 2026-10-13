package com.example.data

import kotlinx.coroutines.flow.Flow

class DecorationRepository(private val dao: DecorationDao) {

    val allDecorations: Flow<List<DecorationItem>> = dao.getAllDecorations()

    fun getByCategory(category: String): Flow<List<DecorationItem>> {
        return if (category == "All" || category == "सभी") {
            dao.getAllDecorations()
        } else {
            dao.getDecorationsByCategory(category)
        }
    }

    suspend fun insert(item: DecorationItem) {
        dao.insertDecoration(item)
    }

    suspend fun update(item: DecorationItem) {
        dao.updateDecoration(item)
    }

    suspend fun delete(item: DecorationItem) {
        dao.deleteDecoration(item)
    }

    suspend fun resetToDefault() {
        dao.deleteAll()
        dao.insertAll(DefaultData.initialDecorations)
    }
}
