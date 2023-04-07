package com.example.playlistmaker.data.repositorie

interface DataRepository<T, P>: Repository {
    fun saveAllData(dataList: MutableList<T>)
    fun loadAllData():MutableList<T>
    fun saveData(data: T)
    fun loadData(key: P): T?
    fun clearDatabase()
    fun remove(key: P)
}