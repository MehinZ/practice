package geektime.hw.week3.gateway.filter.practice;

import geektime.hw.week3.gateway.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class ProxyBizFilter implements HttpRequestFilter {

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        System.out.println("Request " + fullRequest.uri() + " reach filter!");
    }
}
