package org.abn.api.contexts.bot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OperationContext
{
	public String id;
	public String name;
	public HashMap<String,String> args = new HashMap<String, String>();

	public OperationContext()
	{

	}

	public OperationContext(Node node)
	{
		for(int i = 0;i<node.getChildNodes().getLength(); i++)
		{
			Node n = node.getChildNodes().item(i);
			if(n.getNodeName() == "id")
				this.id = n.getTextContent();
			if(n.getNodeName() == "name")
				this.name = n.getTextContent();
			if(n.getNodeName() == "args")
			{
				NodeList args = n.getChildNodes();
				for(int k = 0; k<args.getLength(); k++)
				{
					this.args.put(args.item(k).getNodeName(), args.item(k).getTextContent());
				}
			}

		}
	}

	public String toString()
	{
		String result = "<operation><id>"+this.id+"</id><name>"+this.name+"</name><args>";
		for(Iterator<Entry<String,String>> i = this.args.entrySet().iterator(); i.hasNext();)
		{
			Entry<String,String> entry = i.next();
			result += "<"+entry.getKey()+">"+entry.getValue()+"</"+entry.getKey()+">";
		}
		return result+"</args></operation>";
	}
}
