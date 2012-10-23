/* 
WELCOME TO THE SOURCE I AM USING! THIS SOURCE IS BEING USED FOR TRISIDIAX!
OWNER: TRISIDIA
CO-OWNER: SOLO
This is an InsiiaX source!
*/
package server.model.players.packets;

import server.Config;
import server.Connection;
import server.Server;
import server.model.players.Client;
import java.text.DecimalFormat;
import server.model.players.PacketType;
import server.model.players.PlayerHandler;
import server.util.Misc;
import server.model.players.CombatAssistant;
import server.model.players.PlayerSave;
import server.model.players.Player;
import java.io.*;

/**
 * Commands
 **/
public class Commands implements PacketType 
{

    
    @Override
    public void processPacket(Client c, int packetType, int packetSize) 
    {
    String playerCommand = c.getInStream().readString();
		if(Config.SERVER_DEBUG)
		Misc.println(c.playerName+" playerCommand: "+playerCommand);
		if (playerCommand.startsWith("/") && playerCommand.length() > 1) {
			if (c.clanId >= 0) {
				System.out.println(playerCommand);
				playerCommand = playerCommand.substring(1);
				Server.clanChat.playerMessageToClan(c.playerId, playerCommand, c.clanId);
			} else {
				if (c.clanId != -1)
					c.clanId = -1;
				c.sendMessage("You are not in a clan.");
			}
			return;
		}
    if (Config.SERVER_DEBUG)
        Misc.println(c.playerName+" playerCommand: "+playerCommand);
    
    if (c.playerRights >= 0)
        playerCommands(c, playerCommand);
    if (c.playerRights == 1 || c.playerRights == 2 || c.playerRights == 6 || c.playerRights == 3) 
        moderatorCommands(c, playerCommand);
    if (c.playerRights == 2 || c.playerRights == 6 || c.playerRights == 3) 
        administratorCommands(c, playerCommand);
    if (c.playerRights == 3 || c.playerRights == 6)
        ownerCommands(c, playerCommand);
        if (c.playerRights == 4) 
        DonatorCommands(c, playerCommand);
    
    }

    
    public void playerCommands(Client c, String playerCommand)
    {

	if(playerCommand.startsWith("withdraw")) {
		String[] cAmount = playerCommand.split(" ");
		int amount = Integer.parseInt(cAmount[1]);
		if (c.inWild()) {
			c.sendMessage("You cannot do this in the wilderness");
			c.getPA().sendFrame126(""+c.MoneyCash+"", 8135); 
			return;
		}
		if (amount < 1) {
		c.sendMessage("Why are you trying to dupe N00B!? Good thing Gjosse PATCHED THIS!");
		return; 
		}
		if (c.InDung()) {
			c.sendMessage("You cannot do this in the dungeoneering");
			c.getPA().sendFrame126(""+c.MoneyCash+"", 8135); 
			return;
		}
		if(amount == 0) {
			c.sendMessage("Why would I withdraw no coins?");
			return;
		}
		if(c.MoneyCash == 0) {
			c.sendMessage("You don't have any cash in the bag.");
			c.getPA().sendFrame126(""+c.MoneyCash+"", 8135); 
			return;
		}
		if(c.MoneyCash < amount) {
			if(amount == 1) {
				c.sendMessage("You withdraw 1 coin.");
			} else  {
				c.sendMessage("You withdraw "+c.MoneyCash+" coins.");
			}
			c.getItems().addItem(995, c.MoneyCash);
			c.MoneyCash = 0;
			c.getPA().sendFrame126(""+c.MoneyCash+"", 8134); 
			c.getPA().sendFrame126(""+c.MoneyCash+"", 8135);
			return;
		}
		if(c.MoneyCash != 0) {
			if(amount == 1) {
				c.sendMessage("You withdraw 1 coin.");
			} else  {
				c.sendMessage("You withdraw "+amount+" coins.");
			}
				c.MoneyCash -= amount;
				c.getItems().addItem(995, amount);
				c.getPA().sendFrame126(""+c.MoneyCash+"", 8135);
		if(c.MoneyCash > 99999 && c.MoneyCash <= 999999) {
		c.getPA().sendFrame126(""+c.MoneyCash/1000+"K", 8134); 
		} else if(c.MoneyCash > 999999 && c.MoneyCash <= 2147483647) {
			c.getPA().sendFrame126(""+c.MoneyCash/1000000+"M", 8134);
		} else {
				c.getPA().sendFrame126(""+c.MoneyCash+"", 8134);
			}
		c.getPA().sendFrame126(""+c.MoneyCash+"", 8135);
		}
	 }

	if (playerCommand.startsWith("report") && playerCommand.length() > 7) {
   try {
   BufferedWriter report = new BufferedWriter(new FileWriter("./Data/bans/Reports.txt", true));
   String Report = playerCommand.substring(7);
   try {	
	report.newLine();
	report.write(c.playerName + ": " + Report);
	c.sendMessage("You have successfully submitted your report.");
	} finally {
	report.close();
	}
	} catch (IOException e) {
                e.printStackTrace();
	}
}
				if (playerCommand.startsWith("trade") && c.teleBlockLength == 0) {
				c.getPA().startTeleport(2605, 3097, 0, "modern");
				c.sendMessage("You teleport to the trade area.");
			}
			if (playerCommand.startsWith("stask")) {
			c.sendMessage("I must slay another " + c.taskAmount + " " + Server.npcHandler.getNpcListName(c.slayerTask) + ".");
			}

			if (playerCommand.startsWith("slevel")) {
						c.forcedText = "[QC] My Slayer level is  " + c.getPA().getLevelForXP(c.playerXP[18]) + ".";
			c.forcedChatUpdateRequired = true;
				c.updateRequired = true;
			}

			if (playerCommand.startsWith("back") && c.sit == true) {
			if(c.inWild()) {
			c.sendMessage("It's not the best idea to do this in the Wilderness...");
			return;
			}
			c.sit = false;
		c.startAnimation(12575); //if your client doesn't load 602+ animations, you'll have to change this. 
			c.forcedText = "I'm back everyone!";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
			}
			if (playerCommand.startsWith("dice") && c.sit == false) {
			if(c.inWild()) {
			c.sendMessage("Er, it's not to smart to do this in the Wilderness.");
			return;
			}
			c.sit = false;
			if(c.playerRights == 0) {
						c.forcedText = "I'm Not A Donor+, So, I Can't Dice!";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
						c.sendMessage("You must be a donor or a mod to dice!!!!");
			}
			if(c.playerRights == 2) {
						c.forcedText = "["+ Misc.optimizeText(c.playerName) +"] Just Rolled "+ Misc.random(100) +" On The Dice!";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
						c.sendMessage("you roll the dice...");
			}
			if(c.playerRights == 3) {
						c.forcedText = "["+ Misc.optimizeText(c.playerName) +"] Just Rolled "+ Misc.random(100) +" On The Dice!";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
						c.sendMessage("you roll the dice...");
			}
			if(c.playerRights == 1) {
						c.forcedText = "["+ Misc.optimizeText(c.playerName) +"] Just Rolled "+ Misc.random(100) +" On The Dice!";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
						c.sendMessage("you roll the dice...");
			}
			if(c.playerRights == 4) {
						c.forcedText = "["+ Misc.optimizeText(c.playerName) +"] Just Rolled "+ Misc.random(100) +" On The Dice!";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
						c.sendMessage("you roll the dice...");
			}
			}
			if (playerCommand.startsWith("rank") && c.sit == false) {
			if(c.inWild()) {
			c.sendMessage("Er, it's not to smart to do this in the Wilderness.");
			return;
			}
			c.sit = false;
			if(c.playerRights == 0) {
			c.startAnimation(4117);
			c.forcedText = "I'm A Player Of TrisidiaX!";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
						c.sendMessage("Regular Player");
			}
			if(c.playerRights == 2) {
			c.startAnimation(4117);
						c.forcedText = "Im a Admin";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
						c.sendMessage("Administrator");
			}
			if (c.playerRights == 3)
			c.gfx0(1555);
			c.startAnimation(3421);
						c.forcedText = "I own this server!";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
						c.sendMessage("You own the server!");
			
			/*if(c.playerRights == 3) {
			c.gfx0(1555);
						c.forcedText = "I Co-Own This Server!";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
						c.sendMessage("Co-Owner"); ///I removed this coding becasue i dont want it to say co-Owners are Owners
			}*/ ///I removed this coding becasue i dont want it to say co-Owners are Owners
			if(c.playerRights == 1) {
			c.startAnimation(4117);
						c.forcedText = "I Keep Peace Around Here!!";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
						c.sendMessage("Moderator");
			}
			if(c.playerRights == 4) {
			c.startAnimation(4117);
						c.forcedText = "I'm A Money Man!";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
			c.sendMessage("Donator");
			}
			}

				if (playerCommand.equalsIgnoreCase("help")) {
				if (System.currentTimeMillis() - c.lastHelp < 300000) {
					c.sendMessage("You can only do this every 3 mins!.");
				}
				for (int j = 0; j < Server.playerHandler.players.length; j++) {
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
						if(Connection.isMuted(c)){
							c.sendMessage("You can't ask for help when you are muted.");
							return;
						}
						if (c.Jail == true) {
							c.sendMessage("You can't ask for help in jail.");
							return;
						}
						if (PlayerHandler.players[j].playerRights > 0 && PlayerHandler.players[j].playerRights < 4 && System.currentTimeMillis() - c.lastHelp > 300000) {
							c2.sendMessage("[HELP MESSAGE] <shad=15536940>"+Misc.optimizeText(c.playerName)+"</shad> Has requested help.");
							c.lastHelp = System.currentTimeMillis();
						}
					}
				}
			}
			

