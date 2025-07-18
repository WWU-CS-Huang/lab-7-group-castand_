/*
 * This source file was generated by the Gradle 'init' task
 */
package lab7;
import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import heap.Heap;
import avl.AVL;
import java.io.FileNotFoundException;



public class Huffman {
    public Map<Character, Node> mapf; // map of character frequency 
    public Heap<Node, Integer> heapf; // heap sorted by frequency
    public Node ht; // root of the tree
    public Map<Character, String> freqBit; // char to bitMap
    

    public static void main(String[] args) throws FileNotFoundException{
      String file = args[0];
      String input;
      String ec, dc;
      try {
        input = getString(file);
      } catch(FileNotFoundException e) {
        throw(e);
      }
        
      Huffman h = new Huffman();  
      h.frequency(input);
      
      Map<Character, Node> mapf = h.mapf;
      Heap<Node, Integer> heapf = h.heapf;
      
      h.Tree();
      ec = h.encode(input);
      dc = h.decode(ec);
      
        
        if(input.length() < 100){
        System.out.println("Input String: " + input);
        System.out.println("Encoded String: " + ec);
        System.out.println("Decoded String: " + dc);
        }
        
        System.out.println("Decoded equals input: " + (dc.equals(input)));
        System.out.println("Compression Ratio: " + (ec.length() / input.length() / 8.0));
        
        
     
    }
    
    private static String getString(String name) throws FileNotFoundException {
      try {
        String input = "";
        File file = new File(name);
        Scanner scan = new Scanner(file);
        while (scan.hasNextLine()) {
          String line = scan.nextLine();
          input += line;
        }
        scan.close();
        return input;
      } catch (FileNotFoundException e) {
        throw e;
      }
    
    }
    
    public void Tree() {
      this.freqBit = new HashMap<Character,String>();
							 
		  //get the two lowest freqeuncy nodes
      while (heapf.size() > 1) {
        Node r1 = heapf.poll();
        r1.addCode('0');
        Node r2 = heapf.poll();
        r2.addCode('1');
        Node parent = new Node((r1.priority + r2.priority), r1, r2);
        heapf.add(parent, parent.priority);
      }
      heapf.peek().updateMap(freqBit);
      ht = heapf.poll();
    }
    
    public void frequency(String line) {
    
      line.toLowerCase();
      heapf = new Heap<Node,Integer>();
      mapf = new HashMap<Character,Node>();
      char[] cl = line.toCharArray();

      for (int i = 0; i < cl.length; i++) {
        if (mapf.containsKey(cl[i])) { // if the key is in mapf increase frequency
          char cc= cl[i];
          Node cn= mapf.get(cl[i]);
          cn.priority = cn.priority + 1;
          heapf.changePriority(cn, cn.priority);
        } else { // if not add it to mapf
          Node node = new Node(cl[i], 1);
          mapf.put(cl[i], node);
          heapf.add(node, 1);
        }
      }
    }
    
    public String encode(String line) {
      char[] ca = line.toCharArray();
      String bic = new String();
      for (char character : ca) {
        bic += freqBit.get(character);
      }
      return bic;
    }


    public String decode(String line) {
      return decode(line, ht);
    }
  
    public String decode(String bic, Node tree) {
      if (bic.length() > 0) {
        if (tree.ca != null) {
          return tree.ca + decode(bic, ht);
        } else {
          if (bic.charAt(0) == '0') {
	    return decode(bic.substring(1), tree.left);
	  } else {
	    return decode(bic.substring(1), tree.right);
	  }
        }
      } else {
        return tree.ca + "";
      }
    }
    
}


//nodes for the construction of the tree. 
class Node {
  public int priority;
  public Character ca;
  public Node left, right;
  public String bitcode;
  
  public Node(char charac, int prio) {
    priority = prio;
    ca = charac;
    left = null;
    right = null;
    bitcode = "";
  }
  
  public Node(int prio, Node left, Node right){
    priority = prio;
    this.left = left;
    this.right = right;
  }
  
  public void addCode(char c){
    if(left != null){
      left.addCode(c);
      right.addCode(c);
    } else {
      bitcode = c + bitcode;
    }
  }
  
   public void updateMap(Map<Character, String> freqBit) {
     if (left != null) {
       left.updateMap(freqBit);
       right.updateMap(freqBit);
     } else {
       freqBit.put(ca, bitcode);
     }
   }
}
