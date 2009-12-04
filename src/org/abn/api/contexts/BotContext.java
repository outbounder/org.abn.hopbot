package org.abn.api.contexts;

import org.abn.api.contexts.bot.OperationContext;
import org.abn.api.contexts.bot.OperationResultContext;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.w3c.dom.Node;

public abstract class BotContext implements MessageListener, ChatManagerListener
{
	protected ConnectionContext connection;

	public BotContext(String server,String username,String password)
	{
		this.connection = new ConnectionContext(server,username,password);
	}

	public OperationContext parseOperation(Node operation)
	{
		return new OperationContext(operation);
	}

	public OperationResultContext parseOperationResult(Node operation)
	{
		return new OperationResultContext(operation);
	}

	public OperationResultContext createResult(OperationContext operation)
	{
		OperationResultContext result = new OperationResultContext();
		result.id = operation.id;
		return result;
	}
}
