package controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameController
{
    private String player1Name;
    private String player2Name;

    public void initPlayerNames(String player1Name, String player2Name)
    {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        log.info("");


        // tt kell majd beállítani a labeleket a game view-hoz.
    }

}