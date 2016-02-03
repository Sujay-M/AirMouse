package airmousedesktop;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.InputEvent;


class AirMouse implements Server.IGetMessage
{
	private Server server;
	private int x, y;
	private Robot robot;
	enum Button	{ LEFT, RIGHT }

	private final int MAX_X;
	private final int MAX_Y;
	private final int MIN_X;
	private final int MIN_Y;

	public AirMouse() throws AWTException
	{
		server = new Server(this);
		server.start();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		MAX_X = (int)screenSize.getWidth();
		MAX_Y = (int)screenSize.getHeight();
		MIN_X = 0;
		MIN_Y = 0;
		x = MAX_X/2;
		y = MAX_Y/2;
		robot = new Robot();
		move (0,0);
	}

	public void move(int dx,int dy)
	{
		x = (x+dx)>MAX_X?MAX_X:((x+dx)<MIN_X?MIN_X:x+dx);
		y = (y+dy)>MAX_Y?MAX_Y:((y+dy)<MIN_Y?MIN_Y:y+dy);
		robot.mouseMove(x,y);
	}

	private void clickButton(Button b)
	{
		switch(b)
		{
			case LEFT:
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				break;
			case RIGHT:
				robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
				robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
				break;
		}
	}	
	
	private void processSensorData(float[] values)
	{
		float magnitude = (float)Math.sqrt(values[0]*values[0] + 
							values[1]*values[1] + values[2]*values[2]);
		if(magnitude < 0.25)
			return;
		final float sensitivity = 20.0f;
		int movex = (int)(values[0]*sensitivity);
		int movey = -(int)(values[2]*sensitivity);
		move(movex,movey);
	}

	@Override
	public void msgReceived(String msg)
	{
		String dataParts[] = msg.split(" ");
		if(dataParts.length==1)
			switch(dataParts[0])
			{
				case "LEFT":
					clickButton(Button.LEFT);
					break;
				case "RIGHT":
					clickButton(Button.RIGHT);
					break;
			}
		else
		{
			float[] values = new float[dataParts.length];
			for (int i = 0;i<dataParts.length;i++)
				values[i] = Float.parseFloat(dataParts[i]);
			processSensorData(values);
		}
	}
	
	public void waitForServer()
	{
		server.waitForServer();
	}

	public static void main(String []args) throws AWTException
	{
		AirMouse a = new AirMouse();
		System.out.println("Server Started");
		a.waitForServer();
	}
}