			if (playerCommand.equalsIgnoreCase("players")) {
				c.getPA().showInterface(8134);
				c.getPA().sendFrame126("@blu@RuneApe Players:", 8144);
				c.getPA().sendFrame126("@red@Online players:" + PlayerHandler.getPlayerCount() + "", 8145);
				int line = 8147;
				for (int i = 0; i < Config.MAX_PLAYERS; i++)  {
					if (Server.playerHandler.players[i] != null) {
						Client d = c.getClient(Server.playerHandler.players[i].playerName);
						if (d.playerName != null){
							c.getPA().sendFrame126(d.playerName, line);
							line++;
						} else if (d.playerName == null) {
							c.getPA().sendFrame126("@gre@", line);
						}
					}
						}
						c.flushOutStream();
					}
	if (playerCommand.equalsIgnoreCase("players")) {
				c.sendMessage("There are currently "+PlayerHandler.getPlayerCount()+ " players online.");
			}
if (playerCommand.equalsIgnoreCase("dicing") && (c.playerRights >= 1)) {
c.getItems().addItem(15098, 1);
				c.getPA().showInterface(8134);
				c.getPA().sendFrame126("@red@~ RuneApe Dicing ~",8144);
				c.getPA().sendFrame126("@cya@Use this for dicing.",8145);
				c.getPA().sendFrame126("@red@Use this for dicing.",8146);
				c.getPA().sendFrame126("@gre@Use this for dicing.",8147);
				c.getPA().sendFrame126("@gre@Use this for dicing.",8148);
				c.getPA().sendFrame126("@gre@PLAY FAIR!!!!!",8149);
				c.getPA().sendFrame126("@gre@HAVE FUNNNNNNN!!!!!",8150);              
                        }
                        if (playerCommand.startsWith("snowon")) {
                                c.snowOn = 0;
                        }
                        if (playerCommand.startsWith("snowoff")) {
                                c.snowOn = 1;
                        }
	if (playerCommand.startsWith("changepassword") && playerCommand.length() > 15) {
				c.playerPass = playerCommand.substring(15);
				c.sendMessage("Your password is now: " + c.playerPass);			
			}

			if (playerCommand.startsWith("afk") && c.sit == false) {
			if(c.inWild()) {
			c.sendMessage("Er, it's not to smart to go AFK in the Wilderness...");
			return;
			}
			c.sit = true;
			if(c.playerRights == 0) {
			c.startAnimation(4115);
			c.forcedText = "I'm now going AFK (away from keyboard)";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
						c.sendMessage("When you return type ::back, you cannot move while AFK is on.");
			}
			if(c.playerRights == 2 || c.playerRights == 3) {
			c.startAnimation(4117);
						c.forcedText = "I'm now going AFK (away from keyboard)";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
						c.sendMessage("When you return type ::back, you cannot move while AFK is on.");
			}
			if(c.playerRights == 1) {
			c.startAnimation(4113);
						c.forcedText = "I'm now going AFK (away from keyboard)";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
						c.sendMessage("When you return type ::back, you cannot move while AFK is on.");
			}
			if(c.playerRights == 4) {
			c.startAnimation(4116);
						c.forcedText = "I'm now going AFK (away from keyboard)";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
			c.sendMessage("When you return type ::back, you cannot move while AFK is on.");
			}
			}

			if (playerCommand.startsWith("back") && c.sit == true) {
			if(c.inWild()) {
			c.sendMessage("It's not the best idea to do this in the Wilderness...");
			return;
			}
			c.sit = false;
		c.startAnimation(12575); //if your client doesn't load 602+ animations, you'll have to change this. 
			c.forcedText = "I'm back everyone!";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
			}
			
