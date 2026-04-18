package br.edu.utfpr.client_socket

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.net.Socket
import java.nio.charset.Charset
import java.util.Timer
import java.util.TimerTask

class MyService : Service() {

    private lateinit var clientSocket: Socket
    private lateinit var inputStream: BufferedReader
    private lateinit var outputStream: BufferedWriter

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val timer = Timer()
        timer.schedule(MinhaTimerTask("hora"), 0, 1000)

        return START_NOT_STICKY
    }

    fun conexaoTask(protocol: String) {

        var result = ""

        try {

            if (!::clientSocket.isInitialized) {
                val ip = BuildConfig.SERVER_IP
                val port = BuildConfig.SERVER_PORT

                clientSocket = Socket(ip, port) //linha é bloqueante ou dará exceção
                //Conectado com o Server

                outputStream =
                    clientSocket.getOutputStream().bufferedWriter(Charset.forName("utf-8"))
                inputStream =
                    clientSocket.getInputStream().bufferedReader(Charset.forName("utf-8"))
                //Fluxo de IO Criado
            }


            outputStream.write (protocol+ "\n")
            outputStream.flush()
            //Mensagem enviada ao servidor, sem bloqueios

            result = inputStream.readLine() //linha bloqueante
                //mensagem recebida do servidor
        } catch (e: Exception) {
            result = e.message.toString()
        }


    } //fim conexaoTask

    inner class MinhaTimerTask(msg: String) : TimerTask() {

        override fun run() {
            conexaoTask("hora")
        }
    }


} //fim do MyService