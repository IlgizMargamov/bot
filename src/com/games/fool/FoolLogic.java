package com.games.fool;

import com.common.card.CardImpl;
import com.common.card.Rank;
import com.common.deck.Deck;
import com.common.gamelogic.AnswerToPlayer;
import com.common.gamelogic.BaseGameLogic;
import com.common.gamelogic.EndOfGame;
import com.common.player.BasePlayer;
import com.games.TypeOfCommand;
import telegram.GameLogicToBot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.games.TypeOfCommand.*;

/**
 * Класс Дурака
 */

public class FoolLogic extends BaseGameLogic {
    ArrayList<TupleOfCard> table;
    CardImpl trump;
    boolean deckEmpty;
    boolean trumpGiven;

    final String[] defaultTurn = new String[]{CHECK_HAND.getType(),
            CHECK_TABLE.getType(),
            CHECK_TRUMP.getType(),
            THROW_CARD.getType(),
            PASS.getType(),
            DECK_SIZE.getType()};

    /**
     * Создание игры
     *
     * @param players игроки, которые участвуют
     * @param deck    колода для игры
     */
    public FoolLogic(BasePlayer[] players, Deck deck) {
        super(players, deck);
        giveCardToPlayers(6);
        this.table = new ArrayList<>();
        this.deckEmpty = false;
    }

    public FoolLogic(BasePlayer[] players, Deck deck, GameLogicToBot gameLogicToBot) {
        this(players, deck);
        this.gameLogicToBot = gameLogicToBot;
    }

    /**
     * Начинает игру
     */
    public void startGame() {
        attackPlayer1 = defineFirstPlayer();
        trumpGiven = false;
        while (!defineEndOfGame()) {
            boolean lose = startSet();
            if (lose) movePlayerOn(2);
            else movePlayerOn(1);
            table.clear();
        }
    }

    protected boolean defineEndOfGame() {
        int count = checkEnd();
        if (count == 0) {
            sendToAll(new String[]{EndOfGame.TIE.getMsg()});
            return true;
        } else if (count == 1) {
            for (BasePlayer player : players) {
                if (player.hand.size() != 0) {
                    sendToUser(new String[]{EndOfGame.LOSE.getMsg()}, player.name, false);
                    return true;
                }
            }
        }
        return false;
    }

    private int checkEnd() {
        int count = 0;
        for (BasePlayer player : players) {
            if (player.hand.size() > 0) count++;
        }
        return count;
    }

    @Override
    protected int defineFirstPlayer() {
        trump = deck.giveNext();
        CardImpl minCard = new CardImpl(trump.CardSuit, Rank.ACE);
        int firstPlayer = 0;
        for (int i = 0; i < players.length; i++) {
            for (CardImpl card : players[i].hand) {
                if (card.CardSuit == trump.CardSuit && card.CardRank.ordinal() < minCard.CardRank.ordinal()) {
                    minCard = card;
                    firstPlayer = i;
                }
            }
        }
        return firstPlayer;
    }

    @Override
    protected boolean startSet() {
        int attackPlayer2 = (attackPlayer1 + 2) % players.length;
        int defendPlayer = (attackPlayer1 + 1) % players.length;
        if (attackPlayer2 == attackPlayer1)
            sendToAll(new String[]{AnswerToPlayer.NOW_PLAY.getMsg(), players[attackPlayer1].name, players[defendPlayer].name});
        else
            sendToAll(new String[]{AnswerToPlayer.NOW_PLAY.getMsg(), players[attackPlayer1].name, players[defendPlayer].name, players[attackPlayer2].name});
        boolean end;
        playerTurn(false, attackPlayer1, AttackOrDefend.ATTACK, players[defendPlayer].hand.size());
        while (true) {
            end = playerTurn(false, defendPlayer, AttackOrDefend.DEFEND, 0);
            playerTurn(true, attackPlayer1, AttackOrDefend.ATTACK, players[defendPlayer].hand.size());
            if (attackPlayer1 != attackPlayer2)
                playerTurn(true, attackPlayer2, AttackOrDefend.ATTACK, players[defendPlayer].hand.size());
            if (end) break;
            if (uncoveredCard() == 0) break;
        }
        if (!deckEmpty || !trumpGiven) giveAllToSix();
        return end;
    }

