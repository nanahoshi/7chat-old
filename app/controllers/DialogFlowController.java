package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.dialogflow.DialogFlowActionFactory;

public class DialogFlowController extends Controller{

    public Result webhook() {
        JsonNode json = request().body().asJson();
        Logger.info(Json.stringify(json));
        if(json==null){
            return badRequest(Json.toJson("Request is null."));
        }
        // write debug
        DialogFlowActionFactory factory = new DialogFlowActionFactory();
        return ok(factory.create(json.get("result").get("action").asText()).run(json));
	}
}