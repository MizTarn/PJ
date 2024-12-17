package manager;

public interface IGameManager<T> {

	public boolean isGameStarted();

	public void setGameStarted(boolean gameStarted);

	public PlayerManager<T> getPlayerManager();

	public void setPlayerManager(PlayerManager<T> playerManager);

	public int numberPlayers();

	public boolean checkWin();

	public TurnManager<T> getTurnManager();

	public void setTurnManager(TurnManager<T> turnManager);

	public void startGame();

	public int whoMakeFirstMove();

	public void changeMove();

	public String getPlayerCard(int indexPlayer);

	public String getEndGame();

	public String getGameStateAndPermission(int i);

	public String getGameStart(int i);
	
}
