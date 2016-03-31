package test;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by vladislav on 31.03.16.
 */
public class ServletAbstractBinder extends AbstractBinder {
    private final HttpServletRequest httpServletRequest;

    ServletAbstractBinder(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    protected void configure() {
        bind(httpServletRequest).to(HttpServletRequest.class);
    }
}
