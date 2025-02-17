package com.ronik.stockmarketapp.data.di


import com.ronik.stockmarketapp.data.csv.CSVParser
import com.ronik.stockmarketapp.data.csv.CompanyListingsParser
import com.ronik.stockmarketapp.data.csv.IntradayInfoParser
import com.ronik.stockmarketapp.data.repository.StockRepositoryImpl
import com.ronik.stockmarketapp.domain.model.CompanyListing
import com.ronik.stockmarketapp.domain.model.IntradayInfo
import com.ronik.stockmarketapp.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingsParser: CompanyListingsParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ): CSVParser<IntradayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository
}