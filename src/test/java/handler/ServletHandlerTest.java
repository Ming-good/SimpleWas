package handler;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import org.handler.RouteHandler;
import org.handler.ServletHandler;
import org.http.HttpMethod;
import org.http.HttpStatus;
import org.http.request.HttpRequest;
import org.http.response.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.mapping.strategy.ServletMappingStrategy;
import org.resource.ResourceLoader;
import org.resource.ResourceWrite;
import org.servlet.SimpleServlet;

public class ServletHandlerTest {

    private ServletMappingStrategy strategy;
    private ServletHandler servletHandler;
    private HttpRequest request;
    private HttpResponse response;
    private ResourceWrite resourceWrite;
    private ResourceLoader resourceLoader;
    private SimpleServlet servlet;
    private RouteHandler routeHandler;

    @Before
    public void init() {
        strategy = mock(ServletMappingStrategy.class);
        servletHandler = new ServletHandler(strategy);
        request = mock(HttpRequest.class);
        response = mock(HttpResponse.class);
        resourceLoader = mock(ResourceLoader.class);
        resourceWrite = mock(ResourceWrite.class);
        servlet = mock(SimpleServlet.class);
        routeHandler = mock(RouteHandler.class);
        when(response.getResourceWrite()).thenReturn(resourceWrite);
    }

    @Test
    public void 상위_디렉토리_접근() throws Exception {
        RouteHandler mock = mock(RouteHandler.class);
        when(request.getPath()).thenReturn("/../service.TimeView");
        when(request.getMethod()).thenReturn(HttpMethod.GET.name());
        when(strategy.getServlet(request.getPath())).thenReturn(mock);

        servletHandler.handler(request, response);

        then(resourceWrite).should().error(request, response, HttpStatus.FORBIDDEN);
        then(resourceWrite).shouldHaveNoMoreInteractions();
    }

    @Test
    public void 허용되지_않은_메소드_호출() throws Exception {
        when(request.getPath()).thenReturn("/service.TimeView");
        when(request.getMethod()).thenReturn("PUT");
        when(strategy.getServlet(request.getPath())).thenReturn(routeHandler);

        servletHandler.handler(request, response);

        then(resourceWrite).should().error(request, response, HttpStatus.FORBIDDEN);
        then(resourceWrite).shouldHaveNoMoreInteractions();
    }

    @Test
    public void 지정된_서블릿의_메소드_이외_메소드_호출() throws Exception {
        when(request.getPath()).thenReturn("/service.TimeView");
        when(request.getMethod()).thenReturn("POST");
        when(strategy.getServlet(request.getPath())).thenReturn(routeHandler);
        when(routeHandler.getMethod()).thenReturn(HttpMethod.GET);

        servletHandler.handler(request, response);

        then(resourceWrite).should().error(request, response, HttpStatus.FORBIDDEN);
        then(resourceWrite).shouldHaveNoMoreInteractions();
    }

    @Test
    public void 디폴트페이지호출() throws Exception {
        when(request.getPath()).thenReturn("/index.html");
        when(request.getMethod()).thenReturn("GET");
        when(response.getIndexFileNm()).thenReturn("/index.html");

        servletHandler.handler(request, response);

        then(resourceWrite).should().forward(request,response);
        then(resourceWrite).shouldHaveNoMoreInteractions();
    }

    @Test
    public void NOT_FOUND_PAGE() throws Exception {
        ResourceWrite spyResourceWrite = spy(new ResourceWrite(resourceLoader, "/index.html", new HashMap<>()));

        when(request.getPath()).thenReturn("/service.TimeView");
        when(request.getMethod()).thenReturn("GET");
        when(response.getIndexFileNm()).thenReturn("/index.html");
        when(response.getResourceWrite("/service.TimeView")).thenReturn(spyResourceWrite);
        when(resourceLoader.getResource(anyString())).thenReturn(null);

        servletHandler.handler(request, response);

        then(spyResourceWrite).should().forward(request,response);
        then(spyResourceWrite).should().error(request,response, HttpStatus.NOT_FOUND);
        then(spyResourceWrite).shouldHaveNoMoreInteractions();
    }

    @Test
    public void 서블릿실행() throws Exception {
        RouteHandler routeHandler = new RouteHandler(servlet, HttpMethod.GET);
        when(request.getPath()).thenReturn("/service.TimeView");
        when(request.getMethod()).thenReturn("GET");
        when(response.getIndexFileNm()).thenReturn("/index.html");
        when(response.getResourceWrite("/service.TimeView")).thenReturn(resourceWrite);
        when(strategy.getServlet(request.getPath())).thenReturn(routeHandler);

        servletHandler.handler(request, response);

        then(servlet).should().service(request, response);
    }
}