    private void giveAllToSix() {
        for (BasePlayer player : players) {
            while (player.hand.size() < 6) {
                if (deck.isEmpty()) {
                    deckEmpty = true;
                    player.takeCard(trump);
                    trumpGiven = true;
                    break;
                }
                player.takeCard(deck.giveNext());
            }
        }
    }

    /**
     * @param possiblePass          Можно ли сделать пас в атаке
     * @param currentPlayer         номер текущего игрока
     * @param turn                  Защита или атака
     * @param defendPlayerCardCount сколько карту у защищающегося
     * @return В случае атаки возвращает всегда true. В случае защиты возвращает true - если игрок взял карты
     * и false если игрок покрылся.
     */
    private boolean playerTurn(boolean possiblePass, int currentPlayer, AttackOrDefend turn, int defendPlayerCardCount) {
        if (players[currentPlayer].getClass() == FoolAI.class) {
            return AITurn(currentPlayer, turn);
        } else {
            String name = players[currentPlayer].name;
            sendToUser(new String[]{turn.getMsg(), AnswerToPlayer.PLAYER.getMsg() + name + AnswerToPlayer.MAKE_TURN.getMsg()}, name, false);
            while (turn == AttackOrDefend.DEFEND || uncoveredCard() < defendPlayerCardCount) {
                sendToUser(defaultTurn, name, true);
                TypeOfCommand command = pickTurn(Integer.parseInt(getFromUser()));
                switch (command) {
                    case CHECK_HAND -> sendToUser(players[currentPlayer].showHand().toArray(new String[0]), name, false);
                    case CHECK_TABLE -> {
                        if (table.size() == 0) {
                            sendToUser(new String[]{AnswerToPlayer.TABLE_EMPTY.getMsg()}, name, false);
                            continue;
                        }
                        List<String> msg = new ArrayList<>();
                        for (TupleOfCard card : table) {
                            msg.add(card.toString());
                        }
                        sendToUser(msg.toArray(new String[0]), name, false);
                    }
                    case CHECK_TRUMP -> sendToUser(new String[]{trump.cardSuitAndRank()}, name, false);
                    case THROW_CARD -> {
                        sendToUser(new String[]{AnswerToPlayer.WHAT_THROW.getMsg()}, name, false);
                        ArrayList<String> msg = new ArrayList<>(players[currentPlayer].showHand());
                        msg.add(BACK.getType());
                        sendToUser(msg.toArray(new String[0]), name, true);
                        int numberOfCardOnHand = Integer.parseInt(getFromUser()) - 1;
                        if (numberOfCardOnHand == -1) continue;
                        CardImpl playerCard = players[currentPlayer].hand.get(numberOfCardOnHand);
                        if (turn == AttackOrDefend.ATTACK) {
                            if (checkMoveCorrectness(playerCard)) {
                                sendToUser(new String[]{AnswerToPlayer.TRY_ANOTHER_CARD.getMsg()}, name, false);
                                continue;
                            }
                            table.add(new TupleOfCard(this, playerCard));
                            //sendPhoto(worker.drawTable(table),name);
                            players[currentPlayer].removeCard(numberOfCardOnHand);
                            possiblePass = true;
                            if (table.size() == 6) {
                                sendToUser(new String[]{AnswerToPlayer.TABLE_FULL.getMsg()}, name, false);
                            }
                            sendToUser(new String[]{AnswerToPlayer.DOES_PLAYER_END.getMsg()}, name, false);
                            sendToUser(new String[]{YES.getType(), NO.getType()}, name, true);
                            TypeOfCommand answer = TypeOfCommand.pickTurn(Integer.parseInt(getFromUser()));
                            if (answer == YES) {
                                sendToUser(new String[]{AnswerToPlayer.END_OF_TURN.getMsg()}, name, false);
                                return true;
                            }
                        } else {
                            sendToUser(new String[]{AnswerToPlayer.WHERE_THROW.getMsg()}, name, false);
                            List<String> message = new ArrayList<>();
                            for (int i = 0; i < table.size(); i++) {
                                if (table.get(i).secondCard != null) continue;
                                message.add(i + 1 + ". " + table.get(i).toString());
                            }
                            sendToUser(message.toArray(new String[0]), name, true);
                            int numberOfCardOnTable = Integer.parseInt(getFromUser()) - 1;
                            cover(table.get(numberOfCardOnTable), playerCard, currentPlayer);
                            //sendPhoto(worker.drawTable(table),name);
                            if (table.get(numberOfCardOnTable).secondCard == null) continue;
                            players[currentPlayer].removeCard(numberOfCardOnHand);
                            if (uncoveredCard() == 0) {
                                sendToUser(new String[]{AnswerToPlayer.END_OF_TURN.getMsg()}, name, false);
                                return false;
                            }
                        }
                    }
                    case PASS -> {
                        if (turn == AttackOrDefend.ATTACK) {
                            if (possiblePass) {
                                sendToUser(new String[]{AnswerToPlayer.END_OF_TURN.getMsg()}, name, false);
                                return true;
                            }
                            sendToUser(new String[]{AnswerToPlayer.START_OF_SET.getMsg()}, name, false);
                        } else {
                            for (TupleOfCard card : table) {
                                players[currentPlayer].takeCard(card.firstCard);
                                if (card.secondCard != null)
                                    players[currentPlayer].takeCard(card.secondCard);
                            }
                            sendToUser(new String[]{AnswerToPlayer.END_OF_TURN.getMsg()}, name, false);
                            return true;
                        }
                    }
                    case DECK_SIZE -> sendToUser(new String[]{String.valueOf(deck.getSize())}, name, false);
                }
            }
        }
        return false;
    }

