package handDetector

import client.Client
import client.ClientsContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HandDetector(
    private val coroutineScope: CoroutineScope,
    private val clientsContext: ClientsContext
) {
    lateinit var client: Client
    private val _connectStatus = MutableStateFlow(false)
    val connectStatus: StateFlow<Boolean> = _connectStatus
    val handName = "test2"

    private fun connectStatusCollect() {
        coroutineScope.launch {
            if (::client.isInitialized) {
                client.isConnect.collect {
                    if (_connectStatus.value && !it) {
                        _connectStatus.value = false
                        this.cancel()
                    }
                    _connectStatus.value = it
                }
            }
        }
    }

    init {
        if (clientsContext.getClientsName().contains(handName)) {
            client = clientsContext.getClient(handName)
            connectStatusCollect()
        }

    }

    fun connect(ip: String, port: String) {

        if (!clientsContext.getClientsName().contains(handName)) {
            clientsContext.addClient(handName, ip, port.trim().toInt())
            client = clientsContext.getClient(handName)
        }
        if (!client.isConnect.value) {
            client.connect()
        }
        connectStatusCollect()
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

    fun isInit(): Boolean {
        return ::client.isInitialized
    }
}