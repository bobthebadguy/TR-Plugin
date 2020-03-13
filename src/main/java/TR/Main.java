package TR;

import arc.*;
import arc.math.Mathf;
import arc.util.*;
import mindustry.*;
import arc.graphics.Color;
import arc.util.CommandHandler;
import mindustry.content.Bullets;
import mindustry.entities.bullet.LiquidBulletType;
import mindustry.entities.effect.Lightning;
import mindustry.entities.traits.TeamTrait;
import mindustry.entities.type.Player;
import mindustry.game.Team;
import mindustry.game.Teams;
import mindustry.gen.Call;
import mindustry.plugin.Plugin;
import mindustry.entities.type.Bullet;
import mindustry.world.WorldContext;



import static arc.math.Mathf.*;
import static mindustry.Vars.*;

public class Main extends Plugin implements Runnable{

    int i;
    private int uid;
    private float trapx;
    private float trapy;
    private float playerspeed;
    private float prevRespawnTime;

    /*    //register event handlers and create variables in the constructor
        public Main(){
            //listen for a block selection event
            Events.on(BuildSelectEvent.class, event -> {
                if(!event.breaking && event.builder != null && event.builder.buildRequest() != null && event.builder.buildRequest().block == Blocks.thoriumReactor && event.builder instanceof Player){
                    //send a message to everyone saying that this player has begun building a reactor
                    Call.sendMessage("[scarlet]ALERT![] " + ((Player)event.builder).name + " has begun building a reactor at " + event.tile.x + ", " + event.tile.y);
                }
            });
        }
    */
 /*   //register commands that run on the server
    @Override
    public void registerServerCommands(CommandHandler handler){
        handler.register("reactors", "List all thorium reactors in the map.", args -> {
            for(int x = 0; x < Vars.world.width(); x++){
                for(int y = 0; y < Vars.world.height(); y++){
                    //loop through and log all found reactors
                    if(Vars.world.tile(x, y).block() == Blocks.thoriumReactor){
                        Log.info("Reactor at {0}, {1}", x, y);
                    }
                }
            }
        });
    }

 public Main(){
            Call.sendMessage("test");
        }
*/
    public void run() {
        for (int i = 1; i <= world.getMap().height * world.getMap().width / 125; i++) {
            Call.sendMessage("starting loop number " + i + " of " + (world.getMap().height * world.getMap().width / 100));
            int xpos = Mathf.random(1, world.getMap().width - 1) * tilesize;
            int ypos = Mathf.random(1, world.getMap().height - 1) * tilesize;
            try {
                for (i = 1; i <= 360; i = i + 45) {
                    Lightning.create(Team.blue, Color.white, 25, xpos, ypos, i, 20);
                }
            } catch(Exception e) {
                Call.sendMessage("Lightning.create error: " + e);
            }
            try {
                Thread.sleep( Mathf.random(1, 500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //register commands that player can invoke in-game
    @Override
    public void registerClientCommands(CommandHandler handler) {

        //register a simple reply command
        handler.<Player>register("tr", "<info> [1] [2]", "Additional commands", (arg, player) -> {
            if (!player.isAdmin) {
                player.sendMessage("Admins only.");
                return;
            }

            switch (arg[0]) {
                case "info":
                    player.sendMessage("\tCommands:" +
                            "\ninfo                Lists commands." +
                            "\nsmite               Smites player." +
                            "\nlightning           Summons a lightning storm." +
                            "\nkill                Kills player." +
                            "\ntrap                Traps a player-WIP" +
                            "\ntrapcore            Traps everyone in the core after death <[gray]on/off[]>");
                    break;
                case "smite":
                    uid = Integer.parseInt(arg[1]);
                    try {
                        playerGroup.getByID(uid);
                    } catch (Exception e) {
                        player.sendMessage("Player not found");
                        return;
                    }
                    for (i = 1; i <= 360; i = i + 36) {
                        Lightning.create(Team.crux, Color.white, 100, playerGroup.getByID(uid).x, playerGroup.getByID(uid).y, i, 15);
                    }
                    break;
                case "lightning":
                    try {
                        Integer.parseInt(arg[1]);
                        for (i = 1; i <= Integer.parseInt(arg[1]); i++) {
                            new Thread(new Main()).start();
                        }
                    } catch (Exception e) {
                        new Thread(new Main()).start();
                    }

                    break;
                case "kill":
                    uid = Integer.parseInt(arg[1]);
                    player.mech.buildPower = 0.001f;
                    playerGroup.getByID(uid).dead = true;
                case "trap":
                    uid = Integer.parseInt(arg[1]);
                    trapx = playerGroup.getByID(uid).x;
                    trapy = playerGroup.getByID(uid).y;
                    //find angle of the line between the desired point, trapx and trapy, and the player. Add 180 degrees to point the water stream to push the player the other way.
                    float angle = atan2(playerGroup.getByID(uid).x - trapx, playerGroup.getByID(uid).y - trapy) + 180;
                    playerspeed = sqrt(pow(playerGroup.getByID(uid).getDeltaX(), 2) + pow(playerGroup.getByID(uid).getDeltaY(), 2)) * 2;
                    //find distance from the point we are pushing the player back to.
                    float dist = Mathf.len(trapx - playerGroup.getByID(uid).x, trapy - playerGroup.getByID(uid).y);
                    //Bullet.create(Bullets.waterShot, null, 10 * cosDeg(angle), 10 * sinDeg(angle), angle);
                    for (i = 1; i <= 200; i++) {
                        Call.createBullet(Bullets.waterShot, Team.crux, cosDeg(angle), sinDeg(angle), angle, 1f, 1f);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace(); //wtf what interruped it
                        }
                    }
                    break;
                case "trapcore":
                    switch (arg[1]) {
                        case "on":
                            prevRespawnTime = state.rules.respawnTime;
                            state.rules.respawnTime = 100000000;
                            break;
                        case "off":
                            state.rules.respawnTime = prevRespawnTime;
                            break;
                        default:
                            player.sendMessage("Trapcore: on or off.");
                    }
                    break;
                default:
                    player.sendMessage("That's not an option. Use /tr info for options.");
            }

        });
    }

/*    private int parseName(String name) {
        int uid;
        uid = playerGroup.getID("bob");
        return this.uid;
    }*/
}
