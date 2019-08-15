//package com.katiforis.checkers.controller;
//
//import android.util.Log;
//
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.katiforis.checkers.DTO.request.GetNotifications;
//import com.katiforis.checkers.DTO.response.NotificationList;
//import com.katiforis.checkers.DTO.response.ResponseState;
//import com.katiforis.checkers.activities.MenuActivity;
//import com.katiforis.checkers.conf.Const;
//import com.katiforis.checkers.fragment.NotificationFragment;
//import com.katiforis.checkers.stomp.Client;
//
//import java.util.Date;
//
//import ua.naiksoftware.stomp.dto.StompMessage;
//
//
//public class NotificationController extends MenuController {
//
//    private static NotificationController INSTANCE = null;
//
//    private NotificationFragment notificationFragment;
//
//    private NotificationController() {}
//
//    public static NotificationController getInstance() {
//        if (INSTANCE == null) {
//            synchronized (NotificationController.class) {
//                INSTANCE = new NotificationController();
//            }
//        }
//
//        return INSTANCE;
//    }
//
//    public void setNotificationFragment(NotificationFragment notificationFragment) {
//        this.notificationFragment = notificationFragment;
//    }
//
//    @Override
//    public void onReceive(StompMessage stompMessage) {
//        JsonParser jsonParser = new JsonParser();
//        JsonObject jo = (JsonObject) jsonParser.parse(stompMessage.getPayload());
//
//        Log.i(Const.TAG, "Receive: " + jo.toString());
//
//        JsonObject message = jo.getAsJsonObject("body");
//        String messageStatus = message.get("status").getAsString();
//
//        Log.i(Const.TAG, "Receive: " + messageStatus);
//         if(messageStatus.equalsIgnoreCase(ResponseState.NOTIFICATION_LIST.getState())){
//            NotificationList notificationList = gson.fromJson(message, NotificationList.class);
//
////           List<Notification> notifications =
////                   LocalCache.getInstance().save(LocalCache.NOTIFICATIONS,
////                           notificationList.getNotifications(),
////                           notificationFragment.getActivity());
//
//            notificationFragment.appendNotifications(notificationList.getNotifications());
//        }
//    }
//
//    public void getNotificationList(){
//        addTopic(false);
//        GetNotifications get = new GetNotifications(new Date().getTime());
//        Client.getInstance().send(Const.GET_NOTIFICATION_LIST, gson.toJson(get));
//    }
//}
