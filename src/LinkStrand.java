import java.util.HashMap;
import java.util.Stack;


public class LinkStrand implements IDnaStrand {
	public LinkStrand(String s){
		myFront = new Node(s,null);
		myBack = myFront;
		mySize = s.length();
	}
	public LinkStrand(){
		this("");
		}
	private class Node{
		String value;
		Node next;
		public Node(String s, Node link){
			value = s;
			next = link;
		}
	}
	private Node myFront, myBack;
	private long mySize;
	private int myAppends;


	@Override
	public IDnaStrand cutAndSplice(String enzyme, String splicee) {
		
		if(myFront.next != null) {
			throw new RuntimeException("link strand has more than one node");
		}
		int pos = 0;
		int start = 0;
		StringBuilder search = new StringBuilder(this.myFront.value);
		boolean first = true;
		LinkStrand ret = null;
		while ((pos = search.indexOf(enzyme, pos)) >= 0) {
			if (first){
				ret = new LinkStrand(search.substring(start, pos));
				first = false;
			}
			else {
				ret.append(search.substring(start, pos));

			}
			start = pos + enzyme.length();
			ret.append(splicee);
			pos++;
		}

		if (start < search.length()) {
			if (ret == null){
				ret = new LinkStrand("");
			}
			else {
				ret.append(search.substring(start));
			}
		}
		return ret;
	}

	@Override
	public long size() {
		return mySize;
	}

	@Override
	public void initializeFrom(String source) {
		Node newNode = new Node(source,null);
		myFront = newNode;
		myBack = newNode;
		mySize = source.length();

	}

	@Override
	public String strandInfo() {
		return this.getClass().getName();
	}

	@Override
	public IDnaStrand append(IDnaStrand dna) {
		if (dna instanceof LinkStrand) {
			myBack.next = ((LinkStrand) dna).myFront;
			myBack = ((LinkStrand) dna).myBack;
			myAppends++;
		}
		if (dna instanceof SimpleStrand){
			append(dna.toString());

		}
		return this;
	}

	@Override
	public IDnaStrand append(String dna) {
		Node newNode = new Node(dna,null);
		myBack.next = newNode;
		myBack = newNode;
		mySize += dna.length();
		myAppends++;
		return this;
	}

	@Override
	public IDnaStrand reverse() {
		HashMap<String,String> reverse = new HashMap<String,String>();
		Stack<String> stack = new Stack<String>();
		Node current = myFront;
		while(current != null){
			if(!reverse.containsKey(current.value)){
				String reversed = new StringBuilder(current.value).reverse().toString();
				reverse.put(current.value, reversed);
			}
			stack.push(reverse.get(current.value));
			current = current.next;
		}
		if(stack.isEmpty()){
			return this;
		}
		else{
			myFront = new Node(stack.pop(),null);
			Node current1 = myFront;
			while(!stack.isEmpty()){
				Node newNode = new Node(stack.pop(),null);
				current1.next=newNode;
				current1 = newNode;
			}
			myBack = current1;
		}
		return this;
	}

	@Override
	public String getStats() {
		return String.format("# append calls = %d",myAppends);
	}
	public String toString() {
		StringBuilder ss = new StringBuilder();
		Node current = myFront;
		while(current!=null){
			ss.append(current.value);
			current = current.next;
		}
		String s = ss.toString();
		return s;
	}

}
