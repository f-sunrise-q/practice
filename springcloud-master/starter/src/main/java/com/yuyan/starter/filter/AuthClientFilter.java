package com.yuyan.starter.filter;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

public class AuthClientFilter extends ClientFilter {
    @Override
    public ClientResponse handle(ClientRequest clientRequest) throws ClientHandlerException {
        return null;
    }
}
