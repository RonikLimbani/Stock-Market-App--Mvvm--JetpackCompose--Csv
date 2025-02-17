package com.ronik.stockmarketapp.presentation.company_info

import com.ronik.stockmarketapp.domain.model.CompanyInfo
import com.ronik.stockmarketapp.domain.model.IntradayInfo


data class CompanyInfoState(
    val stockInfos: List<IntradayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
