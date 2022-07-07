package ui.settings

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

        val ip = remember { mutableStateOf("localhost") }
        OutlinedTextField(
            value = ip.value,
            onValueChange = {
                ip.value = it
            },
            label = {
                Text("IP")
            }
        )

        val port = remember { mutableStateOf("9999") }
        OutlinedTextField(
            value = port.value,
            onValueChange = {
                port.value = it
            },
            label = {
                Text("Port")
            }
        )

        val onClick = {
            if (!status.value) {
                handDetector.connect(ip.value, port.value)
            } else {
                handDetector.disconnect()
            }
        }
        SettingLine("Connect to the hand detector", text, onClick)
    }
}