package org.abn.api.contexts;

import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;

public class ConnectionContext
{
	private XMPPContext connectionContext = null;

	private String username = null;
	private String password = null;

	public ConnectionContext(String sever,String username,String password)
	{
		this.username = username;
		this.password = password;
		this.connectionContext = new XMPPContext(sever);
	}

	public String getUsername()
	{
		return this.username;
	}

	public void open(ChatManagerListener listener) throws Exception
	{
		this.connectionContext.connect();
		this.connectionContext.addListener(listener);
		this.username = this.connectionContext.login(this.username,this.password);
		System.out.println("connected");
	}

	public boolean isActive()
	{
		return this.connectionContext.isConnected();
	}

	public void send(String to,String msg,MessageListener listener) throws Exception
	{
		this.connectionContext.send(to, msg, listener);
	}

	public void close()
	{
		this.connectionContext.close();
	}
}
