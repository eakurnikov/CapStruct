package com.private_void.app.controllers;

import com.private_void.app.CapStructApp;

public abstract class CapStructController {
    protected CapStructApp app;

    public void setApp(CapStructApp app) {
        this.app = app;
    }
}
