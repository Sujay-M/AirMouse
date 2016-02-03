package airmousedesktop;

import java.net.*;
import java.io.*;
import java.lang.Thread;
import java.lang.Runnable;

public class Server
{
	interface IGetMessage
	{
		void msgReceived(String msg);
	}

	private ServerSocket connectionSocket;
	private Socket serverSocket;
	private BufferedReader reciever;
	private static final int port = 11111;
	Thread receiverThread;
	IGetMessage callback;

	public Server(IGetMessage callback)
	{
		this.callback = callback;
		try
        {
            connectionSocket = new ServerSocket(port);
        }
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
	}	

	public void start()
    {
    	
    	receiverThread = new Thread(new Runnable()
    	{
    		@Override
    		public void run()
    		{
    			try
    			{
    				serverSocket = connectionSocket.accept();
    				reciever = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
    				while (!Thread.interrupted())
    				{
    					final String msg = reciever.readLine();
    					if (msg==null)
    						break;
    					new Thread(new Runnable()
    					{
    						@Override
    						public void run()
    						{
    							callback.msgReceived(msg);
    						}
    					}).start();
    				}
    			}
    			catch(SocketException e)
    			{
    			    System.out.println("Something went wrong : "+e.getMessage());
    			    e.printStackTrace();
    			}
    			catch(IOException e)
    			{
    			    System.out.println("Something went wrong : "+e.getMessage());
    			    e.printStackTrace();
    			}
    		}
    	});
    	receiverThread.start();
    }
	
	public void stop()
	{
		receiverThread.interrupt();
		try 
		{
			reciever.close();
			serverSocket.close();
			connectionSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void waitForServer()
	{
		try {
			receiverThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}