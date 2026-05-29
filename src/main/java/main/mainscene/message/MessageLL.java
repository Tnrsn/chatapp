package main.mainscene.message;

public class MessageLL { //LINKED LIST
	private MessageNode head;
	private MessageNode tail;
	
	public void add(Message message)
	{
		MessageNode node = new MessageNode(message);
		
		if(head == null)
		{
			head = tail = node;
			return;
		}
		
		tail.setNext(node);
		node.setPrev(tail);
		tail = node;
	}
	
	public MessageNode getHead()
	{
		return head;
	}
}
