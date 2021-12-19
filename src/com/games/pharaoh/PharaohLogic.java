package com.games.pharaoh;

import com.common.card.CardImpl;
import com.common.card.Rank;
import com.common.card.Suit;
import com.common.deck.Deck;
import com.common.gamelogic.AnswerToPlayer;
import com.common.gamelogic.BaseGameLogic;
import com.common.player.BasePlayer;
import com.games.TypeOfTurn;
import telegram.GameLogicToBot;

import java.util.ArrayList;
import java.util.HashMap;

import static com.games.TypeOfTurn.*;

/**
 * Логика 101
 */
public class PharaohLogic extends BaseGameLogic {

    private final ArrayList<CardImpl> table;
    private CardImpl lastCard;
    private final String[] withPass = new String[]{CHECK_HAND.getType(), CHECK_TABLE.getType(), THROW_CARD.getType(), PASS.getType()};
    private final String[] withoutPass = new String[]{CHECK_HAND.getType(), CHECK_TABLE.getType(), THROW_CARD.getType(), TAKE.getType()};
    HashMap<BasePlayer, Integer> score;

    /**
     * Создание объекта
     *
     * @param players участвующие игроки
     * @param deck    колода для игры
     */
    public PharaohLogic(BasePlayer[] players, Deck deck) {
        super(players, deck);
        this.table = new ArrayList<>();
        this.score = new HashMap<>();
    }

    public PharaohLogic(BasePlayer[] players, Deck deck, GameLogicToBot gameLogicToBot){
        this(players,deck);
        input = gameLogicToBot;
    }

    public void startGame() {
        for (BasePlayer player : players) {
            score.put(player, 0);
        }
        while (defineEndOfGame()) {
            startSet();
            countPlayersScore();
            if (lastCard.CardRank == Rank.HIDDEN) {
                score.replace(players[(currentPlayer - 1) % players.length],
                        score.get(players[(currentPlayer - 1) % players.length]) - 20);
            }
            for (BasePlayer player : score.keySet()) {
                sendToUser(new String[]{player.name + " : " + score.get(player)},player.name,false);
            }
        }
    }

    @Override
    protected boolean startSet() {
        giveCardToPlayers(4);
        table.add(players[currentPlayer].giveLastCard());
        lastCard = table.get(table.size() - 1);
        movePlayerOn(1);
        do {
            boolean madeTurn = makeTurn();
            if (madeTurn) {
                if (lastCard.CardRank == Rank.LADY) {
                    sendToUser(new String[]{AnswerToPlayer.CHOOSE_SUIT.getMsg(),
                            Suit.CLUBS.getSuit(),
                            Suit.DIAMOND.getSuit(),
                            Suit.HEARTS.getSuit(),
                            Suit.SPADES.getSuit()},players[currentPlayer].name,false);
                    Suit pickedSuit;
                    do {
                        pickedSuit = Suit.valuesOf(Integer.parseInt(getFromUser()));
                    } while (pickedSuit == Suit.HIDDEN);
                    lastCard = new CardImpl(pickedSuit, Rank.HIDDEN);
                    movePlayerOn(1);
                    continue;
                } else if (lastCard.CardRank == Rank.ACE) {
                    movePlayerOn(2);
                    continue;
                } else if (lastCard.CardRank == Rank.SEVEN) {
                    nextPlayerTakeCardCount(2);
                    movePlayerOn(2);
                    continue;
                } else if (lastCard.CardRank == Rank.KING && lastCard.CardSuit == Suit.CLUBS) {
                    nextPlayerTakeCardCount(5);
                    movePlayerOn(2);
                    continue;
                } else if (lastCard.CardRank == Rank.SIX) {
                    continue;
                }

            }
            movePlayerOn(1);
        } while (!checkSetCondition());
        return false;
    }


    private void nextPlayerTakeCardCount(int count) {
        for (int i = 0; i < count; i++) {
            players[(currentPlayer + 1) % players.length].takeCard(deck.giveNext());
            if (deck.isEmpty()) deck = new Deck(table);
        }
    }

    protected boolean makeTurn() {
        String playerName = players[currentPlayer].name;
        sendToUser(new String[]{AnswerToPlayer.PLAYER.getMsg() + playerName + AnswerToPlayer.MAKE_TURN.getMsg()}, playerName,false);
        boolean take = true;
        while (true) {
            if (take) sendToUser(withoutPass,playerName,true);
            else sendToUser(withPass,playerName,true);
            TypeOfTurn command = TypeOfTurn.pickTurn(Integer.parseInt(getFromUser()));

            switch (command) {
                case CHECK_HAND -> players[currentPlayer].showHand();
                case CHECK_TABLE -> {
                    Rank rank = lastCard.CardRank;
                    if (rank == Rank.HIDDEN) sendToUser(new String[]{lastCard.CardSuit.getSuit()},playerName,false);
                    else sendToUser(new String[]{lastCard.CardSuit + " " + rank},playerName,false);
                }
                case THROW_CARD -> {
                    boolean endTurn = throwCard();
                    if (endTurn) return true;
                }
                case TAKE -> {
                    if (take) {
                        players[currentPlayer].takeCard(deck.giveNext());
                        take = false;
                    } else {
                        return false;
                    }
                }
            }
        }
    }


    private boolean throwCard() {
        ArrayList<String> message = new ArrayList<>();
        message.add(AnswerToPlayer.WHAT_THROW.getMsg());
        message.addAll(players[currentPlayer].showHand());
        message.add(BACK.getType());
        sendToUser(message.toArray(new String[0]),players[currentPlayer].name,true);
        CardImpl playerCard;
        int numberOfCardOnHand;
        while (true) {
            numberOfCardOnHand = Integer.parseInt(getFromUser()) - 1;
            if (numberOfCardOnHand == -1) return false;
            playerCard = players[currentPlayer].hand.get(numberOfCardOnHand);
            if (checkMoveCorrectness(playerCard)) {
                sendToUser(new String[]{AnswerToPlayer.TRY_ANOTHER_CARD.getMsg()},players[currentPlayer].name,false);
                continue;
            }
            break;
        }
        table.add(playerCard);
        lastCard = playerCard;
        players[currentPlayer].removeCard(numberOfCardOnHand);
        return true;
    }

    private void countPlayersScore() {
        for (BasePlayer player : players) {
            while (player.hand.size() != 0) {
                CardImpl card = player.giveLastCard();
                switch (card.CardRank) {
                    case JACK -> score.replace(player, score.get(player) + 2);
                    case LADY -> score.replace(player, score.get(player) + 3);
                    case KING -> score.replace(player, score.get(player) + 4);
                    case ACE -> score.replace(player, score.get(player) + 11);
                    default -> score.replace(player, score.get(player) + card.CardRank.ordinal());
                }
            }
        }
    }

    @Override
    protected int defineFirstPlayer() {
        return 0;
    }

    @Override
    protected boolean checkMoveCorrectness(CardImpl card) {
        CardImpl cardOnTable = table.get(table.size() - 1);
        return (card.CardRank != cardOnTable.CardRank &&
                card.CardSuit != cardOnTable.CardSuit);
    }

    private boolean checkSetCondition() {
        return players[currentPlayer].hand.size() == 0;
    }

    @Override
    protected boolean defineEndOfGame() {
        int count = 0;
        for (BasePlayer player : players) {
            if (score.get(player) < 101) {
                count += 1;
            }
        }
        return count >= 1;

    }
}