import org.abn.hopbot.HopBot;
import org.jivesoftware.smack.XMPPConnection;

public class Main
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		XMPPConnection.DEBUG_ENABLED = true;

		try
		{
			HopBot instance = new HopBot(args[0],args[1],args[2]);
			instance.start(args[3]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
