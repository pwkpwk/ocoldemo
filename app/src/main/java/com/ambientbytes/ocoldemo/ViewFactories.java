package com.ambientbytes.ocoldemo;

/**
 * Created by pakarpen on 3/28/17.
 */

public final class ViewFactories {
    public static IViewFactory getMainViewFactory() {
        return new MainViewFactory();
    }
}