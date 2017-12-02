package utils.dialogflow;

import utils.dialogflow.action.*;

public class DialogFlowActionFactory {
    public final DialogFlowAction create(String action){
        if(action == null){
            return null;
        }else switch(action){
            case "weather":
                return new DialogFlowWeatherAction();
            case "hotel.search":
                return new DialogFlowHotelSearchAction();
            case "input.chat":
                return new DialogFlowChatAction();
        }
        return null;
    }
}