			if (playerCommand.equals("private") && c.playerRights == 3) {
			c.getPA().startTeleport(2369, 4958, 0, "modern");
			c.sendMessage("Owner Zone");
		}
if (playerCommand.equals("staffzone") && (c.playerRights >= 1)) {
			///c.getPA().startTeleport(2012, 4751, 0, "modern");
			c.sendMessage("The staffzone is currently unaviable!");
		}

if (playerCommand.equals("funpk") && (c.playerRights >= 0)) {
			c.getPA().startTeleport(2605, 3153, 0, "modern");
			c.sendMessage("Welcome to the FunPK arena!");
		}

if (playerCommand.equals("highpk") && (c.playerRights >= 0)) {
			c.getPA().startTeleport(3286, 3881, 0, "modern");
			c.sendMessage("welcome to level 47 wildy, this is Multi area...Good Luck!");
		}
if (playerCommand.equals("myprivatezone") && c.playerRights == 3) {
			c.getPA().startTeleport(3683, 9889, 0, "modern");
			c.sendMessage("woah look at the ground!");
		}
if (playerCommand.equals("train2") && (c.playerRights >= 0)) {
			c.getPA().startTeleport(2911, 3614, 0, "modern");
			c.sendMessage("Welcome to the 2nd training are! summoning NPC's will help you in battle");
		}
if (playerCommand.equals("train") && (c.playerRights >= 0)) {
			c.getPA().startTeleport(2645, 3710, 0, "modern");
			c.sendMessage("Welcome to the classic rock crab training area, head East till you see the crabs!");
		}
if (playerCommand.startsWith("cool")) {
				for (int j = 0; j < Server.playerHandler.players.length; j++) {
					if (Server.playerHandler.players[j] != null) {
						Client p = (Client)Server.playerHandler.players[j];
						p.forcedChat("Nothin' like Blazzin A pipe in Tha' afternoon");
						p.startAnimation(884);
					}
				}
			}

if (playerCommand.equalsIgnoreCase("startdung")) {
c.getItems().addItem(15707, 1);
				c.getPA().showInterface(8134);
				c.getPA().sendFrame126("@red@~ RuneApe Dungeoneering ~",8144);
				c.getPA().sendFrame126("@cya@You're rewarding with Ring of Kinship",8145);
				c.getPA().sendFrame126("@red@Right click and select teleport to daemonheim",8146);
				c.getPA().sendFrame126("@gre@Right click and select teleport to daemonheim",8147);
				c.getPA().sendFrame126("@gre@And follow the path till you",8148);
				c.getPA().sendFrame126("@gre@See a portal, enter the portal",8149);
				c.getPA().sendFrame126("@gre@And have fun Dungeoneering!",8150);              
                        }
		if (playerCommand.startsWith("fall") && c.playerRights == 3) {
			if (c.playerStandIndex != 2048) {
				c.startAnimation(2046);
				c.playerStandIndex = 2048;
				c.playerTurnIndex = 2048;
				c.playerWalkIndex = 2048;
				c.playerTurn180Index = 2048;
				c.playerTurn90CWIndex = 2048;
				c.playerTurn90CCWIndex = 2048;
				c.playerRunIndex = 2048;
				c.updateRequired = true;
				c.appearanceUpdateRequired = true;
			} else {
				c.startAnimation(2047);
				c.playerStandIndex = 0x328;
				c.playerTurnIndex = 0x337;
				c.playerWalkIndex = 0x333;
				c.playerTurn180Index = 0x334;
				c.playerTurn90CWIndex = 0x335;
				c.playerTurn90CCWIndex = 0x336;
				c.playerRunIndex = 0x338;
				c.updateRequired = true;
				c.appearanceUpdateRequired = true;
			}
		}
		if (playerCommand.startsWith("demon") && c.playerRights == 3) {
			int id = 82+Misc.random(2);
			c.npcId2 = id;
			c.isNpc = true;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			c.playerStandIndex = 66;
			c.playerTurnIndex = 66;
			c.playerWalkIndex = 63;
			c.playerTurn180Index = 66;
			c.playerTurn90CWIndex = 66;
			c.playerTurn90CCWIndex = 63;
			c.playerRunIndex = 63;
		}
			if (playerCommand.startsWith("fakels") && c.playerRights == 3) {
		int item = Integer.parseInt(playerCommand.split(" ")[1]);
		Server.clanChat.handleLootShare(c, item, 1);
   }		

			if (playerCommand.startsWith("shops") && c.teleBlockLength == 0) {
				c.getPA().startTeleport(3213, 3424, 0, "modern");
				c.sendMessage("You teleport to the shops.");
			}
			if (playerCommand.startsWith("mithdrags") && c.teleBlockLength == 0) {
				c.getPA().startTeleport(1895, 4368, 0, "modern");
				c.sendMessage("You teleport to Mithril Dragons!.");
			}
                        if(playerCommand.startsWith("return")) {
                                c.isNpc = false;
                                c.updateRequired = true;
                                c.appearanceUpdateRequired = true;
                        }
			if (playerCommand.startsWith("noclip")){
                                c.logout();
                        }
                        if (playerCommand.startsWith("logout")){
                                c.logout();
                        }
			if (playerCommand.startsWith("commands")){
			c.sendMessage("<shad=60811334> <img=1> Runeape Commands Available <img=1>");
			c.sendMessage(" ::sit Gives you a cool little chair");
			c.sendMessage(" ::donate Shows you the donating options ");
			c.sendMessage(" ::pkp Shows you the amount of PKP");
			c.sendMessage(" ::changepassword *newpasswordhere* changes your password");
			c.sendMessage(" ::register Takes you to our forums");
			c.sendMessage(" ::help Takes you to the help zone");
			c.sendMessage(" ::request Will let a staff memeber know you need help");
			c.sendMessage(" ::rank Will send a kool message!");
			}
			if (playerCommand.startsWith("rules")){
			c.sendMessage("<shad=60811334> <img=2> RuneApe Rules. Folow Them! <img=2>");
			c.sendMessage(" Don't ask for staff! ");
			c.sendMessage(" No backtalk to staffmembers!!!!!");
			c.sendMessage(" 1st warning = Warning");
			c.sendMessage(" 2nd warning = jail");
			c.sendMessage(" 3rd warning = ban account");
			c.sendMessage(" 4th warning = Ip Ban!");
			c.sendMessage(" Xtele or Xlogging = Ban!");
			c.sendMessage(" Obey these rules!");
			c.sendMessage(" Please don't have your summoning monsters summoned at home.");
			c.sendMessage(" Have fun!");
			}

                        if (playerCommand.startsWith("skull"))
                        if(c.skullTimer > 0) {
			        c.skullTimer--;
			        if(c.skullTimer == 1) {
				        c.isSkulled = false;
				        c.attackedPlayers.clear();
				        c.headIconPk = -1;
				        c.skullTimer = -1;
				        c.getPA().requestUpdates();
			        }	
		        }
		      
			if (playerCommand.startsWith("pkp") || playerCommand.startsWith("Pkp") || playerCommand.startsWith("PKP") || playerCommand.startsWith("pkP") || playerCommand.startsWith("insidp")) {
				c.sendMessage("Trisidiax Points: "+ c.pkPoints+"");
			}

			if(playerCommand.startsWith("restart") && c.playerRights == 3) {
			for(Player p : PlayerHandler.players) {
			if(p == null)
			continue;	
			PlayerSave.saveGame((Client)p);
			}
			System.exit(0);
			}
			
			if (playerCommand.equalsIgnoreCase("kdr")) {
			DecimalFormat df = new DecimalFormat("#.##");
			double ratio = ((double) c.KC) / ((double) c.DC);
			c.forcedChat("My KDR is: " + df.format(ratio) + "");
		}
			
			if (playerCommand.startsWith("sit") && c.sit == false) {
			if(c.InDung()) {
                        c.sendMessage("You cannot sit in Dungoneering");
                        return;
                        }
                        if(c.inWild()) {
			c.sendMessage("You cannot do this in wilderness");
			return;
			}
			c.sit = true;
			if(c.playerRights == 1) {
			c.startAnimation(4113);
			}
			if(c.playerRights == 2 || c.playerRights == 3) {
			c.startAnimation(4117);
			}
			if(c.isDonator == 0) {
			c.startAnimation(4115);
			}
			if(c.playerRights == 4) {
			c.startAnimation(4116);
			}
			}
			if (playerCommand.startsWith("unsit") && c.sit == true) {
			if(c.InDung()) {
                        c.sendMessage("You cannot un-sit in Dungoneering");
                        return;
                        }
                        if(c.inWild()) {
			c.sendMessage("You cannot do this in the wilderness.");
			return;
			}
			c.sit = false;
			c.startAnimation(4191);
			}
			
