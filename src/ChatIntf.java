import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatIntf extends Remote {


    void addClient(ChatIntf c) throws RemoteException;
    String getName() throws RemoteException;
    void sendMessage(String msg ) throws RemoteException;
    void clientToAll(String msg) throws RemoteException;
    int clientList() throws RemoteException;
    void removeClient(ChatIntf c) throws RemoteException;
    void broadcastToAll(String message) throws RemoteException;

}
