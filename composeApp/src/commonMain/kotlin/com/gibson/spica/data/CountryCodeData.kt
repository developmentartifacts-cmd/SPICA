package com.gibson.spica.data

object CountryCodeData {
    // Minimal list — extend as needed. Use ISO country codes / flags offline if desired.
    data class CountryCode(val iso: String, val name: String, val dialCode: String)

    val list = listOf(
        CountryCode("NG", "Nigeria", "+234"),
        CountryCode("GH", "Ghana", "+233"),
        CountryCode("KE", "Kenya", "+254"),
        CountryCode("ZA", "South Africa", "+27"),
        CountryCode("EG", "Egypt", "+20"),
        CountryCode("ET", "Ethiopia", "+251"),
        CountryCode("TZ", "Tanzania", "+255"),
        CountryCode("UG", "Uganda", "+256"),
        CountryCode("RW", "Rwanda", "+250"),
        CountryCode("CM", "Cameroon", "+237"),
        CountryCode("SN", "Senegal", "+221")
    )

    fun findByIso(iso: String) = list.find { it.iso == iso }
    fun findByDial(dial: String) = list.find { it.dialCode == dial }
}
