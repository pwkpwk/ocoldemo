package com.ambientbytes.ocoldemo;

/**
 * Collection of view factory objects available in the layout files for attaching to recycler views.
 * @author Pavel Karpenko
 */

public final class ViewFactories {
    public static IViewHolderFactory getMainViewFactory() {
        return new MainViewFactory();
    }
}
