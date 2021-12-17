package com.games.fool;

import com.common.card.CardImpl;
import com.common.card.Rank;
import com.common.deck.Deck;
import com.common.gamelogic.BaseGameLogic;
import com.common.player.BasePlayer;
import com.games.TypeOfTurn;

import java.util.ArrayList;
import java.util.List;

import static com.games.TypeOfTurn.*;

public class FoolLogic extends BaseGameLogic {

    ArrayList<Tuple> table;
    CardImpl trump;
    int uncoveredCard;
    boolean deckEmpty;
    boolean trumpGiven;
    String[] defaultTurn = new String[]{CHECK_HAND.getType(),
            CHECK_TABLE.getType(),
            CHECK_TRUMP.getType(),
            THROW_CARD.getType(),
            PASS.getType()};

    public FoolLogic(BasePlayer[] players, Deck deck) {
        super(players, deck);
        giveCardToPlayers(6);
        this.table = new ArrayList<>();
        this.uncoveredCard = 0;
        this.deckEmpty = false;
    }

    public void startGame() {
        currentPlayer = defineFirstPlayer();
        trumpGiven = false;
        while (!defineWinner()) {
            boolean lose = startSet();
            if (lose) movePlayerOn(2);
            else movePlayerOn(1);
            table.clear();
        }
    }


    protected boolean defineWinner() {
        int count = checkEnd();
        if (count == 0) {
            sendToUser(new String[]{EndOfGame.Tie.getMsg()});
            return true;
        } else if (count == 1) {
            for (BasePlayer player : players) {
                if (player.hand.size() != 0) {
                    sendToUser(new String[]{EndOfGame.Lose.getMsg()});
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
        int attackPlayer2 = (currentPlayer + 2) % players.length;
        int defendPlayer = (currentPlayer + 1) % players.length;
        boolean end;
        makeTurn(false, currentPlayer, AttackOrDefend.ATTACK);
        while (true) {
            end = makeTurn(false,defendPlayer,AttackOrDefend.DEFEND);
            makeTurn(true, currentPlayer,AttackOrDefend.ATTACK);
            if (currentPlayer != attackPlayer2) makeTurn(true, attackPlayer2,AttackOrDefend.ATTACK);
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
                    player.TakeCard(trump);
                    trumpGiven = true;
                    break;
                }
                player.TakeCard(deck.giveNext());
            }
        }
    }

    private boolean makeTurn(boolean possiblePass, int currentPlayer, AttackOrDefend turn) {
        sendToUser(new String[]{turn.getMsg(), AnswerToPlayer.Player.getMsg() + players[currentPlayer].name + AnswerToPlayer.MakeTurn.getMsg()});
        while (true) {
            sendToUser(defaultTurn);
            TypeOfTurn command = pickTurn(Integer.parseInt(getFromUser()));
            switch (command) {
                case CHECK_HAND -> sendToUser(players[currentPlayer].ShowHand().toArray(new String[0]));
                case CHECK_TABLE -> {
                    if (table.size() == 0) {
                        sendToUser(new String[]{AnswerToPlayer.TableEmpty.getMsg()});
                        continue;
                    }
                    for (Tuple card : table) {
                        sendToUser(new String[]{card.toString()});
                    }
                }
                case CHECK_TRUMP -> sendToUser(new String[]{trump.cardSuitAndRank()});
                case THROW_CARD -> {
                    List<String> msg = new ArrayList<>();
                    msg.add(AnswerToPlayer.WhereThrow.getMsg());
                    msg.addAll(players[currentPlayer].ShowHand());
                    msg.add(BACK.getType());
                    sendToUser(msg.toArray(new String[0]));
                    int numberOfCardOnHand = Integer.parseInt(getFromUser()) - 1;
                    if (numberOfCardOnHand == -1) continue;
                    CardImpl playerCard = players[currentPlayer].hand.get(numberOfCardOnHand);
                    if(turn == AttackOrDefend.ATTACK) {
                        if (checkMoveCorrectness(playerCard)) {
                            sendToUser(new String[]{AnswerToPlayer.TryAnotherCard.getMsg()});
                            continue;
                        }
                        table.add(new Tuple(playerCard));
                        players[currentPlayer].RemoveCard(numberOfCardOnHand);
                        possiblePass = true;
                        uncoveredCard++;
                        if (table.size() == 6) {
                            sendToUser(new String[]{AnswerToPlayer.TableFull.getMsg()});
                        }
                        sendToUser(new String[]{AnswerToPlayer.DoesPlayerEnd.getMsg()});
                        String answer = getFromUser();
                        if (answer.equals("y")) {
                            return true;
                        }
                    }
                    else{
                        sendToUser(new String[]{AnswerToPlayer.WhereThrow.getMsg()});
                        for (int i = 0; i < table.size(); i++) {
                            if (table.get(i).second != null) continue;
                            sendToUser(new String[]{i + 1 + ". " + table.get(i).toString()});
                        }
                        int numberOfCardOnTable = Integer.parseInt(getFromUser()) - 1;
                        Cover(table.get(numberOfCardOnTable),playerCard);
                        if (table.get(numberOfCardOnTable).second == null) continue;
                        players[currentPlayer].RemoveCard(numberOfCardOnHand);
                        uncoveredCard--;
                        if (uncoveredCard == 0) return false;
                    }
                }
                case PASS -> {
                    if(turn == AttackOrDefend.ATTACK){
                        if (possiblePass) return true;
                        sendToUser(new String[]{AnswerToPlayer.StartOfSet.getMsg()});
                    }
                    else{
                        for (Tuple card : table) {
                            players[currentPlayer].TakeCard(card.first);
                            if (card.second != null)
                                players[currentPlayer].TakeCard(card.second);
                        }
                        return true;
                    }
                }
            }
        }
    }

    private void Cover(Tuple cardFirst,CardImpl cardSecond){
        if(cardFirst.isCover(cardSecond)){
            cardFirst.coverWithCard(cardSecond);
            return;
        }
        sendToUser(new String[]{AnswerToPlayer.NotPossibleTurn.getMsg()});
    }

    @Override
    protected boolean checkMoveCorrectness(CardImpl card) {
        if (table.size() == 0) return false;
        for (Tuple tuple : table) {
            if (tuple.first.CardRank == card.CardRank ||
                    (tuple.second != null && tuple.second.CardRank == card.CardRank)) {
                return false;
            }
        }
        return true;
    }

    private class Tuple {
        public CardImpl first;
        public CardImpl second;

        public Tuple(CardImpl first) {
            this.first = first;
        }

        public boolean isCover(CardImpl second) {
            if (first.CardSuit == trump.CardSuit)
                return second.CardSuit == trump.CardSuit && first.CardRank.ordinal() < second.CardRank.ordinal();
            if (second.CardSuit == trump.CardSuit) return true;
            return first.CardRank.ordinal() < second.CardRank.ordinal() && first.CardSuit == second.CardSuit;
        }

        public void coverWithCard(CardImpl card){
            this.second = card;
        }

        public String toString() {
            if (second == null) return first.cardSuitAndRank() + " \\ " + AnswerToPlayer.Nothing.getMsg();
            return first.cardSuitAndRank() + " \\ " + second.cardSuitAndRank();
        }
    }

    private enum AttackOrDefend{
        ATTACK("You Attack"),
        DEFEND("You Defend");

        private final String msg;

        AttackOrDefend(String msg){
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }
}