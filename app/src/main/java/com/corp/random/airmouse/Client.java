package com.corp.random.airmouse;

import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sujay on 1/2/16.
 */
public class Client
{
    private Socket clientSocket;
    private PrintWriter clientOutput;
    private static final int port = 11111;
    private ExecutorService executorService;
    private boolean connected;

    public Client(String IP)
    {
        executorService = Executors.newSingleThreadExecutor();
        connected = false;
        connect(IP);
    }

    private void connect(final String IP)
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    InetAddress serverAddr = InetAddress.getByName(IP);
                    clientSocket = new Socket(serverAddr, port);
                    clientOutput = new PrintWriter(clientSocket.getOutputStream(),true);
                    connected = true;
                }
                catch (UnknownHostException e)
                {
                    Log.e("CLIENT", "Host Unreachable");
                }
                catch (IOException e)
                {
                    Log.e("CLIENT", "Cant Connect");
                }

            }
        }).start();
    }

    public void send(final String msg)
    {
        if (connected)
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    clientOutput.println(msg);
                }
            });
    }

    public boolean isConnected()
    {
        return connected;
    }

    public void disConnect()
    {
        if(connected)
        {
            clientOutput.close();
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
    }


}
