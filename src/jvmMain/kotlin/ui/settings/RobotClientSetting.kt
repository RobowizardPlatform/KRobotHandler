package ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import client.Client

@Composable
fun RobotClientSetting(
    robot: Client,
) {
    SettingSurface(
        header = "Robot settings",
    ) {
        val robotState = robot.isConnect.collectAsState()
        // Connect/Disconnect
        val text = if (!robotState.value) "Connect" else "DisConnect"
        val onClick = {
            if (!robotState.value) {
                robot.connect()
            } else {
                robot.disconnect()
            }
        }
        SettingLine("Connect to the robot", text, onClick)
    }
}