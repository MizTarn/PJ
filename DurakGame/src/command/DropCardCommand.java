package command;

import card.Card;
import card.HandForDurak;
import manager.PlayerManager;
import manager.Table;
import player.Player;

public class DropCardCommand implements Command {

	private PlayerManager playerManager;
	private Table tableManager;

	public DropCardCommand(PlayerManager playerManager, Table tableManager) {
		this.playerManager = playerManager;
		this.tableManager = tableManager;
	}

	@Override
	public boolean execute(String[] params) throws Exception { // drop_card#card#idPlayer
		Card dropped = new Card(params[1]);
		int idPlayer = Integer.parseInt(params[2]);
		System.out.println("current: " + idPlayer);
		Player player = playerManager.getPlayers().get(idPlayer);
		System.out.println("la bai: " + dropped.toString());
		try {
			System.out.println("vao phan try cua drop_car connection"); 
			System.out.println("player co dang attack khoong " + player.isAttacker());
			System.out.println("defending card co size la ---------------------------: "
					+ tableManager.getDefendingCards().size());
			if (tableManager.canPutInTable(dropped, player.isAttacker())) {
				System.out.println("co the dat la bai");
				if (tableManager.getTable().isEmpty()) {
					System.out.println("ban trong");
					playerManager.setDefendPlayer((idPlayer + 1) % playerManager.getPlayers().size());
//					tableManager.addDefendingCards(dropped);
				}
				if (player.isAttacker()) {
					System.out.println("la duoc them vao defendingcards la : " + dropped);
					tableManager.addDefendingCards(dropped);
				}
				Player p = playerManager.getPlayers().get(idPlayer);
				HandForDurak h = p.getHand();
				System.out.println("truoc khi bo nguoi choi co nhung la: - " + h.toString());
				tableManager.getTable().add(h.getCard(dropped));
				System.out.println("sau khi bo la duoc chon nguoi choi con lai - " + h.toString());
				System.out.println("la bai da duoc dat len ban");
				tableManager.setChangeMove(true);
				System.out.println("thay doi luot choi");
				return true;
			} else {
				System.out.println("khong the chon la bai do dat len");
				tableManager.setChangeMove(false);
				System.out.println("luot choi khong doi");
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}