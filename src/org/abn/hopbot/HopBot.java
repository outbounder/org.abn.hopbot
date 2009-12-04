package org.abn.hopbot;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.abn.api.contexts.BotContext;
import org.abn.api.contexts.bot.OperationContext;
import org.abn.api.contexts.bot.OperationResultContext;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.omg.CORBA.SystemException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class HopBot extends BotContext
{
	DocumentBuilderFactory domFactory;
	DocumentBuilder domBuilder;

	private String defaultTarget;
	private HashMap<String, String> masters = new HashMap<String, String>();

	public HopBot(String server,String username,String password) throws Exception
	{
		super(server,username, password);
		this.domFactory = DocumentBuilderFactory.newInstance();
		this.domBuilder = this.domFactory.newDocumentBuilder();
	}

	public void start(String to) throws Exception
	{
		this.defaultTarget = to;
		this.connection.open(this);
		while (connection.isActive()) Thread.sleep(50);
	}

	public void chatCreated(Chat c, boolean l)
	{
		c.addMessageListener(this);
	}

	public void processMessage(Chat fromChat, Message msg)
	{
		String body = msg.getBody();
		String from = msg.getFrom();

		if(body.equals("quit"))
		{
			this.connection.close();
			return;
		}


		try
		{
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(body));
			Document doc = this.domBuilder.parse(inStream);

			OperationContext operation = this.parseOperation(doc.getFirstChild());
			if(operation.id == null)
			{
				System.out.println("could not recognize operation "+operation.toString());
				return;
			}

			String depth = String.valueOf(Integer.parseInt(operation.args.get("value"))+1);
			System.out.println("DEPTH CURRENT "+depth);
			operation.args.put("value", depth);

			if(!from.equals(this.defaultTarget))
			{
				this.masters.put(operation.id, from);

				operation.args.put("value", depth);
				this.connection.send(this.defaultTarget, operation.toString(), this);
			}
			else
			{
				String master = this.masters.get(operation.id);
				if(master != null)
				{
					OperationResultContext operationResult = this.parseOperationResult(doc.getFirstChild());
					operationResult.value = depth;
					this.connection.send(master, operationResult.toString(), this);
				}
			}
		}
		catch(Exception e)
		{
			System.err.println("could not recognize operation "+msg.getBody());
			e.printStackTrace();
		}
	}



}
