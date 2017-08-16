package com.py.library.network.http;

import okhttp3.HttpUrl;

/**
 * Created by pengyi on 2017/7/27.
 */

public interface UrlParser {
    HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl originUrl);
}
