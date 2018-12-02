package udpclient;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;
import java.util.StringTokenizer;

import static udpclient.Client.*;
import static udpclient.Printer.*;
import static udpclient.Statistics.increaseOutgoingMsgCount;
import static udpclient.Util.*;

public class SendingMessageHandler {

    /**
     * command: reg bs_ip_address
     *
     * @param bs_ip
     */
    public static void registerToBS(String bs_ip) {

        /* length REG IP_address port_no username */
        String msg="REG " + myIp + " " + myPort + " " + myUserName;
        String msg_formatted = formatMessage(msg);

        Client.bs_ip=bs_ip;
        sendPacket(bs_ip,bs_port,msg_formatted,"Register");
    }

    /**
     * (testing purpose)
     *
     * command regl
     *
     */
    public static void registerToBSonSameIp() {

        /* length REG IP_address port_no username */
        String msg="REG " + myIp + " " + myPort + " " + myUserName;
        String msg_formatted = formatMessage(msg);

        Client.bs_ip=myIp;
        sendPacket(bs_ip,bs_port,msg_formatted,"Register");
    }


    /**
     *  command: unreg
     */
    public static void unregisterFromBS(){

        /* length UNREG IP_address port_no username */
        String msg="UNREG " + myIp + " " + myPort + " " + myUserName;
        String msg_formatted = formatMessage(msg);

        sendPacket(bs_ip,bs_port,msg_formatted,"Unregister");
    }

    /**
     *  command: join
     */
    public static void joinToSystem() {

        if (getRoutingTable().size()==0) {
            print_nng("No neighbours in routing table to join");
            return;
        }

        /* length JOIN IP_address port_no */
        String msg="JOIN " + myIp + " " + myPort;
        String msg_formatted = formatMessage(msg);

        for (Entry<String, Node> entry : getRoutingTable().entrySet()) {
            sendPacket(entry.getValue().getIp(), entry.getValue().getPort(), msg_formatted,"Join");
        }

    }


    /**
     *  command: leave
     */
    public static void leaveTheSystem() {

        if (getRoutingTable().size()==0) {
            print_nng("No neighbours in routing table to leave");
            return;
        }

        /* length LEAVE IP_address port_no */
        String msg="LEAVE " + myIp + " " + myPort;
        String msg_formatted = formatMessage(msg);

        for (Entry<String,Node> entry: getRoutingTable().entrySet()) {
            sendPacket(entry.getValue().getIp(),entry.getValue().getPort(),msg_formatted,"Leave");
        }

    }


    /**
     *  command: search file_name
     *
     * @param st
     */
    public static void searchFile(StringTokenizer st) {
//
//        length SER IP Port file_name hops search_key
//
//        search key = timestamp + ”_” + ip:port
//
        if (getRoutingTable().isEmpty()){
            print_nng("No neighbours in routing table to search");
            return;
        }

        try {
            String fileNameCreator="";
            String hopsStr="";
            while (st.hasMoreTokens()){
                String part=st.nextToken();
                fileNameCreator+=part+" ";
                hopsStr=part;
            }

            String filename = fileNameCreator.substring(1, (fileNameCreator.length() - hopsStr.length() - 3));


            int hops=1;
            if(st.hasMoreTokens()) {
                try {
                    hops = Integer.parseInt(hopsStr);
                }catch (NumberFormatException e){
                    print_nng("Wrong hops value");
                }
            }
            searchFile(filename);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  command: search
     *
     * @param filename
     */
    public static void searchFile(String filename){

        increaseOutgoingMsgCount();

        /* length SER IP port file_name hops */
        String msg = "SER " + myIp + " " + myPort + " \"" + filename + "\" " + 1;
        String msg_formatted = formatMessage(msg);

        // set search starting time
        timeOfLastSearch=System.currentTimeMillis();

        for (Entry<String, Node> entry : getRoutingTable().entrySet()) {
            sendPacket(entry.getValue().getIp(), entry.getValue().getPort(), msg_formatted, "Search");
        }
    }


    /**
     * command: exit
     */
    public static void exit() {

        unregisterFromBS();

        leaveTheSystem();

    }

    private static Random random = new Random();

    public static void sendGossips() {
        if (getRoutingTable().size() > 1) {
            if (okToGossip) {

                rgStatus.setGossipSendingStatusToRoutingTableStatus();

                ArrayList<Node> allNeighbours = new ArrayList<>();
                allNeighbours.addAll(getRoutingTable().values());


                for (Node node : allNeighbours) {
                    String neighboursToBeSent = "";
                    int count = 0;

                    // random gossip
                    int randomNum1=random.nextInt(allNeighbours.size()-0) + 0;
                    while (allNeighbours.get(randomNum1).isEqual(node.getIp(),node.getPort())){
                        randomNum1=random.nextInt(allNeighbours.size()-0) + 0;
                    }
                    neighboursToBeSent += allNeighbours.get(randomNum1).getIp() + " " + allNeighbours.get(randomNum1).getPort() + " ";
                    count++;
                    neighboursToBeSent.substring(0, neighboursToBeSent.length() - 1); //remove last space


                    sendNeighboursToNeighbourMessage(node, count, neighboursToBeSent);

                }
            }
        }
    }

    /**
     * use this to request neighbour's table
     */
    public static void sendGossipRequest(){
        // format
        // length GOSSIPREQ IP Port
        if (getRoutingTable().size()>0){
            String msg="GOSSIPREQ "+myIp+" "+myPort;
            String msg_formatted=formatMessage(msg);
            for (Node neighbour:getRoutingTable().values()){
                sendPacket(neighbour.getIp(),neighbour.getPort(),msg_formatted,"Gossip request");
            }
        }else {
            print_nng("Routing table is empty.\nFirst register to Bootstrap server before requesting gossips");
        }
    }


    public static void sendNeighboursToNeighbourMessage(Node nodeToBeSent, int neighbourCount,String neighboursDetails){
        String msg="GOSSIP "+myIp+" "+myPort+" "+neighbourCount+" "+neighboursDetails;
        String msg_formatted=formatMessage(msg);
        sendPacket(nodeToBeSent.getIp(),nodeToBeSent.getPort(),msg_formatted,"Gossip");
    }


}
