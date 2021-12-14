package com.games.fool;

import com.common.card.CardImpl;
import com.common.card.Rank;
import com.common.deck.Deck;
import com.common.gamelogic.BaseGameLogic;
import com.common.player.BasePlayer;
import com.games.TypeOfTurn;

import java.util.ArrayList;

import static com.games.TypeOfTurn.*;

public class FoolLogic extends BaseGameLogic {

    ArrayList<Tuple> table;
    CardImpl trump;
    int uncoveredCard;
    boolean deckEmpty;
    boolean trumpGiven;
    String[] defaultTurn = new String[]{CHECK_HAND.getString(), CHECK_TABLE.getString(), CHECK_TRUMP.getString(), THROW_CARD.getString(), PASS.getString()};

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
            sendToUser(new String[]{"Tie!"});
            return true;
        } else if (count == 1) {
            for (BasePlayer player : players) {
                if (player.hand.size() != 0) {
                    sendToUser(new String[]{player.name + "you lose!"});
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
        sendToUser(new String[]{turn.getMsg(),"Player " + players[currentPlayer].name + " make your turn(type number of command)"});
        while (true) {
            sendToUser(defaultTurn);
            TypeOfTurn command = pickTurn(getFromUser());
            switch (command) {
                case CHECK_HAND -> sendToUser(players[currentPlayer].ShowHand().toArray(new String[0]));
                case CHECK_TABLE -> {
                    if (table.size() == 0) {
                        sendToUser(new String[]{"Table is empty"});
                        continue;
                    }
                    for (Tuple card : table) {
                        sendToUser(new String[]{card.toString()});
                    }
                }
                case CHECK_TRUMP -> sendToUser(new String[]{trump.cardSuitAndRank()});
                case THROW_CARD -> {
                    sendToUser(new String[]{"What card you want to throw?"});
                    sendToUser(players[currentPlayer].ShowHand().toArray(new String[0]));
                    sendToUser(new String[]{"0. Back."});
                    int numberOfCardOnHand = Integer.parseInt(getFromUser()) - 1;
                    if (numberOfCardOnHand == -1) continue;
                    CardImpl playerCard = players[currentPlayer].hand.get(numberOfCardOnHand);
                    if(turn == AttackOrDefend.ATTACK) {
                        if (checkMoveCorrectness(playerCard)) {
                            sendToUser(new String[]{"Try another card"});
                            continue;
                        }
                        table.add(new Tuple(playerCard));
                        players[currentPlayer].RemoveCard(numberOfCardOnHand);
                        possiblePass = true;
                        uncoveredCard++;
                        if (table.size() == 6) {
                            sendToUser(new String[]{"Table is full!"});
                        }
                        sendToUser(new String[]{"Is it all? y/n"});
                        String answer = getFromUser();
                        if (answer.equals("y")) {
                            return true;
                        }
                    }
                    else{
                        sendToUser(new String[]{"Where you want to throw it?"});
                        for (int i = 0; i < table.size(); i++) {
                            if (table.get(i).second != null) continue;
                            sendToUser(new String[]{i + 1 + ". " + table.get(i).toString()});
                        }
                        int numberOfCardOnTable = Integer.parseInt(getFromUser()) - 1;
                        table.get(numberOfCardOnTable).Cover(playerCard);
                        if (table.get(numberOfCardOnTable).second == null) continue;
                        players[currentPlayer].RemoveCard(numberOfCardOnHand);
                        uncoveredCard--;
                        if (uncoveredCard == 0) return false;
                    }
                }
                case PASS -> {
                    if(turn == AttackOrDefend.ATTACK){
                        if (possiblePass) return true;
                        sendToUser(new String[]{"It's only start of set!"});
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

        public void Cover(CardImpl second) {
            if (first.CardSuit == trump.CardSuit) {
                if (second.CardSuit == trump.CardSuit && first.CardRank.ordinal() < second.CardRank.ordinal()) {
                    this.second = second;
                }
            } else if (second.CardSuit == trump.CardSuit) {
                this.second = second;
            } else if (first.CardRank.ordinal() < second.CardRank.ordinal() && first.CardSuit == second.CardSuit) {
                this.second = second;
            } else {
                sendToUser(new String[]{"Not possible turn!"});
            }
        }


        public String toString() {
            if (second == null) return first.cardSuitAndRank() + " \\ " + "Nothing";
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