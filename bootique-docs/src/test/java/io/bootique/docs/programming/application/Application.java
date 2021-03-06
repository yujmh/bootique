package io.bootique.docs.programming.application;

import io.bootique.BaseModule;
import io.bootique.Bootique;
import io.bootique.di.Binder;

// tag::Application[]
public class Application extends BaseModule {

    public static void main(String[] args) {
        Bootique.app(args).module(Application.class).autoLoadModules().exec().exit();
    }

    public void configure(Binder binder) {
        // load app-specific services; redefine standard ones
    }
}
// end::Application[]

