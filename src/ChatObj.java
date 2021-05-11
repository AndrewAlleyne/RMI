
/* Add filtering method for text, client and sever differentiation. Core feature works*/
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatObj extends UnicastRemoteObject implements ChatIntf {

    String name;
    int id = 0;
    ChatIntf client = null;  //need to return connected client object to make calls

    static Vector<ChatIntf> connectClients = new Vector<>();
    static HashMap<String, Integer> hmap = new HashMap<>();

    Date today;
    String result;
    SimpleDateFormat simpleDateFormat;
    Locale currentLocale;

    ChatObj(String name) throws RemoteException {
        super();
        this.name = name;

        currentLocale = new Locale("en" , "US");
        simpleDateFormat = new SimpleDateFormat("h:mm a", currentLocale);
        today = new Date();
        result = simpleDateFormat.format(today);
    }

    @Override
    public void addClient(ChatIntf c) throws RemoteException {
        id++;
        client = c;
        connectClients.addElement(c);
        hmap.put(c.getName(),id);
        System.out.println("**** " + c.getName() + " has been added to the room! ID: " + hmap.get(c.getName()) + " ****");
        System.out.println();
        String master = connectClients.elementAt(0).getName();

        //server connects first
        if(connectClients.size() == 1){
            System.out.println("No one yet!");
        }
        //if the first client connects notify it.
        else if (connectClients.size() == 2){
            for (ChatIntf chatObj:connectClients) {
                if (!chatObj.getName().equals(master)) {
                    chatObj.clientToAll(master + " You are the first client. No one is here yet!");
                }
            }
        }else{
            for (ChatIntf chatObj:connectClients) {
                if(chatObj != c && !chatObj.getName().equals(master)){
                    c.clientToAll( "You can chat with " + chatObj.getName() + "!");
                }
            }
            for (ChatIntf chatObj:connectClients) {
                if(chatObj != c && !chatObj.getName().equals(master)){
                    chatObj.clientToAll( "You can chat with " + c.getName() + "!");
                }
            }

        }
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public void sendMessage(String msg) throws RemoteException {
        //Filter client name and look for exit.
        if(msg.replace(client.getName() + ": ","").equalsIgnoreCase("exit")) {
                //do nothing

        }else{
            for (ChatIntf c : connectClients) {
                c.clientToAll("[ " + result + "] " + msg);
            }
        }
    }

    @Override
    public void clientToAll(String msg) throws RemoteException {
        System.out.println(msg);
    }

    @Override
    public int clientList() throws RemoteException {
        int listSize = connectClients.size();
        return listSize;
    }

    @Override
    public void removeClient(ChatIntf c) throws RemoteException {
        if(connectClients.contains(c)){

            // leave the chatroom
            connectClients.remove(c);

            // let other clients know the user is disconnecting.
            for(ChatIntf cObj: connectClients){
                if( c != cObj) {
                    cObj.clientToAll(c.getName() + " has left the chat..");
                }
            }
        }
    }

    @Override
    public void broadcastToAll(String message) throws RemoteException {
        // server should broadcast to all. Already implemented with the
        // chat feature for server.

    }
}

