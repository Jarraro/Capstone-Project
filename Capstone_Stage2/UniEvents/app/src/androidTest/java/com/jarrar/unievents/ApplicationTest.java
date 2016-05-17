package com.jarrar.unievents;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.jarrar.unievents.remote.RemoteEndpointUtil;

import org.json.JSONArray;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
        JSONArray array = RemoteEndpointUtil.fetchJsonArray();
        assertTrue("fetchJsonArray Succeeded",array != null);
    }
}