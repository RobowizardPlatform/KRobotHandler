package ui.settings

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import robot.RRobot

@Composable
fun RobotSetting(
    robot: RRobot,
) {
    SettingSurface(
        header = "Robot settings",
    ) {
        val status = robot.connectStatus.collectAsState()

        val ip = remember { mutableStateOf(robot.ipDefault) }
        OutlinedTextField(
            value = ip.value,
            onValueChange = {
                ip.value = it
            },
            label = {
                Text("IP")
            }
        )

        val port = remember { mutableStateOf(robot.portDefault.toString()) }
        OutlinedTextField(
            value = port.value,
            onValueChange = {
                port.value = it
            },
            label = {
                Text("Port")
            }
        )

        // Connect/Disconnect
        val text = if (!status.value) "Connect" else "DisConnect"
        val onClick = {
            if (!status.value) {
                robot.connect(ip.value, robotPort = port.value.toIntOrNull())
            } else {
                robot.disconnect()
            }
        }
        SettingLine("Connect to the robot", text, onClick)

        // Add points
        SettingLine("Remember first limit point", "Remember") { robot.rememberPoint("firstLimitPoint") }
        SettingLine("Remember second limit point", "Remember") { robot.rememberPoint("secLimitPoint") }
    }
}