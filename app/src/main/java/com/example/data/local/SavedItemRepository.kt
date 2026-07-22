package com.example.data.local

import kotlinx.coroutines.flow.Flow

class SavedItemRepository(private val dao: SavedItemDao) {
    val allSavedItems: Flow<List<SavedItem>> = dao.getAllSavedItems()

    fun getItemsByType(type: String): Flow<List<SavedItem>> = dao.getSavedItemsByType(type)

    suspend fun saveItem(item: SavedItem): Long = dao.insertSavedItem(item)

    suspend fun deleteItem(id: Long) = dao.deleteSavedItemById(id)
}
