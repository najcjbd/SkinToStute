package com.skintostatue.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skintostatue.android.ui.theme.MinecraftGreen
import com.skintostatue.android.ui.theme.SurfaceColor

/**
 * Settings screen
 * Configuration interface for conversion settings
 * Modern clean design with Material Design 3
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {}
) {
    var colorMode by remember { mutableStateOf("LAB") }
    var blockCategories by remember { mutableStateOf(setOf("Wool", "Concrete")) }
    var outputFormat by remember { mutableStateOf("Sponge Schematic") }
    var scale by remember { mutableStateOf(1.0f) }
    var includeArmor by remember { mutableStateOf(false) }
    var armorMaterial by remember { mutableStateOf("Diamond") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MinecraftGreen
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Color Mode Section
            SettingsSection(
                title = "Color Mode",
                icon = Icons.Default.Palette
            ) {
                ColorModeSelector(
                    selectedMode = colorMode,
                    onModeSelected = { colorMode = it }
                )
            }

            // Block Categories Section
            SettingsSection(
                title = "Block Types",
                icon = Icons.Default.Cube
            ) {
                BlockCategorySelector(
                    selectedCategories = blockCategories,
                    onCategoryToggled = { category, selected ->
                        val newSet = blockCategories.toMutableSet()
                        if (selected) {
                            newSet.add(category)
                        } else {
                            newSet.remove(category)
                        }
                        blockCategories = newSet
                    }
                )
            }

            // Output Format Section
            SettingsSection(
                title = "Output Format",
                icon = Icons.Default.FileDownload
            ) {
                OutputFormatSelector(
                    selectedFormat = outputFormat,
                    onFormatSelected = { outputFormat = it }
                )
            }

            // Statue Scale Section
            SettingsSection(
                title = "Statue Scale",
                icon = Icons.Default.ZoomIn
            ) {
                ScaleSlider(
                    value = scale,
                    onValueChange = { scale = it }
                )
            }

            // Armor Section
            SettingsSection(
                title = "Armor",
                icon = Icons.Default.Shield
            ) {
                ArmorSettings(
                    includeArmor = includeArmor,
                    onIncludeArmorChanged = { includeArmor = it },
                    armorMaterial = armorMaterial,
                    onMaterialSelected = { armorMaterial = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MinecraftGreen,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = SurfaceColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun ColorModeSelector(
    selectedMode: String,
    onModeSelected: (String) -> Unit
) {
    val modes = listOf("RGB", "ABSRGB", "HSL", "HSB", "LAB")

    Column {
        modes.forEach { mode ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (selectedMode == mode) {
                            MinecraftGreen.copy(alpha = 0.1f)
                        } else {
                            Color.Transparent
                        }
                    )
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedMode == mode,
                    onClick = { onModeSelected(mode) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MinecraftGreen
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    mode,
                    style = MaterialTheme.typography.bodyLarge
                )

                if (mode == selectedMode) {
                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MinecraftGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BlockCategorySelector(
    selectedCategories: Set<String>,
    onCategoryToggled: (String, Boolean) -> Unit
) {
    val categories = listOf(
        "Wool", "Concrete", "Terracotta", "Planks", "Glass", "Gray", "All"
    )

    Column {
        categories.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { category ->
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (category in selectedCategories) {
                                    MinecraftGreen.copy(alpha = 0.1f)
                                } else {
                                    Color.Transparent
                                }
                            )
                            .padding(vertical = 8.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = category in selectedCategories,
                            onCheckedChange = { onCategoryToggled(category, it) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MinecraftGreen
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            category,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (row.indexOf(category) < row.size - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }

            if (categories.indexOf(row.last()) < categories.size - 1) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun OutputFormatSelector(
    selectedFormat: String,
    onFormatSelected: (String) -> Unit
) {
    val formats = listOf(
        "Sponge Schematic" to ".schem",
        "Litematica" to ".litematic",
        "McStructure" to ".mcstructure"
    )

    Column {
        formats.forEach { (name, extension) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (selectedFormat == name) {
                            MinecraftGreen.copy(alpha = 0.1f)
                        } else {
                            Color.Transparent
                        }
                    )
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedFormat == name,
                    onClick = { onFormatSelected(name) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MinecraftGreen
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        extension,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (selectedFormat == name) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MinecraftGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ScaleSlider(
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Scale: ${String.format("%.1fx", value)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            Text(
                "${(value * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0.5f..2.0f,
            steps = 15,
            colors = SliderDefaults.colors(
                activeTrackColor = MinecraftGreen,
                thumbColor = MinecraftGreen
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "0.5x",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                "2.0x",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ArmorSettings(
    includeArmor: Boolean,
    onIncludeArmorChanged: (Boolean) -> Unit,
    armorMaterial: String,
    onMaterialSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            checked = includeArmor,
            onCheckedChange = onIncludeArmorChanged,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MinecraftGreen,
                checkedTrackColor = MinecraftGreen.copy(alpha = 0.5f)
            )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Include Armor",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            if (includeArmor) {
                Spacer(modifier = Modifier.height(4.dp))

                var expanded by remember { mutableStateOf(false) }
                val materials = listOf(
                    "Leather", "Chainmail", "Iron", "Gold",
                    "Diamond", "Netherite", "Turtle"
                )

                Box {
                    Button(
                        onClick = { expanded = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                armorMaterial,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        materials.forEach { material ->
                            DropdownMenuItem(
                                text = {
                                    Text(material)
                                },
                                onClick = {
                                    onMaterialSelected(material)
                                    expanded = false
                                },
                                trailingIcon = if (armorMaterial == material) {
                                    {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = MinecraftGreen
                                        )
                                    }
                                } else null
                            )
                        }
                    }
                }
            }
        }
    }
}