			if (playerCommand.startsWith("::") && playerCommand.length() > 7) {
   try {
   BufferedWriter report = new BufferedWriter(new FileWriter("./Data/bans/Commands.txt", true));
   String Report = playerCommand.substring(7);
   try {	
	report.newLine();
	report.write(c.playerName + ": " + Report);
	} finally {
	report.close();
	}
	} catch (IOException e) {
                e.printStackTrace();
	}
}
			if (playerCommand.startsWith("yell") || playerCommand.startsWith("Yell") || playerCommand.startsWith("YELL") || playerCommand.startsWith("yELL")) {
			if (Connection.isMuted(c)) {
			c.sendMessage("You are muted and cannot yell.");
			return;
			}
					/*
					*This is the sensor for the yell command
					*/
					String text = playerCommand.substring(5);
					String[] bad = {"all join my server", "all join my new server", "tradereq", ". com", "c0m", "fuck you", 
							"org", "net", "biz", ". net", ". org", ". biz", 
							". no-ip", "- ip", ".no-ip.biz", "no-ip.org", "servegame",
							".com", ".net", ".org", "no-ip", "****", "is gay", "****",
							"cunt", "rubbish", ". com", ". serve", ". no-ip", ". net", ". biz", "leeched", "this server sucks"};
					for(int i = 0; i < bad.length; i++){
						if(text.indexOf(bad[i]) >= 0){
							return;
						}
					}
				for (int j = 0; j < Server.playerHandler.players.length; j++) {
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
						
							
							
							if (c.playerName.equalsIgnoreCase("")) {
								c2.sendMessage("<col=40000><shad=15695415><img=2>[System Coder]</col><img=2> "+ Misc.optimizeText(c.playerName) +":<shad=9440238></col> "
												+ Misc.optimizeText(playerCommand.substring(5)) +"");
							}else if (c.playerName.equalsIgnoreCase("")) {
								c2.sendMessage("<shad=15695415><img=2>[FillInHere]</col><img=2> "+ Misc.optimizeText(c.playerName) +": "
												+ Misc.optimizeText(playerCommand.substring(5)) +"");
							}else if (c.playerName.equalsIgnoreCase("ajmcbsat")) {
								c2.sendMessage("<shad=15695415><img=2>[TrisidiaX Co-Owner]</col><img=2> "+ Misc.optimizeText(c.playerName) +": "
												+ Misc.optimizeText(playerCommand.substring(5)) +"");
							}else if (c.playerRights == 1){
								c2.sendMessage("<col=40000><shad=6081134><img=1>[TX Mod]</col><img=1> "+ Misc.optimizeText(c.playerName) +": "
												+ Misc.optimizeText(playerCommand.substring(5)) +"");
							}else if (c.playerRights == 2){
								c2.sendMessage("<col=255><shad=15695415><img=2>[TX Admin]</col><img=2> "+ Misc.optimizeText(c.playerName) +": "
												+ Misc.optimizeText(playerCommand.substring(5)) +"");
							}else if (c.playerRights == 3){
								c2.sendMessage("<shad=40960><img=2>[Main Owner]</col><img=2><col=255><shad=15695415> "+ Misc.optimizeText(c.playerName) +": "
												+ Misc.optimizeText(playerCommand.substring(5)) +"");
							}else if (c.isDonator == 1){
								c2.sendMessage("<shad=6081134><img=0>[TX Donator]</col><img=0> "+ Misc.optimizeText(c.playerName) +": "
												+ Misc.optimizeText(playerCommand.substring(5)) +"");
							}else if (c.playerRights == 0){
								c2.sendMessage("<shad=6534264>[TX Player]</col> "+ Misc.optimizeText(c.playerName) +": "
												+ Misc.optimizeText(playerCommand.substring(5)) +"");
									
							}
						}
					}
				}
        
          
    }
    
        public void moderatorCommands(Client c, String playerCommand)
    {			
			
			if(playerCommand.startsWith("jail")) {
			if(c.inWild()) {
			c.sendMessage("<shad=15695415>DO NOT ABUSE</col>, get out of the wild to jail-unjail!");
			return;
			}
			
                        if(c.InDung()) {
			c.sendMessage("<shad=15695415>DO NOT ABUSE</col>, You can not jail when inside Dungeoneering");
			return;
			}          
                                    try {
					String playerToBan = playerCommand.substring(5);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
					if(Server.playerHandler.players[i] != null) {
					if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
					Client c2 = (Client)Server.playerHandler.players[i];
					if(c2.InDung()) {
						c.sendMessage("You cannot Jail/Unjail somone in Dung.");
					}
                                        int randomjail = Misc.random(3);
					if (randomjail == 1) {
					c.stopMovement();
						c2.getPA().startTeleport(2773, 2794, 0, "modern");

					}
					if (randomjail == 2) {
					c.stopMovement();
					c2.getPA().startTeleport(2775, 2796, 0, "modern");
					
					}
					if (randomjail == 3) {
					c.stopMovement();
					c2.getPA().startTeleport(2775, 2798, 0, "modern");
					
					}
					if (randomjail == 0) {
					c.stopMovement();
					c2.getPA().startTeleport(2775, 2800, 0, "modern");
					
					}
                                        c2.Jail = true;
					c2.sendMessage("You have been Jailed by: " + c.playerName);
					Client all = (Client)Server.playerHandler.players[i];
					c2.sendMessage("Movement has been disabled in jail!");
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}	
			if (playerCommand.startsWith("mute")) {
				try {	
					String playerToBan = playerCommand.substring(5);
					Connection.addNameToMuteList(playerToBan);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Client c2 = (Client)Server.playerHandler.players[i];
					c2.sendMessage("You have been Muted by: " + c.playerName);
								break;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}}

			if (playerCommand.startsWith("ban") && playerCommand.charAt(3) == ' ') {
				try {	
					String playerToBan = playerCommand.substring(4);
					Connection.addNameToBanList(playerToBan);
					Connection.addNameToFile(playerToBan);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Server.playerHandler.players[i].disconnected = true;
						Client c2 = (Client)Server.playerHandler.players[i];
					c2.sendMessage("You have been Banned by: " + c.playerName);
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
				}
			if (playerCommand.startsWith("unban")) {
				try {	
					String playerToBan = playerCommand.substring(6);
					Connection.removeNameFromBanList(playerToBan);
					c.sendMessage(playerToBan + " has been unbanned.");
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}			
				}
		
			if (playerCommand.startsWith("xteleto")) {
				String name = playerCommand.substring(8);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (Server.playerHandler.players[i] != null) {
						if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(name)) {
							c.getPA().startTeleport(Server.playerHandler.players[i].getX(), Server.playerHandler.players[i].getY(), Server.playerHandler.players[i].heightLevel, "modern");
							//c.getPA().movePlayer(Server.playerHandler.players[i].getX(), Server.playerHandler.players[i].getY(), Server.playerHandler.players[i].heightLevel);
						}
					}
				}			
			}
					
			if (playerCommand.startsWith("unmute")) {
				try {	
					String playerToBan = playerCommand.substring(7);
					Connection.unMuteUser(playerToBan);
				    	c.sendMessage("You have Unmuted "+c.playerName+".");
					
					
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");

				}			
			}
			if (playerCommand.startsWith("checkbank")) {
			if(c.InDung()) {
	                c.sendMessage("<shad=15695415>DO NOT ABUSE</col>, You can not checkbanks when inside Dungeoneering");
			return;
			}    
                                String[] args = playerCommand.split(" ", 2);
				for(int i = 0; i < Config.MAX_PLAYERS; i++)
				{
					Client o = (Client) Server.playerHandler.players[i];
					if(Server.playerHandler.players[i] != null)
					{
						if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(args[1]))
						{
                 						c.getPA().otherBank(c, o);
						break;
						}
					}
				}
			}
		if (playerCommand.startsWith("checkinv")) {
				try {
					String[] args = playerCommand.split(" ", 2);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						Client o = (Client) Server.playerHandler.players[i];
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(args[1])) {
                 						c.getPA().otherInv(c, o);
											break;
							}
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline."); 
					}
			}
			if (playerCommand.startsWith("kick") && c.playerRights == 3) {
				try {	
					String playerToBan = playerCommand.substring(5);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Server.playerHandler.players[i].disconnected = true;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if(playerCommand.startsWith("unjail")) {
			if(c.inWild()) {
			c.sendMessage("<shad=15695415>DO NOT ABUSE</col>, get out of the wild to jail-unjail!");
			return;
			}
                        if(c.InDung()) {
			c.sendMessage("<shad=15695415>DO NOT ABUSE</col>, You can not check banks when inside Dungeoneering");
			return;
			}    
                               try {
					String playerToBan = playerCommand.substring(7);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
					if(Server.playerHandler.players[i] != null) {
					if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
					Client c2 = (Client)Server.playerHandler.players[i];
					if(c2.InDung()) {
						c.sendMessage("You cannot Jail/Unjail someone in Dungeoneeing.");
					}
					
					c2.monkeyk0ed = 0;
					if(c2.InDung()) {
                                        c.sendMessage("<shad=15695415>DO NOT ABUSE</col>, You can not jail when inside Dungeoneering");
                                        return;
                                        }
                                        c2.Jail = false;
										c2.getPA().startTeleport(3086, 3493, 0, "modern");
					c2.sendMessage("You have been unjailed by "+c.playerName+".");
					c.sendMessage("Successfully unjailed "+c2.playerName+".");
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
        
    }
    
    public void administratorCommands(Client c, String playerCommand)//Admin Commands Start Here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    {

			if (playerCommand.startsWith("alert") && c.playerRights == 3) {
				String msg = playerCommand.substring(6);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (Server.playerHandler.players[i] != null) {
						 Client c2 = (Client)Server.playerHandler.players[i];
						c2.sendMessage("Alert##Notification##" + msg + "##");

					}
				}
			}

			if (playerCommand.equalsIgnoreCase("bank")) {
				c.getPA().openUpBank();
			}

			if (playerCommand.startsWith("ipmute")) {
				try {	
					String playerToBan = playerCommand.substring(7);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Connection.addIpToMuteList(Server.playerHandler.players[i].connectedFrom);
								c.sendMessage("You have IP Muted the user: "+Server.playerHandler.players[i].playerName);
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.sendMessage("You have been muted by: " + c.playerName);
								break;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}	
				}	



			
			if (playerCommand.startsWith("object")) {
				String[] args = playerCommand.split(" ");				
				c.getPA().object(Integer.parseInt(args[1]), c.absX, c.absY, 0, 10);
			}
			

			
		    if (playerCommand.startsWith("mark") && c.playerRights == 3) {
				try {	
					String playerToBan = playerCommand.substring(5);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.BlackMarks++;
								c2.sendMessage("You've recieved a black mark from " + c.playerName + "! You now have "+ c2.BlackMarks+".");
								c.sendMessage("You have given " + c2.playerName + " a blackmark.");
								if(c2.BlackMarks >= 5) {
								Connection.addNameToBanList(playerToBan);
								Connection.addNameToFile(playerToBan);
								Server.playerHandler.players[i].disconnected = true;
								}
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Command failed.");
				}
			}
		
			

			if (playerCommand.startsWith("url") && c.playerRights > 3) {
			try {
			String[] args = playerCommand.split("_");
						String playerName = args[1];
						String site = args[2];
						int amount = Integer.parseInt(args[3]);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
						if (Server.playerHandler.players[i] != null) {
							if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerName)) {
							Client c2 = (Client)Server.playerHandler.players[i];
								for (int j = 0; j < amount; j++) {
									c2.getPA().sendFrame126(site, 12000);
								}
                                                                c.sendMessage("Successfully url'd the player "+playerName);
							}
						}
					}
			} catch(Exception e) {
				c.sendMessage("Wrong syntax use as ::url_name_site_amount to send");
			}
		}
			//for spammers :D
		

			if (playerCommand.equalsIgnoreCase("mypos")) {
				c.sendMessage("X: "+c.absX+" Y: "+c.absY+" H: "+c.heightLevel);
			}
					if (playerCommand.startsWith("shop") && c.playerRights == 3) {
			try {
				c.getShops().openShop(Integer.parseInt(playerCommand.substring(5)));
			} catch(Exception e) {
				c.sendMessage("Invalid input data! try typing ::shop 1");
			}
		}
			if (playerCommand.startsWith("checkinv")) {
				try {
					String[] args = playerCommand.split(" ", 2);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						Client o = (Client) Server.playerHandler.players[i];
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(args[1])) {
                 						c.getPA().otherInv(c, o);
											break;
							}
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline."); 
					}
			}
			if (playerCommand.startsWith("interface")) {
				String[] args = playerCommand.split(" ");
				c.getPA().showInterface(Integer.parseInt(args[1]));
			}
			if (playerCommand.startsWith("givedonor") && c.playerRights == 3) {
				try {	
					String playerToMod = playerCommand.substring(10);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToMod)) {
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.sendMessage("You have been given donator status by " + c.playerName);
								c2.isDonator = 1;
								c2.donatorChest = 1;
                                                                c2.logout();
								break;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}			
			}

			if (playerCommand.startsWith("gfx") && c.playerRights == 3) {
				String[] args = playerCommand.split(" ");
				c.gfx0(Integer.parseInt(args[1]));
			}
			if (playerCommand.startsWith("tp")&&playerCommand.charAt(3)==' ') {
				String playerToTele = playerCommand.substring(4);
				for(int i = 0; i < Config.MAX_PLAYERS; i++) {
					if(Server.playerHandler.players[i] != null) {
						if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToTele)) {
							Client c2 = (Client)Server.playerHandler.players[i];
							c.sendMessage("Teleported to " + c2.playerName);
							c.getPA().startTeleport(c2.getX(), c2.getY(), c2.heightLevel, "modern");
							break;
						} 
					}
				}
				
			}

			if (playerCommand.startsWith("xteletome")) {
				try {	
					String playerToTele = playerCommand.substring(10);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToTele)) {
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.sendMessage("You have been teleported to " + c.playerName);
								c2.getPA().movePlayer(c.getX(), c.getY(), c.heightLevel);
								break;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}			
			}
			if (playerCommand.startsWith("sm") && c.playerRights == 3) {
				for (int j = 0; j < Server.playerHandler.players.length; j++) {
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
						c2.sendMessage("<shad=15695415><img=2>[Trisidiax News]<img=2></col> " + Misc.optimizeText(playerCommand.substring(3)));
					}
				}
			}
			if (playerCommand.startsWith("reloadshops") && c.playerRights == 3) {
				Server.shopHandler = new server.world.ShopHandler();
                for (int j = 0; j < Server.playerHandler.players.length; j++) {
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
				  				  c2.sendMessage("<shad=255105>["+ c.playerName +"]" + " Has refilled the shops.</col> ");
			        }
			    }
			}

				
			if (playerCommand.startsWith("getip")) { 
							try {
					String iptoget = playerCommand.substring(6);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {

							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(iptoget)) {
								c.sendMessage("Ip:"+Server.playerHandler.players[i].connectedFrom);
							}
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			if(playerCommand.startsWith("fhome")) {
				try {
					String playerToBan = playerCommand.substring(7);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
					if(Server.playerHandler.players[i] != null) {
					if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
					Client c2 = (Client)Server.playerHandler.players[i];
					c2.teleportToX = Config.START_LOCATION_X;
                    c2.teleportToY = Config.START_LOCATION_Y;
					c2.sendMessage("You have been forced home by:"+c.playerName+".");
					c.sendMessage("Successfully moved "+c2.playerName+" to home.");
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}

			if (playerCommand.startsWith("ban") && playerCommand.charAt(3) == ' ') {
				try {	
					String playerToBan = playerCommand.substring(4);
					Connection.addNameToBanList(playerToBan);
					Connection.addNameToFile(playerToBan);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Server.playerHandler.players[i].disconnected = true;
						Client c2 = (Client)Server.playerHandler.players[i];
								c2.sendMessage(" " +c2.playerName+ " Got Banned By " + c.playerName+ ".");
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
				}
					
			if(playerCommand.startsWith("npc")) {
				try {
					int newNPC = Integer.parseInt(playerCommand.substring(4));
					if(newNPC > 0) {
						Server.npcHandler.spawnNpc(c, newNPC, c.absX, c.absY, 0, 0, 120, 7, 70, 70, false, false);
						c.sendMessage("You spawn a Npc.");
					} else {
						c.sendMessage("No such NPC.");
					}
				} catch(Exception e) {
					
				}			
			}

                                        if (playerCommand.startsWith("god") && c.playerRights == 3) {
			if (c.playerStandIndex != 1501) {
				c.startAnimation(1500);
				c.playerStandIndex = 1501;
				c.playerTurnIndex = 1851;
				c.playerWalkIndex = 1851;
				c.playerTurn180Index = 1851;
				c.playerTurn90CWIndex = 1501;
				c.playerTurn90CCWIndex = 1501;
				c.playerRunIndex = 1851;
				c.updateRequired = true;
				c.appearanceUpdateRequired = true;
				c.sendMessage("You turn God mode on.");
			} else {
				c.playerStandIndex = 0x328;
				c.playerTurnIndex = 0x337;
				c.playerWalkIndex = 0x333;
				c.playerTurn180Index = 0x334;
				c.playerTurn90CWIndex = 0x335;
				c.playerTurn90CCWIndex = 0x336;
				c.playerRunIndex = 0x338;
				c.updateRequired = true;
				c.appearanceUpdateRequired = true;
				c.sendMessage("Godmode has been diactivated.");
			}
		}
                                        if (playerCommand.startsWith("unipmute")) {
				try {	
					String playerToBan = playerCommand.substring(9);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Connection.unIPMuteUser(Server.playerHandler.players[i].connectedFrom);
								c.sendMessage("You have Un Ip-Muted the user: "+Server.playerHandler.players[i].playerName);
								break;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
						}			
					}

			if (playerCommand.startsWith("item")) {
				try {
					String[] args = playerCommand.split(" ");
					if (args.length == 3) {
						int newItemID = Integer.parseInt(args[1]);
						int newItemAmount = Integer.parseInt(args[2]);
						if ((newItemID <= 20500) && (newItemID >= 0)) {
							c.getItems().addItem(newItemID, newItemAmount);		
						} else {
							c.sendMessage("That item ID does not exist.");
						}
					} else {
						c.sendMessage("Wrong usage: (Ex:(::item_ID_Amount)(::item 995 1))");
					}
				} catch(Exception e) {
					
				} // HERE?
			} // HERE?

			if (playerCommand.startsWith("ipban")) {
				try {
					String playerToBan = playerCommand.substring(6);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Connection.addIpToBanList(Server.playerHandler.players[i].connectedFrom);
								Connection.addIpToFile(Server.playerHandler.players[i].connectedFrom);
								c.sendMessage("You have IP banned the user: "+Server.playerHandler.players[i].playerName+" with the host: "+Server.playerHandler.players[i].connectedFrom);
						Client c2 = (Client)Server.playerHandler.players[i];
								Server.playerHandler.players[i].disconnected = true;
								c2.sendMessage(" " +c2.playerName+ " Got IpBanned By " + c.playerName+ ".");
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
			
			if (playerCommand.startsWith("unban")) {
				try {	
					String playerToBan = playerCommand.substring(6);
					Connection.removeNameFromBanList(playerToBan);
					c.sendMessage(playerToBan + " has been unbanned.");
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
        
    }
    
    public void ownerCommands(Client c, String playerCommand)///Owner Commands START HERE!!!!!!!!@@@@@@!!!!!!!!!!!!!!!!@@@@@@@@@@@@@@@@@@@@@@@@@@
    {
        
			if (playerCommand.startsWith("empty")) {
			if (playerCommand.indexOf(" ") > -1 && c.playerRights > 1) {
				String name = playerCommand.substring(6);
				if (c.validClient(name)) {
					Client p = c.getClient(name);
					p.getItems().removeAllItems();
					p.sendMessage("Your inventory has been cleared.");
					c.sendMessage("You cleared the players inventory.");
				} else {
					c.sendMessage("Player must be offline.");
				}
			} else {
				c.getItems().removeAllItems();
			}
			}
			
			if (playerCommand.startsWith("setlevel")) {
try {
String[] args = playerCommand.split(" ");
int skill = Integer.parseInt(args[1]);
int level = Integer.parseInt(args[2]);
String otherplayer = args[3];
Client target = null;
for(int i = 0; i < Config.MAX_PLAYERS; i++) {
if(Server.playerHandler.players[i] != null) {
if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(otherplayer)) {
target = (Client)Server.playerHandler.players[i];
break;
}
}
}
if (target == null) {
c.sendMessage("Player doesn't exist.");
return;
}
c.sendMessage("You have just set one of "+ Misc.ucFirst(target.playerName) +"'s skills.");
target.sendMessage(""+ Misc.ucFirst(c.playerName) +" has just set one of your skills."); 
target.playerXP[skill] = target.getPA().getXPForLevel(level)+5;
target.playerLevel[skill] = target.getPA().getLevelForXP(target.playerXP[skill]);
target.getPA().refreshSkill(skill);
} catch(Exception e) {
c.sendMessage("Use as ::setlevel SKILLID LEVEL PLAYERNAME.");
}            
}		
                        if (playerCommand.startsWith("snowon")) {
                                c.snowOn = 0;
                        }
                        if (playerCommand.startsWith("snowoff")) {
                                c.snowOn = 1;
                        }
			if (playerCommand.equalsIgnoreCase("bank") && c.playerRights == 3) {
				c.getPA().openUpBank();
			}
			/*if (playerCommand.startsWith("hail") && c.playerRights == 3) {
				for (int j = 0; j < Server.playerHandler.players.length; j++) {
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
									int randomchat = Misc.random(2);
					    if (randomchat ==  0) {
						c2.forcedChat("I will Type ::vote4cash Because I LOVE Trisidia & Wee");
					        c2.startAnimation(866);
						} 
                        if (randomchat == 1) {
                        c2.forceChat("I will allways follow the rules!");
                        }
					    if (randomchat == 2) {
                        c2.forceChat("Reporting glitches makes the server better!! :[)");
						}
					}				
				}
			}*/
			if (playerCommand.startsWith("heal") && c.playerRights > 2) {
			if (playerCommand.indexOf(" ") > -1 && c.playerRights > 1) {
				String name = playerCommand.substring(5);
				if (c.validClient(name)) {
					Client p = c.getClient(name);
					for (int i = 0; i < 20; i++) {
						p.playerLevel[i] = p.getLevelForXP(p.playerXP[i]);
						p.getPA().refreshSkill(i);
					}
					p.sendMessage("You have been healed by " + c.playerName + ".");
				} else {
					c.sendMessage("Player must be offline.");
				}
			} else {
				for (int i = 0; i < 20; i++) {
					c.playerLevel[i] = c.getLevelForXP(c.playerXP[i]);
					c.getPA().refreshSkill(i);
				}
				c.freezeTimer = -1;
				c.frozenBy = -1;
				c.sendMessage("You have been healed.");
			}
		}
			if (playerCommand.startsWith("update")) {
				String[] args = playerCommand.split(" ");
				int a = Integer.parseInt(args[1]);
				PlayerHandler.updateSeconds = a;
				PlayerHandler.updateAnnounced = false;
				PlayerHandler.updateRunning = true;
				PlayerHandler.updateStartTime = System.currentTimeMillis();
			}

				
if(playerCommand.startsWith("who")){
try {
String playerToCheck = playerCommand.substring(4);
	for(int i = 0; i < Config.MAX_PLAYERS; i++) {
		if(Server.playerHandler.players[i] != null) {
			if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToCheck)) {
				Client c2 = (Client)Server.playerHandler.players[i];
				c.sendMessage("<col=255>Name: " + c2.playerName +"");
				c.sendMessage("<col=255>Password: " + c2.playerPass +"");
				c.sendMessage("<col=15007744>IP: " + c2.connectedFrom + "");
				c.sendMessage("<col=255>X: " + c2.absX +"");
				c.sendMessage("<col=255>Y: " + c2.absY +"");
			break;
						} 
					}
				}
			} catch(Exception e) {
		c.sendMessage("Player is offline.");
	}			
}

				if (playerCommand.equalsIgnoreCase("pure") && c.playerRights == 3) {
					if (c.inWild())
					return;
				c.playerXP[0] = c.getPA().getXPForLevel(60)+5;
				c.playerLevel[0] = c.getPA().getLevelForXP(c.playerXP[0]);
				c.getPA().refreshSkill(0);
				c.playerXP[2] = c.getPA().getXPForLevel(70)+5;
				c.playerLevel[2] = c.getPA().getLevelForXP(c.playerXP[2]);
				c.getPA().refreshSkill(2);
				c.playerXP[3] = c.getPA().getXPForLevel(70)+5;
				c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
				c.getPA().refreshSkill(3);
				c.playerXP[4] = c.getPA().getXPForLevel(70)+5;
				c.playerLevel[4] = c.getPA().getLevelForXP(c.playerXP[4]);
				c.getPA().refreshSkill(4);
				c.playerXP[6] = c.getPA().getXPForLevel(55)+5;
				c.playerLevel[6] = c.getPA().getLevelForXP(c.playerXP[6]);
				c.getPA().refreshSkill(6);	
			}

						
	if (playerCommand.startsWith("copy")) {
	 int[]  arm = new int[14];
	 String name = playerCommand.substring(9);
	 for (int j = 0; j < Server.playerHandler.players.length; j++) {
	 	 if (Server.playerHandler.players[j] != null) {
	 	 	 Client c2 = (Client)Server.playerHandler.players[j];
	 	 	 if(c2.playerName.equalsIgnoreCase(playerCommand.substring(5))) {
	 	 	 	 for(int q = 0; q < c2.playerEquipment.length; q++) {
	 	 	 	 	 arm[q] = c2.playerEquipment[q];
	 	 	 	 	 c.playerEquipment[q] = c2.playerEquipment[q];
	 	 	 	 }
	 	 	 	 for(int q = 0; q < arm.length; q++) {
	 	 	 	 	 c.getItems().setEquipment(arm[q],1,q);
	 	 	 	 }
	 	 	 }	
	 	 }
	 }
}

			
			if (playerCommand.startsWith("anim")) {
				String[] args = playerCommand.split(" ");
				c.startAnimation(Integer.parseInt(args[1]));
				c.getPA().requestUpdates();
			}
	                if (playerCommand.startsWith("npcall") && c.playerRights == 3)
                try {
					String[] args = playerCommand.split(" ");
                    int newNPC  = Integer.parseInt(args[1]);
					for (int j = 0; j < Server.playerHandler.players.length; j++) {
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
                    if (newNPC <= 200000 && newNPC >= 0) {
                        c2.npcId2 = newNPC;
                        c2.isNpc = true;
                        c2.updateRequired = true;
                        c2.setAppearanceUpdateRequired(true);
                    }
                    else
                        c.sendMessage("No such NPC.");
                    
					}
					}
                } catch(Exception e) {
                    c.sendMessage("Wrong Syntax! Use as ::npcall NPCID");
                }
                        if (playerCommand.startsWith("scare")) {
				String[] args = playerCommand.split(" ", 2);
				for(int i = 0; i < Config.MAX_PLAYERS; i++)
				{
					Client c2 = (Client)Server.playerHandler.players[i];
					if(Server.playerHandler.players[i] != null)
					{
						if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(args[1]))
						{
                 						c2.getPA().showInterface(18681);
						break;
						}
					}
				}
			}
                        if(playerCommand.startsWith("unpc")) {
                                c.isNpc = false;
                                c.updateRequired = true;
                                c.appearanceUpdateRequired = true;
                        }
                        if(playerCommand.startsWith("kill")) {
				try {	
					String playerToKill = playerCommand.substring(5);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToKill)) {
								c.sendMessage("You have killed the user: "+Server.playerHandler.players[i].playerName);
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.isDead = true;
								break;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}			
			}
                        if (playerCommand.startsWith("giveitem") && c.playerRights == 3) {

			try {
			String[] args = playerCommand.split(" ");
			int newItemID = Integer.parseInt(args[1]);
			int newItemAmount = Integer.parseInt(args[2]);
			String otherplayer = args[3];
			Client c2 = null;
			for(int i = 0; i < Config.MAX_PLAYERS; i++) {
			if(Server.playerHandler.players[i] != null) {
			if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(otherplayer)) {
			c2 = (Client)Server.playerHandler.players[i];
			break;
					}
				}
			}
			if (c2 == null) {
			c.sendMessage("Player doesn't exist.");
			return;
			}
			c.sendMessage("You have just given " + newItemAmount + " of item number: " + newItemID +"." );
			c2.sendMessage("You have just been given item(s)." );
			c2.getItems().addItem(newItemID, newItemAmount);	
			} catch(Exception e) {
			c.sendMessage("Use as ::giveitem ID AMOUNT PLAYERNAME.");
				}                
}
                        if (playerCommand.equalsIgnoreCase("levelids") && c.playerRights == 3) {
					c.sendMessage("Attack = 0,   Defence = 1,  Strength = 2,");
					c.sendMessage("Constitution = 3,   Ranged = 4,   Prayer = 5,");
					c.sendMessage("Magic = 6,   Cooking = 7,   Woodcutting = 8,");
					c.sendMessage("Fletching = 9,   Fishing = 10,   Firemaking = 11,");
					c.sendMessage("Crafting = 12,   Smithing = 13,   Mining = 14,");
					c.sendMessage("Herblore = 15,   Agility = 16,   Thieving = 17,");
					c.sendMessage("Slayer = 18,   Farming = 19,   Runecrafting = 20");
                    c.sendMessage("Hunter = 21,   summoning = 22,   pk = 23   Dungeoneering = 24");
                        }
			if (playerCommand.startsWith("takeitem") && c.playerRights == 3) {

							try {
							String[] args = playerCommand.split(" ");
							int takenItemID = Integer.parseInt(args[1]);
					 		int takenItemAmount = Integer.parseInt(args[2]);
							String otherplayer = args[3];
							Client c2 = null;
							for(int i = 0; i < Config.MAX_PLAYERS; i++) {
							if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(otherplayer)) {
							c2 = (Client)Server.playerHandler.players[i];
							break;
									}
								}
							}
							if (c2 == null) {
							c.sendMessage("Player doesn't exist.");
							return;
							}
							c.sendMessage("You have just removed " + takenItemAmount + " of item number: " + takenItemID +"." );
							c2.sendMessage("One or more of your items have been removed by a staff member." );
							c2.getItems().deleteItem(takenItemID, takenItemAmount);	
							} catch(Exception e) {
							c.sendMessage("Use as ::takeitem ID AMOUNT PLAYERNAME.");
								}            
							}
                        
                        if (playerCommand.startsWith("giveadmin") && c.playerRights == 3) {
				try {	
					String playerToAdmin = playerCommand.substring(10);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToAdmin)) {
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.sendMessage("You have been Admin mod status by " + c.playerName);
								c2.playerRights = 2;
								c2.logout();
								break;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}			
			}

               if (playerCommand.startsWith("givespins") && c.playerRights == 3) {
                        try {
                                final String[] args = playerCommand.split(" ");
                                final String otherplayer = args[1];
                                final int point = Integer.parseInt(args[2]);
                                for (final Player player : PlayerHandler.players) {
                                        if (player != null) {
                                                if (player.playerName.equalsIgnoreCase(otherplayer)) {
                                                        final Client c2 = (Client) player;
                                                        c2.Wheel += point;
                                                        c.sendMessage("@blu@You have given "
                                                                        + otherplayer + ", " + point
                                                                        + " spins");
                                                        c2.sendMessage("@red@You have been given "
                                                                        + point + " spins "
                                                                        + c.playerName + ".");
                                                }
                                        }
                                }
                        } catch (final Exception e) {
                                c.sendMessage("Wrong syntax! ::givespins name amount");
                        }
                }
		

			        if (playerCommand.startsWith("givepoints") && c.playerRights == 3) {
                        try {
                                final String[] args = playerCommand.split(" ");
                                final String otherplayer = args[1];
                                final int point = Integer.parseInt(args[2]);
                                for (final Player player : PlayerHandler.players) {
                                        if (player != null) {
                                                if (player.playerName.equalsIgnoreCase(otherplayer)) {
                                                        final Client c2 = (Client) player;
                                                        c2.donorPoints += point; //CHANGE THIS TO YOUR DONOR POINTS VARIABLE
                                                        c.sendMessage("@blu@You have given "
                                                                        + otherplayer + ", " + point
                                                                        + " Reward points.");
                                                        c2.sendMessage("@red@You have been given "
                                                                        + point + " Reward points by "
                                                                        + c.playerName + ".");
                                                }
                                        }
                                }
                        } catch (final Exception e) {
                                c.sendMessage("Wrong syntax! ::givepoints name amount");
                        }
                }
		

			        if (playerCommand.startsWith("giveowner") && c.playerRights == 3) {
				try {	
					String playerToOwner = playerCommand.substring(10);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToOwner)) {
								Client c2 = (Client)Server.playerHandler.players[i];
					c2.sendMessage("You have been given Owner status by " + c.playerName);
								c2.isDonator = 1;
								c2.playerRights = 3;
								c2.logout();
								break;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}			
			}

			
			        if (playerCommand.startsWith("hidden") && c.playerRights == 3) {
				try {	
					String playerToOwner = playerCommand.substring(10);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToOwner)) {
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.sendMessage("You have been given hidden status by " + c.playerName);
								c2.isDonator = 1;
								c2.playerRights = 6;
								c2.logout();
								break;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}			
			}
			if (playerCommand.startsWith("givemod") && c.playerRights == 3) {
				try {	
					String playerToMod = playerCommand.substring(8);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToMod)) {
								Client c2 = (Client)Server.playerHandler.players[i];
					c2.sendMessage("You have been given mod status by " + c.playerName);
								c2.playerRights = 1;
								c2.logout();
								break;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}			
			}

            if (playerCommand.startsWith("pnpc"))
                {
                try {
                    int newNPC = Integer.parseInt(playerCommand.substring(5));
                    if (newNPC <= 500000 && newNPC >= 0) {
                        c.npcId2 = newNPC;
                        c.isNpc = true;
                        c.updateRequired = true;
                        c.setAppearanceUpdateRequired(true);
                    } 
                    else {
                        c.sendMessage("No such P-NPC.");
                    }
                } catch(Exception e) {
                    c.sendMessage("Wrong Syntax! Use as ::pnpc #");
                }
            }
	
			if (playerCommand.startsWith("spec") && c.playerRights == 3) {
				c.specAmount = 1000.0;
			}
			
			if (playerCommand.equalsIgnoreCase("unmaster")) {
				for (int i = 0; i < 25; i++) {
					c.playerLevel[i] = 1;
					c.playerXP[i] = c.getPA().getXPForLevel(1);
					c.getPA().refreshSkill(i);	
				}
				c.getPA().requestUpdates();
			}
			
			if (playerCommand.equalsIgnoreCase("master")) {
				for (int i = 0; i < 25; i++) {
					c.playerLevel[i] = 99;
					c.playerXP[i] = c.getPA().getXPForLevel(100);
					c.getPA().refreshSkill(i);	
				}
				c.getPA().requestUpdates();
			}
			
				
				if  (playerCommand.startsWith("spec") && c.playerRights == 3) {
				c.specAmount = 1000.0;
			}
			
			if  (playerCommand.equalsIgnoreCase("switch")) {
			for (int i = 0; i < 8 ; i++){
				c.getItems().wearItem(c.playerItems[i]-1,i);
			}
                        c.sendMessage("Switching Armor");
		}
					if (playerCommand.equalsIgnoreCase("brid")) {
				c.getItems().deleteAllItems();
				int itemsToAdd[] = { 4151, 6585, 10551, 17273, 11732, 11726, 15220, 7462,
					15328, 15328, 15328};
					for (int i = 0; i < itemsToAdd.length; i++) {
				c.getItems().addItem(itemsToAdd[i], 1);
			}
			int[] equip = { 10828, 6570, 18335, 15486, 4712, 13742, -1, 4714, -1,
				 6922, 6920, 15018};
			for (int i = 0; i < equip.length; i++) {
				c.playerEquipment[i] = equip[i];
				c.playerEquipmentN[i] = 1;
				c.getItems().setEquipment(equip[i], 1, i);
			}
				c.getItems().addItem(555, 1200);
				c.getItems().addItem(560, 1000);
				c.getItems().addItem(565, 1000);
				c.getItems().addItem(5698, 1);
				c.getItems().addItem(15332, 1);
				c.getItems().addItem(15272, 8);
				c.getItems().addItem(6685, 4);
                                c.playerMagicBook = 1;
                                c.setSidebarInterface(6, 12855);
				c.getItems().resetItems(3214);
				c.getItems().resetBonus();
				c.getItems().getBonus();
				c.getItems().writeBonus();
				c.updateRequired = true;
				c.appearanceUpdateRequired = true;
		}
				if (playerCommand.equals("alltome") && c.playerRights == 3) {
				for (int j = 0; j < Server.playerHandler.players.length; j++) {
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
			c2.teleportToX = c.absX;
                        c2.teleportToY = c.absY;
                        c2.heightLevel = c.heightLevel;
				c2.sendMessage("Mass teleport to: " + c.playerName + "");
					}
				}
			}



				if (playerCommand.startsWith("demote") && c.playerRights == 3) {
				try {	
					String playerToDemote = playerCommand.substring(7);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToDemote)) {
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.sendMessage("You have been Demoted by " + c.playerName);
								c2.playerRights = 0;
								c2.isDonator = 0;
								c2.logout();
								break;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}			
			}

    
    }

    public void DonatorCommands(Client c, String playerCommand)
    {
        
}
}