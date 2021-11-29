package com.common.player;

import com.common.card.CardImpl;

import java.util.ArrayList;


public class BasePlayer implements Player {
    public ArrayList<CardImpl> hand;
    public String name;

    public BasePlayer(){
    }

    protected enum typeOfTurn{
        Attack, Defend, Throw, Pass
    }
}
