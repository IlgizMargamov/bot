package com.games.fool;

import com.common.card.CardImpl;
import com.common.card.Rank;
import com.common.deck.Deck;
import com.common.gamelogic.AnswerToPlayer;
import com.common.gamelogic.BaseGameLogic;
import com.common.gamelogic.EndOfGame;
import com.common.player.BasePlayer;
import com.games.TypeOfTurn;
import telegram.GameLogicToBot;

import java.util.ArrayList;

import static com.games.TypeOfTurn.*;

/**
 * Класс Дурака
 */

public class FoolLogic extends BaseGameLogic {
    ArrayList<TupleOfCard> table;
    CardImpl trump;
    int uncoveredCard;
    boolean deckEmpty;
    boolean trumpGiven;

    final String[] defaultTurn = new String[]{CHECK_HAND.getType(),
            CHECK_TABLE.getType(),
            CHECK_TRUMP.getType(),
            THROW_CARD.getType(),
            PASS.getType()};

    /**
     * Создание игры
     * @param players игроки, которые участвуют
     * @param deck колода для игры
     */
    public FoolLogic(BasePlayer[] players, Deck deck) {
        super(players, deck);
        giveCardToPlayers(6);
        this.table = new ArrayList<>();
        this.uncoveredCard = 0;
        this.deckEmpty = false;
    }

    public FoolLogic(BasePlayer[] players, Deck deck, GameLogicToBot gameLogicToBot){
        this(players, deck);
        this.gameLogicToBot = gameLogicToBot;
    }

    /**
     * Начинает игру
     */
    public void startGame() {
        currentPlayer = defineFirstPlayer();
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
                    sendToUser(new String[]{EndOfGame.LOSE.getMsg()},player.name,false);
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
        uncoveredCard = 0;
        int attackPlayer2 = (currentPlayer + 2) % players.length;
        int defendPlayer = (currentPlayer + 1) % players.length;
        if(attackPlayer2 == currentPlayer) sendToAll(new String[]{AnswerToPlayer.NOW_PLAY.getMsg(),players[currentPlayer].name,players[defendPlayer].name});
        else sendToAll(new String[]{AnswerToPlayer.NOW_PLAY.getMsg(),players[currentPlayer].name,players[defendPlayer].name,players[attackPlayer2].name});
        boolean end;
        makeTurn(false, currentPlayer, AttackOrDefend.ATTACK);
        while (true) {
            end = makeTurn(false, defendPlayer, AttackOrDefend.DEFEND);
            makeTurn(true, currentPlayer, AttackOrDefend.ATTACK);
            if (currentPlayer != attackPlayer2) makeTurn(true, attackPlayer2, AttackOrDefend.ATTACK);
            if (end) break;
            if (uncoveredCard == 0) break;
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

    private boolean makeTurn(boolean possiblePass, int currentPlayer, AttackOrDefend turn) {
        String name = players[currentPlayer].name;
        sendToUser(new String[]{turn.getMsg(), AnswerToPlayer.PLAYER.getMsg() + name + AnswerToPlayer.MAKE_TURN.getMsg()}, name,false);
        while (true) {
            sendToUser(defaultTurn, name,true);
            TypeOfTurn command = pickTurn(Integer.parseInt(getFromUser()));
            switch (command) {
                case CHECK_HAND -> sendToUser(players[currentPlayer].showHand().toArray(new String[0]), name,true);
                case CHECK_TABLE -> {
                    if (table.size() == 0) {
                        sendToUser(new String[]{AnswerToPlayer.TABLE_EMPTY.getMsg()}, name,false);
                        continue;
                    }
                    for (TupleOfCard card : table) {
                        sendToUser(new String[]{card.toString()}, name,false);
                    }
                }
                case CHECK_TRUMP -> sendToUser(new String[]{trump.cardSuitAndRank()}, name,false);
                case THROW_CARD -> {
                    sendToUser(new String[]{AnswerToPlayer.WHAT_THROW.getMsg()},name,false);
                    ArrayList<String> msg = new ArrayList<>(players[currentPlayer].showHand());
                    msg.add(BACK.getType());
                    sendToUser(msg.toArray(new String[0]), name,true);
                    int numberOfCardOnHand = Integer.parseInt(getFromUser()) - 1;
                    if (numberOfCardOnHand == -1) continue;
                    CardImpl playerCard = players[currentPlayer].hand.get(numberOfCardOnHand);
                    if (turn == AttackOrDefend.ATTACK) {
                        if (checkMoveCorrectness(playerCard)) {
                            sendToUser(new String[]{AnswerToPlayer.TRY_ANOTHER_CARD.getMsg()}, name,false);
                            continue;
                        }
                        table.add(new TupleOfCard(this, playerCard));
                        players[currentPlayer].removeCard(numberOfCardOnHand);
                        possiblePass = true;
                        uncoveredCard++;
                        if (table.size() == 6) {
                            sendToUser(new String[]{AnswerToPlayer.TABLE_FULL.getMsg()}, name,false);
                        }
                        sendToUser(new String[]{YES.getType(), NO.getType()}, name,true);
                        TypeOfTurn answer = TypeOfTurn.pickTurn(Integer.parseInt(getFromUser()));
                        if (answer == YES) {
                            sendToUser(new String[]{AnswerToPlayer.END_OF_TURN.getMsg()},name,false);
                            return true;
                        }
                    } else {
                        sendToUser(new String[]{AnswerToPlayer.WHERE_THROW.getMsg()}, name,false);
                        for (int i = 0; i < table.size(); i++) {
                            if (table.get(i).secondCard != null) continue;
                            sendToUser(new String[]{i + 1 + ". " + table.get(i).toString()}, name,true);
                        }
                        int numberOfCardOnTable = Integer.parseInt(getFromUser()) - 1;
                        cover(table.get(numberOfCardOnTable), playerCard);
                        if (table.get(numberOfCardOnTable).secondCard == null) continue;
                        players[currentPlayer].removeCard(numberOfCardOnHand);
                        uncoveredCard--;
                        if (uncoveredCard == 0) {
                            sendToUser(new String[]{AnswerToPlayer.END_OF_TURN.getMsg()},name,false);
                            return false;
                        }
                    }
                }
                case PASS -> {
                    if (turn == AttackOrDefend.ATTACK) {
                        if (possiblePass) {
                            sendToUser(new String[]{AnswerToPlayer.END_OF_TURN.getMsg()},name,false);
                            return true;
                        }
                        sendToUser(new String[]{AnswerToPlayer.START_OF_SET.getMsg()},name,false);
                    } else {
                        for (TupleOfCard card : table) {
                            players[currentPlayer].takeCard(card.firstCard);
                            if (card.secondCard != null)
                                players[currentPlayer].takeCard(card.secondCard);
                        }
                        sendToUser(new String[]{AnswerToPlayer.END_OF_TURN.getMsg()},name,false);
                        return true;
                    }
                }
            }
        }
    }

    private void cover(TupleOfCard cardFirst, CardImpl cardSecond) {
        if (cardFirst.isCover(cardSecond)) {
            cardFirst.coverWithCard(cardSecond);
            return;
        }
        sendToUser(new String[]{AnswerToPlayer.NOT_POSSIBLE_TURN.getMsg()},players[currentPlayer].name,false);
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
}