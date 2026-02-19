package com.skintostatue.android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skintostatue.android.core.Config
import com.skintostatue.android.core.SkinToStatueConverter
import com.skintostatue.android.core.processor.ArmorMaterial
import com.skintostatue.android.core.processor.ArmorPiece
import com.skintostatue.android.ui.screens.HomeScreen
import com.skintostatue.android.ui.screens.SettingsScreen
import com.skintostatue.android.ui.theme.SkinToStatueTheme
import kotlinx.coroutines.launch

/**
 * Main activity
 * Entry point for the Skin to Statue Android app
 * Modern clean design with Material Design 3
 */
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.onSkinSelected(it)
            viewModel.requestUriPermission(it)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onPermissionResult(isGranted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check and request storage permission
        checkStoragePermission()

        setContent {
            SkinToStatueTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation(
                        navController = rememberNavController(),
                        viewModel = viewModel,
                        onPickImage = { pickImageLauncher.launch("image/*") },
                        onRequestPermission = { requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE) }
                    )
                }
            }
        }
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.requestPermission()
        }
    }
}

/**
 * Main navigation
 * Handles navigation between screens
 */
@Composable
fun MainNavigation(
    navController: NavHostController,
    viewModel: MainViewModel,
    onPickImage: () -> Unit,
    onRequestPermission: () -> Unit
) {
    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToSettings = {
                    navController.navigate("settings")
                },
                onSkinSelected = {
                    onPickImage()
                },
                onConvertClicked = {
                    viewModel.convertSkin()
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

/**
 * Main view model
 * Manages app state and business logic
 */
class MainViewModel : ViewModel() {
    var selectedSkinUri: Uri? = null
        private set

    var isConverting: Boolean = false
        private set

    var conversionResult: String? = null
        private set

    private val converter = SkinToStatueConverter()

    fun onSkinSelected(uri: Uri) {
        selectedSkinUri = uri
    }

    fun requestUriPermission(uri: Uri) {
        // Request persistent URI permission
        viewModelScope.launch {
            try {
                // This would be implemented in a real app
                // to grant persistent permission to the URI
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun requestPermission() {
        // Trigger permission request from UI
    }

    fun onPermissionResult(isGranted: Boolean) {
        if (!isGranted) {
            // Handle permission denied
        }
    }

    fun convertSkin() {
        val uri = selectedSkinUri ?: return

        isConverting = true

        viewModelScope.launch {
            try {
                // Convert skin
                val result = converter.convert(
                    // Load bitmap from URI
                    android.graphics.BitmapFactory.decodeStream(
                        android.content.ContentResolver.openInputStream(uri)
                    ) ?: throw Exception("Failed to load skin")
                )

                conversionResult = "Converted ${result.blockCount} blocks (${result.uniqueBlockCount} unique)"
            } catch (e: Exception) {
                conversionResult = "Error: ${e.message}"
            } finally {
                isConverting = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        converter.clear()
    }
}