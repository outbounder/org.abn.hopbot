package org.abn.api.contexts;

import java.util.HashMap;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

public class XMPPContext
{
	private XMPPConnection conn;
	private HashMap<String, Chat> connections;

	public XMPPContext(String host)
	{
		ConnectionConfiguration config = new ConnectionConfiguration(host, 5222);
		config.setCompressionEnabled(true);
		config.setSASLAuthenticationEnabled(true);
		SASLAuthentication.supportSASLMechanism("PLAIN", 0);

		this.conn = new XMPPConnection(config);
		this.connections = new HashMap<String, Chat>();
	}

	public void addListener(ChatManagerListener listener)
	{
		this.conn.getChatManager().addChatListener(listener);
	}

	public String login(String username,String password) throws Exception
	{
		this.conn.login(username, password);
		Presence p = new Presence(Presence.Type.available);
		p.setStatus("Let's Talk!");
		this.conn.sendPacket(p);
		return this.conn.getUser();
	}

	public void connect() throws Exception
	{
		this.conn.connect();
	}

	public boolean isConnected()
	{
		return this.conn.isConnected();
	}

	public void close()
	{
		this.conn.disconnect();
	}

	public void send(String to, String operation, MessageListener listener) throws Exception
	{
		Chat chat;

		if(!this.connections.containsKey(to))
			chat = this.conn.getChatManager().createChat(to, listener);
		else
			chat = this.connections.get(to);

		chat.sendMessage(operation);
	}
}
