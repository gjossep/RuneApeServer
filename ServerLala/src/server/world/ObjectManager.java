package server.world;

import java.util.ArrayList;

import server.model.objects.Object;
import server.util.Misc;
import server.model.players.Client;
import server.Server;

/**
 * @author Sanity
 */

public class ObjectManager {

	public ArrayList<Object> objects = new ArrayList<Object>();
	private ArrayList<Object> toRemove = new ArrayList<Object>();
	public void process() {
		for (Object o : objects) {
			if (o.tick > 0)
				o.tick--;
			else {
				updateObject(o);
				toRemove.add(o);
			}		
		}
		for (Object o : toRemove) {
			if (isObelisk(o.newId)) {
				int index = getObeliskIndex(o.newId);
				if (activated[index]) {
					activated[index] = false;
					teleportObelisk(index);
				}
			}
			objects.remove(o);	
		}
		toRemove.clear();
	}
	
	public void removeObject(int x, int y) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
				c.getPA().object(-1, x, y, 0, 10);			
			}	
		}	
	}
	
	public void updateObject(Object o) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
				c.getPA().object(o.newId, o.objectX, o.objectY, o.face, o.type);			
			}	
		}	
	}
	
	public void placeObject(Object o) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
				if (c.distanceToPoint(o.objectX, o.objectY) <= 60)
					c.getPA().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
			}	
		}
	}
	
	public Object getObject(int x, int y, int height) {
		for (Object o : objects) {
			if (o.objectX == x && o.objectY == y && o.height == height)
				return o;
		}	
		return null;
	}
	
	public void loadObjects(Client c) {
		if (c == null)
			return;
		for (Object o : objects) {
			if (loadForPlayer(o,c))
				c.getPA().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
		}
		loadCustomSpawns(c);
		if (c.distanceToPoint(2813, 3463) <= 60) {
			c.getFarming().updateHerbPatch();
		}
	}
	
	private int[][] customObjects = {{}};
	public void loadCustomSpawns(Client c) {
		c.getPA().checkObjectSpawn(4151, 2605, 3153, 1, 10); //portal home FunPk
		c.getPA().checkObjectSpawn(2619, 2602, 3156, 1, 10); //barrel FunPk
		c.getPA().checkObjectSpawn(1032, 2605, 3156, 2, 10); //warning sign FunPk
		c.getPA().checkObjectSpawn(1032, 2603, 3156, 2, 10); //warning sign FunPk
		c.getPA().checkObjectSpawn(1032, 2602, 3155, 1, 10); //warning sign FunPk
		c.getPA().checkObjectSpawn(1032, 2602, 3153, 1, 10); //warning sign FunPk
		c.getPA().checkObjectSpawn(1032, 2536, 4778, 0, 10); //warning sign donor
		c.getPA().checkObjectSpawn(1032, 2537, 4777, 1, 10); //warning sign donor
		c.getPA().checkObjectSpawn(1032, 2536, 4776, 2, 10); //warning sign donor
		c.getPA().checkObjectSpawn(7315, 2536, 4777, 0, 10); //funpk portals
		c.getPA().checkObjectSpawn(7316, 2605, 3153, 0, 10); //funpk portals
		c.getPA().checkObjectSpawn(4008, 2851, 2965, 1, 10); //spec alter
		c.getPA().checkObjectSpawn(194, 2423, 3525, 0, 10); //Dungeoneering Rock
		c.getPA().checkObjectSpawn(16081, 1879, 4620, 0, 10); //Dungeoneering lvl 1 tele
		c.getPA().checkObjectSpawn(2014, 1921, 4640, 0, 10); //Zombie Minigame Chalice of Eternity
		c.getPA().checkObjectSpawn(16078, 1869, 4622, 0, 10); //Dungeoneering Rope
		c.getPA().checkObjectSpawn(2930, 2383, 4714, 3, 10); //Dungeoneering Boss 1 door
		c.getPA().checkObjectSpawn(1032, 2382, 4714, 1, 10); //warning sign FunPk
		c.getPA().checkObjectSpawn(79, 3044, 5105, 1, 10); //dungie blocker
		c.getPA().checkObjectSpawn(10778, 2867, 9530, 1, 10); //dung floor 4 portal
		c.getPA().checkObjectSpawn(7272, 3233, 9316, 1, 10); //dung floor 5 portal
		c.getPA().checkObjectSpawn(4408, 2869, 9949, 1, 10); //dung floor 6 portalEND
		c.getPA().checkObjectSpawn(410, 1860, 4625, 1, 10); //dung floor 6 portalEND
		c.getPA().checkObjectSpawn(6552, 1859, 4617, 1, 10); //dung floor 6 portalEND
		c.getPA().checkObjectSpawn(7318, 2772, 4454, 1, 10); //dung floor 7 portalEND
		c.getPA().checkObjectSpawn(4412, 1919, 4640, 0, 10); //escape ladder
		c.getPA().checkObjectSpawn(4412, 3048, 5233, 0, 10); //escape ladder
		c.getPA().checkObjectSpawn(4412, 2980, 5111, 0, 10); //escape ladder
		c.getPA().checkObjectSpawn(4412, 2867, 9527, 0, 10); //escape ladder
		c.getPA().checkObjectSpawn(4412, 3234, 9327, 0, 10); //escape ladder
		c.getPA().checkObjectSpawn(4412, 2387, 4721, 0, 10); //escape ladder
		c.getPA().checkObjectSpawn(4412, 2429, 4680, 0, 10); //escape ladder
		c.getPA().checkObjectSpawn(4412, 2790, 9328, 0, 10); //escape ladder
		c.getPA().checkObjectSpawn(4412, 3060, 5209, 0, 10); //escape ladder
		c.getPA().checkObjectSpawn(4412, 3229, 9312, 0, 10); //escape ladder
		c.getPA().checkObjectSpawn(4412, 2864, 9950, 0, 10); //escape ladder
		c.getPA().checkObjectSpawn(4412, 2805, 4440, 0, 10); //escape ladder
		c.getPA().checkObjectSpawn(4412, 2744, 4453, 0, 10); //escape ladder
		c.getPA().checkObjectSpawn(4412, 3017, 5243, 0, 10); //escape ladder
		c.getPA().checkObjectSpawn(4412, 2427, 9411, 0, 10); //escape ladder
		c.getPA().checkObjectSpawn(2465, 2422, 9429, 0, 10); //escape ladder
                c.getPA().checkObjectSpawn(2094, 3032, 9836, 0, 10);
		c.getPA().checkObjectSpawn(16078, 1920, 4636, 0, 10);
		c.getPA().checkObjectSpawn(7315, 2871, 2952, 1, 10);
                c.getPA().checkObjectSpawn(2094, 3033, 9836, 0, 10);
                c.getPA().checkObjectSpawn(2091, 3034, 9836, 0, 10);
                c.getPA().checkObjectSpawn(2091, 3035, 9836, 0, 10);
                c.getPA().checkObjectSpawn(2092, 3036, 9836, 0, 10);
                c.getPA().checkObjectSpawn(2092, 3037, 9836, 0, 10);
				
			///Home Objects!	
		c.getPA().checkObjectSpawn(12356, 2396, 3484, 0, 10); ///Portal at home
		c.getPA().checkObjectSpawn(2996, 2400, 3495, 2, 10);  ///al key chest
		c.getPA().checkObjectSpawn(103, 2400, 3481, 0, 10);  ///Squeal Of Fortune Chest
		c.getPA().checkObjectSpawn(3192, 2403, 3492, 0, 10);  ///hs board
		c.getPA().checkObjectSpawn(11356, 2396, 3492, 0, 10); ///frost dragon portals
		///End of home objects
		c.getPA().checkObjectSpawn(2403, 2847, 3333, 0, 10);
                c.getPA().checkObjectSpawn(2103, 3037, 9836, 0, 10);
                c.getPA().checkObjectSpawn(2103, 3039, 9836, 0, 10);
                c.getPA().checkObjectSpawn(2097, 3040, 9836, 0, 10);
                c.getPA().checkObjectSpawn(2097, 3041, 9836, 0, 10);
                c.getPA().checkObjectSpawn(14859, 3042, 9836, 0, 10);
		c.getPA().checkObjectSpawn(14859, 3043, 9836, 0, 10);
                c.getPA().checkObjectSpawn(3044, 3036, 9831, -1, 10);
		c.getPA().checkObjectSpawn(2213, 3037, 9835, -1, 10);
                c.getPA().checkObjectSpawn(2783, 3034, 9832, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3077, 3495, 1, 10);
		c.getPA().checkObjectSpawn(1277, 2048, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2049, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2050, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2051, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2052, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2053, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2054, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2055, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2056, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2057, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2058, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2059, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2060, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2061, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2062, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2063, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2064, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2065, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2066, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2067, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2068, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2069, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2070, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3244, 0, 10);

		c.getPA().checkObjectSpawn(1277, 2048, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2049, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2050, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2051, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2052, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2053, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2054, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2055, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2056, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2057, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2058, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2059, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2060, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2061, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2062, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2063, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2064, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2065, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2066, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2067, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2068, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2069, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2070, 3243, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3243, 0, 10);

		c.getPA().checkObjectSpawn(1277, 2071, 3245, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3246, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3247, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3248, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3249, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3250, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3251, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3252, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3253, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3254, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3255, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3256, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3257, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3258, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3259, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3260, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3261, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3262, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2071, 3263, 0, 10);

		c.getPA().checkObjectSpawn(1277, 2072, 3244, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3245, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3246, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3247, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3248, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3249, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3250, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3251, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3252, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3253, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3254, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3255, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3256, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3257, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3258, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3259, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3260, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3261, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3262, 0, 10);
		c.getPA().checkObjectSpawn(1277, 2072, 3263, 0, 10);
		///What i have added in!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		c.getPA().checkObjectSpawn(3192, 3084, 3488, 0, 10); //hs board
		c.getPA().checkObjectSpawn(4151, 2605, 3153, 1, 10); //portal home FunPk
		c.getPA().checkObjectSpawn(2619, 2602, 3156, 1, 10); //barrel FunPk
		c.getPA().checkObjectSpawn(1032, 2605, 3156, 2, 10); //warning sign FunPk
		c.getPA().checkObjectSpawn(1032, 2603, 3156, 2, 10); //warning sign FunPk
		c.getPA().checkObjectSpawn(1032, 2602, 3155, 1, 10); //warning sign FunPk
		c.getPA().checkObjectSpawn(1032, 2602, 3153, 1, 10); //warning sign FunPk
		c.getPA().checkObjectSpawn(-1, 3077, 3495, 1, 10);
		c.getPA().checkObjectSpawn(-1, 3077, 3496, 1, 10);
		c.getPA().checkObjectSpawn(-1, 3079, 3501, 1, 10);
				c.getPA().checkObjectSpawn(-1, 3080, 3501, 1, 10);
		c.getPA().checkObjectSpawn(1, 2599, 4777, 1, 10);
		c.getPA().checkObjectSpawn(1, 2599, 4780, 1, 10);	
		c.getPA().checkObjectSpawn(1, 2598, 4780, 1, 10);	
		c.getPA().checkObjectSpawn(1, 2597, 4780, 1, 10);	
		c.getPA().checkObjectSpawn(1, 2597, 4779, 1, 10);	
		c.getPA().checkObjectSpawn(1, 2597, 4778, 1, 10);	
		c.getPA().checkObjectSpawn(1, 2597, 4777, 1, 10);
		c.getPA().checkObjectSpawn(1, 2598, 4777, 1, 10);
		c.getPA().checkObjectSpawn(2286, 2598, 4778, 1, 10);
		c.getPA().checkObjectSpawn(12356, 3094, 3487, 1, 10);
				c.getPA().checkObjectSpawn(2403, 3095, 3487, 2, 10);
	
		c.getPA().checkObjectSpawn(13617, 2044, 4521, 1, 10); //Barrelportal donor	
		
		c.getPA().checkObjectSpawn(411, 3093, 3506, 2, 10); // Curse Prayers

		c.getPA().checkObjectSpawn(13615, 2036, 4518, 0, 10);
		c.getPA().checkObjectSpawn(13620, 2041, 4518, 0, 10);
		c.getPA().checkObjectSpawn(13619, 2031, 4518, 0, 10);

		c.getPA().checkObjectSpawn(6163, 2029, 4527, 1, 10);
		c.getPA().checkObjectSpawn(6165, 2029, 4529, 1, 10);
		c.getPA().checkObjectSpawn(6166, 2029, 4531, 1, 10);

		c.getPA().checkObjectSpawn(410, 3105, 3502, 0, 10); 

		c.getPA().checkObjectSpawn(4874, 3084, 3483, 1, 10);
		c.getPA().checkObjectSpawn(4875, 3085, 3483, 1, 10);
		c.getPA().checkObjectSpawn(4876, 3086, 3483, 0, 10);
		c.getPA().checkObjectSpawn(4877, 3087, 3483, 0, 10);
		c.getPA().checkObjectSpawn(4878, 3088, 3483, 0, 10);

		c.getPA().checkObjectSpawn(1596, 3008, 3850, 1, 0);
		c.getPA().checkObjectSpawn(1596, 3008, 3849, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3040, 10307, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3040, 10308, 1, 0);
		c.getPA().checkObjectSpawn(1596, 3022, 10311, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3022, 10312, 1, 0);
		c.getPA().checkObjectSpawn(1596, 3044, 10341, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3044, 10342, 1, 0);
		c.getPA().checkObjectSpawn(6552, 3097, 3506, 2, 10); //ancient prayers
		c.getPA().checkObjectSpawn(409, 3095, 3506, 2, 10);
		c.getPA().checkObjectSpawn(2213, 3047, 9779, 1, 10);
		c.getPA().checkObjectSpawn(2213, 3080, 9502, 1, 10);
		c.getPA().checkObjectSpawn(1530, 3093, 3487, 1, 10);

                                          //X     Y     ID -> ID X Y
		c.getPA().checkObjectSpawn(1276, 2843, 3442, 0, 10);
		c.getPA().checkObjectSpawn(1281, 2844, 3499, 0, 10);
		c.getPA().checkObjectSpawn(4156, 3083, 3440, 0, 10);
		c.getPA().checkObjectSpawn(1308, 2846, 3436, 0, 10);
		c.getPA().checkObjectSpawn(1309, 2846, 3439, -1, 10);
		c.getPA().checkObjectSpawn(1306, 2850, 3439, -1, 10);
		c.getPA().checkObjectSpawn(320, 3048, 10342, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2844, 3440, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2846, 3437, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2840, 3439, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2841, 3443, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2851, 3438, -1, 10);

		c.getPA().checkObjectSpawn(-1, 2009, 4752, -1, 10);//staffzone
		c.getPA().checkObjectSpawn(-1, 2010, 4752, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2011, 4752, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2012, 4752, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2013, 4752, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2014, 4752, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2015, 4752, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2016, 4752, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2009, 4753, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2010, 4753, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2011, 4753, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2012, 4753, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2013, 4753, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2014, 4753, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2015, 4753, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2016, 4753, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2009, 4754, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2010, 4754, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2011, 4754, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2012, 4754, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2013, 4754, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2014, 4754, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2015, 4754, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2016, 4754, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2009, 4755, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2010, 4755, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2011, 4755, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2012, 4755, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2013, 4755, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2014, 4755, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2015, 4755, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2016, 4755, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2009, 4756, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2010, 4756, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2011, 4756, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2012, 4756, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2013, 4756, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2014, 4756, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2015, 4756, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2016, 4756, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2009, 4757, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2010, 4757, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2011, 4757, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2012, 4757, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2013, 4757, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2014, 4757, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2015, 4757, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2016, 4757, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2009, 4758, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2010, 4758, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2011, 4758, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2012, 4758, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2013, 4758, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2014, 4758, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2015, 4758, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2016, 4758, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2008, 4752, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2007, 4752, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2006, 4752, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2005, 4752, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2004, 4752, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2003, 4752, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2008, 4752, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2007, 4752, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2006, 4753, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2005, 4753, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2004, 4753, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2003, 4753, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2008, 4753, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2007, 4753, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2006, 4753, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2005, 4754, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2004, 4754, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2003, 4754, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2008, 4754, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2007, 4754, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2006, 4754, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2005, 4754, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2004, 4754, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2003, 4755, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2008, 4755, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2007, 4755, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2006, 4755, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2005, 4755, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2004, 4755, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2003, 4755, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2008, 4755, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2007, 4756, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2006, 4756, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2005, 4756, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2004, 4756, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2003, 4756, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2008, 4756, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2007, 4756, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2006, 4756, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2005, 4757, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2004, 4757, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2003, 4757, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2008, 4757, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2007, 4757, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2006, 4757, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2005, 4757, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2004, 4757, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2003, 4758, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2008, 4758, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2007, 4758, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2006, 4758, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2005, 4758, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2004, 4758, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2003, 4758, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2016, 4758, -1, 10);

		c.getPA().checkObjectSpawn(2213, 2010, 4748, 0, 10);//addeditems
		c.getPA().checkObjectSpawn(2213, 2009, 4748, 0, 10);
		c.getPA().checkObjectSpawn(2864, 2005, 4755, -1, 10);
		c.getPA().checkObjectSpawn(2864, 2014, 4755, -1, 10);
		///End!!!!!!!!!!!@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
         
       
		//empty bulding spaces
		//1
		c.getPA().checkObjectSpawn(11214, 2069, 3247, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2065, 3247, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2061, 3247, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2057, 3247, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2053, 3247, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2049, 3247, 0, 10);
		//2
		c.getPA().checkObjectSpawn(11214, 2067, 3248, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2063, 3248, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2059, 3248, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2055, 3248, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2051, 3248, 0, 10);
		//1
		c.getPA().checkObjectSpawn(11214, 2069, 3249, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2065, 3249, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2061, 3249, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2057, 3249, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2053, 3249, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2049, 3249, 0, 10);
		//2
		c.getPA().checkObjectSpawn(11214, 2067, 3250, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2063, 3250, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2059, 3250, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2055, 3250, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2051, 3250, 0, 10);
		//1
		c.getPA().checkObjectSpawn(11214, 2069, 3251, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2065, 3251, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2061, 3251, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2057, 3251, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2053, 3251, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2049, 3251, 0, 10);
		//2
		c.getPA().checkObjectSpawn(11214, 2067, 3252, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2063, 3252, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2059, 3252, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2055, 3252, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2051, 3252, 0, 10);
		//1
		c.getPA().checkObjectSpawn(11214, 2069, 3253, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2065, 3253, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2061, 3253, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2057, 3253, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2053, 3253, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2049, 3253, 0, 10);
		//2
		c.getPA().checkObjectSpawn(11214, 2067, 3254, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2063, 3254, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2059, 3254, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2055, 3254, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2051, 3254, 0, 10);
		//1
		c.getPA().checkObjectSpawn(11214, 2069, 3255, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2065, 3255, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2061, 3255, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2057, 3255, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2053, 3255, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2049, 3255, 0, 10);
		//2
		c.getPA().checkObjectSpawn(11214, 2067, 3256, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2063, 3256, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2059, 3256, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2055, 3256, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2051, 3256, 0, 10);
		//1
		c.getPA().checkObjectSpawn(11214, 2069, 3257, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2065, 3257, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2061, 3257, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2057, 3257, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2053, 3257, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2049, 3257, 0, 10);
		//2
		c.getPA().checkObjectSpawn(11214, 2067, 3258, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2063, 3258, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2059, 3258, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2055, 3258, 0, 10);
		c.getPA().checkObjectSpawn(11214, 2051, 3258, 0, 10);
		c.getPA().checkObjectSpawn(-1, 3077, 3496, 1, 10);
		c.getPA().checkObjectSpawn(-1, 3079, 3501, 1, 10);
		c.getPA().checkObjectSpawn(-1, 3080, 3501, 1, 10);
		c.getPA().checkObjectSpawn(1, 2599, 4777, 1, 10);
		c.getPA().checkObjectSpawn(1, 2599, 4780, 1, 10);	
		c.getPA().checkObjectSpawn(1, 2598, 4780, 1, 10);	
		c.getPA().checkObjectSpawn(1, 2597, 4780, 1, 10);	
		c.getPA().checkObjectSpawn(1, 2597, 4779, 1, 10);	
		c.getPA().checkObjectSpawn(1, 2597, 4778, 1, 10);	
		c.getPA().checkObjectSpawn(1, 2597, 4777, 1, 10);
		c.getPA().checkObjectSpawn(1, 2598, 4777, 1, 10);
		c.getPA().checkObjectSpawn(2286, 2598, 4778, 1, 10);
		c.getPA().checkObjectSpawn(12356, 2845, 2957, 1, 10);
				c.getPA().checkObjectSpawn(2403, 2844, 2957, 2, 10);
		c.getPA().checkObjectSpawn(2996, 2854, 2962, 1, 10);//al key chest
	    c.getPA().checkObjectSpawn(14859, 2520, 4773, 0, 10);//runite ore donor.
		c.getPA().checkObjectSpawn(14859, 2518, 4775, 0, 10);//runite ore donor.
		c.getPA().checkObjectSpawn(14859, 2518, 4774, 0, 10);//runite ore donor.
		c.getPA().checkObjectSpawn(13617, 2527, 4770, 2, 10); //Barrelportal donor	

		
		c.getPA().checkObjectSpawn(13615, 2525, 4770, 2, 10); // hill giants donor
		c.getPA().checkObjectSpawn(13620, 2523, 4770, 2, 10); // steel drags donor
		c.getPA().checkObjectSpawn(13619, 2521, 4770, 2, 10); // tormented demons donor


		c.getPA().checkObjectSpawn(6163, 2029, 4527, 1, 10);
		c.getPA().checkObjectSpawn(6165, 2029, 4529, 1, 10);
		c.getPA().checkObjectSpawn(6166, 2029, 4531, 1, 10);

		c.getPA().checkObjectSpawn(410, 2864, 2955, 1, 10); 

		c.getPA().checkObjectSpawn(1596, 3008, 3850, 1, 0);
		c.getPA().checkObjectSpawn(1596, 3008, 3849, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3040, 10307, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3040, 10308, 1, 0);
		c.getPA().checkObjectSpawn(1596, 3022, 10311, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3022, 10312, 1, 0);
		c.getPA().checkObjectSpawn(1596, 3044, 10341, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3044, 10342, 1, 0);
		c.getPA().checkObjectSpawn(6552, 2842, 2954, 1, 10); //ancient prayers
		c.getPA().checkObjectSpawn(409, 2530, 4779, 3, 10);
		c.getPA().checkObjectSpawn(2213, 3047, 9779, 1, 10);
		c.getPA().checkObjectSpawn(2213, 3080, 9502, 1, 10);
		c.getPA().checkObjectSpawn(2213, 2516, 4780, 1, 10);
		c.getPA().checkObjectSpawn(2213, 2516, 4775, 1, 10);
		c.getPA().checkObjectSpawn(1530, 3093, 3487, 1, 10);

                                          //X     Y     ID -> ID X Y
		c.getPA().checkObjectSpawn(1278, 2843, 3442, 0, 10);
		c.getPA().checkObjectSpawn(1281, 2844, 3499, 0, 10);
		c.getPA().checkObjectSpawn(4156, 3083, 3440, 0, 10);
		c.getPA().checkObjectSpawn(1308, 2846, 3436, 0, 10);
		c.getPA().checkObjectSpawn(1309, 2846, 3439, -1, 10);
		c.getPA().checkObjectSpawn(1306, 2850, 3439, -1, 10);
		c.getPA().checkObjectSpawn(2728, 2429, 9416, 0, 10);//cooking range dung!
		c.getPA().checkObjectSpawn(320, 3048, 10342, 0, 10);
				c.getPA().checkObjectSpawn(104, 2522, 4780, 1, 10); //Donatorchest
		c.getPA().checkObjectSpawn(-1, 2844, 3440, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2846, 3437, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2840, 3439, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2841, 3443, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2851, 3438, -1, 10);
		
		//  Disabled for now - Need location
		//c.getPA().checkObjectSpawn(8972, 2580, 3102, 0, 10); //Strykeworms portal at spawn
		
		// New spawn - Yanille
		
		c.getPA().checkObjectSpawn(4421, 2612, 3103, -1, 10); //barricades at spawn-wall
		c.getPA().checkObjectSpawn(4421, 2613, 3102, -1, 10); //barricades at spawn-wall
		c.getPA().checkObjectSpawn(4421, 2614, 3101, -1, 10); //barricades at spawn-wall
		c.getPA().checkObjectSpawn(4421, 2615, 3106, -1, 10); //barricades at spawn-wall
		c.getPA().checkObjectSpawn(4421, 2616, 3105, -1, 10); //barricades at spawn-wall
		c.getPA().checkObjectSpawn(4421, 2617, 3104, -1, 10); //barricades at spawn-wall
		c.getPA().checkObjectSpawn(409, 2602, 3085, 0, 10); //prayer altar at spawn
		c.getPA().checkObjectSpawn(411, 2604, 3085, 0, 10); //chaos altar at spawn
		c.getPA().checkObjectSpawn(6552, 2606, 3085, 0, 10); //ancient altar at spawn
		c.getPA().checkObjectSpawn(410, 2615, 3098, -1, 10); //round altar at spawn
		c.getPA().checkObjectSpawn(1032, 2610, 3104, 2, 10); //warning sign spawn fun pk
		c.getPA().checkObjectSpawn(1032, 2609, 3103, 2, 10); //warning sign spawn fun pk
		c.getPA().checkObjectSpawn(1032, 2611, 3103, 2, 10); //warning sign spawn fun pk
		c.getPA().checkObjectSpawn(7315, 2610, 3103, 2, 10); //funpk portals
		c.getPA().checkObjectSpawn(3192, 2606, 3101, 1, 10);  ///highscore board
		c.getPA().checkObjectSpawn(4874, 2619, 3095, 1, 10); //Home Stalls lvl 1 - Thieving
		c.getPA().checkObjectSpawn(4875, 2619, 3094, 1, 10); //Home Stalls lvl 25 - Thieving
		c.getPA().checkObjectSpawn(4876, 2619, 3093, 0, 10); //Home Stalls lvl 50 - Thieving
		c.getPA().checkObjectSpawn(4877, 2619, 3092, 0, 10); //Home Stalls lvl 75- Thieving
		c.getPA().checkObjectSpawn(4878, 2619, 3091, 0, 10); //Home Stalls lvl 90 - Thieving
		c.getPA().checkObjectSpawn(1281, 2598, 3093, 0, 10); //A tree at home, close to bank
		c.getPA().checkObjectSpawn(1306, 2617, 3084, 0, 10); //A magic tree, behind bank
		c.getPA().checkObjectSpawn(103, 2602, 3091, 1, 10);  ///Squeal Of Fortune Chest
		c.getPA().checkObjectSpawn(11356, 2581, 3102, 0, 10); ///frost dragon portals at spawn
		c.getPA().checkObjectSpawn(1032, 2580, 3102, -2, 10); //warning sign frost portal
		c.getPA().checkObjectSpawn(1032, 2583, 3102, -2, 10); //warning sign frost portal
		
		
		//Skilling Area
		
		c.getPA().checkObjectSpawn(3044, 2853, 3424, -1, 10); //Furnace 
		c.getPA().checkObjectSpawn(2090, 2853, 3433, -1, 10); //crappy ores 1
		c.getPA().checkObjectSpawn(2094, 2856, 3432, -1, 10); //crappy ores 2
		c.getPA().checkObjectSpawn(2092, 2855, 3430, -1, 10); //crappy ores 3
		c.getPA().checkObjectSpawn(2096, 2858, 3433, -1, 10); //crappy ores 4
		c.getPA().checkObjectSpawn(2102, 2857, 3427, -1, 10); //mithril ore
		c.getPA().checkObjectSpawn(2105, 2858, 3430, 0, 10); //adamite ore
		c.getPA().checkObjectSpawn(14859, 2863, 3428, -1, 10); //rune ore
		c.getPA().checkObjectSpawn(2728, 2849, 3428, 0, 10); //cooking stove
		c.getPA().checkObjectSpawn(2783, 2851, 3426, -1, 10); //anvil
		c.getPA().checkObjectSpawn(2213, 2855, 3440, 0, 10); //bank booth
		c.getPA().checkObjectSpawn(4874, 2839, 3440, 1, 10); //Skill Stalls lvl 1 - Thieving
		c.getPA().checkObjectSpawn(4875, 2839, 3441, 1, 10); //Skill Stalls lvl 25 - Thieving
		c.getPA().checkObjectSpawn(4876, 2839, 3442, 0, 10); //Skill Stalls lvl 50 - Thieving
		c.getPA().checkObjectSpawn(4877, 2839, 3443, 0, 10); //Skill Stalls lvl 75- Thieving
		c.getPA().checkObjectSpawn(4878, 2839, 3444, 0, 10); //Skill Stalls lvl 90 - Thieving
		
		//Donater Zone
		//cords = 2529,4779 - For donator island
		
		
		
	 if (c.heightLevel == 0) {
			c.getPA().checkObjectSpawn(2492, 2911, 3614, 1, 10);
		 }else{
			c.getPA().checkObjectSpawn(-1, 2911, 3614, 1, 10); 
	}
	}
	
	public final int IN_USE_ID = 14825;
	public boolean isObelisk(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return true;
		}
		return false;
	}
	public int[] obeliskIds = {14829,14830,14827,14828,14826,14831};
	public int[][] obeliskCoords = {{3154,3618},{3225,3665},{3033,3730},{3104,3792},{2978,3864},{3305,3914}};
	public boolean[] activated = {false,false,false,false,false,false};
	
	public void startObelisk(int obeliskId) {
		int index = getObeliskIndex(obeliskId);
		if (index >= 0) {
			if (!activated[index]) {
				activated[index] = true;
				addObject(new Object(14825, obeliskCoords[index][0], obeliskCoords[index][1], 0, -1, 10, obeliskId,16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4, obeliskCoords[index][1], 0, -1, 10, obeliskId,16));
				addObject(new Object(14825, obeliskCoords[index][0], obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId,16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4, obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId,16));
			}
		}	
	}
	
	public int getObeliskIndex(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return j;
		}
		return -1;
	}
	
	public void teleportObelisk(int port) {
		int random = Misc.random(5);
		while (random == port) {
			random = Misc.random(5);
		}
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
				int xOffset = c.absX - obeliskCoords[port][0];
				int yOffset = c.absY - obeliskCoords[port][1];
				if (c.goodDistance(c.getX(), c.getY(), obeliskCoords[port][0] + 2, obeliskCoords[port][1] + 2, 1)) {
					c.getPA().startTeleport2(obeliskCoords[random][0] + xOffset, obeliskCoords[random][1] + yOffset, 0);
				}
			}		
		}
	}
	
	public boolean loadForPlayer(Object o, Client c) {
		if (o == null || c == null)
			return false;
		return c.distanceToPoint(o.objectX, o.objectY) <= 60 && c.heightLevel == o.height;
	}
	
	public void addObject(Object o) {
		if (getObject(o.objectX, o.objectY, o.height) == null) {
			objects.add(o);
			placeObject(o);
		}	
	}




}