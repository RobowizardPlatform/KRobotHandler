package handDetector

import client.Client
import client.ClientsContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HandDetector(
    private val coroutineScope: CoroutineScope,
    private val clientsContext: ClientsContext,
    var ipDefault: String = "localhost",
    var portDefault: Int = 9999,
) {
    lateinit var client: Client
    private val _connectStatus = MutableStateFlow(false)
    val connectStatus: StateFlow<Boolean> = _connectStatus

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
        val _client = clientsContext.isConnected(ipDefault, 9999)
        if (_client != null) {
            client = _client
            connectStatusCollect()
        }
    }

    fun connect(ip: String, port: String) {
        ipDefault = ip
        portDefault = port.toIntOrNull() ?: 9999

        client = clientsContext.connect(ipDefault, portDefault)
        connectStatusCollect()
    }

    fun disconnect() {
        clientsContext.disconnect(ipDefault, portDefault)
    }

    fun isInit(): Boolean {
        return ::client.isInitialized
    }
}