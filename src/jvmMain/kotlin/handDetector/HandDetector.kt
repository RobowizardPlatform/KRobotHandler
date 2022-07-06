package handDetector

import client.Client
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HandDetector(
    private val coroutineScope: CoroutineScope,
    private val client: Client
) {
    val isConnect = client.isConnect

    fun connect() {
        client.connect()
    }

    fun disconnect() {
        client.disconnect()
    }

    fun startFlow(
        onRead: (String) -> Unit
    ) {
        coroutineScope.launch(Dispatchers.IO) {
            client.dataHandler.collect {
                onRead(it)
            }
        }
    }
}