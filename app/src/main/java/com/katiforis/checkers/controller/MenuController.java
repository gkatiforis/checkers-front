package com.katiforis.checkers.controller;

import com.katiforis.checkers.conf.Const;


public abstract class MenuController extends AbstractController{
    public void addTopic() {
         super.addTopic(Const.MAIN_TOPIC_RESPONSE);
    }
}
