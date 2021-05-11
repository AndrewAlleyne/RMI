import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws RemoteException {

        //check for arguments
        if(args.length < 2){
            System.out.println("Missing localhost and port number.");
        }else {
            System.out.println("Sever preparing to go live!");
        }

        String localhost = args[0];
        int portNumber = Integer.parseInt(args[1]);


        // remote object class for implementing interfaces
        ChatObj chatObj = new ChatObj("Chatroom Master: ");

        // create server registry
        Registry registry = LocateRegistry.createRegistry(portNumber);
        registry.rebind(localhost,chatObj);

        // set client (server) which is also a server.
        chatObj.addClient(chatObj);
        Scanner  scanner = new Scanner(System.in);
        System.out.println("Hello, I am the " + chatObj.getName().replace(": ","") +  ". I can send messages as well.");

        //communicate with clients
        while(true){
            String msg = scanner.next().trim();
          if(chatObj.clientList() >= 1){
              chatObj.sendMessage(chatObj.name + msg);
          }
        }
    }
}
