package com.ronik.stockmarketapp.data.mapper

import com.ronik.stockmarketapp.data.local.CompanyListingEntity
import com.ronik.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.ronik.stockmarketapp.domain.model.CompanyInfo
import com.ronik.stockmarketapp.domain.model.CompanyListing


fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}