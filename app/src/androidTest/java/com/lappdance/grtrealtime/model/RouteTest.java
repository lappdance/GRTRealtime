package com.lappdance.grtrealtime.model;

import android.graphics.Color;

import com.google.gson.Gson;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouteTest extends TestCase {
    String json;
    Route route;
    String[] multipleRoutes;
    Gson gson;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        gson = new Gson();

        json = "{\"RouteId\":\"8\",\"ShortName\":\"8\",\"LongName\":\"University/Fairview Park\",\"Description\":\"\",\"Color\":\"009933\",\"ColorText\":\"EA4AA3\",\"RouteNameHtml\":\"8 - University/Fairview Park\",\"OrderBy\":\"0008\"}";

        multipleRoutes = new String[] {
                "{\"RouteId\":\"200\",\"ShortName\":\"200\",\"LongName\":\"Ixpress\",\"Description\":\"\",\"Color\":\"0066FF\",\"ColorText\":\"00C513\",\"RouteNameHtml\":\"200 - Ixpress\",\"OrderBy\":\"0200\"}",
                "{\"RouteId\":\"51\",\"ShortName\":\"51\",\"LongName\":\"Hespeler Road\",\"Description\":\"\",\"Color\":\"CC0000\",\"ColorText\":\"009CE0\",\"RouteNameHtml\":\"51 - Hespeler Road\",\"OrderBy\":\"0051\"}",
                "{\"RouteId\":\"10\",\"ShortName\":\"10\",\"LongName\":\"Conestoga College\",\"Description\":\"\",\"Color\":\"0066CC\",\"ColorText\":\"00C513\",\"RouteNameHtml\":\"10 - Conestoga College\",\"OrderBy\":\"0010\"}",
        };

        route = gson.fromJson(json, Route.class);
    }

    public void testRoute_IsNotNull() {
        assertNotNull(route);
    }

    public void testRoute_HasCorrectId() {
        assertEquals(8, route.getId());
    }

    public void testRoute_HasCorrectName() {
        assertEquals("wrong short name", "8", route.getShortName());
        assertEquals("wrong display name", "8 - University/Fairview Park", route.getDisplayName());
    }

    public void testRoute_HasCorrectColor() {
        assertEquals(Color.parseColor("#009933"), route.getColor());
    }

    public void testRoute_Sorts() {
        List<Route> routes = new ArrayList<Route>();
        for(String json : multipleRoutes) {
            routes.add(gson.fromJson(json, Route.class));
        }

        Collections.sort(routes);

        assertEquals(10, routes.get(0).getId());
        assertEquals(51, routes.get(1).getId());
        assertEquals(200, routes.get(2).getId());
    }
}