    private void cover(TupleOfCard cardFirst, CardImpl cardSecond, int currentPlayer) {
        if (cardFirst.isCover(cardSecond)) {
            cardFirst.coverWithCard(cardSecond);
            return;
        }
        if (players[currentPlayer].getClass() != FoolAI.class) {
            sendToUser(new String[]{AnswerToPlayer.NOT_POSSIBLE_TURN.getMsg()}, players[currentPlayer].name, false);
        }
    }

    protected boolean AITurn(int currentPlayer, AttackOrDefend turn) {
        FoolAI ai = (FoolAI) players[currentPlayer];

        if (turn == AttackOrDefend.ATTACK) {
            ArrayList<CardImpl> cardOnTable = new ArrayList<>();
            for (TupleOfCard tupleOfCard : table) {
                cardOnTable.add(tupleOfCard.firstCard);
                if (tupleOfCard.secondCard != null) cardOnTable.add(tupleOfCard.secondCard);
            }
            CardImpl card = ai.attackTurn(cardOnTable, trump.CardSuit);
            if (card == null) return false;
            else {
                table.add(new TupleOfCard(this, card));
                return true;
            }
        } else if (turn == AttackOrDefend.DEFEND) {
            ArrayList<CardImpl> cardOnTable = new ArrayList<>();
            for (TupleOfCard tuple : table) if (tuple.secondCard == null) cardOnTable.add(tuple.firstCard);
            HashMap<CardImpl, CardImpl> defendTurn = ai.defendTurn(cardOnTable, trump.CardSuit);
            if (defendTurn == null) return true;
            for (CardImpl card : defendTurn.keySet()) {
                for (TupleOfCard tuple : table) {
                    if (card == tuple.firstCard) {
                        cover(tuple, defendTurn.get(card), currentPlayer);
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected boolean checkMoveCorrectness(CardImpl card) {
        boolean result = true;
        if (table.size() == 0) {
            result = false;
        } else {
            for (TupleOfCard tuple : table) {
                if (tuple.firstCard.CardRank == card.CardRank ||
                        (tuple.secondCard != null && tuple.secondCard.CardRank == card.CardRank)) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public void run() {
        startGame();
    }

    private int uncoveredCard() {
        int count = 0;
        for (TupleOfCard tuple : table) {
            if (tuple.secondCard == null) count++;
        }
        return count;
    }
}