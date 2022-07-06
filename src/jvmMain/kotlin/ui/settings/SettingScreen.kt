package ui.settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import client.Client
import handDetector.HandDetector
import robot.RRobot

@Composable
fun SettingScreen(
    handDetector: HandDetector,
    RRobot: RRobot,
    client: Client
) {
    LazyColumn {
        item {
            HandDetectorSetting(handDetector)
        }

        item {
            RobotSetting(RRobot)
        }

        item {
            RobotClientSetting(client)
        }
    }
}