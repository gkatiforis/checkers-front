package com.katiforis.top10.controller;

import com.katiforis.top10.conf.Const;


public abstract class MenuController extends AbstractController{
    public void addTopic() {
         super.addTopic(Const.MAIN_TOPIC_RESPONSE);
    }
}
