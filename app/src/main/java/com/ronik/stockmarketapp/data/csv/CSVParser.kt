package com.ronik.stockmarketapp.data.csv

import java.io.InputStream


interface CSVParser<T> {
    suspend fun parse(stream: InputStream): List<T>
}

/*
interface CSVParser {
    suspend fun<T> parse(stream: InputStream): List<T>
}*/
