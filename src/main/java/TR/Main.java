package TR;

import arc.*;
import arc.util.*;
import mindustry.*;
import arc.graphics.Color;
import arc.util.CommandHandler;
import mindustry.entities.effect.Lightning;
import mindustry.entities.type.Player;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.plugin.Plugin;

import mindustry.content.*;
import mindustry.entities.type.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.plugin.Plugin;

import static arc.math.Mathf.*;
import static mindustry.Vars.playerGroup;

public class Main extends Plugin {

    private int uid;
    private float trapx;
    private float trapy;

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
    } */

        public Main(){
            Call.sendMessage("test");
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
                            "\nweaken              Weakens player." +
                            "\ntrap                Traps a player");
                    break;
                case "smite":
                    uid = Integer.parseInt(arg[1]);
                    //if(uid == "") {player.sendMessage("Must be number"); return;}
                    for (int i = 1; i <= 360; i = i + 36) {
                        Lightning.create(Team.crux, Color.white, 100, playerGroup.getByID(uid).x, playerGroup.getByID(uid).y, i, 15);
                    }
                    break;
                case "lightning":
                    player.sendMessage("nothing yet, sorry");
                    break;
                case "weaken":
                    uid = Integer.parseInt(arg[1]);
                    //debuff player
                case "trap":
                    uid = Integer.parseInt(arg[1]);
                    trapx = playerGroup.getByID(uid).x;
                    trapy = playerGroup.getByID(uid).y;

                    atan2(playerGroup.getByID(uid).x - trapx, playerGroup.getByID(uid).y - trapy);
                    //+ sqrt(pow(playerGroup.getByID().getDeltaX(), 2) + pow(playerGroup.getByID().getDeltaY(), 2)) * 2;
                    sqrt(pow(trapx - playerGroup.getByID(uid).x , 2) + pow(trapy - playerGroup.getByID(uid).y, 2));


            }
        });

    }
}