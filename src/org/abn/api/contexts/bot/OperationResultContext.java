package org.abn.api.contexts.bot;

import org.w3c.dom.Node;

public class OperationResultContext
{
	public String id;
	public String value;

	public OperationResultContext()
	{

	}

	public OperationResultContext(Node node)
	{
		for(int i = 0;i<node.getChildNodes().getLength(); i++)
		{
			Node n = node.getChildNodes().item(i);
			if(n.getNodeName() == "id")
				this.id = n.getTextContent();
			if(n.getNodeName() == "value")
				this.value = n.getTextContent();
		}
	}

	public String toString()
	{
		return "<operationresult><id>"+this.id+"</id><value>"+this.value+"</value></operationresult>";
	}
}
