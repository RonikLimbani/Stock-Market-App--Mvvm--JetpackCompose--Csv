package com.ronik.stockmarketapp.data.repository

import com.ronik.stockmarketapp.data.csv.CSVParser
import com.ronik.stockmarketapp.data.local.StockDatabase
import com.ronik.stockmarketapp.data.mapper.toCompanyInfo
import com.ronik.stockmarketapp.data.mapper.toCompanyListing
import com.ronik.stockmarketapp.data.mapper.toCompanyListingEntity
import com.ronik.stockmarketapp.data.remote.StockApi
import com.ronik.stockmarketapp.domain.model.CompanyInfo
import com.ronik.stockmarketapp.domain.model.CompanyListing
import com.ronik.stockmarketapp.domain.model.IntradayInfo
import com.ronik.stockmarketapp.domain.repository.StockRepository
import com.ronik.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StockRepositoryImpl @Inject constructor(
    private  val api: StockApi,
    private   val db: StockDatabase,
    private  val companyListingsParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>,
): StockRepository {

    private val dao=db.dao
    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))
            val localListing =dao.searchCompanyListing(query=query)
            emit(Resource.Success(
                data = localListing.map { it.toCompanyListing() }
            ))

            val isDbEmpty =localListing.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache){
                emit(Resource.Loading(isLoading = false))
                return@flow
            }
            val remoteListings = try {
                val response = api.getListings()
                companyListingsParser.parse(response.byteStream())
                } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(data = null, message = "Couldn't load data"))
                null
                }
            catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(data = null,message="Couldn't load data"))
                null
                }

            remoteListings?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )
                emit(Resource.Success(
                    data = dao
                        .searchCompanyListing("")
                        .map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))
            }


        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            val results = intradayInfoParser.parse(response.byteStream())
            Resource.Success(results)
        } catch(e: IOException) {
            e.printStackTrace()
            Resource.Error(data = null,
                message = "Couldn't load intraday info"
            )
        } catch(e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                data = null,
                message = "Couldn't load intraday info"
            )
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())
        } catch(e: IOException) {
            e.printStackTrace()
            Resource.Error(
                data = null,
                message = "Couldn't load company info"
            )
        } catch(e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                data = null,
                message = "Couldn't load company info"
            )
        }
    }
}