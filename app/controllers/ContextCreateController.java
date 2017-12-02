package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import utils.word2vector.ContextAnalysisSingleton;
import play.libs.Json;

import javax.inject.Inject;

public class ContextCreateController extends Controller {

    public Result near(String text)
    {
        ContextAnalysisSingleton vec = ContextAnalysisSingleton.getInstance();
        return ok(Json.toJson(vec.getNearest(text,10))).as("application/json; charset=utf-8");
    }
    public Result calcNear(String positive, String negative)
    {
        ContextAnalysisSingleton vec = ContextAnalysisSingleton.getInstance();
        return ok(Json.toJson(vec.getNearest(positive,negative,10))).as("application/json; charset=utf-8");
    }
}
