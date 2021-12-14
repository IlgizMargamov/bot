package com.games.pharaoh;

import com.common.card.CardImpl;
import com.common.card.Rank;
import com.common.card.Suit;
import com.common.deck.Deck;
import com.common.gamelogic.BaseGameLogic;
import com.common.player.BasePlayer;
import com.games.TypeOfTurn;

import java.util.ArrayList;
import java.util.HashMap;

import static com.games.TypeOfTurn.*;


public class PharaohLogic extends BaseGameLogic {

    private final ArrayList<CardImpl> table;
    private CardImpl lastCard;
    private final String[] withPass = new String[]{CHECK_HAND.getString(), CHECK_TABLE.getString(), THROW_CARD.getString(), PASS.getString()};
    private final String[] withoutPass = new String[]{CHECK_HAND.getString(), CHECK_TABLE.getString(), THROW_CARD.getString(), TAKE.getString()};
    HashMap<BasePlayer, Integer> score;

    public PharaohLogic(BasePlayer[] players, Deck deck) {
        super(players, deck);
        this.table = new ArrayList<>();
        this.score = new HashMap<>();
    }

    public void startGame() {
        for (BasePlayer player : players) {
            score.put(player, 0);
        }
        while (defineWinner()) {
            startSet();
            countPlayersScore();
            if (lastCard.CardRank == Rank.HIDDEN) {
                score.replace(players[(currentPlayer - 1) % players.length],
                        score.get(players[(currentPlayer - 1) % players.length]) - 20);
            }
            ArrayList<String> message = new ArrayList<>();
            for (BasePlayer player: score.keySet() ) {
                message.add(player.name + " : " + score.get(player));
            }
            sendToUser(message.toArray(new String[0]));
        }
    }

    @Override
    protected boolean startSet() {
        giveCardToPlayers(4);
        table.add(players[currentPlayer].GiveLastCard());
        lastCard = table.get(table.size() - 1);
        movePlayerOn(1);
        do {
            boolean madeTurn = makeTurn();
            if (madeTurn) {
                if (lastCard.CardRank == Rank.LADY) {
                    sendToUser(new String[]{"Choose a next card suit",
                            "1. Club",
                            "2. Diamond",
                            "3. Hearts",
                            "4. Spades"});
                    Suit pickedSuit;
                    do {
                        pickedSuit = Suit.valuesOf(getFromUser());
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
            players[(currentPlayer + 1) % players.length].TakeCard(deck.giveNext());
            if (deck.isEmpty()) deck = new Deck(table);
        }
    }

    protected boolean makeTurn() {
        sendToUser(new String[]{"Player " + players[currentPlayer].name + " make your turn(type number of command)\n"});
        boolean take = true;
        while (true) {
            if (take) sendToUser(withoutPass);
            else sendToUser(withPass);
            TypeOfTurn command = TypeOfTurn.pickTurn(getFromUser());

            switch (command) {
                case CHECK_HAND -> players[currentPlayer].ShowHand();
                case CHECK_TABLE -> {
                    Rank rank = lastCard.CardRank;
                    if (rank == Rank.HIDDEN) sendToUser(new String[]{lastCard.CardSuit.getSuit()});
                    else sendToUser(new String[]{lastCard.CardSuit + " " + rank});
                }
                case THROW_CARD -> {
                    boolean endTurn = throwCard();
                    if (endTurn) return true;
                }
                case TAKE -> {
                    if (take) {
                        players[currentPlayer].TakeCard(deck.giveNext());
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
        message.add("What card you want to throw?");
        message.addAll(players[currentPlayer].ShowHand());
        message.add("0. Back.");
        sendToUser(message.toArray(new String[0]));
        CardImpl playerCard;
        int numberOfCardOnHand;
        while (true) {
            numberOfCardOnHand = Integer.parseInt(getFromUser()) - 1;
            if (numberOfCardOnHand == -1) return false;
            playerCard = players[currentPlayer].hand.get(numberOfCardOnHand);
            if (checkMoveCorrectness(playerCard)) {
                sendToUser(new String[]{"Try another card"});
                continue;
            }
            break;
        }
        table.add(playerCard);
        lastCard = playerCard;
        players[currentPlayer].RemoveCard(numberOfCardOnHand);
        return true;
    }

    private void countPlayersScore() {
        for (BasePlayer player : players) {
            while (player.hand.size() != 0) {
                CardImpl card = player.GiveLastCard();
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
    protected boolean defineWinner() {
        int count = 0;
        for (BasePlayer player : players) {
            if (score.get(player) < 101) {
                count += 1;
            }
        }
        return count >= 1;

    }
}