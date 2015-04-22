package com.wizzardo.http.mapping;

/**
* Created by wizzardo on 27.03.15.
*/
abstract class UrlMappingMatcher<T> extends UrlMapping<T> {
    protected UrlMappingMatcher(UrlMapping<T> parent) {
        super(parent);
    }

    protected abstract boolean matches(String urlPart);
}
