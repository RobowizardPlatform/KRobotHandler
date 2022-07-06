package ui.settings

import androidx.compose.runtime.Composable
import robot.RRobot

@Composable
fun RobotSetting(
    RRobot: RRobot,
) {
    SettingSurface(
        header = "Robot settings",
    ) {
        // Connect/Disconnect
        val text = if (!RRobot.isConnect.value) "Connect" else "DisConnect"
        val onClick = {
            if (!RRobot.isConnect.value) {
                RRobot.connect()
            } else {
                RRobot.disconnect()
            }
        }
        SettingLine("Connect to the robot", text, onClick)

        // Add points
        SettingLine("Remember first limit point", "Remember") { RRobot.rememberPoint("firstLimitPoint") }
        SettingLine("Remember second limit point", "Remember") { RRobot.rememberPoint("secLimitPoint") }
    }
}