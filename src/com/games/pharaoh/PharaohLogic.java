package com.games.pharaoh;

import com.common.card.CardImpl;
import com.common.card.Rank;
import com.common.card.Suit;
import com.common.deck.Deck;
import com.common.gamelogic.AnswerToPlayer;
import com.common.gamelogic.BaseGameLogic;
import com.common.player.BasePlayer;
import com.games.TypeOfCommand;
import telegram.GameLogicToBot;

import java.util.ArrayList;
import java.util.HashMap;

import static com.games.TypeOfCommand.*;

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

    public PharaohLogic(BasePlayer[] players, Deck deck, GameLogicToBot gameLogicToBot) {
        this(players, deck);
        this.gameLogicToBot = gameLogicToBot;
    }

    public void startGame() {
        for (BasePlayer player : players) {
            score.put(player, 0);
        }
        while (defineEndOfGame()) {
            startSet();
            countPlayersScore();
            if (lastCard.CardRank == Rank.HIDDEN) {
                score.replace(players[(attackPlayer1 - 1) % players.length],
                        score.get(players[(attackPlayer1 - 1) % players.length]) - 20);
            }
            for (BasePlayer player : score.keySet()) {
                sendToUser(new String[]{player.name + " : " + score.get(player)}, player.name, false);
            }
        }
    }

    @Override
    protected boolean startSet() {
        giveCardToPlayers(4);
        table.add(players[attackPlayer1].giveLastCard());
        lastCard = table.get(table.size() - 1);
        movePlayerOn(1);
        do {
            if (players[attackPlayer1].getClass() == PharaohAI.class) {
                if (AITurn()) continue;
            } else {
                if (playerTurn()) continue;
            }
            movePlayerOn(1);
        } while (!checkSetCondition());
        return false;
    }

    private boolean playerTurn() {
        boolean madeTurn = makeTurn();
        if (lastCard.CardRank != Rank.SIX && lastCard.CardRank != Rank.LADY)
            sendToUser(new String[]{AnswerToPlayer.END_OF_TURN.getMsg()}, players[attackPlayer1].name, false);
        if (madeTurn) {
            if (lastCard.CardRank == Rank.LADY) {
                sendToUser(new String[]{AnswerToPlayer.CHOOSE_SUIT.getMsg()}, players[attackPlayer1].name, false);
                sendToUser(new String[]{Suit.CLUBS.getSuit(),
                        Suit.DIAMOND.getSuit(),
                        Suit.HEARTS.getSuit(),
                        Suit.SPADES.getSuit()}, players[attackPlayer1].name, true);
                Suit pickedSuit;
                do {
                    pickedSuit = Suit.valuesOf(Integer.parseInt(getFromUser()));
                } while (pickedSuit == Suit.HIDDEN);
                lastCard = new CardImpl(pickedSuit, Rank.HIDDEN);
                movePlayerOn(1);
                sendToUser(new String[]{AnswerToPlayer.END_OF_TURN.getMsg()}, players[attackPlayer1].name, false);
                return true;
            } else if (lastCard.CardRank == Rank.ACE) {
                sendToUser(new String[]{AnswerToPlayer.SKIP_THE_TURN.getMsg()}, players[(attackPlayer1 + 1) % players.length].name, false);
                movePlayerOn(2);
                return true;
            } else if (lastCard.CardRank == Rank.SEVEN) {
                sendToUser(new String[]{AnswerToPlayer.YOU_TAKE_2_CARD.getMsg()}, players[(attackPlayer1 + 1) % players.length].name, false);
                nextPlayerTakeCardCount(2);
                movePlayerOn(2);
                return true;
            } else if (lastCard.CardRank == Rank.KING && lastCard.CardSuit == Suit.CLUBS) {
                sendToUser(new String[]{AnswerToPlayer.YOU_TAKE_5_CARD.getMsg()}, players[(attackPlayer1 + 1) % players.length].name, false);
                nextPlayerTakeCardCount(5);
                movePlayerOn(2);
                return true;
            } else return lastCard.CardRank == Rank.SIX;
        }
        return false;
    }

    protected boolean AITurn() {
        PharaohAI player = (PharaohAI) players[attackPlayer1];
        CardImpl card = player.Turn(lastCard);
        if (card == null) {
            player.takeCard(deck.giveNext());
            return true;
        }
        table.add(card);
        lastCard = card;
        if (lastCard.CardRank == Rank.LADY) {
            lastCard = new CardImpl(player.pickSuit(), Rank.HIDDEN);
            movePlayerOn(1);
        } else if (lastCard.CardRank == Rank.ACE) {
            movePlayerOn(2);
            return true;
        } else if (lastCard.CardRank == Rank.SEVEN) {
            nextPlayerTakeCardCount(2);
            movePlayerOn(2);
            return true;
        } else if (lastCard.CardRank == Rank.KING && lastCard.CardSuit == Suit.CLUBS) {
            nextPlayerTakeCardCount(5);
            movePlayerOn(2);
            return true;
        } else return lastCard.CardRank == Rank.SIX;
        return false;
    }


    private void nextPlayerTakeCardCount(int count) {
        for (int i = 0; i < count; i++) {
            players[(attackPlayer1 + 1) % players.length].takeCard(deck.giveNext());
            if (deck.isEmpty()) deck = new Deck(table);
        }
    }

    protected boolean makeTurn() {
        String playerName = players[attackPlayer1].name;
        sendToUser(new String[]{AnswerToPlayer.PLAYER.getMsg() + playerName + AnswerToPlayer.MAKE_TURN.getMsg()}, playerName, false);
        boolean take = true;
        while (true) {
            if (take) sendToUser(withoutPass, playerName, true);
            else sendToUser(withPass, playerName, true);
            TypeOfCommand command = TypeOfCommand.pickTurn(Integer.parseInt(getFromUser()));
            switch (command) {
                case CHECK_HAND -> sendToUser(players[attackPlayer1].showHand().toArray(new String[0]), playerName, false);
                case CHECK_TABLE -> {
                    Rank rank = lastCard.CardRank;
                    if (rank == Rank.HIDDEN) sendToUser(new String[]{lastCard.CardSuit.getSuit()}, playerName, false);
                    else sendToUser(new String[]{lastCard.CardSuit + " " + rank}, playerName, false);
                }
                case THROW_CARD -> {
                    boolean endTurn = throwCard();
                    if (endTurn) return true;
                }
                case TAKE -> {
                    if (take) {
                        players[attackPlayer1].takeCard(deck.giveNext());
                        sendToUser(new String[]{players[attackPlayer1].getLastCard().cardSuitAndRank()}, playerName, false);
                        take = false;
                    }
                }
                case PASS -> {
                    if (!take) {
                        return false;
                    }
                }
            }
        }
    }

    private boolean throwCard() {
        sendToUser(new String[]{AnswerToPlayer.WHAT_THROW.getMsg()}, players[attackPlayer1].name, false);
        ArrayList<String> message = new ArrayList<>(players[attackPlayer1].showHand());
        message.add(BACK.getType());
        sendToUser(message.toArray(new String[0]), players[attackPlayer1].name, true);
        CardImpl playerCard;
        int numberOfCardOnHand;
        while (true) {
            numberOfCardOnHand = Integer.parseInt(getFromUser()) - 1;
            if (numberOfCardOnHand == -1) return false;
            playerCard = players[attackPlayer1].hand.get(numberOfCardOnHand);
            if (checkMoveCorrectness(playerCard)) {
                sendToUser(new String[]{AnswerToPlayer.TRY_ANOTHER_CARD.getMsg()}, players[attackPlayer1].name, false);
                continue;
            }
            break;
        }
        table.add(playerCard);
        lastCard = playerCard;
        players[attackPlayer1].removeCard(numberOfCardOnHand);
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
                card.CardSuit != cardOnTable.CardSuit &&
                card.CardRank != Rank.LADY);
    }

    private boolean checkSetCondition() {
        return players[attackPlayer1].hand.size() == 0;
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

    @Override
    public void run() {
        startGame();
    }
}