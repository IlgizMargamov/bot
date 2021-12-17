package telegram;

import com.common.player.BasePlayer;

import java.util.List;

public class Lobby {
    public String m_creator;
    public String m_pin;
    public List<BasePlayer> m_playerList;

    public Lobby(String creator, String pin, List<BasePlayer> playersList) {
        m_creator = creator;
        m_pin = pin;
        m_playerList = playersList;
    }
}
