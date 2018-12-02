package udpclient;

public class Statistics {

    static int incomeMsgCount=0;
    static int outgoingMsgCount=0;
    static int forwardedMsgCount=0;
    static int answeredMsgCount=0;


    public static int getIncomeMsgCount() {
        return incomeMsgCount;
    }

    public static int getOutgoingMsgCount() {
        return outgoingMsgCount;
    }

    public static int getForwardedMsgCount() {
        return forwardedMsgCount;
    }

    public static int getAnsweredMsgCount() {
        return answeredMsgCount;
    }

    public static void clearCounts(){
        incomeMsgCount=0;
        outgoingMsgCount=0;
        forwardedMsgCount=0;
        answeredMsgCount=0;
    }

    public static void increaseIncomeMsgCount() {
        incomeMsgCount++;
    }

    public static void  increaseOutgoingMsgCount() {
        outgoingMsgCount++;
    }

    public static void increaseForwardedMsgCount() {
        forwardedMsgCount++;
    }

    public static void increaseAnsweredMsgCount() {
        answeredMsgCount++;
    }
}
