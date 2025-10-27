package com.gibson.spica.data

/**

🌍 Africa Country → State → Town data

Used for SPICA account setup dropdowns.

Designed for offline, lightweight use (no JSON parsing required).
*/
object LocationData {

/** List of available countries */
val countries = listOf(
"Nigeria",
"Ghana",
"Kenya",
"South Africa",
"Egypt",
"Ethiopia",
"Tanzania",
"Uganda",
"Rwanda",
"Cameroon",
"Senegal"
)

// 🇳🇬 NIGERIA
val nigeriaStatesAndTowns: Map<String, List<String>> = mapOf(
"Abia" to listOf(
"Aba North", "Aba South", "Arochukwu", "Bende", "Ikwuano",
"Isiala Ngwa North", "Isiala Ngwa South", "Isiukwuato",
"Obi Ngwa", "Ohafia", "Osisioma", "Ugwunagbo", "Ukwa East",
"Ukwa West", "Umuahia North", "Umuahia South", "Umuneochi"
),
"Adamawa" to listOf(
"Demsa", "Fufore", "Ganye", "Girei", "Gombi", "Guyuk", "Hong",
"Jada", "Lamurde", "Madagali", "Maiha", "Mayo-Belwa", "Michika",
"Mubi North", "Mubi South", "Numan", "Shelleng", "Song",
"Toungo", "Yola North", "Yola South"
),
"Akwa Ibom" to listOf(
"Abak", "Eastern Obolo", "Eket", "Esit-Eket", "Essien Udim",
"Etim Ekpo", "Etinan", "Ibeno", "Ibesikpo Asutan", "Ibiono Ibom",
"Ika", "Ikono", "Ikot Abasi", "Ikot Ekpene", "Ini", "Itu",
"Mbo", "Mkpat Enin", "Nsit Atai", "Nsit Ibom", "Nsit Ubium",
"Obot Akara", "Okobo", "Onna", "Oron", "Oruk Anam", "Udung Uko",
"Ukanafun", "Uruan", "Urue-Offong/Oruko", "Uyo"
),
"Anambra" to listOf(
"Aguata", "Anambra East", "Anambra West", "Anocha", "Awka North",
"Awka South", "Ayamelum", "Dunukofia", "Ekwusigo", "Idemili North",
"Idemili South", "Ihiala", "Njikoka", "Nnewi North", "Nnewi South",
"Ogbaru", "Onitsha North", "Onitsha South", "Orumba North", "Orumba South", "Oyi"
),
"Bauchi" to listOf(
"Alkaleri", "Bauchi", "Bogoro", "Damban", "Darazo", "Dass", "Gamawa",
"Ganjuwa", "Giade", "Itas/Gadau", "Jama’are", "Katagum", "Kirfi", "Misau",
"Ningi", "Shira", "Tafawa Balewa", "Toro", "Warji", "Zaki"
),
"Bayelsa" to listOf("Brass", "Ekeremor", "Kolokuma/Opokuma", "Nembe", "Ogbia", "Sagbama", "Southern Ijaw", "Yenagoa"),
"Benue" to listOf("Ado", "Agatu", "Apa", "Buruku", "Gboko", "Guma", "Gwer East", "Gwer West", "Katsina-Ala", "Konshisha",
"Kwande", "Logo", "Makurdi", "Obi", "Ogbadibo", "Ohimini", "Oju", "Okpokwu", "Otukpo", "Tarka", "Ukum", "Ushongo", "Vandeikya"
),
"Borno" to listOf(
"Abadam", "Askira/Uba", "Bama", "Bayo", "Biu", "Chibok", "Damboa", "Dikwa",
"Gubio", "Guzamala", "Gwoza", "Hawul", "Jere", "Kaga", "Kala/Balge", "Konduga",
"Kukawa", "Kwaya Kusar", "Mafa", "Magumeri", "Maiduguri", "Marte", "Mobbar", "Monguno",
"Ngala", "Nganzai", "Shani"
),
"Cross River" to listOf("Abi", "Akamkpa", "Akpabuyo", "Bakassi", "Bekwarra", "Biase", "Boki", "Calabar Municipal", "Calabar South", "Etung", "Ikom", "Obanliku", "Obubra", "Obudu", "Odukpani", "Ogoja", "Yakurr", "Yala"),
"Delta" to listOf("Aniocha North", "Aniocha South", "Bomadi", "Burutu", "Ethiope East", "Ethiope West", "Ika North East", "Ika South", "Isoko North", "Isoko South", "Ndokwa East", "Ndokwa West", "Okpe", "Oshimili North", "Oshimili South", "Patani", "Sapele", "Udu", "Ughelli North", "Ughelli South", "Ukwuani", "Uvwie", "Warri North", "Warri South", "Warri South West"),
"Ebonyi" to listOf("Abakaliki", "Afikpo North", "Afikpo South", "Ezza North", "Ezza South", "Ikwo", "Ishielu", "Ivo", "Izzi", "Ohaozara", "Ohaukwu", "Onicha", "Ebonyi"),
"Edo" to listOf("Akoko Edo", "Egor", "Esan Central", "Esan North-East", "Esan South-East", "Esan West", "Etsako Central", "Etsako East", "Etsako West", "Igueben", "Ikpoba-Okha", "Oredo", "Orhionmwon", "Ovia North-East", "Ovia South-West", "Owan East", "Owan West", "Uhunmwonde"),
"Ekiti" to listOf("Ado Ekiti", "Efon", "Ekiti East", "Ekiti South-West", "Ekiti West", "Emure", "Gbonyin", "Ido-Osi", "Ijero", "Ikere", "Ikole", "Ilejemeje", "Irepodun/Ifelodun", "Ise/Orun", "Moba", "Oye"),
"Enugu" to listOf("Aninri", "Awgu", "Enugu East", "Enugu North", "Enugu South", "Ezeagu", "Igbo Etiti", "Igbo Eze North", "Igbo Eze South", "Isi Uzo", "Nkanu East", "Nkanu West", "Nsukka", "Oji River", "Udenu", "Udi", "Uzo-Uwani"),
"Gombe" to listOf("Akko", "Balanga", "Billiri", "Dukku", "Funakaye", "Gombe", "Kaltungo", "Kwami", "Nafada", "Shongom", "Yamaltu/Deba"),
"Imo" to listOf("Aboh Mbaise", "Ahiazu Mbaise", "Ehime Mbano", "Ezinihitte", "Ideato North", "Ideato South", "Ihitte/Uboma", "Ikeduru", "Isiala Mbano", "Isu", "Mbaitoli", "Ngor Okpala", "Njaba", "Nkwerre", "Nwangele", "Obowo", "Oguta", "Ohaji/Egbema", "Okigwe", "Onuimo", "Orlu", "Orsu", "Oru East", "Oru West", "Owerri North", "Owerri Municipal", "Owerri West"),
"Jigawa" to listOf("Auyo", "Babura", "Biriniwa", "Birnin Kudu", "Buji", "Dutse", "Gagarawa", "Garki", "Gumel", "Guri", "Gwaram", "Gwiwa", "Hadejia", "Jahun", "Kafin Hausa", "Kazaure", "Kiri Kasama", "Kiyawa", "Maigatari", "Malam Madori", "Miga", "Ringim", "Roni", "Sule Tankarkar", "Taura", "Yankwashi"),
"Kaduna" to listOf("Birnin Gwari", "Chikun", "Giwa", "Igabi", "Ikara", "Jaba", "Jema’a", "Kachia", "Kaduna North", "Kaduna South", "Kagarko", "Kajuru", "Kaura", "Kauru", "Kubau", "Kudan", "Lere", "Makarfi", "Sabon Gari", "Sanga", "Soba", "Zangon Kataf", "Zaria"),
"Kano" to listOf("Ajingi", "Albasu", "Bagwai", "Bebeji", "Bichi", "Bunkure", "Dala", "Dambatta", "Dawakin Kudu", "Dawakin Tofa", "Doguwa", "Fagge", "Gabasawa", "Garko", "Garun Mallam", "Gaya", "Gezawa", "Gwale", "Gwarzo", "Kabo", "Kano Municipal", "Karaye", "Kibiya", "Kiru", "Kumbotso", "Kunchi", "Kura", "Madobi", "Makoda", "Minjibir", "Nasarawa", "Rano", "Rimin Gado", "Rogo", "Shanono", "Sumaila", "Takai", "Tarauni", "Tofa", "Tsanyawa", "Tudun Wada", "Ungogo", "Warawa", "Wudil"),
"Katsina" to listOf("Bakori", "Batagarawa", "Batsari", "Baure", "Bindawa", "Charanchi", "Dan Musa", "Dandume", "Danja", "Daura", "Dutsi", "Dutsin-Ma", "Faskari", "Funtua", "Ingawa", "Jibia", "Kafur", "Kaita", "Kankara", "Kankia", "Kusada", "Mai’Adua", "Malumfashi", "Mani", "Mashi", "Matazu", "Musawa", "Rimi", "Sabuwa", "Safana", "Sandamu", "Zango", "Kurfi", "Katsina"),
"Kebbi" to listOf("Aleiro", "Arewa Dandi", "Argungu", "Augie", "Bagudo", "Birnin Kebbi", "Bunza", "Dandi", "Fakai", "Gwandu", "Jega", "Kalgo", "Koko/Besse", "Maiyama", "Ngaski", "Sakaba", "Shanga", "Suru", "Wasagu/Danko", "Yauri", "Zuru"),
"Kogi" to listOf("Adavi", "Ajaokuta", "Ankpa", "Bassa", "Dekina", "Ibaji", "Idah", "Igalamela-Odolu", "Ijumu", "Kabba/Bunu", "Kogi", "Lokoja", "Mopa-Muro", "Ofu", "Ogori/Magongo", "Okehi", "Okene", "Olamaboro", "Omala", "Yagba East", "Yagba West"),
"Kwara" to listOf("Asa", "Baruten", "Edu", "Ekiti", "Ifelodun", "Ilorin East", "Ilorin South", "Ilorin West", "Irepodun", "Isin", "Kaiama", "Moro", "Offa", "Oke Ero", "Oyun", "Patigi"),
"Lagos" to listOf("Agege", "Ajeromi-Ifelodun", "Alimosho", "Amuwo-Odofin", "Apapa", "Badagry", "Epe", "Eti-Osa", "Ibeju-Lekki", "Ifako-Ijaiye", "Ikeja", "Ikorodu", "Kosofe", "Lagos Island", "Lagos Mainland", "Mushin", "Ojo", "Oshodi-Isolo", "Shomolu", "Surulere"),
"FCT" to listOf("Abaji", "Abuja Municipal", "Bwari", "Gwagwalada", "Kuje", "Kwali")
)

// 🌍 GHANA
val ghanaRegions = mapOf(
"Greater Accra" to listOf("Accra", "Tema", "Ashaiman", "Ga West", "Ga South"),
"Ashanti" to listOf("Kumasi", "Obuasi", "Ejisu", "Bekwai"),
"Northern" to listOf("Tamale", "Savelugu", "Yendi", "Walewale"),
"Central" to listOf("Cape Coast", "Elmina", "Mankessim", "Winneba"),
"Western" to listOf("Takoradi", "Sekondi", "Tarkwa", "Axim")
)

// 🇰🇪 KENYA
val kenyaCounties = mapOf(
"Nairobi" to listOf("Westlands", "Kibra", "Langata", "Kasarani", "Embakasi"),
"Mombasa" to listOf("Nyali", "Likoni", "Kisauni", "Changamwe"),
"Kiambu" to listOf("Thika", "Ruiru", "Limuru", "Githunguri"),
"Nakuru" to listOf("Naivasha", "Nakuru Town", "Gilgil", "Molo"),
"Kisumu" to listOf("Kisumu Central", "Nyando", "Muhoroni")
)

// 🇿🇦 SOUTH AFRICA
val southAfricaProvinces = mapOf(
"Gauteng" to listOf("Johannesburg", "Pretoria", "Soweto"),
"Western Cape" to listOf("Cape Town", "Stellenbosch", "Paarl"),
"KwaZulu-Natal" to listOf("Durban", "Pietermaritzburg", "Richards Bay")
)

// 🇪🇬 EGYPT
val egyptGovernorates = mapOf(
"Cairo" to listOf("Nasr City", "Heliopolis", "Maadi"),
"Alexandria" to listOf("Montaza", "Borg El Arab", "Sidi Gaber"),
"Giza" to listOf("6th of October", "Dokki", "Mohandessin")
)

// 🇪🇹 ETHIOPIA
val ethiopiaRegions = mapOf(
"Addis Ababa" to listOf("Bole", "Yeka", "Arada", "Nifas Silk"),
"Oromia" to listOf("Adama", "Jimma", "Shashamane", "Bishoftu"),
"Amhara" to listOf("Bahir Dar", "Gondar", "Debre Markos")
)

// 🇹🇿 TANZANIA
val tanzaniaRegions = mapOf(
"Dar es Salaam" to listOf("Ilala", "Kinondoni", "Temeke"),
"Arusha" to listOf("Arusha City", "Meru", "Monduli"),
"Kilimanjaro" to listOf("Moshi", "Hai", "Rombo")
)

// 🇺🇬 UGANDA
val ugandaDistricts = mapOf(
"Kampala" to listOf("Makindye", "Rubaga", "Nakawa", "Kawempe"),
"Wakiso" to listOf("Entebbe", "Kira", "Nansana"),
"Mukono" to listOf("Mukono Town", "Seeta", "Namataba")
)

// 🇷🇼 RWANDA
val rwandaProvinces = mapOf(
"Kigali" to listOf("Gasabo", "Kicukiro", "Nyarugenge"),
"Eastern Province" to listOf("Rwamagana", "Nyagatare", "Kayonza"),
"Northern Province" to listOf("Musanze", "Gakenke", "Burera")
)

// 🇨🇲 CAMEROON
val cameroonRegions = mapOf(
"Centre" to listOf("Yaoundé", "Obala", "Mbalmayo"),
"Littoral" to listOf("Douala", "Manoka", "Nkongsamba"),
"North West" to listOf("Bamenda", "Ndop", "Kumbo")
)

// 🇸🇳 SENEGAL

val senegalRegions = mapOf(
"Dakar" to listOf("Dakar", "Pikine", "Guediawaye"),
"Thies" to listOf("Thies City", "Tivaouane", "Mbour"),
"Saint-Louis" to listOf("Saint-Louis", "Richard-Toll", "Dagana")
)

/**

Returns the appropriate state/town map for the given country.
*/
fun getStatesForCountry(country: String): Map<String, List<String>> = when (country) {
"Nigeria" -> nigeriaStatesAndTowns
"Ghana" -> ghanaRegions
"Kenya" -> kenyaCounties
"South Africa" -> southAfricaProvinces
"Egypt" -> egyptGovernorates
"Ethiopia" -> ethiopiaRegions
"Tanzania" -> tanzaniaRegions
"Uganda" -> ugandaDistricts
"Rwanda" -> rwandaProvinces
"Cameroon" -> cameroonRegions
"Senegal" -> senegalRegions
else -> emptyMap()
}
}



