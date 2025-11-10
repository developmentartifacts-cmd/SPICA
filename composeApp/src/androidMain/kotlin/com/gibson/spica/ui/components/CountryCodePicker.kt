package com.gibson.spica.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.data.CountryCodeData
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CountryPicker(
    selectedCountryIso: String?,
    onSelect: (iso: String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val displayName = CountryCodeData.findByIso(selectedCountryIso ?: "")?.name ?: "Select Country"

    Box {
        Surface(
            shape = RoundedCornerShape(50),
            tonalElevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { showDialog = true }
        ) {
            Row(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(displayName)
                Spacer(Modifier.weight(1f))
                Text("▾", style = MaterialTheme.typography.bodyLarge)
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Select country") },
                text = {
                    Column(Modifier.fillMaxWidth()) {
                        CountryCodeData.list.forEach { c ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSelect(c.iso)
                                        showDialog = false
                                    }
                                    .padding(vertical = 12.dp)
                            ) {
                                Text("${c.name} (${c.dialCode})")
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Close") }
                }
            )
        }
    }
}

@Composable
fun CountryCodePicker(
    selectedDialCode: String,
    onDialCodeSelected: (String) -> Unit
) {
    var show by remember { mutableStateOf(false) }
    val label = CountryCodeData.findByDial(selectedDialCode)?.dialCode ?: selectedDialCode

    Box {
        Surface(
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .height(56.dp)
                .width(IntrinsicSize.Min)
                .clickable { show = true }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(label, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(8.dp))
                Text("▾")
            }
        }

        if (show) {
            AlertDialog(
                onDismissRequest = { show = false },
                title = { Text("Select country code") },
                text = {
                    Column {
                        CountryCodeData.list.forEach { c ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onDialCodeSelected(c.dialCode)
                                        show = false
                                    }
                                    .padding(vertical = 12.dp)
                            ) {
                                Text("${c.name} ${c.dialCode}")
                            }
                        }
                    }
                },
                confirmButton = { TextButton(onClick = { show = false }) { Text("Close") } }
            )
        }
    }
}
