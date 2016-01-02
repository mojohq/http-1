package com.wizzardo.http.framework;

import com.wizzardo.http.Handler;
import com.wizzardo.http.mapping.UrlMapping;
import com.wizzardo.http.mapping.UrlTemplate;

/**
 * Created by wizzardo on 05.05.15.
 */
public class ControllerUrlMapping extends UrlMapping<Handler> {

    public ControllerUrlMapping() {
    }

    public ControllerUrlMapping append(String url, ControllerHandler handler) {
        super.append(url, handler.getControllerName() + "." + handler.getActionName(), handler);
        return this;
    }

    public ControllerUrlMapping append(String url, Class<? extends Controller> controllerClass, String action) {
        append(url, new ControllerHandler(controllerClass, action));
        return this;
    }

    @Override
    public ControllerUrlMapping append(String url, Handler handler) {
        super.append(url, handler);
        return this;
    }

    @Override
    public ControllerUrlMapping append(String url, String name, Handler handler) {
        super.append(url, name, handler);
        return this;
    }

    public UrlTemplate getUrlTemplate(Class<? extends Controller> controllerClass, String action) {
        return getUrlTemplate(toMapping(controllerClass, action));
    }

    public UrlTemplate getUrlTemplate(String controller, String action) {
        return getUrlTemplate(toMapping(controller, action));
    }

    public String toMapping(Class<? extends Controller> controllerClass, String action) {
        return toMapping(Controller.getControllerName(controllerClass), action);
    }

    public String toMapping(String controller, String action) {
        return controller + "." + action;
    }
}
