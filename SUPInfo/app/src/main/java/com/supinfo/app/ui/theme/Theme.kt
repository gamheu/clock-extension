package com.supinfo.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary       = OceanMid,
    onPrimary     = SurfaceColor,
    primaryContainer   = SkyBlue,
    onPrimaryContainer = OceanDeep,
    secondary     = WaveTeal,
    onSecondary   = SurfaceColor,
    background    = SkyBlue,
    surface       = SurfaceColor,
    onBackground  = OceanDeep,
    onSurface     = OceanDeep
)

private val DarkColors = darkColorScheme(
    primary       = OceanLight,
    onPrimary     = OceanDeep,
    primaryContainer   = OceanDeep,
    onPrimaryContainer = OceanLight,
    secondary     = WaveTeal,
    background    = OceanDeep,
    surface       = OceanMid,
    onBackground  = SkyBlue,
    onSurface     = SkyBlue
)

@Composable
fun SUPInfoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
