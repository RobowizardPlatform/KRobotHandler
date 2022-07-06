package ui.settings

import androidx.compose.runtime.Composable
import handDetector.HandDetector

@Composable
fun HandDetectorSetting(
    handDetector: HandDetector,
) {
    SettingSurface(
        header = "Hand detector settings",
    ) {
        // Connect/Disconnect
        val text = if (!handDetector.isConnect.value) "Connect" else "DisConnect"
        val onClick = {
            if (!handDetector.isConnect.value) {
                handDetector.connect()
            } else {
                handDetector.disconnect()
            }
        }
        SettingLine("Connect to the hand detector", text, onClick)
    }
}