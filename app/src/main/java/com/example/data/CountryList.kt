package com.example.data

import com.example.model.Country

object CountryList {
    val allCountries = listOf(
        Country(name = "Pakistan", dialCode = "+92", flag = "🇵🇰", code = "PK"),
        Country(name = "United Arab Emirates", dialCode = "+971", flag = "🇦🇪", code = "AE"),
        Country(name = "Saudi Arabia", dialCode = "+966", flag = "🇸🇦", code = "SA"),
        Country(name = "United States", dialCode = "+1", flag = "🇺🇸", code = "US"),
        Country(name = "United Kingdom", dialCode = "+44", flag = "🇬🇧", code = "GB"),
        Country(name = "Canada", dialCode = "+1", flag = "🇨🇦", code = "CA"),
        Country(name = "Turkey", dialCode = "+90", flag = "🇹🇷", code = "TR"),
        Country(name = "Qatar", dialCode = "+974", flag = "🇶🇦", code = "QA"),
        Country(name = "Oman", dialCode = "+968", flag = "🇴🇲", code = "OM"),
        Country(name = "Kuwait", dialCode = "+965", flag = "🇰🇼", code = "KW"),
        Country(name = "India", dialCode = "+91", flag = "🇮🇳", code = "IN"),
        Country(name = "Germany", dialCode = "+49", flag = "🇩🇪", code = "DE"),
        Country(name = "France", dialCode = "+33", flag = "🇫🇷", code = "FR"),
        Country(name = "China", dialCode = "+86", flag = "🇨🇳", code = "CN"),
        Country(name = "Australia", dialCode = "+61", flag = "🇦🇺", code = "AU"),
        Country(name = "Malaysia", dialCode = "+60", flag = "🇲🇾", code = "MY"),
        Country(name = "Indonesia", dialCode = "+62", flag = "🇮🇩", code = "ID"),
        Country(name = "Egypt", dialCode = "+20", flag = "🇪🇬", code = "EG"),
        Country(name = "South Africa", dialCode = "+27", flag = "🇿🇦", code = "ZA"),
        Country(name = "Brazil", dialCode = "+55", flag = "🇧🇷", code = "BR")
    )

    val defaultCountry = allCountries.first() // Pakistan (+92)
}
