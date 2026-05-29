package main.mainscene.message;

public class MessageNode {
	private Message message;
	private MessageNode next;
	private MessageNode prev;
	
	public MessageNode(Message message)
	{
		this.message = message;
	}
	
	public Message getMessage()
	{
		return message;
	}
	
	public MessageNode getNext()
	{
		return next;
	}
	
	public void setNext(MessageNode node)
	{
		next = node;
	}
	
	public MessageNode getPrev()
	{
		return prev;
	}
	
	public void setPrev(MessageNode node)
	{
		prev = node;
	}
}
