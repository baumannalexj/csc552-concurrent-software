package ajeffrey.teaching.hash.client;

import ajeffrey.teaching.hash.iface.Hash;

import java.rmi.Naming;

public class Main {

  public static void main (String[] args) throws Exception {
    String url;
    if (args.length > 0) {
      url = args[0];
    } else {
      url = "///hashserver";
    }
    final Hash hashTable = (Hash)(Naming.lookup (url));
    final GUI gui = GUI.factory.build (hashTable);
    gui.start ();
  }

}
