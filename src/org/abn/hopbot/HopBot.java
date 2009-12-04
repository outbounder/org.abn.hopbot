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
		try
		{
			c.sendMessage("greeting - "+this.connection.getUsername()+" - ");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void processMessage(Chat fromChat, Message msg)
	{
		if(msg.getBody() == "quit")
		{
			this.connection.close();
			return;
		}


		try
		{
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(msg.getBody()));
			Document doc = this.domBuilder.parse(inStream);

			if(msg.getFrom() != this.defaultTarget) // master is calling
			{
				OperationContext operation = this.parseOperation(doc.getFirstChild());
				if(operation.id == null)
				{
					System.out.println("could not recognize operation "+operation.toString());
					return;
				}

				this.masters.put(operation.id, msg.getFrom()); // save from who is coming from

				String depth = String.valueOf(Integer.parseInt(operation.args.get("value"))+1);
				System.out.println("DEPTH CURRENT "+depth);
				operation.args.put("value", depth);
				if(this.defaultTarget != "endpoint")
				{
					this.connection.send(this.defaultTarget, operation.toString(), this);
				}
				else
				{
					OperationResultContext operationResult = this.createResult(operation);
					operationResult.value = depth;
					this.connection.send(msg.getFrom(), operationResult.toString(), this);
				}
			}
			else // slave is resuling
			{
				OperationResultContext operationResult = this.parseOperationResult(doc.getFirstChild());
				if(operationResult.id == null)
					return;
				String to = this.masters.get(operationResult.id);
				this.connection.send(to, operationResult.toString(), this);
			}
		}
		catch(Exception e)
		{
			System.err.println("could not recognize operation "+msg.getBody());
			e.printStackTrace();
		}
	}



}
