package ui.settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
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
            Button(
                onClick = onBack
            ) {
                Text("Back")
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