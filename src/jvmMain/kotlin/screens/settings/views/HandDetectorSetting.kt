package ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import handDetector.HandDetector

@Composable
fun HandDetectorSetting(
    handDetector: HandDetector,
) {
    SettingSurface(
        header = "Hand detector settings",
    ) {
        val status = handDetector.connectStatus.collectAsState()
        // Connect/Disconnect
        val text = if (!status.value) {
            "Connect"
        } else {
            "DisConnect"
        }

        val onClick = {
            if (!status.value) {
                handDetector.connect()
            } else {
                handDetector.disconnect()
            }
        }
        SettingLine("Connect to the hand detector", text, onClick)
    }
}