package Images;

import com.common.card.Rank;
import com.common.card.Suit;
import com.games.fool.TupleOfCard;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageWorker {

    HashMap<String,BufferedImage> cards;
    String pathName = "src\\Images\\Cards\\";

    public ImageWorker(){
        Suit[] arraySuit = new Suit[]{Suit.CLUBS,Suit.DIAMOND,Suit.HEARTS,Suit.SPADES};
        Rank[] arrayRank = new Rank[]{Rank.TWO,Rank.THREE,Rank.FOUR,Rank.FIVE,Rank.SIX,Rank.SEVEN,Rank.EIGHT,Rank.NINE,Rank.TEN,Rank.JACK,Rank.LADY,Rank.KING,Rank.ACE};
        cards = new HashMap<>();
        for (Suit suit : arraySuit) {
            for (Rank rank : arrayRank) {
                try {

                    File f = new File(pathName +suit.getSuit() + " " + rank.getRank()+".jpg");
                    cards.put(suit.getSuit() + rank.getRank(), ImageIO.read(f));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public BufferedImage drawTable(ArrayList<TupleOfCard> table){
        BufferedImage tableImg = new BufferedImage(800,800,BufferedImage.TYPE_INT_ARGB);

        for(int i = 0; i<table.size();i++){
            TupleOfCard card = table.get(i);
            tableImg.getGraphics().drawImage(cards.get(card.firstCard.CardSuit.getSuit() + " " + card.firstCard.CardRank.getRank()),
                    (i%3)*140,(i/3)*200,133,185,null);
            if(card.secondCard != null){
                tableImg.getGraphics().drawImage(cards.get(card.secondCard.CardSuit.getSuit()+" "+card.secondCard.CardRank.getRank()),
                        (i%3)*140+ 40,(i/3)*200 + 31,133,185,null);
            }
        }
        try {
            ImageIO.write(tableImg,".jpg",new File(pathName+"xyu.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableImg;
    }
}
