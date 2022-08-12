package ui.settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import handDetector.HandDetector
import robot.RRobot

@Composable
fun SettingScreen(
    onBack: () -> Unit,
    handDetector: HandDetector,
    RRobot: RRobot,
) {
    LazyColumn {
        item {
            IconButton(
                onClick = onBack
            ) {
                Icon(Icons.Default.ArrowBack, "Back")
            }
        }
        item {
            HandDetectorSetting(handDetector)
        }

        item {
            RobotSetting(RRobot)
        }
    }
}