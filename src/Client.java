import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {

        //check for arguments
        if(args.length < 2){
            System.out.println("Missing localhost and port number.");
        }else {
            System.out.println("Client searching for open chatroom!");
        }

        String localhost = args[0];
        int portNumber = Integer.parseInt(args[1]);

        ChatIntf service = (ChatIntf) Naming.lookup("rmi://" + localhost + ":" + portNumber
                + "/" + localhost);

        Scanner scanner = new Scanner(System.in);
        System.out.println("You're attempting to connect to a chatroom. To exit , type [exit]");
        System.out.print("Enter username: ");
        String name = scanner.nextLine();

        ChatIntf client = new ChatObj(name);

        service.addClient(client);

        while(true){
            String serverInput = scanner.next().trim();
            String buildMsg = client.getName() + ": " + serverInput;
            service.sendMessage(buildMsg);

            if(serverInput.equalsIgnoreCase("exit")){
                service.removeClient(client);
                break;
            }
        }

        for(int i = 5; i >= 0; --i) {
            System.out.print("System will exit in [" + i + "] second(s)!");
            Thread.sleep(1000);
            System.out.print("\r");
        }
        System.out.println("You've been disconnected!");
        System.exit(0);

    }
}
