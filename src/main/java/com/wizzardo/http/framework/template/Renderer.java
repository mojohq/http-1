package com.wizzardo.http.framework.template;


import com.wizzardo.epoll.readable.ReadableBuilder;
import com.wizzardo.epoll.readable.ReadableData;

/**
 * @author: moxa
 * Date: 5/6/13
 */
public abstract class Renderer {

    protected Model model;

    public Renderer(Model model) {
        this.model = model;
    }

    public Renderer() {
    }

    public Model getModel() {
        return model;
    }

    public abstract RenderResult render();

    public ReadableBuilder render(ReadableBuilder builder) {
        render().provideBytes(builder::append);
        return builder;
    }

    public ReadableData renderReadableData() {
        return render(new ReadableBuilder());
    }
}
